package com.manev.quislisting.repository;

import com.manev.quislisting.domain.NavMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaticPageNavMenuRelRepository extends JpaRepository<NavMenuItem, Long> {

}

