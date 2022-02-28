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
