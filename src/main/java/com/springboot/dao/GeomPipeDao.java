package com.springboot.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.springboot.entity.GeomPipe;

@Mapper
public interface GeomPipeDao {

	public void insertGeomPipe(GeomPipe geomPipe);

	public void updateGeomPipe(GeomPipe geomPipe);

	public void deleteGeomPipe(GeomPipe geomPipe);

	public GeomPipe findGeomPipeByID(int id);

	public GeomPipe findInfoGeomPipe(Map<String, Object> map);

	public List<GeomPipe> findListGeomPipe(Map<String, Object> map);

	public Map<String, Double> findSMHGradeA(String smhGradeA);

	public Map<String, Double> findFMHGradeA(String fmhGradeA);
}
