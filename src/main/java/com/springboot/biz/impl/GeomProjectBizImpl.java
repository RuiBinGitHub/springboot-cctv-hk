package com.springboot.biz.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.bean.Coordinate;
import com.springboot.biz.GeomPipeBiz;
import com.springboot.biz.GeomProjectBiz;
import com.springboot.biz.PipeBiz;
import com.springboot.dao.GeomProjectDao;
import com.springboot.entity.GeomPipe;
import com.springboot.entity.GeomProject;
import com.springboot.entity.Person;
import com.springboot.entity.Pipe;
import com.springboot.util.AppUtils;

@Transactional
@Service(value = "geomProjectBiz")
public class GeomProjectBizImpl implements GeomProjectBiz {

	@Resource
	private GeomProjectDao geomProjectDao;
	@Resource
	private GeomPipeBiz geomPipeBiz;
	@Resource
	private PipeBiz pipeBiz;

	private Map<String, Object> map = null;

	public void insertGeomProject(GeomProject geomProject) {
		geomProjectDao.insertGeomProject(geomProject);
	}

	public void updateGeomProject(GeomProject geomProject) {
		geomProjectDao.updateGeomProject(geomProject);
	}

	public void deleteGeomProject(GeomProject geomProject) {
		geomProjectDao.deleteGeomProject(geomProject);
	}

	public GeomProject findInfoGeomProject(Map<String, Object> map) {
		return geomProjectDao.findInfoGeomProject(map);
	}

	public List<GeomProject> findListGeomProject(Map<String, Object> map) {
		return geomProjectDao.findListGeomProject(map);
	}

	public GeomProject findInfoGeomProject(int id, Person person) {
		map = AppUtils.getMap("id", id, "person", person);
		return geomProjectDao.findInfoGeomProject(map);
	}

	public void appendGeomProject(GeomProject geomProject) {
		this.insertGeomProject(geomProject);
		List<GeomPipe> geomPipes = new ArrayList<>();
		List<Pipe> pipes = pipeBiz.findListPipe(geomProject.getProject());
		for (int i = 0; pipes != null && i < pipes.size(); i++) {
			Pipe pipe = pipes.get(i);
			GeomPipe geomPipe = new GeomPipe();
			geomPipe.setSmhNo(pipe.getSmanholeno());
			geomPipe.setFmhNo(pipe.getFmanholeno());
			geomPipe.setPipe(pipe);
			geomPipe.setGeomProject(geomProject);
			geomPipeBiz.insertGeomPipe(geomPipe);
			geomPipes.add(geomPipe);
		}
		geomProject.setGeomPipes(geomPipes);
	}

	public void importValue(GeomProject geomProject, MultipartFile file) {
		XSSFWorkbook workbook = AppUtils.getWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		Map<String, Coordinate> map = new HashMap<>();
		DecimalFormat foramt1 = new DecimalFormat("#0.000");
		DecimalFormat foramt2 = new DecimalFormat("#0.00");

		for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
			XSSFRow row = sheet.getRow(i);
			String smh = AppUtils.getString(row.getCell(0)); // 获取起始井编号
			Coordinate coordinate = new Coordinate();
			coordinate.setX(AppUtils.getValue(foramt1.format((AppUtils.getNumeric(row.getCell(4))))));
			coordinate.setY(AppUtils.getValue(foramt1.format((AppUtils.getNumeric(row.getCell(5))))));
			coordinate.setH(AppUtils.getValue(foramt2.format((AppUtils.getNumeric(row.getCell(3))))));
			map.put(smh, coordinate);
		}
		List<GeomPipe> geomPipeList = geomPipeBiz.findListGeomPipe(geomProject);
		for (GeomPipe geomPipe : geomPipeList) {
			if (map.containsKey(geomPipe.getSmhNo())) {
				Coordinate coordinate = map.get(geomPipe.getSmhNo());
				geomPipe.setSmhX(coordinate.getX());
				geomPipe.setSmhY(coordinate.getY());
				geomPipe.setSmhH(coordinate.getH());
				geomPipeBiz.replacePipegeom(geomPipe);
			}
			if (map.containsKey(geomPipe.getFmhNo())) {
				Coordinate coordinate = map.get(geomPipe.getFmhNo());
				geomPipe.setFmhX(coordinate.getX());
				geomPipe.setFmhY(coordinate.getY());
				geomPipe.setFmhH(coordinate.getH());
				geomPipeBiz.replacePipegeom(geomPipe);
			}
		}
	}

	public void computeCenter(GeomProject geomProject) {
		double x = 0.0, y = 0.0;
		String context = geomProject.getExtent();
		List<String> extent = Arrays.asList(context.split(","));
		for (int i = 0; i < extent.size() - 1; i++) {
			String[] coords = extent.get(i).split(" ");
			x += Double.valueOf(coords[0]);
			y += Double.valueOf(coords[1]);
		}
		x = x / (extent.size() - 1);
		y = y / (extent.size() - 1);
		geomProject.setCenter(x + " " + y);
	}

}
