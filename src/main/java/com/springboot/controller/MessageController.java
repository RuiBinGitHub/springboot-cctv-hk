package com.springboot.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.biz.MessageBiz;
import com.springboot.entity.Message;
import com.springboot.entity.Person;
import com.springboot.util.AppUtils;

@RestController
@RequestMapping(value = "/message")
public class MessageController {

	@Resource
	private MessageBiz messageBiz;

	private Map<String, Object> map = null;

	/** 获取消息列表 */
	@RequestMapping(value = "/showlist")
	public ModelAndView showlist(@RequestParam(defaultValue = "1") int page) {
		ModelAndView view = new ModelAndView("message/showlist");
		Person person = (Person) AppUtils.findMap("user");
		map = AppUtils.getMap("accept", person);
		int cont = messageBiz.getPageCount(map, 15);
		page = page > cont ? cont : page;
		map.put("page", page);
		List<Message> messages = messageBiz.findListMessage(map);
		view.addObject("messages", messages);
		view.addObject("count1", getCount("未读"));
		view.addObject("count2", getCount("已读"));
		view.addObject("page", page);
		view.addObject("cont", cont);
		return view;
	}

	/** 获取消息数量 */
	@RequestMapping("/getcount")
	public int getCount(String type) {
		Person person = (Person) AppUtils.findMap("user");
		int count = messageBiz.getCount(person, type);
		return count;
	}

	/** 获取消息信息 */
	@RequestMapping("/findinfo")
	public Message findInfo(@RequestParam(defaultValue = "0") int id) {
		Person person = (Person) AppUtils.findMap("user");
		Message message = messageBiz.findInfoMessage(id, person);
		if (StringUtils.isEmpty(message))
			return null;
		message.setType("已读");
		messageBiz.updateMessage(message);
		message.setSenderName(message.getSender().getNickname());
		message.setAcceptName(message.getAccept().getNickname());
		message.setSender(null);
		message.setAccept(null);
		return message;
	}

	/** 删除信息 */
	@RequestMapping("/delete")
	public boolean delete(@RequestParam(defaultValue = "0") int id) {
		Person user = (Person) AppUtils.findMap("user");
		Message message = messageBiz.findInfoMessage(id, user);
		if (!StringUtils.isEmpty(message))
			messageBiz.deleteMessage(message);
		return true;
	}
}
