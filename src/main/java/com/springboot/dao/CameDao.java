package com.springboot.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.springboot.entity.Came;

@Mapper
public interface CameDao {
	
	public Came findInfoCame(Map<String, Object> map);

	public List<Came> findListCame(Map<String, Object> map);
}
