package com.gmail.jeremy94.service;

import java.util.Scanner;

public interface MemberService {
	//회원가입 처리를 위한 메소드 
	//Scanner를 이용해서 데이터를 입력받고 
	//데이터를 삽입한 후 성공과 실패여부를 리턴 
	public boolean insertMember(
			Scanner sc);
	
	//로그인 처리를 위한 메소드 
	public boolean login(
			Scanner sc);
	
	
}
