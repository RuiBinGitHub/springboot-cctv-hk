package com.springboot.controller;

import java.util.ArrayList;
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
import com.springboot.biz.MarkPipeBiz;
import com.springboot.biz.MarkProjectBiz;
import com.springboot.biz.PersonBiz;
import com.springboot.biz.PipeBiz;
import com.springboot.biz.ProjectBiz;
import com.springboot.entity.MarkProject;
import com.springboot.entity.Person;
import com.springboot.entity.Project;
import com.springboot.util.AppUtils;

@RestController
@RequestMapping(value = "/person")
public class PersonController {

	@Resource
	private PersonBiz personBiz;
	@Resource
	private CompanyBiz companyBiz;
	@Resource
	private ProjectBiz projectBiz;
	@Resource
	private PipeBiz pipeBiz;
	@Resource
	private MarkProjectBiz markProjectBiz;
	@Resource
	private MarkPipeBiz markPipeBiz;
	
	private Map<String, Object> map = null;

	/** 获取人员列表 */
	@RequestMapping(value = "/showlist")
	public ModelAndView showlist(String name, @RequestParam(defaultValue = "1") int page) {
		ModelAndView view = new ModelAndView("person/showlist");
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("company", user.getCompany());
		if (!StringUtils.isEmpty(name))
			map.put("nickname", name);
		int cont = personBiz.getPageCount(map, 15);
		page = page > cont ? cont : page;
		map.put("page", page);
		List<Person> persons = personBiz.findListPerson(map);
		view.addObject("persons", persons);
		view.addObject("page", page);
		view.addObject("cont", cont);
		return view;
	}

	/** 跳转编辑页面 */
	@RequestMapping(value = "/updateview")
	public ModelAndView updateView(@RequestParam(defaultValue = "0") int id) {
		ModelAndView view = new ModelAndView("redirect:/failure");
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", id, "company", user.getCompany());
		Person person = personBiz.findInfoPerson(map);
		if (StringUtils.isEmpty(person))
			return view;
		view.setViewName("person/update");
		view.addObject("person", person);
		return view;
	}

	/** 编辑人员 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(int id, String role) {
		ModelAndView view = new ModelAndView("redirect:/failure");
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", id, "company", user.getCompany());
		Person person = personBiz.findInfoPerson(map);
		if (StringUtils.isEmpty(person))
			return view;
		if ("Role2".equals(role) || "Role3".equals(role) || "Role4".equals(role))
			person.setRole(role);
		personBiz.updatePerson(person);
		view.setViewName("redirect:/success");
		return view;
	}

	/** 删除用户 */
	@RequestMapping(value = "/delete")
	public boolean delete(@RequestParam(defaultValue = "0") int id) {
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", id, "company", user.getCompany());
		Person person = personBiz.findInfoPerson(map);
		if (!StringUtils.isEmpty(person))
			personBiz.removePerson(person);
		return true;
	}

	/** 个人中心 */
	@RequestMapping(value = "/center")
	public ModelAndView center() {
		ModelAndView view = new ModelAndView("person/center");
		Person user = (Person) AppUtils.findMap("user");
		view.addObject("person", user);
		return view;
	}

	/** 修改用户昵称 */
	@RequestMapping(value = "/updatename")
	public boolean updatename(String name) {
		Person user = (Person) AppUtils.findMap("user");
		user.setNickname(name);
		personBiz.updatePerson(user);
		return true;
	}
	
	/** 修改用户密码 */
	@RequestMapping(value = "/updatepass", method = RequestMethod.POST)
	public boolean updatepass(String name, String pass) {
		Person user = (Person) AppUtils.findMap("user");
		if (user.getPassword().equals(name)) {
			user.setPassword(pass);
			personBiz.updatePerson(user);
			return true;
		}
		return false;
	}

	/** 修改登录邮箱 */
	@RequestMapping(value = "/updatemail", method = RequestMethod.POST)
	public boolean updatemail(String mail, String code) {
		Person user = (Person) AppUtils.findMap("user");
		if (!StringUtils.isEmpty(code)) {
			user.setEmail(mail);
			personBiz.updatePerson(user);
		}
		return true;
	}

	@RequestMapping(value = "/showinfo")
	public ModelAndView showinfo(@RequestParam(defaultValue = "0") int id) {
		ModelAndView view = new ModelAndView("redirect:/failure");
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", id, "company", user.getCompany());
		Person person = personBiz.findInfoPerson(map);
		if (StringUtils.isEmpty(person))
			return view;
		// 统计项目
		ArrayList<Integer> counts = new ArrayList<Integer>();
		map = AppUtils.getMap("person", person);
		int count1 = projectBiz.getCountPage(map, 1);
		map = AppUtils.getMap("person", person, "company", person.getCompany());
		int count2 = projectBiz.getCountPage(map, 1);
		counts.add(count1 + count2);
		counts.add(count2);
		// 统计评分
		int count = 0;
		map = AppUtils.getMap("user", person);
		List<MarkProject> markProjects = markProjectBiz.findListMarkProject(map);
		for (int i = 0; markProjects != null && i < markProjects.size(); i++) {
			MarkProject markProject = markProjects.get(i);
			markProjectBiz.setAverage(markProject);
			if (markProject.getScore1() >= 95 && markProject.getScore2() >= 85)
				count++;
		}
		counts.add(markProjects.size());
		counts.add(count);

		view.addObject("person", person);
		view.addObject("counts", counts);
		view.addObject("markProjects", markProjects);
		view.setViewName("person/showinfo");
		return view;
	}

	@RequestMapping(value = "/statistics")
	public ModelAndView statistics(@RequestParam(defaultValue = "0") int id) {
		ModelAndView view = new ModelAndView("user/failure");
		return view;
	}

	@RequestMapping(value = "/statisuser")
	public ModelAndView statisuser(@RequestParam(defaultValue = "0") int id) {
		ModelAndView view = new ModelAndView("person/statisuser");
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", id, "company", user.getCompany());
		Person person = personBiz.findInfoPerson(map);
		if (StringUtils.isEmpty(person))
			return view;
		map = AppUtils.getMap("person", person);
		List<Project> projects1 = projectBiz.findListProject(map);
		map = AppUtils.getMap("person", person, "company", person.getCompany());
		List<Project> projects2 = projectBiz.findListProject(map);
		int pipeSize = pipeBiz.getCount(map); // 编辑管道数量
		
		int count = 0;  
		map = AppUtils.getMap("user", person);
		List<MarkProject> markProjects = markProjectBiz.findListMarkProject(map);
		for (int i = 0; markProjects != null && i < markProjects.size(); i++) {
			MarkProject markProject = markProjects.get(i);
			markProjectBiz.setAverage(markProject);
			if (markProject.getScore1() >= 95 && markProject.getScore2() >= 85)
				count++;
		}
		view.addObject("person", person);
		view.addObject("pipeSize", pipeSize);
		view.addObject("projects1", projects1);  // 未完成项目
		view.addObject("projects2", projects2);  // 已完成项目
		view.addObject("markProjects", markProjects);
		view.addObject("count", count);  // 及格项目
		return view;
	}
}
