package com.manev.quislisting.repository.taxonomy;

import com.manev.quislisting.domain.taxonomy.Term;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Long> {
}
