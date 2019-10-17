package pattern;

public class Dao {

	private Dao() {}
	//변수에 static이 붙으면 1개만 생성 
	private static Dao dao; 
	
	public static Dao getInstance() {
		if(dao == null) {
			dao = new Dao();
		}
		return dao;
	}
	
}
