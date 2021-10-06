package com.memo.post;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.bo.PostBO;

@RequestMapping("/post")
@RestController
public class PostRestController {
	
	@Autowired
	private PostBO postBO;
	
	/**
	 * 새 게시물 업로드
	 * @param subject
	 * @param content
	 * @param file
	 * @param request
	 * @return
	 */
	@PostMapping("/create")
	//json으로 리턴하므로 맵으로 리턴
	public Map<String, Object> create(
			@RequestParam("subject") String subject,
			@RequestParam("content") String content,
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request
			) {
		// MultipartFile file : 찐파일이 덩어리로 받아짐
		// session에서 userId를 가져온다
		HttpSession session = request.getSession();
		// 세션에 있는 것 : userId, userName, userLoginId
		Integer userId = (Integer) session.getAttribute("userId");
		String userLoginId = (String) session.getAttribute("userLoginId");
		// if (userId == null) ...logger...넣어도 좋음
		
		// DB에 내용 인서트 BO한테 시킴 -> userId, userLoginId, subject, content, file
		Map<String, Object> result = new HashMap<>();
		result.put("result", "error"); //일단 error로 셋팅
		
		int row = postBO.createPost(userId, userLoginId, subject, content, file);
		if (row > 0) {
			result.put("result", "success");
		}
		
		// 결과값 response
		return  result;
	}
	
	/**
	 * 기존 글 수정
	 * @param postId
	 * @param subject
	 * @param content
	 * @param file
	 * @param request
	 * @return
	 */
	@PutMapping("/update")
	public Map<String, Object> update(
			@RequestParam("postId") int postId,
			@RequestParam("subject") String subject,
			@RequestParam("content") String content,
			@RequestParam(value = "file" , required = false) MultipartFile file,
			HttpServletRequest request
			) {
		HttpSession session = request.getSession();
		String loginId = (String) session.getAttribute("userLoginId");
		
		//DB업데이트 - 5개 bo한테 넘겨줌
		postBO.updatePost(postId, loginId, subject, content, file);
		
		
		//result 응답값 구성 -- 여기까지 온거면 무조건 성공인 것임.if row>0 해서 돌려도됨
		Map<String, Object> result = new HashMap<>();
		result.put("result", "success");
		return result;
	}
	
	@DeleteMapping("/delete")
	public Map<String, Object> delete(
			@RequestParam("postId") int postId
			) {
		// 검증은 생략
		// db postId 해당하는 데이터 삭제
		postBO.deletePost(postId);
		
		
		// 결과 리턴
		Map<String, Object> result = new HashMap<>();
		result.put("result", "success"); //row>0으로 해도 되고, 여기까지온게 성공이므로 둘중 하나 선택해서 하면됨
		
		return result;
	}
	
}
