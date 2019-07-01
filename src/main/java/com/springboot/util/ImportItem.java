package com.springboot.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.biz.CameBiz;
import com.springboot.biz.ItemBiz;
import com.springboot.biz.PipeBiz;
import com.springboot.biz.ProjectBiz;
import com.springboot.entity.Item;
import com.springboot.entity.Person;
import com.springboot.entity.Pipe;
import com.springboot.entity.Project;

@Component(value = "importItem")
public class ImportItem {

	@Value(value = "${myfile}")
	private String path;
	@Value(value = "${mypath}")
	private String ImgPath;
	@Resource
	private ProjectBiz projectBiz;
	@Resource
	private PipeBiz pipeBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private CameBiz cameBiz;

	@Transactional
	public boolean importMode(MultipartFile file) {
		try {
			String name = AppUtils.UUIDCode();
			File temp = new File(path + "TempFile\\" + name + ".mdb");
			file.transferTo(temp);
			Connection conn = getConnection(temp.getPath());
			conn.setAutoCommit(true);

			Project project = new Project();
			List<Pipe> pipes = new ArrayList<Pipe>();
			List<Item> items = new ArrayList<Item>();

			Statement stat = conn.createStatement();
			ResultSet rs1 = stat.executeQuery("select * from project");
			if (rs1.next()) {
				Person user = (Person) AppUtils.findMap("user");
				project.setName(rs1.getString(3));
				project.setClient(rs1.getString(4));
				if (rs1.getString(5) != null && rs1.getString(5).indexOf("H") != -1)
					project.setStandard("HKCCEC 2009");
				if (rs1.getString(5) != null && rs1.getString(5).indexOf("M") != -1)
					project.setStandard("MSCC 2004");
				project.setSlope(rs1.getBoolean(6) ? "Y" : "N");
				project.setOperator("");
				project.setDate(AppUtils.getDate(null));
				project.setPerson(user);
			}
			rs1.close();
			stat = conn.createStatement();
			ResultSet rs2 = stat.executeQuery("select * from table1 order by id");
			while (rs2.next()) {
				Pipe pipe = new Pipe();
				pipe.setNo(getInt(rs2.getString(30)));
				pipe.setOperator(rs2.getString(6));
				pipe.setWorkorder(rs2.getString(7));
				pipe.setReference(rs2.getString(8));
				pipe.setPurposes(rs2.getString(5));
				pipe.setSlope(rs2.getString(43).substring(0, 1));
				pipe.setSloperef(rs2.getString(44));
				pipe.setYearlaid(getYear(rs2.getString(42)));
				pipe.setDate(getDate(rs2.getString(37)));
				pipe.setTime(rs2.getString(25));

				pipe.setDistrict1(rs2.getString(18));
				pipe.setDistrict2(rs2.getString(45));
				pipe.setDistrict3(rs2.getString(46));
				pipe.setRoadname(rs2.getString(9));
				pipe.setHousenum(rs2.getString(47));
				pipe.setBuilding(rs2.getString(10));
				pipe.setDivision(rs2.getString(19));
				pipe.setAreacode(rs2.getString(36));

				pipe.setSmanholeno(rs2.getString(11));
				pipe.setFmanholeno(rs2.getString(12));
				pipe.setUse(rs2.getString(28).substring(0, 1));
				pipe.setDirection(rs2.getString(32).substring(0, 1));
				pipe.setHsize(rs2.getString(39));
				pipe.setWsize(rs2.getString(13));
				pipe.setShape(cameBiz.getCameName(rs2.getString(14), "shape"));
				pipe.setMaterial(cameBiz.getCameName(rs2.getString(15), "material"));
				pipe.setLining(cameBiz.getCameName(rs2.getString(21), "lining"));
				pipe.setPipelength(getValue(rs2.getString(29)));
				pipe.setTotallength(getValue(rs2.getString(3)));

				pipe.setSdepth(rs2.getString(16));
				pipe.setScoverlevel(rs2.getString(26));
				pipe.setSinvertlevel(rs2.getString(27));
				pipe.setFdepth(rs2.getString(33));
				pipe.setFcoverlevel(rs2.getString(34));
				pipe.setFinvertlevel(rs2.getString(35));
				pipe.setCategory(rs2.getString(20));
				pipe.setCleaned(rs2.getString(41));
				pipe.setWeather(cameBiz.getCameName(rs2.getString(31), "weather"));
				pipe.setVideono(rs2.getString(24));
				pipe.setComment(rs2.getString(22));
				pipe.setProject(project);
				pipes.add(pipe);

				// 查询记录表格
				stat = conn.createStatement();
				ResultSet rs3 = stat.executeQuery("select * from table2 where ID=" + rs2.getInt(2) + " order by IID");
				while (rs3.next()) {
					Item item = new Item();
					item.setNo(rs3.getInt(1));
					item.setVideo(rs3.getString(8));
					item.setPhoto(rs3.getString(5));
					item.setDist(rs3.getString(9));
					item.setCont(rs3.getString(10));
					item.setCode(rs3.getString(3));
					item.setDiam(rs3.getString(11));
					item.setClockAt(rs3.getString(12));
					item.setClockTo(rs3.getString(13));
					item.setPercent(rs3.getString(14));
					item.setLengths(rs3.getString(15));
					item.setRemarks(rs3.getString(16));
					byte[] data = rs3.getBytes(19);
					if (data != null && data.length > 0) {
						name = AppUtils.UUIDCode();
						saveFile(data, ImgPath, name + ".png");
						item.setPicture(name);
					}
					item.setPipe(pipe);
					items.add(item);
				}
				rs3.close();
			}
			rs2.close();
			stat.close();
			conn.close();
			projectBiz.insertProject(project);
			for (Pipe pipe : pipes)
				pipeBiz.insertPipe(pipe);
			for (Item item : items)
				itemBiz.insertItem(item);
			temp.delete();
			return true;
		} catch (SQLException | IllegalStateException | IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/** 获取数据库链接对象 */
	private Connection getConnection(String path) {
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");
			String url = "jdbc:Access:///" + path;
			Connection conn = DriverManager.getConnection(url, "", "");
			conn.setAutoCommit(true);
			return conn;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public void close(){
		
	}

	/** 将文件流转换为图片 */
	public void saveFile(byte[] data, String filePath, String fileName) {
		try {
			File file = new File(filePath);
			if (!file.exists())
				file.mkdirs();
			OutputStream fstream = new FileOutputStream(filePath + fileName);
			OutputStream bstream = new BufferedOutputStream(fstream);
			bstream.write(data);
			bstream.flush();
			bstream.close();
			fstream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getYear(String text) {
		if ("<30".equals(text))
			return "10";
		else if ("30-50".equals(text))
			return "40";
		else if (">50".equals(text))
			return "60";
		else
			return "0";
	}
	public String getDate(String text) {
		if (!StringUtils.isEmpty(text))
			return text.replace("/", "-");
		else
			return "";
	}
	public double getValue(String text) {
		try {
			return Double.valueOf(text);
		} catch (Exception e) {
			return 0;
		}
	}
	public int getInt(String text){
		try {
			return Integer.valueOf(text);
		} catch (Exception e) {
			return 0;
		}
	}
}
