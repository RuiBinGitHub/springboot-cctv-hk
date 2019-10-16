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
	private CompanyBiz companyBiz;
	@Resource
	private PersonBiz personBiz;

	private Map<String, Object> map = null;

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
	public ModelAndView showInfo(@RequestParam(defaultValue = "0") int id) {
		ModelAndView view = new ModelAndView("user/failure");
		Company company = companyBiz.findInfoCompany(id);
		if (StringUtils.isEmpty(company))
			return view;
		map = AppUtils.getMap("company", company);
		List<Person> persons = personBiz.findListPerson(map);
		view.setViewName("company/showinfo");
		view.addObject("company", company);
		view.addObject("persons", persons);
		return view;
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ModelAndView insert(Company company) {
		ModelAndView view = new ModelAndView("company/insert");
		if (company.getName() == null)
			return view;
		map = AppUtils.getMap("define", company.getDefine());
		if (companyBiz.findInfoCompany(map) != null) {
			view.addObject("tips", "*账号前缀已被使用！");
			view.addObject("company", company);
			return view;
		}
		 int id = companyBiz.appendCompany(company);
		 view.setViewName("redirect:showinfo?id=" + id);
		return view;
	}

	@RequestMapping(value = "/updateview")
	public ModelAndView updateView(@RequestParam(defaultValue = "0") int id) {
		ModelAndView view = new ModelAndView("user/failure");
		Company company = companyBiz.findInfoCompany(id);
		if (StringUtils.isEmpty(company))
			return view;
		view.setViewName("company/update");
		view.addObject("company", company);
		return view;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(Company company) {
		ModelAndView view = new ModelAndView();
		view.setViewName("redirect:/success");
		companyBiz.repeatCompany(company);
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
