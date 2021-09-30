package com.memo.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;
import com.memo.user.model.User;

@RequestMapping("/user")
@RestController
public class UserRestController {

	@Autowired
	private UserBO userBO;

	/**
	 * 아이디 중복확인 체크
	 * 
	 * @param loginId
	 * @return
	 */
	@RequestMapping("/is_duplicated_id")
	public Map<String, Boolean> isDuplicatedID(@RequestParam("loginId") String loginId) {
		// loginId 중복 여부 DB조회
		userBO.existLoginId(loginId);

		// 중복여부에 대한 결과 map생성
		Map<String, Boolean> result = new HashMap<>();
		result.put("result", userBO.existLoginId(loginId));

		return result;
	}

	/**
	 * 회원가입 (DB에 insert)
	 * 
	 * @param loginId
	 * @param password
	 * @param name
	 * @param email
	 * @return
	 */
	@PostMapping("/sign_up")
	public Map<String, Object> signUp(@RequestParam("loginId") String loginId,
			@RequestParam("password") String password, @RequestParam("name") String name,
			@RequestParam("email") String email) {
		// 비밀번호 해싱
		String encryptPassword = EncryptUtils.md5(password); // 해싱된 스트링을 리턴함

		// DB user insert
		int row = userBO.addNewUser(loginId, encryptPassword, name, email);

		// insert성공했다면 row=1
		Map<String, Object> result = new HashMap<>();
		result.put("result", "success");

		// 응답값 생성 후 리턴
		return result;
	}
	
	/**
	 * 로그인
	 * 
	 * @param loginId
	 * @param password
	 * @param request
	 * @return
	 */
	@PostMapping("/sign_in")
	public Map<String, Object> signIn(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			HttpServletRequest request) {
		//HttpServletRequest request는 세션을 위해!
		
		// 파라미터로 받은 비밀번호를 해싱한다.
		String encrytPassword = EncryptUtils.md5(password);

		// id와 해싱된 password로 DB SELECT
		User user = userBO.getUserByLoginIdAndPassword(loginId, encrytPassword);

		Map<String, Object> result = new HashMap<>();
		if (user != null) {
			// 있으면 로그인 성공
			result.put("result", "success");

			// 로그인 상태 유지 = 세션
			HttpSession session = request.getSession();
			//나중에 필요한 것들 저장해 놓으면 됨 - 글쓸때..
			session.setAttribute("userId", user.getId()); //맵 처럼 생각하면 됨
			session.setAttribute("userName", user.getName());
			session.setAttribute("userLoginId", user.getLoginId());
			// Controller / jsp 에서 사용가능 
			// -> 로그인이 되어있으면 .getAttribute해서 키이름으로 꺼냄 / ${userName}으로 jsp에서 꺼내서 사용가능
		} else {
			// 없으면 로그인 실패
			result.put("result", "error");
		}

		// 결과 리턴
		return result;
	}

}
