package fsClient.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fsClient.api.DropBoxAPI;
import fsClient.api.FSRestAPI;

@Path("/")
public class Service {
	FSRestAPI apiProvider = new DropBoxAPI();

	@GET
	@Path("listContents")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listContents(@HeaderParam("token") String token, @HeaderParam("path") String path) {
		return apiProvider.listContents(token, path);
	}

	@POST
	@Path("upload")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response upload(@HeaderParam("token") String token, String content, @HeaderParam("path") String cloudPath) {
		return apiProvider.upload(token, content, cloudPath);
	}
	
	@POST
	@Path("download")
	@Produces(MediaType.TEXT_PLAIN)
	public Response download(@HeaderParam("token") String token, @HeaderParam("fileToDownload") String cloudPath,
			@HeaderParam("path") String localPath) {
		return apiProvider.download(token, cloudPath, localPath);
	}
}
