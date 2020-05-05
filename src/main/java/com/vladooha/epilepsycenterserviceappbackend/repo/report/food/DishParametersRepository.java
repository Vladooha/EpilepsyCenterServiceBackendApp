package com.vladooha.epilepsycenterserviceappbackend.repo.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Dish;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.DishParameters;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishParametersRepository extends JpaRepository<DishParameters, Long> {
    @Query("SELECT dishParameters " +
            "FROM DishParameters dishParameters " +
            "WHERE dishParameters.dish = ?1" +
            "AND dishParameters.menu.date = (" +
            "   SELECT MAX(dishParameters.menu.date) " +
            "   FROM DishParameters dishParameters" +
            "   WHERE dishParameters.dish = ?1)")
    DishParameters findByDishMaxDate(Dish dish);

    @Query("SELECT dishParameters " +
            "FROM DishParameters dishParameters " +
            "WHERE dishParameters.dish IN ?1 " +
            "ORDER BY dishParameters.menu.date DESC ")
    List<DishParameters> findByDishOrderByDateDescTop1(Dish dish, Pageable pageable);
}
