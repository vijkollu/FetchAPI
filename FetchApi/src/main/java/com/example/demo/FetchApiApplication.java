package com.example.demo;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.diff.GitHubAPI_Search;
import com.example.demo.imple.FetchRAML;

@SpringBootApplication
public class FetchApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FetchApiApplication.class, args);
		try {
			//GitHubAPI_Search.gitHubSearch();
			FetchRAML.downloadRAML("vijkollu","FetchAPI");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
