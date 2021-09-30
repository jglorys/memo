<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="d-flex justify-content-center mt-5">
	<div class="login-box">
		<h2 class="text-center m-4">LOGIN</h2>
		<%-- 키보드 Enter키로 로그인이 될 수 있도록 form 태그를 만들어준다.(submit 타입의 버튼이 동작됨) --%>
		<form id="loginForm" action="/user/sign_in" method="post">
			<div class="input-group mb-3">
				<%-- input-group-prepend: input box 앞에 ID 부분을 회색으로 붙인다. --%>
				<div class="input-group-prepend">
					<span class="input-group-text">ID</span>
				</div>
				<input type="text" class="form-control" id="loginId" name="loginId">
			</div>
	
			<div class="input-group mb-3">
				<div class="input-group-prepend">
					<span class="input-group-text">PW</span>
				</div>
				<input type="password" class="form-control" id="password" name="password">
			</div>
			
			<%-- btn-block: 로그인 박스 영역에 버튼을 가득 채운다. 한 행을 차지하게 됨--%>
			<%-- submit에 있는게 엔터키 눌러서 나와야 함 = 로그인 버튼 --%>
			<input type="submit" id="loginBtn" class="btn btn-block btn-primary" value="로그인">
			<a class="btn btn-block btn-secondary" href="/user/sign_up_view">회원가입</a>
		</form>
	</div>
</div>

<script>
	
	$(document).ready(function(){
		$('#loginForm').submit(function(e){
			e.preventDefault(); //submjit 자동 수행 중단
			
			// validation
			let loginId = $('input[name=loginId]').val().trim();
			if (loginId == '') {
				alert("아이디를 입력해주세요.");
				return false; //alert창 뜨고 끝 / return 해버리면 그냥 submit 돼버림 => submit 에대해 return false라는 뜻
			}
			
			let password = $('input[name=password]').val();
			if (password == ''){
				alert("비밀번호를 입력해주세요.");
				return false;
			}
			
			// <form id="loginForm" action="/user/sign_in" method="post">
			let url = $(this).attr('action'); //$(this) = $('#loginForm')
			let data = $(this).serialize(); // 쿼리스트링으로 name 값들을 구성하고 requestBody에 넣어 보낸다.
			console.log("url:" + url);
			console.log("data:" + data);
			
			//$.post(url, data).done(function(data){     ....  });
			$.post(url, data)
			.done(function(data) { //data는 json으로 내려옴
				if (data.result =='success') {
					location.href = '/post/post_list_view';
				} else {
					alert("로그인에 실패했습니다. 다시 시도해주세요.");
				}
			});
			
		});
	});
</script>

