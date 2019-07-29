package com.springboot;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import com.springboot.biz.CompanyBiz;
import com.springboot.biz.PersonBiz;
import com.springboot.entity.Company;
import com.springboot.entity.Person;
import com.springboot.util.AppUtils;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MainApplicationTests {

	@Resource
	private CompanyBiz companyBiz;
	@Resource
	private PersonBiz personBiz;

	
	
	@Test
	public void test1(){
		Company company = new Company();
		company.setId(64);
		Map<String, Object> map = AppUtils.getMap("company", company, "nickname", "");
		if (!StringUtils.isEmpty(map.get("name")))
			map.put("name", "%" + map.get("name") + "%");
		List<Person> persons = personBiz.findListPerson(map);
		System.out.println(persons.size());
	}
}

