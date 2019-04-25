package fsClient.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.ws.rs.core.Response;

public class DropBoxAPI implements FSRestAPI {
	
	@Override
	public Response listContents(String token, String path) {
		StringBuilder response = new StringBuilder();
		try {
			URL url = new URL("https://api.dropboxapi.com/2/files/list_folder");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + token);
			conn.setRequestProperty("Content-Type", "application/json");

			String input = "{\"path\": \"" + path + "\"}";
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return Response.status(conn.getResponseCode()).entity("{\"ResponseMsg\":\""+conn.getResponseMessage()+"\"}").build();
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			while ((output = br.readLine()) != null) {
				response.append(output);				
			}
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response upload(String token, String content, String path) {
		try {
			String input = "{\"path\": \"" + path + "\"}";
			URL url = new URL("https://content.dropboxapi.com/2/files/upload");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + token);
			conn.setRequestProperty("Dropbox-API-Arg", input);
			conn.setRequestProperty("Content-Type", "application/octet-stream");
			
			OutputStream os = conn.getOutputStream();
			os.write(content.getBytes());
			os.flush();
			
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return Response.status(conn.getResponseCode()).entity(conn.getResponseMessage()).build();
			} 
			
			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity("File upload successful").build();
	}

	@Override
	public Response download(String token, String source, String destination) {
		try {
			String input = "{\"path\": \"" + source + "\"}";
			URL url = new URL("https://content.dropboxapi.com/2/files/download");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + token);
			conn.setRequestProperty("Dropbox-API-Arg", input);
			
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return Response.status(conn.getResponseCode()).entity("File download failed").build();
			}
			Files.copy(conn.getInputStream(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);				

			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity("File downloaded: " + destination).build();
	}

}
