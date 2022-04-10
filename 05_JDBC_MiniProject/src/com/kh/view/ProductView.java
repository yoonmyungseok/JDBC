package com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.kh.controller.ProductController;
import com.kh.model.vo.Product;

public class ProductView {

	private Scanner sc = new Scanner(System.in);
	private ProductController pc = new ProductController();

	public void mainMenu() {

		while (true) {

			System.out.println("************* 5조 상점 *************");
			System.out.println("1. 상품 전체 조회하기");
			System.out.println("2. 상품 추가하기");
			System.out.println("3. 상품명으로 검색하기");
			System.out.println("4. 카테고리로 검색하기");
			System.out.println("5. 상품 정보 수정하기");
			System.out.println("6. 상품 삭제하기");
			System.out.println("7. 상품 삭제 취소하기");
			System.out.println("8. 상품 완전 삭제하기");
			System.out.println("0. 프로그램 종료하기");
			System.out.println("----------------------------------");

			System.out.print("원하는 메뉴를 입력하세요(0~8): ");
			int menu = sc.nextInt();
			sc.nextLine();

			switch (menu) {
			case 1:
				selectAll();
				break;
			case 2:
				insertProduct();
				break;
			case 3:
				selectByProductName();
				break;
			case 4:
				selectByProductCategory();
				break;
			case 5:
				updateProduct();
				break;
			case 6:
				updatedeleteYn();
				break;
			case 7:
				updateDeleteCancel();
				break;
			case 8:
				deleteProduct();
				break;
			case 0:
				System.out.println("프로그램을 종료합니다.");
				return;
			default:
				System.out.println("없는 메뉴입니다. 번호를 다시 입력해주세요.");
			}
			System.out.println();
		}
	}

	// 1. 상품 전체 조회 화면
	public void selectAll() {
		System.out.println("\n============ <상품 전체 조회> ============");
		pc.selectAll();
	}

	// 2. 상품 추가 화면
	public void insertProduct() {
		System.out.println("\n============ <상품 추가> ============");

		// 상품추가 시 입력 받아야하는 데이터 상품 아이디, 상품명, 상품가격, 상품 상세정보, 재고, 카테고리
		System.out.print("상품아이디: ");
		String productId = sc.nextLine();

		System.out.print("상품명: ");
		String productName = sc.nextLine();

		System.out.print("상품가격: ");
		int price = sc.nextInt();
		sc.nextLine();

		System.out.print("상품상세정보(100자 이내): ");
		String description = sc.nextLine();

		System.out.print("재고: ");
		int stock = sc.nextInt();
		sc.nextLine();

		System.out.print("카테고리(1.핸드폰/2.노트북/3.PC/4.테블릿): ");
		int category_no = sc.nextInt();

		pc.insertProduct(productId, productName, price, description, stock, category_no);
	}

	// 3. 상품명 검색 화면
	public void selectByProductName() {
		System.out.println("\n====== 상품명 검색 ======");
		System.out.print("상품명 입력: ");
		String keyword = sc.nextLine();

		pc.selectByProductName(keyword);
	}

	// 4. 상품 카테고리별 검색 화면
	public void selectByProductCategory() {
		System.out.println("\n============ <카테고리 검색> ============");

		// 조회할 카테고리명 입력받기
		System.out.print("검색할 카테고리 명을 입력해주세요 : ");
		String category_name = sc.nextLine();

		// Controller -> 상품 카테고리 별 조회 메소드 호출
		pc.selectByProductCategory(category_name);
	}

	// 5. 상품 정보 수정 화면
	public void updateProduct() {
		System.out.println("\n============ <상품 정보 수정> ============");

		// 전체목록 출력
		pc.selectAll();

		// 전체 목록을 조회 후, 수정이 필요한 상품의 아이디 입력받기
		System.out.print("\n수정할 상품아이디 입력: ");
		String productId = sc.nextLine();

		// 수정할 상품 가격 입력받기
		System.out.print("수정할 상품가격: ");
		int price = sc.nextInt();
		sc.nextLine();

		// 수정할 상세정보 입력 받기
		System.out.print("수정할 상품상세정보(100자 이내): ");
		String description = sc.nextLine();

		// 수정할 재고량 입력 받기
		System.out.print("수정할 상품 재고: ");
		int stock = sc.nextInt();
		sc.nextLine();

		pc.updateProduct(productId, price, description, stock);
	}

	// 6. 상품 삭제 화면
	public void updatedeleteYn() {
		System.out.println("\n============ <상품 삭제> ============");

		// 삭제할 상품 입력 전 전체 상품 보여주기
		pc.selectAll();
		System.out.print("\n삭제할 상품 아이디 입력: ");

		// 삭제할  상품 아이디 입력받아 productId에 담아주기
		String productId = sc.nextLine();

		// ProductController의 updatedeleteYn 호출
		// productId를 매개변수로 넘겨준다
		pc.updatedeleteYn(productId);
	}
	
	// 7. 상품 삭제 취소 화면
	// 7-1) 삭제 상품 전체 조회
	public void selectDelete() {
		System.out.println("\n============ <삭제된 상품 조회> ============");
		// pc.selectDelete();
	}
	
	// 7-2) 상품 삭제 취소 화면
	public void updateDeleteCancel() {
		System.out.println("\n============ <상품 삭제 취소> ============");

		// 삭제할 상품 입력 전 전체 상품 보여주기
		pc.selectDelete();
		System.out.print("\n삭제 취소할 상품 아이디 입력: ");

		// 삭제를 취소할 상품 아이디 입력받아 productId에 담아주기
		String productId = sc.nextLine();

		// ProductController의 updateDeleteCancel 호출
		// productId를 매개변수로 넘겨준다
		pc.updateDeleteCancel(productId);
	}
	
	public void deleteProduct() {
		System.out.println("\n============ <상품 최종 삭제> ============");

		// 삭제할 상품 입력 전 전체 상품 보여주기
		pc.selectDelete();
		System.out.print("\n완전히 삭제할 상품 아이디 입력: ");

		// 완전히 삭제할 상품의 아이디 입력받아 productId에 담아주기
		String productId = sc.nextLine();

		// ProductController의 deleteProduct 호출
		// productId를 매개변수로 넘겨준다
		pc.deleteProduct(productId);
	}
	
	// ---------------------------------------------------------------

	// 성공한 경우 출력용 메소드
	public void displaySuccess(String message) {
		System.out.println("서비스 성공: " + message);
	}

	// 실패한 경우 출력용 메소드
	public void displayFail(String message) {
		System.out.println("서비스 실패: " + message);
	}

	// 조회 결과가 여러 행일 경우 출력용 메소드
	public void displayList(ArrayList<Product> list) {
		System.out.println("조회된 상품은 총 " + list.size() + "개 입니다.");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

	// 조회 결과가 한 행일 경우 출력용 메소드
	public void displayOne(Product p) {

		System.out.println("조회된 상품입니다.");

		System.out.println(p);
	}

	// 조회 결과가 없을 경우 출력용 메소드
	public void displayNodata(String message) {
		System.out.println(message);
	}
}
