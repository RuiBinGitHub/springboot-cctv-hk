package com.springboot.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestController {

	@RequestMapping(value = "/hello1")
	public Map<String, String> hello1() {
		Map<String, String> map = new HashMap<>();
		map.put("pass", "001");
		return map;
	}

	@RequestMapping(value = "/hello2")
	public Map<String, String> hello2() {
		Map<String, String> map = new HashMap<>();
		map.put("pass", "002");
		return map;
	}

	@RequestMapping(value = "/ajax")
	public boolean test(@RequestParam("nickname") String nickname) {
		System.out.println(nickname);
		return true;
	}

	@InitBinder("company")
	public void initBinderCompany(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("company."); // 别名前缀
	}

	@RequestMapping("/index")
	public void index() {
		initpage(10, "123456", "123465");
	}

	@RequestMapping(value = "/init")
	public String initpage(int id, String name, String pass) {
		System.out.println("---" + id);
		return name + "--" + pass;
	}
}
