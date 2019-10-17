package definition;

public class MySQLSyncMongoDB {
/*

ex)buytbl에서 2번이상 나온 userid의 usertbl테이블에서 userid와 name을 조회 
=>2개의 테이블을 사용해야 원하는 결과를 만들 수 있는데 조회하려고 하는 항목이 
하나의 테이블에 존재하므로 subquery로 원하는 결과를 만들 수 있습니다. 

select userid, name
from usertbl
where userid in (select userid
                     from buytbl
                     group by userid
                     having count(userid) >= 2
                     );
  
 =>서브쿼리의 결과가 2개 이상일 때는 단일행 연산자를 사용하면 에러  


** MySQL과 Java연동 
=>MySQL데이터베이스 드라이버 파일을 준비 
=>MySQL의 드라이버 클래스: com.mysql.jdbc.Driver
=>MySQL의 접속 URL: jdbc:mysql://접속위치:포트번호/데이터베이스이름?
useUnicode=true&characterEncoding=utf8 

MySQL의 기본 포트(3306)를 사용하면 포트번호는 생략이 가능 

=>계정과 비밀번호 

 1.Class.forName(드라이버클래스);
 드라이버 클래스를 JVM에 로드 
 이 코드는 한 번만 수행하면 됨 
 애플리케이션 종류에 따라서 수행하지 않아도 되는 경우가 있습니다.
 웹 애플리케이션에서는 반드시 해야합니다. 
 드라이버 파일을 build path에 추가하지 않거나 드라이버 클래스 이름이 틀리면 
 예외가 발생합니다. 
 
 2.Connection 객체 생성 
 Connection 변수명 = DriverManager.getConnection(데이터베이스 URL, 
 계정, 비밀번호);
 
변수명.setAutoCommit(boolean);//AutoCommit여부 설정 
//기본은 true 

3.필요한 Statement 객체 생성 
1)Statement: 완성된 SQL을 이용해서 실행하고자 하는 경우 

2)PreparedStatement: 데이터가 삽입될 자리에 ?를 기재하고 나중에 실제 데이터를 
바인딩해서 실행 

PreparedStatement 변수명 = Connection객체.prepareStatement(SQL);
//물음표가 있으면 데이터를 바인딩 
 변수명.set자료형(?의 인덱스, 실제데이터);

3)CallableStatement:프로시저를 실행할 때 사용 

=>책이나 일반적인 코드에서는 PreparedStatement를 많이 사용하고 실무에서는 
CallableStatement를 많이 사용 

4.SQL실행 
1)select
ResultSet 변수 = PreparedStatement 객체.executeQuery();

2)그 이외 SQL 
int 변수 = PreparedStatement 객체.executeUpdate();
=>AutoCommit이 아니라면 Connection객체.commit();이 추가되어야 함 

5.실행결과 사용 
1)select
=>ResultSet
next(): 다음 데이터가 있으면 true 없으면false를 리턴
자료형 get자료형(컬럼의 인덱스 또는 컬럼이름): 하나의 컬럼값을 읽어오는 메소드 

=> 여러 개의 데이터 리턴 
while(ResultSet객체.next()){

}

=>하나의 데이터 리턴 
if(ResultSet객체.next()){

}

=>여러 개의 데이터가 리턴될 때 데이터가 없는 경우를 별도로 처리 
if(ResultSet객체.next()){
   do{
   
   }while{
} else {
   //읽어오는 데이터가 없을 때 처리 
}
 
=>여러 개의 컬럼을 조회하는 경우에는 여러 개의 컬럼을 저장할 수 있는 DTO클래스
를 만들어서 DTO클래스에 각 컬럼의 값을 저장 
while(ResultSet객체.next()){
    DTO 변수명 = new DTO(); 
    변수명.set?(ResultSet객체.get자료형(인덱스 또는 이름));
    ...
    List객체.add(변수명);
}
=>Mybatis나 Hibernate를 사용하면 이부분을 자동화 해줍니다. 
=>읽어서 리턴을 할 때 1개를 읽어서 리턴하는 경우에는 데이터가 없을 때는 null을 
데이터가 있을때는 DTO를 리턴 
=>여러 개를 읽어서 리턴하는 경우에는 List객체를 먼저 만들고 리턴 
여러개의 데이터를 조회할 때는 데이터가 없으면 List의 size가 0이 되게 합니다. 
List는 대부분 for를 이용해서 사용하기 때문에 NULL을 리턴하게 되면 에러가 발생 
합니다.
출력하는 부분에서 에러를 방지하기 위해서 데이터가 없을 때도 null을 리턴하지 않고 
객체를 생성해서 리턴합니다.  

2)select이외의 구문 
=>select이외의 구문은 정수를 리턴하는데 이 정수는 영향받은 행의 개수입니다. 
정수가 리턴되면 문법적인 오류는 없어서 정상적으로 수행이 된 것입니다. 
create, alter, drop은 정상적으로 수행되면 0이 리턴되고 
update, delete는 0이 리턴되면 조건에 맞는 데이터가 없는 것이고 1이상이 리턴되면 
조건에 맞는 데이터가 있어서 작업을 수행한 것입니다. 
insert의 경우는 0이 리턴되는 경우가 없습니다. 

6.사용한 객체를 정리 
=>생성해서 사용한 객체들이 close()를 호출하면 됩니다. 

7.이러한 데이터베이스 작업을 할때는 별도의 클래스를 만들어서 데이터베이스 
작업만 하도록 해주는 것이 좋습니다. 
DAO패턴이라고 하는데 DAO클래스에서는 데이터베이스 연동 코드만 존재하는 것
이 좋습니다. 
대부분 데이터베이스는 공유해서 사용하게 되는데 데이터베이스에 접속하고 다른 코드를
처리하는데 시간을 소모하면 안되기 때문입니다. 
데이터베이스 연동 이외의 작업을 처리하는 클래스를 Service클래스라고 합니다. 

8.DAO나 Service클래스를 만들 때는 서버에서 사용하는 경우 싱글톤 패턴을 이용합니다.
싱글톤 패턴은 객체를 1개만 생성하도록 해주는 디자인 패턴입니다. 
템플릿메소드 패턴을 적용합니다. 
템플릿메소드 패턴은 메소드의 원형을 인터페이스나 추상클래스에 만들어두고 이메소드를 
상속받는 클래스에서 구현하는 패턴입니다. 

개발을 하는 개발자를 제외하고는 수행되는 코드가 중요하지 않고 기능이 존재하는디 
여부가 중요하기때문에 인터페이스나 추상클래스에 기능의 모형만 만들어서 확인하게 해주면
됩니다. 
인터페이스나 추상클래스는 어떤 기능이 있다는 것을 보장하기 위해서 사용합니다. 

** 콘솔에서 회원관리 작업 
=>회원테이블: Member 
=>DTO, DAO(템플릿 메소드패턴, 싱글톤 패턴), Service(템플릿메소드패턴, 싱글톤패턴), 
Main
=>Spring같은 프레임워크를 이용하면 싱글톤 패턴을 자동으로 적용시킬 수 있습니다. 
=>템플릿메소드 같은 경우도 인터페이스는 자동으로 생성해주는 도구들이 있습니다. 

1.회원테이블 생성 
아이디 - 변하지 않는 문자열로 30자까지, 기본키 
비밀번호 - 자주 변경되는 문자열로 20자까지, 필수 
이름 - 변하지 않는 문자열로 20자까지 , 필수 
별명 - 자주 변겨오디는 문자열로 20자까지, 필수이고 유일해야함 
가입일 - 날짜 , 기본값을 오늘 날짜 

create table member(
  id varchar(30) primary key,
  pw char(20) not null,
  name varchar(20) not null,
  alias char(20) not null,
  regdate date,
  unique(alias)
)default charset=utf8;

2. Java Application 프로젝트 생성 

3. 필요한 라이브러리 들을 build path에 추가 
=>mysql드라이버 파일을 build path에 추가  


4.회원테이블과 매핑할 DTO클래스 생성 

public class Member {

	private String id;
	private String pw;
	private String name;
	private String alias; 
	private Date regdate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	
	@Override
	public String toString() {
		return "Member [id=" + id + ", pw=" + pw + ", name=" + name + ", alias=" + alias + ", regdate=" + regdate + "]";
	}

}
5.Dao 인터페이스 생성 
public interface MemberDao {
    
	// 아이디 중복 검사를  수행해 주는 메소드 - 데이터 조회  
	public String idCheck(String id);
	
	// 회원가입을 처리하는  메소드 - 데이터 삽입 
	public int insertMember(Member member); 
	
	// 로그인을 처리하는 메소드 
	public Member login(Map<String, Object> map);
	
	
	
}

6. Dao클래스를 생성해서 메소드를 구현 
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

7.데이커베이스 연동이외의 작업을 수행하는 서비스 인터페이스를 생성하고 필요한
메서드를 선언 
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

8.Service인터페이스를 구현한 Service클래스를 생성해서 메소드를 구현 
package com.gmail.jeremy94.service;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.gmail.jeremy94.dao.MemberDao;
import com.gmail.jeremy94.domain.Member;

public class MemberServiceImpl implements MemberService {
	//
	private MemberDao dao;
	
	
	public MemberDao getDao() {
		return dao;
	}

	public void setDao(MemberDao dao) {
		this.dao = dao;
	}

	@Override
	public boolean insertMember(Scanner sc) {
		boolean result = false;
		//id, pw , name, alias를 입력 받아서 데이터를 삽입 
		while(true) {
			System.out.printf("아이디 입력:");
			String id = sc.nextLine();
			if(dao.idCheck(id)!= null) {
				System.out.println(
						"이미 사용중인 아이디 입니다.\n");
			    continue;
			}
			System.out.printf("비밀번호 입력:");
			String pw = sc.nextLine();
			
			System.out.printf("이름 입력:");
			String name = sc.nextLine();
			
			System.out.printf("별명 입력:");
			String alias = sc.nextLine();
			
			Calendar cal =
					new GregorianCalendar();
			Date regdate =
					new Date(cal.getTimeInMillis());
			
			Member member = new Member();
			member.setId(id);
			member.setPw(pw);
			member.setName(name);
			member.setAlias(alias);
			member.setRegdate(regdate);
			
			int r = dao.insertMember(member);
			if( r > 0) {
				result = true;
			}
			
			break;
		}
				
		return result;
	}

	@Override
	public boolean login(Scanner sc) {
		boolean result =false;
		System.out.printf("아이디 입력:");
		String id = sc.nextLine();
		System.out.printf("비밀번호 입력:");
		String pw = sc.nextLine();
		//id와 pw를 묶어서 Map에 저장 
		Map<String, Object> map =
				new HashMap<String, Object>();
		map.put("id", id);
		map.put("pw",pw);
		//Dao의 로그인 메소드 호출  
		Member member = dao.login(map);
		
		if(member != null) {
			result = true;
		}
		
		return result;
	}

}

9.Main메소드를 만들어 실행 
import java.util.Scanner;

import com.gmail.jeremy94.dao.MemberDao;
import com.gmail.jeremy94.dao.MemberDaoImpl;
import com.gmail.jeremy94.service.MemberServiceImpl;

public class Main2 {

	public static void main(String[] args) {
		//1번이면 회원가입 2번이면 로그인 
		//3번이면 종료를 할 수 있도록 메뉴를 구성 
		Scanner sc = new Scanner (System.in);
		
		MemberDao dao = 
				MemberDaoImpl.getInstance();
		
		MemberServiceImpl service = 
				new MemberServiceImpl();
		//Service의 dao에 여기서 만든 dao를 주입 
		service.setDao(dao); 
		
        while(true) {
        	System.out.printf(
        			"메뉴 입력( 1.회원가입  2.로그인  3.종료 ): ");
        	String menu = sc.nextLine();
        	
        	switch(menu) {
        	
        	case "1":
        		boolean x = service.insertMember(sc);
        		String msg = null;
        		if(x == true) {        			
        			msg = "회원가입 성공\n";
        		}else {
        			msg ="회원가입 실패 \n";
        		}
        		System.out.printf(msg);
        		break;
        		
        	case "2":
        		boolean y = service.login(sc);
        		if(y == true) {
        		    System.out.printf("로그인 성공\n");
        		} else {
        			 System.out.printf("로그인 실패\n");
        		}
        		break;
        	
        	case "3":
        		System.out.printf("프로그램 종료\n");
        		sc.close();
        		System.exit(0);
        		break;
        	
        	default:
        		System.out.printf("잘못된 메뉴 선택\n");
        		break;
        	}
        	
        }
		
	}

}



** Singleton Pattern 
=>클래스의 객체를 하나만 만들 수 있도록 디자인 하는 패턴 
=> 진입점(프로그램의 시작)에 해당하는 클래스의 경우나 Controller클래스의 경우는 
여러개의 객체가 생성되면 혼란이 올 수 있습니다. 
=>개발자가 코드를 잘 만들어서 객체를 하나가지고 프로그래밍하면 좋겠지만 이작업은 
쉽지 않습니다. 
=>클래스를 만들 때 한 개만 생성되도록 하면 개발자가 코드를 잘못 작성하더라도 
하나의 객체를 가지고 작업을 하게 됩니다. 

1.외부에서 객체를 생성하는 생성자를 호출할 수 없도록 private으로 생성 

2. 자신의 타입으로 static변수를 생성 

3. 자신의 타입을 리턴하는 static 메소드를 생성해서 2번에서 만든 변수가 null일 때 
만 객체를 생성하도록 작성하고 변수를 리턴 

**템플릿 메소드 패턴 
=>인터페이스를 만들어서 필요한 메소드를 전부 선언하고 클래스에서 구현 
  
**Service와 DAO를 만들때의 차이 
=>DAO 에서는 하나의 SQL마다 하나의 메소드를 생성 

=>Service에서는 하나의  트랜잭션마다 하나의 메소드를 생성 

=>Service에서는 DAO를 주입받아서 사용 
외부에서 생성해서 대입받아서 사용합니다. 

DB -> DAO -> Service -> Controller(Main)

**접근지정자 
 =>객체지향언어에서 클래스안에 만든 변수와 메소드의 접근 범위를 한정하기위한 
 예약어 
 
1.private: 클래스 내에서만 접근이 가능 
=> 현재 클래스 외부에서는 이 접근 지정자에 접근할 수 없습니다. 

2. default(package)
=>자신의 패키지내에서는 public이 되고 패키지 외부에서는 private 
=> 접근지정자를 생략하면 default 

3. protected 
=>현재 클래스 내부와 상속받은 클래스 내부에서 접근이 가능 

4. public 
=>현재 클래스 내부와 외부 모두에서 접근이 가능 

5. 객체지향언어에서는 일반적으로 변수는 private으로 하고 메소드를 public으로
설정해서 메소드를 통해서 접근하는 것을 권장 (getter&setter)

=>클라이언트용 프로그래밍에서는 접근지정자를 사용하지 않는 경우가 많고 
서버프로그래밍에서는 접근지정자를 이용해서 접근을 제한하고 메소드를 통해서만 변수에 
접근하는 것을 권장 



** NoSQL 
=>하나의 데이터를 하나의 문서로 형태로 표현하는 도큐먼크 지향 데이터베이스 
=>Not Only SQl의 약자
=>관계형 데이터베이스(테이블 구조에 데이터를 저장하는 데이터베이스 - RDBMS)
는 테이블을 먼저 생성하고  그 테이블의 구조에 맞게 데이터를 삽입해서 사용합니다. 
관계형 데이터베이스는 이미 만들어져 있는 경우 구조변경이 어렵습니다. 
기존 데이터의 변형을 가해야 합니다. 
고객의 니즈가 변경되는 경우 관계형 데이터베이스는 구조를 다시 만들어야 합니다. 

데이터가 많아져서 분산해서 저장해야 하는 경우 데이터를 조회하려면 많은 join작업을 수행 
해야합니다. 

관계형 데이터베이스는 엄격한 트랜잭션 처리를 지원하기때문에 안정성이 높습니다. 

=>이러한 관계형 데이터베이스의 단점을 보완하기 위해서 등장한 저장기술 중의 하나가
NoSQL 입니다. 
다른 저장기술로는 Hadoop등 도 있습니다. 

=>NoSQL중에서 유명한 제품으로는 Mongo DB와 Cassandra, HBase등이 있습니다.


**JSON(Javascript Object Notation)
=>데이터를 자바스크립트의 객체표현법으로 작성 

[] -> 배열 
{} -> 객체 
: -> 속성과 값의 구분 
, -> 객체내에서 속성과 속성의 구분 또는 배열내에서 데이터와 데이터의 구분을 위해서 사용 

  int[] ar = {10,20,30};
  [10,20,30]
  
  Member member =new Member(0;
  member.setId("gg");
  member.setPw("kk");
  => 관계형 데이터베이스 - DTO  
  
  {"id":"gg","pw":"kk"}  {"id":"gg","pw":"kk","email":"jeremy94@naver.com"}
  =>NoSQL- Map
 
 간단한 서버 만드는 것은 node.js + mongo db 
 M(mongo DB)E(Express.js)A(Angular.js)N(Node.js)
 =>풀스택을 구현할 때 가장 간단하게 할 수 있는 방법 

** Mongo DB 설치 
1.www.mongodb.org/downloads community server를 클릭하고 윈도우 용을 다운로드 받아서 설치 
=>msi버전을 설치를 하고 zip은 압축을 해제하고 명령어를 이용해서 실행 

2.Mac에서 설치 
1)homebrew를 이용해서 설치 
=>homebrew는 apple	이 제공하지 않는 패키지를 설치하기 위한 패키지 관리자 
터미널에 명령어를 입력해서 설치 

=> brew.sh/index_ko.html

/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/
 install/master/install)"
 
2)mongo db설치 - 터미널에 입력 
brew install mongodb
위의 명령이 안되면 아래 명령을 수행 
brew tap mongodb/brew
brew install mongodb-community@4.2

3) 관리자 명령으로 mongo db의 데이터 디렉토리를 생성하고 설정 
sudo mkdir -p /data/db 

sudo chown 컴퓨터아이디 /data/db

3.몽고DB서버 실행 
터미널에서 mongod라는 명령을 실행 
윈도우즈에서는 압축을 해제할 디렉토리로 경로를 이동한 후 
mongod -- dbpath 데이터베이스 파일 경로 
윈도우즈에서 c드라이브의 mongodb디렉토리에 데이터를 저장하고자
하는 경우에는 
mongod--dbpath c:\mongodb

 =>기본적으로 27017번 포트를 이용해서 서버가 실행됩니다. 
 
 4. 클라이언트 실행 - 새로운 터미널에서 실행 
 로콜에 접속:mongo
 다른 컴퓨터에 접속: mongo --host "아이피주소":"포트번호"
 

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
 
 */
}
