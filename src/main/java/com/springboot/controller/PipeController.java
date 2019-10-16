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

import com.springboot.biz.ItemBiz;
import com.springboot.biz.PipeBiz;
import com.springboot.biz.ProjectBiz;
import com.springboot.biz.SiteBiz;
import com.springboot.entity.Item;
import com.springboot.entity.Person;
import com.springboot.entity.Pipe;
import com.springboot.entity.Project;
import com.springboot.entity.Site;
import com.springboot.util.AppUtils;

@RestController
@RequestMapping(value = "/pipe")
public class PipeController {

	@Value("${mypath}")
	private String path;

	@Resource
	private ProjectBiz projectBiz;
	@Resource
	private PipeBiz pipeBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private SiteBiz siteBiz;

	private Map<String, Object> map = null;

	/** 插入数据 */
	@RequestMapping(value = "/insert")
	public boolean insertPipe(int id, int no) {
		Person person = (Person) AppUtils.findMap("user");
		Project project = projectBiz.findInfoProject(id, person);
		if (StringUtils.isEmpty(project))
			return false;
		Pipe pipe = new Pipe();
		map = AppUtils.getMap("id", no);
		Pipe temp = pipeBiz.findInfoPipe(map);
		if (!StringUtils.isEmpty(temp)) {
			pipe.setNo(temp.getNo() + 1);
			pipe.setWorkorder(temp.getWorkorder());
			pipe.setSlope(temp.getSlope());
			pipe.setSloperef(temp.getSloperef());
			pipe.setYearlaid(temp.getYearlaid());
			pipe.setDate(temp.getDate());
			pipe.setDistrict1(temp.getDistrict1());
			pipe.setDistrict2(temp.getDistrict2());
			pipe.setDistrict3(temp.getDistrict3());
			pipe.setRoadname(temp.getRoadname());
			pipe.setHousenum(temp.getHousenum());
			pipe.setBuilding(temp.getBuilding());
			pipe.setDivision(temp.getDivision());
			pipe.setAreacode(temp.getAreacode());
			pipe.setPipelength(temp.getPipelength());
		} else {
			pipe.setNo(1);
			pipe.setYearlaid("0");
			pipe.setDate(project.getDate());
			pipe.setSlope(project.getSlope());
			pipe.setSloperef("N/A");
			pipe.setDivision("--");
		}
		pipe.setOperator(project.getOperator());
		pipe.setPurposes("Structural defects");
		pipe.setCategory("Z");
		pipe.setCleaned("N");
		pipe.setWeather("1");
		pipe.setProject(project);
		pipeBiz.appendPipe(pipe);
		return true;
	}

	/** 更新数据 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public boolean updatePipe(Pipe pipe) {
		Person user = (Person) AppUtils.findMap("user");
		if (pipeBiz.findInfoPipe(pipe.getId(), user) == null)
			return false;
		pipeBiz.updatePipe(pipe);
		Site site = siteBiz.findInfoSite(pipe.getRoadname(), user.getCompany());
		if (StringUtils.isEmpty(site)) {
			site = new Site();
			site.setValue(pipe.getRoadname());
			site.setOption("roadname");
			site.setCompany(user.getCompany());
			siteBiz.insertSite(site);
		}
		List<Item> items = pipe.getItems();
		for (int i = 0; items != null && i < items.size(); i++) {
			Item item = items.get(i);
			String data = item.getPicture();
			if ("#已移除#".equals(item.getPhoto()))
				items.get(i).setPhoto("");
			if (data != null && data.length() > 40) {
				String name = AppUtils.UUIDCode();
				AppUtils.saveImage(data, path, name);
				item.setPicture(name);
			}
			item.setNo(i);
			item.setPipe(pipe);
			if (item.getId() == 0)
				itemBiz.insertItem(item);
			else
				itemBiz.updateItem(item);
		}
		itemBiz.sortItemImg(pipe.getProject());
		return true;
	}

	/** 删除数据 */
	@RequestMapping(value = "/delete")
	public boolean daletePipe(@RequestParam(defaultValue = "0") int id) {
		Person user = (Person) AppUtils.findMap("user");
		Pipe pipe = pipeBiz.findInfoPipe(id, user);
		if (StringUtils.isEmpty(pipe))
			return false;
		pipeBiz.deletePipe(pipe);
		itemBiz.sortItemImg(pipe.getProject());
		return true;
	}

}
