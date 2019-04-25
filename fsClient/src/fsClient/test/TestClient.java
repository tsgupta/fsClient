package fsClient.test;

import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

import fsClient.api.DropBoxAPI;

public class TestClient {
	private static String token = "";

	public static void main(String[] args) {

		// System.out.println(listContents(""));
		// System.out.println(download("/cloudEle/new.txt","C:\\dev\\new.txt"));
		System.out.println(uploadFile("C:\\dev\\new.txt", "/ce/new4.txt"));
		System.out.println(uploadContent("hello", "/ce/new5.txt"));
	}

	public static String listContents(String path) {
		return "" + new DropBoxAPI().listContents(token, path).getStatus();
	}

	public static String uploadFile(String localPath, String cloudPath) {
		try {
			String input = "{\"path\": \"" + cloudPath + "\"}";
			URL url = new URL("https://content.dropboxapi.com/2/files/upload");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + token);
			conn.setRequestProperty("Dropbox-API-Arg", input);
			conn.setRequestProperty("Content-Type", "application/octet-stream");

			OutputStream os = conn.getOutputStream();
			Files.copy(new File(localPath).toPath(), os);
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "File upload successful";
	}

	public static String uploadContent(String content, String cloudPath) {
		return "" + new DropBoxAPI().upload(token, content, cloudPath).getStatus();
	}

	public static String download(String source, String dest) {
		return "" + new DropBoxAPI().download(token, source, dest);
	}
}
