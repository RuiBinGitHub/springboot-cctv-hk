package com.springboot.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.springboot.entity.MarkItem;

@Mapper
public interface MarkItemDao {

	public void insertMarkItem(MarkItem markItem);

	public void updateMarkItem(MarkItem markItem);

	public void deleteMarkItem(MarkItem markItem);

	public MarkItem findInfoMarkItem(Map<String, Object> map);

	public List<MarkItem> findListMarkItem(Map<String, Object> map);
}
