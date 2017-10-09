package com.ansu.ansu_iam;

import java.sql.SQLException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/token")
public class TokenResource {
	
	@GET
	@Path("/checkValid")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkValidToken(
			@QueryParam("appId") String appId,
			@QueryParam("token") String token) {
		JsonObject resObj = null;
		
		if(appId == null) {
			resObj = Json.createObjectBuilder()
					.add("status", "Error")
					.add("payload", "Missing appId parameter")
					.build();
		}
		else if(token == null) {
			resObj = Json.createObjectBuilder()
					.add("status", "Error")
					.add("payload", "Missing token parameter")
					.build();
		}
		else {
			try {
				Model m = new Model();
				resObj = m.validateToken(appId, token);
			} catch (ClassNotFoundException | SQLException e) {
				resObj = Json.createObjectBuilder()
						.add("status", "Error")
						.add("payload", "Internal Server Error, please contact admins")
						.build();
			}
		}
		
		return Response.ok(resObj.toString(), MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/generate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(
			@FormParam("uid") String uid,
			@FormParam("pass") String password,
			@FormParam("appId") String appId) {
		JsonObject resObj = null;
		
		if(uid == null) {
			resObj = Json.createObjectBuilder()
					.add("status", "Error")
					.add("payload", "Missing uid parameter")
					.build();
		}
		else if(password == null) {
			resObj = Json.createObjectBuilder()
					.add("status", "Error")
					.add("payload", "Missing password parameter")
					.build();
		}
		else if(appId == null) {
			resObj = Json.createObjectBuilder()
					.add("status", "Error")
					.add("payload", "Missing appId parameter")
					.build();
		}
		else {
			try {
				Model m = new Model();
				resObj = m.generateToken(uid, password, appId);
			} catch (ClassNotFoundException | SQLException e) {
				resObj = Json.createObjectBuilder()
						.add("status", "Error")
						.add("payload", "Internal Server Error, please contact admins")
						.build();
			}
		}
				
		return Response.ok(resObj.toString(), MediaType.APPLICATION_JSON).build();
	}
	
	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response invalidateToken(
			@FormParam("appId") String appId,
			@FormParam("token") String token) {
		JsonObject resObj = null;
		
		if(appId == null) {
			resObj = Json.createObjectBuilder()
					.add("status", "Error")
					.add("payload", "Missing appId parameter")
					.build();
		}
		else if(token == null) {
			resObj = Json.createObjectBuilder()
					.add("status", "Error")
					.add("payload", "Missing token parameter")
					.build();
		}
		else {
			try {
				Model m = new Model();
				resObj = m.invalidateToken(appId, token);
			} catch (ClassNotFoundException | SQLException e) {
				resObj = Json.createObjectBuilder()
						.add("status", "Error")
						.add("payload", "Internal Server Error, please contact admins")
						.build();
			}
		}
	
		return Response.ok(resObj.toString(), MediaType.APPLICATION_JSON).build();
	}
}
