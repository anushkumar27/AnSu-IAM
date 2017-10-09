package com.ansu.ansu_iam;

import java.sql.SQLException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/app")
public class ApplicationResource {
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAppId(@QueryParam("appName") String appName) {
		JsonObject resObj = null;
		
		try {
			Model m = new Model();
			resObj = m.getAppId(appName);
		} catch (ClassNotFoundException | SQLException e) {
			resObj = Json.createObjectBuilder()
					.add("status", "Error")
					.add("payload", "Internal Server Error, please contact admins")
					.build();
		}

		return Response.ok(resObj.toString(), MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(
			@FormParam("appName") String appName) {
		JsonObject resObj = null;
		
		try {
			Model m = new Model();
			resObj = m.createApplication(appName);
		} catch (ClassNotFoundException | SQLException e) {
			resObj = Json.createObjectBuilder()
					.add("status", "Error")
					.add("payload", "Internal Server Error, please contact admins")
					.build();
		}
		
		return Response.ok(resObj.toString(), MediaType.APPLICATION_JSON).build();
	}
	
	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateTime(
			@FormParam("appId") String appId,
			@FormParam("seconds") int seconds) {
		JsonObject resObj = null;

		try {
			Model m = new Model();
			resObj = m.updateTime(appId, seconds);
		} catch (ClassNotFoundException | SQLException e) {
			resObj = Json.createObjectBuilder().add("status", "Error")
					.add("payload", "Internal Server Error, please contact admins").build();
		}

		return Response.ok(resObj.toString(), MediaType.APPLICATION_JSON).build();
	}
}
