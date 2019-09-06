package com.springboot.biz.impl;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.biz.PipeBiz;
import com.springboot.biz.ProjectBiz;
import com.springboot.dao.ProjectDao;
import com.springboot.entity.Person;
import com.springboot.entity.Pipe;
import com.springboot.entity.Project;
import com.springboot.util.AppUtils;

@Service(value = "projectBiz")
@Transactional
public class ProjectBizImpl implements ProjectBiz {

	@Resource
	private ProjectDao projectDao;
	@Resource
	private PipeBiz pipeBiz;

	private Map<String, Object> map = null;

	public void insertProject(Project project) {
		projectDao.insertProject(project);
	}

	public void updateProject(Project project) {
		projectDao.updateProject(project);
	}

	public void deleteProject(Project project) {
		projectDao.deleteProject(project);
	}

	public Project findInfoProject(int id, Person person) {
		map = AppUtils.getMap("id", id, "person", person);
		return projectDao.findInfoProject(map);
	}

	public Project findInfoProject(Map<String, Object> map) {
		if (!StringUtils.isEmpty(map.get("name")))
			map.put("name", "%" + map.get("name") + "%");
		return projectDao.findInfoProject(map);
	}

	public List<Project> findListProject(Map<String, Object> map) {
		if (!StringUtils.isEmpty(map.get("name")))
			map.put("name", "%" + map.get("name") + "%");
		return projectDao.findListProject(map);
	}

	public List<Project> markListProject(Map<String, Object> map) {
		if (!StringUtils.isEmpty(map.get("name")))
			map.put("name", "%" + map.get("name") + "%");
		return projectDao.markListProject(map);
	}

	public int getCountPage(Map<String, Object> map, int size) {
		if (!StringUtils.isEmpty(map.get("name")))
			map.put("name", "%" + map.get("name") + "%");
		int count = projectDao.getCount(map);
		return (int) Math.ceil((double) count / size);
	}

	/** 新建项目 */
	public int appendProject(Project project) {
		this.insertProject(project);
		Pipe pipe = new Pipe();
		pipe.setNo(1);
		pipe.setOperator(project.getOperator());
		pipe.setPurposes("Structural defects");
		pipe.setSlope(project.getSlope());
		if ("N".equals(project.getSlope()))
			pipe.setSloperef("N/A");
		pipe.setDate(project.getDate());
		pipe.setTime("");
		pipe.setDivision("--");
		pipe.setAreacode("--");
		pipe.setCategory("Z");
		pipe.setCleaned("N");
		pipe.setWeather("1");
		pipe.setProject(project);
		pipeBiz.appendPipe(pipe);
		return project.getId();
	}

	public void importDepth(Project project, MultipartFile file) {
		try {
			XSSFWorkbook workbook = AppUtils.getWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Map<String, String[]> map = new HashMap<>();
			DecimalFormat foramt1 = new DecimalFormat("#0.00");
			for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
				XSSFRow row = sheet.getRow(i);
				String[] values = new String[3];
				String mh = AppUtils.getString(row.getCell(0));
				double value1 = AppUtils.getNumeric(row.getCell(1));
				double value2 = AppUtils.getNumeric(row.getCell(2));
				double value3 = AppUtils.getNumeric(row.getCell(3));
				values[0] = value1 == 0.0 ? "--" : foramt1.format(value1);
				values[1] = value2 == 0.0 ? "--" : foramt1.format(value2);
				values[2] = value3 == 0.0 ? "--" : foramt1.format(value3);
				map.put(mh, values);
			}
			List<Pipe> pipes = pipeBiz.findListPipe(project);
			for (int i = 0; pipes != null && i < pipes.size(); i++) {
				Pipe pipe = pipes.get(i);
				if (map.containsKey(pipe.getSmanholeno())) {
					pipe.setSdepth(map.get(pipe.getSmanholeno())[0]);
					pipe.setScoverlevel(map.get(pipe.getSmanholeno())[1]);
					pipe.setSinvertlevel(map.get(pipe.getSmanholeno())[2]);
					pipeBiz.updatePipe(pipe);
				}
				if (map.containsKey(pipe.getFmanholeno())) {
					pipe.setFdepth(map.get(pipe.getFmanholeno())[0]);
					pipe.setFcoverlevel(map.get(pipe.getFmanholeno())[1]);
					pipe.setFinvertlevel(map.get(pipe.getFmanholeno())[2]);
					pipeBiz.updatePipe(pipe);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
