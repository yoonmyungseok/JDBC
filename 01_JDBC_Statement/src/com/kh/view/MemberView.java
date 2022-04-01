package com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import com.kh.controller.MemberController;
import com.kh.model.vo.Member;

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
			case 1: insertMember();break;
			case 2: selectAll();break;
			case 3: selectByUserId(); break;
			case 4: selectByUserName(); break; // 힌트: SELECT * FROM MEMBER WHERE USERNAME LIKE '%keyword%'
			case 5: break;
			case 6: break;
			case 0: System.out.println("프로그램을 종료합니다.");return;
			default: System.out.println("번호를 잘못 입력했습니다. 다시 입력해주세요");
			}
		}
	}
	//회원추가용 화면: 추가하고자 하는 회원의 정보를 키보드로 입력받아서 추가 요청할 수 있는 화면
	public void insertMember() {
		System.out.println("----- 회원 추가 -----");
		
		//회원 추가 시 입력받아야 하는 데이터
		//아이디, 비밀번호, 이름, 성별, 나이, 이메일, 휴대폰번호, 주소, 취미
		System.out.print("아이디: ");
		String userId=sc.nextLine();
		
		System.out.print("비밀번호: ");
		String userPwd=sc.nextLine();
		
		System.out.print("이름: ");
		String userName=sc.nextLine();
		
		System.out.print("성별(M/F): ");
		String gender=sc.nextLine().toUpperCase();
		
		System.out.print("나이: ");
		int age=sc.nextInt();
		sc.nextLine();
		
		System.out.print("이메일: ");
		String email=sc.nextLine();
		
		System.out.print("전화번호(숫자만): ");
		String phone=sc.nextLine();
		
		System.out.print("주소: ");
		String address=sc.nextLine();
		
		System.out.print("취미(,로 공백없이 나열): ");
		String hobby=sc.nextLine();
		
		//필요한 데이터를 다 입력받았다면
		//회원을 추가하고자 요청=>MemberController의 어떤 메소드를 호출
		mc.insertMember(userId, userPwd, userName, gender, age, email, phone, address, hobby);
	}
	//회원 전체 조회를 할 수 있는 화면
	public void selectAll() {
		System.out.println("----- 회원 전체 조회 -----");
		//이 경우에는 따로 입력받을만한 값이 없음
		//곧바로 MemberController의 메소드를 호출하여 바로 요청을 넘길것임
		mc.selectAll(); //입력받은 값이 없기 때문에 넘길 값 또한 없음
	}
	
	//회원 아이디로 검색할 수 있는 화면
	public void selectByUserId() {
		System.out.println("----- 회원 아이디로 검색 -----");
		
		//필요한 데이터: 검색을 원하는 아이디 한 개 
		System.out.print("검색할 회원의 아이디: ");
		String userId=sc.nextLine();
		
		//MemberController의 어떤 메소드를 호출해서 아이디 검색 요청
		mc.selectByUserId(userId);
	}
	
	//회원 이름 키워드로 검색 화면
	public void selectByUserName() {
		System.out.println("----- 회원 이름 키워드로 검색 -----");
		
		//필요한 데이터: 회원 이름 키워드
		System.out.print("검색할 회원 이름 키워드: ");
		String userName=sc.nextLine();
		
		//MemberController의 메소드를 호출해서 이름 검색 요청
		mc.selectByUserName(userName);
	}
	
	//------------------------------------------------------------------------
	//성공 메시지 출력용 메소드
	public void displaySuccess(String message) {
		System.out.println("서비스 요청 성공: "+message);
	}
	//실패 메시지 출력용 메소드
	public void displayFail(String message) {
		System.out.println("서비스 요청 실패: "+message);
	}
	//조회 서비스 요청 시 조회결과가 없을 때 보게 될 화면을 만드는 메소드
	public void displayNodata(String message) {
		System.out.println(message);
	}
	//조회 서비스 요청 시 조회결과가 있을 때 보게 될 화면을 만드는 메소드
	public void displayList(ArrayList<Member> list) {
		System.out.println("조회된 데이터는 총 "+list.size()+"건 입니다.");
		for(int i=0; i<list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
	//조회 서비스 요청 시 한 개의 행이 조회된 결과를 받아서 보게 될 화면을 만드는 메소드
	public void displayOne(Member m) {
		System.out.println("조회된 데이터는 다음과 같습니다");
		System.out.println(m);
	}
	
}
