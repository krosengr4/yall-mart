package com.pluralsight.yallmart.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pluralsight.yallmart.models.authentification.Authority;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {

	private int id;
	private String username;
	@JsonIgnore
	private String password;
	@JsonIgnore
	private boolean isActivated;
	private Set<Authority> authorities = new HashSet<>();

	public User() {
		this.isActivated = true;
	}

	public User(int id, String userName, String password, String authorities) {
		this.id = id;
		this.username = userName;
		this.password = password;
		if(authorities != null)
			this.setAuthorities(authorities);

		this.isActivated = true;
	}

	//region Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActivated() {
		return isActivated;
	}

	public void setActivated(boolean activated) {
		isActivated = activated;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public void setAuthorities(String authorities) {
		String[] roles = authorities.split(",");
		for(String role : roles) {
			addRole(role);
		}
	}
	//endregion

	public void addRole(String role) {
		String authority = role.contains("ROLE_") ? role : "ROLE_" + role;
		this.authorities.add(new Authority(authority));
	}

	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;

		User user = (User) o;
		return id == user.id &&
					   isActivated == user.isActivated &&
					   Objects.equals(username, user.username) &&
					   Objects.equals(password, user.password) &&
					   Objects.equals(authorities, user.authorities);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, username, password, isActivated, authorities);
	}

	@Override
	public String toString() {
		return "User{" +
					   "id=" + id +
					   ", username='" + username + '\'' +
					   ", activated=" + isActivated +
					   ", authorities=" + authorities +
					   '}';
	}

	@JsonIgnore
	public String getRole() {
		if(!authorities.isEmpty()) {
			for(Authority role : authorities) {
				return role.getName().toUpperCase();
			}
		}
		return "ROLE_ADMIN";
	}
}
