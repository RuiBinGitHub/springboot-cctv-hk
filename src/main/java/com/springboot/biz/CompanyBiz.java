package com.springboot.biz;

import java.util.List;
import java.util.Map;

import com.springboot.entity.Company;

public interface CompanyBiz {

	public void insertCompany(Company company);

	public void updateCompany(Company company);

	public void deleteCompany(Company company);

	public Company findInfoCompany(int id);

	public Company findInfoCompany(Map<String, Object> map);

	public List<Company> findListCompany(Map<String, Object> map);

	public int getCountPage(Map<String, Object> map, int size);

	public void appendCompany(Company company);
	
	public void repeatCompany(Company company);

}
