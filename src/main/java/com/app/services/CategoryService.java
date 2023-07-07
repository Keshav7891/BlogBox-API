package com.app.services;

import com.app.payloads.CategoryDto;
import com.app.payloads.CategoryResponse;

public interface CategoryService {

	
	CategoryDto createCategory(CategoryDto categoryDto);
	
	CategoryDto updateCategory(CategoryDto categoryDto , Integer categoryId);
	
	void deleteCategory(Integer categoryId);
	
	CategoryResponse getAllCategories(Integer pageNo , Integer pageSize , String sortBy , String  sortDir);
	
	CategoryDto getcategoryByID(Integer categoryId);
	
}
