package com.springboot.biz.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.springboot.biz.CameBiz;
import com.springboot.dao.CameDao;
import com.springboot.entity.Came;
import com.springboot.util.AppUtils;

@Service(value = "cameBiz")
public class CameBizImpl implements CameBiz {

	@Resource
	private CameDao cameDao;

	private Map<String, Object> map = null;

	/** 获取came对象 */
	public Came findInfoCame(Map<String, Object> map) {
		return cameDao.findInfoCame(map);
	}

	/** 获取came列表 */
	public List<Came> findListCame(Map<String, Object> map) {
		return cameDao.findListCame(map);
	}

	/** 根据came的value获取name */
	public String getCameName(String value, String option) {
		try {
			map = AppUtils.getMap("value", value, "option", option);
			Came came = cameDao.findInfoCame(map);
			return came.getName();
		} catch (Exception e) {
			return "";
		}
	}

	/** 根据came的name获取value */
	public String getCameValue(String name, String option) {
		try {
			map = AppUtils.getMap("name", name, "option", option);
			Came came = cameDao.findInfoCame(map);
			return came.getValue();
		} catch (Exception e) {
			return "";
		}
	}

}
