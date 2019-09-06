package com.springboot.util;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.springboot.biz.CameBiz;
import com.springboot.biz.CodeBiz;
import com.springboot.biz.ItemBiz;
import com.springboot.biz.OperatorBiz;
import com.springboot.biz.PipeBiz;
import com.springboot.biz.ProjectBiz;
import com.springboot.entity.Code;
import com.springboot.entity.Company;
import com.springboot.entity.Item;
import com.springboot.entity.Operator;
import com.springboot.entity.Pipe;
import com.springboot.entity.Project;

@Component(value = "createPDF")
public class CreatePDF {

	@Value(value = "${mypath}")
	private String ImagePath;
	@Resource
	private OperatorBiz operatorBiz;
	@Resource
	private Computes computes;
	@Resource
	private ProjectBiz projectBiz;
	@Resource
	private PipeBiz pipeBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private CameBiz cameBiz;
	@Resource
	private CodeBiz codeBiz;

	private Map<String, Object> map = null;
	private Document document = null;
	private Project project = null;

	public void initPDF(int id, String path) {
		try {
			document = new Document(PageSize.A4);
			Paragraph wrap = new Paragraph("\n"); // 换行符
			project = projectBiz.findInfoProject(id, null);
			/*****************************************************************/
			document.setMargins(20, 20, 15, 10); // 左右上下间距
			String FileName = path + "\\" + project.getDate() + "_" + project.getName();
			OutputStream output = new FileOutputStream(FileName + "_CCTV.pdf");
			PdfWriter writer = PdfWriter.getInstance(document, output);
			MyPageEvent header = new MyPageEvent();
			writer.setPageEvent(header);
			document.open(); // 打开文档
			Font font = getFont(6, 0, null);
			/*****************************************************************/
			// 输出FormA
			int heightA = 14;
			double pipeLengthA = 0.0, tableHeightA = 0.0;
			DecimalFormat foramt1 = new DecimalFormat("#0");
			DecimalFormat foramt2 = new DecimalFormat("#0.0");
			map = AppUtils.getMap("project", project, "order", "");
			List<Pipe> pipes = pipeBiz.findListPipe(map);

			PdfPTable PipeTitleA = null;
			String WorkOrder = null, PipeUse = null;
			for (int i = 0; pipes != null && i < pipes.size(); i++) {
				Pipe pipe = computes.computePipe(pipes.get(i), project.getStandard());
				if (pipe.getWorkorder() == null)
					pipe.setWorkorder("");
				if (pipe.getUse() == null)
					pipe.setUse("");
				if (!pipe.getWorkorder().equals(WorkOrder) || !pipe.getUse().equals(PipeUse)) {
					WorkOrder = pipe.getWorkorder();
					PipeUse = pipe.getUse();
					if (tableHeightA != 0) {
						PipeTitleA.addCell(getCell("Total Length", font, 4, 1, 1, 16, 1));
						PipeTitleA.addCell(getCell(foramt2.format(pipeLengthA), font, 25, 1, 1, 16, 1));
						tableHeightA = PipeTitleA.getTotalHeight();
						document.add(PipeTitleA);
						appendWrap(document, 556 - tableHeightA);
						document.add(getStandar(project));
					}
					tableHeightA = 0.0;
					pipeLengthA = 0.0;
				}
				if (tableHeightA == 0.0) {
					document.newPage();
					writeTitle(document, "fm1");
					document.add(wrap); // 添加空行
					document.add(getTableFormA(pipe));
					document.add(wrap); // 添加空行
					PipeTitleA = getPipeTitleA();
				}
				String direct = "U".equals(pipe.getDirection()) ? "Upstream" : "Downstream";
				PipeTitleA.addCell(getCell(pipe.getNo() + "", font, 1, 1, 1, heightA, 1));
				PipeTitleA.addCell(getCell(pipe.getSmanholeno(), font, 1, 1, 1, heightA, 1));
				PipeTitleA.addCell(getCell(pipe.getFmanholeno(), font, 1, 1, 1, heightA, 1));
				PipeTitleA.addCell(getCell(direct, font, 1, 1, 1, heightA, 1));
				PipeTitleA.addCell(getCell(pipe.getTotallength() + "", font, 1, 1, 1, heightA, 1));
				PipeTitleA.addCell(getCell(pipe.getHsize(), font, 1, 1, 1, heightA, 1));
				PipeTitleA.addCell(getCell(pipe.getMaterial(), font, 1, 1, 1, heightA, 1));
				PipeTitleA.addCell(getCell(pipe.getSinvertlevel(), font, 1, 1, 1, heightA, 1));
				PipeTitleA.addCell(getCell(pipe.getScoverlevel(), font, 1, 1, 1, heightA, 1));
				PipeTitleA.addCell(getCell(pipe.getSdepth(), font, 1, 1, 1, heightA, 1));
				for (int s = 0; s < pipe.getScore().length; s++)
					PipeTitleA.addCell(getCell(foramt2.format(pipe.getScore()[s]), font, 1, 1, 1, heightA, 1));
				for (int s = 0; s < pipe.getGrade().length; s++)
					PipeTitleA.addCell(getCell(foramt1.format(pipe.getGrade()[s]), font, 1, 1, 1, heightA, 1));
				PipeTitleA.addCell(getCell(pipe.getComment(), font, 1, 1, 1, heightA, 1));

				pipeLengthA += pipe.getTotallength(); // 计算管道长度
				if (i == pipes.size() - 1) {
					PipeTitleA.addCell(getCell("Total Length", font, 4, 1, 1, 16, 1));
					PipeTitleA.addCell(getCell(foramt2.format(pipeLengthA), font, 25, 1, 1, 16, 1));
				}
				tableHeightA = PipeTitleA.getTotalHeight();
				if (tableHeightA >= 508 || i == pipes.size() - 1) {
					document.add(PipeTitleA);
					appendWrap(document, 556 - tableHeightA);
					document.add(getStandar(project));
					tableHeightA = 0;
				}
			}
			/*****************************************************************/
			// 输出FormB
			int heightB = 14, surve[] = new int[20];
			double pipeLengthB = 0.0, tableHeightB = 0.0;
			PdfPTable PipeTitleB1 = getPipeTitleB1(), PipeTitleB2 = null;
			for (int i = 0; pipes != null && i < pipes.size(); i++) {
				Pipe pipe = pipes.get(i);
				if (pipe.getWorkorder() == null)
					pipe.setWorkorder("");
				if (pipe.getUse() == null)
					pipe.setUse("");
				if (!pipe.getWorkorder().equals(WorkOrder) || !pipe.getUse().equals(PipeUse)) {
					WorkOrder = pipe.getWorkorder();
					PipeUse = pipe.getUse();
					if (tableHeightB != 0) {
						PipeTitleB2.addCell(getCell("Total", font, 4, 1, 1, 16, 1));
						PipeTitleB2.addCell(getCell(foramt2.format(pipeLengthB), font, 1, 1, 1, 16, 1));
						for (int j = 0; j < surve.length; j++) {
							if (surve[j] == 0)
								PipeTitleB2.addCell(getCell("", font, 1, 1, 1, 16, 1));
							else
								PipeTitleB2.addCell(getCell(surve[j] + "", font, 1, 1, 1, 16, 1));
							surve[j] = 0;
						}
						document.add(PipeTitleB2);

					}
					tableHeightB = 0.0;
					pipeLengthB = 0.0;
				}
				if (tableHeightB == 0.0) {
					document.newPage();
					writeTitle(document, "fm2");
					document.add(wrap); // 添加空行
					document.add(getTableFormA(pipe));
					document.add(wrap); // 添加空行
					document.add(PipeTitleB1); // 添加空行
					PipeTitleB2 = getPipeTitleB2();
				}
				String direct = "U".equals(pipe.getDirection()) ? "Upstream" : "Downstream";
				PipeTitleB2.addCell(getCell(pipe.getNo() + "", font, 1, 1, 1, heightB, 1));
				PipeTitleB2.addCell(getCell(pipe.getSmanholeno(), font, 1, 1, 1, heightB, 1));
				PipeTitleB2.addCell(getCell(pipe.getFmanholeno(), font, 1, 1, 1, heightB, 1));
				PipeTitleB2.addCell(getCell(direct, font, 1, 1, 1, heightB, 1));
				PipeTitleB2.addCell(getCell(pipe.getTotallength() + "", font, 1, 1, 1, heightB, 1));
				pipeLengthB += pipe.getTotallength();
				for (int j = 0; j < pipe.getSurve().length; j++) {
					String value = pipe.getSurve()[j] == 0 ? "" : pipe.getSurve()[j] + "";
					PipeTitleB2.addCell(getCell(value, font, 1, 1, 1, heightB, 1));
					surve[j] += pipe.getSurve()[j];
				}
				if (i == pipes.size() - 1) {
					PipeTitleB2.addCell(getCell("Total", font, 4, 1, 1, 16, 1));
					PipeTitleB2.addCell(getCell(foramt2.format(pipeLengthB), font, 1, 1, 1, 16, 1));
					for (int j = 0; j < surve.length; j++) {
						if (surve[j] == 0)
							PipeTitleB2.addCell(getCell("", font, 1, 1, 1, 16, 1));
						else
							PipeTitleB2.addCell(getCell(surve[j] + "", font, 1, 1, 1, 16, 1));
					}
				}
				tableHeightB = PipeTitleB2.getTotalHeight();
				if (tableHeightB >= 518 || i == pipes.size() - 1) {
					document.add(PipeTitleB2);
					tableHeightB = 0;
				}
			}
			/*****************************************************************/
			font = getFont(12, 1, null);
			Paragraph paragraph = new Paragraph("Coding", font);
			PdfPCell Coding = new PdfPCell(paragraph);
			Coding.setVerticalAlignment(Element.ALIGN_MIDDLE);
			Coding.setHorizontalAlignment(Element.ALIGN_RIGHT);
			Coding.setUseAscender(true);
			Coding.setRotation(90);
			Coding.setBorder(0);
			/*****************************************************************/
			// 输出FormC
			Code code = null;
			List<Item> items = null;
			PdfPTable TableFormC = null;
			PdfPTable explain = getExplain();
			for (int i = 0; pipes != null && i < pipes.size(); i++) {
				int page = 1;
				Pipe pipe = pipes.get(i);
				map = AppUtils.getMap("pipe", pipe, "page", page);
				while ((items = itemBiz.findListItem(map)).size() > 0) {
					map.put("page", (int) map.get("page") + 1);
					document.newPage();
					writeTitle(document, "fm3");
					TableFormC = getTableFormC(pipe);
					tableWrap(TableFormC, 2, 5);
					TableFormC.addCell(Coding);
					/*********************************************************/
					double size1 = Double.valueOf(items.get(0).getDist());
					double size2 = Double.valueOf(items.get(items.size() - 1).getDist());
					int length = 1; // 绘画管道的长度
					int propor = (int) (Math.ceil((size2 - size1) / 3.075) * 25);
					if (propor != 0)
						length = (int) ((size2 - size1) / propor * 100 / 12.3 * 350);
					font = getFont(8, 1, null);
					int[] widths = new int[] { 60, 40, 55, 40, 400, 40, 40 };
					PdfPTable title = getTable(7, 505, widths);
					title.addCell(getCell("Video No", font, 1, 1, 0, 16, 1));
					title.addCell(getCell("1:" + propor, font, 1, 1, 0, 0, 1));
					title.addCell(getCell("Chainage", font, 1, 1, 0, 0, 1));
					title.addCell(getCell("Code", font, 1, 1, 0, 0, 1));
					title.addCell(getCell("Observation", font, 1, 1, 0, 0, 0));
					title.addCell(getCell("Photo", font, 1, 1, 0, 0, 1));
					title.addCell(getCell("Grade", font, 1, 1, 0, 0, 1));
					PdfPCell pdfPCell = new PdfPCell(title);
					pdfPCell.setHorizontalAlignment(1);
					pdfPCell.setBorderWidthBottom(0);
					TableFormC.addCell(pdfPCell);
					/*********************************************************/
					// 添加流向图片
					TableFormC.addCell(getImage(pipe.getDirection()));
					/*********************************************************/
					Paragraph pragraph = new Paragraph("", font);
					pdfPCell = new PdfPCell(pragraph);
					pdfPCell.setBorderWidthTop(0);
					pdfPCell.setBorderWidthBottom(0);
					pdfPCell.setFixedHeight(460);
					TableFormC.addCell(pdfPCell);
					/*********************************************************/
					PdfContentByte content = writer.getDirectContent();
					PdfTemplate template = content.createTemplate(570, 540);
					PdfGraphics2D graphics = new PdfGraphics2D(content, 570, 540);
					graphics.setFont(new java.awt.Font("", 0, 9));
					graphics.setColor(new Color(150, 150, 150));
					graphics.fillRect(90, 50, 12, length); // 画管道

					double pipesize = size2 - size1 == 0.0 ? 1 : size2 - size1;
					for (int index = 0; items != null && index < items.size(); index++) {
						Item item = items.get(index);
						double itemdist = Double.valueOf(item.getDist()) - size1;
						if (item.getCode().equals("MH")) { // 画出MH
							double dist = Double.valueOf(item.getDist());
							int distance = "0.0".equals(item.getDist()) ? 20 : 50;
							int location = (int) (itemdist / pipesize * length + distance);
							if ("F".equals(pipe.getUse()))
								graphics.fillRect(81, location, 30, 30);
							else
								graphics.fillArc(81, location, 30, 30, 0, 360);
							if (item.getDist().equals("0.0"))
								graphics.drawString(pipe.getSmanholeno() + "", 88, 16);
							if (dist == pipe.getTotallength() && pipe.getTotallength() != 0)
								graphics.drawString(pipe.getFmanholeno() + "", 88, length + 90);
						}
						if (item.getCode().equals("JN")) { // 画出JN
							int location = (int) (itemdist / pipesize * length + 50);
							graphics.fillRect(78, location - 6, 12, 6);
						}
					}

					int index = 0, itemlentg = 50, distance = 0, location = 0; // 位置
					graphics.setFont(new java.awt.Font("", 0, 6)); // 设置画笔
					while (items != null && index < items.size()) {
						Item item = computes.computeItem(items.get(index++), project.getStandard());
						distance = (int) ((Double.valueOf(item.getDist()) - size1) / pipesize * length) + 50;
						location = distance - itemlentg < 0 ? itemlentg : distance;

						List<String> list = getList(item.getDepict(), 66);
						itemlentg = location + 10 * list.size();
						if (project.getStandard().indexOf("H") != -1)
							code = codeBiz.findInfoCode(AppUtils.getMap("hkccec", item.getCode()));
						if (project.getStandard().indexOf("M") != -1)
							code = codeBiz.findInfoCode(AppUtils.getMap("mscc", item.getCode()));
						String type = code == null ? "" : code.getType3();
						if (item.getGrade() >= 4) {
							graphics.setFont(new java.awt.Font("", 1, 9));
							if ("Service".equals(type))
								graphics.setColor(new Color(139, 69, 19));
							else
								graphics.setColor(new Color(255, 60, 60));
						} else {
							graphics.setFont(new java.awt.Font("", 0, 9));
							if ("Miscel-laneous".equals(type))
								graphics.setColor(new Color(0, 0, 0));
							else if ("Node".equals(type) || "Repair Points".equals(type))
								graphics.setColor(new Color(0, 128, 0));
							else if ("Service".equals(type))
								graphics.setColor(new Color(139, 69, 19));
							else
								graphics.setColor(new Color(255, 60, 60));
						}
						graphics.drawLine(90, distance, 102, distance);
						graphics.drawLine(102, distance, 125, location);
						graphics.drawLine(125, location, 145, location);
						graphics.drawString(foramt2.format(getValue(item.getDist())), 150, location + 3);
						graphics.drawString(item.getCode() + "", 180, location + 3);

						for (int no = 0; no < list.size(); no++)
							graphics.drawString(list.get(no), 208, location + 3 + no * 10);
						graphics.drawString(item.getPhoto() + "", 510, location + 3);
						graphics.drawString((int) item.getGrade() + "", 542, location + 3);

					}
					// 结束绘画
					graphics.dispose();
					content.addTemplate(template, 0, 0);
					// 添加备注
					pdfPCell = new PdfPCell(explain);
					pdfPCell.setBorderWidthTop(0);
					pdfPCell.setFixedHeight(36);
					TableFormC.addCell(pdfPCell);
					document.add(TableFormC);
				}
				// /*************************************************************/
				// 输出formD
				int[] widths = new int[] { 1 };
				Image image = null;
				PdfPCell pdfPCell = null;
				PdfPTable tableFormD = null;
				PdfPTable PipeFormD = getPipeFormD(pipe);
				items = itemBiz.findListItem(AppUtils.getMap("pipe", pipe, "picture", ""));
				for (int j = 0; items != null && j < items.size(); j++) {
					Item item = computes.computeItem(items.get(j), project.getStandard());
					if (j % 2 == 0) {
						document.newPage(); // 新建页面
						writeTitle(document, "fm4");
						tableFormD = getTable(1, 500, widths);
						pdfPCell = new PdfPCell(PipeFormD);
						pdfPCell.setBorderWidthBottom(0);
						pdfPCell.setFixedHeight(40);
						pdfPCell.setPadding(4);
						tableFormD.addCell(pdfPCell);
					}
					// 添加图片1
					image = Image.getInstance(ImagePath + item.getPicture() + ".png");
					float propor = image.getWidth() / image.getHeight();
					if (propor > (4f / 3))
						image.scaleAbsolute(360, 360 / propor);
					else if (propor < (4f / 3))
						image.scaleAbsolute(270 * propor, 270);
					else
						image.scaleAbsolute(360, 270);
					pdfPCell = new PdfPCell(image);
					pdfPCell.setFixedHeight(275);
					pdfPCell.setUseAscender(true);
					pdfPCell.setHorizontalAlignment(1);
					pdfPCell.setVerticalAlignment(5);
					pdfPCell.setBorderWidthTop(0);
					pdfPCell.setBorderWidthBottom(0);
					tableFormD.addCell(pdfPCell);
					// 添加说明1
					pdfPCell = new PdfPCell(getItemFormD(item));
					pdfPCell.setUseAscender(true);
					pdfPCell.setFixedHeight(36);
					pdfPCell.setHorizontalAlignment(1);
					pdfPCell.setVerticalAlignment(5);
					pdfPCell.setBorderWidthTop(0);
					pdfPCell.setBorderWidthBottom(0);
					tableFormD.addCell(pdfPCell);

					if (j % 2 == 1 || j == items.size() - 1) {
						float height = 665 - tableFormD.getTotalHeight();
						pdfPCell = new PdfPCell();
						pdfPCell.setBorderWidthTop(0);
						pdfPCell.setBorderWidthBottom(0);
						pdfPCell.setFixedHeight(height);
						tableFormD.addCell(pdfPCell);
						// 添加说明
						pdfPCell = new PdfPCell(explain);
						pdfPCell.setBorderWidthTop(0);
						pdfPCell.setFixedHeight(34);
						tableFormD.addCell(pdfPCell);
						document.add(tableFormD);
					}
				}
			}
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		} finally {
			document.close();
		}
	}

