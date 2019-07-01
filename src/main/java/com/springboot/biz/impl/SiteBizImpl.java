package com.springboot.biz.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.springboot.biz.SiteBiz;
import com.springboot.dao.SiteDao;
import com.springboot.entity.Company;
import com.springboot.entity.Site;
import com.springboot.util.AppUtils;

@Service(value = "siteBiz")
public class SiteBizImpl implements SiteBiz {

	@Resource
	private SiteDao siteDao;

	private Map<String, Object> map = null;

	public void insertSite(Site site) {
		siteDao.insertSite(site);
	}

	public void deleteSite(Site site) {
		siteDao.deleteSite(site);
	}

	public Site findInfoSite(String value, Company company) {
		map = AppUtils.getMap("value", value, "company", company);
		return siteDao.findInfoSite(map);
	}

	public Site findInfoSite(Map<String, Object> map) {
		return siteDao.findInfoSite(map);
	}

	public List<Site> findListSite(Company company) {
		map = AppUtils.getMap("company", company);
		return siteDao.findListSite(map);
	}

	public List<Site> findListSite(Map<String, Object> map) {
		return siteDao.findListSite(map);
	}

}
