package com.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.entities.Category;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CategoryDto;
import com.app.payloads.CategoryResponse;
import com.app.repositories.CategoryRepo;

@Service
public class CategoryServiceIMPL implements CategoryService{

	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	private Category dtoToEntity(CategoryDto categoryDto) {
		Category category = this.modelMapper.map(categoryDto, Category.class);
		return category;
	}
	
	private CategoryDto entityToDto(Category category) {
		CategoryDto categoryDto = this.modelMapper.map(category, CategoryDto.class);
		return categoryDto;
	}

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		Category category = this.dtoToEntity(categoryDto);
		Category savedCategory = this.categoryRepo.save(category);
		return this.entityToDto(savedCategory);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
		Category category = this.categoryRepo.findById(categoryId).orElseThrow( () -> new ResourceNotFoundException("Category"," ID ", categoryId) );
		category.setCategoryTitle(categoryDto.getCategoryTitle());
		category.setCategoryDescription(categoryDto.getCategoryDescription());
		Category updatedCategory = this.categoryRepo.save(category);
		return this.entityToDto(updatedCategory);
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		this.categoryRepo.findById(categoryId).orElseThrow( () -> new ResourceNotFoundException("Category", " ID ", categoryId) );
		this.categoryRepo.deleteById(categoryId);
	}

	@Override
	public CategoryResponse getAllCategories(Integer pageNo , Integer pageSize , String sortBy , String  sortDir) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Category> allCategoriesPage = this.categoryRepo.findAll(pageable);
		List<Category> allCategories = allCategoriesPage.getContent();
		List<CategoryDto> allCategoriesDto = allCategories.stream().map( category -> this.entityToDto(category) ).collect(Collectors.toList());
		
		
		CategoryResponse categoryResponse = new CategoryResponse();
		categoryResponse.setAllCategory(allCategoriesDto);
		categoryResponse.setPageNo(allCategoriesPage.getNumber());
		categoryResponse.setPageSize(allCategoriesPage.getSize());
		categoryResponse.setTotalElements(allCategoriesPage.getTotalElements());
		categoryResponse.setTotalPages(allCategoriesPage.getTotalPages());
		
		return categoryResponse;
	}

	@Override
	public CategoryDto getcategoryByID(Integer categoryId) {
		Category foundCategory = this.categoryRepo.findById(categoryId).orElseThrow( () -> new ResourceNotFoundException("Category", " ID ", categoryId) );
		return entityToDto(foundCategory);
	}
	
	
}
