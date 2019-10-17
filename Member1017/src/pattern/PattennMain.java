package pattern;

public class PattennMain {

	public static void main(String[] args) {
	
/*
		Dao dao1 = new Dao();
		Dao dao2 = new Dao();
		
		//2개의 객체 동일성 여부 확인 
		System.out.printf("%d\n",
				dao1.hashCode());
		System.out.printf("%d\n",
				dao2.hashCode());
*/
		
		// getInstance를 몇 번 호출하더라도 
		// 모두 동일한 객체를 리턴 
		Dao dao1 = Dao.getInstance();
		Dao dao2 = Dao.getInstance();
		
		System.out.printf("%d\n",
				dao1.hashCode());
		System.out.printf("%d\n",
				dao2.hashCode());
		
	}

}
