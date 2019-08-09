package com.springboot.biz;

import java.util.List;
import java.util.Map;

import com.springboot.entity.GeomPipe;
import com.springboot.entity.GeomProject;
import com.springboot.entity.Person;

public interface GeomPipeBiz {

	public void insertGeomPipe(GeomPipe geomPipe);

	public void updateGeomPipe(GeomPipe geomPipe);

	public void deleteGeomPipe(GeomPipe geomPipe);

	public GeomPipe findInfoGeomPipe(Map<String, Object> map);

	public List<GeomPipe> findListGeomPipe(Map<String, Object> map);

	public List<GeomPipe> findListGeomPipe(GeomProject geomProject);

	public GeomPipe findInfoGeomPipe(int id, Person person);

	public Map<String, Double> findSMHGradeA(String smhGradeA);

	public Map<String, Double> findFMHGradeA(String fmhGradeA);
	
	public void replacePipegeom(GeomPipe geomPipe);

}
