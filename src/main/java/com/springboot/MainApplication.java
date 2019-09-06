package com.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MainApplication {

	/** 项目主程序 */
	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
	
}
