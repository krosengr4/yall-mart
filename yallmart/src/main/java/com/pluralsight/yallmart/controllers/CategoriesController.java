package com.pluralsight.yallmart.controllers;

import com.pluralsight.yallmart.data.CategoryDao;
import com.pluralsight.yallmart.data.ProductDao;
import com.pluralsight.yallmart.models.Category;
import com.pluralsight.yallmart.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

	@GetMapping("")
	@PreAuthorize("permitAll()")
	public List<Category> getAll() {
		try {
			return categoryDao.getAll();
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
		}
	}

	@GetMapping("/{categoryId}")
	@PreAuthorize("permitAll()")
	public Category getById(@PathVariable int categoryId) {
		try {
			return categoryDao.getById(categoryId);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
		}
	}

	@GetMapping("/{categoryId}/products")
	@PreAuthorize("permitAll()")
	public List<Product> getProductsById(@PathVariable int categoryId) {
		try {
			return productDao.listByCategoryId(categoryId);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
		}
	}

	@PostMapping("")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Category addCategory(@RequestBody Category category) {
		try {
			return categoryDao.create(category);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
		}
	}

	@PutMapping("/{categoryId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void updateCategory(@PathVariable int categoryId, @RequestBody Category category) {
		try  {
			categoryDao.update(categoryId, category);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
		}
	}

	@DeleteMapping("/{categoryId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteCategory(@PathVariable int categoryId) {
		try {
			categoryDao.delete(categoryId);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
		}
	}

}
