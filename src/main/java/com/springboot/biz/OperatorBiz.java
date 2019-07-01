package com.springboot.biz;

import java.util.List;
import java.util.Map;

import com.springboot.entity.Company;
import com.springboot.entity.Operator;

public interface OperatorBiz {

	public void insertOperator(Operator operator);

	public void updateOperator(Operator operator);

	public void deleteOperator(Operator operator);

	public Operator findInfoOperator(Map<String, Object> map);

	public List<Operator> findListOperator(Map<String, Object> map);

	public List<String> findFullName(Company company);

	public int getCountPage(Map<String, Object> map, int size);

}
