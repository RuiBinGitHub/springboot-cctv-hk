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
		filterMap.put("/company/**", "roles[role1]"); // 公司操作
		filterMap.put("/message/**", "roles[role4]"); // 消息操作
		filterMap.put("/operator/**", "roles[role2]"); // 人员管理

		filterMap.put("/project/showlist", "roles[role4]"); // 个人列表
		filterMap.put("/project/findlist", "roles[role2]"); // 项目管理
		filterMap.put("/project/insertview", "roles[role4]"); // 新建项目链接
		filterMap.put("/project/updateview", "roles[role4]"); // 更新项目链接
		filterMap.put("/project/insert", "roles[role4]"); // 新建项目
		filterMap.put("/project/update", "roles[role4]"); // 更新项目
		filterMap.put("/project/delete", "roles[role4]"); // 删除项目
		filterMap.put("/project/remove", "roles[role2]"); // 移除项目
		filterMap.put("/project/submit", "roles[role4]"); // 提交项目
		filterMap.put("/project/revoke", "roles[role2]"); // 撤回项目
		filterMap.put("/project/combineview", "roles[role2]"); // 合并项目
		filterMap.put("/project/combinelist", "roles[role2]"); // 合并列表
		filterMap.put("/project/combine", "roles[role2]"); // 项目合并
		filterMap.put("/project/editinfo", "roles[role4]"); // 编辑项目
		filterMap.put("/project/findinfo", "roles[role4]"); // 浏览项目
		
		filterMap.put("/project/importdepth", "roles[role4]"); // 导入项目
		filterMap.put("/project/import", "roles[role4]"); // 坐标导入

		filterMap.put("/pipe/**", "roles[role4]"); // 管道操作
		filterMap.put("/item/**", "roles[role4]"); // 记录操作
		filterMap.put("/geominfo/**", "roles[vrole,role4]"); // 坐标操作
		filterMap.put("/imapinfo/**", "roles[vrole,role4]"); // 展示操作

		filterMap.put("/markinfo/markview", "roles[vrole,role3]"); // 评分项目
		filterMap.put("/markinfo/marklist", "roles[vrole,role3]"); // 评分列表
		filterMap.put("/markinfo/showlist", "roles[vrole,role3]"); // 评分列表
		filterMap.put("/markinfo/findlist", "roles[vrole,role2]"); // 评分管理
		filterMap.put("/markinfo/insert", "roles[vrole,role3]"); // 新建评分
		filterMap.put("/markinfo/update", "roles[vrole,role3]"); // 更新评分
		filterMap.put("/markinfo/delete", "roles[vrole,role3]"); // 删除评分
		filterMap.put("/markinfo/remove", "roles[vrole,role2]"); // 移除评分
		filterMap.put("/markinfo/editinfo", "roles[vrole,role3]"); // 评分编辑
		filterMap.put("/markinfo/findinfo", "roles[vrole,role4]"); // 评分浏览

		filterMap.put("/person/showlist", "roles[role2]"); // 人员管理
		filterMap.put("/person/showinfo", "roles[role2]"); // 人员信息
		filterMap.put("/person/updatepass", "roles[role4]"); // 更新密码
		filterMap.put("/person/updatemail", "roles[role4]"); // 更新邮箱
		filterMap.put("/person/updateview", "roles[role2]"); // 更新人员链接
		filterMap.put("/person/update", "roles[role2]"); // 更新人员
		filterMap.put("/person/delete", "roles[role2]"); // 删除人员
		filterMap.put("/person/center", "roles[role4]"); // 个人中心
		filterMap.put("/person/statistics", "roles[role2]"); // 
		filterMap.put("/person/statisuser", "roles[role2]"); // 

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
