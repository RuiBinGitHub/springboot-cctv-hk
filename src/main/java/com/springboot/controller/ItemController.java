package com.springboot.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.biz.ItemBiz;
import com.springboot.biz.ProjectBiz;
import com.springboot.entity.Item;
import com.springboot.entity.Person;
import com.springboot.entity.Project;
import com.springboot.util.AppUtils;

@RestController
@RequestMapping(value = "/item")
public class ItemController {
	@Resource
	private ProjectBiz projectBiz;
	@Resource
	private ItemBiz itemBiz;

	@Value(value = "${mypath}")
	private String path;

	private Map<String, Object> map = null;

	/** 删除数据 */
	@RequestMapping(value = "/delete")
	public boolean deleteItem(@RequestParam(defaultValue = "0") int id) {
		Person user = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("id", id, "person", user);
		Item item = itemBiz.findInfoItem(map);
		if (StringUtils.isEmpty(item))
			return false;
		itemBiz.deleteItem(item);
		if (!StringUtils.isEmpty(item.getPhoto())) {
			Project project = item.getPipe().getProject();
			itemBiz.sortItemImg(project);
		}
		return true;
	}

	/** 导入图片 */
	@Transactional
	@RequestMapping(value = "/inputimages", method = RequestMethod.POST)
	public Map<String, Object> inputImages(int id, MultipartFile[] files) throws IOException {
		Person user = (Person) AppUtils.findMap("user");
		Project project = projectBiz.findInfoProject(id, user);
		if (StringUtils.isEmpty(project))
			return AppUtils.getMap("result", false);
		map = AppUtils.getMap("project", project, "photo", "*插入图片");
		List<Item> items = itemBiz.findListItem(map);
		if (items == null || files == null || items.size() != files.length)
			return AppUtils.getMap("result", false, "count", items.size());
		Arrays.sort(files, (file1, file2) -> {
			String name1 = file1.getOriginalFilename();
			String name2 = file2.getOriginalFilename();
			if (name1.length() < name2.length())
				return -1;
			else if (name1.length() > name2.length())
				return 1;
			return name1.compareTo(name1);
		});

		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			String name = AppUtils.UUIDCode();
			File dest = new File(path + name + ".png");
			files[i].transferTo(dest);
			item.setPicture(name);
			itemBiz.updateItem(item);
		}
		itemBiz.sortItemImg(project);
		return AppUtils.getMap("result", true);
	}

	/** 移除图片 */
	@Transactional
	@RequestMapping(value = "/removeimage")
	public boolean removeImage(@RequestParam(defaultValue = "0") int id) {
		Person user = (Person) AppUtils.findMap("user");
		Project project = projectBiz.findInfoProject(id, user);
		if (StringUtils.isEmpty(project))
			return false;
		map = AppUtils.getMap("project", project, "picture", "");
		List<Item> items = itemBiz.findListItem(map);
		for (int i = 0; items != null && i < items.size(); i++) {
			String name = items.get(i).getPicture();
			items.get(i).setPhoto("*插入图片");
			items.get(i).setPicture("");
			itemBiz.updateItem(items.get(i));
			File file = new File(path + name + ".png");
			file.delete();
		}
		return true;
	}

}
