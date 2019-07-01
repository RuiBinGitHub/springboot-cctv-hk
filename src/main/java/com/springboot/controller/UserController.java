package com.springboot.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.biz.CompanyBiz;
import com.springboot.biz.PersonBiz;
import com.springboot.entity.Company;
import com.springboot.entity.Person;
import com.springboot.util.AppUtils;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Resource
	private CompanyBiz companyBiz;
	@Resource
	private PersonBiz personBiz;
	@Resource
	private JavaMailSender sender;

	private Map<String, Object> map = null;

	/** 用户注册 */
	@RequestMapping(value = "/logon", method = RequestMethod.POST)
	public ModelAndView logon(Person person) {
		ModelAndView view = new ModelAndView("user/logon");
		String tips1 = "*The UserName already exists！";
		String tips2 = "*The E-mail has been used！";
		String tips3 = "*The company name and serial number mismatch!";
		String tips4 = "*Maximum number of company users!";
		String i18n = (String) AppUtils.findMap("i18n");
		if (i18n != null && !"zh_CN".equals(i18n)) {
			tips1 = "*登錄賬號已存在，請重新輸入！";
			tips2 = "*電子郵箱已註冊，請重新輸入！";
			tips3 = "*公司名稱與序列號不匹配！";
			tips4 = "*公司用戶已經達人數上限！";
		}
		/** 验证登录账号 */
		if (isExistName(person.getUsername())) {
			view.addObject("tips", tips1);
			return view;
		}
		/** 验证电子邮箱 */
		if (isExistMail(person.getEmail())) {
			view.addObject("tips", tips2);
			return view;
		}
		/** 验证公司名称 */
		map = new HashMap<String, Object>();
		map.put("name", person.getCompany().getName());
		map.put("code", person.getCompany().getCode());
		Company company = companyBiz.findInfoCompany(map);
		if (StringUtils.isEmpty(company)) {
			view.addObject("tips", tips3);
			return view;
		}
		map = AppUtils.getMap("company", company);
		int count = personBiz.getPageCount(map, 1);
		if (count >= company.getCont()) {
			view.addObject("tips", tips4);
			return view;
		}
		/** 提交数据，完成注册 */
		person.setPhone("--");
		person.setRole("Role2");
		person.setDate(AppUtils.getDate(null));
		person.setCompany(company);
		personBiz.insertPerson(person);
		view.setViewName("redirect:/user/completes");
		return view;
	}

	/** 用户登录 */
	@RequestMapping(value = "login")
	public ModelAndView login(HttpServletRequest request, String username, String password) {
		try {
			ModelAndView view = new ModelAndView("user/login");
			if (username == null || password == null) {
				view.addObject("username", username);
				view.addObject("password", password);
				return view;
			}
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			SecurityUtils.getSubject().login(token);
			Person user = (Person) AppUtils.findMap("user");
			Company company = user.getCompany();
			Calendar calendar = Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			calendar.add(Calendar.DATE, -company.getTerm());
			String time = format.format(calendar.getTime());
			if (company.getDate().compareTo(time) <= 0) {
				view.addObject("tips", "The Company has expired!");
				return view;
			}
			SecurityUtils.getSubject().getSession().setTimeout(-2000);
			SavedRequest location = WebUtils.getSavedRequest(request);
			if (!StringUtils.isEmpty(location)) {
				String path = location.getRequestUrl();
				view.setViewName("redirect:" + path.substring(5, path.length()));
			} else if ("Role1".equals(user.getRole()))
				view.setViewName("redirect:/company/showlist");
			else
				view.setViewName("redirect:/project/showlist");
			return view;
		} catch (IncorrectCredentialsException e) {
			ModelAndView view = new ModelAndView("user/login");
			view.addObject("tips", "username or password mismatch!");
			view.addObject("username", username);
			view.addObject("password", password);
			return view;
		}
	}

	/** 退出登录 */
	@RequestMapping(value = "leave")
	public ModelAndView leave() {
		ModelAndView view = new ModelAndView();
		view.setViewName("redirect:/user/login");
		SecurityUtils.getSubject().logout();
		AppUtils.removeSession("user");
		return view;
	}

	/** 判断账号是否存在 */
	@RequestMapping(value = "/isexistname")
	public boolean isExistName(@RequestParam("name") String name) {
		map = AppUtils.getMap("username", name);
		if (personBiz.findInfoPerson(map) == null)
			return false;
		else
			return true;
	}

	/** 判断邮箱是否存在 */
	@RequestMapping(value = "/isexistmail")
	public boolean isExistMail(@RequestParam("mail") String mail) {
		map = AppUtils.getMap("email", mail);
		if (personBiz.findInfoPerson(map) == null)
			return false;
		else
			return true;
	}

	/** 判断账号和邮箱 */
	@RequestMapping(value = "/checknamemail")
	public boolean checkNameMail(String username, String mail) {
		map = AppUtils.getMap("username", username, "email", mail);
		if (personBiz.findInfoPerson(map) == null)
			return false;
		else
			return true;
	}

	/** 重置密码 */
	@RequestMapping(value = "/resetpass", method = RequestMethod.POST)
	public ModelAndView resetpass(String username, String password, String email) {
		ModelAndView view = new ModelAndView("user/resetpass");
		map = AppUtils.getMap("username", username, "email", email);
		Person person = personBiz.findInfoPerson(map);
		if (StringUtils.isEmpty(person)) {
			view.addObject("tips", "*UserName and E-Mail mismatch!");
			view.addObject("username", username);
			view.addObject("email", email);
			return view;
		}
		person.setPassword(password);
		personBiz.updatePerson(person);
		view.setViewName("redirect:/success");
		return view;
	}

	/** 发送电子邮件 */
	@RequestMapping(value = "/sendmail")
	public String sendmail(String mail) {
		try {
			MimeMessage mimeMessage = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom("SZMSDI@126.com");
			helper.setTo(mail);
			StringBuffer text = new StringBuffer("【深圳麦斯迪埃】");
			String code = String.valueOf(100000 + (int) (Math.random() * 899999));
			text.append("您正在使用邮箱进行校验，效验码：<a href='#'>" + code + "</a>。");
			text.append("有效时间10分钟，超时请重新获取。(如非本人操作，请忽略该信息)");
			text.append("<p style='color:#999999'>该信息为系统自动发件，请勿回复!</p>");
			helper.setSubject("信息验证");
			helper.setText(text.toString(), true);
			sender.send(mimeMessage);
			return code;
		} catch (MessagingException e) {
			return "";
		}
	}

	/** 切换语言 */
	@RequestMapping(value = "/change")
	public boolean change(String l, HttpServletRequest request) {
		AppUtils.pushMap("i18n", l);
		return true;
	}
	
}
