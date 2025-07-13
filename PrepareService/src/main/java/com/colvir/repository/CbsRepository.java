package com.colvir.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CbsRepository {

    private final JdbcOperations jdbcTemplate;

    public String getCbsData(String query) {
        return jdbcTemplate.queryForObject(query, String.class);
    }
}