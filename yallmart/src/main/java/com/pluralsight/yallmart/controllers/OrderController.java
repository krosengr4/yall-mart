package com.pluralsight.yallmart.controllers;

import com.pluralsight.yallmart.data.*;
import com.pluralsight.yallmart.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
@CrossOrigin
public class OrderController {

	private final OrderDao orderDao;
	private final CartDao cartDao;
	private final UserDao userDao;
	private final ProfileDao profileDao;

	@Autowired
	public OrderController(OrderDao orderDao, CartDao cartDao, UserDao userDao, ProfileDao profileDao) {
		this.orderDao = orderDao;
		this.cartDao = cartDao;
		this.userDao = userDao;
		this.profileDao = profileDao;
	}

}
