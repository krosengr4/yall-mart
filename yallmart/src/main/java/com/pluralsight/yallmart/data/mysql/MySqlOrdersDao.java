package com.pluralsight.yallmart.data.mysql;

import com.pluralsight.yallmart.data.OrderDao;
import com.pluralsight.yallmart.data.ProductDao;
import com.pluralsight.yallmart.models.CartItem;
import com.pluralsight.yallmart.models.Order;
import com.pluralsight.yallmart.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlOrdersDao extends MySqlDaoBase implements OrderDao {

	private final ProductDao productDao;

	@Autowired
	public MySqlOrdersDao(DataSource dataSource, ProductDao productDao) {
		super(dataSource);
		this.productDao = productDao;
	}

	@Override
	public Order getById(int orderId) {
		String query = """
				SELECT * FROM orders
				WHERE order_id = ?;
				""";
		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, orderId);

			ResultSet result = statement.executeQuery();
			if(result.next()) {
				return mapRow(result);
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public List<CartItem> getItemsOrdered(int userId) {
		List<CartItem> itemsList = new ArrayList<>();
		String query = """
				SELECT * FROM order_line_items
				WHERE user_id = ?;
				""";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, userId);

			ResultSet results = statement.executeQuery();
			while(results.next()) {
				Product product = productDao.getById(results.getInt("product_id"));
				int quantity = results.getInt("quantity");

				CartItem cartItem = new CartItem() {{
					setProduct(product);
					setQuantity(quantity);
				}};
				itemsList.add(cartItem);
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}

		return itemsList;
	}

	@Override
	public Order addOrder(Order order) {
		String query = """
				INSERT INTO orders (user_id, date, address, city, state, zip, shipping_amount)
				VALUES (?, ?, ?, ?, ?, ?, ?);
				""";
		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, order.getUserId());
			statement.setTimestamp(2, Timestamp.valueOf(order.getDateTime()));
			statement.setString(3, order.getAddress());
			statement.setString(4, order.getCity());
			statement.setString(5, order.getState());
			statement.setString(6, order.getZip());

			int rows = statement.executeUpdate();
			if(rows > 0) {
				ResultSet key = statement.getGeneratedKeys();

				if(key.next()) {
					int orderId = key.getInt(1);
					return getById(orderId);
				}
			}

		}  catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public void insertLineItems(CartItem cartItem, int orderId) {
		String query = """
				INSERT INTO order_line_items (order_id, product_id, sales_price, quantity)
				VALUES (?, ?, ?, ?);
				""";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, orderId);
			statement.setInt(2, cartItem.getProductId());
			statement.setBigDecimal(3, cartItem.getLineTotal());
			statement.setInt(4, cartItem.getQuantity());

			statement.executeUpdate();

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private Order mapRow(ResultSet result) throws SQLException{
		int orderId = result.getInt("order_id");
		int userId = result.getInt("user_id");
		LocalDateTime dateTime = result.getTimestamp("date").toLocalDateTime();
		String address = result.getString("address");
		String city = result.getString("city");
		String state = result.getString("state");
		String zip = result.getString("zip");
		BigDecimal shippingAmount = result.getBigDecimal("shipping_amount");

		return new Order(orderId, userId, dateTime, address, city, state, zip, shippingAmount);
	}

}
