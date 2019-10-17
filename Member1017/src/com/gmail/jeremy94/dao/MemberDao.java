package com.gmail.jeremy94.dao;

import java.util.Map;

import com.gmail.jeremy94.domain.Member;

public interface MemberDao {
    
	// 아이디 중복 검사를  수행해 주는 메소드 - 데이터 조회  
	public String idCheck(String id);
	
	// 회원가입을 처리하는  메소드 - 데이터 삽입 
	public int insertMember(Member member); 
	
	// 로그인을 처리하는 메소드 
	public Member login(Map<String, Object> map);
	
	
	
}
