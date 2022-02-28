package com.example.demo.diff;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GitHubAPI_GetTree {

	private static Gson gson;

	public static void main(String[] args) throws IOException, URISyntaxException {

		
		
		try {

		gson = new GsonBuilder().setPrettyPrinting().create();

		/*
		
		 * 
		 * https://api.github.com/repos/vijkollu/FetchAPI/branches/main
		 */
		Map jsonMap = makeRESTCall("https://api.github.com/repos/vijkollu/FetchAPI/branches/main");
		System.out.println(
				"Branches API Response = \n<API RESPONSE START>\n " + gson.toJson(jsonMap) + "\n<API RESPONSE END>\n");

		String treeApiUrl = gson.toJsonTree(jsonMap).getAsJsonObject().get("commit").getAsJsonObject().get("commit")
				.getAsJsonObject().get("tree").getAsJsonObject().get("url").getAsString();
		System.out.println("TREE API URL = " + treeApiUrl + "\n");

		Map jsonTreeMap = makeRESTCall(treeApiUrl + "?recursive=1");
		System.out.println(
				"TREE API Response = \n<API RESPONSE START>\n " + gson.toJson(jsonMap) + "\n<API RESPONSE END>\n");


		System.out.println("Directory & files list :");
		for (Object obj : ((List) jsonTreeMap.get("tree"))) {

			Map fileMetadata = (Map) obj;

		
			if (fileMetadata.get("type").equals("tree")) {
				System.out.println("Directory = " + fileMetadata.get("path"));
			} else {
				System.out.println(
						"File = " + fileMetadata.get("path") + " | Size = " + fileMetadata.get("size") + " Bytes");
			}
		}
		
		
		}catch(Exception e) {
			System.out.println(e.getLocalizedMessage()+"::::"+e.getMessage());
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			e.printStackTrace();
		}
	}


	private static Map makeRESTCall(String restUrl) throws ClientProtocolException, IOException {
		
		Content content = Request.Get(restUrl).execute().returnContent();
		String jsonString = content.asString();
		System.out.println("content = " + jsonString);


		Map jsonMap = gson.fromJson(jsonString, Map.class);
		return jsonMap;
	}
}
