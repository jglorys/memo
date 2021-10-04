<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- JSTL Core태그 --%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%-- JSTL Formatting --%>


<div>
	<h2 class="font-weight-bold m-2">글 목록</h2>

	<table class="table table-hover">
	<%-- table-hover : 마우스 올렸을때 호버 효과 --%>
		<thead>
			<tr>
				<th>No</th>
				<th>제목</th>
				<th>작성 날짜</th>
				<th>수정 날짜</th>
			</tr>
		</thead>
		<tbody>
		<%-- 반복문 돌리기 위해 JSTL사용함 => JSTL core 라이브러리 가져옴 --%>
			<c:forEach items="${postList}" var="post">
			<%-- Post객체에서 .으로 꺼낼 수 있음 --%>
				<tr>
					<td>${post.id}</td>
					<td>${post.subject}</td>
					<td>
						<%--Date 객체로 내려온 값을 String Format으로 변경해서 출력 --%>
						<fmt:formatDate value="${post.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<fmt:formatDate value="${post.updatedAt}" pattern="yyyy-MM-dd HH:mm:ss" var="updatedAt"/>
						${updatedAt}
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<div class="d-flex justify-content-end">
		<a href="/post/post_create_view" class="btn btn-primary">글쓰기</a>
	</div>
</div>