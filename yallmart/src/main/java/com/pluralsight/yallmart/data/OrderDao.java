package com.pluralsight.yallmart.data;

import com.pluralsight.yallmart.models.CartItem;
import com.pluralsight.yallmart.models.Order;

import java.util.List;

public interface OrderDao {

	Order getById(int orderId);

	List<CartItem> getItemsOrdered(int userId);

	void insertLineItems(CartItem cartItem, int orderId);

	Order addOrder(Order order);
}
