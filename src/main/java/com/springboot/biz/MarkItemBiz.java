package com.springboot.biz;

import java.util.List;
import java.util.Map;

import com.springboot.entity.MarkItem;

public interface MarkItemBiz {

	public void insertMarkItem(MarkItem markItem);

	public void updateMarkItem(MarkItem markItem);

	public void deleteMarkItem(MarkItem markItem);

	public MarkItem findInfoMarkItem(Map<String, Object> map);

	public List<MarkItem> findListMarkItem(Map<String, Object> map);
}
