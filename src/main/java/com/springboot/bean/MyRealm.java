package com.springboot.bean;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import com.springboot.biz.PersonBiz;
import com.springboot.entity.Company;
import com.springboot.entity.Person;
import com.springboot.util.AppUtils;

@Component(value = "myRealm")
public class MyRealm extends AuthorizingRealm {

	@Resource
	private PersonBiz personBiz;
	private Map<String, Object> map = null;

	/** 执行授权逻辑 */
	public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection collection) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Person person = (Person) collection.getPrimaryPrincipal();
		if ("Role1".equals(person.getRole())) {
			info.addRole("role1");
		} else if ("Role2".equals(person.getRole())) {
			info.addRole("role2");
			info.addRole("role4");
		} else if ("Role3".equals(person.getRole())) {
			info.addRole("role3");
			info.addRole("role4");
		} else if ("Role4".equals(person.getRole()))
			info.addRole("role4");
		Company company = person.getCompany();
		if ("版本 1.2".equals(company.getVersion()))
			info.addRole("vrole");
		return info;
	}

	/** 执行认证逻辑 */
	public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
		AuthenticationInfo info = null;
		AuthenticationException exception = null;
		UsernamePasswordToken tempToken = (UsernamePasswordToken) token;
		String username = tempToken.getUsername();
		String password = new String((char[]) tempToken.getCredentials());
		map = AppUtils.getMap("username", username, "password", password);
		Person user = personBiz.findInfoPerson(map);
		if (user == null) { // 账号密码错误
			exception = new IncorrectCredentialsException();
			throw exception;
		}
		AppUtils.pushMap("user", user);
		info = new SimpleAuthenticationInfo(user, password, "");
		return info;
	}
}
