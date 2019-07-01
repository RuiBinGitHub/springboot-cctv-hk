package com.springboot.biz.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.springboot.biz.MarkPipeBiz;
import com.springboot.biz.MarkProjectBiz;
import com.springboot.biz.MessageBiz;
import com.springboot.biz.PipeBiz;
import com.springboot.dao.MarkProjectDao;
import com.springboot.entity.MarkPipe;
import com.springboot.entity.MarkProject;
import com.springboot.entity.Person;
import com.springboot.entity.Pipe;
import com.springboot.entity.Project;
import com.springboot.util.AppUtils;

@Transactional
@Service(value = "markProjectBiz")
public class MarkProjectBizImpl implements MarkProjectBiz {

	@Resource
	private MarkProjectDao markProjectDao;
	@Resource
	private MarkPipeBiz markPipeBiz;
	@Resource
	private PipeBiz pipeBiz;
	@Resource
	private MessageBiz messageBiz;

	private Map<String, Object> map = null;

	public void insertMarkProject(MarkProject markProject) {
		markProjectDao.insertMarkProject(markProject);
	}

	public void deleteMarkProject(MarkProject markProject) {
		markProjectDao.deleteMarkProject(markProject);
	}

	public MarkProject findInfoMarkProject(Map<String, Object> map) {
		if (!StringUtils.isEmpty(map.get("name")))
			map.put("name", "%" + map.get("name") + "%");
		return markProjectDao.findInfoMarkProject(map);
	}

	public MarkProject findInfoMarkProject(int id, Person person) {
		map = AppUtils.getMap("id", id, "person", person);
		return markProjectDao.findInfoMarkProject(map);
	}

	public List<MarkProject> findListMarkProject(Map<String, Object> map) {
		if (!StringUtils.isEmpty(map.get("name")))
			map.put("name", "%" + map.get("name") + "%");
		return markProjectDao.findListMarkProject(map);
	}

	public int getCount(Map<String, Object> map, int size) {
		if (!StringUtils.isEmpty(map.get("name")))
			map.put("name", "%" + map.get("name") + "%");
		int count = markProjectDao.getCount(map);
		return (int) Math.ceil((double) count / size);
	}

	public void setAverage(MarkProject markProject) {
		Map<String, Double> iMap = markProjectDao.getAverage(markProject);
		if (!StringUtils.isEmpty(iMap)) {
			markProject.setScore1(iMap.get("score1"));
			markProject.setScore2(iMap.get("score2"));
		}
	}

	public void appendMarkProject(MarkProject markProject) {
		this.insertMarkProject(markProject);
		Project project = markProject.getProject();
		List<Pipe> pipes = pipeBiz.findListPipe(project);
		for (int i = 0; pipes != null && i < pipes.size(); i++) {
			MarkPipe markPipe = new MarkPipe();
			markPipe.setScore1(5);
			markPipe.setScore2(5);
			markPipe.setPipe(pipes.get(i));
			markPipe.setMarkProject(markProject);
			markPipeBiz.insertMarkPipe(markPipe);
		}
		// 发送消息
		Person accept = project.getPerson();
		String title = "您有项目被评分";
		StringBuffer text = new StringBuffer("<p>" + accept.getNickname() + "，您好！</p>");
		text.append("<p>您所提交的项目：<a href='/CCTV/project/findinfo?id=" + project.getId() + "'>");
		text.append(project.getName() + "</a>被浏览并进行分数评定。</p>");
		text.append("<p>查看评分详情及评定结果请点击<a href='/CCTV/markinfo/findinfo?id=");
		text.append(markProject.getId() + "'>此链接</a>跳转。</p>");
		text.append("<p>【深圳麦斯迪埃高科技有限责任公司】</p>");
		text.append("<p style='text-align:right'>" + AppUtils.getDate("yyyy-MM-dd HH:mm:ss") + "</p>");
		text.append("<p style='color:#999'>提示：此邮件为系统提示邮件，查阅完毕可删除该信息。</p>");
		messageBiz.sendMessage(title, text.toString(), accept);
	}
}
