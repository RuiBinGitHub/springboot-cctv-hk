package com.springboot.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.biz.CodeBiz;
import com.springboot.biz.ItemBiz;
import com.springboot.biz.OperatorBiz;
import com.springboot.biz.PipeBiz;
import com.springboot.biz.ProjectBiz;
import com.springboot.biz.SiteBiz;
import com.springboot.entity.Code;
import com.springboot.entity.Person;
import com.springboot.entity.Pipe;
import com.springboot.entity.Project;
import com.springboot.entity.Site;
import com.springboot.util.AppUtils;
import com.springboot.util.ImportItem;

@RestController
@RequestMapping(value = "/project")
public class ProjectController {

	@Resource
	private OperatorBiz operatorBiz;
	@Resource
	private ImportItem importItem;
	@Resource
	private ProjectBiz projectBiz;
	@Resource
	private PipeBiz pipeBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private CodeBiz codeBiz;
	@Resource
	private SiteBiz siteBiz;

	private Map<String, Object> map = null;
	private List<String> fullnames = null;

	/** 获取个人项目列表 */
	@RequestMapping(value = "/showlist")
	public ModelAndView showList(String name, @RequestParam(defaultValue = "1") int page) {
		ModelAndView view = new ModelAndView("project/showlist");
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("person", user);
		if (!StringUtils.isEmpty(name))
			map.put("name", name);
		int cont = projectBiz.getCountPage(map, 15);
		page = page > cont ? cont : page;
		map.put("page", page);
		List<Project> projects = projectBiz.findListProject(map);
		view.addObject("projects", projects);
		view.addObject("page", page);
		view.addObject("cont", cont);
		return view;
	}

	/** 获取公司项目列表 */
	@RequestMapping(value = "/findlist")
	public ModelAndView findList(String name, @RequestParam(defaultValue = "1") int page) {
		ModelAndView view = new ModelAndView("project/findlist");
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("company", user.getCompany());
		if (!StringUtils.isEmpty(name))
			map.put("name", name);
		int cont = projectBiz.getCountPage(map, 15);
		page = page > cont ? cont : page;
		map.put("page", page);
		List<Project> projects = projectBiz.findListProject(map);
		view.addObject("projects", projects);
		view.addObject("page", page);
		view.addObject("cont", cont);
		return view;
	}

	/** 新建项目信息 */
	@RequestMapping(value = "/insertview")
	public ModelAndView insertView() {
		ModelAndView view = new ModelAndView("project/insert");
		Person user = (Person) AppUtils.findMap("user");
		fullnames = operatorBiz.findFullName(user.getCompany());
		view.addObject("fullnames", fullnames);
		return view;
	}

	/** 新建项目 */
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ModelAndView insert(Project project) {
		ModelAndView view = new ModelAndView("redirect:/failure");
		if (StringUtils.isEmpty(project.getName()))
			return view;
		project.setPerson((Person) AppUtils.findMap("user"));
		projectBiz.appendProject(project);
		view.setViewName("redirect:editinfo?id=" + project.getId());
		return view;
	}

	/** 更新项目信息 */
	@RequestMapping(value = "/updateview")
	public ModelAndView updateView(@RequestParam(defaultValue = "0") int id) {
		ModelAndView view = new ModelAndView("redirect:/failure");
		Person user = (Person) AppUtils.findMap("user");
		Project project = projectBiz.findInfoProject(id, user);
		if (StringUtils.isEmpty(project))
			return view;
		view.setViewName("project/update");
		fullnames = operatorBiz.findFullName(user.getCompany());
		view.addObject("fullnames", fullnames);
		view.addObject("project", project);
		return view;
	}

	/** 更新项目 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(Project project) {
		ModelAndView view = new ModelAndView("redirect:/failure");
		if (StringUtils.isEmpty(project.getName()))
			return view;
		Person user = (Person) AppUtils.findMap("user");
		if (projectBiz.findInfoProject(project.getId(), user) == null)
			return view;
		project.setPerson(user);
		projectBiz.updateProject(project);
		view.setViewName("redirect:/success");
		return view;
	}

	/** 删除项目 */
	@RequestMapping(value = "/delete")
	public boolean delete(@RequestParam(defaultValue = "0") int id) {
		Person user = (Person) AppUtils.findMap("user");
		Project project = projectBiz.findInfoProject(id, user);
		if (!StringUtils.isEmpty(project))
			projectBiz.deleteProject(project);
		return true;
	}

	/** 移除项目 */
	@RequestMapping(value = "/remove")
	public boolean remove(@RequestParam(defaultValue = "0") int id) {
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", id, "company", user.getCompany());
		Project project = projectBiz.findInfoProject(map);
		if (!StringUtils.isEmpty(project))
			projectBiz.deleteProject(project);
		return true;
	}

