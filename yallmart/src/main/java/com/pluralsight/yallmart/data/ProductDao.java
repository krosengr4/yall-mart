package com.pluralsight.yallmart.data;

import com.pluralsight.yallmart.models.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    List<Product> getAll ();
    Product getById (int productId);
    List<Product> search (Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> listByCategoryId (Integer categoryId);
    Product create (Product product);
    void update (int productId, Product product);
    void delete (int productId);
}
