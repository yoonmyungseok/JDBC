package com.kh.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.common.JDBCTemplate;
import com.kh.model.dao.ProductDao;
import com.kh.model.vo.Product;

public class ProductService {
	//0. 상품 id로 조회
	public Product selectById(String productId) {
		Connection conn=JDBCTemplate.getConnection();
		Product p=new ProductDao().selectById(conn, productId);
		return p;
	}
	//1. 전체 조회 하기
	public ArrayList<Product> selectAll(){
		Connection conn=JDBCTemplate.getConnection();
		ArrayList<Product> list=new ProductDao().selectAll(conn);
		JDBCTemplate.close(conn);
		return list;
	}
	//2. 상품 추가 하기
	public int insertProduct(Product p) {
		Connection conn=JDBCTemplate.getConnection();
		int result=new ProductDao().insertProduct(conn,p);
		JDBCTemplate.close(conn);
		return result;
	}
	//3. 상품 이름으로 키워드 검색
	public ArrayList<Product> selectByProduct(String keyword){
		Connection conn=JDBCTemplate.getConnection();
		ArrayList<Product> list=new ProductDao().selectByProduct(conn,keyword);
		JDBCTemplate.close(conn);
		return list;
	}
	//4. 상품 정보 수정 하기
	public int updateProduct(Product p) {
		Connection conn=JDBCTemplate.getConnection();
		int result=new ProductDao().updateProduct(conn,p);
		JDBCTemplate.close(conn);
		return result;
	}
	//5. 상품 삭제 하기
	public int deleteProduct(String productId) {
		Connection conn=JDBCTemplate.getConnection();
		int result=new ProductDao().deleteProduct(conn, productId);
		JDBCTemplate.close(conn);
		return result;
	}
}
