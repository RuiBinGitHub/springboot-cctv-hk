package com.springboot.bean;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.session.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope(value = "singleton")
@Component(value = "applicationContext")
public class ApplicationContext {

	private Session tempSession = null;
	public Map<String, Session> map = new HashMap<>();

	public void pushMap(String name, Session session) {
		try {
			if ((tempSession = map.get(name)) != null)
				tempSession.stop();
		} catch (Exception e) {
			// 结束异常
		}
		map.put(name, session);
	}

}
