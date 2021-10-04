package com.memo.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
public class UserController {
	
	// 회원가입 View
	// Path: /user/sign_up_view
	/**
	 * 회원가입 페이지
	 * @param model
	 * @return
	 */
	@RequestMapping("/sign_up_view")
	public String signUpView(Model model) {
		
		model.addAttribute("viewName", "user/sign_up");
		return "template/layout";
	}
	
	/**
	 * 로그인 화면
	 * @param model
	 * @return
	 */
	//로그인 View
	//Path: /user/sign_in_view
	@RequestMapping("/sign_in_view")
	public String signInView(Model model) {
		model.addAttribute("viewName", "user/sign_in");
		return "template/layout";
	}
	
	/**
	 * 로그아웃
	 * @param request
	 * @return
	 */
	@RequestMapping("/sign_out")
	public String signOutView(HttpServletRequest request) {
		// session을 가져와서 로그아웃 해야 하므로 - 세션 관련된 객체는 request에 담겨져 있음
		HttpSession session = request.getSession();
		session.removeAttribute("userId");
		session.removeAttribute("userName");
		session.removeAttribute("userLoginId");
		// redirect = 완전히 다른 화면으로 보냄
		// 뒤에는 RequestMapping의 URL이 들어감
		return "redirect:/user/sign_in_view";
	}
}
