package com.vladooha.epilepsycenterserviceappbackend.facade.report;

import com.vladooha.epilepsycenterserviceappbackend.model.report.Report;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.*;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.service.report.ReportService;
import com.vladooha.epilepsycenterserviceappbackend.service.report.food.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
public class ReportFacade {
    private static final String REPORT_WRONG_DATA_ERROR = "В отчёте указаны некорректные данные!\n" +
            "Попробуйте пересоздать отчёт";
    private static final String EXPIRED_MENU_PARAMETERS = "Отчёт составлен на основе старых рекоммендаций врача\n" +
            "Попробуйте пересоздать отчёт";

    private final ReportService reportService;
    private final MenuService menuService;
    private final MenuParametersService menuParametersService;
    private final DishService dishService;
    private final DishParameterService dishParameterService;
    private final ProductService productService;

    public ReportFacade(ReportService reportService, MenuService menuService, MenuParametersService menuParametersService, DishService dishService, DishParameterService dishParameterService, ProductService productService) {
        this.reportService = reportService;
        this.menuService = menuService;
        this.menuParametersService = menuParametersService;
        this.dishService = dishService;
        this.dishParameterService = dishParameterService;
        this.productService = productService;
    }

    @Transactional(rollbackOn = DataIntegrityViolationException.class)
    public Report postReport(Report report, User owner) throws DataIntegrityViolationException {
        report.setId(null);
        report.setOwner(owner);
        report.setMenuParameters(getMenuParametersIfActual(report, owner));

        Menu menu = report.getMenu();
        if (menu != null) {
            menu.setDate(report.getDate());
        }
        Menu mergedMenu = mergeMenu(menu, owner);
        report.setMenu(mergedMenu);

        Report savedReport = reportService.save(report);
        if (savedReport != null) {
            mergedMenu.setReport(savedReport);
            menuService.save(mergedMenu);

            return savedReport;
        }

        return null;
    }

    @Transactional(rollbackOn = DataIntegrityViolationException.class)
    public MenuParameters getMenuParametersIfActual(Report report, User owner)
            throws DataIntegrityViolationException {
        MenuParameters menuParameters = report.getMenuParameters();
        if (null == menuParameters) {
            throw new DataIntegrityViolationException(EXPIRED_MENU_PARAMETERS);
        }

        MenuParameters actualMenuParameters = menuParametersService.findActualForPatient(owner);
        if (actualMenuParameters.getId() != menuParameters.getId()) {
            throw new DataIntegrityViolationException(EXPIRED_MENU_PARAMETERS);
        }

        return actualMenuParameters;
    }

    @Transactional(rollbackOn = DataIntegrityViolationException.class)
    public Menu mergeMenu(Menu menu, User owner) throws DataIntegrityViolationException {
        if (null == menu) {
            throw new DataIntegrityViolationException(REPORT_WRONG_DATA_ERROR);
        }

        menu.setOwner(owner);

        List<DishParameters> mergedDishParametersList =
                mergeDishParameters(menu.getDishParametersList(), owner);
        menu.setDishParametersList(mergedDishParametersList);

        Menu mergedMenu = menuService.merge(menu);
        if (null == mergedMenu) {
            throw new DataIntegrityViolationException(REPORT_WRONG_DATA_ERROR);
        } else {
            for (DishParameters dishParameters : mergedDishParametersList) {
                dishParameters.setMenu(menu);
            }
        }

        return mergedMenu;
    }

    @Transactional(rollbackOn = DataIntegrityViolationException.class)
    public List<DishParameters> mergeDishParameters(List<DishParameters> dishParametersList, User owner)
            throws DataIntegrityViolationException {
        if (null == dishParametersList) {
            return new ArrayList<>();
        }

        List<DishParameters> mergedDishParametersList = new ArrayList<>();
        if (dishParametersList != null) {
            for (DishParameters dishParameters : dishParametersList) {
                Map<Product, Double> productWeightMap = dishParameters.getProductWeightMap();
                if (productWeightMap != null) {
                    Map<Product, Double> mergedProductWeightMap = new HashMap<>();
                    for (var entry : productWeightMap.entrySet()) {
                        Product mergedProduct = mergeProduct(entry.getKey(), owner);
                        Double weight = entry.getValue() != null ? entry.getValue() : 100.0;

                        mergedProductWeightMap.put(mergedProduct, weight);
                    }
                    dishParameters.setProductWeightMap(mergedProductWeightMap);

                    Dish dish = dishParameters.getDish();
                    if (dish != null) {
                        List<Product> mergedProducts = new ArrayList<>(productWeightMap.keySet());
                        dish.setProductList(mergedProducts);
                        Dish mergedDish = mergeDish(dish, owner);

                        dishParameters.setDish(mergedDish);
                    }
                }

                DishParameters mergedDishParameters = dishParameterService.merge(dishParameters);
                if (null == mergedDishParameters) {
                    throw new DataIntegrityViolationException(REPORT_WRONG_DATA_ERROR);
                }
                mergedDishParametersList.add(mergedDishParameters);
            }
        }

        for (DishParameters dishParameters : mergedDishParametersList) {
            Dish dish = dishParameters.getDish();
            if (dish != null) {
                dish.setLastDishParameters(dishParameters);
                dishService.save(dish);
            }
        }

        return mergedDishParametersList;
    }

    @Transactional(rollbackOn = DataIntegrityViolationException.class)
    public Product mergeProduct(Product product, User owner)
            throws DataIntegrityViolationException {
        product.setOwner(owner);
        Product mergedProduct = productService.merge(product);

        if (null == mergedProduct) {
            throw new DataIntegrityViolationException(REPORT_WRONG_DATA_ERROR);
        }

        return mergedProduct;
    }

    @Transactional(rollbackOn = DataIntegrityViolationException.class)
    public Dish mergeDish(Dish dish, User owner) {
        dish.setOwner(owner);
        dish = dishService.merge(dish);

        if (null == dish) {
            throw new DataIntegrityViolationException(REPORT_WRONG_DATA_ERROR);
        }

        return dish;
    }
}
