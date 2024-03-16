package com.assignment.csvDriver.repository;

import com.assignment.csvDriver.model.CSVDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CSVRepository extends JpaRepository<CSVDAO, Long> {
    Optional<CSVDAO> findByCode(String aLong);
}
