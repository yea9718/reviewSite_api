package com.project.gansul.repository;

import com.project.gansul.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostingRepository
        extends JpaRepository<Posting, Long>, QuerydslPredicateExecutor<Posting> {
}
