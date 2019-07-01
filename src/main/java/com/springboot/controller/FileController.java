package com.springboot.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.biz.ProjectBiz;
import com.springboot.entity.Project;
import com.springboot.util.AppUtils;
import com.springboot.util.CreateDOC;
import com.springboot.util.CreateMDB;
import com.springboot.util.CreatePDF;

@RestController
public class FileController {

	@Value(value = "${myfile}")
	private String path;
	@Resource
	private ProjectBiz projectBiz;
	@Resource
	private CreateMDB createMDB;
	@Resource
	private CreatePDF createPDF;
	@Resource
	private CreateDOC createDOC;

	private Project project = null;

	@RequestMapping(value = "/download")
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

		String fileName = project.getDate() + "_" + project.getName() + ".zip";
		File zipFile = this.toZip(srcPath + name, zipPath, fileName);
		response.setContentType("application/force-download");
		response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
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
