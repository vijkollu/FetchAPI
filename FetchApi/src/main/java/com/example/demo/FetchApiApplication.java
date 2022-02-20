package com.example.demo;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.logic.GitHubAPI_Search;

@SpringBootApplication
public class FetchApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FetchApiApplication.class, args);
		try {
			GitHubAPI_Search.gitHubSearch();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
