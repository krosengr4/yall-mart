package com.pluralsight.yallmart.security;

import com.pluralsight.yallmart.data.UserDao;
import com.pluralsight.yallmart.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserModelDetailsService implements UserDetailsService {

	private final Logger log = LoggerFactory.getLogger(UserModelDetailsService.class);

	private final UserDao userDao;

	public UserModelDetailsService(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public UserDetails loadUserByUsername(final String login) {
		log.debug("Authentication user '{}'", login);
		String lowerCaseLogin = login.toLowerCase();
		return createSpringSecurityUser(lowerCaseLogin, userDao.getByUsername(lowerCaseLogin));
	}

	private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowerCaseLogin, User user) {
		if(!user.isActivated()) {
			throw new UserNotActivatedException("User " + lowerCaseLogin + " was not activated!");
		}
		List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
															.map(authority -> new SimpleGrantedAuthority(authority.getName()))
															.collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
	}

}
