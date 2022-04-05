package com.kh.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.kh.common.JDBCTemplate;
import com.kh.model.vo.Board;

public class BoardDao {
	public int insertBoard(Connection conn, Board b) {
		int result=0;
		PreparedStatement pstmt=null;
		String sql="INSERT INTO BOARD VALUES (SEQ_BOARD.NEXTVAL,?,?,DEFAULT,?,DEFAULT)";
		
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, b.getTitle());
			pstmt.setString(2, b.getContent());
			pstmt.setString(3, b.getWriter());
			result=pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	
	public ArrayList<Board> selectAll(Connection conn){
		ArrayList<Board> board=new ArrayList<>();
		PreparedStatement pstmt=null;
		ResultSet rset=null;
		String sql="SELECT BNO, TITLE, CONTENT, CREATE_DATE, USERID FROM BOARD, MEMBER WHERE WRITER=USERNO AND DELETE_YN="+"'N'"+" ORDER BY CREATE_DATE DESC";
		
		try {
			pstmt=conn.prepareStatement(sql);
			
			rset=pstmt.executeQuery();
			
			while(rset.next()) {
				Board b=new Board(rset.getInt("BNO"),rset.getString("TITLE"),rset.getString("CONTENT"),rset.getDate("CREATE_DATE"),rset.getString("USERID"));
				board.add(b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return board;
	}
	
	public ArrayList<Board> selectByWriter(Connection conn, String writer){
		ArrayList<Board> board=new ArrayList<>();
		
		PreparedStatement pstmt=null;
		ResultSet rset=null;
		
		String sql="SELECT BNO, TITLE, CONTENT, CREATE_DATE, USERID FROM BOARD, MEMBER WHERE WRITER=USERNO AND DELETE_YN="+"'N'"+" AND USERID=? ORDER BY CREATE_DATE DESC";
		
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, writer);
			
			rset=pstmt.executeQuery();
			
			while(rset.next()) {
				Board b=new Board(rset.getInt("BNO"),rset.getString("TITLE"),rset.getString("CONTENT"),rset.getDate("CREATE_DATE"),rset.getString("USERID"));
				board.add(b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return board;		
	}
}
