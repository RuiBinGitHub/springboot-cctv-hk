package com.springboot.biz;

import java.util.List;
import java.util.Map;

import com.springboot.entity.Message;
import com.springboot.entity.Person;

public interface MessageBiz {

	public void insertMessage(Message message);

	public void updateMessage(Message message);

	public void deleteMessage(Message message);

	public Message findInfoMessage(int id, Person person);

	public Message findInfoMessage(Map<String, Object> map);

	public List<Message> findListMessage(Map<String, Object> map);

	public int getCount(Person accept, String type);

	public int getCount(Map<String, Object> map);

	public int getPageCount(Map<String, Object> map, int size);

	public void sendMessage(String title, String text, Person accept);

}
