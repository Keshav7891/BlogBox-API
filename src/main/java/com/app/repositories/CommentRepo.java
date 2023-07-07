package com.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.entities.Comment;

public interface CommentRepo extends JpaRepository<Comment, Integer>{

	@Query("select c from Comment c where c.content like :key")
	List<Comment> searchByTitle(@Param("key")String string);

}
