package fsClient.api;

import javax.ws.rs.core.Response;

public interface FSRestAPI {
	public Response listContents(String token, String path);
	public Response upload(String token, String content, String path);
	public Response download(String token, String source, String destination);
}
