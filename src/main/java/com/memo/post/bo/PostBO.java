package com.memo.post.bo;

import java.io.IOException;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.dao.PostDAO;
import com.memo.post.model.Post;

@Service
public class PostBO {
	
	private Logger logger = LoggerFactory.getLogger(PostBO.class);
	
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
	
	public Post getPost(int postId) {
		return postDAO.selectPost(postId);
	}
	
	public void updatePost(int postId, String loginId, String subject, String content, MultipartFile file) {
		
		// postId로 게시물이 있는지 확인
		Post post = getPost(postId); // 기존에 있던 실제파일을 삭제하기 위해 가져온다.
		if (post == null) {
			logger.error("[글 수정] post is null. postId:{}", postId);
			return; //void이므로 아무값도 return 하지 않음
		}
		
		// file이 있는 경우, 업로드 후  imagePath얻어와야한다.
		String imagePath = null;
		if (file != null) {
			// 파일 업로드
			try {
				imagePath = fileManagerService.saveFile(loginId, file);
				
				// 기존에 있던 파일을 삭제 (새로운파일 업로드 잘 됐으면,, 이것부터하면 파일 날라갈수도있음)
				// => imagePath가 존재(업로드 성공) && 기존에 파일이 있으면 파일 제거
				if (imagePath != null && post.getImagePath() != null) {
					// 업로드가 실패할 수 있으므로 업로드 성공 후 제거
					fileManagerService.deleteFile(post.getImagePath()); //기존에 있었던 path보내줌
				}
				
			} catch (IOException e) {
				// fileManagerService가 예외상황일때 처리해줌 
			}
		}
		
		// db update
		postDAO.updatePost(postId, subject, content, imagePath);
		
	}
}
