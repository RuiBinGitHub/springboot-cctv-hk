package com.springboot.biz.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.springboot.biz.CompanyBiz;
import com.springboot.biz.PersonBiz;
import com.springboot.dao.CompanyDao;
import com.springboot.entity.Company;
import com.springboot.entity.Person;
import com.springboot.util.AppUtils;

@Service(value = "companyBiz")
public class CompanyBizImpl implements CompanyBiz {

	@Resource
	private CompanyDao companyDao;
	@Resource
	private PersonBiz personBiz;
	
	private Map<String, Object> map = null;

	/** 插入数据 */
	public void insertCompany(Company company) {
		companyDao.insertCompany(company);
	}

	/** 更新数据 */
	public void updateCompany(Company company) {
		companyDao.updateCompany(company);
	}

	/** 删除数据 */
	public void deleteCompany(Company company) {
		companyDao.deleteCompany(company);
	}

	public Company findInfoCompany(int id) {
		map = AppUtils.getMap("id", id);
		return companyDao.findInfoCompany(map);
	}

	/** 获取company对象 */
	public Company findInfoCompany(Map<String, Object> map) {
		return companyDao.findInfoCompany(map);
	}

	/** 获取company列表 */
	public List<Company> findListCompany(Map<String, Object> map) {
		return companyDao.findListCompany(map);
	}

	public int getCountPage(Map<String, Object> map, int size) {
		if (!StringUtils.isEmpty(map.get("name")))
			map.put("name", "%" + map.get("name") + "%");
		int count = companyDao.getCount(map);
		return (int) Math.ceil((double) count / size);
	}

	public void appendCompany(Company company, Person person) {
		person.setPhone("--");
		person.setRole("Role2");
		person.setCompany(company);
		person.setDate(AppUtils.getDate(null));
		company.setCode(AppUtils.findCode());
		company.setDate(AppUtils.getDate(null));
		companyDao.insertCompany(company);
		personBiz.insertPerson(person);
	}

}
