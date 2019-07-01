package com.springboot.biz;

import java.util.List;
import java.util.Map;

import com.springboot.entity.Code;

public interface CodeBiz {

	public Code findInfoCode(Map<String, Object> map);

	public List<Code> findListCode(Map<String, Object> map);
	
}
