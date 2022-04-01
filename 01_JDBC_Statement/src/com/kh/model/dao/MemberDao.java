package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.kh.model.vo.Member;

/*
 * DAO(Data Access Object)
 * Controllr로 부터 전달받은 요청 기능을 수행하기 위해서 
 * DB에 직접적으로 접근 한 후 해당 SQL을 실행하고 결과를 받아내는 역할=>실질적인 JDBC 코드 작성
 * 추가적으로 결과값을 가공하거나, 성공 실패 여부에 따라서 트랜잭션 처리
 * 결과를 Controller로 리턴해줌
 */
public class MemberDao {
	private String url="jdbc:oracle:thin:@localhost:1521:xe";
	private String id="JDBC";
	private String pw="JDBC";
	/*
	 * JDBC용 객체
	 * -Connection: DB의 연결정보를 담고 있는 객체
	 * -(Prepared)Statement: 해당 DB에 SQL 문을 전달하고 실행한 후 결과를 받아내는 객체
	 * -ResultSet: 만일 내가 실행한 SQL문이 SELECT 문일 경우 조회된 결과들이 담겨있는 객체
	 * 
	 * JDBC 처리 순서
	 * 1)JDBC Driver 등록: 해당 DBMS가 제공하는 클래스를 등록(DriverManager)
	 * 2)Connection 객체 생성: 접속하고자 하는 DB 정보를 입력해서 DB에 접속하면서 생성
	 * 3)Statement 객체 생성: Connection 객체를 통해서 생성
	 * 4)SQL 문을 전달하면서 실행: Statement 객체를 이용해서 SQL 문 실행
	 * 	>SELECT: executeQuery() 메소드를 호출하여 실행
	 * 	>INSERT, UPDATE, DELETE: executeUpdate() 메소드를 호출하여 실행
	 * 5)결과 받기
	 * 	>SELECT: ResultSet 객체로 받기(조회된 데이터들이 담겨있음) =>6_1)로
	 * 	>INSERT, UPDATE, DELETE: int로 받기 (처리된 행의 개수가 담겨있음)=>6_2)로
	 * 6)결과에 따른 후처리
	 * 6_1)SELECT: ResultSet에 담겨있는 데이터들을 하나씩 뽑아서 VO 객체에 담기(여러명이면 ArrayList 사용)
	 * 6_2)INSERT,UPDATE,DELETE: 트랜잭션 처리(성공이면 COMMIT, 실패면 ROLLBACK)
	 * 7)자원반납(.close() 메소드 사용): 생성된 순서의 역순으로 
	 * 8)결과 반환(Controller 한테)
	 * 	>SELECT: 6_1) 만들어진 결과
	 * 	>INSERT,UPDATE,DELETE: int(처리된 행의 개수)
	 */
	//사용자가 회원 추가 요청 시 입력했던 값들을 가지고 INSERT 문을 실행하는 메소드
	public int insertMember(Member m) {//INSERT 문 => 처리된 행의 개수 반환=>트랜 잭션 처리
		// 0) JDBC 처리를 하기 전에 우선적으로 필요한 변수들 먼저 세팅
		int result=0; //처리된 결과(처리된 행의 개수)를 담아줄 변수
		Connection conn=null; //접속할 DB의 연결정보를 담는 변수
		Statement stmt=null; //SQL문 실행 후 결과를 받기 위한 변수
		
		//실행할 SQL 문(완성된 형태로 String으로 정의해둘 것)=>반드시 세미콜론은 떼고 넣어줄 것
		//INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL, '아이디', '비밀번호', '이름', '성별', 나이, '이메일', '휴대폰번호', '주소', '취미', DEFAULT)
		String sql="INSERT INTO MEMBER VALUES "
						+"(SEQ_USERNO.NEXTVAL,"
						+"'"+m.getUserId()+"',"
						+"'"+m.getUserPwd()+"',"
						+"'"+m.getUserName()+"',"
						+"'"+m.getGender()+"',"
						+m.getAge()+","
						+"'"+m.getEmail()+"',"
						+"'"+m.getPhone()+"',"
						+"'"+m.getAddress()+"',"
						+"'"+m.getHobby()+"',"
						+"DEFAULT)";
		//System.out.println(sql);
		try {
			//1) JDBC Drive 등록(DriverManager)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//ojdbc6.jar 파일이 누락되거나, 잘 추가되었지만 오타가 있을 경우=>ClassNotFoundException 발생
			
			//2) Connection 객체 생성(DB와 연결->url, 계정명, 비밀번호)
			conn=DriverManager.getConnection(url, id, pw);
			
			//3) Statement 객체 생성(Connection 객체를 이용해서 생성)
			stmt=conn.createStatement();
			
			//4,5) DB에 완성된 SQL 문을 전달하면서 실행 후 결과를 받기
			result=stmt.executeUpdate(sql); //INSERT=> int (처리된 행의 개수)
			
			//6_2) 트랜잭션 처리
			if(result>0) { //성공
				conn.commit(); //COMMIT 처리
			}else { //실패
				conn.rollback(); //ROLLBACK 처리
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//7) 다 쓴 JDBC 자원 반납=> 객체 생성의 역순으로 close
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//8 ) 결과 반환
		return result;
	}
	
	//사용자의 회원 전체 요청 시 SELECT 문을 실행할 메소드
	public ArrayList<Member> selectAll() { //SELECT=>ResultSet 객체(여러 행 조회)
		//0) 필요한 변수들 셋팅
		//조회된 결과를 뽑아서 담아줄 ArrayList 생성(현재 텅 빈 리스트)
		ArrayList<Member> list=new ArrayList<>(); //조회된 회원들이 담김
		
		Connection conn=null; //접속할 DB의 연결정보를 담는 변수
		Statement stmt=null; //SQL 문 실행 후 결과를 받기 위한 변수
		ResultSet rset=null; //SELECT 문이 실행된 조회결과값들이 처음에 실질적으로 담길 변수
		
		//실행할 SQL 문(완성된 형태로 String으로 정의해둘 것)=>반딋 세미콜론은 떼고 넣어줄 것
		//SELECT * FROM MEMBER
		String sql="SELECT * FROM MEMBER";
		
		try {
			//1) JDBC Driver 등록(DriverManager)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//2) Connection 객체 생성
			conn=DriverManager.getConnection(url, id, pw);
			
			//3) Statement 객체 생성
			stmt=conn.createStatement();
			
			//4,5) SQL 문(SELECT)을 전달해서 실행 후 결과 받기
			rset=stmt.executeQuery(sql);
			
			//6_1) 현재 조회 결과가 담긴 ResultSet에서 한행씩 뽑아서 VO 객체에 담기
			//=>커서를 한 행 한 행 씩 아래로 옮겨서 현재 행의 위치를 나타낸다
			//=>이 때 커서는 rset.next() 메소드로 다음 줄로 넘겨버린다
			//rset.next() : 커서를 한 행 밑으로 움직여주고 해당 행이 존재할 경우 true, 존재하지 않을 경우 false를 반환
			while(rset.next()) { //더이상 뽑아낼 게 없을 때 까지 반복을 돌리겠다
				//뽑아낼 게 있다면
				//모든 컬럼에 대해서 값을 일일이 다 뽑아야 한다
				//Member 테이블에는 11개의 컬럼이 있음
				
				//현재 rset의 커서가 가리키고 있는 해당 행의 데이터를 하나씩 뽑아서 Member 객체에 담는다
				Member m=new Member();
				//rset으로부터 어떤 컬럼에 해당하는 값을 뽑을건지 제시
				//rset.getInt(컬럼명 또는 컬럼의 순번) : int 타입으로 값을 뽑아줌
				//rset.getString(컬럼명 또는 컬럼의 순번) : String 타입으로 값을 뽑아줌
				//rset.getDate(컬럼명 또는 컬럼의 순번) : Date 타입으로 값을 뽑아줌
				//=>권장사항: 컬럼명으로 꼭 쓸 것. 컬럼명 작성 시 대문자로 작성(소문자도 가능)
				m.setUserNo(rset.getInt("USERNO")); //rset.getInt(1)
				m.setUserId(rset.getString("USERID")); //rset.getString(2)
				m.setUserPwd(rset.getString("USERPWD")); //rset.getString(3)
				m.setUserName(rset.getString("USERNAME")); 
				m.setGender(rset.getString("GENDER"));
				m.setAge(rset.getInt("AGE"));
				m.setEmail(rset.getString("EMAIL"));
				m.setPhone(rset.getString("PHONE"));
				m.setAddress(rset.getString("ADDRESS"));
				m.setHobby(rset.getString("HOBBY"));
				m.setEnrollDate(rset.getDate("ENROLLDATE"));
				//한 행에 대한 모든 데이터값들을 하나의 Member 객체에 옮겨담기 끝
				
				//리스트에 해당 Member 객체를 add
				list.add(m);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//7) 자원 반납(생성된 순서의 역순)
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		//8) 결과 반환
		return list;
	}
	
	//사용자의 아이디로 회원 검색 요청시 SELECT 문을 실행할 메소드
	public Member selectByUserId(String userId) {//SELECT 문=>ResultSet 객체(한 행 조회)
		//0) 필요한 변수들 셋팅
		//조회된 한 회원에 대한 정보를 담을 변수
		Member m=null;
		
		Connection conn=null; //접속할 DB의 연결정보를 담는 변수
		Statement stmt=null; //SQL 문 실행 후 결과를 받기 위한 변수
		ResultSet rset=null; //SELECT문이 실행된 조회결과값들이 처음에 실질적으로 담길 변수
		
		//실행할 SQL 문(완성된 형태, 세미콜론 제외)
		String sql="SELECT * FROM MEMBER WHERE USERID='"+userId+"'";
		
		
		try {
			//1) JDBC Driver 등록(DriverManager)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//2) Connection 객체 생성
			conn=DriverManager.getConnection(url, id, pw);
			
			//3) Statement 객체 생성
			stmt=conn.createStatement();
			
			//4,5) SQL 문(SELECT)을 전달해서 실행 후 결과를 받기
			rset=stmt.executeQuery(sql);
			
			//6_1) 현재 조회 결과가 담긴 ResultSet에서 한행씩 뽑아서 VO 객체에 담기
			//만약 next() 메소드 실행 후 뽑아낼게 있다면 true 반환
			if(rset.next()) {//어차피 unique 제약조건에 의해 최대 1개의 행만 조회될것이기 때문에 if를 씀
				//조회된 한 행에 대한 데이터값들을 뽑아서 하나의 Member 객체에 담기
				m=new Member(rset.getInt("USERNO"),
							rset.getString("USERID"),
							rset.getString("USERPWD"),
							rset.getString("USERNAME"),
							rset.getString("GENDER"),
							rset.getInt("AGE"),
							rset.getString("EMAIL"),
							rset.getString("PHONE"),
							rset.getString("ADDRESS"),
							rset.getString("HOBBY"),
							rset.getDate("ENROLLDATE"));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//7) 다 쓴 JDBC 객체 반납(생성된 순서의 역순)
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		//8) 결과 반환
		return m; //조회된 한명의 회원의 정보
	}
	
	//사용자의 이름 키워드로 회원 검색 요청시 SELECT 문을 실행할 메소드
	public ArrayList<Member> selectByUserName(String userName) {
		//0) 필요한 변수들 셋팅
		//조회된 결과를 뽑아서 담아줄 ArrayList 생성(현재 텅 빈 리스트)
		ArrayList<Member> list=new ArrayList<>(); //조회된 회원들이 담김
				
		Connection conn=null; //접속할 DB의 연결정보를 담는 변수
		Statement stmt=null; //SQL 문 실행 후 결과를 받기 위한 변수
		ResultSet rset=null; //SELECT문이 실행된 조회결과값들이 처음에 실질적으로 담길 변수
				
		//실행할 SQL 문(완성된 형태, 세미콜론 제외)
		String sql="SELECT * FROM MEMBER WHERE USERNAME LIKE '%"+userName+"%'";
		
		try {
			//1) JDBC Driver 등록(DriverManager)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//2) Connection 객체 생성
			conn=DriverManager.getConnection(url, id, pw);
			
			//3) Statement 객체 생성
			stmt=conn.createStatement();
			
			//4,5) SQL 문(SELECT)을 전달해서 실행 후 결과를 받기
			rset=stmt.executeQuery(sql);
			
			//6_1) 현재 조회 결과가 담긴 ResultSet에서 한행씩 뽑아서 VO 객체에 담기
			while(rset.next()) {
				Member m=new Member(rset.getInt("USERNO"),
						rset.getString("USERID"),
						rset.getString("USERPWD"),
						rset.getString("USERNAME"),
						rset.getString("GENDER"),
						rset.getInt("AGE"),
						rset.getString("EMAIL"),
						rset.getString("PHONE"),
						rset.getString("ADDRESS"),
						rset.getString("HOBBY"),
						rset.getDate("ENROLLDATE"));
				list.add(m);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//7) 다 쓴 JDBC 객체 반납(생성된 순서의 역순)
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		//8) 결과 반환
		return list;
	}
}
