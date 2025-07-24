package com.pluralsight.yallmart.data.mysql;

import com.pluralsight.yallmart.data.CartDao;
import com.pluralsight.yallmart.data.ProductDao;
import com.pluralsight.yallmart.models.Cart;
import com.pluralsight.yallmart.models.CartItem;
import com.pluralsight.yallmart.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MySqlCartDao extends MySqlDaoBase implements CartDao {

	ProductDao productDao;

	@Autowired
	public MySqlCartDao(DataSource dataSource, ProductDao productDao) {
		super(dataSource);
		this.productDao = productDao;
	}

	@Override
	public Cart getByUserId(int userId) {
		List<CartItem> cartItemsList = getItemsInCart(userId);
		Map<Integer, CartItem> cartItemMap = new HashMap<>();
		Cart cart = new Cart();

		for(CartItem item : cartItemsList) {
			cartItemMap.put(item.getProductId(), item);
		}

		cart.setItems(cartItemMap);
		return cart;
	}

	@Override
	public List<CartItem> getItemsInCart(int userId) {
		List<CartItem> cartItemsList = new ArrayList<>();
		String query  = """
				SELECT * FROM shopping_cart
				WHERE user_id = ?;
				""";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, userId);

			ResultSet results = statement.executeQuery();
			while(results.next()) {
				int productId = results.getInt("product_id");
				int quantity = results.getInt("quantity");
				Product product = productDao.getById(productId);

				CartItem cartItem = new CartItem();
				cartItem.setProduct(product);
				cartItem.setQuantity(quantity);

				cartItemsList.add(cartItem);
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return cartItemsList;
	}

	@Override
	public Cart addToCart(int userId, int productId) {
		String query = "";
		int quantity = getQuantityInCart(userId, productId);

		if(quantity > 0) {
			query = """
					UPDATE shopping_cart SET quantity = ?
					WHERE user_id = ? AND product_id = ?;
					""";
		} else {
			query = """
					INSERT INTO shopping_cart (quantity, order_id, product_id)
					VALUES (?, ?, ?);
					""";
		}
		quantity += 1;

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, quantity);
			statement.setInt(2, userId);
			statement.setInt(3, productId);

			int rows = statement.executeUpdate();
			if(rows > 0)
				System.out.println("Success! Product was added to cart!");
			else
				System.err.println("ERROR! Could not add the product to cart!!!");

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}

		return getByUserId(userId);
	}

	@Override
	public void updateCart(int userId, int productId, CartItem cartItem) {
		String query = """
				UPDATE shopping_cart
				SET quantity = ?
				WHERE user_id = ? AND product_id = ?;
				""";
		int quantity = cartItem.getQuantity();

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, quantity);
			statement.setInt(2, userId);
			statement.setInt(3, productId);

			int rows = statement.executeUpdate();
			if(rows > 0)
				System.out.println("Success! The cart was updated!");
			else
				System.err.println("ERROR! Could not update the cart!!!");

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void clearCart(int userId) {
		String query = """
				DELETE FROM shopping_cart
				WHERE user_id = ?;
				""";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, userId);

			int rows = statement.executeUpdate();
			if(rows > 0)
				System.out.println("Success! The cart was cleared!");
			else
				System.err.println("ERROR! Could not clear the cart!!!");

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private int getQuantityInCart(int userId, int productId) {
		String query = """
				SELECT * FROM shopping_cart 
				WHERE user_id = ? 
				AND product_id = ?;
				""";
		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, userId);
			statement.setInt(2, productId);

			ResultSet results = statement.executeQuery();
			if(results.next()) {
				return results.getInt("quantity");
			} else {
				return 0;
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
