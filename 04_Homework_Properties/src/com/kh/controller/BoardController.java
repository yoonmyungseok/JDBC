package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.service.BoardService;
import com.kh.model.vo.Board;
import com.kh.view.BoardView;

public class BoardController {
	public void insertBoard(String title, String content, String writer) {
		Board b=new Board(title, content, writer);
		
		int result=new BoardService().insertBoard(b);
		
		if(result>0) {
			new BoardView().displaySuccess("게시글 추가 성공");
		}else {
			new BoardView().displayFail("게시글 추가 실패");
		}
	}
	
	public void selectAll() {
		ArrayList<Board> board=new BoardService().selectAll();
		
		if(board.isEmpty()) {
			new BoardView().displayNodata("전체 조회 결과가 없습니다.");
		}else {
			new BoardView().displayBoard(board);
		}
	}
	
	public void selectByWriter(String writer) {
		ArrayList<Board> board=new BoardService().selectByWriter(writer);
		if(board.isEmpty()) {
			new BoardView().displayNodata("해당 아이디로 작성한 게시글 조회 결과가 없습니다.");
		}else {
			new BoardView().displayBoard(board);
		}
	}
	
	public void selectByTitle(String keyword) {
		ArrayList<Board> board=new BoardService().selectByTitle(keyword);
		if(board.isEmpty()) {
			new BoardView().displayNodata("해당 키워드로 작성한 게시글 제목 결과가 없습니다.");
		}else {
			new BoardView().displayBoard(board);
		}
	}
	
	public void updateTitle(String title) {
		
	}
}
