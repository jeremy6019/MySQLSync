package definition;

public class Mongodbsync {
/*
  
 **Mongodb 서버 실행  
 mongod --dbpath 데이터 저장 경로 
 =>mongod라고만 입력하면 기본 디렉토리에 데이터를 저장 
 =>windows의 경우는 mongodb설치경로를 path에 추가하지 않았다면 프롬프트를 
 명령어가 있는 곳으로 이동하고 명령을 실행 
  
** Mongodb클라이언트 실행 
mongo
mongo --host "서버주소":"포트번호" 
mongo --host 192.168.0.151:27017 

**데이터베이스 사용 
use데이터베이스 이름 
=>데이터베이스가 없으면 생성 


** MongoDB 특징 
=>NoSQL데이터베이스들은 확장이 쉽습니다. 
=>데이터의 표현은 JSON형태의 BSON을 이용 
=>프로시저 대신에 자바스크립트를 사용 
=>업데이트가 자주 발생해서 하위버전과 호환성이 떨어집니다.

=>관계형 데이터베이스와의 용어 비교 
Database(오라클은 SID) - Database : 하나의 저장소 
Table <-> Collection 
Index <-> Index(데이터를 빠르게 접근하기 위한 개체) 
Row<-> Document(하나의 행) 
Column <-> Attribute(하나의 열 - 속성) 
Join <-> Embedding&Linking 

**하나의 행 표현 
{"속성이름 - key":데이터,...}
=>속성이름 순서대로 저장합니다. 
동일한 속성이름들을 가졌더라도 속성의 순서가 다르면 다른 문서입니다. 
{"name":"park", "age":49}
{"age":49,"name":"park" }
=>위 2갱의 문서는 다른 문서입니다. 
=> key는 예약어를 사용할 수 없습니다. 
=>key는 중복되거나 null일 수 없습니다. 
=>.,$,_는 key에 사용하면 안됩니다. 

**배열 표현 - 여러 개의 데이터표현 
[데이터 나열]

** 	데이터 삽입 
db.컬렉션이름.insert(객체)
=>id와 password라는 속성을 가진 객체를 생성해서 users라는 컬렉션에 추가 

** Collection 생성 
db.createCollection('컬렉션 이름")
=>컬렉션을 만들지 않아도 데이터를 삽입할 때 새로운 컬렉션이름을 사용하면 
자동으로 생성됩니다.
db.users.insert({"id":"root","password":"1234"})
db.users.insert({"id":"user01","password":"5678"})
db.users.insert({"id":"user02","password":"5678"})
db.users.insert({"id":"user01","password":"5678"})
db.users.insert({"id":"user03","password":"6019"})

** 데이터 조회 
1.전체 데이터 조회 
db.컬렉션이름.find()

db.users.find() // users의 전체 데이터 조회 

2. 특정 속성만 조회하거나 제외 
db.컬렉션이름.find({조건 또는 생략}, {속성이름:0 또는 1,...})
=>0을 대입하면 제외되고 1을 대입하면 출력 

db.users.find({},{"id":1}) // users에서 id만 조회 
db.users.find({},{id:0}) //users 에서 id만 제외 

=>컬렉션을 만들지 않고 바로 데이터 삽입 가능 
=>기본키는 없지만 MongoDB가 ObjectId라는 속성을 생성해서 해시코드를 저장 
=>별도의 SQL사용하는 것이 아니라 프로그래밍 언어의 함수호출 형태로 작업 

3. 조건을 이용한 조회 
db.컬렉션이름.find({속성이름:일치하는 값})

id가 user01인 데이터를 조회 
db.users.find({"id":"user01"})

{}안에 여러 개의 속성을 기재하면 and가 됩니다. 
id가 user01이고 password가 1234 인 데이터를 조회 
db.users.find({"id":"user01","password":"1234"})

=>ne(not), lt(lte), gt(gte), in, or, and, nor등을 사용 
웹프로그래밍 언어에서는 >,<를 잘 사용하지 않습니다. 
태그가 <>로 감싸기 때문에 >,<대신에 다른 기호를 사용하는 경우가 많습니다. 
 
 db.컬렉션이름.find({속성이름:{$ne:값}}):아닌 경우 
 => lt와 gt도 동일한 방법으로 사용 
 
 db.컬렉션이름.find({속성이름:{$in:[값을 나열]}}):[]안에 있는 값 중에 하나 
 
 db.컬렉션이름.find({$or:[{속성이름:값},{속성이름:값}]})
 
 =>users컬렉션에서idㅏ root가 아닌 데이터 조회 
 db.users.find({"id":{$ne:"root"}})
 
 =>users컬렉션에서 id가 root또는 user01인 데이터 조회 
 db.users.find({"id":{$in:["root","user01"]}})
 
 =>users컬렉션에서 id가 root이거나 비밀번호가 5678인 데이터를 조회 
 db.users.find({$or:[{"id":"root"},{"password":"5678"}]})
 
 like는 정규식(Regular Expression)을 이용
 =>users컬렉션에서 id가 users로 시작하는 모든 데이터를 조회 
  db.users.find({"id":/user. 별 /})  


4. 데이터 1개만 조회 
find대신에 findOne 

5. 데이터 조회 관련 기타 함수 
=>limit(개수): 개수만큼만 조회 
=>skip(개수):개수만큼 건너 뛰기 
=>sort({속성이름:1 또는 -1,...}):데이터를 속성이름으로 정렬하는 데 1이면 
오름차순이고 -1이면 내림차순입니다. 
=>3개함수는 find()에 사용하는 데 조합해서 사용하는 것이 가능 

6.데이터를 순서대로 하나씩 조회 
=>데이터베이스에서는 cursor라고 합니다. 
프로그래밍언어에서는 Iterator(Enumerator)

=>mongodb에서는 find()의 결과를 변수에 담으면 변수가 커서가 됩니다. 
변수에서 hasNext()를 호출하면 읽을 데이터의 존재여부를 리턴 
next()를 호출하면 다음 데이터를 읽어옵니다. 

** 데이터 수정 
=> 특정 컬럼 수정
db.컬렉션이름.update({조건}, {$set:{컬럼이름:수정할내용}})
set대신에 unset을 사용하면 컬럼이 삭제 됩니다. 

 =>데이터 전체 수정 
db.컬렉션이름.update({조건}, {컬럼이름:수정할내용})

=>update대신에 save()를 이용해서 기존 데이터를 삭제하고 삽입하는 것이 가능 

=>옵션으로 {upsert:true}를 추가로 대입하면 데이터가 없으면 추가

=>users컬렉션에서 id가 root인 데이터의 password를 0000으로 수정 
db.users.update({"id":"root"},{$set:{"password":"0000"}}) 

** 데이터 삭제 
db.컬렉션.remove({조건})

** 컬렉션 삭제 
db.컬렉션이름.drop()

** 기본적으로 트랜잭션의 개념이 없습니다. 

/*
** 알고리즘 테스트에서 괄호 짝 맞추기 문제를 많이 출제합니다. 

**Java와 MongoDB의 CRUD(Create-Insert, Read, Update, Delete)작업 

1.드라이버 파일
mongo-driver
mongo-driver-core 
bson
=>mongo-driver에 있는 클래스가 mongo-driver-core의 클래스를 이용하고
mongo-driver-core의 클래스가 bson의 클래스를 이용합니다. 
서로 다른 라이브러릴의클래스를 사용하는 것을 의존한다고 합니다. 
이런 경우 이러한 의존성을 파악해서 라이브러리를 사용하는 것은 너무 어려운
일이라서 maven이나 gradle이라는 build tool들은 최상위 라이브러리만 가져오는 코드
를 작성하면 알아서 다운로드 받아서 설치를 합니다.  

2.Java Application프로젝트를 생성해서 위 3개의 파일을 복사하고 build path에 
추가 
1)필요한 라이브러리를 다운로드 
=>oracle을 제외하고는 www.mvnrepository에서 다운로드 가능 
mongo-driver, mongo-driver-core bson 

2) 프로젝트에 라이브러리를 복사 
=>복사하지 않으면 프로젝트를 옮기거나 파일을 삭제하면 프로젝트가 실행 안됨 

3)라이브러릴를 선택하고 Build path에 추가 
파일을 선택하고 마우스 오른쪽을 클릭한 후 build path - add To Build 
=>maven 이나 gradle을 사용하지 않는 Java Application Project(운영체제에 
설치하고 실행시키는 프로그램)에서는 이 방법을 사용 

3.MongoDB와 연결 
=>new MongoClient(String ip, int port)

4. 데이터베이스의 모든 컬렉션 이름 가져오기 
1)데이터베이스 사용 객체 만들기 
MongoDatabase 이름1 = MongoClient객체.getDatabase(String dbname); 

2)모든 컬렉션 이름 가져오기 
MongoIterable<String>이름2 = 이름1.listCollectionNames();

3)빠른 열거를 이용해서 이름2를 접근 
for(Strinf 임시이름: 이름2){

}

5. 데이터베이스 닫기 
MongoClient객체.close();

6.로컬에 있는 몽고 데이터베이스의 mymongo라는 데이터베이스에 있는 모든 
컬렉션 이름을 출력 

 1)프로젝트가 저장된 디렉토리에 db.txt파일을 만들어서 내용을 작성 
 127.0.0.1,27017,mymongo
 =>프로그램이 처음에 읽어서 계속 사용하는 텍스트가 있을 때 별도의 파일에 기록 
 해두고 사용하면 좋습니다. 
 내용이 변경되어야 하는 경우 파일의 내용만 변경하고 프로그램을 다시 시작하면 
 되기 때문입니다.
 프로그램 내의 코드로 만들면 수정할 떄 컴파일부터 다시해야합니다. 
 =>접속할 데이터베이스 정보나 자주 사용하는 메세지들은 별도의 파일에 기록합니다.
 => 국제화(국가나 언어설정에 따라 다르게 보여지거나 사용되는 것)를 할 때도 
 이 방법을 사용합니다. 
 
  2)접속해서 데이터베이스에 만들어진 컬렉션 확인- main메소드에 작성  
  

	public static void main(String[] args) {
		
	    String ip = null;
	    int port = -1; 
	    String dbName = null;
	    
	    //문자를 읽기 위한 스트림을 생성 
	    //close()하지 않기 위해서 
	    //try()에서 생성 
	    try(BufferedReader br = 
	    		new BufferedReader(
	    				new InputStreamReader(
	    						new FileInputStream(
	    								"./db.txt")))) {
	    	//한 줄 읽기 
	    	String line = br.readLine();
//	    	System.out.printf("%s\n", line);
	    	
	    	// , 로 구분된 문자열을 배열에 저장하기 
	    	String [] ar = line.split(",");
	    	//분할 데이터를 저장 
	    	ip = ar[0];
	    	port = Integer.parseInt(ar[1]); 
	        dbName = ar[2]; 
	        
//	        System.out.printf("ip:%s\n", ip);
//	        System.out.printf("port:%d\n", port);
//	        System.out.printf("dbName:%s\n", dbName);
	    	
	        //MongoDB 연결 
	        MongoClient mc = 
	        		new MongoClient(ip, port);
	        //데이터베이스 연결 
	        MongoDatabase db = 
	        		 mc.getDatabase("mymongo");
	        //데이터베이스의 모든 컬렉션 가져오기 
	        MongoIterable<String> collections = 
	        		db.listCollectionNames();
	        
	        //빠른 열거를 이용한 접근 
	        for(String collection : collections) {
	        	System.out.printf("%s\n", collection);
	        }
	        mc.close();
	            
	    }catch(Exception e) {
	    	System.out.printf("파일 읽기 예외%s\n",
	    			e.getMessage());
	    	e.printStackTrace();
	    	System.exit(0);
	    }

	}
7. 
	public class MongoMain {

	public static void main(String[] args) {
		
	    String ip = null;
	    int port = -1; 
	    String dbName = null;
	    
	    //문자를 읽기 위한 스트림을 생성 
	    //close()하지 않기 위해서 
	    //try()에서 생성 
	    try(BufferedReader br = 
	    		new BufferedReader(
	    				new InputStreamReader(
	    						new FileInputStream(
	    								"./db.txt")))) {
	    	//한 줄 읽기 
	    	String line = br.readLine();
//	    	System.out.printf("%s\n", line);
	    	
	    	// , 로 구분된 문자열을 배열에 저장하기 
	    	String [] ar = line.split(",");
	    	//분할 데이터를 저장 
	    	ip = ar[0];
	    	port = Integer.parseInt(ar[1]); 
	        dbName = ar[2]; 
	        
//	        System.out.printf("ip:%s\n", ip);
//	        System.out.printf("port:%d\n", port);
//	        System.out.printf("dbName:%s\n", dbName);
	    	
	        //MongoDB 연결 
	        MongoClient mc = 
	        		new MongoClient(ip, port);
	        //데이터베이스 연결 
	        MongoDatabase db = 
	        		 mc.getDatabase("mymongo");
/*	        
	        //데이터베이스의 모든 컬렉션 가져오기 
	        MongoIterable<String> collections = 
	        		db.listCollectionNames();        
	        //빠른 열거를 이용한 접근 
	        for(String collection : collections) {
	        	System.out.printf("%s\n", collection);
	        }
	*/        
	/*    
	        //데이터를 삽입하거나 삭제 및 수정 또는 조회를 
	      // 할 Document컬렉션을 가져오기
	        MongoCollection<Document>users = 
	        		db.getCollection("users");
	        //쓰기 권한을 가져오기 
	        users.getWriteConcern();
	        //기록할 데이터 만들기 
	        Document document = new Document();
	        document.put("id",  "jeremy94");
	        document.put("password", "root");
	        //데이터 삽입 
	        users.insertOne(document);
	        
	        mc.close();
	            
	    }catch(Exception e) {
	    	System.out.printf("파일 읽기 예외%s\n",
	    			e.getMessage());
	    	e.printStackTrace();
	    	System.exit(0);
	    }

	}
}
*/
/*  
8. 데이터 수정 
1)수정하는 방법 
=> 수정할 객체를 Document 객체로 생성 

=> 컬렉션변수.update 또는 updateOne(Filters.조건, new Document("$set,
Document 객체));

2)users컬렉션에 있는 데이터 중에서 id가 jeremy94인 데이터의 password를  
tjoeun으로 수정하고 name을 군계로 설정 

        //데이터를 삽입하거나 삭제 및 수정 또는 조회를 
	      // 할 Document컬렉션을 가져오기
	        MongoCollection<Document>users = 
	        		db.getCollection("users");
	        //쓰기 권한을 가져오기 
	        users.getWriteConcern();
	        //수정할 데이터 만들기 
	        Document document = new Document();
	        document.put("password",  "tjoeun");
	        document.put("name", "군계");
	        //데이터 수정  
	        users.updateMany(
	        		Filters.eq("id", "jeremy94"),
	        		new Document("$set", document));
	        
	        mc.close();
	            
	    }catch(Exception e) {
	    	System.out.printf("파일 읽기 예외%s\n",
	    			e.getMessage());
	    	e.printStackTrace();
	    	System.exit(0);
	    }

	}
}

9. 데이터 삭제 
1)삭제방법 
컬렉션객,deleteMany또는 deleteOne(Filters.조건);

2)users컬렉션에서 id가 jeremy94인 데이터를 삭제 
/*
public class MongoMain {

	public static void main(String[] args) {
		
	    String ip = null;
	    int port = -1; 
	    String dbName = null;
	    
	    //문자를 읽기 위한 스트림을 생성 
	    //close()하지 않기 위해서 
	    //try()에서 생성 
	    try(BufferedReader br = 
	    		new BufferedReader(
	    				new InputStreamReader(
	    						new FileInputStream(
	    								"./db.txt")))) {
	    	//한 줄 읽기 
	    	String line = br.readLine();
//	    	System.out.printf("%s\n", line);
	    	
	    	// , 로 구분된 문자열을 배열에 저장하기 
	    	String [] ar = line.split(",");
	    	//분할 데이터를 저장 
	    	ip = ar[0];
	    	port = Integer.parseInt(ar[1]); 
	        dbName = ar[2]; 
	        
//	        System.out.printf("ip:%s\n", ip);
//	        System.out.printf("port:%d\n", port);
//	        System.out.printf("dbName:%s\n", dbName);
	    	
	        //MongoDB 연결 
	        MongoClient mc = 
	        		new MongoClient(ip, port);
	        //데이터베이스 연결 
	        MongoDatabase db = 
	        		 mc.getDatabase("mymongo");

	
/*	    
	        //데이터를 삽입하거나 삭제 및 수정 또는 조회를 
	      // 할 Document컬렉션을 가져오기
	        MongoCollection<Document>users = 
	        		db.getCollection("users");
	        //쓰기 권한을 가져오기 
	        users.getWriteConcern();
	      
	        //데이터 삭제   
	        users.deleteMany(
	        		Filters.eq("id", "jeremy94"));
	        
	        
	        mc.close();
	            
	    }catch(Exception e) {
	    	System.out.printf("파일 읽기 예외%s\n",
	    			e.getMessage());
	    	e.printStackTrace();
	    	System.exit(0);
	    }

	}
}
*/
	/*
10.데이터 읽기 
1)읽은 방법 
FindIterable<Document>읽기객체 = 컬렉션객체.find(Filters.조건);
=>조건이 생략되면 컬렉션의 모든 데이터를 가져옵니다. 

public class MongoMain {

	public static void main(String[] args) {
		
	    String ip = null;
	    int port = -1; 
	    String dbName = null;
	    
	    //문자를 읽기 위한 스트림을 생성 
	    //close()하지 않기 위해서 
	    //try()에서 생성 
	    try(BufferedReader br = 
	    		new BufferedReader(
	    				new InputStreamReader(
	    						new FileInputStream(
	    								"./db.txt")))) {
	    	//한 줄 읽기 
	    	String line = br.readLine();
//	    	System.out.printf("%s\n", line);
	    	
	    	// , 로 구분된 문자열을 배열에 저장하기 
	    	String [] ar = line.split(",");
	    	//분할 데이터를 저장 
	    	ip = ar[0];
	    	port = Integer.parseInt(ar[1]); 
	        dbName = ar[2]; 
	        
//	        System.out.printf("ip:%s\n", ip);
//	        System.out.printf("port:%d\n", port);
//	        System.out.printf("dbName:%s\n", dbName);
	    	
	        //MongoDB 연결 
	        MongoClient mc = 
	        		new MongoClient(ip, port);
	        //데이터베이스 연결 
	        MongoDatabase db = 
	        		 mc.getDatabase("mymongo");
	    
	        //데이터를 삽입하거나 삭제 및 수정 또는 조회를 
	      // 할 Document컬렉션을 가져오기
	        MongoCollection<Document>users = 
	        		db.getCollection("users");
	        //쓰기 권한을 가져오기 
	        users.getWriteConcern();
	      
	        //데이터 읽기  
	       FindIterable<Document> documents =
	    		   users.find(Filters.eq("id", "user03"));
	        
	      for(Document document  : documents) {
	    	  System.out.printf("%s\n", document);
	      }
	        
	        mc.close();
	            
	    }catch(Exception e) {
	    	System.out.printf("파일 읽기 예외%s\n",
	    			e.getMessage());
	    	e.printStackTrace();
	    	System.exit(0);
	    }

	}
}


**하나의 DTO객체나 Map객체이 CRUD

배열이나 List에 CRUD 

파일에 CRUD 

데이터베이스에 CRUD 

 
** 웹 프로그래밍 

데이터베이스 서버 <-> DAO <->서비스(알고리즘) <->웹서버 <->웹 브라우저

 예전의 프로그래밍 방식은 관계형 데이터베이스의 SQL을 학습해서 데이터를 
 저장하고 Back-End용 프로그래밍언어(Java-Servlet&JSP, Php, C#-ASP.Net,
 Python, Ruby등)을 학습해서 데이터베이스 서버와 연동하고 서버쪽 알고리즘을 
 구현하고 HTML, CSS, JavaScript를 학습해서 웹 페이지를 만들어서 출력 
 
  Oracle - Java -HTML,CSS, JavaScript - Java Web Server Programming -
  AndRoid & iOS 
  
  MongoDB(자바스크립트) <-> Node.js(자바스크립트 라이브러리) <-> 
  Angular.js(서버의 데이터를 html에 바인딩 시켜주는 자바스크립트 라이브러리 -
  Vue.js, react등) <-> 웹서버를 express.js(자바스크립트 라이브러리)
  
모바일도 자바스크립트로 만들자(iOS&Android - ionic, react native, phonegap
등)
삼성의 Tizen은 자바스크립트로 앱 개발 가능 

  

  
  
  
  
  
 */
}
