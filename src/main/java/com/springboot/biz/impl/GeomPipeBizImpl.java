package com.springboot.biz.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.biz.GeomItemBiz;
import com.springboot.biz.GeomPipeBiz;
import com.springboot.biz.ItemBiz;
import com.springboot.dao.GeomPipeDao;
import com.springboot.entity.GeomItem;
import com.springboot.entity.GeomPipe;
import com.springboot.entity.GeomProject;
import com.springboot.entity.Item;
import com.springboot.entity.Person;
import com.springboot.util.AppUtils;
import com.springboot.util.Computes;

@Transactional
@Service(value = "geomPipeBiz")
public class GeomPipeBizImpl implements GeomPipeBiz {
	@Resource
	private Computes computes;
	@Resource
	private GeomPipeDao geomPipeDao;
	@Resource
	private GeomItemBiz geomItemBiz;
	@Resource
	private ItemBiz itemBiz;

	private Map<String, Object> map = null;

	public void insertGeomPipe(GeomPipe geomPipe) {
		geomPipeDao.insertGeomPipe(geomPipe);
	}

	public void updateGeomPipe(GeomPipe geomPipe) {
		geomPipeDao.updateGeomPipe(geomPipe);
	}

	public void deleteGeomPipe(GeomPipe geomPipe) {
		geomPipeDao.deleteGeomPipe(geomPipe);
	}

	public GeomPipe findInfoGeomPipe(Map<String, Object> map) {
		return geomPipeDao.findInfoGeomPipe(map);
	}

	public List<GeomPipe> findListGeomPipe(Map<String, Object> map) {
		return geomPipeDao.findListGeomPipe(map);
	}

	public GeomPipe findInfoGeomPipe(int id, Person person) {
		map = AppUtils.getMap("id", id, "person", person);
		return geomPipeDao.findInfoGeomPipe(map);
	}

	public List<GeomPipe> findListGeomPipe(GeomProject geomProject) {
		map = AppUtils.getMap("geomProject", geomProject);
		return this.findListGeomPipe(map);
	}

	public void appendGeomPipe(GeomPipe geomPipe) {
		this.insertGeomPipe(geomPipe);
		String type = geomPipe.getPipe().getProject().getStandard();
		List<GeomItem> geomItems = new ArrayList<>();
		List<Item> items = itemBiz.findListItem(geomPipe.getPipe());
		for (int i = 0; items != null && i < items.size(); i++) {
			Item item = computes.computeItem(items.get(i), type);
			GeomItem geomItem = new GeomItem();
			geomItem.setScore(item.getScore());
			geomItem.setGrade(item.getGrade());
			geomItem.setPicture(item.getPicture());
			geomItem.setItem(item);
			geomItem.setGeomPipe(geomPipe);
			geomItemBiz.insertGeomItem(geomItem);
			geomItems.add(geomItem);
		}
		geomPipe.setGeomItems(geomItems);
	}

	public Map<String, Double> findSMHGradeA(String smhGradeA) {
		return geomPipeDao.findSMHGradeA(smhGradeA);
	}

	public Map<String, Double> findFMHGradeA(String fmhGradeA) {
		return geomPipeDao.findFMHGradeA(fmhGradeA);
	}
	
	@Transactional
	public void replacePipegeom(GeomPipe geomPipe) {
		String smhGradeA = findGrideA(geomPipe.getSmhX(), geomPipe.getSmhY()); // 计算管道起始点GradeA编号
		String fmhGradeA = findGrideA(geomPipe.getFmhX(), geomPipe.getFmhY()); // 计算管道终止点GradeA编号
		geomPipe.setSmhGradeA(smhGradeA + geomPipe.getPipe().getUse());
		geomPipe.setFmhGradeA(fmhGradeA + geomPipe.getPipe().getUse());
		String smhGradeB = findGrideB(geomPipe.getSmhX(), geomPipe.getSmhY()); // 计算管道起始点GradeB编号
		String fmhGradeB = findGrideB(geomPipe.getFmhX(), geomPipe.getFmhY()); // 计算管道终止点GradeB编号
		geomPipe.setSmhGradeB(smhGradeB + geomPipe.getPipe().getUse());
		geomPipe.setFmhGradeB(fmhGradeB + geomPipe.getPipe().getUse());
		this.updateGeomPipe(geomPipe);
		/****************************************************************/
		// 获取输入井号平均坐标值
		smhGradeA = geomPipe.getSmhGradeA();
		fmhGradeA = geomPipe.getFmhGradeA();
		Map<String, Double> map1 = this.findSMHGradeA(smhGradeA);
		Map<String, Double> map2 = this.findFMHGradeA(fmhGradeA);
		map = AppUtils.getMap("smhGradeA", smhGradeA, "fmhGradeA", fmhGradeA);
		List<GeomPipe> geomPipes = this.findListGeomPipe(map);
		for (GeomPipe geompipe : geomPipes) {
			geompipe.setActualX1(map1.get("mhx"));
			geompipe.setActualY1(map1.get("mhy"));
			geompipe.setActualX2(map2.get("mhx"));
			geompipe.setActualY2(map2.get("mhy"));
			this.updateGeomPipe(geompipe);
			// 设置记录的坐标值
			List<GeomItem> geomItems = geomItemBiz.findListGeomItem(geompipe);
			for (GeomItem geomItem : geomItems) {
				double dist = Double.valueOf(geomItem.getItem().getDist());
				double proport = dist / geompipe.getPipe().getTotallength();
				double x = (map2.get("mhx") - map1.get("mhx")) * proport + map1.get("mhx");
				double y = (map2.get("mhy") - map2.get("mhy")) * proport + map1.get("mhy");
				geomItem.setX((double) (int) (x * 1000) / 1000);
				geomItem.setY((double) (int) (y * 1000) / 1000);
				geomItemBiz.updateGeomItem(geomItem);
			}
		}
	}
	
	/** 计算GradeA */
	private String findGrideA(double x, double y) {
		String x1 = ((int) x / 1000) % 100 + "";
		String y1 = ((int) y / 1000) % 100 + "";
		String x2 = (int) x % 1000 + "";
		String y2 = (int) y % 1000 + "";
		return x1 + y1 + x2 + y2;
	}

	/** 计算GradeB */
	private String findGrideB(double x, double y) {
		String x1 = ((int) x / 1000) % 100 + "";
		String y1 = ((int) y / 1000) % 100 + "";
		String x2 = (int) x % 1000 + "";
		String y2 = (int) y % 1000 + "";
		String x3 = (int) (x * 100) % 100 + "";
		String y3 = (int) (y * 100) % 100 + "";
		return x1 + y1 + x2 + y2 + x3 + y3;
	}


}
