package com.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
@RequestMapping(value = "/company")
public class CompanyController {

	@Resource
	private UserController userController;
	@Resource
	private CompanyBiz companyBiz;
	@Resource
	private PersonBiz personBiz;

	private Map<String, Object> map = null;
	private Company tempCompany = null;
	private Person tempPerson = null;

	@RequestMapping(value = "/showlist")
	public ModelAndView showlist(String name, @RequestParam(defaultValue = "1") int page) {
		ModelAndView view = new ModelAndView("company/showlist");
		map = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(name))
			map.put("name", name);
		int cont = companyBiz.getCountPage(map, 15);
		page = page > cont ? cont : page;
		map.put("page", page);
		List<Company> companies = companyBiz.findListCompany(map);
		view.addObject("companies", companies);
		view.addObject("page", page);
		view.addObject("cont", cont);
		return view;
	}

	@RequestMapping(value = "/showinfo")
	public ModelAndView showInfo() {
		ModelAndView view = new ModelAndView();
		view.setViewName("compang/showinfo");
		view.addObject("company", tempCompany);
		view.addObject("person", tempPerson);
		return view;
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ModelAndView insert(Company company, Person person) {
		ModelAndView view = new ModelAndView("company/insert");
		if (company == null || person == null)
			return view;
		if (userController.isExistName(person.getUsername()))
			return view;
		companyBiz.appendCompany(company, person);
		view.setViewName("redirect:showinfo");
		tempCompany = company;
		tempPerson = person;
		return view;
	}

	@RequestMapping(value = "/updateview")
	public ModelAndView updateView(@RequestParam(defaultValue = "0") int id) {
		ModelAndView view = new ModelAndView("user/failure");
		Company company = companyBiz.findInfoCompany(id);
		if (company == null)
			return view;
		view.setViewName("company/update");
		view.addObject("company", company);
		return view;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(Company company) {
		ModelAndView view = new ModelAndView("redirect:/success");
		companyBiz.updateCompany(company);
		return view;
	}

	@RequestMapping(value = "/delete")
	public boolean delete(@RequestParam(defaultValue = "0") int id) {
		map = AppUtils.getMap("id", id);
		Company company = companyBiz.findInfoCompany(map);
		if (!StringUtils.isEmpty(company))
			companyBiz.deleteCompany(company);
		return true;
	}
}