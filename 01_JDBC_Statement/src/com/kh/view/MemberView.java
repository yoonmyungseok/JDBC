package com.kh.view;

import java.util.Scanner;

import com.kh.controller.MemberController;

//View: 사용자가 보게될 시각적인 요소(화면)를 담당하는 곳(출력과 입력)
//		항상 출력문과 입력문들은 View단에서만 작성할 것
public class MemberView {
	//전역으로 다 쓸 수 있게끔 Scanner 객체 생성(전역 변수)
	private Scanner sc=new Scanner(System.in);
	
	//전역으로 다 쓸 수 있게끔
	//어느 메소드에서든 간에 바로 MemberController의 메소드로 요청할 수 있게끔 객체 생성(전역변수)
	private MemberController mc=new MemberController();
	
	//메인화면을 담당하는 메소드(제일 먼저 사용자가 볼 화면)
	public void mainMenu() {
		while(true) {
			System.out.println("***** 회원관리 프로그램 *****");
			System.out.println("1. 회원 추가");
			System.out.println("2. 회원 전체 조회");
			System.out.println("3. 회원 아이디로 검색");
			System.out.println("4. 회원 이름 키워드 검색");
			System.out.println("5. 회원 정보 변경");
			System.out.println("6. 회원 탈퇴");
			System.out.println("0. 프로그램 종료");
			System.out.println("-----------------------------");
			System.out.print("이용할 메뉴 선택: ");
			int menu=sc.nextInt();
			sc.nextLine();
			
			switch(menu) {
			case 1:
			}
		}
	}
	
}
