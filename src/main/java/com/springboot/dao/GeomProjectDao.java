package com.springboot.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.springboot.entity.GeomProject;

@Mapper
public interface GeomProjectDao {

	public void insertGeomProject(GeomProject geomProject);

	public void updateGeomProject(GeomProject geomProject);

	public void deleteGeomProject(GeomProject geomProject);

	public GeomProject findGeomProjectByID(int id);

	public GeomProject findInfoGeomProject(Map<String, Object> map);

	public List<GeomProject> findListGeomProject(Map<String, Object> map);

}
