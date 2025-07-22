package com.pluralsight.yallmart.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

public class CartItem {

	private Product product = null;
	private int quantity = 1;
//	private BigDecimal discountPercent = BigDecimal.ZERO;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	//If want to use discount, have to make getters and setters for discountPercent

	@JsonIgnore
	public int getProductId() {
		return this.product.getProductId();
	}

	public BigDecimal getLineTotal() {
		BigDecimal basePrice = product.getPrice();
		BigDecimal quantity = new BigDecimal(this.quantity);

		return basePrice.multiply(quantity);
	}
}

