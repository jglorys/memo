package com.memo.post;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
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
	public String postListView(
			@RequestParam(value="prevId", required = false) Integer prevIdParam, 
			@RequestParam(value="nextId", required = false) Integer nextIdParam, 
			Model model, HttpServletRequest request) {
		// 글 목록들을 가져온다 ==> 모델에 담는다.
		HttpSession session = request.getSession();
		
		Integer userId = (Integer) session.getAttribute("userId"); // 아주작은확률로 세션이 풀려서 null일 수 있으므로 Integer
		if (userId == null) {
			logger.info("[post_list_view] userId is null. " + userId); //로깅으로 남기면 추적이 가능함. 에러의 원인을 쉽게 찾을 수 있음
			return "redirect:/user/sign_in_view";
		}
		
		List<Post> postList = postBO.getPostList(userId, prevIdParam, nextIdParam); //파라미터 userId 
		// jsp로 보내는 Post객체의 형태가 많이 흐트러지면 그냥 새로운 객체를 하나 만드는게 나음 => BO에서 가공함!!!-> BO는 원래 가공하는 애라서 길어야됨
		
		int prevId = 0;
		int nextId = 0;
		// null과 비어있는 리스트를 구별해야함 - list에서 직접 isEmpty사용하지 않음
		if (!CollectionUtils.isEmpty(postList)) { // == false
			 prevId = postList.get(0).getId();
			 nextId = postList.get(postList.size() - 1).getId();
			 
			 // 이전이나 다음이 없는 경우 0으로 세팅한다. (jsp에서 0인지 검사해서 버튼을 노출시킬것인지)
			 
			 // 마지막페이지(다음 기준) 인 경우 0으로 세팅
			 if (postBO.isLastPage(userId, nextId)) {
				 nextId = 0;
			 }
			 
			 // 첫번째페이지(이전기준)인 경우 0으로 세팅
			 if (postBO.isFirstPage(userId, prevId)) {
				 prevId = 0;
			 }
				 
			 // db로 미리 조회하는 수 밖에 없다.
		}
		
		
		// 모델에 담는다.
		model.addAttribute("postList", postList);
		model.addAttribute("viewName", "post/post_list");
		model.addAttribute("prevId", prevId);
		model.addAttribute("nextId", nextId);
		
		
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
	
	/**
	 * 게시글 선택시 화면
	 * @param request
	 * @param postId
	 * @param model
	 * @return
	 */
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
