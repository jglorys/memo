package com.memo.post.bo;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.dao.PostDAO;
import com.memo.post.model.Post;

@Service
public class PostBO {
	
	@Autowired
	private PostDAO postDAO;
	
	@Autowired 
	private FileManagerService fileManagerService; //스프링 빈이므로 
	
	public List<Post> getPostList(int userId) {
		return postDAO.selectPostList(userId);
	}
	
	public int createPost(int userId, String userLoginId, String subject, String content, MultipartFile file) {
		String imagePath = null;
		
		// file이 있는 경우에만 로직을 타서 imagePath변환해야 함
		if (file != null) {
			// 새로운 클래스를 만들어서 이미지를 처리하는 roll을 가지게 할 것 (나중에 또 댓글이나 그런데서 또 사용할 수 있기 때문에) - common 패키지
			try {
				imagePath = fileManagerService.saveFile(userLoginId, file);
			} catch (IOException e) {
				// 이미지는 실패하고 글만 올라가게
				imagePath = null;
			}
		}
		
		return postDAO.insertPost(userId, subject, content, imagePath);

	}
}
