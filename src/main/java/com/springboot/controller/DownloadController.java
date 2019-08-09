package com.springboot.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.biz.CompanyBiz;
import com.springboot.biz.PersonBiz;
import com.springboot.biz.ProjectBiz;
import com.springboot.entity.Company;
import com.springboot.entity.Person;
import com.springboot.entity.Project;
import com.springboot.util.AppUtils;
import com.springboot.util.CreateDOC;
import com.springboot.util.CreateMDB;
import com.springboot.util.CreatePDF;

@RestController
@RequestMapping(value = "/download")
public class DownloadController {

	@Value(value = "${myfile}")
	private String path;

	@Resource
	private CompanyBiz companyBiz;
	@Resource
	private ProjectBiz projectBiz;
	@Resource
	private PersonBiz personBiz;
	@Resource
	private CreateMDB createMDB;
	@Resource
	private CreatePDF createPDF;
	@Resource
	private CreateDOC createDOC;

	private Project project = null;
	private Map<String, Object> map = null;

	@RequestMapping(value = "/file")
	public void download(int id, HttpServletResponse response) throws Exception {
		if ((project = projectBiz.findInfoProject(id, null)) == null)
			return;
		String srcPath = path + "report\\";
		String zipPath = path + "compre\\";
		String name = AppUtils.UUIDCode();
		File data = new File(srcPath + name + "\\data\\");
		File vedio = new File(srcPath + name + "\\vedio\\");
		File report = new File(srcPath + name + "\\report\\");
		data.mkdirs();
		vedio.mkdirs();
		report.mkdirs();

		createMDB.initMDB(id, data.getPath() + "\\");
		createPDF.initPDF(id, srcPath + name + "\\");
		createDOC.initDOC(id, srcPath + name + "\\");

		String fileName = project.getDate() + "_" + project.getName();
		File zipFile = this.toZip(srcPath + name, zipPath, fileName);
		response.addHeader("Content-Disposition", "attachment;fileName=" + fileName + ".zip");
		response.setContentType("application/force-download");

		int len = -1;
		byte[] buffer = new byte[1024];
		InputStream fstream = new FileInputStream(zipFile.getPath());
		InputStream bstream = new BufferedInputStream(fstream);
		OutputStream outputStream = response.getOutputStream();
		while ((len = bstream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, len);
			outputStream.flush();
		}
		bstream.close();
		zipFile.delete();
	}

	@RequestMapping(value = "/user")
	public void findUserList(int id, HttpServletResponse response) throws IOException {
		String[] header = { "编号", "用户昵称", "登录账号", "登录密码", "用户职务", "注册日期" };
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

	/** 压缩文件 */
	public File toZip(String srcPath, String zipPath, String name) throws Exception {
		File srcFile = new File(srcPath);
		File zipFile = new File(zipPath + name);
		FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
		ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
		compress(srcFile, zipOutputStream, name);
		zipOutputStream.close();
		fileOutputStream.close();
		return zipFile;
	}

	/** 压缩文件 */
	public boolean compress(File srcFile, ZipOutputStream zip, String name) throws IOException {
		if (srcFile.isFile()) {
			int len = 0;
			byte[] data = new byte[1024];
			ZipEntry entry = new ZipEntry(name);
			zip.putNextEntry(entry);
			FileInputStream input = new FileInputStream(srcFile);
			while ((len = input.read(data)) != -1)
				zip.write(data, 0, len);
			zip.closeEntry();
			input.close();
			return true;
		}
		File[] listFiles = srcFile.listFiles();
		for (int i = 0; listFiles != null && i < listFiles.length; i++) {
			File file = listFiles[i];
			if (file.isFile())
				compress(file, zip, name + "/" + file.getName());
			else {
				ZipEntry entry = new ZipEntry(name + "/" + file.getName() + "/");
				zip.putNextEntry(entry);
				zip.closeEntry();
				File[] tempList = file.listFiles();
				for (File temp : tempList)
					compress(temp, zip, name + "/" + file.getName() + "/" + temp.getName());
			}
		}
		return true;
	}
}
