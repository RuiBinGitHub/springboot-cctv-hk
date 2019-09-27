package com.springboot.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.springboot.biz.PipeBiz;
import com.springboot.biz.ProjectBiz;
import com.springboot.entity.Item;
import com.springboot.entity.Pipe;
import com.springboot.entity.Project;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component(value = "createDOC")
public class CreateDOC {

	@Resource
	private Computes computes;
	@Resource
	private ProjectBiz projectBiz;
	@Resource
	private PipeBiz pipeBiz;

	public void initDOC(int id, String path) {
		try {
			Configuration config = new Configuration(Configuration.VERSION_2_3_22);
			config.setDefaultEncoding("utf-8");
			config.setClassForTemplateLoading(CreateDOC.class, "/");
			Template template = config.getTemplate("Template.xml");
			Project project = projectBiz.findInfoProject(id, null);

			String FileName = path + "\\" + project.getDate() + "_" + project.getName();
			OutputStream stream = new FileOutputStream(FileName + "_CCTV.doc");
			OutputStreamWriter writer = new OutputStreamWriter(stream, "utf-8");
			BufferedWriter bwriter = new BufferedWriter(writer);

			double length = 0;
			List<Pipe> pipes = pipeBiz.findListPipe(project);
			if (project.getStandard().indexOf("H") != -1 && pipes.size() >= 3) {
				pipes.remove(0);
				pipes.remove(1);
				pipes.remove(2);
			}
			for (int i = 0; pipes != null && i < pipes.size(); i++) {
				Pipe pipe = pipes.get(i);
				if (pipe.getSmanholeno() == null)
					pipe.setSmanholeno("");
				if (pipe.getFmanholeno() == null)
					pipe.setFmanholeno("");
				if (pipe.getHsize() == null)
					pipe.setHsize("");
				if (pipe.getShape() == null)
					pipe.setShape("");
				if (pipe.getMaterial() == null)
					pipe.setMaterial("");
				if (pipe.getVideono() == null)
					pipe.setVideono("");
				String value = pipe.getVideono();
				value = value.replaceAll("&", "&amp;");
				value = value.replaceAll("<", "&lt;");
				value = value.replaceAll(">", "&gt;");
				pipe.setVideono(value);
				
				value = pipe.getSmanholeno();
				value = value.replaceAll("&", "&amp;");
				value = value.replaceAll("<", "&lt;");
				value = value.replaceAll(">", "&gt;");
				pipe.setSmanholeno(value);
				
				value = pipe.getFmanholeno();
				value = value.replaceAll("&", "&amp;");
				value = value.replaceAll("<", "&lt;");
				value = value.replaceAll(">", "&gt;");
				pipe.setFmanholeno(value);
				// 计算分数和等级
				computes.computePipe(pipe, project.getStandard());
				List<Item> items = pipe.getItems();
				for (int j = 0; items != null && j < items.size(); j++) {
					Item item = items.get(j);
					if (item.getCode().length() > 2 &&item.getCode().indexOf("-") != -1)
						item.setCode(item.getCode().substring(0, item.getCode().length() - 2));
					if (item.getCode() != null && item.getCode().indexOf("SA") == 0 || item.getCode().equals("#4"))
						item.setIsSA("Y");
					else
						item.setIsSA("N");
					value = item.getDepict();
					value = value.replaceAll("&", "&amp;");
					value = value.replaceAll("<", "&lt;");
					value = value.replaceAll(">", "&gt;");
					item.setDepict(value);
				}
				length += pipe.getTotallength();
			}
			Map<String, Object> data = new HashMap<>();
			data.put("list", pipes);
			data.put("sum", length);
			template.process(data, bwriter);

		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
	}
}
