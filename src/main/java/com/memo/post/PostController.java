package com.memo.post;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.memo.post.bo.PostBO;
import com.memo.post.model.Post;

@RequestMapping("/post")
@Controller
public class PostController {
	
	private Logger logger = LoggerFactory.getLogger(PostController.class);
	
	@Autowired
	private PostBO postBO;
	
	/**
	 * 글 목록 화면
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/post_list_view")
	public String postListView(Model model, HttpServletRequest request) {
		// 글 목록들을 가져온다 ==> 모델에 담는다.
		HttpSession session = request.getSession();
		
		Integer userId = (Integer) session.getAttribute("userId"); // 아주작은확률로 세션이 풀려서 null일 수 있으므로 Integer
		if (userId == null) {
			logger.info("[post_list_view] userId is null. " + userId); //로깅으로 남기면 추적이 가능함. 에러의 원인을 쉽게 찾을 수 있음
			return "redirect:/user/sign_in_view";
		}
		
		List<Post> postList = postBO.getPostList(userId); //파라미터 userId -> 리스트로 그냥 전부 다 가져옴
		// jsp로 보내는 Post객체의 형태가 많이 흐트러지면 그냥 새로운 객체를 하나 만드는게 나음 => BO에서 가공함!!!-> BO는 원래 가공하는 애라서 길어야됨
		
		// 모델에 담는다.
		model.addAttribute("postList", postList);
		model.addAttribute("viewName", "post/post_list");
		return "template/layout";
	}
	
	@RequestMapping("/post_create_view")
	public String postCreateView(Model model) {
		
		model.addAttribute("viewName", "post/post_create"); //post/post_create jsp로 보냄
		return "template/layout";
	}
}
