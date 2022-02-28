package com.example.demo.poc;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GitHubAPI_GetContent {

	public static void main(String[] args) throws IOException, URISyntaxException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Bearer " + "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");

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		RestTemplate restTemplate = new RestTemplate();

		/*
		 * List<Map> response = restTemplate.getForObject(
		 * "https://api.github.com/repos/{vijkollu}/{repo}/contents", List.class,
		 * "vijkollu", "excel",entity);
		 */
		
		ResponseEntity<List> response=restTemplate.exchange("https://api.github.com/repos/vijkollu/Excel/contents",HttpMethod.GET,entity,List.class);
		
		List<Map> responseMap=response.getBody();
		

		/*
		 * List<Map> response = restTemplate.postForObject(
		 * "https://api.github.com/repos/vijkollu/Excel/contents", entity, List.class);
		 */
		// https://api.github.com/repos/vijkollu/FetchAPI/contents/FetchApi/src/main/resources/openapi.yaml?ref=main

		/*
		 * List<Map> response = restTemplate.getForObject(
		 * "https://api.github.com/repos/vijkollu/FetchAPI/contents/FetchApi/src/main/resources/",
		 * List.class);
		 */
		// FetchAPI/FetchApi/src/main/resources/

		// List<Map> response =
		// restTemplate.getForObject("https://api.github.com/repos/vijkollu/FetchAPI/contents/FetchApi/src/main/resources/openapi.yaml",List.class);

		
		  Gson gson = new GsonBuilder().setPrettyPrinting().create();
		  System.out.println("<JSON RESPONSE START>\n" + gson.toJson(response) +
		  "\n<JSON RESPONSE END>\n");
		  
		  for (Map fileMetaData : responseMap) {
		  
		  String fileName = (String) fileMetaData.get("name"); String downloadUrl =
		  (String) fileMetaData.get("download_url"); System.out.println("File Name = "
		  + fileName + " | Download URL = " + downloadUrl);
		  
		  if (downloadUrl != null && downloadUrl.contains("README")) {
		  
		  String fileContent = IOUtils.toString(new URI(downloadUrl),
		  Charset.defaultCharset());
		  System.out.println("\nfileContent = <FILE CONTENT START>\n" + fileContent +
		  "\n<FILE CONTENT END>\n");
		  
		  File file = new File("github-api-downloaded-" + fileName);
		  FileUtils.copyURLToFile(new URL(downloadUrl), file); } }
		 
	}
}
