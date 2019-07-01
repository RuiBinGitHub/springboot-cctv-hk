package com.springboot.biz.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.springboot.biz.OperatorBiz;
import com.springboot.dao.OperatorDao;
import com.springboot.entity.Company;
import com.springboot.entity.Operator;

@Service(value = "operatorBiz")
public class OperatorBizImpl implements OperatorBiz {

	@Resource
	private OperatorDao operatorDao;

	public void insertOperator(Operator operator) {
		operatorDao.insertOperator(operator);
	}

	public void updateOperator(Operator operator) {
		operatorDao.updateOperator(operator);
	}

	public void deleteOperator(Operator operator) {
		operatorDao.deleteOperator(operator);
	}

	public Operator findInfoOperator(Map<String, Object> map) {
		if (!StringUtils.isEmpty(map.get("fullname")))
			map.put("fullname", "%" + map.get("fullname") + "%");
		return operatorDao.findInfoOperator(map);
	}

	public List<Operator> findListOperator(Map<String, Object> map) {
		if (!StringUtils.isEmpty(map.get("fullname")))
			map.put("fullname", "%" + map.get("fullname") + "%");
		return operatorDao.findListOperator(map);
	}

	public List<String> findFullName(Company company) {
		return operatorDao.findFullName(company);
	}

	public int getCountPage(Map<
			String, Object> map, int size) {
		if (!StringUtils.isEmpty(map.get("fullname")))
			map.put("fullname", "%" + map.get("fullname") + "%");
		int count = operatorDao.getCount(map);
		return (int) Math.ceil((double) count / size);
	}

}
