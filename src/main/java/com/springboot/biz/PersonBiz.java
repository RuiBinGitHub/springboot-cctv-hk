package com.springboot.biz;

import java.util.List;
import java.util.Map;

import com.springboot.entity.Person;

public interface PersonBiz {

	public void insertPerson(Person person);

	public void updatePerson(Person person);

	public void deletePerson(Person person);

	public void removePerson(Person person);
	
	public Person likeInfoPerson(Map<String, Object> map);

	public Person findInfoPerson(Map<String, Object> map);

	public List<Person> findListPerson(Map<String, Object> map);

	public int getPageCount(Map<String, Object> map, int size);

}
