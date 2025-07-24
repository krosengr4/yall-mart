package com.pluralsight.yallmart.data.mysql;

import com.pluralsight.yallmart.data.CartDao;
import com.pluralsight.yallmart.data.ProductDao;
import com.pluralsight.yallmart.models.Cart;
import com.pluralsight.yallmart.models.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
		return null;
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
				Product product = productDao.get
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
