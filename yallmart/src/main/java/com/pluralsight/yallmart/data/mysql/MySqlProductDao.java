package com.pluralsight.yallmart.data.mysql;

import com.pluralsight.yallmart.data.ProductDao;
import com.pluralsight.yallmart.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlProductDao extends MySqlDaoBase implements ProductDao {

    @Autowired
    public MySqlProductDao(DataSource dataSource) {
        super(dataSource);
    }

    public List<Product> getAll () {
        List<Product> allProducts = new ArrayList<>();
        String query = """
                SELECT * FROM products;
                """;
        try (Connection connection = getConnection()) {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
            allProducts.add(rsHelper(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error getting products");
        }
        return allProducts;
    }

    public Product getById (int productId) {
        Product product = null;
        String query = """
                SELECT * FROM products
                WHERE product_id = ?
                """;
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                product = rsHelper(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return product;
    }

    Product rsHelper (ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("product_id");
        String name = resultSet.getString("name");
        BigDecimal price = resultSet.getBigDecimal("price");
        int categoryId = resultSet.getInt("category_id");
        String desc = resultSet.getString("description");
        int stock = resultSet.getInt("stock");
        return new Product(id, name, price, categoryId, desc, stock);
    }
}
