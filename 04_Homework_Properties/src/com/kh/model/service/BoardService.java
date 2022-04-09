package com.kh.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.common.JDBCTemplate;
import com.kh.model.dao.BoardDao;
import com.kh.model.vo.Board;

public class BoardService {
	public int insertBoard(Board b) {
		Connection conn=JDBCTemplate.getConnection();
		
		int result=new BoardDao().insertBoard(conn,b);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);
		
		return result;
	}
	
	public ArrayList<Board> selectAll(){
		Connection conn=JDBCTemplate.getConnection();
		
		ArrayList<Board> board=new BoardDao().selectAll(conn);
		
		JDBCTemplate.close(conn);
		
		return board;
	}
	
	public ArrayList<Board> selectByWriter(String writer){
		Connection conn=JDBCTemplate.getConnection();
		ArrayList<Board> board=new BoardDao().selectByWriter(conn,writer);
		JDBCTemplate.close(conn);
		return board;
	}
	
	public ArrayList<Board> selectByTitle(String keyword){
		Connection conn=JDBCTemplate.getConnection();
		ArrayList<Board> board=new BoardDao().selectByTitle(conn,keyword);
		JDBCTemplate.close(conn);
		return board;
	}
}
