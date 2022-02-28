package com.example.demo.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FetchRAML {

	private static Gson gson;

	private static void copyURLToFile(URL url, File file) {

		try {
			InputStream input = url.openStream();
			if (file.exists()) {
				if (file.isDirectory())
					throw new IOException("File '" + file + "' is a directory");

				if (!file.canWrite())
					throw new IOException("File '" + file + "' cannot be written");
			} else {
				File parent = file.getParentFile();
				if ((parent != null) && (!parent.exists()) && (!parent.mkdirs())) {
					throw new IOException("File '" + file + "' could not be created");
				}
			}

			FileOutputStream output = new FileOutputStream(file);

			byte[] buffer = new byte[4096];
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
			}

			input.close();
			output.close();

			System.out.println("File '" + file + "' downloaded successfully!");
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
	}

	private static String findRamllocate(String username, String repository, String token)
			throws IOException, URISyntaxException {

		String locateDir = "";
		String locateRAML = "";

		// To print response JSON, using GSON. Any other JSON parser can be used here.
		gson = new GsonBuilder().setPrettyPrinting().create();

		Map jsonMap = makeRESTCall("https://api.github.com/repos/" + username + "/" + repository + "/branches/main",
				token);

		String treeApiUrl = gson.toJsonTree(jsonMap).getAsJsonObject().get("commit").getAsJsonObject().get("commit")
				.getAsJsonObject().get("tree").getAsJsonObject().get("url").getAsString();

		Map jsonTreeMap = makeRESTCall(treeApiUrl + "?recursive=1", token);

		for (Object obj : ((List) jsonTreeMap.get("tree"))) {

			Map fileMetadata = (Map) obj;

			if (fileMetadata.get("type").equals("tree")) {

				locateDir = fileMetadata.get("path").toString();

			} else {

				locateRAML = fileMetadata.get("path").toString();
				if (locateRAML.contains(".yaml")) {

					return locateDir;

				}

			}
		}

		return null;
	}

	private static Map makeRESTCall(String restUrl, String token) throws ClientProtocolException, IOException {

		Request request = Request.Get(restUrl).addHeader("Authorization", "Bearer " + token);

		Content content = request.execute().returnContent();
		String jsonString = content.asString();
		System.out.println("content = " + jsonString);
		Map jsonMap = gson.fromJson(jsonString, Map.class);
		return jsonMap;
	}

	public static void downloadRAML(String username, String repo, String token) throws IOException, URISyntaxException {

		String foundRaml = findRamllocate(username, repo, token);
		String downloadUrl = "";
		if (foundRaml != null) {

			downloadUrl = FetchDonwloadUrl(username, repo, foundRaml, token);

			downloadRaml(downloadUrl);

		} else {
			System.out.println("No Associated RAML present");
		}

	}

	private static String FetchDonwloadUrl(String username, String repo, String foundRaml, String token)
			throws IOException, URISyntaxException {
		// TODO Auto-generated method stub

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Bearer " + token);

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<List> response = restTemplate.exchange(
				"https://api.github.com/repos/" + username + "/" + repo + "/contents/" + foundRaml, HttpMethod.GET,
				entity, List.class);

		List<Map> responseMap = response.getBody();

		for (Map fileMetaData : responseMap) {

			String fileName = (String) fileMetaData.get("name");

			if (fileName.contains(".yaml")) {
				return (String) fileMetaData.get("download_url");
			}

		}

		return null;
	}

	public static void downloadRaml(String sUrl) throws MalformedURLException {

		URL url = new URL(sUrl);

		File file = new File("C://Users/vijkollu/Documents/checkDownload.yaml");

		FetchRAML.copyURLToFile(url, file);

	}

}
