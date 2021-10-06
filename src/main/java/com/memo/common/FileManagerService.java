package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component 	// 스프링 빈
public class FileManagerService {
	
	// WebMvcConfig도 같이 볼것 => 실제 저장된 파일과 이미지 패스를 매핑해줌
	
	
	// 나중에 에러 보려고 Logger 추가
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// 넘어온 덩어리 파일을 우리 컴퓨터 폴더에 저장하고, WAS의 url 만들고, url에 접속할때 사진 뿌리게 함
	
	// 실제 이미지가 저장될 경로 (경로 복붙하고 맨 마지막에 "/" 붙이자!
	public final static String FILE_UPLOAD_PATH ="C:\\Users\\jglor\\웹개발\\6_spring_project\\ex\\images/";
	
	// 바이너리 파일을 통으로 받아서 url로 만들고 걔를 리턴 (나중에 BO가 파일줄테니깐 url내놓으라고 함)
	public String saveFile(String loginId, MultipartFile file) throws IOException {
		// 파일 디렉토리 경로    예: marobiana_2620349394/apple.png (loginId_현재시간을 변환한값) => 파일명이 겹치지 않게 현재시간을 경로에 붙여준다.
		String directoryName = loginId + "_" + System.currentTimeMillis() + "/";
		String filePath = FILE_UPLOAD_PATH + directoryName;
		
		File directory = new File(filePath); 
		// directory라는 경로에 폴더를 생성
		if (directory.mkdir() == false) {
			// 디렉토리가 만들어지지 않음 (에로) -로거로 찍음
			logger.error("[파일업로드] 디렉토리 생성 실패" + directoryName + ", filePath: " + filePath);
			return null; //메소드의 역할을 이해하면 어떤 것을 리턴해야 하는지 알 수 있음
		}
		
		// 파일 업로드 : byte단위로 업로드 된다.
		byte[] bytes = file.getBytes();
		Path path = Paths.get(filePath + file.getOriginalFilename()); // 사용자가 input에 올린 파일명임
		Files.write(path, bytes);
		
		// 이미지 URL Path를 리턴한다. - 웹주소로 들어갔을때 이미지가 보여지게
		// 예) http://localhost/images/   qwer_138237428/   apple.png
		return "/images/" + directoryName + file.getOriginalFilename();
	}
	
	public void deleteFile(String imagePath) throws IOException {
		// 파라미터:    /images/   qwer_138237428/   apple.png
		// 실제경로:   FILE_UPLOAD_PATH ="C:\\Users\\jglor\\웹개발\\6_spring_project\\ex\\images/"
		// 실제경로 + 파라미터 => 겹쳐져 있는 /images 를 제거해줘야한다.(파라미터쪽에서 공백으로 치환)
		Path path = Paths.get(FILE_UPLOAD_PATH + imagePath.replace("/images/", "")); //사진의 경로
		if (Files.exists(path)) {
			// 실제로 파일이 존재하면 삭제한다.
			Files.delete(path); //BO가 exception 처리함로 얘는 던진다
		}
		// 디렉토리 삭제
		path = path.getParent(); //사진의 부모인 디렉토리가 나옴
		if (Files.exists(path)) {
			// 디렉토리가 존재하면 삭제한다.
			Files.delete(path);
		}
	}
	
	
}
