package com.springboot.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.biz.CameBiz;
import com.springboot.biz.GeomPipeBiz;
import com.springboot.biz.GeomProjectBiz;
import com.springboot.biz.ItemBiz;
import com.springboot.entity.GeomPipe;
import com.springboot.entity.Pipe;
import com.springboot.util.AppUtils;
import com.springboot.util.Computes;

@RestController
@RequestMapping(value = "/imapinfo")
public class MapInfoController {

	@Resource
	private GeomProjectBiz geomProjectBiz;
	@Resource
	private GeomPipeBiz geomPipeBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private Computes computes;
	@Resource
	private CameBiz cameBiz;

	private Map<String, Object> map = null;
	// private List<GeomProject> GeomProjects = null;
	private List<GeomPipe> geomPipes = null;

	@RequestMapping(value = "/showlist")
	public ModelAndView showlist(String option, String name) {
		ModelAndView view = new ModelAndView("imapinfo/showlist");
		map = AppUtils.getMap("actualX1", "null", "actualX2", "null");
		geomPipes = geomPipeBiz.findListGeomPipe(map);
		for (int i = 0; geomPipes != null && i < geomPipes.size(); i++) {
			Pipe pipe = geomPipes.get(i).getPipe();
			computes.computePipe(pipe, pipe.getProject().getStandard());
			double value1 = pipe.getGrade()[0], value2 = pipe.getGrade()[3];
			geomPipes.get(i).setGrade(value1 > value2 ? value1 : value2);
		}
		view.addObject("geomPipes", geomPipes);
		return view;
	}

	@RequestMapping(value = "/findinfo")
	public GeomPipe findInfo(@RequestParam(defaultValue = "0") int id) {
		GeomPipe geomPipe = geomPipeBiz.findInfoGeomPipe(id, null);
		if (!StringUtils.isEmpty(geomPipe)) {
			Pipe pipe = geomPipe.getPipe();
			computes.computePipe(pipe, pipe.getProject().getStandard());
			pipe.setMaterial(cameBiz.getCameValue(pipe.getMaterial(), "material"));
			pipe.setShape(cameBiz.getCameValue(pipe.getShape(), "shape"));
			pipe.setProject(null);
		}
		return geomPipe;
	}
}
