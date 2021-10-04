package com.memo.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component //스프링 빈 일때
public class PermissionInterceptor implements HandlerInterceptor {
	
	//private Logger logger = LoggerFactory.getLogger(PermisssionInterCeptor.class);
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		
		//리퀘스트 URL
		//System.out.println 쓰면 스레드 safe하지 않음.. logger.info (레벨낮음.debug보다 한 단계 높음) 써준다
		logger.info("[###preHandle]" + request.getRequestURI());

		// 세션을 가져온다
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
	
		// URL Path (어떤 주소로 요청왔는지) 가져온다
		String uri = request.getRequestURI();
		
		
		
		if (userId != null && uri.startsWith("/user")) {
			// 만약 로그인이 되어 있으면  + /user => Post쪽으로 보낸다.
			response.sendRedirect("/post/post_list_view");
			return false; //false를 만나고 위의 리다이렉트로 가게 됨\
		} else if (userId == null && uri.startsWith("/post")) {
			// 반대로, 로그인이 안 되어 있으면 + /post => User쪽으로 보낸다.
			response.sendRedirect("/user/sign_in_view");
			return false; //원래의 요청대로 못가게!!
		}
		
		return true; //무조건 Controller 요청 됨
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response
			, Object handler, ModelAndView modelAndView) {
		logger.info("[###postHandle]" + request.getRequestURI());
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response
			, Object handler, Exception exception) {
		logger.info("[###afterCompletion]" + request.getRequestURI());
	}
}
