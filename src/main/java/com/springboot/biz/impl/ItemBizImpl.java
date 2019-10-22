package com.springboot.biz.impl;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.springboot.biz.ItemBiz;
import com.springboot.dao.ItemDao;
import com.springboot.entity.Item;
import com.springboot.entity.Person;
import com.springboot.entity.Pipe;
import com.springboot.entity.Project;
import com.springboot.util.AppUtils;

@Service(value = "itemBiz")
public class ItemBizImpl implements ItemBiz {

	@Value("${mypath}")
	private String paht;

	@Resource
	private ItemDao itemDao;

	private Map<String, Object> map = null;

	/** 插入数据 */
	public void insertItem(Item item) {
		itemDao.insertItem(item);
	}

	/** 更新数据 */
	public void updateItem(Item item) {
		itemDao.updateItem(item);
	}

	/** 删除数据 */
	public void deleteItem(Item item) {
		itemDao.deleteItem(item);
	}

	/** 移除数据 */
	public void removeItem(Item item) {
		String name = item.getPicture();
		if (!StringUtils.isEmpty(name)) {
			File file = FileUtils.getFile(paht + name + ".png");
			FileUtils.deleteQuietly(file);
		}
		this.deleteItem(item);
	}

	public Item findInfoItem(int id, Person person) {
		map = AppUtils.getMap("id", id, "person", person);
		return itemDao.findInfoItem(map);
	}

	public Item findInfoItem(Map<String, Object> map) {
		return itemDao.findInfoItem(map);
	}

	public List<Item> findListItem(Pipe pipe) {
		map = AppUtils.getMap("pipe", pipe);
		return itemDao.findListItem(map);
	}

	public List<Item> findListItem(Project project) {
		map = AppUtils.getMap("project", project);
		return itemDao.findListItem(map);
	}

	public List<Item> findListItem(Map<String, Object> map) {
		return itemDao.findListItem(map);
	}

	public void sortItemImg(Project project) {
		DecimalFormat format = new DecimalFormat("#000");
		map = AppUtils.getMap("project", project, "picture", "");
		List<Item> items = this.findListItem(map);
		for (int i = 0; items != null && i < items.size(); i++) {
			items.get(i).setPhoto(format.format(i + 1));
			System.out.println(items.get(i).getId() + items.get(i).getPhoto());
			this.updateItem(items.get(i));
		}
	}

}
