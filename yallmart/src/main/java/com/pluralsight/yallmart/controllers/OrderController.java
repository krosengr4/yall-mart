package com.pluralsight.yallmart.controllers;

import com.pluralsight.yallmart.data.*;
import com.pluralsight.yallmart.models.CartItem;
import com.pluralsight.yallmart.models.Order;
import com.pluralsight.yallmart.models.Profile;
import com.pluralsight.yallmart.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

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

	@GetMapping("")
	public List<CartItem> getItemsOrdered(Principal principal) {
		try {
			// Get info of the logged-in user
			String userName = principal.getName();
			User user = userDao.getByUsername(userName);
			int userId = user.getId();

			return orderDao.getItemsOrdered(userId);
		} catch(Exception e) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Uh oh... something went wrong");
			throw new RuntimeException(e);
		}
	}

	@PostMapping("")
	public void addOrder(Principal principal) {
		try {
			// Get info of the logged-in user
			String userName = principal.getName();
			User user = userDao.getByUsername(userName);
			int userId = user.getId();

			Profile profile = profileDao.getByUserId(userId);

			List<CartItem> cartItems = cartDao.getItemsInCart(userId);

			if(!cartItems.isEmpty()) {
				Order order = new Order(0, userId, LocalDateTime.now(), profile.getAddress(), profile.getCity(),
						profile.getState(), profile.getZip(), BigDecimal.ZERO);

				Order addedOrder = orderDao.addOrder(order);

				for(CartItem item : cartItems) {
					orderDao.insertLineItems(item, addedOrder.getOrderId());
				}

				cartDao.clearCart(userId);
			}
		} catch(Exception e) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Uh oh... something went wrong");
			throw new RuntimeException(e);
		}
	}

}
