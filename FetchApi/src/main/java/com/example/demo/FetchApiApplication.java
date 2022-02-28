package com.example.demo;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.impl.FetchRAML;
import com.example.demo.poc.GitHubAPI_Search;

@SpringBootApplication
public class FetchApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FetchApiApplication.class, args);
		try {
			FetchRAML.downloadRAML("vijkollu","Excel","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");
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
