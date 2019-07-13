package com.springboot.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.springboot.bean.MyRealm;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

	@Resource
	private MyRealm myRealm;

	private SecurityManager manager = null;
	private ShiroFilterFactoryBean factoryBean = null;

	@Bean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
		manager = new DefaultWebSecurityManager(myRealm);
		factoryBean = new ShiroFilterFactoryBean();
		// 设置安全管理器
		factoryBean.setSecurityManager(manager);
		Map<String, String> filterMap = new HashMap<>();
		filterMap.put("/user/**", "anon");
		filterMap.put("/company/**", "roles[role1]");
		filterMap.put("/message/**", "roles[role4]");
		filterMap.put("/operator/**", "roles[role2]");

		filterMap.put("/project/showlist", "roles[role4]");
		filterMap.put("/project/findlist", "roles[role2]");
		filterMap.put("/project/insertview", "roles[role4]");
		filterMap.put("/project/updateview", "roles[role4]");
		filterMap.put("/project/insert", "roles[role4]");
		filterMap.put("/project/update", "roles[role4]");
		filterMap.put("/project/delete", "roles[role4]");
		filterMap.put("/project/remove", "roles[role2]");
		filterMap.put("/project/submit", "roles[role4]");
		filterMap.put("/project/revoke", "roles[role2]");
		filterMap.put("/project/combineview", "roles[role2]");
		filterMap.put("/project/combinelist", "roles[role2]");
		filterMap.put("/project/combine", "roles[role2]");
		filterMap.put("/project/editinfo", "roles[role4]");
		filterMap.put("/project/findinfo", "roles[role4]");
		filterMap.put("/project/importdepth", "roles[role4]");
		filterMap.put("/project/import", "roles[role4]");

		filterMap.put("/pipe/**", "roles[role4]");
		filterMap.put("/item/**", "roles[role4]");
		filterMap.put("/geominfo/**", "roles[role4]");
		filterMap.put("/imapinfo/**", "roles[role4]");

		filterMap.put("/markinfo/markview", "roles[role3]");
		filterMap.put("/markinfo/marklist", "roles[role3]");
		filterMap.put("/markinfo/showlist", "roles[role3]");
		filterMap.put("/markinfo/findlist", "roles[role2]");
		filterMap.put("/markinfo/insert", "roles[role3]");
		filterMap.put("/markinfo/update", "roles[role3]");
		filterMap.put("/markinfo/delete", "roles[role3]");
		filterMap.put("/markinfo/remove", "roles[role2]");
		filterMap.put("/markinfo/editinfo", "roles[role3]");
		filterMap.put("/markinfo/findinfo", "roles[role4]");

		filterMap.put("/person/showlist", "roles[role2]");
		filterMap.put("/person/showinfo", "roles[role2]");
		filterMap.put("/person/updatepass", "roles[role4]");
		filterMap.put("/person/updatemail", "roles[role4]");
		filterMap.put("/person/updateview", "roles[role2]");
		filterMap.put("/person/update", "roles[role2]");
		filterMap.put("/person/delete", "roles[role2]");
		filterMap.put("/person/center", "roles[role4]");

		factoryBean.setFilterChainDefinitionMap(filterMap);
		// 配置跳转的登录页面
		factoryBean.setLoginUrl("/user/loginview");
		// 设置未授权提示页面
		factoryBean.setUnauthorizedUrl("/failure");
		return factoryBean;
	}

	/** 定义识图控制器 */
	public void addViewControllers(ViewControllerRegistry registry) {
		// user 操作
		registry.addViewController("*/loginview").setViewName("user/login");
		registry.addViewController("*/logonview").setViewName("user/logon");
		registry.addViewController("*/resetview").setViewName("user/resetpass");
		registry.addViewController("*/completes").setViewName("user/completes");

		registry.addViewController("/success").setViewName("user/success");
		registry.addViewController("/failure").setViewName("user/failure");
		registry.addViewController("/authoriz").setViewName("authoriz");

		registry.addViewController("/company/insertview").setViewName("company/insert");
		registry.addViewController("/operator/insertview").setViewName("operator/insert");
		registry.addViewController("/project/combineview").setViewName("project/combine");

		registry.addViewController("showmap").setViewName("showmap");
	}

	@Bean
	public ShiroDialect shiroDialect() {
		ShiroDialect dialect = new ShiroDialect();
		return dialect;
	}

	@Bean
	public LocaleResolver localeResolver() {
		return new MyLocaleResolver();
	}
}
