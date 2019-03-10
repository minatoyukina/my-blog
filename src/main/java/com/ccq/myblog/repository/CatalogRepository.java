package com.ccq.myblog.repository;

import java.util.List;

import com.ccq.myblog.domain.Catalog;
import com.ccq.myblog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogRepository extends JpaRepository<Catalog, Long>{

	List<Catalog> findByUser(User user);

	List<Catalog> findByUserAndName(User user, String name);
}
