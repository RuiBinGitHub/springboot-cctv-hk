package com.springboot.biz;

import java.util.List;
import java.util.Map;

import com.springboot.entity.Person;
import com.springboot.entity.Pipe;
import com.springboot.entity.Project;

public interface PipeBiz {

	public void insertPipe(Pipe pipe);

	public void updatePipe(Pipe pipe);

	public void deletePipe(Pipe pipe);

	public Pipe findInfoPipe(int id, Person person);

	public Pipe findInfoPipe(Map<String, Object> map);

	public List<Pipe> findListPipe(Project project);

	public List<Pipe> findListPipe(Map<String, Object> map);

	public int getCount(Map<String, Object> map);

	public void appendPipe(Pipe pipe);
	

}
