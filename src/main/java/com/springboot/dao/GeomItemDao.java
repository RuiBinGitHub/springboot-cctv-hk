package com.springboot.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.springboot.entity.GeomItem;

@Mapper
public interface GeomItemDao {

	public void insertGeomItem(GeomItem geomItem);

	public void updateGeomItem(GeomItem geomItem);

	public void deleteGeomItem(GeomItem geomItem);

	public GeomItem findInfoGeomItem(Map<String, Object> map);

	public List<GeomItem> findListGeomItem(Map<String, Object> map);
}
