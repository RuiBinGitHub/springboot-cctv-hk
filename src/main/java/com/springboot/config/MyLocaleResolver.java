package com.springboot.config;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.LocaleResolver;

import com.springboot.util.AppUtils;

public class MyLocaleResolver implements LocaleResolver {

	public Locale resolveLocale(HttpServletRequest request) {
		Locale locale = new Locale("en", "US");
		String i18n = (String) AppUtils.findMap("i18n");
		if (i18n != null && !"".equals(i18n)) {
			String[] list = i18n.split("_");
			locale = new Locale(list[0], list[1]);
		}
		return locale;
	}

	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		// 设置Locale
	}

}
