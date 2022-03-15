<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>

<%
	request.setCharacterEncoding("utf-8");
	
	String userId = request.getParameter("user_id");
	String userPw = request.getParameter("user_pw");
	String userNickName = request.getParameter("user_nick_name");
	
	System.out.println(userId);
	System.out.println(userPw);
	System.out.println(userNickName);
	String dbUrl = "jdbc:mysql://localhost:3306/android";
	String dbId = "root";
	String dbPw = "whdgns1002@";
	
	Class.forName("com.mysql.cj.jdbc.Driver");
	
	Connection conn = DriverManager.getConnection(dbUrl, dbId, dbPw);
	String sql = "insert into user_table "
			+ "(user_id, user_pw, user_autologin, user_nick_name) "
			+"values (?, ?, 0, ?)";
	
	
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, userId);
	pstmt.setString(2, userPw);
	pstmt.setString(3, userNickName);
	
	pstmt.execute();
	
	conn.close();
			
%>   