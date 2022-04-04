package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
	 * *Statement(부모)와 PreparedStatement(자식)의 차이점
	 * -Statement 같은 경우 완성된 SQL 문을 바로 실행하는 객체
	 * (==SQL 문이 완전하게 완성된 형태로 셋팅되어있어야만 한다. 애초에 사용자가 입력했던 값들이 다 채워진 채로 만들어져 있어야만 함)
	 * >Connection 객체를 가지고 Statement 객체 생성: createStatement() 메소드 이용. stmt=conn.createStatement();
	 * >SQL문 실행 시: executeXXX(쿼리문) 메소드 이용. 결과=stmt.executeXXX(쿼리문); (==쿼리문 실행하는 순간 완성된 쿼리문을 넘기겠다)
	 * 
	 * -PreparedStatement 같은 경우 SQL 문을 바로 실행하지 않고 잠시 보관을 해둘 수 있음
	 * (==미완성된 SQL문을 미리 보내놓고 잠시 보관해둘 수 있음! 
	 * 단, 사용자가 입력한 값들이 들어갈 수 있는 공간을 미리 확보, ?(위치홀더)로 구멍 뚫어놓기
	 * 해당 SQL문을 실행하기 전에 완성형태로 만든 후 실행을 해야한다)
	 * >Connection 객체를 가지고 PreparedStatement 객체 생성: prepareStatement() 메소드 이용
	 *  pstmt=conn.prepareStatement(미완성된 쿼리문);
	 *  (==객체를 생성하는 순간 미완성된 쿼리문을 먼저 넘겨두겠다)
	 * >현재 담긴 SQL 문이 미완성된 SQL 문일 경우: 빈 공간을 실제 값으로 채워주는 과정(완성된 쿼리문을 넘긴 경우에는 생략 가능)
	 *  pstmt.setString(1, "실제값"); / pstmt.setInt(2, 실제값);
	 * >executeXXX() 메소드를 이용해서 SQL문을 실행
	 *  결과=pstmt.executeXXX();
	 * 
	 * *JDBC 처리 순서
	 * 1) JDBC Driver 등록: 해당 DBMS가 제공하는 클래스 등록(DriverManager)
	 * 2) Connection 객체 생성: 접속하고자 하는 DB의 정보를 입력해서 DB에 접속하면서 생성
	 * 3_1) PreparedStatement 객체 생성: Connection 객체를 이용해서 생성(애초에 SQL 문을 담은 채로 생성)
	 * 3_2) 현재 미완성된 SQL문을 완성형태로 채우는 과정=> 미완성된 경우에만 해당 / 완성된 쿼리문을 3_1단계에서 미리 보냈다면 이 단계는 생략 
	 * 4) SQL문을 실행: PreparedStatement 객체를 이용해서(매개변수 없음)
	 * 	>SELECT 문의 경우- executeQuery() 메소드를 호출해서 실행
	 * 	>INSERT, UPDATE, DELETE 문의 경우 - executeUpdate() 메소드를 호출해서 실행
	 * 5) 결과 받기
	 * 	>SELECT 문의 경우 - ResultSet 객체(조회된 데이터들이 담겨있음)로 받기=>6_1로
	 * 	>INSERT, UPDATE, DELETE 문의 경우 - int (처리된 행의 갯수)로 받기=>6_2로
	 * 6_1)SELECT : ResultSet에 담겨있는 데이터들을 하나씩 뽑아서 VO 객체에 담기(여러개일 경우 ArrayList)
	 * 6_2)INSERT, UPDATE, DELETE: 트랜잭션 처리 (성공이면 COMMIT, 실패면 ROLLBACK)
	 * 7) 다 쓴 JDBC 자원들을 반납(close)=>생성된 순서의 역순으로
	 * 8) 결과를 반환(Controller 한테)
	 * 	>SELECT문의 경우 - 6_1)에서 만들어진 VO객체 또는 ArrayList 보내기
	 * 	>INSERT, UPDATE, DELETE 문의 경우 - int(처리된 행의 갯수)
	 */
	//사용자가 회원 추가 요청 시 입력했던 값들을 가지고 INSERT 문을 실행하는 메소드
	public int insertMember(Member m) {//INSERT 문 => 처리된 행의 개수 반환=>트랜 잭션 처리
		// 0) JDBC 처리를 하기 전에 우선적으로 필요한 변수들 먼저 세팅
		int result=0; //처리된 결과(처리된 행의 개수)를 담아줄 변수
		Connection conn=null; //접속할 DB의 연결정보를 담는 변수
		PreparedStatement pstmt=null; //SQL문 실행 후 결과를 받기 위한 변수. Statement와 역할은 똑같지만 사용법은 다름
		
		//실행할 SQL 문(완성된 형태로 String으로 정의해둘 것)=>반드시 세미콜론은 떼고 넣어줄 것
		//INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, DEFAULT)
		String sql="INSERT INTO MEMBER VALUES (SEQ_USERNO.NEXTVAL,?,?,?,?,?,?,?,?,?,DEFAULT)";
		//System.out.println(sql);
		try {
			//1) JDBC Drive 등록(DriverManager)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//ojdbc6.jar 파일이 누락되거나, 잘 추가되었지만 오타가 있을 경우=>ClassNotFoundException 발생
			
			//2) Connection 객체 생성(DB와 연결->url, 계정명, 비밀번호)
			conn=DriverManager.getConnection(url, id, pw);
			
			//3_1) PreparedStatement 객체 생성
			pstmt=conn.prepareStatement(sql); //미리 sql을 넘기는 꼴
			
			//3_2) 내가 담은 SQL문이 미완성된 상태라면 값을 채워넣기
			//pstmt.setXXX(위치홀더의순번, 값) 메소드 호출 
			//=>pstmt.setString(위치홀더의순번, 값); : '값'
			//=>pstmt.setInt(위치홀더의순번, 값); : 값
			pstmt.setString(1, m.getUserId());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getGender());
			pstmt.setInt(5, m.getAge());
			pstmt.setString(6, m.getEmail());
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());
			pstmt.setString(9, m.getHobby());
			
			//PreparedStatement의 최대 장점: 쿼리 작성하기 간편해짐, 가독성 증가
			//PreparedStatement의 최대 단점: 구멍채우기가 귀찮아짐, 완정된 SQL문의 형태를 확인 불가
			
			
			//4,5) DB에 완성된 SQL 문을 실행 후 결과를 받기
			result=pstmt.executeUpdate(); //INSERT=> int (처리된 행의 개수)
			
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
				pstmt.close();
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
		PreparedStatement pstmt=null; //SQL 문 실행 후 결과를 받기 위한 변수
		ResultSet rset=null; //SELECT 문이 실행된 조회결과값들이 처음에 실질적으로 담길 변수
		
		//SELECT 문의 경우는 굳이 PreparedStatement를 사용하지 않아도 되지만
		//PreparedStatement 사용 시 완성된 쿼리문을 사용할 수도 있기 때문에 연습삼아 해보자
		
		//실행할 SQL 문(완성된 형태로 String으로 정의해둘 것)=>반드시 세미콜론은 떼고 넣어줄 것
		//SELECT * FROM MEMBER
		String sql="SELECT * FROM MEMBER";
		
		try {
			//1) JDBC Driver 등록(DriverManager)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//2) Connection 객체 생성
			conn=DriverManager.getConnection(url, id, pw);
			
			//3_1) PreparedStatement 객체 생성
			pstmt=conn.prepareStatement(sql);
			
			//3_2) 미완성된 SQL문 완성 단계
			//=>쿼리문이 완성된 형태로 넘어갔기 때문에 생략 가능
			
			//4,5) SQL 문(SELECT)을 전달해서 실행 후 결과 받기
			rset=pstmt.executeQuery();
			
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
				pstmt.close();
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
		PreparedStatement pstmt=null; //SQL 문 실행 후 결과를 받기 위한 변수
		ResultSet rset=null; //SELECT문이 실행된 조회결과값들이 처음에 실질적으로 담길 변수
		
		//실행할 SQL 문(미완성된 형태, 세미콜론 제외)
		String sql="SELECT * FROM MEMBER WHERE USERID=?";
		
		
		try {
			//1) JDBC Driver 등록(DriverManager)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//2) Connection 객체 생성
			conn=DriverManager.getConnection(url, id, pw);
			
			//3_1) PrepareStatement 객체 생성
			pstmt=conn.prepareStatement(sql);
			
			//3_2) 내가 담은 SQL문이 미완성된 상태라면 값을 채워넣기
			pstmt.setString(1, userId);
			
			//4,5) SQL 문(SELECT)을 실행 후 결과를 받기
			rset=pstmt.executeQuery();
			
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
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		//8) 결과 반환
		return m; //조회된 한명의 회원의 정보
	}
	
	//사용자의 이름 키워드로 회원 검색 요청시 SELECT 문을 실행할 메소드
	public ArrayList<Member> selectByUserName(String keyword) {
		//0) 필요한 변수들 셋팅
		//조회된 결과를 뽑아서 담아줄 ArrayList 생성(현재 텅 빈 리스트)
		ArrayList<Member> list=new ArrayList<>(); //조회된 회원들이 담김
				
		Connection conn=null; //접속할 DB의 연결정보를 담는 변수
		PreparedStatement pstmt=null; //SQL 문 실행 후 결과를 받기 위한 변수
		ResultSet rset=null; //SELECT문이 실행된 조회결과값들이 처음에 실질적으로 담길 변수
				
		//실행할 SQL 문(완성된 형태, 세미콜론 제외)
		//SELECT * FROM MEMBER WHERE USERNAME LIKE '%keyword%'
		//String sql="SELECT * FROM MEMBER WHERE USERNAME LIKE '%?%'";
		//정상적으로 수행이 되지 않는다 why? 문자열의 경우는 매꿔질 때 홑따옴표가 자동으로 들어가기때문에 '%'?'%' 이 모양이 될것임(문법 오류)
		//방법1)
		//String sql="SELECT * FROM MEMBER WHERE USERNAME LIKE '%'||?||'%'";
		//정상적으로 수행이 될것임 WHY? 문자열의 경우는 매꿔질 때 홑따옴표가 자동으로 들어가기 때문에 '%'||'?'||'%' 이 모양이 될것임
		
		//방법2)
		String sql="SELECT * FROM MEMBER WHERE USERNAME LIKE ?";
		//단, 구멍을 매꾸는 과정에서 양쪽에 %를 붙여서 매꿔줘야 함
		
		try {
			//1) JDBC Driver 등록(DriverManager)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//2) Connection 객체 생성
			conn=DriverManager.getConnection(url, id, pw);
			
			//3_1) PreparedStatement 객체 생성
			pstmt=conn.prepareStatement(sql);
			
			//3_2) 미완성된 SQL 문을 완성시키기
			//String sql="SELECT * FROM MEMBER WHERE USERNAME LIKE '%?%'";
			//정상적으로 수행이 안되는 케이스
			//pstmt.setString(1, keyword); // '%'keyword'%' 문법오류
			
			//방법1)
			//String sql="SELECT * FROM MEMBER WHERE USERNAME LIKE '%'||?||'%'";
			//정상적으로 수행이 되는 케이스
			//pstmt.setString(1, keyword); // '%'||'keyword'||'%' =>잘 실행됨
			
			//방법2)
			//String sql="SELECT * FROM MEMBER WHERE USERNAME LIKE ?";
			pstmt.setString(1, "%"+keyword+"%"); //'%keyword%' =>잘 실행됨 
									
			//4,5) SQL 문(SELECT)을 실행 후 결과를 받기
			rset=pstmt.executeQuery();
			
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
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		//8) 결과 반환
		return list;
	}
	
	//회원 변경 요청이 들어왔을 때 UPDATE 구문을 실행할 메소드
	public int updateMember(Member m) {//UPDATE 문 => int 형 반환(처리된 행의 갯수) =>트랜잭션 처리
		//0) 필요한 변수들 셋팅
		int result=0;//최종적으로 SQL 문을 실행할 결과를 담을 변수
		Connection conn=null;
		PreparedStatement pstmt=null;
		//실행할 SQL 문
		//UPDATE MEMBER SET USERPWD='m.getUserPwd', EMAIL='m.getEmail', PHONE='m.getPhone', ADDRESS='m.getAddress' WHERE USERID='m.getUserId'
		String sql="UPDATE MEMBER SET USERPWD=?, EMAIL=?, PHONE=?, ADDRESS=? WHERE USERID=?";
		try {
			//1) JDBC Driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//2) Connection 객체 생성
			conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","JDBC","JDBC");
			//3_1) PrepareStatement 객체 생성
			pstmt=conn.prepareStatement(sql);
			//3_2) 미완성된 SQL 문을 완성시키기
			pstmt.setString(1, m.getUserPwd());
			pstmt.setString(2, m.getEmail());
			pstmt.setString(3, m.getPhone());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getUserId());
			//4,5) SQL문을 실행 후 결과 받기
			result=pstmt.executeUpdate();
			//6_2) 트랜잭션 처리
			if(result>0){//result 값이 0보다 크다면=>성공(COMMIT)
				conn.commit();
			}else {
				conn.rollback();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {//7) 자원 반납
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}
		//8) 결과 반환
		return result; //UPDATE 된 행의 갯수
	}
	
	//회원 탈퇴 요청이 들어왔을 때 DELETE 구문을 실행하는 메소드
	public int deleteMember(String userId) {
		//0)필요한 변수 셋팅
		int result=0;
		Connection conn=null;
		Statement stmt=null;
		
		//실행할 SQL 문
		//DELETE FROM MEMBER WHERE USERID='';
		String sql="DELETE FROM MEMBER WHERE USERID='"+userId+"'";
		
		
		try {
			//1)JDBC Driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//2)Connection 객체 생성
			conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			
			
			//3)Statement 객체 생성
			stmt=conn.createStatement();
			
			//4,5)SQL 문 실행 후 결과 받기
			result=stmt.executeUpdate(sql);
			
			//6_2)트랜잭션 처리
			if(result>0) {
				conn.commit();
			}else {
				conn.rollback();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				//7)자원 반납
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//8)결과 반환
		return result;
	}
}
