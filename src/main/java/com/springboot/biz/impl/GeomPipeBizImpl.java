package com.springboot.biz.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.biz.GeomPipeBiz;
import com.springboot.dao.GeomPipeDao;
import com.springboot.entity.GeomPipe;
import com.springboot.entity.GeomProject;
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

	public Map<String, Double> findSMHGradeA(String smhGradeA) {
		return geomPipeDao.findSMHGradeA(smhGradeA);
	}

	public Map<String, Double> findFMHGradeA(String fmhGradeA) {
		return geomPipeDao.findFMHGradeA(fmhGradeA);
	}

	public void replacePipegeom(GeomPipe geomPipe) {
		String use = geomPipe.getPipe().getUse();
		geomPipe.setSmhGradeA(findGrideA(geomPipe.getSmhX(), geomPipe.getSmhY(), use));
		geomPipe.setFmhGradeA(findGrideA(geomPipe.getFmhX(), geomPipe.getFmhY(), use));
		geomPipe.setSmhGradeB(findGrideB(geomPipe.getSmhX(), geomPipe.getSmhY(), use));
		geomPipe.setFmhGradeB(findGrideB(geomPipe.getFmhX(), geomPipe.getFmhY(), use));
		this.updateGeomPipe(geomPipe);
		/****************************************************************/
		// 获取输入井号平均坐标值
		String smhGradeA = geomPipe.getSmhGradeA();
		String fmhGradeA = geomPipe.getFmhGradeA();
		if (smhGradeA == null || fmhGradeA == null)
			return;
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
		}
	}

	/** 计算GradeA */
	private String findGrideA(double x, double y, String use) {
		if (x == 0.0 || y == 0.0)
			return null;
		String x1 = ((int) x / 1000) % 100 + "";
		String y1 = ((int) y / 1000) % 100 + "";
		String x2 = (int) x % 1000 + "";
		String y2 = (int) y % 1000 + "";
		return x1 + y1 + x2 + y2 + use;
	}

	/** 计算GradeB */
	private String findGrideB(double x, double y, String use) {
		if (x == 0.0 || y == 0.0)
			return null;
		String x1 = ((int) x / 1000) % 100 + "";
		String y1 = ((int) y / 1000) % 100 + "";
		String x2 = (int) x % 1000 + "";
		String y2 = (int) y % 1000 + "";
		String x3 = (int) (x * 100) % 100 + "";
		String y3 = (int) (y * 100) % 100 + "";
		return x1 + y1 + x2 + y2 + x3 + y3 + use;
	}

}
