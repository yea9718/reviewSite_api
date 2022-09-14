package com.project.gansul.repository;

import com.project.gansul.entity.AlcoholDown;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AlcoholDownRepository
        extends JpaRepository<AlcoholDown, Long>, QuerydslPredicateExecutor<AlcoholDown> {
}