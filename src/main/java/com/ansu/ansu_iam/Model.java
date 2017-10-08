package com.ansu.ansu_iam;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class Model {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/ansu_iam";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "";
	
	Connection conn = null;
	
	Model() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
	}
	
	public static void main( String args[]) {
		Model m;
		try {
			m = new Model();
			//System.out.println(m.createApplication("Text1"));"
			//System.out.println(m.generateToken("a7038b29-c16b-4f2a-aeb3-74652c0524cb", "123", "7ebe7894-4a80-47ce-b64f-3d9980a3ee45"));
			System.out.println(m.validateToken("7ebe7894-4a80-47ce-b64f-3d9980a3ee45", "d7a05c08-f941-4a47-9316-15eeb063617b"));
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
//	public ResultSet runQuery(String q) throws SQLException {
//		
//		Statement stmt = null;
//		
//		stmt = conn.createStatement();
//		ResultSet rs = stmt.executeQuery(q);
//		
//		stmt.close();
//		
//		return rs;
//	}
	
	public JsonObject createApplication(String appName)  {
		Statement stmt = null;
		String appId = UUID.randomUUID().toString();
		String sql = null;
		try {
			stmt = conn.createStatement();
			sql = "INSERT INTO `app`(`appName`, `appId`) VALUES ( \""+appName+"\",\""+appId+"\")";
			stmt.executeUpdate(sql);
			stmt.close();
		}catch(MySQLIntegrityConstraintViolationException s) {
			//s.printStackTrace();
			JsonObject error = Json.createObjectBuilder()
				     .add("status", "Error")
				     .add("payload", "AppName already exists, Try again with a new AppName").build();
			return error;
		}
		catch(SQLException s) {
			//s.printStackTrace();
			JsonObject error = Json.createObjectBuilder()
				     .add("status", "Error")
				     .add("payload", "Internal Server Error, Try again later and contact Sushi").build();
			return error;
		}
		JsonObject res = Json.createObjectBuilder()
			     .add("status", "OK")
			     .add("payload", appId).build();
		return res;
	}
	
	public JsonObject createUser(String username, String password, String appId) {
		Statement stmt = null;
		String uid = UUID.randomUUID().toString();
		String sql = null;
		try {
			stmt = conn.createStatement();
			sql = "INSERT INTO `user`(`uname`, `uid`, `pass`, `appId`) VALUES ( \""+username+"\",\""+uid+"\", \""+password+"\", \""+appId+"\")";
			stmt.executeUpdate(sql);
			stmt.close();
		}catch(MySQLIntegrityConstraintViolationException s) {
			String msg = "Username already exists, Try again with a new Username";
			if(s.getMessage().contains("foreign key")) {
				msg = "Invalid AppId, Please check and try again"; 
			}
			JsonObject error = Json.createObjectBuilder()
				     .add("status", "Error")
				     .add("payload", msg).build();
			return error;
		}
		catch(SQLException s) {
			//s.printStackTrace();
			JsonObject error = Json.createObjectBuilder()
				     .add("status", "Error")
				     .add("payload", "Internal Server Error, Try again later and contact Sushi").build();
			return error;
		}
		JsonObject res = Json.createObjectBuilder()
			     .add("status", "OK")
			     .add("payload", uid).build();
		return res;
	}
	
	public JsonObject generateToken(String uid, String pass, String appId) {
		Statement stmt = null;
		String uname = null;
		String token = UUID.randomUUID().toString();
		String sql = null;
		try {
			stmt = conn.createStatement();
			sql = "SELECT `uname` FROM `user` WHERE `uid`=\""+uid+"\" AND `pass`=\""+pass+"\" AND `appId`=\""+appId+"\""; 
			ResultSet rs = stmt.executeQuery(sql);
			// check if result is empty 
			if(!rs.isBeforeFirst()) {
				JsonObject error = Json.createObjectBuilder()
					     .add("status", "Error")
					     .add("payload", "Invalid credentials").build();
				return error;
			}else {
				rs.next();
				uname = rs.getString("uname");
			}
			rs.close();
		}catch(SQLException s) {
			s.printStackTrace();
			JsonObject error = Json.createObjectBuilder()
				     .add("status", "Error")
				     .add("payload", "Internal Server Error, Try again later and contact Sushi").build();
			return error;
		}
		
		// insert the token into the database
		try {
			sql = "DELETE FROM `token` WHERE `uname`=\""+uname+"\" AND `appId`=\""+appId+"\"";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO `token`(`token`, `uname`, `appId`) VALUES ( \""+token+"\",\""+uname+"\",\""+appId+"\")"; 
			stmt.executeUpdate(sql);
			stmt.close();
		}catch(SQLException s) {
			//s.printStackTrace();
			JsonObject error = Json.createObjectBuilder()
				     .add("status", "Error")
				     .add("payload", "Internal Server Error, Try again later and contact Sushi").build();
			return error;
		}
		
		JsonObject res = Json.createObjectBuilder()
			     .add("status", "OK")
			     .add("payload", token).build();
		return res;
	}
	
	public JsonObject validateToken(String appId, String token) {
		// query for the expiry time for the appID
		// query for time
		Statement stmt = null;
		String sql = null;
		String toi = null;
		Date currentDate = new Date();
		int expiryTime = 0;
		try {
			stmt = conn.createStatement();
			// get expiry time for an appId
			sql = "SELECT `expiryTime` FROM `app` WHERE `appId`=\""+appId+"\""; 
			ResultSet res = stmt.executeQuery(sql);
			if(!res.isBeforeFirst()) {
				JsonObject error = Json.createObjectBuilder()
					     .add("status", "Error")
					     .add("payload", "Invalid appId").build();
				return error;
			}else {
				res.next();
				expiryTime = res.getInt("expiryTime");
			}
			res.close();
			// check for token in table
			sql = "SELECT `toi` FROM `token` WHERE `appId`=\""+appId+"\" AND `token`=\""+token+"\""; 
			ResultSet rs = stmt.executeQuery(sql);
			// check if result is empty 
			if(!rs.isBeforeFirst()) {
				JsonObject error = Json.createObjectBuilder()
					     .add("status", "Error")
					     .add("payload", "Invalid").build();
				return error;
			}else {
				rs.next();
				toi = rs.getString("toi");
			}
			rs.close();
		}catch(SQLException s) {
			s.printStackTrace();
			JsonObject error = Json.createObjectBuilder()
				     .add("status", "Error")
				     .add("payload", "Internal Server Error, Try again later and contact Sushi").build();
			return error;
		}
		// all time calculations done in IST
		// the date object returns local time i.e IST
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("IST"));
		Date toiDate = null;
		try {
			toiDate = sdf.parse(toi);
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		System.out.println("Current Time: "+ currentDate);
//		System.out.println("Toi: "+ toiDate);
//
//		System.out.println("Current Time: "+ currentDate.getTime());
//		System.out.println("Toi: "+ toiDate.getTime());
		long seconds = (currentDate.getTime()- toiDate.getTime())/1000;
		
		//System.out.println("Seconds "+ seconds);
		String validity = "Valid";
		if(seconds > expiryTime) {
			validity = "Expired";
		}
		JsonObject res = Json.createObjectBuilder()
			     .add("status", "OK")
			     .add("payload", validity).build();
		return res;
	}

	public void updateTime(String appId) {}
	
	public void invalidateToken(String appId, String token) {}
	
	public void removeUser(String appId, String uid) {}
	
	public void getAppId(String appName) {}
}
