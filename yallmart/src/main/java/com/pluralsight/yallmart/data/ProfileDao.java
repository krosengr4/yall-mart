package com.pluralsight.yallmart.data;

import com.pluralsight.yallmart.models.Profile;

public interface ProfileDao {

	Profile getByUserId(int userId);

	void updateProfile(Profile profile);

	Profile create(Profile profile);

}
