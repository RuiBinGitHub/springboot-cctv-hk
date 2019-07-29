package com.springboot.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.biz.CompanyBiz;
import com.springboot.biz.PersonBiz;
import com.springboot.entity.Company;
import com.springboot.entity.Person;
import com.springboot.util.AppUtils;

@RestController
@RequestMapping(value = "/export")
public class ExportController {

	@Resource
	private CompanyBiz companyBiz;
	@Resource
	private PersonBiz personBiz;

	private Map<String, Object> map = null;

	@RequestMapping("/userlist")
	public void findUserList(int id, HttpServletResponse response) throws IOException {

		String[] header = { "编号", "用户昵称", "登录账号", "登录密码", "用户职务", "注册日期" };
		// 声明一个工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("用户表");
		sheet.setDefaultColumnWidth(12);
		sheet.setDefaultRowHeight((short) 360);
		HSSFRow headrow = sheet.createRow(0);
		for (int i = 0; i < header.length; i++) {
			HSSFCell cell = headrow.createCell(i);
			cell.setCellValue(header[i]);
		}

		Company company = companyBiz.findInfoCompany(id);
		map = AppUtils.getMap("company", company);
		List<Person> persons = personBiz.findListPerson(map);
		for (int i = 0; persons != null && i < persons.size(); i++) {
			Person person = persons.get(i);
			HSSFRow row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(i + "");
			row.createCell(1).setCellValue(person.getNickname());
			row.createCell(2).setCellValue(person.getUsername());
			row.createCell(3).setCellValue(person.getPassword());
			if ("Role2".equals(person.getRole()))
				row.createCell(4).setCellValue("管理人员");
			else if ("Role3".equals(person.getRole()))
				row.createCell(4).setCellValue("评分人员");
			else if ("Role4".equals(person.getRole()))
				row.createCell(4).setCellValue("操作人员");
			row.createCell(5).setCellValue(person.getDate());

		}
		response.setHeader("Content-disposition", "attachment;filename=user.xls");
		response.setContentType("application/octet-stream");
		workbook.write(response.getOutputStream());
		response.flushBuffer(); // 刷新缓冲区
	}

}
