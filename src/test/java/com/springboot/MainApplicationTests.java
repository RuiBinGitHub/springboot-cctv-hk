package com.springboot;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

	
	private Map<String, Object> map = null;
	
	@Test
	public void test1(){
		Company company = new Company();
		company.setId(1);
		map = AppUtils.getMap("stauts", "0", "company", company);
		List<Person> persons = personBiz.findListPerson(map);
		System.out.println(persons.size());
	}
}

