package com.pluralsight.yallmart.controllers;

import com.pluralsight.yallmart.data.CartDao;
import com.pluralsight.yallmart.data.UserDao;
import com.pluralsight.yallmart.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
