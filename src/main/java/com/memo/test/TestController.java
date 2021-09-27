package com.memo.test;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.memo.test.bo.TestBO;

@Controller
public class TestController {
	
	@Autowired
	private TestBO testBO;
	
	//Spring Response 테스트
	//URL : http://localhost/test1
	@RequestMapping("/test1")
	@ResponseBody
	public String test1() {
		return "hello world!";
	}

	//DB 연동 테스트
	@RequestMapping("/test2")
	@ResponseBody
	public List<Map<String, Object>> test2() { 
		return testBO.getUserList();
	}

}
