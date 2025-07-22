package com.pluralsight.yallmart.data.mysql;

import com.pluralsight.yallmart.data.UserDao;
import com.pluralsight.yallmart.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlUserDao extends MySqlDaoBase implements UserDao {

	@Autowired
	public MySqlUserDao(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public User create(User newUser) {
		String query = "INSERT INTO users (username, hashed_password, role) " +
							   "VALUES (?, ?, ?);";
		String hashedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword());

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, newUser.getUsername());
			statement.setString(2, hashedPassword);
			statement.setString(3, newUser.getRole());

			statement.executeUpdate();

			User user = getByUsername(newUser.getUsername());
			user.setPassword("");

			return user;

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<User> getAll() {
		List<User> userList = new ArrayList<>();
		String query = "SELECT * FROM users;";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);

			ResultSet results = statement.executeQuery();
			while(results.next()) {
				User user = mapRow(results);
				userList.add(user);
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}

		return userList;
	}

	@Override
	public User getUserById(int id) {
		String sql = "SELECT * FROM users WHERE user_id = ?";
		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);

			ResultSet row = statement.executeQuery();

			if(row.next()) {
				User user = mapRow(row);
				return user;
			}
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public User getByUsername(String username) {
		String query = "SELECT * FROM users " +
							   "WHERE username = ?;";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, username);

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
	public int getIdByUsername(String username) {
		User user = getByUsername(username);

		if(user != null) {
			return user.getId();
		}

		return -1;
	}

	@Override
	public boolean exists(String username) {
		User user = getByUsername(username);
		return user != null;
	}

	private User mapRow(ResultSet row) throws SQLException {
		int userId = row.getInt("user_id");
		String username = row.getString("username");
		String hashedPassword = row.getString("hashed_password");
		String role = row.getString("role");

		return new User(userId, username, hashedPassword, role);
	}

}
