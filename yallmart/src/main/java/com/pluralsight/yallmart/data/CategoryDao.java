package com.pluralsight.yallmart.data;

import com.pluralsight.yallmart.models.Category;

import java.util.List;

public interface CategoryDao {

    List<Category> getAll();

    Category getById(int categoryId);

    Category create(Category category);

    void update(int categoryId, Category category);

    void delete(int categoryId);
}
