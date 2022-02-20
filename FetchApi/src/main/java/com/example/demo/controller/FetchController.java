package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FetchController {
	
	@RequestMapping("/getApiResults")
	public String getApiResults() {
		
		return "Yes, you are in!!";
	}

}
