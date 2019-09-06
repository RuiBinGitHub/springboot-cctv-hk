package com.springboot.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.biz.ItemBiz;
import com.springboot.biz.MarkPipeBiz;
import com.springboot.biz.MarkProjectBiz;
import com.springboot.biz.ProjectBiz;
import com.springboot.entity.Item;
import com.springboot.entity.MarkPipe;
import com.springboot.entity.MarkProject;
import com.springboot.entity.Person;
import com.springboot.entity.Project;
import com.springboot.util.AppUtils;

@RestController
@RequestMapping(value = "/markinfo")
public class MarkInfoContorller {

	@Resource
	private ProjectBiz projectBiz;
	@Resource
	private MarkProjectBiz markProjectBiz;
	@Resource
	private MarkPipeBiz markPipeBiz;
	@Resource
	private ItemBiz itemBiz;

	@Value(value = "${ImgPath}")
	private String path;
	
	private Map<String, Object> map = null;
	private MarkProject markProject = null;
	private List<MarkProject> markProjects = null;

	/** 获取项目列表 */
	@RequestMapping(value = "/markview")
	public ModelAndView markView(String name, @RequestParam(defaultValue = "1") int page) {
		ModelAndView view = new ModelAndView("markinfo/markview");
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("company", user.getCompany());
		if (!StringUtils.isEmpty(name))
			map.put("name", name);
		int cont = projectBiz.getCountPage(map, 15);
		page = page > cont ? cont : page;
		map.put("page", page);
		map.put("person", user);
		List<Project> projects = projectBiz.markListProject(map);
		view.addObject("projects", projects);
		view.addObject("page", page);
		view.addObject("cont", cont);
		return view;
	}

	/** 获取个人评分列表 */
	@RequestMapping(value = "/marklist")
	public ModelAndView marklist(String name, @RequestParam(defaultValue = "1") int page) {
		ModelAndView view = new ModelAndView("markinfo/marklist");
		Person person = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("person", person);
		if (!StringUtils.isEmpty("name"))
			map.put("name", name);
		int cont = markProjectBiz.getCount(map, 15);
		page = page > cont ? cont : page;
		map.put("page", page);
		markProjects = markProjectBiz.markListMarkProject(map);
		view.addObject("markProjects", markProjects);
		view.addObject("page", page);
		view.addObject("cont", cont);
		return view;
	}

	/** 获取指定项目评分列表 */
	@RequestMapping(value = "/showlist")
	public List<MarkProject> showList(@RequestParam(defaultValue = "0") int id) {
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", id, "company", user.getCompany());
		Project project = projectBiz.findInfoProject(map);
		if (StringUtils.isEmpty(project))
			return null;
		map = AppUtils.getMap("project", project, "person", user);
		markProjects = markProjectBiz.markListMarkProject(map);
		for (MarkProject markProject : markProjects)
			markProject.setPerson(null);
		return markProjects;
	}

	/** 获取项目评分列表 */
	@RequestMapping(value = "/findlist")
	public ModelAndView findList(String name, @RequestParam(defaultValue = "1") int page) {
		ModelAndView view = new ModelAndView("markinfo/findlist");
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("company", user.getCompany());
		if (!StringUtils.isEmpty("name"))
			map.put("name", name);
		int cont = markProjectBiz.getCount(map, 15);
		page = page > cont ? cont : page;
		map.put("page", page);
		markProjects = markProjectBiz.markListMarkProject(map);
		view.addObject("markProjects", markProjects);
		view.addObject("page", page);
		view.addObject("cont", cont);
		return view;
	}

	/** 创建项目评分 */
	@RequestMapping(value = "/insert")
	public ModelAndView insert(@RequestParam(defaultValue = "0") int id) {
		ModelAndView view = new ModelAndView("user/failure");
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", id, "company", user.getCompany());
		Project project = projectBiz.findInfoProject(map);
		if (StringUtils.isEmpty(project))
			return view;
		markProject = new MarkProject();
		markProject.setProject(project);
		markProject.setPerson(user);
		markProject.setDate(AppUtils.getDate(null));
		id = markProjectBiz.appendMarkProject(markProject);
		view.setViewName("redirect:editinfo?id=" + id);
		return view;
	}

	/** 项目评分 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public boolean update(MarkPipe markPipe) {
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", markPipe.getId(), "person", user);
		if (markPipeBiz.findInfoMarkPipe(map) == null)
			return false;
		markPipeBiz.updateMarkPipe(markPipe);
		return true;
	}

	/** 删除项目评分 */
	@RequestMapping(value = "/delete")
	public boolean delete(@RequestParam(defaultValue = "0") int id) {
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", id, "person", user);
		markProject = markProjectBiz.findInfoMarkProject(map);
		if (!StringUtils.isEmpty(markProject))
			markProjectBiz.deleteMarkProject(markProject);
		return true;
	}

	@RequestMapping(value = "/remove")
	public boolean remove(@RequestParam(defaultValue = "0") int id) {
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", id, "company", user.getCompany());
		markProject = markProjectBiz.findInfoMarkProject(map);
		if (!StringUtils.isEmpty(markProject))
			markProjectBiz.deleteMarkProject(markProject);
		return true;
	}

	/** 编辑项目评分 */
	@RequestMapping(value = "/editinfo")
	public ModelAndView editinfo(int id, @RequestParam(defaultValue = "0") int no) {
		ModelAndView view = new ModelAndView("user/failure");
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", id, "person", user);
		markProject = markProjectBiz.findInfoMarkProject(map);
		if (StringUtils.isEmpty(markProject))
			return view;
		map = AppUtils.getMap("markProject", markProject);
		List<MarkPipe> markPipes = markPipeBiz.findListMarkPipe(map);
		if (markPipes != null && markPipes.size() > no) {
			MarkPipe markPipe = markPipes.get(no);
			List<Item> items = itemBiz.findListItem(markPipe.getPipe());
			markPipe.getPipe().setItems(items);
			view.addObject("markPipe", markPipe);
		}
		view.setViewName("markinfo/editinfo");
		view.addObject("markPipes", markPipes);
		view.addObject("path", path);
		return view;
	}

	/** 查看项目评分 */
	@RequestMapping(value = "/findinfo")
	public ModelAndView findinfo(int id, @RequestParam(defaultValue = "0") int no) {
		ModelAndView view = new ModelAndView("user/failure");
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", id, "company", user.getCompany());
		markProject = markProjectBiz.findInfoMarkProject(map);
		if (StringUtils.isEmpty(markProject))
			return view;
		map = AppUtils.getMap("markProject", markProject);
		List<MarkPipe> markPipes = markPipeBiz.findListMarkPipe(map);
		if (markPipes != null && markPipes.size() > no) {
			MarkPipe markPipe = markPipes.get(no);
			List<Item> items = itemBiz.findListItem(markPipe.getPipe());
			markPipe.getPipe().setItems(items);
			view.addObject("markPipe", markPipe);
		}
		view.setViewName("markinfo/findinfo");
		view.addObject("markPipes", markPipes);
		view.addObject("path", path);
		return view;
	}
}
