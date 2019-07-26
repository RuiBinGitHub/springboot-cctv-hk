package com.springboot.biz.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.springboot.biz.PersonBiz;
import com.springboot.biz.ProjectBiz;
import com.springboot.dao.PersonDao;
import com.springboot.entity.Person;
import com.springboot.entity.Project;
import com.springboot.util.AppUtils;

@Transactional
@Service(value = "personBiz")
public class PersonBizImpl implements PersonBiz {

	@Resource
	private PersonDao personDao;
	@Resource
	private ProjectBiz projectBiz;

	public void insertPerson(Person person) {
		personDao.insertPerson(person);
	}

	public void updatePerson(Person person) {
		personDao.updatePerson(person);
	}

	public void deletePerson(Person person) {
		personDao.deletePerson(person);
	}

	public void removePerson(Person person) {
		Person user = (Person) AppUtils.findMap("user");
		Map<String, Object> map = AppUtils.getMap("person", person);
		List<Project> projects1 = projectBiz.findListProject(map);
		for (Project project : projects1)
			projectBiz.deleteProject(project);
		map = AppUtils.getMap("person", person, "company", user.getCompany());
		List<Project> projects2 = projectBiz.findListProject(map);
		for (Project project : projects2) {
			project.setPerson(user);
			projectBiz.updateProject(project);
		}
		personDao.deletePerson(person);
	}

	public Person findInfoPerson(Map<String, Object> map) {
		return personDao.findInfoPerson(map);
	}

	public List<Person> likeInfoPerson(Map<String, Object> map) {
		if (!StringUtils.isEmpty(map.get("username")))
			map.put("username", "%" + map.get("username") + "%");
		return personDao.likeInfoPerson(map);
	}
	
	public List<Person> findListPerson(Map<String, Object> map) {
		if (!StringUtils.isEmpty(map.get("nickname")))
			map.put("nickname", "%" + map.get("nickname") + "%");
		return personDao.findListPerson(map);
	}

	public List<Person> findListPerson(String name, int page) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nickname", "%" + name + "%");
		map.put("page", page);
		return findListPerson(map);
	}

	public int getPageCount(Map<String, Object> map, int size) {
		if (!StringUtils.isEmpty(map.get("nickname")))
			map.put("nickname", "%" + map.get("nickname") + "%");
		int count = personDao.getCount(map);
		return (int) Math.ceil((double) count / size);
	}

}
