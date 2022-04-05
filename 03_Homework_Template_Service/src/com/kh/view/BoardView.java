package com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import com.kh.controller.BoardController;
import com.kh.model.vo.Board;

public class BoardView {
	private Scanner sc=new Scanner(System.in);
	
	private BoardController bc=new BoardController();
	
	public void mainMenu() {
		while(true) {
			System.out.println("***** 게시판 관리 프로그램 *****");
			System.out.println("1. 게시글 추가");
			System.out.println("2. 게시글 전체 조회");
			System.out.println("3. 작성자 아이디로 조회");
			System.out.println("4. 게시글 제목 키워드로 조회");
			System.out.println("5. 게시글 제목 변경");
			System.out.println("6. 게시글 내용 변경");
			System.out.println("7. 게시글 삭제");
			System.out.println("0. 프로그램 종료");
			System.out.println("-----------------------------");
			System.out.print("이용할 메뉴 선택: ");
			int menu=sc.nextInt();
			sc.nextLine();
			
			switch(menu) {
			case 1: insertBoard();break;
			case 2: selectAll();break;
			case 3: selectByWriter(); break;
			case 4: selectByTitle(); break; 
			case 5: updateTitle(); break;
			case 6: updateContent(); break;
			case 7: deleteBoard(); break;
			case 0: System.out.println("프로그램을 종료합니다.");return;
			default: System.out.println("번호를 잘못 입력했습니다. 다시 입력해주세요");
			}
		}
	}
	//1.게시글 추가 ->제목, 내용, 작성자
	public void insertBoard() {
		System.out.println("게시글 추가");
		System.out.print("제목: ");
		String title=sc.nextLine();
		
		System.out.print("내용: ");
		String content=sc.nextLine();
		
		System.out.print("작성자 번호: ");
		String writer=sc.nextLine();
		
		bc.insertBoard(title, content, writer);
	}
	//2.게시글 전체 조회
	public void selectAll() {
		System.out.println("게시글 전체 조회");
		bc.selectAll();
	}
	//3.작성자 아이디로 검색
	public void selectByWriter() {
		System.out.println("작성자 아이디로 조회");
		System.out.print("작성자 아이디: ");
		String writer=sc.nextLine();
		
		bc.selectByWriter(writer);
	}
	//4.게시글 제목 키워드로 검색
	public void selectByTitle() {
		
	}
	//5.게시글 제목 변경
	public void updateTitle() {
		
	}
	//6.게시글 내용 변경
	public void updateContent() {
		
	}
	//7.게시글 삭제
	public void deleteBoard() {
		
	}
	
	//------------------------------------------------------------------------
	public void displaySuccess(String message) {
		System.out.println("서비스 요청 성공: "+message);
	}
	//실패 메시지 출력용 메소드
	public void displayFail(String message) {
		System.out.println("서비스 요청 실패: "+message);
	}
	
	public void displayNodata(String message) {
		System.out.println(message);
	}
	public void displayBoard(ArrayList<Board> board ) {
		System.out.println("조회된 데이터는 총 "+board.size()+"건 입니다.");
		for(int i=0; i<board.size(); i++) {
			System.out.println(board.get(i));
		}
	}
}