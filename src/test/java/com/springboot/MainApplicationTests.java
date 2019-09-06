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
import com.springboot.biz.ProjectBiz;
import com.springboot.entity.Company;
import com.springboot.entity.Person;
import com.springboot.entity.Project;
import com.springboot.util.AppUtils;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MainApplicationTests {

	@Resource
	private ProjectBiz projectBiz;
	@Resource
	private CompanyBiz companyBiz;
	@Resource
	private PersonBiz personBiz;
	
	@Test
	public void test1(){
		Company company = new Company();
		company.setId(1);
		Person person = new Person();
		person.setId(9);
		Map<String, Object> map = AppUtils.getMap("company", company, "person", person);
		List<Project> projectss = projectBiz.markListProject(map);
		for (Project project : projectss) {
			System.out.println(project.getName() + "    " + project.getCount());
		}
	}
}

