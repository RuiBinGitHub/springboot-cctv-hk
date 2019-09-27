package com.springboot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.springboot.biz.CameBiz;
import com.springboot.biz.ItemBiz;
import com.springboot.biz.PipeBiz;
import com.springboot.biz.ProjectBiz;
import com.springboot.entity.Item;
import com.springboot.entity.Pipe;
import com.springboot.entity.Project;

@Component(value = "createMDB")
public class CreateMDB {

	@Value(value = "${myfile}")
	private String myfile;
	@Value(value = "${mypath}")
	private String mypath;
	@Resource
	private Computes compute;
	@Resource
	private ProjectBiz projectBiz;
	@Resource
	private PipeBiz pipeBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private CameBiz cameBiz;

	private Connection conn = null;
	private PreparedStatement pstat = null;

	public void initMDB(int id, String path) {
		try {
			int ImageName = 1;
			Project project = projectBiz.findInfoProject(id, null);
			String FileName = path + "\\" + project.getDate() + "_" + project.getName();
			File file1 = new File(myfile + "temp.mdb"); // 模版位置
			File file2 = new File(FileName + "_CCTV.mdb");
			FileUtils.copyFile(file1, file2);
			conn = getConnection(file2.getPath());

			String insertSql1 = "insert into project values(?, ?, ?, ?, ?, ?)";
			pstat = conn.prepareStatement(insertSql1);
			pstat.setInt(1, 1);
			pstat.setString(2, "1");
			pstat.setString(3, project.getName());
			pstat.setString(4, project.getClient());
			pstat.setString(5, project.getStandard());
			pstat.setBoolean(6, "Y".equals(project.getSlope()) ? true : false);
			pstat.executeUpdate(); // 插入项目数据

			List<Pipe> pipes = pipeBiz.findListPipe(project);
			int itemNo = 1;
			StringBuffer str1 = new StringBuffer();
			StringBuffer str2 = new StringBuffer();
			for (int i = 0; i < 46; i++)
				str1.append("?, ");
			for (int i = 0; i < 19; i++)
				str2.append("?, ");
			for (int i = 0; pipes != null && i < pipes.size(); i++) {
				Pipe pipe = pipes.get(i);
				pstat = conn.prepareStatement("insert into table1 values(" + str1 + "?)");
				pstat.setInt(1, i + 1);
				pstat.setString(2, pipe.getNo() + "");
				pstat.setDouble(3, pipe.getTotallength());
				pstat.setString(4, "");
				pstat.setString(5, getValue(pipe.getPurposes()));
				pstat.setString(6, getValue(pipe.getOperator()));
				pstat.setString(7, getValue(pipe.getWorkorder()));
				pstat.setString(8, getValue(pipe.getReference()));
				pstat.setString(9, getValue(pipe.getRoadname()));
				pstat.setString(10, getValue(pipe.getBuilding()));
				pstat.setString(11, getValue(pipe.getSmanholeno()));
				pstat.setString(12, getValue(pipe.getFmanholeno()));
				pstat.setString(13, pipe.getHsize() + "");
				pstat.setString(14, cameBiz.getCameValue(pipe.getShape(), "shape"));
				pstat.setString(15, cameBiz.getCameValue(pipe.getMaterial(), "material"));
				pstat.setString(16, getValue(pipe.getSdepth()));
				pstat.setString(17, "");
				pstat.setString(18, getValue(pipe.getDistrict1()));
				pstat.setString(19, getValue(pipe.getDivision()));
				pstat.setString(20, getValue(pipe.getCategory()));
				pstat.setString(21, cameBiz.getCameValue(pipe.getLining(), "lining"));
				pstat.setString(22, getValue(pipe.getComment()));
				pstat.setString(23, "");
				pstat.setString(24, getValue(pipe.getVideono()));
				pstat.setString(25, getValue(pipe.getTime()));
				pstat.setString(26, getValue(pipe.getScoverlevel()));
				pstat.setString(27, getValue(pipe.getSinvertlevel()));
				pstat.setString(28, cameBiz.getCameValue(pipe.getUse(), "use"));
				pstat.setString(29, pipe.getPipelength() + "");
				pstat.setString(30, pipe.getNo() + "");
				pstat.setString(31, cameBiz.getCameValue(pipe.getWeather(), "weather"));
				pstat.setString(32, cameBiz.getCameValue(pipe.getDirection(), "direction"));
				pstat.setString(33, getValue(pipe.getFdepth()));
				pstat.setString(34, getValue(pipe.getFcoverlevel()));
				pstat.setString(35, getValue(pipe.getFinvertlevel()));
				pstat.setString(36, getValue(pipe.getAreacode()));
				pstat.setString(37, getValue(pipe.getDate()));
				pstat.setString(38, "");
				pstat.setString(39, pipe.getHsize() + "");
				pstat.setString(40, getValue("Z"));
				pstat.setString(41, getValue(pipe.getCleaned()));
				if (getInt(pipe.getYearlaid()) <= 30)
					pstat.setString(42, "<30");
				else if (getInt(pipe.getYearlaid()) >= 30 && getInt(pipe.getYearlaid()) <50)
					pstat.setString(42, "30-50");
				else
					pstat.setString(42, ">50");
				if ("Y".equals(pipe.getSlope()))
					pstat.setString(43, getValue("YES"));
				else
					pstat.setString(43, getValue("NO"));
				pstat.setString(44, getValue(pipe.getSloperef()));
				pstat.setString(45, getValue(pipe.getDistrict2()));
				pstat.setString(46, getValue(pipe.getDistrict3()));
				pstat.setString(47, getValue(pipe.getHousenum()));
				pstat.executeUpdate(); // 插入管道数据

				// 查询管道记录数据
				List<Item> items = itemBiz.findListItem(pipe);
				for (int j = 0; items != null && j < items.size(); j++) {
					Item item = items.get(j);
					compute.computeItem(item, project.getStandard());
					if (item.getCode().length() > 2 &&item.getCode().indexOf("-") != -1)
						item.setCode(item.getCode().substring(0, item.getCode().length() - 2));
					pstat = conn.prepareStatement("insert into table2 values(" + str2 + "?)");

					pstat.setInt(1, j);
					pstat.setDouble(2, 0);
					pstat.setString(3, getValue(item.getCode()));
					pstat.setString(4, item.getDepict() + "");
					pstat.setString(5, item.getPhoto() + "");
					pstat.setInt(6, (int) item.getGrade());
					pstat.setString(7, pipe.getNo() + "");
					pstat.setString(8, getValue(item.getVideo()));
					pstat.setDouble(9, Double.valueOf(item.getDist()));
					pstat.setString(10, getValue(item.getCont()));
					pstat.setString(11, getValue(item.getDiam()));
					pstat.setString(12, getValue(item.getClockAt()));
					pstat.setString(13, getValue(item.getClockTo()));
					pstat.setString(14, getValue(item.getPercent()));
					pstat.setString(15, getValue(item.getLengths()));
					pstat.setString(16, getValue(item.getRemarks()));
					pstat.setString(17, getValue(pipe.getSmanholeno()));
					pstat.setString(18, getValue(pipe.getFmanholeno()));
					if (item.getPicture() == null || "".equals(item.getPicture()))
						pstat.setNull(19, Types.BIT);
					else {
						InputStream stream = getStream(mypath + item.getPicture() + ".png");
						if (stream == null)
							pstat.setNull(19, Types.BIT);
						else {
							pstat.setBinaryStream(19, stream, stream.available());
							File Image = new File(mypath + item.getPicture() + ".png");
							File temap = new File(path + "\\" + ImageName++ + ".png");
							FileUtils.copyFile(Image, temap);
						}
					}
					pstat.setInt(20, itemNo++);
					pstat.executeUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pstat);
		}
	}

	private static Connection getConnection(String path) {
		try {
			Connection conn = null;
			Class.forName("com.hxtt.sql.access.AccessDriver");
			String url = "jdbc:Access:///" + path;
			conn = DriverManager.getConnection(url, "", "");
			conn.setAutoCommit(true);
			return conn;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 获取文本值 */
	private static String getValue(String value) {
		if (value == null)
			return "";
		else
			return value;
	}

	private int getInt(String text) {
		try {
			return Integer.valueOf(text);
		} catch (Exception e) {
			return 0;
		}
	}
	/** 获取文件流对象 */
	private static InputStream getStream(String path) {
		try {
			File file = new File(path);
			InputStream stream = new FileInputStream(file);
			return stream;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 关闭连接
	private static void close(Connection conn, PreparedStatement stat) {
		try {
			if (stat != null)
				stat.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
