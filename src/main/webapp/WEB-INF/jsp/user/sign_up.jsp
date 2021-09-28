<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="d-flex justify-content-center">
	<div class="sign-up-box">
		<h1 class="mb-4 mt-4">회원가입</h1>
		<form id="signUpForm" method="post" action="/user/sign_up">
			<table class="sign-up-table table table-bordered">
				<tr>
					<th>* 아이디(4자 이상)<br></th>
					<td>
						<%-- 인풋박스 옆에 중복확인을 붙이기 위해 div를 하나 더 만들고 d-flex --%>
						<div class="d-flex">
							<input type="text" id="loginId" name="loginId"
								class="form-control" placeholder="아이디를 입력하세요.">
							<button type="button" id="loginIdCheckBtn"
								class="btn btn-success ml-2">중복확인</button>
							<br>
						</div> <%-- 아이디 체크 결과 --%> <%-- d-none 클래스: display none (보이지 않게) --%>
						<div id="idCheckLength" class="small text-danger d-none">ID를
							4자 이상 입력해주세요.</div>
						<div id="idCheckDuplicated" class="small text-danger d-none">이미
							사용중인 ID입니다.</div>
						<div id="idCheckOk" class="small text-success d-none">사용 가능한
							ID 입니다.</div>
					</td>
				</tr>
				<tr>
					<th>* 비밀번호</th>
					<td><input type="password" id="password" name="password"
						class="form-control" placeholder="비밀번호를 입력하세요."></td>
				</tr>
				<tr>
					<th>* 비밀번호 확인</th>
					<td><input type="password" id="confirmPassword"
						class="form-control" placeholder="비밀번호를 입력하세요."></td>
				</tr>
				<tr>
					<th>* 이름</th>
					<td><input type="text" id="name" name="name"
						class="form-control" placeholder="이름을 입력하세요."></td>
				</tr>
				<tr>
					<th>* 이메일</th>
					<td><input type="text" id="email" name="email"
						class="form-control" placeholder="이메일 주소를 입력하세요."></td>
				</tr>
			</table>
			<br>

			<button type="button" id="signUpBtn"
				class="btn btn-primary float-right">회원가입</button>
		</form>
	</div>
</div>

<script>
	$(document).ready(function() {

		//아이디 중복 확인
		$('#loginIdCheckBtn').on('click', function(e) {
			let loginId = $('input[name=loginId]').val().trim();

			//alert(loginId); 확인용

			// idCheckLength, idCheckDuplicated, idCheckOk
			if (loginId.length < 4) {
				$('#idCheckLength').removeClass('d-none'); // d-none제거해서 경고문구 노출 => 나머지 두개의 상태는 숨겨야 함
				$('#idCheckDuplicated').addClass('d-none');
				$('#idCheckOk').addClass('d-none');
				return;
			}

			//ajax 서버 호출(중복여부)
			$.ajax({
				type : 'get',
				url : '/user/is_duplicated_id',
				data : {'loginId' : loginId},
				success : function(data) {
					//alert(data.result);

					if (data.result) {
						// 중복이다.
						$('#idCheckLength').addClass('d-none');
						$('#idCheckDuplicated').removeClass('d-none');
						$('#idCheckOk').addClass('d-none');
					} else {
						// 중복이 아니면 => 가능 
						$('#idCheckLength').addClass('d-none');
						$('#idCheckDuplicated').addClass('d-none');
						$('#idCheckOk').removeClass('d-none');
					}
				},
				error : function(e) {
					alert("아이디 중복확인에 실패했습니다. 관리자에게 문의해주세요.");
				}
			});
		});
		
		// signUpBtn
		$('#signUpBtn').on('click', function() {
			
			//validation check
			//name이나 id로 가져오면 되는데, id는 가져올때 #만 붙이면 되니가 id로..
			let loginId = $('#loginId').val().trim();
			if (loginId =='') {
				alert("아이디를 입력하새요");
				return;
			}
			
			let password = $('#password').val();
			let confirmPassword = $('#confirmPassword').val();
			if (password =='' || confirmPassword == '') {
				alert("비밀번호를 입력하세요.");
				return;
			}
			
			if (password != confirmPassword) {
				alert("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
				$('#password').val('');
				$('#confirmPassword').val('');
				return;
			}
			
			let name = $('#name').val().trim();
			if (name == '') {
				alert("이름을 입력해주세요");
				return;
			}
			
			let email = $('#email').val().trim();
			if (email == '') {
				alert("이메일을 입력해주세요");
				return;
			}
			
			// id 중복확인 결과가 있어야만 회원가입 할 수 있게 => 중복확인 완료됐는지 확인
			//-- #idCheckOk <div>클래스에 d-none 이 없으면 사용 가능
			
			if ($('#idCheckOk').hasClass('d-none')) {
				alert("아이디 중복 확인을 해주세요.");
				return;
			}
			
			// 모든 validation체크 완료!! => 서버에 요청하자!!!
			
			// <form>태그 id="signUpForm" method="post" action="/user/sign_up"
			let url = $('#signUpForm').attr('action');
			let data = $('#signUpForm').serialize();
			//serialize : form태그에 있는 데이터를 한번에 스트링으로 쫙 해서 보냄..
			// =>  loginId=aaaaa&password=aaa&name=aaa&email=aaa%40naver.com  이런식으로..-> 쿼리스트링으로 보냄
			// serialize(쿼리스트링)  아니면   json으로 구성해서 보낼 수도 있음 ==> 서버에게 보내는 방법이 많음!
			
			//이 ajax방식은 url, data넣음!  $.post(url, data).done(function(data){     ....  });
			$.post(url, data)
			.done(function(data){
				if (data.result == 'success') {
					alert("가입이 완료되었습니다! 로그인 해주세요.")
					location.href="/user/sign_in_view";
				} else {
					alert("가입에 실패했습니다.");
				}
				
			});

			
		});
		
		
	});
</script>
