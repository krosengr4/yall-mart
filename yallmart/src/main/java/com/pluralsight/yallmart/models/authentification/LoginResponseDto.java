package com.pluralsight.yallmart.models.authentification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pluralsight.yallmart.models.User;

public class LoginResponseDto {
		/*
	DTO = "data transfer object". This class is created to transfer data between the server and client side.
	LoginDTO is for the client passing data tp the server for a login endpoint, or when someone tries to log in.
	LoginResponseDTO is the data that is passed back to the client from the server at a login endpoint.
	 */

	private String token;
	private User user;

	public LoginResponseDto(String token, User user) {
		this.token = token;
		this.user = user;
	}

	//region Getters and Setters
	@JsonProperty("token")
	String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@JsonProperty("user")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	//endregion
}
