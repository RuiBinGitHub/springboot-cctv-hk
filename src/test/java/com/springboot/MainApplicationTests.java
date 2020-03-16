package com.springboot;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.springboot.biz.CompanyBiz;
import com.springboot.biz.ItemBiz;
import com.springboot.biz.PersonBiz;
import com.springboot.biz.ProjectBiz;
import com.springboot.entity.Item;
import com.springboot.entity.Project;
import com.springboot.util.AppUtils;
import com.springboot.util.CreatePDF;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MainApplicationTests {

	@Resource
	private ProjectBiz projectBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private CompanyBiz companyBiz;
	@Resource
	private PersonBiz personBiz;
	
	private Map<String, Object> map = null;
	
	@Test
	public void test1(){
		Project project = new Project();
		project.setId(28946);
		map = AppUtils.getMap("project", project, "picture", "");
		List<Item> items = itemBiz.findListItem(map);
		for (Item item : items) {
			System.out.println(item.getId());
		}
		System.out.println("--");
	}
	
	@Value("${myfile}")
	private String file;
	
	@Value("${mypath}")
	private String path;
	
	@Test
	public void test2(){
		System.out.println(file);
		System.out.println(path);
	}
	
	@Resource
	private CreatePDF createPDF;
	
	@Test
	public void test3(){
		createPDF.initPDF(26537, "D:/");
		System.out.println("--");
	}
}

