package com.springboot;

import java.util.Map;

import org.springframework.util.StringUtils;

import com.springboot.util.AppUtils;

public class AppTest {

	public static void main(String[] args) {
		Map<String, Object> map = AppUtils.getMap("id", 10 ,"name", "name");
		if (!StringUtils.isEmpty(map.get("name")))
			map.put("name", "%" + map.get("name") + "%");
		System.out.println(map);
		
	}
}
