package com.memo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.memo.interceptor.PermissionInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Autowired //@Component해서 스프링 빈을 가져옴
	private PermissionInterceptor interceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor)
		.addPathPatterns("/**")		// /** 손주를 포함한 모든 디렉토리 확인
		.excludePathPatterns("/user/sign_out", "/static/**", "/error");		
		// 주소들 중에서 이 주소로 들어오면 예외 처리 = 인터셉터 안태울 path설정 (로그아웃할때-로직과 반대됨 post-user)
		// static 밑에 있는 모든 애들
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**")	// http://localhost/images/qwer_1633347398902/KakaoTalk_20180224_215432988.jpg
		.addResourceLocations("file:///C:\\Users\\jglor\\웹개발\\6_spring_project\\ex\\memo_workspace\\Memo\\images/"); 
		//  실제 파일 저장 위치
		// "file:///"  맨 앞에 붙이기  ,   맨뒤에 "/"붙이기!
	}
}
