package com.app.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.payloads.ApiResponse;
import com.app.payloads.CategoryDto;
import com.app.payloads.CategoryResponse;
import com.app.services.CategoryService;
import com.app.utils.AppConstants;

@RestController
@RequestMapping("/api/")
public class CategoryController {
	
	
	@Autowired
	private CategoryService categoryService;
	
	//Add a category
	@PostMapping("/categories")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
		CategoryDto createdCategoryDto = this.categoryService.createCategory(categoryDto);
		return new ResponseEntity<CategoryDto>(createdCategoryDto,HttpStatus.CREATED);
	}
	
	//Update a category
	@PutMapping("/categories/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categorydto , @PathVariable Integer categoryId ){
		CategoryDto updatedCategory = this.categoryService.updateCategory(categorydto, categoryId);
		return new ResponseEntity<CategoryDto>(updatedCategory , HttpStatus.OK);
	}
	
	//Delete a category
	@DeleteMapping("/categories/{categoryId}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId){
		this.categoryService.deleteCategory(categoryId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Category Deleted Successfully" , true),HttpStatus.OK);
	}
	
	//get all categories
	@GetMapping("/categories")
	public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(value = "pageNo" , defaultValue = AppConstants.DEFAULT_PAGE_NUMBER , required = false) Integer pageNo,
															 @RequestParam(value = "pageSize" , defaultValue = AppConstants.DEFAULT_PAGE_SIZE , required = false) Integer pageSize,
															 @RequestParam(value = "sortBy" , defaultValue = "categoryId" , required = false) String sortBy,
															 @RequestParam(value = "sortDir" , defaultValue = AppConstants.DEFAULT_SORT_DIRECTION , required = false) String sortDir){
		CategoryResponse allCategoriesResponse = this.categoryService.getAllCategories(pageNo,pageSize,sortBy,sortDir);
		return new ResponseEntity<CategoryResponse>(allCategoriesResponse,HttpStatus.OK);
	}
	
	//get category by id
	@GetMapping("/categories/{categoryId}")
	public ResponseEntity<CategoryDto> getcategoryByID(@PathVariable Integer categoryId){
		CategoryDto foundCategory = this.categoryService.getcategoryByID(categoryId);
		return new ResponseEntity<CategoryDto>(foundCategory,HttpStatus.OK);
	}
}
