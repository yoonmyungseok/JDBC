package com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import com.kh.controller.ProductController;
import com.kh.model.vo.Product;

public class ProductView {
	//Scanner 객체 전역으로 생성
	private Scanner sc=new Scanner(System.in);
	
	//컨트롤러 객체 전역으로 생성
	private ProductController pc=new ProductController();
	
	//메뉴 화면
	public void mainMenu() {
		while(true) {
			System.out.println("***** 메뉴 구성 *****");
			System.out.println("1. 상품 전체 조회 하기");
			System.out.println("2. 상품 추가 하기");
			System.out.println("3. 상품명 검색 하기 (상품 이름으로 키워드 검색)");
			System.out.println("4. 상품 정보 수정 하기 (상품 id로 조회하고 수정");
			System.out.println("5. 상품 삭제 하기 (상품 id로 조회해서 삭제)");
			System.out.println("0. 프로그램 종료하기");
			System.out.println("-------------------------------------------------");
			System.out.print("메뉴 입력: ");
			int menu=sc.nextInt();
			sc.nextLine();
			
			switch(menu){
			case 1: selectAll(); break;
			case 2: insertProduct(); break;
			case 3: selectByProduct(); break;
			case 4: updateProduct(); break;
			case 5: deleteProduct(); break;
			case 0: System.out.println("프로그램을 종료합니다."); return;
			default: System.out.println("번호를 잘못 입력했습니다. 다시 입력해주세요.");
			}
		}
	}
	//1. 상품 전체 조회 하기
	public void selectAll() {
		System.out.println("상품 전체 조회");
		pc.selectAll();
	}
	//2. 상품 추가 하기
	public void insertProduct() {
		System.out.println("상품 추가 하기");
		System.out.print("상품 아이디: ");
		String productId=sc.nextLine();
		System.out.print("상품명: ");
		String productName=sc.nextLine();
		System.out.print("상품 가격: ");
		int price=sc.nextInt();
		sc.nextLine();
		System.out.print("상품 상세정보: ");
		String description=sc.nextLine();
		System.out.print("재고: ");
		int stock=sc.nextInt();
		sc.nextLine();
		pc.insertProduct(productId,productName,price,description,stock);
	}
	//3. 상품 이름으로 키워드 검색
	public void selectByProduct() {
		System.out.println("상품명 검색 하기");
		System.out.print("상품 키워드: ");
		String keyword=sc.nextLine();
		pc.selectByProduct(keyword);
	}
	//4. 상품 id로 조회하고 수정
	public void updateProduct() {
		System.out.println("상품 정보 수정 하기");
		System.out.print("수정할 상품의 id: ");
		String productId=sc.nextLine();
		pc.selectById(productId);
		System.out.print("수정할 상품명: ");
		String productName=sc.nextLine();
		System.out.print("수정할 가격: ");
		int price=sc.nextInt();
		sc.nextLine();
		System.out.print("수정할 상세정보: ");
		String description=sc.nextLine();
		System.out.print("수정할 재고: ");
		int stock=sc.nextInt();
		sc.nextLine();
		pc.updateProduct(productId,productName, price, description, stock);
	}
	//5. 상품 id로 조회해서 삭제
	public void deleteProduct() {
		System.out.println("상품 삭제 하기");
		System.out.print("삭제할 상품의 id: ");
		String productId=sc.nextLine();
		pc.selectById(productId);
		pc.deleteProduct(productId);
	}
	//-----------------------------------------
	//조회 성공 시 출력
	public void displayProduct(ArrayList<Product> list) {
		System.out.println("조회된 결과는 총 "+list.size()+"건입니다");
		for(int i=0; i<list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
	//id로 조회 성공시 출력
	public void displayById(Product p) {
		System.out.println(p);
	}
	//조회 실패시 출력
	public void displayNodata(String message) {
		System.out.println(message);
	}
	//성공 메시지 출력
	public void displaySuccess(String message) {
		System.out.println("서비스 요청에 성공했습니다."+message);
	}
	//실패 메시지 출력
	public void displayFail(String message) {
		System.out.println("서비스 요청에 실패했습니다."+message);
	}
}
