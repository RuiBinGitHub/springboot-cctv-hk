package com.springboot.biz.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.springboot.biz.MarkItemBiz;
import com.springboot.dao.MarkItemDao;
import com.springboot.entity.MarkItem;

@Service(value = "markItemBiz")
public class MarkItemBizImpl implements MarkItemBiz {

	@Resource
	private MarkItemDao markItemDao;

	public void insertMarkItem(MarkItem markItem) {
		markItemDao.insertMarkItem(markItem);
	}

	public void updateMarkItem(MarkItem markItem) {
		markItemDao.updateMarkItem(markItem);
	}

	public void deleteMarkItem(MarkItem markItem) {
		markItemDao.deleteMarkItem(markItem);
	}

	public MarkItem findInfoMarkItem(Map<String, Object> map) {
		return markItemDao.findInfoMarkItem(map);
	}

	public List<MarkItem> findListMarkItem(Map<String, Object> map) {
		return markItemDao.findListMarkItem(map);
	}
}
