package com.vladooha.epilepsycenterserviceappbackend.repo.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Dish;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Eating;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findAllByOwner(User owner);

    @Query("SELECT dish " +
            "FROM Dish dish " +
            "WHERE dish.owner = ?1 " +
            "AND dish.lastDishParameters.eating = ?2 " +
            "AND dish.name LIKE %?3%")
    List<Dish> findAllByOwnerAndEatingAndNameContains(User owner, Eating eating, String nameQuery);
}
