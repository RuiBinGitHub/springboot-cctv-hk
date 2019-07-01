package com.springboot.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.springboot.entity.MarkProject;

@Mapper
public interface MarkProjectDao {

	public void insertMarkProject(MarkProject markProject);
	
	public void deleteMarkProject(MarkProject markProject);
	
	public MarkProject findMarkProjectByID(int id);
	
	public MarkProject findInfoMarkProject(Map<String, Object> map);
	
	public List<MarkProject> findListMarkProject(Map<String, Object> map);
	
	public int getCount(Map<String, Object> map);
	
	public Map<String, Double> getAverage(MarkProject markProject);

}
