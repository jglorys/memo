package com.memo.post.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.model.Post;

@Repository
public interface PostDAO {
	
	public List<Post> selectPostList(int userId);
	
	public int insertPost(
			@Param("userId") int userId,
			@Param("subject") String subject, 
			@Param("content") String content, 
			@Param("imagePath") String imagePath);
	
	public Post selectPost(int id); 
	
	public void updatePost(
			@Param("id") int id, 
			@Param("subject") String subject, 
			@Param("content") String content, 
			@Param("imagePath") String imagePath);
}
