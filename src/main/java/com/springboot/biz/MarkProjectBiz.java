package com.springboot.biz;

import java.util.List;
import java.util.Map;

import com.springboot.entity.MarkProject;
import com.springboot.entity.Person;

public interface MarkProjectBiz {

	public void insertMarkProject(MarkProject markProject);

	public void deleteMarkProject(MarkProject markProject);

	public MarkProject findInfoMarkProject(Map<String, Object> map);

	public MarkProject findInfoMarkProject(int id, Person person);
	
	public List<MarkProject> findListMarkProject(Map<String, Object> map);

	public List<MarkProject> markListMarkProject(Map<String, Object> map);
	
	public int getCount(Map<String, Object> map, int size);

	public int appendMarkProject(MarkProject markProject);
	
}
