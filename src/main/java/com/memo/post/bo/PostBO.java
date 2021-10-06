package com.memo.post.bo;

import java.io.IOException;
import java.util.Collections;
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
	
	private static final int POST_MAX_SIZE = 3; // static final : 상수(못고침)
	
	@Autowired
	private PostDAO postDAO;
	
	@Autowired 
	private FileManagerService fileManagerService; //스프링 빈이므로 
	
	public List<Post> getPostList(int userId, Integer prevId, Integer nextId) {
		// 765 에서 이전을 눌렀을때, 7보다 큰 3개 => 오름차순 8 9 10 ==> 코드에서 역순으로 변경해줘야 10 9 8이 됨
		// 765에서 다음을 눌렀을때, 5보다 작은 3개 => 내림차순 432 ==> 코드에서 역순변경 필요 없음
		// 쿼리작성할때 오름차순/내림차순도 파라미터로 보내줘야함
		// 3가지 (아무것도 안들어온경우, 이전이 들어왔을때, 다음이 들어왔을때) => myBatis에서 if문사용해서!
		String direction = null; //null || next || prev
		Integer standardId = null; //null || nextId || prevId
		if (prevId != null) {
			// 이전 클릭
			direction = "prev";
			standardId = prevId;
			
			// 7보다 큰 3개 => 8 9 10 => reverse해서 10 9 8 
			List<Post> postList = postDAO.selectPostList(userId, direction, standardId, POST_MAX_SIZE); // 8 9 10으로 되어있음
			
			// reverse
			Collections.reverse(postList); //받아온데이터를 역순으로 뒤집어서 저장까지 해줌. return안함
			return postList;
			
		} else if (nextId != null) {
			// 다음 클릭
			direction = "next";
			standardId = nextId;
			//5보다 작은 3개 => 내림차순 432 ==> 코드에서 역순변경 필요 없음
		}
		
		return postDAO.selectPostList(userId, direction, standardId, POST_MAX_SIZE);
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
	
	// 다음 기준으로 마지막 페이지인가? - 오름차순으로 가장 작은 값 가져와서 같으면 마지막페이지
	public boolean isLastPage(int userId, int nextId) {
		// 오름차순 limit 1 => 제일 작은 값
		return nextId == postDAO.selectIdByUserIdAndSort(userId, "ASC");
	}
	
	// 이전 기준으로 마지막 페이지인가? - 가장 큰 값 가져와서 같으면 첫페이지
	public boolean isFirstPage(int userId, int prevId) {
		// 내림차순 limit 1 => 제일 큰 값
		return prevId == postDAO.selectIdByUserIdAndSort(userId, "DESC");
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
	
	public void deletePost(int postId) {
		// postId로 post를 조회
		Post post = getPost(postId);
		//post가 null인지 검사!!
		if (post == null) {
			logger.error("[delete post] 삭제할 게시물이 없습니다. {}", postId);
			return; //메소드 종료
		}
		
		// 그림부터 확인 - 있으면 삭제 
		String imagePath = post.getImagePath();
		if (imagePath != null) {
			//FileManagerService의 deleteFile(String imagePath)메소드를 사용
			try {
				fileManagerService.deleteFile(imagePath);
			} catch (IOException e) {
				// 삭제에 실패함
				logger.error("[delete post] 그림 삭제 실패 postId: {} , path: {}", postId, imagePath);
			}
		}
		
		// post를 삭제한다
		postDAO.deletePost(postId);
		
	}
}
