package com.pluralsight.yallmart.controllers;

import com.pluralsight.yallmart.data.CartDao;
import com.pluralsight.yallmart.data.UserDao;
import com.pluralsight.yallmart.models.Cart;
import com.pluralsight.yallmart.models.CartItem;
import com.pluralsight.yallmart.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("cart")
@CrossOrigin
public class CartController {

	private final CartDao cartDao;
	private final UserDao userDao;

	@Autowired
	public CartController(CartDao cartDao, UserDao userDao) {
		this.cartDao = cartDao;
		this.userDao = userDao;
	}

	@GetMapping("")
	public Cart getCart(Principal principal) {
		try {
			// Get info of the logged-in user
			String userName = principal.getName();
			User user = userDao.getByUsername(userName);
			int userId = user.getId();

			return cartDao.getByUserId(userId);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Uh oh... something went wrong");
		}
	}

	@PostMapping("/products/{productId}")
	public Cart addProductToCart(@PathVariable int productId, Principal principal) {
		try {
			// Get info of the logged-in user
			String userName = principal.getName();
			User user = userDao.getByUsername(userName);
			int userId = user.getId();

			return cartDao.addToCart(userId, productId);
		} catch(Exception e) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Uh oh... something went wrong");
			throw new RuntimeException(e);
		}
	}

	@PutMapping("/products/{productId}")
	public void updateCart(@PathVariable int productId, @RequestBody CartItem cartItem, Principal principal) {
		try {
			// Get info of the logged-in user
			String userName = principal.getName();
			User user = userDao.getByUsername(userName);
			int userId = user.getId();

			cartDao.updateCart(userId, productId, cartItem);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Uh oh... something went wrong");
		}
	}

	@DeleteMapping("")
	public Cart clearCart(Principal principal) {
		try {
			// Get info of the logged-in user
			String userName = principal.getName();
			User user = userDao.getByUsername(userName);
			int userId = user.getId();

			cartDao.clearCart(userId);

			return cartDao.getByUserId(userId);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Uh oh... something went wrong");
		}
	}
}
