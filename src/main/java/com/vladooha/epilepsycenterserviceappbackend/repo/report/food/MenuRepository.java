package com.vladooha.epilepsycenterserviceappbackend.repo.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Menu;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.MenuParameters;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    Menu findFirstByOwnerOrderByDateDesc(User patient);
}
