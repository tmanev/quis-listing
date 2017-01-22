package com.manev.quislisting.repository;

import com.manev.quislisting.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Long> {
}
