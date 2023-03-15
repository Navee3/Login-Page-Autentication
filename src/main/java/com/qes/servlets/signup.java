package com.qes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class signup
 */
@WebServlet("/signup")
public class signup extends HttpServlet {
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt1 = null;
	ResultSet res = null;
	
	String url = "jdbc:mysql://localhost:3306/qes";
	String un = "root";
	String pwd = "root";
	
	@Override
	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, un, pwd);
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		String firstname = req.getParameter("firstname");
		String lastname = req.getParameter("lastname");
		String username = req.getParameter("username");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
		try {
			String query = "SELECT * FROM register WHERE username = ? or email = ?;";
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, username);
			pstmt.setString(2, email);
			res = pstmt.executeQuery();
			
			if(res.next()) {
				req.getRequestDispatcher("resignup.html").forward(req, resp);
			}
			else {
				String query1= "INSERT INTO register (firstname, lastname, username, email, password) "
						+ "VALUES (?,?,?,?,?);";
				pstmt1 = con.prepareStatement(query1);
				pstmt1.setString(1, firstname);
				pstmt1.setString(2, lastname);
				pstmt1.setString(3, username);
				pstmt1.setString(4, email);
				pstmt1.setString(5, password);
				pstmt1.executeUpdate();
			}
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void destroy() {
		try {
			res.close();
			pstmt1.close();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
