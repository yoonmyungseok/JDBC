package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.service.ProductService;
import com.kh.model.vo.Product;
import com.kh.view.ProductView;

public class ProductController {
	//0. id로 조회
	public void selectById(String productId) {
		Product p=new ProductService().selectById(productId);
		if(p==null) {
			new ProductView().displayNodata("전체 조회 결과를 찾을 수 없습니다");
		}else {
			new ProductView().displayById(p);
		}		
	}
	//1. 상품 전체 조회
	public void selectAll() {
		ArrayList<Product> list=new ProductService().selectAll();
		if(list.isEmpty()) {
			new ProductView().displayNodata("전체 조회 결과를 찾을 수 없습니다");
		}else {
			new ProductView().displayProduct(list);
		}
	}
	//2. 상품 추가 하기
	public void insertProduct(String productId,String productName, int price, String description, int stock) {
		Product p=new Product(productId, productName, price, description, stock);
		int result=new ProductService().insertProduct(p);
		if(result>0) {
			new ProductView().displaySuccess("게시글 추가 성공");
		}else {
			new ProductView().displayFail("게시글 추가 실패");
		}
	}
	//3. 상품 이름으로 키워드 검색
	public void selectByProduct(String keyword) {
		ArrayList<Product> list=new ProductService().selectByProduct(keyword);
		if(list.isEmpty()) {
			new ProductView().displayNodata("조회 결과를 찾을 수 없습니다");
		}else {
			new ProductView().displayProduct(list);
		}
	}
	//4. 상품 id로 조회하고 수정
	public void updateProduct(String productId,String productName, int price, String description, int stock) {
		//1) VO 객체로 가공
		Product p=new Product();
		p.setProductId(productId);
		p.setProductName(productName);
		p.setPrice(price);
		p.setDescription(description);
		p.setStock(stock);
		
		int result=new ProductService().updateProduct(p);
		if(result>0) {
			new ProductView().displaySuccess("상품 정보 수정 성공");
		}else {
			new ProductView().displayFail("상품 정보 수정 실패");
		}
	}
	//5. 상품 id로 조회해서 삭제
	public void deleteProduct(String productId) {
		int result=new ProductService().deleteProduct(productId);
		if(result>0) {
			new ProductView().displaySuccess("상품 삭제 성공");
		}else {
			new ProductView().displayFail("상품 삭제 실패");
		}
	}
}
