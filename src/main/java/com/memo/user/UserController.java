package com.memo.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
public class UserController {
	
	// 회원가입 View
	// Path: /user/sign_up_view
	@RequestMapping("/sign_up_view")
	public String signUpView(Model model) {
		
		model.addAttribute("viewName", "user/sign_up");
		return "template/layout";
	}
	
	//로그인 View
	//Path: /user/sign_in_view
	@RequestMapping("/signIpView")
	public String signInView() {
		return "user/sign_in_view";
	}
	
}
