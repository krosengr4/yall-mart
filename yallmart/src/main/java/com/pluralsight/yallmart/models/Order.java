package com.pluralsight.yallmart.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {

	private int orderId;
	private int userId;
	LocalDateTime dateTime;
	private String address;
	private String city;
	private String state;
	private String zip;
	private BigDecimal shippingAmount;

	public Order() {}

	public Order(int orderId, int userId, LocalDateTime dateTime, String address, String city, String state, String zip, BigDecimal shippingAmount) {
		this.orderId = orderId;
		this.userId = userId;
		this.dateTime = dateTime;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.shippingAmount = shippingAmount;
	}

	//region Getters and Setters
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public BigDecimal getShippingAmount() {
		return shippingAmount;
	}

	public void setShippingAmount(BigDecimal shippingAmount) {
		this.shippingAmount = shippingAmount;
	}
	//endregion
}
