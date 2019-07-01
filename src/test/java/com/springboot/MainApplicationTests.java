package com.springboot;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.springboot.biz.PipeBiz;
import com.springboot.entity.Pipe;
import com.springboot.util.Computes;
import com.springboot.util.CreateDOC;
import com.springboot.util.CreatePDF;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MainApplicationTests {

	@Resource
	public CreatePDF createPDF;
	@Resource
	public CreateDOC createDOC;
	@Resource
	public Computes compute;
	@Resource
	public PipeBiz pipeBiz;
	
	@Test
	public void contextLoads() {
		createPDF.initPDF(11802, "D:\\");
		System.out.println("---");
	}
	
	@Test
	public void test1(){
		Pipe pipe = pipeBiz.findInfoPipe(21666, null);
		pipe = compute.computePipe(pipe, "HKCCEC 2009");
		for (double value : pipe.getGrade()) {
			System.out.print(value + "    ");
		}
		System.out.println("--");
		for (double value : pipe.getScore()) {
			System.out.print(value + "    ");
		}
	}
}

