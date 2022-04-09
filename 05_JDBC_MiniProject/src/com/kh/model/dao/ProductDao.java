package com.kh.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.common.JDBCTemplate;
import com.kh.model.vo.Product;

public class ProductDao {
	private Properties prop=new Properties();
	
	public ProductDao() {
		try {
			prop.loadFromXML(new FileInputStream("resources/query.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//0. 상품 id로 조회
	public Product selectById(Connection conn, String productId) {
		Product p=null;
		PreparedStatement pstmt=null;
		ResultSet rset=null;
		
		String sql=prop.getProperty("selectById");
		
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, productId);
			rset=pstmt.executeQuery();
			if(rset.next()) {
				p=new Product(rset.getString("PRODUCT_ID"),rset.getString("PRODUCT_NAME"),rset.getInt("PRICE"),rset.getString("DESCRIPTION"),rset.getInt("STOCK"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return p;
	}
	//1. 전체 조회 하기
	public ArrayList<Product> selectAll(Connection conn){
		ArrayList<Product> list=new ArrayList<>();
		PreparedStatement pstmt=null;
		ResultSet rset=null;
		
		String sql=prop.getProperty("selectAll");
		
		try {
			pstmt=conn.prepareStatement(sql);
			rset=pstmt.executeQuery();
			
			while(rset.next()) {
				Product p=new Product(rset.getString("PRODUCT_ID"),rset.getString("PRODUCT_NAME"),rset.getInt("PRICE"),rset.getString("DESCRIPTION"),rset.getInt("STOCK"));
				list.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return list;
	}
	//2. 상품 추가 하기
	public int insertProduct(Connection conn, Product p) {
		int result=0;
		PreparedStatement pstmt=null;
		
		String sql=prop.getProperty("insertProduct");
		
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, p.getProductId());
			pstmt.setString(2, p.getProductName());
			pstmt.setInt(3, p.getPrice());
			pstmt.setString(4, p.getDescription());
			pstmt.setInt(5, p.getStock());
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	//3. 상품 이름으로 키워드 검색
	public ArrayList<Product> selectByProduct(Connection conn, String keyword){
		ArrayList<Product> list=new ArrayList<>();
		PreparedStatement pstmt=null;
		ResultSet rset=null;
		
		String sql=prop.getProperty("selectByProduct");
		
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, "%"+keyword+"%");
			rset=pstmt.executeQuery();
			
			while(rset.next()) {
				Product p=new Product(rset.getString("PRODUCT_ID"),rset.getString("PRODUCT_NAME"),rset.getInt("PRICE"),rset.getString("DESCRIPTION"),rset.getInt("STOCK"));
				list.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return list;
	}
	//4. 상품 정보 수정 하기
	public int updateProduct(Connection conn, Product p) {
		int result=0;
		PreparedStatement pstmt=null;
		
		String sql=prop.getProperty("updateProduct");
		
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, p.getProductName());
			pstmt.setInt(2, p.getPrice());
			pstmt.setString(3, p.getDescription());
			pstmt.setInt(4, p.getStock());
			pstmt.setString(5, p.getProductId());
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	//5. 상품 삭제 하기
	public int deleteProduct(Connection conn, String productId) {
		int result=0;
		PreparedStatement pstmt=null;
		
		String sql=prop.getProperty("deleteProduct");
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, productId);
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
}
