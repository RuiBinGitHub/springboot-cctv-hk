package com.springboot.bean;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value = "applicationContext")
@Scope(value = "singleton")
public class ApplicationContext {

	public Map<String, HttpSession> map = new HashMap<>();
	
	public void pushMap(HttpSession session) {
		
	}
	
}
