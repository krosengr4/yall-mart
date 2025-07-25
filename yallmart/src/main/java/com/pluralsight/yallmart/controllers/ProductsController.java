package com.pluralsight.yallmart.controllers;

import com.pluralsight.yallmart.data.CategoryDao;
import com.pluralsight.yallmart.data.ProductDao;
import com.pluralsight.yallmart.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("products")
public class ProductsController {

	private final ProductDao productDao;
	@Autowired
	public ProductsController(ProductDao productDao, CategoryDao categoryDao) {
		this.productDao = productDao;
	}

	//THIS METHOD WAS USED FOR TESTING GET ALL METHOD
//	@GetMapping("")
//	public List<Product> getAllProducts() {
//		try {
//			return productDao.getAll();
//		} catch(Exception e) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Uh oh... something went wrong.");
//		}
//	}

	@GetMapping("")
	@PreAuthorize("permitAll()")
	public List<Product> search(@RequestParam(name = "cat", required = false) Integer categoryId,
								@RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
								@RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice) {
		try {
			return productDao.search(categoryId, minPrice, maxPrice);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Uh oh... something went wrong.");
			// Used for debugging. Do not want to show the stack.
//			throw new RuntimeException(e);
		}
	}

	@GetMapping("/{productId}")
	@PreAuthorize("permitAll()")
	public Product getById(@PathVariable int productId) {
		try {
			return productDao.getById(productId);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Uh oh... something went wrong.");
		}
	}


	@PostMapping("")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Product addProduct(@RequestBody Product product) {
		try {
			return productDao.create(product);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Uh oh... something went wrong.");
			// Used for debugging. Do not want to show the stack.
//		throw new RuntimeException(e);
		}
	}

	@PutMapping("/{productId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void updateProduct(@PathVariable int productId, @RequestBody Product product) {
		try {
			productDao.update(productId, product);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Uh oh... something went wrong.");
		}
	}

	@DeleteMapping("/{productId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteProduct(@PathVariable int productId) {
		try {
			productDao.delete(productId);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Uh oh... something went wrong.");
		}
	}

}
