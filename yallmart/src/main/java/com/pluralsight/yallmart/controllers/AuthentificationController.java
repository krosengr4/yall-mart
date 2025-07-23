package com.pluralsight.yallmart.controllers;

import com.pluralsight.yallmart.data.ProfileDao;
import com.pluralsight.yallmart.data.UserDao;
import com.pluralsight.yallmart.models.Profile;
import com.pluralsight.yallmart.models.User;
import com.pluralsight.yallmart.models.authentification.LoginDto;
import com.pluralsight.yallmart.models.authentification.LoginResponseDto;
import com.pluralsight.yallmart.models.authentification.RegisterUserDto;
import com.pluralsight.yallmart.security.jwt.JWTFilter;
import com.pluralsight.yallmart.security.jwt.TokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@CrossOrigin
@PreAuthorize("permitAll()")
public class AuthentificationController {

	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final UserDao userDao;
	private final ProfileDao profileDao;

	public AuthentificationController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserDao userDao, ProfileDao profileDao) {
		this.tokenProvider = tokenProvider;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.userDao = userDao;
		this.profileDao = profileDao;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) {

		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.createToken(authentication, false);

		try {
			User user = userDao.getByUsername(loginDto.getUsername());

			if(user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
			return new ResponseEntity<>(new LoginResponseDto(jwt, user), httpHeaders, HttpStatus.OK);
		} catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
		}
	}

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<User> register(@Valid @RequestBody RegisterUserDto newUser) {

		try {
			boolean exists = userDao.exists(newUser.getUsername());
			if(exists) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Already Exists.");
			}

			// create user
			User user = userDao.create(new User(0, newUser.getUsername(), newUser.getPassword(), newUser.getRole()));

			// create profile
			Profile profile = new Profile();
			profile.setUserId(user.getId());
			profileDao.create(profile);

			return new ResponseEntity<>(user, HttpStatus.CREATED);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
		}
	}

}
