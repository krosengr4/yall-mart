package com.pluralsight.yallmart.data.mysql;

import com.pluralsight.yallmart.data.ProfileDao;
import com.pluralsight.yallmart.models.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao {

	@Autowired
	public MySqlProfileDao(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public Profile getByUserId(int userId) {
		String query = "SELECT * FROM profiles " +
							   "WHERE user_id = ?;";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, userId);

			ResultSet result = statement.executeQuery();
			if(result.next()) {
				return mapRow(result);
			} else {
				System.out.println("Could not find a profile with that user ID...");
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public void updateProfile(Profile profile) {
		String query = "UPDATE profiles " +
							   "SET first_name = ?, " +
							   "last_name = ?, " +
							   "email = ?, " +
							   "address = ?, " +
							   "city = ?, " +
							   "state = ?, " +
							   "zip = ? " +
							   "WHERE user_id = ?;";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, profile.getFirstName());
			statement.setString(2, profile.getLastName());
			statement.setString(3, profile.getEmail());
			statement.setString(4, profile.getAddress());
			statement.setString(5, profile.getCity());
			statement.setString(6, profile.getState());
			statement.setString(7, profile.getZip());
			statement.setInt(8, profile.getUserId());

			int rows = statement.executeUpdate();
			if(rows > 0)
				System.out.println("Success! The profile was updated!");
			else
				System.err.println("ERROR! Could not update the profile!!!");

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Profile create(Profile profile) {
		String query = "INSERT INTO profiles (user_id, first_name, last_name, email, address, city, state, zip) " +
							   "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, profile.getUserId());
			statement.setString(2, profile.getFirstName());
			statement.setString(3, profile.getLastName());
			statement.setString(4, profile.getEmail());
			statement.setString(5, profile.getAddress());
			statement.setString(6, profile.getCity());
			statement.setString(7, profile.getState());
			statement.setString(8, profile.getZip());

			int rows = statement.executeUpdate();
			if(rows > 0) {
				ResultSet key = statement.getGeneratedKeys();

				if(key.next()) {
					int userId = key.getInt(1);
					return getByUserId(userId);
				}
			} else {
				System.err.println("ERROR! The profile was not created!!!");
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}

		return null;
	}

	//Maps a result row from MySQL to a Profile object and returns it.
	private Profile mapRow(ResultSet result) throws SQLException {
		int userId = result.getInt("user_id");
		String firstName = result.getString("first_name");
		String lastName = result.getString("last_name");
		String email = result.getString("email");
		String address = result.getString("address");
		String city = result.getString("city");
		String state = result.getString("state");
		String zip = result.getString("zip");

		return new Profile(userId, firstName, lastName, email, address, city, state, zip);
	}

}
