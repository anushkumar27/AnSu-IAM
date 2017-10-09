package com.ansu.ansu_iam;

import java.sql.SQLException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserResource {
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(
			@FormParam("name") String name,
			@FormParam("pass") String password,
			@FormParam("appId") String appId) {
		JsonObject resObj = null;
		
		if(name == null) {
			resObj = Json.createObjectBuilder()
					.add("status", "Error")
					.add("payload", "Missing name parameter")
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
				resObj = m.createUser(name, password, appId);
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
	public Response deleteUser(
			@FormParam("uid") String uid,
			@FormParam("appId") String appId) {
		JsonObject resObj = null;
		
		if(uid == null) {
			resObj = Json.createObjectBuilder()
					.add("status", "Error")
					.add("payload", "Missing uid parameter")
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
				resObj = m.removeUser(appId, uid);
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
