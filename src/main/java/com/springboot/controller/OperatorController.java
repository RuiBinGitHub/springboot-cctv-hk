package com.springboot.controller;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.biz.OperatorBiz;
import com.springboot.entity.Company;
import com.springboot.entity.Operator;
import com.springboot.entity.Person;
import com.springboot.util.AppUtils;

@RestController
@RequestMapping(value = "/operator")
public class OperatorController {

	@Resource
	private OperatorBiz operatorBiz;

	private Map<String, Object> map = null;

	/** 获取人员列表 */
	@RequestMapping(value = "/showlist")
	public ModelAndView findlistOperator(String name, @RequestParam(defaultValue = "1") int page) {
		ModelAndView view = new ModelAndView("operator/showlist");
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("company", user.getCompany());
		if (!StringUtils.isEmpty(name))
			map.put("fullname", name);
		int cont = operatorBiz.getCountPage(map, 15);
		page = page > cont ? cont : page;
		map.put("page", page);
		List<Operator> operators = operatorBiz.findListOperator(map);
		view.addObject("operators", operators);
		view.addObject("page", page);
		view.addObject("cont", cont);
		return view;
	}

	/** 插入数据 */
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ModelAndView insert(Operator operator) {
		ModelAndView view = new ModelAndView("user/failure");
		if (StringUtils.isEmpty(operator.getName()))
			return view;
		Person user = (Person) AppUtils.findMap("user");
		operator.setDate(AppUtils.getDate(null));
		operator.setCompany(user.getCompany());
		operatorBiz.insertOperator(operator);
		view.setViewName("redirect:/success");
		return view;
	}

	/** 获取人员信息 */
	@RequestMapping(value = "/updateview")
	public ModelAndView updateView(@RequestParam(defaultValue = "0") int id) {
		ModelAndView view = new ModelAndView("user/failure");
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", id, "company", user.getCompany());
		Operator operator = operatorBiz.findInfoOperator(map);
		if (StringUtils.isEmpty(operator))
			return view;
		view.setViewName("operator/update");
		view.addObject("operator", operator);
		return view;
	}

	/** 更新数据 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(Operator operator) {
		ModelAndView view = new ModelAndView("user/failure");
		if (StringUtils.isEmpty(operator))
			return view;
		Person user = (Person) AppUtils.findMap("user");
		Company company = user.getCompany();
		map = AppUtils.getMap("id", operator.getId(), "company", company);
		if (operatorBiz.findInfoOperator(map) == null)
			return view;
		operator.setCompany(user.getCompany());
		operatorBiz.updateOperator(operator);
		view.setViewName("redirect:/success");
		return view;
	}

	/** 删除数据 */
	@RequestMapping(value = "/delete")
	public boolean delete(@RequestParam(defaultValue = "0") int id) {
		Person user = (Person) AppUtils.findMap("user");
		Company company = user.getCompany();
		map = AppUtils.getMap("id", id, "company", company);
		Operator operator = operatorBiz.findInfoOperator(map);
		if (!StringUtils.isEmpty(operator))
			operatorBiz.deleteOperator(operator);
		return true;
	}

	@RequestMapping(value = "/isexistname")
	public boolean isExistName(int id, String name) {
		Person user = (Person) AppUtils.findMap("user");
		Company company = user.getCompany();
		map = AppUtils.getMap("fullname", name, "company", company);
		Operator operator = operatorBiz.findInfoOperator(map);
		if (operator == null || operator.getId() == id)
			return false;
		return true;
	}

}
