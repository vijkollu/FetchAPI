package com.example.demo.poc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GitHubAPI_Search {

	private static Gson gson;

	private static String GITHUB_API_BASE_URL = "https://api.github.com/";

	private static String GITHUB_API_SEARCH_CODE_PATH = "search/code?q=";

	private static String GITHUB_API_SEARCH_ISSUES_PATH = "search/issues";

	private static String GITHUB_API_SEARCH_COMMITS_PATH = "search/commits";

	public static void gitHubSearch() throws IOException, URISyntaxException {

		gson = new GsonBuilder().setPrettyPrinting().create();

		getApiName();

		// searchCodeByContent();

		// searchPullRequests();

		// searchCommits();

	}

	private static void searchCommits() throws ClientProtocolException, IOException {

		String commitsQuery = "?q=author:garydgregory+committer-date:%3e2022-02-10";

		Map commitsSearchResult = makeRESTCall(GITHUB_API_BASE_URL + GITHUB_API_SEARCH_COMMITS_PATH + commitsQuery,
				"application/vnd.github.cloak-preview");

		System.out.println("Total number or results = " + commitsSearchResult.get("total_count"));
		gson.toJsonTree(commitsSearchResult).getAsJsonObject().get("items").getAsJsonArray()
				.forEach(r -> System.out
						.println("\n\t | Message: " + r.getAsJsonObject().get("commit").getAsJsonObject().get("message")
								+ "\n\t | Date: " + r.getAsJsonObject().get("commit").getAsJsonObject().get("committer")
										.getAsJsonObject().get("date")));
	}

	private static void searchPullRequests() throws ClientProtocolException, IOException {

		String pullRequestsQuery = "?q=number+repo:apache/commons-lang+type:pr+state:open+base:master&sort=created&order=asc";

		Map pullRequestsSearchResult = makeRESTCall(
				GITHUB_API_BASE_URL + GITHUB_API_SEARCH_ISSUES_PATH + pullRequestsQuery);

		System.out.println("Total number or results = " + pullRequestsSearchResult.get("total_count"));
		gson.toJsonTree(pullRequestsSearchResult).getAsJsonObject().get("items").getAsJsonArray()
				.forEach(r -> System.out.println("\tTitle: " + r.getAsJsonObject().get("title") + "\n\t\t | By User: "
						+ r.getAsJsonObject().get("user").getAsJsonObject().get("login") + "\n\t\t | Path: "
						+ r.getAsJsonObject().get("pull_request").getAsJsonObject().get("html_url")));
	}

	private static void searchCodeByContent() throws ClientProtocolException, IOException {

		String codeContentQuery = "containsAny+in:file+language:java+repo:apache/commons-lang";

		Map contentSearchResult = makeRESTCall(GITHUB_API_BASE_URL + GITHUB_API_SEARCH_CODE_PATH + codeContentQuery,
				"application/vnd.github.v3.text-match+json");

		System.out.println("Total number or results = " + contentSearchResult.get("total_count"));
		gson.toJsonTree(contentSearchResult).getAsJsonObject().get("items").getAsJsonArray().forEach(r -> {
			System.out.println("\tFile: " + r.getAsJsonObject().get("name") + "\n\t\t | Repo: "
					+ r.getAsJsonObject().get("repository").getAsJsonObject().get("html_url") + "\n\t\t | Path: "
					+ r.getAsJsonObject().get("path"));

			r.getAsJsonObject().get("text_matches").getAsJsonArray()
					.forEach(t -> System.out.println("\t\t| Matched line: " + t.getAsJsonObject().get("fragment")));
		});
	}

	private static void searchFileByFileName(String input) throws ClientProtocolException, IOException {

		String codeFileQuery = "filename:" + input + "+extension:yaml+org:vijkollu";

		Map fileNameSearchResult = makeRESTCall(GITHUB_API_BASE_URL + GITHUB_API_SEARCH_CODE_PATH + codeFileQuery);
		
		System.out.println(GITHUB_API_BASE_URL + GITHUB_API_SEARCH_CODE_PATH + codeFileQuery);
		System.out.println("Total number or results = " + fileNameSearchResult.get("total_count"));
		gson.toJsonTree(fileNameSearchResult).getAsJsonObject().get("items").getAsJsonArray()
				.forEach(r -> System.out.println("\tFile: " + r.getAsJsonObject().get("name") + "\n\t\t | Repo: "
						+ r.getAsJsonObject().get("repository").getAsJsonObject().get("html_url") + "\n\t\t | Path: "
						+ r.getAsJsonObject().get("path")));
	}

	private static void test() throws ClientProtocolException, IOException {
	}

	private static Map makeRESTCall(String restUrl, String acceptHeaderValue)
			throws ClientProtocolException, IOException {
		Request request = Request.Get(restUrl);

		if (acceptHeaderValue != null && !acceptHeaderValue.isBlank()) {
			request.addHeader("Accept", acceptHeaderValue);
		}

		Content content = request.execute().returnContent();
		String jsonString = content.asString();
		System.out.println();
		System.out.println("Response:::JSON");
		System.out.println(jsonString);
		System.out.println();
		System.out.println();

		Map jsonMap = gson.fromJson(jsonString, Map.class);
		return jsonMap;
	}

	private static Map makeRESTCall(String restUrl) throws ClientProtocolException, IOException {
		
		return makeRESTCall(restUrl, null);
	}

	public static void getApiName() {
		try {
			FileInputStream file = new FileInputStream(new File("demo.xlsx"));

			XSSFWorkbook workbook = new XSSFWorkbook(file);

			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) {

				Row row = rowIterator.next();

				Iterator<Cell> cellIterator = row.cellIterator();
				int i = 0;

				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();

					if (i == 1) {

						searchFileByFileName(cell.getStringCellValue());
					}
					i++;

				}
				System.out.println("");

			}
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
