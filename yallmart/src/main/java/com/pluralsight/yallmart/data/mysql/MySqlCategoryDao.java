package com.pluralsight.yallmart.data.mysql;

import com.pluralsight.yallmart.data.CategoryDao;
import com.pluralsight.yallmart.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {

    @Autowired
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAll() {
        List<Category> categoryList = new ArrayList<>();
        String query = "SELECT * FROM categories;";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet results = statement.executeQuery();
            while (results.next()) {
                categoryList.add(mapRow(results));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return categoryList;
    }

    @Override
    public Category getById(int categoryId) {
        String query = """
                SELECT * FROM categories 
                WHERE category_id = ?;
                """;

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, categoryId);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return mapRow(result);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Category create(Category category) {
        String query = """
                INSERT INTO categories (name, description) 
                VALUES (?, ?);
                """;

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            int rows = statement.executeUpdate();
            if (rows > 0) {
                ResultSet key = statement.getGeneratedKeys();

                if (key.next()) {
                    int categoryId = key.getInt(1);
                    return getById(categoryId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void update(int categoryId, Category category) {
        String query = """
                UPDATE categories 
                SET name = ?, 
                description = ?
                WHERE category_id = ?;
                """;

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            int rows = statement.executeUpdate();
            if (rows > 0)
                System.out.println("Success! Category was updated!");
            else
                System.err.println("ERROR! Could not update category!!!");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId) {
        String query = """
                DELETE FROM categories 
                WHERE category_id = ?;
                """;

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, categoryId);

            int rows = statement.executeUpdate();
            if (rows > 0)
                System.out.println("Success! Category was deleted!");
            else
                System.err.println("ERROR! Could not delete category!!!");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Category mapRow(ResultSet result) throws SQLException {
        int categoryId = result.getInt("category_id");
        String name = result.getString("name");
        String description = result.getString("description");

        return new Category(categoryId, name, description);
    }


}
