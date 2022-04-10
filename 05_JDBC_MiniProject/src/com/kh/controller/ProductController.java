package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.service.ProductService;
import com.kh.model.vo.Product;
import com.kh.view.ProductView;

public class ProductController {

	private ProductService ps = new ProductService();
	
	// 상품 전체 조회 처리 메소드
	public void selectAll() {
			
		// productService 의 selectAll 의 리턴값으로 받을 ArrayList를 생성.
		ArrayList<Product> list = ps.selectAll();
		
		// 리스트의 사이즈가 0 이상, 즉 리스트에 데이터가 한 개라도 들어있으면
		if (list.size() > 0) {
			
			// 데이터의 정보를 출력하는 productView 클래스의 displatData(매개변수 리스트) 를 생성하고
			new ProductView().displayList(list);
		} else {
			// 사이즈가 0, 즉 혹시라도 데이터가 안들어있다면(오타의 경우도 이쪽으로 들어오게됨) 데이터가 없음을 나타내는 
			// productView 클래스의 displatNoData를 출력한다.
			new ProductView().displayNodata("조회 결과가 없습니다.");
		}
	}
	
	// 상품 추가 처리 메소드
	public void insertProduct(String productId, String productName, int price, String description, int stock, int category_no) {
		
		Product p = new Product(productId, productName, price, description, stock, category_no);
		
		int result = new ProductService().insertProduct(p);
		
		if(result > 0) {
			new ProductView().displaySuccess("상품 추가 성공");
		}
		else {
			new ProductView().displayFail("상품 추가 실패");
		}
	}
	
	// 상품명 검색 처리 메소드
	public void selectByProductName(String keyword) {
		
		// ProductService의 selectByProductName 메소드의 반환값을 받을 ArrayList 생성
		// selectByProductName메소드에 매개변수로 keyword 전달
		ArrayList<Product> list=new ProductService().selectByProductName(keyword);
		
		if(list.isEmpty()) { //list에 값이 비어있을 경우.=>keyword로 조회한 결과가 없을 경우 출력
			new ProductView().displayNodata("조회 결과를 찾을 수 없습니다");
		}else {//list에 값이 비어있지 않을 경우.=>keyword로 조회한 결과를 출력
			new ProductView().displayList(list);
		}
	}
	
	// 상품 카테고리별 검색 처리 메소드
	public void selectByProductCategory(String category_name) {
		
		// 0. 필요 변수 선언 = > select => 조회된 결과값 ArrayList<Product>타입으로로 받기 
		ArrayList<Product> list = new ArrayList<Product>();
		
		// 1. Service단 호출(Connection객체 생성)및 넘어온 결과값 category_list에 담기
		list = new ProductService().selectByProductCategory(category_name);
		
		// 2. 조회된 결과값 category_list (있/없)확인하고 상황에 맞는 메소드 호출
		if(!list.isEmpty()) {
			//비어있지 않을 경우 => select성공
			new ProductView().displayList(list);
		} else {
			//비어있는 경우 => select된 결과값 없음 메세지 FailDisplay()메소드로 넘김
			new ProductView().displayNodata("조회된 상품이 없습니다");
		}
	}
	
	// 상품 정보 수정 처리 메소드
	public void updateProduct(String productId, int price, String description, int stock) {
		
		// 0. VO객체로 가공하기
		Product p = new Product();
		p.setProductId(productId);
		p.setPrice(price);
		p.setDescription(description);
		p.setStock(stock);
		
		// 2) Service 메소드 호출하여 객체를 매개변수에 담아 전달
		int result = ps.updateProduct(p);
		
		// 3) 조회결과에 따른 화면 출력 메소드 (View단으로보내기)
		if (result > 0) {
			new ProductView().displaySuccess("성공적으로 수정되었습니다.");
		} else {
			new ProductView().displayFail("수정에 실패했습니다.");
		}
	}
	
	// 상품 삭제 처리 메소드
	public void updatedeleteYn(String productId) {
		
		// Service의 updatedeleteYn 메소드 호출
		// UPDATE 실행 => 처리된 행 개수 반환 = int형으로 리턴값 받기
		int result = new ProductService().updatedeleteYn(productId);
		
		// 응답뷰 지정
		if(result > 0) { // result가 0 초과 => 상품 삭제 성공
			new ProductView().displaySuccess("상품 삭제 성공");
		} 
		else { // result가 0 이하 => 상품 삭제 실패
			new ProductView().displayFail("상품 삭제 실패");
		}
	} 
	
	// 상품 삭제 취소 메소드
	// 삭제 상품 조회 처리 메소드
	public void selectDelete() {
			
		// Service의 selectDelete 메소드 호출
		// SELECT 실행 => ArrayList로 리턴값 받기
		ArrayList<Product> list = ps.selectDelete();
		
		// 리스트의 사이즈가 0 이상, 즉 리스트에 데이터가 한 개라도 들어있으면
		if (list.size() > 0) { // list의 크기가 0 초과 => 삭제된 상품 조회 성공
			new ProductView().displayList(list);
		} 
		else { // list의 크기가 0 이하 => 삭제된 상품 조회 실패
			new ProductView().displayNodata("삭제된 상품이 존재하지 않습니다.");
		}
	}
	
	// 상품 삭제 처리 메소드
	public void updateDeleteCancel(String productId) {
		
		// Service의 updateDeleteCancel 메소드 호출
		// UPDATE 실행 => 처리된 행 개수 반환 = int형으로 리턴값 받기
		int result = new ProductService().updateDeleteCancel(productId);
		
		// 응답뷰 지정
		if(result > 0) { // result가 0 초과 => 상품 삭제 취소 성공
			new ProductView().displaySuccess("상품 삭제 취소");
		} 
		else { // result가 0 이하 => 상품 삭제 실패
			new ProductView().displayFail("상품 삭제 취소 실패");
		}
	}
	
	public void deleteProduct(String productId) {
		
		// Service의 deleteProduct 메소드 호출
		// delete 실행 => 처리된 행 개수 반환 = int형으로 리턴값 받기
		int result = new ProductService().deleteProduct(productId);
		
		// 응답뷰 지정
		if(result > 0) { // result가 0 초과 => 상품 삭제 취소 성공
			new ProductView().displaySuccess("상품 최종 삭제 완료");
		} 
		else { // result가 0 이하 => 상품 삭제 실패
			new ProductView().displayFail("상품 최종 삭제 실패");
		}
	} 
}
