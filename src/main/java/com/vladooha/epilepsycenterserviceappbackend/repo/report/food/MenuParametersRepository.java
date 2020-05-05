package com.vladooha.epilepsycenterserviceappbackend.repo.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.MenuParameters;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuParametersRepository extends JpaRepository<MenuParameters, Long> {
    MenuParameters findFirstByPatientOrderByDateDesc(User patient);
}
