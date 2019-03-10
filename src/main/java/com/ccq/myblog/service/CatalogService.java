package com.ccq.myblog.service;

import com.ccq.myblog.domain.Catalog;
import com.ccq.myblog.domain.User;

import java.util.List;

public interface CatalogService {

	Catalog saveCatalog(Catalog catalog);

	void removeCatalog(Long id);

	Catalog getCatalogById(Long id);

	List<Catalog> listCatalogs(User user);
}
