<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
	integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm"
	crossorigin="anonymous">

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
	integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
	crossorigin="anonymous"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
	integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
	crossorigin="anonymous"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
	integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
	crossorigin="anonymous"></script>

</head>
<body>
	<div class="d-flex justify-content-center">
		<div class="sign-up-box">
			<h1 class="mb-4 mt-4">회원가입</h1>
			<form id="signUpForm" method="post" action="/user/sign_up_for_submit">
				<table class="sign-up-table table table-bordered">
					<tr>
						<th>* 아이디(4자 이상)<br></th>
						<td>
							<%-- 인풋박스 옆에 중복확인을 붙이기 위해 div를 하나 더 만들고 d-flex --%>
							<div class="d-flex">
								<input type="text" id="loginId" name="loginId"
									class="form-control" placeholder="아이디를 입력하세요.">
								<button type="button" id="loginIdCheckBtn"
									class="btn btn-success">중복확인</button>
								<br>
							</div> <%-- 아이디 체크 결과 --%> <%-- d-none 클래스: display none (보이지 않게) --%>
							<div id="idCheckLength" class="small text-danger d-none">ID를
								4자 이상 입력해주세요.</div>
							<div id="idCheckDuplicated" class="small text-danger d-none">이미
								사용중인 ID입니다.</div>
							<div id="idCheckOk" class="small text-success d-none">사용
								가능한 ID 입니다.</div>
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

				<button type="submit" id="signUpBtn" class="btn btn-primary float-right">회원가입</button>
			</form>
		</div>
	</div>
</body>
</html>