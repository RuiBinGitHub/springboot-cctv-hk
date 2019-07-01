package com.springboot.biz;

import java.util.List;
import java.util.Map;

import com.springboot.entity.MarkPipe;
import com.springboot.entity.Person;

public interface MarkPipeBiz {

	public void insertMarkPipe(MarkPipe markPipe);

	public void updateMarkPipe(MarkPipe markPipe);

	public void deleteMarkPipe(MarkPipe markPipe);

	public MarkPipe findInfoMarkPipe(Map<String, Object> map);

	public MarkPipe findInfoMarkPipe(int id, Person person);
	
	public List<MarkPipe> findListMarkPipe(Map<String, Object> map);

	public void appendMarkPipe(MarkPipe markPipe);

}
