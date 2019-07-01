package com.springboot.biz;

import java.util.List;
import java.util.Map;

import com.springboot.entity.Came;

public interface CameBiz {
	
	public Came findInfoCame(Map<String, Object> map);

	public List<Came> findListCame(Map<String, Object> map);
	
	public String getCameName(String value, String option);

	public String getCameValue(String name, String option);
}
