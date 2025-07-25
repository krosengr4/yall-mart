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

    //region Get Request Methods
    @Override
    public List<Product> getAll () {
        List<Product> allProducts = new ArrayList<>();
        String query = """
                SELECT * FROM products;
                """;
        try (Connection connection = getConnection()) {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
            allProducts.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error getting products");
        }
        return allProducts;
    }
    @Override
    public Product getById (int productId) {
        Product product = null;
        String query = """
                SELECT * FROM products
                WHERE product_id = ?
                """;
        try (Connection connection = getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                product = mapRow(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return product;
    }
    @Override
    public List<Product> search (Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> results = new ArrayList<>();
        String query = """
                SELECT * FROM products
                WHERE (category_id = ? OR ? =-1)
                AND (price >= ? OR ? = -1)
                AND (price <= ? OR ? = -1);
                """;
        categoryId = categoryId == null
                ? -1
                : categoryId;
        minPrice = minPrice == null
                ? new BigDecimal("-1")
                : minPrice;
        maxPrice = maxPrice == null
                ? new BigDecimal("-1")
                : maxPrice;

        try (Connection connection = getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, categoryId);
            stmt.setInt(2, categoryId);
            stmt.setBigDecimal(3, minPrice);
            stmt.setBigDecimal(4, minPrice);
            stmt.setBigDecimal(5, maxPrice);
            stmt.setBigDecimal(6, maxPrice);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return results;
    }
    @Override
    public List<Product> listByCategoryId (Integer categoryId) {
        List<Product> results = new ArrayList<>();
        String query = """
                SELECT * FROM products
                WHERE category_id = ?;
                """;
        try (Connection connection = getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }
    //endregion

    //region CREATE, UPDATE, DELETE Methods
    @Override
    public Product create (Product product) {
        String query = """
                INSERT INTO products (name, price, category_id, description, stock)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection connection = getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, product.getName());
            stmt.setBigDecimal(2, product.getPrice());
            stmt.setInt(3, product.getCategoryId());
            stmt.setString(4, product.getDescription());
            stmt.setInt(5, product.getStock());

            /// If Adding Product Was Successful, will return product using getByID method with the new ID ///
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);
                    return getById(orderId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update (int productId, Product product) {
        String query = """
                UPDATE products
                SET name = ?,
                price = ?,
                category_id = ?,
                description = ?,
                stock = ?
                WHERE product_id = ?;
                """;
        try (Connection connection = getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, product.getName());
            stmt.setBigDecimal(2, product.getPrice());
            stmt.setInt(3, product.getCategoryId());
            stmt.setString(4, product.getDescription());
            stmt.setInt(5, product.getStock());
            stmt.setInt(6, productId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete (int productId) {
        String query = """
                DELETE FROM products
                WHERE product_id = ?;
                """;
        try (Connection connection = getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, productId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //endregion

    protected Product mapRow(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("product_id");
        String name = resultSet.getString("name");
        BigDecimal price = resultSet.getBigDecimal("price");
        int categoryId = resultSet.getInt("category_id");
        String desc = resultSet.getString("description");
        int stock = resultSet.getInt("stock");
        return new Product(id, name, price, categoryId, desc, stock);
    }
}
