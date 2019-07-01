package com.springboot.biz;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.springboot.entity.GeomProject;
import com.springboot.entity.Person;

public interface GeomProjectBiz {

	public void insertGeomProject(GeomProject geomProject);

	public void updateGeomProject(GeomProject geomProject);

	public void deleteGeomProject(GeomProject geomProject);

	public GeomProject findInfoGeomProject(Map<String, Object> map);

	public List<GeomProject> findListGeomProject(Map<String, Object> map);

	public GeomProject findInfoGeomProject(int id, Person person);
	
	public void appendGeomProject(GeomProject geomProject);
	
	public void importValue(GeomProject geomProject, MultipartFile file);

	public void computeCenter(GeomProject geomProject);
}
