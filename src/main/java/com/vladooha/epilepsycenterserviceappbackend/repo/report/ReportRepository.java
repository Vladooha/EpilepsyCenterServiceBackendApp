package com.vladooha.epilepsycenterserviceappbackend.repo.report;

import com.vladooha.epilepsycenterserviceappbackend.model.report.Report;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Report findByOwnerAndDate(User owner, LocalDate date);

    @Query(value = "SELECT report.date FROM Report report WHERE report.owner = ?1")
    List<LocalDate> findAllDatesByOwner(User owner);
}
