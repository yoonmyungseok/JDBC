package com.kh.model.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.kh.common.JDBCTemplate;
import com.kh.model.dao.ProductDao;
import com.kh.model.vo.Product;


public class ProductService {

	public ArrayList<Product> selectAll(){
			
		// service 단에서는 Connection 객체와 관련된 부분을 다룬다.
		
		// 객체인 conn을 미리 생성해준 JDBCTemplate.getConnection()을 통해 
		// DB와 연결해준다.
		Connection conn = JDBCTemplate.getConnection();
		
		// 그 후 ProductDao 클래스의 selectAll 메소드의 리턴 값을 담을 ArrayList 를 생성해준다.
		ArrayList<Product> list = new ProductDao().selectAll(conn);
		
		// 다 사용한 connection 객체를 닫아준다.
		JDBCTemplate.close(conn);
		
		return list;
	}
	
	public int insertProduct(Product p) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new ProductDao().insertProduct(conn, p);
		
		if(result > 0) { // 성공 => COMMIT
			
			JDBCTemplate.commit(conn);
		}
		else { // 실패 => ROLLBACK
			
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}
	
	public ArrayList<Product> selectByProductName(String keyword){
		
		//Connection 객체 생성
		Connection conn=JDBCTemplate.getConnection();
		
		// ProductDao의 selectByProductName 메소드의 반환값을 받을 Product타입의 ArrayList 생성
		// selectByProductName메소드로 conn, keyword를 매개변수로 전달한다
		ArrayList<Product> list=new ProductDao().selectByProductName(conn,keyword);
		
		//SELECT문은 트랜잭션처리를 하지 않는다
		
		//사용한 Connection 객체를 반납해준다
		JDBCTemplate.close(conn);
		
		//결과값을 ProductController로 리턴해준다
		return list;
	}
	
	public ArrayList<Product> selectByProductCategory(String category_name){
		
		// 0. DriverManager생성위해 Connection객체 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 1. DAO단 호출 => 결과값 ArrayList<Product>에 담아주기 
		ArrayList<Product> list = new ProductDao().selectByProductCategory(conn, category_name);
		
		// 2. Connection 객체 자원반납
		JDBCTemplate.close(conn);
		
		// 3. 결과값 Controller단에 넘겨주기  
		return list;
	}
	
	public int updateProduct(Product p) {
		
		// 1) Connection 객체 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2) DAO호출하여 result에 담아 (conn, 전달값) 보내주기
		int result = new ProductDao().updateProduct(conn, p);
		
		// 3) 트랜잭션 처리 
		if (result > 0) { // 결과가 있음, 성공 했을 경우
			JDBCTemplate.commit(conn);
		} else {// 실패했을 경우 ROLLBACK
			JDBCTemplate.rollback(conn);
		}
		
		// 4) Connection 객체 반납 
		JDBCTemplate.close(conn);
		
		// 5) 결과를 Controller단에 리턴
		return result;
	}

	public int updatedeleteYn(String productId) {
		
		// 1) Connection 객체 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2) DAO의 updatedeleteYn 메소드 호출
		//    conn과 productId를 매개변수로 전달
		//    메소드의 결과는 처리된 행의 개수로 반환 => int형의 변수로 리턴값을 담는다
		int result = new ProductDao().updatedeleteYn(conn, productId);
		
		// 3) 트랜잭션 처리
		if(result > 0) { // result가 0 초과 = 성공 => COMMIT
			JDBCTemplate.commit(conn);
		}
		else { // result가 0 이하 = 실패 => ROLLBACK
			JDBCTemplate.rollback(conn);
		}
		
		// 4) Connection 객체 반납
		JDBCTemplate.close(conn);
		
		// 5) Controller에 결과 전송
		return result;
	}
	
	public ArrayList<Product> selectDelete(){
		
		// 1) Connection 객체 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2) DAO의 selectDelete 메소드 호출
		//    메소드의 결과는 리스트 형식으로 반환 => ArrayList로 리턴값을 담는다
		ArrayList<Product> list = new ProductDao().selectDelete(conn);
		
		// 3) 트랜잭션 처리 => 생략
		
		// 4) Connection 객체 반납
		JDBCTemplate.close(conn);
		
		// 5) Controller에 결과 전송
		return list;
	}
	
	public int updateDeleteCancel(String productId) {
		
		// 1) Connection 객체 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2) DAO의 updateDeleteCancel 메소드 호출
		//    conn과 productId를 매개변수로 전달
		//    메소드의 결과는 처리된 행의 개수로 반환 => int형의 변수로 리턴값을 담는다
		int result = new ProductDao().updateDeleteCancel(conn, productId);
		
		// 3) 트랜잭션 처리
		if(result > 0) { // result가 0 초과 = 성공 => COMMIT
			JDBCTemplate.commit(conn);
		}
		else { // result가 0 이하 = 실패 => ROLLBACK
			JDBCTemplate.rollback(conn);
		}
		
		// 4) Connection 객체 반납
		JDBCTemplate.close(conn);
		
		// 5) Controller에 결과 전송
		return result;
	}
	
	public int deleteProduct(String productId) {
		
		// 1) Connection 객체 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2) DAO의 deleteProduct 메소드 호출
		//    conn과 productId를 매개변수로 전달
		//    메소드의 결과는 처리된 행의 개수로 반환 => int형의 변수로 리턴값을 담는다
		int result = new ProductDao().deleteProduct(conn, productId);
		
		// 3) 트랜잭션 처리
		if(result > 0) { // result가 0 초과 = 성공 => COMMIT
			JDBCTemplate.commit(conn);
		}
		else { // result가 0 이하 = 실패 => ROLLBACK
			JDBCTemplate.rollback(conn);
		}
		
		// 4) Connection 객체 반납
		JDBCTemplate.close(conn);
		
		// 5) Controller에 결과 전송
		return result;
	}
}
