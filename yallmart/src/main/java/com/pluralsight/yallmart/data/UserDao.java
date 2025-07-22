package com.pluralsight.yallmart.data;

import com.pluralsight.yallmart.models.User;

import java.util.List;

public interface UserDao {

	List<User> getAll();

	User getUserById(int userId);

	User getByUsername(String userName);

	int getIdByUsername(String userName);

	User create(User user);

	boolean exists(String userName);
}
