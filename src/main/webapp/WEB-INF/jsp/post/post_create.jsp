<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1 class="m-2">글쓰기</h1>
		
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력해주세요.">
		<textarea id="content" rows="7" cols="100" class="form-control mt-2" placeholder="내용을 입력해주세요."></textarea>
	
		<div class="d-flex justify-content-end">
			<%-- file업로드 가능한 버튼 / id: 여기있는 파일 가지고 와야되니까 / accept : 확장자 지정 (얘는 사용자 편의를 위한것..나중에 확장자 확인해줘야함!)--%>
			<input type="file" id="file" accept=".jpg, .jpeg, .png, .gif" class="mt-2">
		</div>
		<br>
		<div class="d-flex justify-content-between">
			<a href="/post/post_list_view" class="btn btn-dark">목록</a>
			
			<div>
				<%-- 모두지우기 - 스크립트로지움 -> button, a 둘다됨 --%>
				<button type="button" id="clearBtn" class="btn btn-secondary">모두지우기</button>
				<button type="button" id="saveBtn" class="btn btn-primary">저장</button>
			</div>
		</div>
	</div>
</div>

<script>
$(document).ready(function(){
	
	// 모두지우기 버튼 클릭
	$('#clearBtn').on('click', function(){
			if (confirm("내용을 지우겠습니까?")) { 	// 버튼 클릭시 지울거냐고 alert창으로 띄움
				$('#subject').val(''); // 공백으로 만든다!
				$('#content').val('');
				$('#file').val('');
			}
	});
	
	// 글 내용 저장버튼 클릭
	$('#saveBtn').on('click', function(){
		
		//제목 , 내용 validation check
		let subject = $('#subject').val().trim();
		//console.log(subject);
		if (subject == '') {
			alert('제목을 입력해주세요.');
			return;
		}
		
		let content = $('#content').val();
		//console.log(content);
		if (content == '') {
			alert('내용을 입력해주세요.');
			return;
		}
		
		// 파일이 업로드 된 경우에 확장자 검사
		let file = $('#file').val(); //파일의 이름을 가지고옴
		//console.log("file : " + file);
		if (file != '') { //파일이 있을때 확장자 검사
			//console.log("파일명자름" + file.split('.'));
			let ext = file.split('.').pop().toLowerCase();
			if ($.inArray(ext, ['jpg', 'jpeg', 'png', 'gif']) == -1) { //없으면 -1이 나오고 그걸 찾아내야됨
				alert("이미지 파일만 업로드 할 수 있습니다.");
				$('#file').val(''); //잘못된 파일을 지운다
				return;
			}
		}
		
		// form태그를 자바스크립트에서 만든다
		let formData = new FormData();
		formData.append('subject', subject); //비어있던 form태그에 들어감 '' :안에 들어가는건 request parameter
		formData.append('content', content);
		formData.append('file', $('#file')[0].files[0]); //file이라는 태그의 첫번째, 그 중에 첫번째 파일을 가지고 온다.
		//$('#file')[0].files[0] ==> 진짜 파일이 담겨짐 , 컨트롤러에서는 멀티파트파일로 받을것
		
		
		//ajax
		$.ajax({
			//파일은 무조건 post방식으로 바디에 담아서..get방식으로 url에 안담김
			type:'post',
			url: '/post/create',
			data: formData,
			enctype: 'multipart/form-data',	// 파일 업로드 필수 설정
			processData: false,	// 파일 업로드 필수 설정
			contentType: false,  // 파일 업로드 필수 설정 -----여기까지가 request를 위한 설정
			success: function(data) {
				if (data.result == 'success') {
					alert("메모가 저장됐습니다.");
					location.href ="/post/post_list_view";
				}		
			}, 
			error: function(e) {
				alert("메모 저장에 실패했습니다." + e);
			}
		
		});
		
	});
	
	
	
});



</script>