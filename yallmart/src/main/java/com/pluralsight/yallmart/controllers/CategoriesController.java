package com.pluralsight.yallmart.controllers;

import com.pluralsight.yallmart.data.CategoryDao;
import com.pluralsight.yallmart.data.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categories")
@CrossOrigin
public class CategoriesController {

	private final CategoryDao categoryDao;
	private final ProductDao productDao;

	@Autowired
	public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
		this.categoryDao = categoryDao;
		this.productDao = productDao;
	}

}
