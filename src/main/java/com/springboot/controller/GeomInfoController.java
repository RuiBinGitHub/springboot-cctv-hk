package com.springboot.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.biz.GeomPipeBiz;
import com.springboot.biz.GeomProjectBiz;
import com.springboot.biz.ProjectBiz;
import com.springboot.entity.GeomPipe;
import com.springboot.entity.GeomProject;
import com.springboot.entity.Person;
import com.springboot.entity.Project;
import com.springboot.util.AppUtils;

@RestController
@RequestMapping(value = "/geominfo")
public class GeomInfoController {

	@Resource
	private ProjectBiz projectBiz;
	@Resource
	private GeomProjectBiz geomProjectBiz;
	@Resource
	private GeomPipeBiz geomPipeBiz;

	private GeomProject geomProject = null;
	private Map<String, Object> map = null;

	@RequestMapping(value = "/input")
	public ModelAndView input(@RequestParam(defaultValue = "0") int id) {
		ModelAndView view = new ModelAndView("user/failure");
		Person user = (Person) AppUtils.findMap("user");
		Project project = projectBiz.findInfoProject(id, user);
		if (StringUtils.isEmpty(project))
			return view;
		map = AppUtils.getMap("project", project, "person", user);
		geomProject = geomProjectBiz.findInfoGeomProject(map);
		List<GeomPipe> geomPipes = null;
		if (StringUtils.isEmpty(geomProject)) {
			geomProject = new GeomProject();
			geomProject.setProject(project);
			geomProjectBiz.appendGeomProject(geomProject);
		} else {
			geomPipes = geomPipeBiz.findListGeomPipe(geomProject);
			geomProject.setGeomPipes(geomPipes);
		}
		List<String> extent = null;
		if (!StringUtils.isEmpty(geomProject.getExtent()))
			extent = Arrays.asList(geomProject.getExtent().split(","));
		view.setViewName("geominfo/input");
		view.addObject("geomProject", geomProject);
		view.addObject("extent", extent);
		return view;
	}

	@RequestMapping(value = "/delete")
	public ModelAndView delete(@RequestParam(defaultValue = "0") int id) {
		ModelAndView view = new ModelAndView("redirect:/success");
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", id, "person", user);
		geomProject = geomProjectBiz.findInfoGeomProject(map);
		geomProjectBiz.deleteGeomProject(geomProject);
		return view;
	}

	/** 输入项目范围 */
	@RequestMapping(value = "/inputextents", method = RequestMethod.POST)
	public boolean inputExtents(int id, String extent) {
		Person user = (Person) AppUtils.findMap("user");
		geomProject = geomProjectBiz.findInfoGeomProject(id, user);
		if (StringUtils.isEmpty(geomProject))
			return false;
		geomProject.setExtent(extent);
		geomProjectBiz.updateGeomProject(geomProject);
		return true;
	}

	/** 导入管道坐标 */
	@RequestMapping(value = "/importvalue", method = RequestMethod.POST)
	public boolean importValue(int id, MultipartFile file) {
		Person user = (Person) AppUtils.findMap("user");
		geomProject = geomProjectBiz.findInfoGeomProject(id, user);
		if (StringUtils.isEmpty(geomProject))
			return false;
		geomProjectBiz.importValue(geomProject, file);
		return true;
	}

	/** 输入管道坐标 */
	@RequestMapping(value = "/inputvalue", method = RequestMethod.POST)
	public boolean inputValue(int id, GeomPipe geomPipe) {
		Person user = (Person) AppUtils.findMap("user");
		GeomPipe geomTepm = geomPipeBiz.findInfoGeomPipe(id, user);
		if (StringUtils.isEmpty(geomTepm))
			return false;
		// 设置管道的地图位置
		geomTepm.setSmhX(geomPipe.getSmhX());
		geomTepm.setSmhY(geomPipe.getSmhY());
		geomTepm.setSmhH(geomPipe.getSmhH());
		geomTepm.setFmhX(geomPipe.getFmhX());
		geomTepm.setFmhY(geomPipe.getFmhY());
		geomTepm.setFmhH(geomPipe.getFmhH());
		geomPipeBiz.replacePipegeom(geomTepm);
		return true;
	}

	/** 修改管道编码 */
	@RequestMapping(value = "/updategrade", method = RequestMethod.POST)
	public boolean updateGrade(int id, GeomPipe geomPipe) {
		Person user = (Person) AppUtils.findMap("user");
		GeomPipe geomTepm = geomPipeBiz.findInfoGeomPipe(id, user);
		if (StringUtils.isEmpty(geomTepm))
			return false;
		geomTepm.setSmhGradeA(geomPipe.getSmhGradeA());
		geomTepm.setSmhGradeB(geomPipe.getSmhGradeB());
		geomTepm.setFmhGradeA(geomPipe.getFmhGradeA());
		geomTepm.setFmhGradeB(geomPipe.getFmhGradeB());
		geomPipeBiz.updateGeomPipe(geomTepm);
		return true;
	}

	/** 显示地图信息 */
	@RequestMapping(value = "/showmap")
	public ModelAndView showMap(String option, String value) {
		ModelAndView view = new ModelAndView("geominfo/showmap");
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("company", user.getCompany(), "extent", "000000");
		List<GeomProject> geomProjects = geomProjectBiz.findListGeomProject(map);
		for (GeomProject geomproject : geomProjects)
			geomProjectBiz.computeCenter(geomproject);
		view.addObject("geomProjects", geomProjects);
		return view;
	}

}
