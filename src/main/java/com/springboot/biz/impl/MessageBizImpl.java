package com.springboot.biz.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.springboot.biz.MessageBiz;
import com.springboot.biz.PersonBiz;
import com.springboot.dao.MessageDao;
import com.springboot.entity.Message;
import com.springboot.entity.Person;
import com.springboot.util.AppUtils;

@Service(value = "messageBiz")
public class MessageBizImpl implements MessageBiz {

	@Resource
	private MessageDao messageDao;
	@Resource
	private PersonBiz personBiz;

	private Map<String, Object> map = null;

	public void insertMessage(Message message) {
		messageDao.insertMessage(message);
	}

	public void updateMessage(Message message) {
		messageDao.updateMessage(message);
	}

	public void deleteMessage(Message message) {
		messageDao.deleteMessage(message);
	}

	public Message findInfoMessage(int id, Person accept) {
		map = AppUtils.getMap("id", id, "accept", accept);
		Message message = messageDao.findInfoMessage(map);
		return message;
	}

	public Message findInfoMessage(Map<String, Object> map) {
		return messageDao.findInfoMessage(map);
	}

	public List<Message> findListMessage(Map<String, Object> map) {
		return messageDao.findListMessage(map);
	}

	public int getCount(Map<String, Object> map) {
		return messageDao.getCount(map);
	}

	public int getCount(Person accept, String type) {
		map = AppUtils.getMap("accept", accept, "type", type);
		return messageDao.getCount(map);
	}

	public int getPageCount(Map<String, Object> map, int size) {
		int count = messageDao.getCount(map);
		return (int) Math.ceil((double) count / size);
	}

	public void sendMessage(String title, String text, Person accept) {
		map = AppUtils.getMap("id", 0);
		Person sender = personBiz.findInfoPerson(map);
		String data = AppUtils.getDate(null);
		Message message = new Message();
		message.setTitle(title);
		message.setType("未读");
		message.setText(text);
		message.setDate(data);
		message.setSender(sender);
		message.setAccept(accept);
		insertMessage(message);
	}

}
