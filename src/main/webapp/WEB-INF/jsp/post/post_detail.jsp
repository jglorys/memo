<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- JSTL Core태그 --%>

<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1 class="m-2">글 상세/수정</h1>
		
		<input type="text" id="subject" class="form-control" value="${post.subject}">
		<textarea id="content" rows="7" cols="100" class="form-control mt-2">${post.content}</textarea>
	
		<div class="d-flex justify-content-end">
			<%-- file업로드 가능한 버튼 / id: 여기있는 파일 가지고 와야되니까 / accept : 확장자 지정 (얘는 사용자 편의를 위한것..나중에 확장자 확인해줘야함!)--%>
			<input type="file" id="file" accept=".jpg, .jpeg, .png, .gif" class="mt-2">
		</div>
		
		<%-- 이미지가 있을 때에만 이미지 영역 추가 --%>
		<c:if test="${not empty post.imagePath}">
			<div class="m-2">
				<img src="${post.imagePath}" alt="업로드 이미지" width="200">
			</div>
		</c:if>
		
		<div class="d-flex justify-content-between">
			<a href="#" class="btn btn-secondary" id="deleteBtn" data-post-id="${post.id}">삭제</a>
			
			<div>
				<%-- 목록으로 : 간단한 링크 ==> a, button(스크립트에 함수구현필요) 다 됨 --%>
				<button type="button" id="listBtn" class="btn btn-dark">목록으로</button>
				<button type="button" id="saveBtn" class="btn btn-primary" data-post-id="${post.id}">수정</button>
			</div>
		</div>
		<br>
	</div>
</div>


<script>
$(document).ready(function(){
	//목록으로 버튼 클릭 => 목록으로 이동
	$('#listBtn').on('click', function() {
		location.href = "/post/post_list_view";
	});
	
	// 글 수정
	$('#saveBtn').on('click', function(){
	
		let subject = $('#subject').val().trim();
		if (subject == '') {
			alert("제목을 입력해주세요");
			return;
		}
		
		let content = $('#content').val();
		if (content == ''){
			alert("내용을 입력해주세요");
			return;
		}
		
		let fileName = $('#file').val();
		if (fileName != '') {
			let fileArr = fileName.split('.');
			// pop함수 / arr의 [length-1] 둘중하나 사용가능
			let ext = fileArr.pop().toLowerCase(); //확장자 뽑아옴
			
			if ($.inArray(ext, ['gif', 'png', 'jpg', 'jpeg']) == -1){
				// -1이 나온것은 잘못된것임 (잘못된 확장자)
				alert("이미지 파일만 업로드 할 수 있습니다.");
				$('#file').val(''); //올라간 잘못된 파일을 비운다.
				return;
			}
		}
		
		let postId = $(this).data('post-id'); //우리가 지어준 이름을 가지고옴
		// console.log("postId: " +postId);
		
		//임의로 form 태그 객체를 자바스크립트에서 만든다.
		let formData = new FormData();
		formData.append('postId', postId);
		formData.append('subject', subject);
		formData.append('content', content);
		formData.append('file', $('#file')[0].files[0]);
		
		// ajax 통신으로 서버에 전송한다.
		$.ajax({
			type : 'put',
			url : '/post/update',
			data : formData,
			enctype : 'multipart/form-data', //파일업로드를 위한 필수 설정
			processData : false, //파일업로드를 위한 필수 설정
			contentType : false, //파일업로드를 위한 필수 설정 --- 여기까지 request 
			success : function(data) {
				if (data.result == 'success') {
					alert("메모가 수정되었습니다.");
					location.reload(); //새로고침
				}
			},
			error : function(e) {
				alert("메모 수정에 실패했습니다. 관리자에게 문의해주세요." + e);
			}
			
		});
	});
	
	$('#deleteBtn').on('click', function(e){
			e.preventDefault(); // a태그가 화면 상단으로 올라가는 것 방지(버튼태그면 안해도됨)
			
			let postId = $(this).data('post-id'); //deleteBtn에 담아놓은 post-id데이터를 가져옴
			
			// ajax 통신으로 삭제 요청
			$.ajax({
				type : 'delete',
				url : '/post/delete',
				data : {'postId':postId}, //json으로 넘김
				success : function(data){
					if (data.result == 'success'){
						// 삭제했으면 글 목록으로 보냄
						alert("글이 삭제되었습니다.");
						location.href = "/post/post_list_view";
					}
				},
				error : function(e) {
					alert("메모를 삭제하는데 실패했습니다." + e);
					location.reload();
				}
				
			});
	});
	
});

</script>
