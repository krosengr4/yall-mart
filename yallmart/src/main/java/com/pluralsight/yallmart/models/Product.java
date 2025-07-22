package com.pluralsight.yallmart.models;

import java.math.BigDecimal;

public class Product {

	private int productId;
	private String name;
	private BigDecimal price;
	private int categoryId;
	private String description;
	private int stock;

	public Product() {}

	public Product(int productId, String name, BigDecimal price, int categoryId, String description, int stock) {
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.categoryId = categoryId;
		this.description = description;
		this.stock = stock;
	}

	//region Getters and Setters
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
	//endregion
}
