package com.pluralsight.yallmart.controllers;

import com.pluralsight.yallmart.data.ProfileDao;
import com.pluralsight.yallmart.data.UserDao;
import com.pluralsight.yallmart.models.Profile;
import com.pluralsight.yallmart.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("profile")
public class ProfileController {

	private final ProfileDao profileDao;
	private final UserDao userDao;

	@Autowired
	public ProfileController(ProfileDao profileDao, UserDao userDao) {
		this.profileDao = profileDao;
		this.userDao = userDao;
	}

	//GET URL = localhost:8080/profile
	@GetMapping("")
	public Profile getByUserId(Principal principal) {
		try {
			//get the info of the user that is logged in
			String userName = principal.getName();
			User user = userDao.getByUsername(userName);
			int userId = user.getId();

			//Use ProfileDao to get & return logged-in users profile
			return profileDao.getByUserId(userId);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad :(");
		}
	}

	@PutMapping("")
	public void updateProfile(@RequestBody Profile profile, Principal principal) {
		try {
			//get the info of the user that is logged in
			String userName = principal.getName();
			User user = userDao.getByUsername(userName);
			int userId = user.getId();

			profile.setUserId(userId);
			profileDao.updateProfile(profile);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad :(");
		}
	}

}
