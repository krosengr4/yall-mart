package com.pluralsight.yallmart.models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Cart {

	private Map<Integer, CartItem> items = new HashMap<>();

	public Map<Integer, CartItem> getItems() {
		return items;
	}

	public void setItems(Map<Integer, CartItem> items) {
		this.items = items;
	}

	public boolean contains(int productId) {
		return items.containsKey(productId);
	}

	public void add(CartItem item) {
		items.put(item.getProductId(), item);
	}

	public CartItem get(int productId) {
		return items.get(productId);
	}

	public BigDecimal getTotal() {
		return items.values()
					   .stream()
					   .map(i -> i.getLineTotal())
					   .reduce(BigDecimal.ZERO, (lineTotal, subTotal) -> subTotal.add(lineTotal));
	}
}
