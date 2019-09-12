package com.springboot.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.bean.AppBean;
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
	private AppBean appBean;

	private Map<String, Object> map = null;
	private UsernamePasswordToken token = null;

	/** 用户登录 */
	@RequestMapping(value = "/login")
	public ModelAndView login(HttpServletRequest request, String username, String password) {
		try {
			ModelAndView view = new ModelAndView("user/login");
			if (username == null || password == null) {  // 账号或密码为空
				view.addObject("username", username);
				view.addObject("password", password);
				return view;
			}
			token = new UsernamePasswordToken(username, password);
			SecurityUtils.getSubject().login(token);
			Person user = (Person) AppUtils.findMap("user");
			if ("0".equals(user.getState())) {  // 账号已经被冻结
				view.addObject("tips", "*This Account is frozen!");
				return view;
			}
			// 计算使用账号期限
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Company company = user.getCompany();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE, -company.getTerm());
			String time = format.format(calendar.getTime());
			if (company.getDate().compareTo(time) <= 0) {
				view.addObject("tips", "The Company has expired!");
				return view;
			}
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
	public ModelAndView resetpass(String username, String password, String mail) {
		ModelAndView view = new ModelAndView("user/resetpass");
		map = AppUtils.getMap("username", username, "email", mail);
		Person person = personBiz.findInfoPerson(map);
		if (StringUtils.isEmpty(person)) {
			view.addObject("tips", "*UserName and E-Mail mismatch!");
			view.addObject("username", username);
			view.addObject("email", mail);
			return view;
		}
		person.setPassword(password);
		personBiz.updatePerson(person);
		view.setViewName("redirect:/success");
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

	/** 发送电子邮件 */
	@RequestMapping(value = "/sendmail")
	public String sendmail(String mail) {
		int random = (int) (Math.random() * 899999);
		String code = String.valueOf(100000 + random);
		appBean.sendMail(mail, code);
		return code;
	}

	/** 切换语言 */
	@RequestMapping(value = "/change")
	public boolean change(String l, HttpServletRequest request) {
		AppUtils.pushMap("i18n", l);
		return true;
	}
	
	@RequestMapping(value = "/index")
	public ModelAndView index() {
		try {
			ModelAndView view = new ModelAndView();
			Person user = (Person) AppUtils.findMap("user");
			if (user.getRole().equals("Role1"))
				view.setViewName("redirect:/company/showlist");
			else
				view.setViewName("redirect:/project/showlist");
			return view;
		} catch (Exception e) {
			ModelAndView view = new ModelAndView();
			view.setViewName("redirect:/project/showlist");
			return view;
		}
	}

	

}
