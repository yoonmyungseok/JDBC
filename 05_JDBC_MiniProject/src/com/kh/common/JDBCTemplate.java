package com.kh.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class JDBCTemplate {
 
	public static Connection getConnection() {
		

		Properties prop = new Properties();
		
		Connection conn = null;
		
		try {
			prop.load(new FileInputStream("resources/Driver.properties"));
			
			Class.forName(prop.getProperty("driver"));
			
			conn = DriverManager.getConnection(prop.getProperty("url")
												, prop.getProperty("username")
												, prop.getProperty("password"));
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;

	}
		
	public static void close(Connection conn) {
		
		try {
			if (conn != null && !conn.isClosed()) {
				
				conn.close();
				
			}
		} catch (SQLException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}		
	}
		
	public static void close(Statement stmt) {
		
		try {
			if (stmt != null && !stmt.isClosed()) {
				
				stmt.close();
			}
		} catch (SQLException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
		
	}


	public static void close(ResultSet rset) {
		
		try {
			if (rset != null && !rset.isClosed()) {
				rset.close();
			}
		} catch (SQLException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
		
	}
		

	public static void commit(Connection conn) {
		
		try {
			if (conn != null && !conn.isClosed()) {
				
				conn.commit();
			}
		} catch (SQLException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
		
	}

	public static void rollback(Connection conn) {
		
		try {
			if (conn != null && !conn.isClosed()) {
				
				conn.rollback();
			}
		} catch (SQLException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
		
	}
	
	
}
