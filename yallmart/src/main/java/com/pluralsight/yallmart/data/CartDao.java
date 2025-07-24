package com.pluralsight.yallmart.data;

import com.pluralsight.yallmart.models.Cart;
import com.pluralsight.yallmart.models.CartItem;

import java.util.List;

public interface CartDao {

	Cart getByUserId(int userId);

	List<CartItem> getItemsInCart(int userId, int productId);

	Cart addToCart(int userId, int productId);

	void updateCart(int userId, int productId, CartItem cartItem);

	void clearCart(int userId);

}
