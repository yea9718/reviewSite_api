package com.project.gansul.repository;

import com.project.gansul.entity.Liked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedRepository
        extends JpaRepository<Liked, Long>, QuerydslPredicateExecutor<Liked> {
}