	/** 提交数据 */
	@RequestMapping(value = "/submit")
	public boolean submit(@RequestParam(defaultValue = "0") int id) {
		Person user = (Person) AppUtils.findMap("user");
		Project project = projectBiz.findInfoProject(id, user);
		if (!StringUtils.isEmpty(project)) {
			project.setCompany(user.getCompany());
			projectBiz.updateProject(project);
		}
		return true;
	}

	/** 撤回项目 */
	@RequestMapping(value = "/revoke")
	public boolean revoke(@RequestParam(defaultValue = "0") int id) {
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", id, "company", user.getCompany());
		Project project = projectBiz.findInfoProject(map);
		if (!StringUtils.isEmpty(project)) {
			project.setCompany(null);
			projectBiz.updateProject(project);
		}
		return true;
	}

	/** 获取项目列表 */
	@RequestMapping(value = "/combinelist")
	public List<Project> combineList(String name) {
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("name", name, "company", user.getCompany());
		List<Project> projects = projectBiz.findListProject(map);
		for (Project project : projects) {
			project.setCompanyName(project.getCompany().getName());
			project.setPersonName(project.getPerson().getNickname());
			project.setCompany(null);
			project.setPerson(null);
		}
		return projects;
	}

	@Transactional
	@RequestMapping(value = "/combine", method = RequestMethod.POST)
	public boolean combin(String list) {
		Person user = (Person) AppUtils.findMap("user");
		List<String> iList = Arrays.asList(list.split(", "));
		int id = Integer.valueOf(iList.get(0));
		map = AppUtils.getMap("id", id, "company", user.getCompany());
		Project project = projectBiz.findInfoProject(map);
		if (StringUtils.isEmpty(project))
			return false;
		for (int i = 1; iList != null && i < iList.size(); i++) {
			id = Integer.valueOf(iList.get(i));
			map = AppUtils.getMap("id", id, "company", user.getCompany());
			Project temp = projectBiz.findInfoProject(map);
			if (StringUtils.isEmpty(temp))
				throw new RuntimeException();
			List<Pipe> pipes = pipeBiz.findListPipe(temp);
			for (int j = 0; pipes != null && j < pipes.size(); j++) {
				pipes.get(i).setProject(project);
				pipeBiz.updatePipe(pipes.get(i));
			}
		}
		itemBiz.sortItemImg(project);
		return true;
	}

	/** 编辑项目 */
	@RequestMapping(value = "/editinfo")
	public ModelAndView editInfo(int id, @RequestParam(defaultValue = "0") int no) {
		ModelAndView view = new ModelAndView("user/failure");
		Person user = (Person) AppUtils.findMap("user");
		Project project = projectBiz.findInfoProject(id, user);
		if (StringUtils.isEmpty(project))
			return view;
		List<Pipe> pipes = pipeBiz.findListPipe(project);
		view.setViewName("project/editinfo");
		view.addObject("pipes", pipes);
		if (pipes != null && pipes.size() != 0) {
			no = no >= pipes.size() ? pipes.size() - 1 : no;
			Pipe pipe = pipes.get(no);
			pipe.setItems(itemBiz.findListItem(pipe));
			view.addObject("pipe", pipe);
		}
		fullnames = operatorBiz.findFullName(user.getCompany());
		List<Site> sites = siteBiz.findListSite(user.getCompany());
		List<Code> codes = codeBiz.findListCode(null);
		view.addObject("fullnames", fullnames);
		view.addObject("project", project);
		view.addObject("sites", sites);
		view.addObject("codes", codes);
		return view;
	}

	/** 项目浏览 */
	@RequestMapping(value = "/findinfo")
	public ModelAndView findInfo(int id, @RequestParam(defaultValue = "0") int no) {
		ModelAndView view = new ModelAndView("user/failure");
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", id, "company", user.getCompany());
		Project project = projectBiz.findInfoProject(map);
		if (StringUtils.isEmpty(project))
			return view;
		List<Pipe> pipes = pipeBiz.findListPipe(project);
		view.setViewName("project/findinfo");
		view.addObject("pipes", pipes);
		if (pipes != null && pipes.size() != 0) {
			no = no >= pipes.size() ? pipes.size() - 1 : no;
			Pipe pipe = pipes.get(no);
			pipe.setItems(itemBiz.findListItem(pipe));
			view.addObject("pipe", pipe);
		}
		return view;
	}

	/** 导入项目 */
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	public ModelAndView inport(MultipartFile file) {
		ModelAndView view = new ModelAndView();
		view.setViewName("redirect:showlist");
		importItem.importMode(file);
		return view;
	}
	
	/** 导入深度 */
	@RequestMapping(value = "/importdepth", method = RequestMethod.POST)
	public boolean importDepth(int id, MultipartFile file) {
		Person user = (Person) AppUtils.findMap("user");
		Project project = projectBiz.findInfoProject(id, user);
		if (project == null || file == null)
			return false;
		projectBiz.importDepth(project, file);
		return true;
	}

}
