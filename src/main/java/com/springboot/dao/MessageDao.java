package com.springboot.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import com.springboot.entity.Message;

@Mapper
public interface MessageDao {

	public void insertMessage(Message message);

	public void updateMessage(Message message);

	public void deleteMessage(Message message);

	public Message findInfoMessage(Map<String, Object> map);

	public List<Message> findListMessage(Map<String, Object> map);

	public int getCount(Map<String, Object> map);
}
