package com.memo.post;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
	
}
