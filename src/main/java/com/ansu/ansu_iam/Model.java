package com.ansu.ansu_iam;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	static final String PASS = "root";

	Connection conn = null;

	Model() throws ClassNotFoundException, SQLException {
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
	}
	
//	public static void main( String args[]) {
//		try {
//
//			Model m = new Model();
//		//	System.out.println(m.createUser("anush", "123", "7ebe7894-4a80-47ce-b64f-3d9980a3ee45"));
////			//System.out.println(m.createApplication("Text1"));"
//		//System.out.println(m.generateToken("b96ecc13-e227-4133-8746-98e445c3532b", "123", "0ef39447-a43d-4972-bcd7-e895e40f898d"));
////			System.out.println(m.validateToken("7ebe7894-4a80-47ce-b64f-3d9980a3ee45", "d7a05c08-f941-4a47-9316-15eeb063617b"));
////			//System.out.println(m.updateTime("7ebe7894-4a80-47ce-b64f-3d9980a3ee45", 500));
////			//System.out.println(m.invalidateToken("7ebe7894-4a80-47ce-b64f-3d9980a3ee45", "0fa5dac4-429e-450b-8232-39054eb2b710"));
//	//System.out.println(m.removeUser("0ef39447-a43d-4972-bcd7-e895e40f898d", "b96ecc13-e227-4133-8746-98e445c3532b"));
////			System.out.println(m.getAppId("Text12"));
//		} catch (ClassNotFoundException | SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	

	public JsonObject createApplication(String appName) {
		Statement stmt = null;
		String appId = UUID.randomUUID().toString();
		String sql = null;
		try {
			stmt = conn.createStatement();
			sql = "INSERT INTO `app`(`appName`, `appId`) VALUES ( \"" + appName + "\",\"" + appId + "\")";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (MySQLIntegrityConstraintViolationException s) {
			JsonObject error = Json.createObjectBuilder().add("status", "Error")
					.add("payload", "AppName already exists, Try again with a new AppName").build();
			return error;
		} catch (SQLException s) {
			JsonObject error = Json.createObjectBuilder().add("status", "Error")
					.add("payload", "Internal Server Error, Try again later and contact Sushi").build();
			return error;
		}
		JsonObject res = Json.createObjectBuilder().add("status", "OK").add("payload", appId).build();
		return res;
	}

	public JsonObject createUser(String username, String password, String appId) {
		Statement stmt = null;
		String uid = UUID.randomUUID().toString();
		String sql = null;
		try {
			stmt = conn.createStatement();
			sql = "INSERT INTO `user`(`uname`, `uid`, `pass`, `appId`) VALUES ( \"" + username + "\",\"" + uid
					+ "\", \"" + password + "\", \"" + appId + "\")";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (MySQLIntegrityConstraintViolationException s) {
			String msg = "Username already exists, Try again with a new Username";
			if (s.getMessage().contains("foreign key")) {
				msg = "Invalid AppId, Please check and try again";
			}
			JsonObject error = Json.createObjectBuilder().add("status", "Error").add("payload", msg).build();
			return error;
		} catch (SQLException s) {
			JsonObject error = Json.createObjectBuilder().add("status", "Error")
					.add("payload", "Internal Server Error, Try again later and contact Sushi").build();
			return error;
		}
		JsonObject res = Json.createObjectBuilder().add("status", "OK").add("payload", uid).build();
		return res;
	}

	public JsonObject generateToken(String uid, String pass, String appId) {
		Statement stmt = null;
		String uname = null;
		String token = UUID.randomUUID().toString();
		String sql = null;
		try {
			stmt = conn.createStatement();
			sql = "SELECT `uname` FROM `user` WHERE `uid`=\"" + uid + "\" AND `pass`=\"" + pass + "\" AND `appId`=\""
					+ appId + "\"";
			ResultSet rs = stmt.executeQuery(sql);
			// check if result is empty
			if (!rs.isBeforeFirst()) {
				JsonObject error = Json.createObjectBuilder().add("status", "Error")
						.add("payload", "Invalid credentials").build();
				return error;
			} else {
				rs.next();
				uname = rs.getString("uname");
			}
			rs.close();
		} catch (SQLException s) {
			JsonObject error = Json.createObjectBuilder().add("status", "Error")
					.add("payload", "Internal Server Error, Try again later and contact Sushi").build();
			return error;
		}

		// insert the token into the database
		try {
			sql = "DELETE FROM `token` WHERE `uid`=\"" + uid + "\" AND `appId`=\"" + appId + "\"";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO `token`(`token`, `uid`, `appId`) VALUES ( \"" + token + "\",\"" + uid + "\",\""
					+ appId + "\")";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException s) {
			JsonObject error = Json.createObjectBuilder().add("status", "Error")
					.add("payload", "Internal Server Error, Try again later and contact Sushi").build();
			return error;
		}

		JsonObject res = Json.createObjectBuilder().add("status", "OK").add("payload", token).build();
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
			sql = "SELECT `expiryTime` FROM `app` WHERE `appId`=\"" + appId + "\"";
			ResultSet res = stmt.executeQuery(sql);
			if (!res.isBeforeFirst()) {
				JsonObject error = Json.createObjectBuilder().add("status", "Error").add("payload", "Invalid appId")
						.build();
				return error;
			} else {
				res.next();
				expiryTime = res.getInt("expiryTime");
			}
			res.close();
			// check for token in table
			sql = "SELECT `toi` FROM `token` WHERE `appId`=\"" + appId + "\" AND `token`=\"" + token + "\"";
			ResultSet rs = stmt.executeQuery(sql);
			// check if result is empty
			if (!rs.isBeforeFirst()) {
				JsonObject error = Json.createObjectBuilder().add("status", "Error").add("payload", "Invalid").build();
				return error;
			} else {
				rs.next();
				toi = rs.getString("toi");
			}
			rs.close();
		} catch (SQLException s) {
			s.printStackTrace();
			JsonObject error = Json.createObjectBuilder().add("status", "Error")
					.add("payload", "Internal Server Error, Try again later and contact Sushi").build();
			return error;
		}

		// Amazon EC2 instance is configured for UTC timezone
		// all time calculations done in UTC
		// the date object returns local time i.e UTC
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date toiDate = null;
		try {
			toiDate = sdf.parse(toi);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long seconds = (currentDate.getTime() - toiDate.getTime()) / 1000;
		System.out.println("Seconds elapsed: " + seconds);
		String validity = "Valid";
		if (seconds > expiryTime) {
			validity = "Expired";
		}
		JsonObject res = Json.createObjectBuilder().add("status", "OK").add("payload", validity).build();
		return res;
	}

	public JsonObject updateTime(String appId, int seconds) {
		Statement stmt = null;
		String sql = null;
		try {
			stmt = conn.createStatement();
			sql = "SELECT * FROM `app` WHERE `appId`=\""+appId+"\""; 
			ResultSet rs = stmt.executeQuery(sql);
			// check if result is empty 
			if(!rs.isBeforeFirst()) {
				JsonObject error = Json.createObjectBuilder()
					     .add("status", "Error")
					     .add("payload", "Invalid AppId").build();
				return error;
			}
			rs.close();
		}catch(SQLException s) {
			s.printStackTrace();
			JsonObject error = Json.createObjectBuilder()
				     .add("status", "Error")
				     .add("payload", "Internal Server Error, Try again later and contact Sushi").build();
			return error;
		}
		
		try {
			stmt = conn.createStatement();
			sql = "UPDATE `app` SET `expiryTime`=\""+seconds+"\" WHERE `appId`=\""+appId+"\""; 
			stmt.executeUpdate(sql);
		}catch(SQLException s) {
			s.printStackTrace();
			JsonObject error = Json.createObjectBuilder()
				     .add("status", "Error")
				     .add("payload", "Internal Server Error, Try again later and contact Sushi").build();
			return error;
		}
		JsonObject res = Json.createObjectBuilder()
			     .add("status", "OK")
			     .add("payload", "Success").build();
		return res;
	}
	
	public JsonObject invalidateToken(String appId, String token) {
		Statement stmt = null;
		String sql = null;
		try {
			stmt = conn.createStatement();
			sql = "SELECT * FROM `token` WHERE `appId`=\""+appId+"\" AND `token`=\""+token+"\""; 
			ResultSet rs = stmt.executeQuery(sql);
			// check if result is empty 
			if(!rs.isBeforeFirst()) {
				JsonObject error = Json.createObjectBuilder()
					     .add("status", "Error")
					     .add("payload", "Invalid AppId or Token").build();
				return error;
			}
			rs.close();
		}catch(SQLException s) {
			s.printStackTrace();
			JsonObject error = Json.createObjectBuilder()
				     .add("status", "Error")
				     .add("payload", "Internal Server Error, Try again later and contact Sushi").build();
			return error;
		}
		
		sql = "DELETE FROM `token` WHERE `appId`=\""+appId+"\"";
		try {
			stmt.executeUpdate(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JsonObject res = Json.createObjectBuilder()
			     .add("status", "OK")
			     .add("payload", "Success").build();
		return res;
	}
	
	public JsonObject removeUser(String appId, String uid) {
		Statement stmt = null;
		String sql = null;
		try {
			stmt = conn.createStatement();
			sql = "SELECT * FROM `user` WHERE `appId`=\""+appId+"\" AND `uid`=\""+uid+"\""; 
			ResultSet rs = stmt.executeQuery(sql);
			// check if result is empty 
			if(!rs.isBeforeFirst()) {
				JsonObject error = Json.createObjectBuilder()
					     .add("status", "Error")
					     .add("payload", "Invalid AppId or uid").build();
				return error;
			}
			rs.close();
		}catch(SQLException s) {
			s.printStackTrace();
			JsonObject error = Json.createObjectBuilder()
				     .add("status", "Error")
				     .add("payload", "Internal Server Error, Try again later and contact Sushi").build();
			return error;
		}
		
		sql = "DELETE FROM `user` WHERE `appId`=\""+appId+"\" AND `uid`=\""+uid+"\"";
		try {
			stmt.executeUpdate(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JsonObject res = Json.createObjectBuilder()
			     .add("status", "OK")
			     .add("payload", "Success").build();
		return res;
	}
	
	public JsonObject getAppId(String appName) {
		Statement stmt = null;
		String sql = null;
		String appId = null;
		try {
			stmt = conn.createStatement();
			sql = "SELECT `appId` FROM `app` WHERE `appName`=\""+appName+"\""; 
			ResultSet rs = stmt.executeQuery(sql);
			// check if result is empty 
			if(!rs.isBeforeFirst()) {
				JsonObject error = Json.createObjectBuilder()
					     .add("status", "Error")
					     .add("payload", "Invalid AppName").build();
				return error;
			}else {
				rs.next();
				appId = rs.getString("appId");
			}
			rs.close();
		}catch(SQLException s) {
			s.printStackTrace();
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
}
