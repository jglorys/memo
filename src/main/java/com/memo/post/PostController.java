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
import org.springframework.web.bind.annotation.RequestParam;

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
	
	/**
	 * 글쓰기 화면
	 * @param model
	 * @return
	 */
	@RequestMapping("/post_create_view")
	public String postCreateView(HttpServletRequest request, Model model) {
		
		// 로그인된 상태에서만 페이지 접근 가능 - 검증필요(인터셉터 있지만 이중으로 검사)
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			//세션에 id가 없으면 로그인 하는 페이지로 보낸다 (redirect)
			return "redirect:/user/sign_in_view";	
		}
		
		
		model.addAttribute("viewName", "post/post_create"); //post/post_create jsp로 보냄
		return "template/layout";
	}
	
	@RequestMapping("/post_detail_view")
	public String postDetailView(
			HttpServletRequest request,
			@RequestParam("postId") int postId,
			Model model) {
		// post의 id값이 파라미터로 필요함
		
		// 로그인 된 상태에서만 이 페이지에 접근 가능 - 검증필요 (인터셉터 없으면 필수임.. 있는데 이중으로 검사)
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			//세션에 id가 없으면 로그인 하는 페이지로 보낸다 (redirect)
			return "redirect:/user/sign_in_view";	
		}
		
		// postId에 해당하는 게시물을 가져와서 model에 담는다.
		Post post = postBO.getPost(postId);
		model.addAttribute("post", post); //jsp에서 꺼내서 사용 가능
		
		
		model.addAttribute("viewName", "post/post_detail");
		return "template/layout";
	}
	
	
	
	
}
