package com.springboot.biz;

import java.util.List;
import java.util.Map;

import com.springboot.entity.Company;
import com.springboot.entity.Site;

public interface SiteBiz {

	public void insertSite(Site site);

	public void deleteSite(Site site);

	public Site findInfoSite(Map<String, Object> map);

	public Site findInfoSite(String value, Company company);

	public List<Site> findListSite(Map<String, Object> map);

	public List<Site> findListSite(Company company);

}
