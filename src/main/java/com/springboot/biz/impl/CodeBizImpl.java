package com.springboot.biz.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.springboot.biz.CodeBiz;
import com.springboot.dao.CodeDao;
import com.springboot.entity.Code;

@Service(value = "codeBiz")
public class CodeBizImpl implements CodeBiz {

	@Resource
	private CodeDao codeDao;

	/** 获取code对象 */
	public Code findInfoCode(Map<String, Object> map) {
		return codeDao.findInfoCode(map);
	}

	/** 获取code列表 */
	public List<Code> findListCode(Map<String, Object> map) {
		return codeDao.findListCode(map);
	}

}
