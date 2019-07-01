package com.springboot.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.springboot.entity.Site;

@Mapper
public interface SiteDao {

	public void insertSite(Site site);

	public void deleteSite(Site site);

	public Site findInfoSite(Map<String, Object> map);

	public List<Site> findListSite(Map<String, Object> map);
}
