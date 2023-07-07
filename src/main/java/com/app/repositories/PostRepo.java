package com.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.entities.Category;
import com.app.entities.Post;
import com.app.entities.User;

public interface PostRepo extends JpaRepository<Post, Integer>{

	
	List<Post> findByUser(User user);

	@Query("select p from Post p where p.title like :key")
	List<Post> searchByTitle(@Param("key") String string);

	List<Post> findByCategory(Category category);

	List<Post> findByTitleContaining(String string);
	
}
