package com.kh.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

import com.kh.common.JDBCTemplate;
import com.kh.model.vo.Product;

public class ProductDao {

	private Properties prop = new Properties();

	public ProductDao() {

		try {
			prop.loadFromXML(new FileInputStream("resources/query.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 상품 전체 조회 요청 시 SELECT 문을 실행하는 메소드
	public ArrayList<Product> selectAll(Connection conn) {

		// 0단계로 해당 매소드에서 사용하게 될 변수들을 선언해준다.
		// product 객체를 담을 ArrayList인 list
		ArrayList<Product> list = new ArrayList<Product>();
		// PreparedStatement 객체인 pstmt;
		PreparedStatement pstmt = null;
		// ResultSet 객체인 rset;
		ResultSet rset = null;

		try {
			// 매개변수로 받은 Connection 객체인 conn의 prepareStatement 메소드를 호출한다.
			// 해당 메소드의 매개변수값으로는 sql 구문을 작성해주어야 하는데
			// ProductDao의 기본생성자 생성 시 Properties 객체와 xml 파일을 연결시켜주었다.
			// 따라서 Prop.getProperty 메소드를 통해서 원하는 sql 구문이 들어있는 property 의 key 값을 입력해준다.
			pstmt = conn.prepareStatement(prop.getProperty("selectAll"));

			// rset에 select 절일 때 사용하는 executeQuery() 를 실행시킨 결과값을 담아준다.
			rset = pstmt.executeQuery();

			// next, 즉 rset에 담긴 데이터가 존재할 때 까지 반복하는 while 반복문을 작성한 뒤
			while (rset.next()) {
				// ArrayList 객체 안에 값을 더해준다.
				// new Product 생성 시, rset의 get~ 메소드를 사용해서 오라클의 컬럼값과 매칭시켜서 해당 값을 가져오게 한다.
				Product p = new Product();
				
				p.setProductId(rset.getString("product_id"));
				p.setProductName(rset.getString("Product_Name"));
				p.setPrice(rset.getInt("price"));
				p.setDescription(rset.getString("description"));
				p.setStock(rset.getInt("stock"));
				p.setCategory_name(rset.getString("category_name"));
				
				
				list.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 모든 작업이 완료된 뒤에는 사용이 끝난 ResultSet과 PreparedStatement 객체를 닫아준다.
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}

		// 값들을 담은 ArrayList list를 리턴한다.
		return list;
	}

	// 상품 추가 요청시 INSERT 문을 실행하는 메소드
	public int insertProduct(Connection conn, Product p) {
		int result = 0;

		PreparedStatement pstmt = null;

		String sql = prop.getProperty("insertProduct");

		try {

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, p.getProductId());
			pstmt.setString(2, p.getProductName());
			pstmt.setInt(3, p.getPrice());
			pstmt.setString(4, p.getDescription());
			pstmt.setInt(5, p.getStock());
			pstmt.setInt(6, p.getCategory_no());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	// 상품명 검색하기 요청 시 keyword를 이용해 SELECT문을 실행하는 메소드
	public ArrayList<Product> selectByProductName(Connection conn, String keyword) {

		// 조회된 결과를 담아줄 ArrayList 생성
		ArrayList<Product> list = new ArrayList<>();

		// SQL문 실행 후 결과를 받기 위한 변수 pstmt 선언
		PreparedStatement pstmt = null;

		// SELECT 문이 실행된 조회 결과값들이 담길 변수 rset 선언
		ResultSet rset = null;

		// prop.getProperty() 메소드로 selectByProductName라는 key값을 가진 value값을 sql변수에 담아준다.
		String sql = prop.getProperty("selectByProductName");

		try {
			// PrepareStatement 객체를 생성해서 매개변수로 sql문을 전달한다
			pstmt = conn.prepareStatement(sql);
			// 미완성된 sql문을 전달해줬기 때문에 비어있는 위치홀더에 값을 넣어준다
			pstmt.setString(1, "%" + keyword + "%");
			// 완성된 SQL 문을 실행 후 ResultSet 결과를 rset에 담는다
			rset = pstmt.executeQuery();

			// rset에 담긴 데이터가 존재할 때 까지 계속 반복을 돌려준다
			while (rset.next()) {
				// rset.getString, rset.getInt로 해당 컬럼의 데이터 타입에 맞게 데이터을 가져와 Product 객체를 생성해서 값을
				// 넣어준다
				Product p = new Product();
				
				p.setProductId(rset.getString("product_id"));
				p.setProductName(rset.getString("Product_Name"));
				p.setPrice(rset.getInt("price"));
				p.setDescription(rset.getString("description"));
				p.setStock(rset.getInt("stock"));
				p.setCategory_name(rset.getString("category_name"));
				
				// 리스트에 생성된 Product 객체를 담아준다
				list.add(p);
			}
		} catch (SQLException e) { // SQLException 예외 발생 시 처리
			e.printStackTrace();

		} finally {
			// 사용한 자원을 생성 역순으로 반납해준다
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}

		// list에 담긴 값을 호출한 곳으로 반환해준다
		return list;
	}

	// 상품 카테고리별 검색 요청 시 SELECT문을 실행하는 메소드
	public ArrayList<Product> selectByProductCategory(Connection conn, String category_name) {

		// 0. 필요변수 선언
		ArrayList<Product> list = new ArrayList<Product>(); // 결과 담을 ArrayList변수
		PreparedStatement pstmt = null; // preparedStatement 변수(SQL쿼리 실행시 필요)
		ResultSet rset = null; // SQL실행 결과값 담아주는 변수

		// 1. query.xml파일 key => productSearch에 작성된 DB쿼리문 String sql에 넣어주기
		String sql = prop.getProperty("selectByProductCategory");

		try {
			// 2. Conn객체로 pstmt객체 생성
			// .xml파일로 읽어온 DB쿼리문 담아주기
			pstmt = conn.prepareStatement(sql);

			// 3. DB쿼리문 ?(위치홀더) 값 세팅 => 쿼리 컬럼 순서에 맞게 작성해야함
			// %의 경우 ""로 따로 묶어서 + 기호로 문자 붙여쓰기
			pstmt.setString(1, category_name);

			// 4. SELECT의 경우 executeQuery()메소드 사용하여 쿼리문 실행
			rset = pstmt.executeQuery();

			// 5. rset에 조회된 결과값 0이될때까지 while반복 수행
			while (rset.next()) {

				Product p = new Product();

				// 6. Product(vo객체)에 조회된 컬럼 값들 추출
				p.setProductId(rset.getString("PRODUCT_ID"));
				p.setProductName(rset.getString("PRODUCT_NAME"));
				p.setPrice(rset.getInt("PRICE"));
				p.setDescription(rset.getString("DESCRIPTION"));
				p.setStock(rset.getInt("STOCK"));
				p.setCategory_name(rset.getString("CATEGORY_NAME"));

				// 7. 한행 씩 category_list에 추가
				list.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 8. 자원반납(생성된 역순으로 자원반납)
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return list;
	}

	// 상품 정보 수정 요청 시 UPDATE문을 실행하는 메소드
	public int updateProduct(Connection conn, Product p) {
		// 0. 변수 셋팅
		int result = 0;
		PreparedStatement pstmt = null;
		// 1,2)는 Service단에서

		try {
			// 3_1)실행할 SQL문 PreapredStatement객체 생성
			pstmt = conn.prepareStatement(prop.getProperty("updateProduct"));

			// 3_2) 미완성 SQL문 완성시키기
			pstmt.setInt(1, p.getPrice());
			pstmt.setString(2, p.getDescription());
			pstmt.setInt(3, p.getStock());
			pstmt.setString(4, p.getProductId());

			// 4,5) SQL문 실행 후 결과 받기
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 7) 자원 반납
			JDBCTemplate.close(pstmt);
		}
		// 8) Service단으로 결과 리턴
		return result;
	}

	// 상품 삭제 요청 시 UPDATE문을 실행하는 메소드
	public int updatedeleteYn(Connection conn, String productId) {

		// 필요한 변수 세팅
		int result = 0; // 리턴값을 담을 변수
		PreparedStatement pstmt = null; // PreparedStatement를 담을 변수 생성

		// query.xml의 key값이 updatedeleteYn의 값을 sql에 변수에 담아준다
		String sql = prop.getProperty("updatedeleteyn");

		try {
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);

			// updatedeleteYn의 미완성된 SQL문 완성
			pstmt.setString(1, productId);

			// SQL문 실행 후 결과 받기
			// updatedeleteYn은 UPDATE문을 실행하는 메소드 => executeUpdate
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt); // 사용한 PreparedStatement 자원 반납
		}

		// 결과 반환
		return result;
	}
	
	// 상품 삭제 취소 요청 시 SELECT문을 실행해 삭제된 상품을 보여주는 메소드
	public ArrayList<Product> selectDelete(Connection conn) {

		// 필요한 변수 세팅
		ArrayList<Product> list = new ArrayList<>(); // 리턴값을 담을 리스트 생성
		
		PreparedStatement pstmt = null; // PreparedStatement를 담을 변수 생성
		ResultSet rset = null; // ResultSet을 담을 변수 생성

		// query.xml의 key값이 selectDelete의 값을 sql에 변수에 담아준다
		String sql = prop.getProperty("selectDelete");

		try {
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// SQL문 실행 후 결과 받기
			// selectDelete은 SELECT문을 실행하는 메소드 => executeQuery
			rset = pstmt.executeQuery();
			
			// ResultSet에서 한행씩 뽑아 list에 담기
			while(rset.next()) {
				
				// Product 객체 생성
				Product p = new Product();
				
				// Product 객체에 ResultSet 값 넣어주기
				p.setProductId(rset.getString("PRODUCT_ID"));
				p.setProductName(rset.getString("PRODUCT_NAME"));
				p.setPrice(rset.getInt("PRICE"));
				p.setDescription(rset.getString("DESCRIPTION"));
				p.setStock(rset.getInt("STOCK"));
				p.setCategory_name(rset.getString("CATEGORY_NAME"));
				
				list.add(p);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);	// 사용한 ResultSet 자원 반납
			JDBCTemplate.close(pstmt); 	// 사용한 PreparedStatement 자원 반납

		}

		// 결과 반환
		return list;
	}
	
	// 상품 삭제 취소 요청 시 UPDATE문을 실행하는 메소드
	public int updateDeleteCancel(Connection conn, String productId) {

		// 필요한 변수 세팅
		int result = 0; // 리턴값을 담을 변수
		PreparedStatement pstmt = null; // PreparedStatement를 담을 변수 생성

		// query.xml의 key값이 updateDeleteCancel의 값을 sql에 변수에 담아준다
		String sql = prop.getProperty("updateDeleteCancel");

		try {
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);

			// updateDeleteCancel의 미완성된 SQL문 완성
			pstmt.setString(1, productId);

			// SQL문 실행 후 결과 받기
			// updateDeleteCancel은 UPDATE문을 실행하는 메소드 => executeUpdate
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt); // 사용한 PreparedStatement 자원 반납
		}

		// 결과 반환
		return result;
	}
	
	public int deleteProduct(Connection conn, String productId) {

		// 필요한 변수 세팅
		int result = 0; // 리턴값을 담을 변수
		PreparedStatement pstmt = null; // PreparedStatement를 담을 변수 생성

		// query.xml의 key값이 deleteProduct의 값을 sql에 변수에 담아준다
		String sql = prop.getProperty("deleteProduct");

		try {
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);

			// updateDeleteCancel의 미완성된 SQL문 완성
			pstmt.setString(1, productId);

			// SQL문 실행 후 결과 받기
			// delete문을 실행하는 메소드 => executeUpdate
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt); // 사용한 PreparedStatement 자원 반납
		}

		// 결과 반환
		return result;
	}
}
