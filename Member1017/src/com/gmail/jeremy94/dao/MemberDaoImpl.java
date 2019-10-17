package com.gmail.jeremy94.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import com.gmail.jeremy94.domain.Member;

public class MemberDaoImpl implements MemberDao {

	//싱글톤 패턴 구현을 위한 코드 
	private MemberDaoImpl() {}
	
	private static MemberDao memberDao;
	
	public static MemberDao getInstance() {
		if(memberDao == null) {
			memberDao = new MemberDaoImpl();
		}
		return memberDao;
	}
	
	//클래스를 처음 사용할 때 1번만 호출되서 수행되는 부분 
	static {
		try {
			//MySQL 드라이버 클래스 로드 
			Class.forName(
					"com.mysql.jdbc.Driver");
		} catch (Exception e) {
			System.out.printf("클래스 로드 예외 %s\n",
					e.getMessage());
			e.printStackTrace();
		} 
	}
	
	// 여러 메소드에서 사용해야 하는 변수를 선언
	// 인터페이스 들로 객체를 생성해서 리턴하는 메소드를 
	// 이용해서 인터페이스를 구현한 
	// Anonymous 객체를 넘겨받아서 저장합니다. 
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	
	@Override
	public String idCheck(String id) {
		String result = null; 
		//null이 리턴되면 아이디가 없는 것이고 
		//null이 아닌 데이터가 리턴되면 아이디가 존재 
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://192.168.0.151:3306/"
					+ "user05?useUnicode=true"
					+ "&characterEncoding=utf8", 
					"user05", "user05");
			// member테이블에서 id가 존재하는지 확인 
			pstmt = con.prepareStatement(
					"select id from member where id=? ");
			pstmt.setString(1, id);
			// SQL을 실행 
		    rs = pstmt.executeQuery();
		    // 결과 사용 - 하나의 행이 리턴되는 경우 
		    if(rs.next()) {
		    	result = rs.getString("id");
		    }
		    
		    //정리 
		    rs.close();
		    pstmt.close();
		    con.close();
		    
		} catch(Exception e) {
			System.out.printf(
					"아이디 중복 체크 예외:%s\n",
					e.getMessage());
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public int insertMember(Member member) {
		int result = -1;
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://192.168.0.151:3306/"
					+ "user05?useUnicode=true"
					+ "&characterEncoding=utf8", 
					"user05", "user05");
			// member테이블에서 id가 존재하는지 확인 
			pstmt = con.prepareStatement(
					"insert into member(id, pw, name, alias, regdate) "
					+ "values(?,?,?,?,?) ");
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPw());
			pstmt.setString(3, member.getName());
			pstmt.setString(4, member.getAlias());
			pstmt.setDate(5, member.getRegdate());
			
			//SQL을 실행 
			result = pstmt.executeUpdate();
			
		    //정리 
		    pstmt.close();
		    con.close();
		    
		} catch(Exception e) {
			System.out.printf(
					"회원 가입 예외:%s\n",
					e.getMessage());
			e.printStackTrace();
		}
			
		return result;
	}

	@Override
	public Member login(Map<String, Object> map) {
		Member member = null;
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://192.168.0.151:3306/"
					+ "user05?useUnicode=true"
					+ "&characterEncoding=utf8", 
					"user05", "user05");
			// member테이블에서 id와 pw가 일치하는 
			//데이터가 있는지 확인 
			pstmt = con.prepareStatement(
					"select * "
					+ "from member "
					+ "where id = ? and pw = ? ");
			//map은 바인딩시 강제 형변환을 해주어야 함 
			pstmt.setString(1, 
					(String)map.get("id"));
			pstmt.setString(2, 
					(String)map.get("pw"));		
			
			//SQL을 실행 
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				member = new Member();
				member.setId(rs.getString("id"));
				member.setAlias(rs.getString("alias"));
			}
			
		    //정리 
			rs.close();
		    pstmt.close();
		    con.close();
		    
		} catch(Exception e) {
			System.out.printf(
					"회원 가입 예외:%s\n",
					e.getMessage());
			e.printStackTrace();
		}
		
		
		return member;		
	}

}
