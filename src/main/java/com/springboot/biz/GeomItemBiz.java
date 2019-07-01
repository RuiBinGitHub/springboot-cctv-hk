package com.springboot.biz;

import java.util.List;
import java.util.Map;

import com.springboot.entity.GeomItem;
import com.springboot.entity.GeomPipe;

public interface GeomItemBiz {

	public void insertGeomItem(GeomItem geomItem);

	public void updateGeomItem(GeomItem geomItem);

	public void deleteGeomItem(GeomItem geomItem);

	public GeomItem findInfoGeomItem(Map<String, Object> map);

	public List<GeomItem> findListGeomItem(Map<String, Object> map);
	
	public List<GeomItem> findListGeomItem(GeomPipe geomPipe);
}
