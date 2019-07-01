package com.springboot.biz.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.springboot.biz.GeomItemBiz;
import com.springboot.dao.GeomItemDao;
import com.springboot.entity.GeomItem;
import com.springboot.entity.GeomPipe;
import com.springboot.util.AppUtils;

@Service(value = "geomItemBiz")
public class GeomItemBizImpl implements GeomItemBiz {

	@Resource
	private GeomItemDao geomItemDao;

	private Map<String, Object> map = null;

	public void insertGeomItem(GeomItem geomItem) {
		geomItemDao.insertGeomItem(geomItem);
	}

	public void updateGeomItem(GeomItem geomItem) {
		geomItemDao.updateGeomItem(geomItem);
	}

	public void deleteGeomItem(GeomItem geomItem) {
		geomItemDao.deleteGeomItem(geomItem);
	}

	public GeomItem findInfoGeomItem(Map<String, Object> map) {
		return geomItemDao.findInfoGeomItem(map);
	}

	public List<GeomItem> findListGeomItem(Map<String, Object> map) {
		return geomItemDao.findListGeomItem(map);
	}

	public List<GeomItem> findListGeomItem(GeomPipe geomPipe) {
		map = AppUtils.getMap("geomPipe", geomPipe);
		return geomItemDao.findListGeomItem(map);
	}
}