	private Font getFont(int size, int bold, BaseColor color) {
		try {
			BaseFont baseFont = BaseFont.createFont("Helvetica", "Cp1252", false);
			Font font = new Font(baseFont, size, bold);
			// font.setFamily("Times New Roman");
			font.setFamily("宋体");
			if (color != null)
				font.setColor(color);
			return font;
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Font getChineseFont(int size, int bold, BaseColor color) {
		try {
			BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			Font font = new Font(baseFont, size, bold);
			font.setFamily("Times New Roman");
			if (color != null)
				font.setColor(color);
			return font;
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 设置表头图片
	private void writeTitle(Document document, String name) {
		try {
			String path = "classpath:static/picture/";
			File file = ResourceUtils.getFile(path + name + ".png");
			Image image = Image.getInstance(file.getPath());
			image.scaleAbsolute(400, 80);
			image.setAlignment(1);
			document.add(image);
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
	}

	private PdfPTable getPipeTitleA() {
		Font font = getFont(8, 1, null);
		int[] widths = new int[29];
		this.getWidths(widths, 0, 25, 30, 30, 48, 24, 24, 24, 25, 25, 25, 24, 24, 24, 24, 24);
		this.getWidths(widths, 15, 24, 24, 24, 24, 15, 15, 15, 15, 15, 15, 15, 15, 15, 80);
		PdfPTable pipeTitleA = getTable(29, 570, widths);
		appendTitle(pipeTitleA, "ID", font, 1, 3, 0, 0);
		appendTitle(pipeTitleA, "Manhole", font, 2, 1, 25, 0);
		appendTitle(pipeTitleA, "Direction", font, 1, 3, 0, 0);
		appendTitle(pipeTitleA, "Pipe", font, 3, 1, 0, 0);
		appendTitle(pipeTitleA, "Manhole(From)", font, 3, 1, 0, 0);
		appendTitle(pipeTitleA, "Score", font, 9, 1, 0, 0);
		appendTitle(pipeTitleA, "Grades", font, 9, 1, 0, 0);
		appendTitle(pipeTitleA, "Remarks", font, 1, 3, 0, 0);
		appendTitle(pipeTitleA, "From", font, 1, 2, 0, 0);
		appendTitle(pipeTitleA, "To", font, 1, 2, 0, 0);
		appendTitle(pipeTitleA, "Length(m)", font, 1, 2, 0, 90);
		appendTitle(pipeTitleA, "Size(mm)", font, 1, 2, 0, 90);
		appendTitle(pipeTitleA, "Material", font, 1, 2, 0, 90);
		appendTitle(pipeTitleA, "I.L", font, 1, 2, 0, 0);
		appendTitle(pipeTitleA, "C.L", font, 1, 2, 0, 0);
		appendTitle(pipeTitleA, "Depths(m)", font, 1, 2, 0, 90);
		appendTitle(pipeTitleA, "SCG", font, 3, 1, 25, 0);
		appendTitle(pipeTitleA, "ICG", font, 3, 1, 0, 0);
		appendTitle(pipeTitleA, "SPG", font, 3, 1, 0, 0);
		appendTitle(pipeTitleA, "SCG", font, 3, 1, 0, 0);
		appendTitle(pipeTitleA, "ICG", font, 3, 1, 0, 0);
		appendTitle(pipeTitleA, "SPG", font, 3, 1, 0, 0);
		for (int i = 0; i < 6; i++) {
			appendTitle(pipeTitleA, "P", font, 1, 1, 25, 0);
			appendTitle(pipeTitleA, "M", font, 1, 1, 0, 0);
			appendTitle(pipeTitleA, "T", font, 1, 1, 0, 0);
		}
		return pipeTitleA;
	}

	private PdfPTable getPipeTitleB1() {
		Font font1 = getFont(12, 1, null);
		int[] widths = new int[] { 219, 180, 160, 40 };
		PdfPTable pipeTitleB1 = getTable(4, 570, widths);
		appendTitle(pipeTitleB1, "Works Order No", font1, 1, 2, 0, 0);
		appendTitle(pipeTitleB1, "Colour CCTV Drainage Survey", font1, 3, 1, 20, 0);
		appendTitle(pipeTitleB1, "Pipe", font1, 1, 1, 20, 0);
		appendTitle(pipeTitleB1, "Service Condition", font1, 1, 1, 20, 0);
		appendTitle(pipeTitleB1, "MISC", font1, 1, 1, 20, 0);
		return pipeTitleB1;
	}

	private PdfPTable getPipeTitleB2() {
		int[] widths = new int[25];
		this.getWidths(widths, 0, 25, 32, 32, 60, 35, 35, 20, 20, 20, 20, 20, 20);
		this.getWidths(widths, 12, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20);
		Font font = getFont(8, 1, null);
		PdfPTable pipeTitleB2 = getTable(25, 570, widths);
		appendTitle(pipeTitleB2, "ID", font, 1, 2, 0, 0);
		appendTitle(pipeTitleB2, "Manhole", font, 2, 1, 42, 0);
		appendTitle(pipeTitleB2, "Direction", font, 1, 2, 0, 0);
		appendTitle(pipeTitleB2, "Meters", font, 1, 2, 0, 0);
		appendTitle(pipeTitleB2, "Urgent", font, 1, 2, 0, 0);
		appendTitle(pipeTitleB2, "Cracked", font, 1, 2, 84, 90);
		appendTitle(pipeTitleB2, "Fractured", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Broken", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Deformed", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Collapsed", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Hole", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Surface Spalling/Wear", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Joint Displaced", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Open Joint", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Roots", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Infiltration", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Encrustation", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Silt", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Grease", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Obstruction", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Water Line", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Line", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Survey Abandoned", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Camera Under Water", font, 1, 2, 0, 90);
		appendTitle(pipeTitleB2, "Form", font, 1, 1, 42, 0);
		appendTitle(pipeTitleB2, "To", font, 1, 1, 42, 0);
		return pipeTitleB2;
	}

	private PdfPTable getTableFormA(Pipe pipe) {
		Company company = project.getCompany();
		if (StringUtils.isEmpty(company))
			company = project.getPerson().getCompany();
		map = AppUtils.getMap("fullname", pipe.getOperator(), "company", company);
		Operator operator = operatorBiz.findInfoOperator(map);
		String operator1 = "", operator2 = "";
		if (!StringUtils.isEmpty(operator)) {
			operator1 = operator.getFullname();
			operator2 = operator.getMembernumber();
		}
		String use = "";
		if ("F".equals(pipe.getUse()))
			use = "Foul";
		else
			use = "Surface water";
		int height = 14;
		Font font1 = getFont(10, 0, null); // 正常字体
		Font font2 = getFont(10, 1, null); // 加粗字体
		int[] widths = new int[] { 16, 20, 1, 16, 20 };// 按百分比分配单元格宽带
		PdfPTable table = getTable(5, 520, widths); // 创建信息表格1
		/********************************************************************/
		table.addCell(getCell("Project No", font1, 1, 1, 0, height, 0));
		table.addCell(getCell(project.getName(), font2, 1, 1, 1, 0, 0));
		table.addCell(getCell("", font1, 1, 1, 0, 0, 0));
		table.addCell(getCell("Works Order No", font1, 1, 1, 0, height, 0));
		table.addCell(getCell(pipe.getWorkorder(), font2, 1, 1, 1, 0, 0));
		table.addCell(getCell("", font1, 5, 1, 0, 4, 0)); // 插入空白行

		table.addCell(getCell("Slope Reference No", font1, 1, 1, 0, 0, 0));
		table.addCell(getCell(pipe.getSloperef(), font2, 1, 1, 1, 0, 0));
		table.addCell(getCell("", font1, 1, 1, 0, 0, 0));
		table.addCell(getCell("Operator", font1, 1, 1, 0, 0, 0));
		table.addCell(getCell(operator1, font2, 1, 1, 1, 0, 0));
		table.addCell(getCell("", font1, 5, 1, 0, 4, 0)); // 插入空白行

		table.addCell(getCell("Company", font1, 1, 1, 0, height, 0));
		table.addCell(getCell(project.getClient(), font2, 1, 1, 1, 0, 0));
		table.addCell(getCell("", font1, 1, 1, 0, 0, 0));
		table.addCell(getCell("Operator No", font1, 1, 1, 0, 0, 0));
		table.addCell(getCell(operator2, font2, 1, 1, 1, 0, 0));
		table.addCell(getCell("", font1, 5, 1, 0, 4, 0)); // 插入空白行

		table.addCell(getCell("Drain/Sewer Use", font1, 1, 1, 0, height, 0));
		table.addCell(getCell(use, font2, 1, 1, 1, 0, 0));
		table.addCell(getCell("", font2, 1, 1, 0, 0, 0));
		table.addCell(getCell("Date", font1, 1, 1, 0, height, 0));
		table.addCell(getCell(pipe.getDate(), font2, 1, 1, 1, 0, 0));
		table.addCell(getCell("", font1, 5, 1, 0, 4, 0)); // 插入空白行

		table.addCell(getCell("Road Name", font1, 1, 1, 0, height, 0));
		table.addCell(getCell(pipe.getRoadname(), font2, 1, 1, 1, 0, 0));
		table.addCell(getCell("", font2, 1, 1, 0, 0, 0));
		table.addCell(getCell("Time", font1, 1, 1, 0, height, 0));
		table.addCell(getCell(pipe.getTime(), font2, 1, 1, 1, 0, 0));
		return table;
	}

	private PdfPTable getTableFormC(Pipe pipe) {
		int[] widths = new int[] { 30, 680 };
		Font font = getFont(12, 1, null);
		PdfPTable report = getTable(2, 540, widths);
		Paragraph paragraph = new Paragraph("Heading", font);
		PdfPCell cell = new PdfPCell(paragraph);
		cell.setVerticalAlignment(5);
		cell.setHorizontalAlignment(2);
		cell.setUseAscender(true);
		cell.setRotation(90);
		cell.setBorder(0);
		report.addCell(cell);
		/*********************************************************************/
		Font font1 = getFont(7, 0, null);
		Font font2 = getFont(7, 1, null);
		// DecimalFormat foramt = new DecimalFormat("#0.0");
		widths = new int[] { 80, 120, 80, 120, 80, 120, 80, 120 };
		PdfPTable pipeInfo = getTable(8, 505, widths);
		// 第一行
		/** border为边框数组，分别代表上右下左 */
		int[] border = new int[] { 1, 1, 0, 1 };
		appendTableTitle(pipeInfo, "Project No", font1, 10);
		appendTableValue(pipeInfo, project.getName(), font2, 10, 1, border);
		appendTableTitle(pipeInfo, "Operator", font1, 10);
		appendTableValue(pipeInfo, pipe.getOperator(), font2, 10, 1, border);
		appendTableTitle(pipeInfo, "Date", font1, 10);
		appendTableValue(pipeInfo, pipe.getDate(), font2, 10, 1, border);
		appendTableTitle(pipeInfo, "ID", font1, 10);
		appendTableValue(pipeInfo, pipe.getNo() + "", font2, 10, 1, border);

		border = new int[] { 0, 1, 1, 1 };
		appendTableTitle(pipeInfo, "Works Order", font1, 10);
		appendTableValue(pipeInfo, pipe.getWorkorder(), font2, 10, 1, border);
		appendTableTitle(pipeInfo, "Purpose", font1, 10);
		appendTableValue(pipeInfo, pipe.getPurposes(), font2, 10, 1, border);
		appendTableTitle(pipeInfo, "Time", font1, 10);
		appendTableValue(pipeInfo, pipe.getTime(), font2, 10, 1, border);
		appendTableTitle(pipeInfo, "PLR", font1, 10);
		appendTableValue(pipeInfo, pipe.getReference(), font2, 10, 1, border);
		tableWrap(pipeInfo, 8, 5); // 空行
		// 第二行
		border = new int[] { 1, 1, 0, 1 };
		appendTableTitle(pipeInfo, "Start MH", font1, 10);
		appendTableValue(pipeInfo, pipe.getSmanholeno(), font2, 10, 1, border);
		appendTableTitle(pipeInfo, "Finish MH", font1, 10);
		appendTableValue(pipeInfo, pipe.getFmanholeno(), font2, 10, 1, border);
		appendTableTitle(pipeInfo, "Weather", font1, 10);
		appendTableValue(pipeInfo, cameBiz.getCameValue(pipe.getWeather(), "weather"), font2, 10, 1, border);
		appendTableTitle(pipeInfo, "Use", font1, 10);
		appendTableValue(pipeInfo, cameBiz.getCameValue(pipe.getUse(), "use"), font2, 10, 1, border);

		border = new int[] { 0, 1, 0, 1 };
		appendTableTitle(pipeInfo, "Depth", font1, 10);
		if ("--".equals(pipe.getSdepth()))
			appendTableValue(pipeInfo, "N/A", font2, 10, 1, border);
		else
			appendTableValue(pipeInfo, pipe.getSdepth(), font2, 10, 1, border);
		appendTableTitle(pipeInfo, "Depth", font1, 10);
		if ("--".equals(pipe.getFdepth()))
			appendTableValue(pipeInfo, "N/A", font2, 10, 1, border);
		else
			appendTableValue(pipeInfo, pipe.getFdepth(), font2, 10, 1, border);
		appendTableTitle(pipeInfo, "Cleaned", font1, 10);
		appendTableValue(pipeInfo, pipe.getCleaned(), font2, 10, 1, border);
		appendTableTitle(pipeInfo, "Direction", font1, 10);
		appendTableValue(pipeInfo, cameBiz.getCameValue(pipe.getDirection(), "direction"), font2, 10, 1, border);

		appendTableTitle(pipeInfo, "Cover Level", font1, 10);
		if ("--".equals(pipe.getScoverlevel()))
			appendTableValue(pipeInfo, "N/A", font2, 10, 1, border);
		else
			appendTableValue(pipeInfo, pipe.getScoverlevel(), font2, 10, 1, border);
		appendTableTitle(pipeInfo, "Cover Level", font1, 10);
		if ("--".equals(pipe.getFcoverlevel()))
			appendTableValue(pipeInfo, "N/A", font2, 10, 1, border);
		else
			appendTableValue(pipeInfo, pipe.getFcoverlevel(), font2, 10, 1, border);
		appendTableTitle(pipeInfo, "Score", font1, 10);
		appendTableValue(pipeInfo, pipe.getScore()[3] + "(ST)    " + pipe.getScore()[0] + "(SE)", font2, 10, 1, border);
		appendTableTitle(pipeInfo, "Pipe Length", font1, 10);
		if (pipe.getPipelength() == 0)
			appendTableValue(pipeInfo, "Unknow", font2, 10, 1, border);
		else
			appendTableValue(pipeInfo, pipe.getPipelength() + "", font2, 10, 1, border);

		border = new int[] { 0, 1, 1, 1 };
		appendTableTitle(pipeInfo, "Invert Leve", font1, 10);
		if ("--".equals(pipe.getSinvertlevel()))
			appendTableValue(pipeInfo, "N/A", font2, 10, 1, border);
		else
			appendTableValue(pipeInfo, pipe.getSinvertlevel(), font2, 10, 1, border);
		appendTableTitle(pipeInfo, "Invert Leve", font1, 10);
		if ("--".equals(pipe.getFinvertlevel()))
			appendTableValue(pipeInfo, "N/A", font2, 10, 1, border);
		else
			appendTableValue(pipeInfo, pipe.getFinvertlevel(), font2, 10, 1, border);
		appendTableTitle(pipeInfo, "Grade", font1, 10);
		appendTableValue(pipeInfo, pipe.getGrade()[3] + "(ST)    " + pipe.getGrade()[0] + "(SE)", font2, 10, 1, border);
		appendTableTitle(pipeInfo, "Total Length", font1, 10);
		appendTableValue(pipeInfo, pipe.getTotallength() + "", font2, 10, 1, border);
		tableWrap(pipeInfo, 8, 5); // 空行
		// 第三行
		border = new int[] { 1, 1, 0, 1 };
		appendTableTitle(pipeInfo, "Building", font1, 10);
		appendTableValue(pipeInfo, pipe.getBuilding(), font2, 10, 3, border);
		appendTableTitle(pipeInfo, "Size", font1, 10);
		appendTableValue(pipeInfo, pipe.getHsize(), font2, 10, 3, border);

		border = new int[] { 0, 1, 0, 1 };
		appendTableTitle(pipeInfo, "House No", font1, 10);
		appendTableValue(pipeInfo, pipe.getHousenum(), font2, 10, 3, border);
		appendTableTitle(pipeInfo, "Shape", font1, 10);
		appendTableValue(pipeInfo, cameBiz.getCameValue(pipe.getShape(), "shape"), font2, 10, 3, border);

		appendTableTitle(pipeInfo, "Road Name", font1, 10);
		appendTableValue(pipeInfo, pipe.getRoadname(), font2, 10, 3, border);
		appendTableTitle(pipeInfo, "Material", font1, 10);
		appendTableValue(pipeInfo, cameBiz.getCameValue(pipe.getMaterial(), "material"), font2, 10, 3, border);

		appendTableTitle(pipeInfo, "District3", font1, 10);
		appendTableValue(pipeInfo, pipe.getDistrict3(), font2, 10, 3, border);
		appendTableTitle(pipeInfo, "Lining", font1, 10);
		appendTableValue(pipeInfo, cameBiz.getCameValue(pipe.getLining(), "lining"), font2, 10, 3, border);

		appendTableTitle(pipeInfo, "District2", font1, 10);
		appendTableValue(pipeInfo, pipe.getDistrict2(), font2, 10, 3, border);
		appendTableTitle(pipeInfo, "Slope No", font1, 10);
		appendTableValue(pipeInfo, pipe.getSloperef(), font2, 10, 3, border);

		appendTableTitle(pipeInfo, "District1", font1, 10);
		appendTableValue(pipeInfo, pipe.getDistrict1(), font2, 10, 3, border);
		appendTableTitle(pipeInfo, "Category", font1, 10);
		appendTableValue(pipeInfo, pipe.getCategory(), font2, 10, 3, border);

		appendTableTitle(pipeInfo, "Division", font1, 10);
		appendTableValue(pipeInfo, pipe.getDivision(), font2, 10, 3, border);
		appendTableTitle(pipeInfo, "Comment", font1, 10);
		appendTableValue(pipeInfo, pipe.getComment(), font2, 10, 3, border);

		border = new int[] { 0, 1, 1, 1 };
		appendTableTitle(pipeInfo, "Area Code", font1, 10);
		appendTableValue(pipeInfo, pipe.getAreacode(), font2, 10, 3, border);
		appendTableTitle(pipeInfo, "Video No", font1, 10);
		appendTableValue(pipeInfo, pipe.getVideono(), font2, 10, 3, border);
		nestTable(report, pipeInfo, 1, 1, 4, 1, 1);
		return report;
	}

	private PdfPTable getPipeFormD(Pipe pipe) {
		Font font = getFont(8, 0, null);
		int[] widths = new int[] { 40, 110, 40, 60, 40, 110, 30, 60 };
		PdfPTable table = getTable(8, 480, widths);
		int[] border = new int[] { 1, 1, 0, 1 };
		appendTableTitle(table, "Road Name", font, 10);
		appendTableValue(table, pipe.getRoadname(), font, 10, 1, border);
		appendTableTitle(table, "Start MH", font, 10);
		appendTableValue(table, pipe.getSmanholeno(), font, 10, 1, border);
		appendTableTitle(table, "Size", font, 10);
		appendTableValue(table, pipe.getHsize() + "", font, 10, 1, border);
		appendTableTitle(table, "ID", font, 10);
		appendTableValue(table, pipe.getNo() + "", font, 10, 1, border);

		border = new int[] { 0, 1, 0, 1 };
		appendTableTitle(table, "", font, 10);
		appendTableValue(table, "", font, 10, 1, border);
		appendTableTitle(table, "", font, 10);
		appendTableValue(table, "", font, 10, 1, border);
		appendTableTitle(table, "Shape", font, 10);
		appendTableValue(table, cameBiz.getCameValue(pipe.getShape(), "shape"), font, 10, 1, border);
		appendTableTitle(table, "", font, 10);
		appendTableValue(table, "", font, 10, 1, border);

		border = new int[] { 0, 1, 1, 1 };
		appendTableTitle(table, "Building", font, 10);
		appendTableValue(table, pipe.getBuilding(), font, 10, 1, border);
		appendTableTitle(table, "Finish MH", font, 10);
		appendTableValue(table, pipe.getFmanholeno(), font, 10, 1, border);
		appendTableTitle(table, "Material", font, 10);
		appendTableValue(table, cameBiz.getCameValue(pipe.getMaterial(), "material"), font, 10, 1, border);
		appendTableTitle(table, "PLR", font, 10);
		appendTableValue(table, pipe.getReference(), font, 10, 1, border);
		return table;
	}

	private PdfPCell getImage(String direct) {
		try {
			String path = "classpath:static/picture/";
			String name = "U".equals(direct) ? "Flow_up.png" : "Flow_dp.png";
			File file = ResourceUtils.getFile(path + name);
			Image image = Image.getInstance(file.getPath());
			image.scaleAbsolute(18, 110); // 设置图片大小
			PdfPCell pdfPCell = new PdfPCell(image);
			pdfPCell.setVerticalAlignment(5);
			pdfPCell.setRowspan(2);
			pdfPCell.setBorder(0);
			return pdfPCell;
		} catch (IOException | BadElementException e) {
			e.printStackTrace();
			return null;
		}
	}

	private PdfPTable getItemFormD(Item item) {
		if (project.getStandard().indexOf("H") != -1)
			map = AppUtils.getMap("hkccec", item.getCode());
		if (project.getStandard().indexOf("M") != -1)
			map = AppUtils.getMap("mscc", item.getCode());
		Code code = codeBiz.findInfoCode(map);
		BaseColor color = null;
		String type = code == null ? "" : code.getType3();
		Font font1 = getFont(8, 0, null);
		Font font2 = getFont(8, 0, color);
		if (item.getGrade() >= 4) {
			if ("Service".equals(type))
				color = new BaseColor(139, 69, 19);
			else
				color = new BaseColor(255, 60, 60);
			font2 = getFont(8, 1, color);
		} else {
			if ("Miscel-laneous".equals(type))
				color = new BaseColor(0, 0, 0);
			else if ("Node".equals(type) || "Repair Points".equals(type))
				color = new BaseColor(0, 128, 0);
			else if ("Service".equals(type))
				color = new BaseColor(139, 69, 19);
			else
				color = new BaseColor(255, 60, 60);
			font2 = getFont(8, 0, color);
		}
		String name = project.getName() + "/" + item.getPipe().getWorkorder() + "/" + item.getPipe().getNo();
		int[] widths = new int[] { 80, 240, 80, 160 };
		PdfPTable table = getTable(4, 360, widths);

		int[] border = new int[] { 1, 0, 0, 1 };
		TableValue(table, "Video No:", font1, 10, 1, border, 2);
		border = new int[] { 1, 0, 0, 0 };
		TableValue(table, item.getPipe().getVideono(), font1, 10, 1, border, 0);
		TableValue(table, "Chainage:", font1, 10, 1, border, 2);
		border = new int[] { 1, 1, 0, 0 };
		TableValue(table, item.getDist() + " m", font1, 10, 1, border, 0);

		border = new int[] { 0, 0, 0, 1 };
		TableValue(table, "Photo Ref:", font1, 10, 1, border, 2);
		border = new int[] { 0, 1, 0, 0 };
		TableValue(table, name + "/" + item.getPhoto(), font1, 10, 3, border, 0);

		border = new int[] { 0, 0, 1, 1 };
		TableValue(table, "Observation:", font1, 10, 1, border, 2);
		border = new int[] { 0, 1, 1, 0 };
		TableValue(table, item.getDepict(), font2, 10, 3, border, 0);
		return table;
	}

	private PdfPTable getStandar(Project project) {
		String standar1 = "", standar2 = "";
		String standar = project.getStandard();
		if (standar != null && standar.indexOf("H") != -1)
			standar1 = "√";
		if (standar != null && standar.indexOf("M") != -1)
			standar2 = "√";
		String slope1 = "Y".equals(project.getSlope()) ? "√" : "";
		String slope2 = "N".equals(project.getSlope()) ? "√" : "";
		Font font1 = getFont(8, 1, null);
		Font font2 = getChineseFont(8, 0, null);
		int[] widths = new int[] { 60, 12, 60, 12, 140, 60, 12, 60, 12, 60 };
		PdfPTable standarTable = getTable(10, 500, widths);
		standarTable.addCell(getCell("Standard: ", font1, 1, 1, 0, 12, 2));
		standarTable.addCell(getCell(standar1, font2, 1, 1, 1, 12, 1));
		standarTable.addCell(getCell("HKCCEC 2009", font1, 1, 1, 0, 12, 1));
		standarTable.addCell(getCell(standar2, font2, 1, 1, 1, 12, 1));
		standarTable.addCell(getCell("MSCC 2004", font1, 1, 1, 0, 12, 0));
		standarTable.addCell(getCell("Slope: ", font1, 1, 1, 0, 12, 2));
		standarTable.addCell(getCell(slope1, font2, 1, 1, 1, 12, 1));
		standarTable.addCell(getCell("YES", font1, 1, 1, 0, 12, 0));
		standarTable.addCell(getCell(slope2, font2, 1, 1, 1, 12, 1));
		standarTable.addCell(getCell("NO", font1, 1, 1, 0, 12, 0));
		standarTable.addCell(getCell("", font1, 10, 1, 0, 8, 0)); // 插入间隔行

		standarTable.addCell(getCell("", font1, 3, 1, 0, 12, 2));
		standarTable.addCell(getCell("", font1, 2, 1, 0, 12, 0));
		standarTable.addCell(getCell("NOTE: ", font1, 1, 1, 0, 12, 2));
		standarTable.addCell(getCell("P--Peak; M--Mean; T--Total", font1, 4, 1, 0, 12, 0));
		return standarTable;
	}

	private PdfPTable getExplain() {
		int[] widths = new int[] { 3, 4, 2 };
		PdfPTable explain = getTable(3, 480, widths);
		BaseColor color = new BaseColor(255, 60, 60);
		Font font1 = getFont(7, 0, color);
		explain.addCell(getCell("•Structural Defects", font1, 1, 1, 0, 12, 0));
		font1 = getFont(7, 1, color);
		explain.addCell(getCell("•Structural Defects with Grade 4 or 5", font1, 1, 1, 0, 10, 0));
		color = new BaseColor(0, 108, 0);
		font1 = getFont(7, 0, color);
		explain.addCell(getCell("•Constructional Features", font1, 1, 1, 0, 10, 0));
		color = new BaseColor(139, 69, 19);
		font1 = getFont(7, 0, color);
		explain.addCell(getCell("•Service Defects", font1, 1, 1, 0, 10, 0));
		font1 = getFont(7, 1, color);
		explain.addCell(getCell("•Service Defects with Grade 4 or 5", font1, 1, 1, 0, 10, 0));
		color = new BaseColor(0, 0, 0);
		font1 = getFont(7, 0, color);
		explain.addCell(getCell("•Miscellaneous Features", font1, 1, 1, 0, 10, 0));
		return explain;
	}

	/** 创建表格 */
	private PdfPTable getTable(int colspan, float width, int[] widths) {
		try {
			PdfPTable table = new PdfPTable(colspan);
			table.setTotalWidth(width);
			table.setWidths(widths);
			table.setLockedWidth(true);// 使绝对宽度模式生效
			return table;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	private PdfPCell getCell(String text, Font font, int colspan, int rowspan, int border, int height, int align) {
		PdfPCell cell = new PdfPCell();
		text = text == null ? "" : text;
		Paragraph pragraph = new Paragraph(text, font);
		pragraph.setAlignment(align);
		cell.addElement(pragraph);
		cell.setColspan(colspan);
		cell.setRowspan(rowspan);
		cell.setUseAscender(true);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		if (border == 0)
			cell.setBorder(border);
		if (height != 0)
			cell.setMinimumHeight(height);
		return cell;
	}

	private void appendTitle(PdfPTable table, String text, Font font, int col, int row, int height, int rotate) {
		text = text == null ? "" : text;
		Paragraph pragraph = new Paragraph(text, font);
		pragraph.setAlignment(Element.ALIGN_CENTER);
		if (rotate == 90)
			pragraph.setAlignment(0);
		PdfPCell cell = new PdfPCell(pragraph);
		cell.setColspan(col);
		cell.setRowspan(row);
		cell.setUseAscender(true);
		cell.setHorizontalAlignment(1);
		cell.setVerticalAlignment(5);
		cell.setRotation(rotate);
		if (height != 0)
			cell.setMinimumHeight(height);
		table.addCell(cell);
	}

	private void appendTableTitle(PdfPTable table, String text, Font font, int height) {
		text = text == null ? "" : text;
		Paragraph paragraph = new Paragraph(text, font);
		PdfPCell cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(2);
		cell.setFixedHeight(height);
		cell.setUseAscender(true);
		cell.setBorder(0);
		table.addCell(cell);
	}

	private void appendTableValue(PdfPTable table, String text, Font font, int height, int colspan, int border[]) {
		text = text == null ? "" : text;
		Paragraph paragraph = new Paragraph(text, font);
		PdfPCell cell = new PdfPCell(paragraph);
		cell.setFixedHeight(height);
		cell.setUseAscender(true);
		cell.setColspan(colspan);
		if (border[0] == 0)
			cell.setBorderWidthTop(0);
		if (border[1] == 0)
			cell.setBorderWidthRight(0);
		if (border[2] == 0)
			cell.setBorderWidthBottom(0);
		if (border[3] == 0)
			cell.setBorderWidthLeft(0);
		table.addCell(cell);
	}

	private void TableValue(PdfPTable table, String text, Font font, int height, int colspan, int border[], int align) {
		text = text == null ? "" : text;
		Paragraph paragraph = new Paragraph(text, font);
		PdfPCell cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(align);
		cell.setFixedHeight(height);
		cell.setUseAscender(true);
		cell.setColspan(colspan);
		if (border[0] == 0)
			cell.setBorderWidthTop(0);
		if (border[1] == 0)
			cell.setBorderWidthRight(0);
		if (border[2] == 0)
			cell.setBorderWidthBottom(0);
		if (border[3] == 0)
			cell.setBorderWidthLeft(0);
		table.addCell(cell);
	}

	private void nestTable(PdfPTable table1, PdfPTable table2, int col, int row, int padding, int border, int height) {
		PdfPCell cell = new PdfPCell(table2);
		cell.setColspan(col);
		cell.setRowspan(row);
		cell.setPadding(padding);
		cell.setHorizontalAlignment(1);
		if (border == 0)
			cell.setBorder(border);
		if (height != 0)
			cell.setMinimumHeight(height);
		table1.addCell(cell);
	}

	private void tableWrap(PdfPTable table, int colspan, int height) {
		PdfPCell cell = new PdfPCell();
		cell.setColspan(colspan);
		cell.setFixedHeight(height);
		cell.setBorder(0);
		table.addCell(cell);
	}

	public void getWidths(int[] widths, int index, int... value) {
		for (int i = 0; i < value.length; i++)
			widths[index + i] = value[i];
	}

	public void appendWrap(Document document, double height) {
		try {
			int[] width = new int[] { 1 };
			PdfPTable table = getTable(1, 560f, width);
			PdfPCell cell = new PdfPCell();
			cell.setFixedHeight((float) height);
			cell.setBorder(0);
			table.addCell(cell);
			document.add(table);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/** 设置页面事件 */
	private class MyPageEvent extends PdfPageEventHelper {
		public void onEndPage(PdfWriter writer, Document document) {
			try {
				BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
				File file = ResourceUtils.getFile("classpath:static/picture/footer.png");
				Image footerIamge = Image.getInstance(file.getPath());
				footerIamge.scaleAbsolute(560, 32);
				footerIamge.setAbsolutePosition(20, 0);
				writer.getDirectContent().addImage(footerIamge);
				Font font = new Font(bfChinese, 8, 0);
				Phrase phrase = new Phrase(writer.getPageNumber() + "", font);
				ColumnText.showTextAligned(writer.getDirectContent(), 1, phrase, 300, 16, 0);
			} catch (IOException | DocumentException e) {
				e.printStackTrace();
			}
		}
	}

	private List<String> getList(String text, int size) {
		List<String> list = new ArrayList<String>();
		int count = text.length() / size;
		if (text.length() % size != 0)
			count++;
		for (int index = 0; index < count; index++) {
			int length = (index + 1) * size;
			if (length > text.length())
				length = text.length();
			if (index < count - 1 && text.charAt(length) != ' ')
				list.add(text.substring(index * size, length) + "-");
			else
				list.add(text.substring(index * size, length));
		}
		return list;
	}

	private double getValue(String text) {
		try {
			return Double.valueOf(text);
		} catch (Exception e) {
			return 0;
		}
	}
}
