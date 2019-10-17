import java.util.HashMap;
import java.util.Map;

import com.gmail.jeremy94.dao.MemberDao;
import com.gmail.jeremy94.dao.MemberDaoImpl;
import com.gmail.jeremy94.domain.Member;

public class Main {

	public static void main(String[] args) {
		
		MemberDao dao = 
				MemberDaoImpl.getInstance();
	
		Member member =new Member();
		
		Map <String, Object>map = 
				new HashMap<String, Object>(); 
/*		
		member.setId("gg");
		member.setPw("kk");
		System.out.printf("%s\n", member);
*/
		
		
/*		
	
		// 로그인을 처리할 때 id와 pw일치하면 
		//id에 해당하는 회원정보를 저장 
		//일치하는 데이터가 없으면 null을 리턴 				
		map.put("id","gg" );
		map.put("pw","kk" );
	
		map.put("id","oo" );
		map.put("pw","kk" );
		System.out.printf(
				"결과:%s\n", dao.login(map));
*/
		
		
/*		//새로운 회원 가입 테스트 
		member.setId("jerome94");
		member.setPw("1234");
		member.setName("김기범");
		member.setAlias("하하");
		
		Calendar cal = 
				new GregorianCalendar();
		Date regdate =
				new Date(cal.getTimeInMillis());
		member.setRegdate(regdate);
		
		int r =dao.insertMember(member);
		System.out.printf("결과:%d\n", r);
*/ 
	
		
/*		
		System.out.printf("%s\n", dao.idCheck("gg")); // 존재하는 아이디 조회 
		
		System.out.printf("%s\n", dao.idCheck("jerome")); //없는 아이디 조회 
*/		
		
		
	}
}
