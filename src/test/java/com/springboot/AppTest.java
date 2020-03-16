package com.springboot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.util.ResourceUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class AppTest extends PdfPageEventHelper {

	private Document document = null;

	public static void main(String[] args) {
		AppTest test = new AppTest();
		test.initPDF("man", "D:/");
		System.out.println("--");
	}

	public void initPDF(String name, String path) {
		try {
			document = new Document(PageSize.A4);
			document.setMargins(20, 20, 20, 10); // 左右上下间距
			OutputStream output = new FileOutputStream(path + name + ".pdf");
			PdfWriter writer = PdfWriter.getInstance(document, output);
			writer.setPageEvent(this);
			document.open(); // 打开文档
			/*****************************************************************/
			document.newPage();
			Font font1 = getFont(4, 0, null);
			Font font2 = getFont(4, 1, null);
			int[] border1 = { 0, 0, 0, 0 };
			int[] border2 = { 1, 1, 1, 1 };
			int[] bordera = { 1, 0, 0, 1 };
			int[] borderb = { 1, 0, 0, 0 };
			int[] borderc = { 1, 1, 0, 0 };
			int[] borderd = { 0, 0, 0, 1 };
			int[] bordere = { 0, 0, 0, 0 };
			int[] borderf = { 0, 1, 0, 0 };
			int[] borderi = { 0, 0, 1, 1 };
			int[] borderj = { 0, 0, 1, 0 };
			int[] borderk = { 0, 1, 1, 0 };

			int[] width1 = { 1 };
			PdfPTable ptable1 = getTable(1, 560f, width1);

			Font titleFont = getFont(10, 1, null);
			titleFont.setStyle("italic");
			int[] widths = { 1000, 460 };
			PdfPTable title = getTable(widths.length, 540f, widths);
			String ImgPath = "classpath:static/img/";
			File file = ResourceUtils.getFile(ImgPath + "info.png");
			Image image = Image.getInstance(file.getPath());
			image.scaleAbsolute(220, 44); // 设置图片大小
			PdfPCell ImgPCell = new PdfPCell(image);
			ImgPCell.setBorder(0);
			title.addCell(ImgPCell);
			title.addCell(writeValue("IDMS Manhole Record Card", titleFont, 1, 1, 0, 1, border2));
			title.addCell(writeValue("", null, 1, widths.length, 10, 0, border1)); // 添加空行
			// title完成
			nestTable(ptable1, title, new int[] { 10, 10, 0, 10 }, new int[] { 1, 1, 0, 1 }, 0);

			int height1 = 18;
			int[] widths1 = { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 120, 50, 120, 120, 50 };
			PdfPTable table1 = getTable(widths1.length, 540f, widths1);
			table1.addCell(writeValue("", null, 1, 9, height1, 1, border1));
			table1.addCell(writeValue("", null, 1, 3, 0, 1, border1));
			table1.addCell(writeValue("(1) Node ref.", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("S51", font1, 1, 2, 0, 1, border2));

			table1.addCell(writeValue("(6) Project no.", font1, 1, 1, height1, 0, border2));
			table1.addCell(writeValue("Y18-P-040-999", font1, 1, 3, 0, 1, border2));
			table1.addCell(writeValue("(8) IDMS Manhole ID.", font1, 1, 1, 20, 0, border2));
			table1.addCell(writeValue("", font1, 1, 4, 0, 1, border2));
			table1.addCell(writeValue("", font1, 1, 3, 0, 1, border1));
			table1.addCell(writeValue("(2) Grid ref.", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("X\nY", font1, 1, 2, 0, 1, border2));

			table1.addCell(writeValue("(7) WO no.", font1, 1, 1, height1, 0, border2));
			table1.addCell(writeValue("", font1, 1, 3, 0, 1, border2));
			table1.addCell(writeValue("(9) DSD ref.", font1, 1, 1, 20, 0, border2));
			table1.addCell(writeValue("", font1, 1, 4, 0, 1, border2));
			table1.addCell(writeValue("", font1, 1, 3, 0, 1, border1));
			table1.addCell(writeValue("(3) Drainage area code", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("", font1, 1, 2, 0, 1, border2));

			table1.addCell(writeValue("(10) Location", font1, 1, 1, height1, 0, border2));
			table1.addCell(writeValue("", font1, 1, 8, 0, 1, border2));
			table1.addCell(writeValue("", font1, 1, 3, 0, 1, border1));
			table1.addCell(writeValue("(4) Survey by", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("", font1, 1, 2, 0, 1, border2));

			table1.addCell(writeValue("", font1, 1, 1, height1, 1, border1));
			table1.addCell(writeValue("(11) Year laid", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("Unknown", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("(12) Status*", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("PU", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("(13) Function", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("S", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("(14) Node type*", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("M", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("", font1, 1, 3, 0, 1, border1));
			table1.addCell(writeValue("(5) Survey date (DD/MM/YYYY)", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("18/06/2019", font1, 1, 2, 0, 1, border2));

			table1.addCell(writeValue("COVER", font2, 1, 1, height1, 1, border1));
			table1.addCell(writeValue("(15) Shape*", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("S", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("(16) Hinged", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("(17) Lock", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("(18) Duty*", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("M", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("(19) Cover size", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("570", font1, 1, 1, 0, 1, bordera));
			table1.addCell(writeValue("X", font1, 1, 1, 0, 1, borderb));
			table1.addCell(writeValue("570", font1, 1, 1, 0, 1, borderc));
			table1.addCell(writeValue("(29) Toxic atmosphere", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));

			table1.addCell(writeValue("SHAFT", font2, 1, 1, height1, 1, border1));
			table1.addCell(writeValue("(20) Side entry", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("(21) Regular course", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("0", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("(22) Depth (mm)", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("250", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("", font1, 1, 2, 0, 0, border1));
			table1.addCell(writeValue("(23) Shaft size", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("570", font1, 1, 1, 0, 1, borderd));
			table1.addCell(writeValue("X", font1, 1, 1, 0, 1, bordere));
			table1.addCell(writeValue("570", font1, 1, 1, 0, 1, borderf));
			table1.addCell(writeValue("(30) Evidence of vermi", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));

			table1.addCell(writeValue("CHAMBER", font2, 1, 1, height1, 1, border1));
			table1.addCell(writeValue("(24) Soffit*", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("S", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("(25) Steps", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("0", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("(26) Ladders", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("0", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("(27) LNDGS", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("0", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("(28) Chamber size", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("570", font1, 1, 1, 0, 1, borderi));
			table1.addCell(writeValue("X", font1, 1, 1, 0, 1, borderj));
			table1.addCell(writeValue("570", font1, 1, 1, 0, 1, borderk));
			table1.addCell(writeValue("(31) Construct code *", font1, 1, 1, 0, 0, borderf));
			table1.addCell(writeValue("I", font1, 1, 1, 0, 1, border2));

			table1.addCell(writeValue("", font1, 1, widths1.length, 4, 0, border1)); // 添加空行

			table1.addCell(writeValue("", font1, 1, 1, height1, 1, border1));
			table1.addCell(writeValue("(32) Depth of flow (mm)", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("S", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("(33) Depth of silt (mm)", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("0", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("(34) Height surch (mm)", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("0", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("(61) MH depth (m)", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("0", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("(35) Cover level (mPD)", font1, 1, 1, 0, 0, border1));
			table1.addCell(writeValue("", font1, 1, 1, 0, 1, border2));
			table1.addCell(writeValue("", font1, 1, 4, 0, 1, border1));
			// 表格1完成
			table1.addCell(writeValue("", font1, 1, widths1.length, 4, 0, border1)); // 添加空行
			nestTable(ptable1, table1, new int[] { 0, 10, 0, 10 }, new int[] { 0, 1, 0, 1 }, 0);

			int height2 = 14;
			int[] widths2 = { 50, 50, 100, 100, 100, 60, 100, 100, 100, 100, 100, 100, 100, 145, 145 };
			PdfPTable table2 = getTable(widths2.length, 540f, widths2);
			table2.addCell(writeValue("", font1, 2, 2, 12, 0, border1));
			table2.addCell(writeValue("(36) Upstream.Ref", font1, 2, 1, 0, 1, border2));
			table2.addCell(writeValue("(37) Pipe shape*", font1, 2, 1, 0, 1, border2));
			table2.addCell(writeValue("(38) Pipe size\n(mm)", font1, 2, 3, 0, 1, border2));
			table2.addCell(writeValue("(39) Backdrop\n(mm)", font1, 2, 1, 0, 1, border2));
			table2.addCell(writeValue("(40) Pipe material*", font1, 2, 1, 0, 1, border2));
			table2.addCell(writeValue("(41) Lining", font1, 2, 1, 0, 1, border2));
			table2.addCell(writeValue("(42) Pipe depth\n(m)", font1, 2, 1, 0, 1, border2));
			table2.addCell(writeValue("(43) Invert level\n(mPD)", font1, 2, 1, 0, 1, border2));
			table2.addCell(writeValue("(71) Photo no.", font1, 2, 1, 0, 1, border2));
			table2.addCell(iwriteValue("(63) For office use", font1, 1, 2, 0, 1, new int[] { 1, 1, 0, 1 }));
			table2.addCell(iwriteValue("Wall No", font1, 1, 1, 0, 1, new int[] { 0, 0, 1, 1 }));
			table2.addCell(iwriteValue("Location (m)", font1, 1, 1, 0, 1, new int[] { 0, 1, 1, 0 }));

			PdfPCell cell1 = writeValue("INCOMING PIPES", font2, 8, 1, 0, 1, border1);
			cell1.setRotation(90);
			table2.addCell(cell1);
			for (int i = 0; i < 8; i++) {
				table2.addCell(writeValue((char) (65 + i) + "", font2, 1, 1, height2, 1, border1));
				table2.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
				table2.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
				table2.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
				table2.addCell(writeValue("X", font1, 1, 1, height2, 1, border2));
				table2.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
				table2.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
				table2.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
				table2.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
				table2.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
				table2.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
				table2.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
				table2.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
				table2.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			}
			// 表格2完成
			table2.addCell(writeValue("", font1, 1, widths2.length, 4, 0, border1)); // 添加空行
			nestTable(ptable1, table2, new int[] { 0, 10, 0, 10 }, new int[] { 0, 1, 0, 1 }, 0);

			int height3 = 14;
			int[] widths3 = { 100, 100, 100, 60, 100, 100, 890 };
			PdfPTable table3 = getTable(widths3.length, 540f, widths3);
			table3.addCell(writeValue("", font1, 1, 1, height3, 1, border1));
			table3.addCell(writeValue("(44) CCTV COND", font1, 1, 1, 0, 1, border2));
			table3.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));
			table3.addCell(writeValue("", font1, 1, 1, 0, 1, border1));
			table3.addCell(writeValue("(45) CRITY", font1, 1, 1, 0, 1, border2));
			table3.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));
			table3.addCell(writeValue("", font1, 1, 1, 0, 1, border1));
			// 表格3完成
			table3.addCell(writeValue("", font1, 1, widths3.length, 4, 0, border1)); // 添加空行
			nestTable(ptable1, table3, new int[] { 0, 10, 0, 10 }, new int[] { 0, 1, 0, 1 }, 0);

			int height4 = 14;
			int[] widths4 = { 50, 50, 100, 100, 100, 60, 100, 100, 100, 100, 100, 100, 100, 145, 145 };
			PdfPTable table4 = getTable(widths4.length, 540f, widths4);
			table4.addCell(writeValue("", font1, 2, 2, 12, 0, border1));
			table4.addCell(writeValue("(36) Upstream.Ref", font1, 2, 1, 0, 1, border2));
			table4.addCell(writeValue("(37) Pipe shape*", font1, 2, 1, 0, 1, border2));
			table4.addCell(writeValue("(38) Pipe size\n(mm)", font1, 2, 3, 0, 1, border2));
			table4.addCell(writeValue("(39) Backdrop\n(mm)", font1, 2, 1, 0, 1, border2));
			table4.addCell(writeValue("(40) Pipe material*", font1, 2, 1, 0, 1, border2));
			table4.addCell(writeValue("(41) Lining", font1, 2, 1, 0, 1, border2));
			table4.addCell(writeValue("(42) Pipe depth\n(m)", font1, 2, 1, 0, 1, border2));
			table4.addCell(writeValue("(43) Invert level\n(mPD)", font1, 2, 1, 0, 1, border2));
			table4.addCell(writeValue("(71) Photo no.", font1, 2, 1, 0, 1, border2));
			table4.addCell(iwriteValue("(63) For office use", font1, 1, 2, 0, 1, new int[] { 1, 1, 0, 1 }));
			table4.addCell(iwriteValue("Wall No", font1, 1, 1, 0, 1, new int[] { 0, 0, 1, 1 }));
			table4.addCell(iwriteValue("Location (m)", font1, 1, 1, 0, 1, new int[] { 0, 1, 1, 0 }));

			PdfPCell cell2 = writeValue("OUTGOING\nPIPES", font2, 2, 1, 0, 1, border1);
			cell2.setRotation(90);
			table4.addCell(cell2);
			table4.addCell(writeValue("X", font2, 1, 1, height4, 1, border1));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("X", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			// 第二行
			table4.addCell(writeValue("Y", font2, 1, 1, height4, 1, border1));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("X", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			table4.addCell(writeValue("", font1, 1, 1, height2, 1, border2));
			// 表格4完成
			table4.addCell(writeValue("", font1, 1, widths4.length, 4, 0, border1)); // 添加空行
			nestTable(ptable1, table4, new int[] { 0, 10, 0, 10 }, new int[] { 0, 1, 0, 1 }, 0);

			int height5 = 14;
			int[] widths5 = { 100, 100, 100, 30, 100, 100, 30, 100, 100, 30, 100, 100, 30, 100, 100, 30, 100, 100 };
			PdfPTable table5 = getTable(widths5.length, 540f, widths5);
			table5.addCell(writeValue("Condition", font2, 1, 1, height5, 0, border1));
			table5.addCell(writeValue("(46) Cover", font1, 1, 1, 0, 1, border2));
			table5.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));
			table5.addCell(writeValue("", font1, 1, 1, 0, 1, border1));
			table5.addCell(writeValue("(47) Iron / Ladder", font1, 1, 1, 0, 1, border2));
			table5.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));
			table5.addCell(writeValue("", font1, 1, 1, 0, 1, border1));
			table5.addCell(writeValue("(48) Shaft", font1, 1, 1, 0, 1, border2));
			table5.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));
			table5.addCell(writeValue("", font1, 1, 1, 0, 1, border1));
			table5.addCell(writeValue("(49) Chamber", font1, 1, 1, 0, 1, border2));
			table5.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));
			table5.addCell(writeValue("", font1, 1, 1, 0, 1, border1));
			table5.addCell(writeValue("(50) Benching", font1, 1, 1, 0, 1, border2));
			table5.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));
			table5.addCell(writeValue("", font1, 1, 1, 0, 1, border1));
			table5.addCell(writeValue("(51) Others", font1, 1, 1, 0, 1, border2));
			table5.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));

			table5.addCell(writeValue("(52) Photo no.", font2, 1, 1, height5, 0, border1));
			table5.addCell(writeValue("N", font1, 1, 2, 0, 1, border2));
			table5.addCell(writeValue("", font1, 1, 1, 0, 1, border1));
			table5.addCell(writeValue("N", font1, 1, 2, 0, 1, border2));
			table5.addCell(writeValue("", font1, 1, 1, 0, 1, border1));
			table5.addCell(writeValue("N", font1, 1, 2, 0, 1, border2));
			table5.addCell(writeValue("", font1, 1, 1, 0, 1, border1));
			table5.addCell(writeValue("N", font1, 1, 2, 0, 1, border2));
			table5.addCell(writeValue("", font1, 1, 1, 0, 1, border1));
			table5.addCell(writeValue("N", font1, 1, 2, 0, 1, border2));
			table5.addCell(writeValue("", font1, 1, 1, 0, 1, border1));
			table5.addCell(writeValue("N", font1, 1, 2, 0, 1, border2));
			// 表格5完成
			table5.addCell(writeValue("", font1, 1, widths5.length, 4, 0, border1)); // 添加空行
			nestTable(ptable1, table5, new int[] { 0, 10, 0, 10 }, new int[] { 0, 1, 0, 1 }, 0);

			int height6 = 14;
			int[] widths6 = { 100, 60, 140, 230, 30, 100, 100, 100, 100, 100, 100, 290 };
			PdfPTable table6 = getTable(widths6.length, 540f, widths6);
			table6.addCell(writeValue("(53) UTR", font1, 1, 1, height6, 0, border2));
			table6.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));
			table6.addCell(writeValue("(59) Location photo no.", font1, 2, 1, 0, 0, border2));
			table6.addCell(writeValue("S51-P01", font1, 2, 1, 0, 1, border2));
			table6.addCell(writeValue("", font1, 2, 1, 0, 0, border1));
			table6.addCell(writeValue("(63) Remark", font1, 2, 1, 0, 0, border2));
			table6.addCell(writeValue("", font1, 2, 6, 0, 0, border2));
			table6.addCell(writeValue("(54) UTL", font1, 1, 1, height6, 0, border2));
			table6.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));

			table6.addCell(writeValue("(55) UTGA", font1, 1, 1, height6, 0, border2));
			table6.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));
			table6.addCell(writeValue("(60) Internal photo no.", font1, 2, 1, 0, 0, border2));
			table6.addCell(writeValue("S51-P02", font1, 2, 1, 0, 1, border2));
			table6.addCell(writeValue("", font1, 2, 1, 0, 0, border1));
			table6.addCell(writeValue("(64) Record plan difference", font1, 2, 1, 0, 0, border2));
			table6.addCell(writeValue("N", font1, 2, 1, 0, 1, border2));
			table6.addCell(writeValue("(Y of attention required)", font1, 2, 5, 0, 0, border2));
			table6.addCell(writeValue("(56) UTS", font1, 1, 1, height6, 0, border2));
			table6.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));

			table6.addCell(writeValue("(57) JETTING", font1, 1, 1, height6, 0, border2));
			table6.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));
			table6.addCell(writeValue("Slope No.", font1, 2, 1, 0, 0, border2));
			table6.addCell(writeValue("S51-P02", font1, 2, 1, 0, 1, border2));
			table6.addCell(writeValue("", font1, 2, 1, 0, 0, border1));
			table6.addCell(writeValue("(65) Cover type", font1, 2, 1, 0, 0, border2));
			table6.addCell(writeValue("Standard ", font1, 2, 1, 0, 1, border2));
			table6.addCell(writeValue("Standard - Large", font1, 2, 1, 0, 1, border2));
			table6.addCell(writeValue("Multiple Cover", font1, 2, 1, 0, 1, border2));
			table6.addCell(writeValue("Large ", font1, 2, 1, 0, 1, border2));
			table6.addCell(writeValue("With decoration cover", font1, 2, 1, 0, 1, border2));
			table6.addCell(writeValue("Others: ()", font1, 2, 1, 0, 0, border2));
			table6.addCell(writeValue("(56) UTS", font1, 1, 1, height6, 0, border2));
			table6.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));
			// 表格6完成
			table6.addCell(writeValue("", font1, 1, widths6.length, 4, 0, border1)); // 添加空行
			nestTable(ptable1, table6, new int[] { 0, 10, 0, 10 }, new int[] { 0, 1, 0, 1 }, 0);

			int height7 = 12;
			int[] widths7 = { 100, 100 };
			PdfPTable table7 = getTable(widths7.length, 540f, widths7);
			table7.addCell(writeValue("(66) Location Sketch", font1, 1, 1, height7, 0, border2));
			table7.addCell(writeValue("(67) Plan of MH", font1, 1, 1, 0, 0, border2));
			table7.addCell(writeValue("", font1, 1, 1, 170, 0, border2));
			table7.addCell(writeValue("", font1, 1, 1, 170, 0, border2));
			// 表格7完成
			table7.addCell(writeValue("", font1, 1, widths7.length, 5, 0, border1)); // 添加空行
			nestTable(ptable1, table7, new int[] { 0, 10, 0, 10 }, new int[] { 0, 1, 0, 1 }, 0);

			int height8 = 10;
			int[] widths8 = { 100, 100, 1250 };
			String remark = "Remarks:  * - Code description refers to Appendix - Quick Reference for Code in IDMS Manhole Card";
			PdfPTable table8 = getTable(widths8.length, 540f, widths8);
			table8.addCell(writeValue("(68) With Risk Assessment", font1, 1, 1, height8, 0, border2));
			table8.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));
			table8.addCell(writeValue("", font1, 1, 1, 0, 0, border1));
			table8.addCell(writeValue("(69) With Permit to Work", font1, 1, 1, height8, 0, border2));
			table8.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));
			table8.addCell(writeValue("", font1, 1, 1, 0, 0, border1));
			table8.addCell(writeValue("(70) With Traffic Permit", font1, 1, 1, height8, 0, border2));
			table8.addCell(writeValue("N", font1, 1, 1, 0, 1, border2));
			table8.addCell(writeValue("", font1, 1, 1, 0, 0, border1));
			table8.addCell(writeValue(remark, getFont(8, 1, null), 1, 3, 20, 1, border1));
			nestTable(ptable1, table8, new int[] { 0, 10, 10, 10 }, new int[] { 0, 1, 1, 1 }, 0);

			// 页面1完成
			document.add(ptable1);
			/************************************************************/
			int[] iboder1 = new int[] { 0, 0, 1, 0 };
			int[] ibodera = new int[] { 1, 1, 0, 1 };
			int[] iboderb = new int[] { 0, 1, 1, 1 };
			document.setMargins(20, 20, 35, 10); // 左右上下间距
			document.newPage();
			Font ifont1 = getFont(11, 0, null);
			Font ifont2 = getFont(10, 0, null);
			int[] width2 = { 4, 1 };
			PdfPTable ptable2 = getTable(width2.length, 500f, width2);

			int[] iwidth1 = { 340, 240 };
			PdfPTable itable1 = getTable(2, 490f, iwidth1);

			file = ResourceUtils.getFile(ImgPath + "005.png");
			image = Image.getInstance(file.getPath());
			image.scaleAbsolute(256, 40); // 设置图片大小
			ImgPCell = new PdfPCell(image);
			ImgPCell.setRowspan(2);
			ImgPCell.setBorder(0);
			itable1.addCell(ImgPCell);
			itable1.addCell(writeValue("Manhole Survey Photographs", titleFont, 1, 1, 25, 1, border2));
			itable1.addCell(writeValue("", titleFont, 1, 1, 20, 0, border1));
			// 表格1完成
			nestTable(ptable2, itable1, new int[] { 5, 5, 25, 5 }, new int[] { 1, 1, 0, 1 }, 2);

			int iheight1 = 15;
			int[] iwidth2 = { 1, 1, 1, 1, 1 };
			PdfPTable itable2 = getTable(iwidth2.length, 490f, iwidth2);
			itable2.addCell(writeValue("Project No.: ", ifont1, 1, 1, iheight1, 0, border1));
			itable2.addCell(writeValue("Y18-P-040-999", ifont1, 1, 1, 0, 1, iboder1));
			itable2.addCell(writeValue("", null, 1, 1, 0, 0, border1));
			itable2.addCell(writeValue(" Manhole Referenc: ", ifont1, 1, 1, 0, 2, border1));
			itable2.addCell(writeValue("S1", ifont1, 1, 1, 0, 1, iboder1));
			itable2.addCell(writeValue("WO  No.: ", ifont1, 1, 1, iheight1, 0, border1));
			itable2.addCell(writeValue("W12", ifont1, 1, 1, 0, 1, iboder1));
			itable2.addCell(writeValue("", null, 1, 3, 0, 0, border1));
			// 表格2完成
			nestTable(ptable2, itable2, new int[] { 5, 5, 15, 5 }, new int[] { 0, 1, 0, 1 }, 2);

			int iheight2 = 15, iheight3 = 60;
			ptable2.addCell(writeValue("", null, 8, 1, 0, 0, border2)); // 图片位置
			ptable2.addCell(writeValue("Photograph No.:", ifont2, 1, 1, iheight2, 0, ibodera));
			ptable2.addCell(writeValue("S51 - P01", ifont2, 1, 1, iheight3, 1, iboderb));
			ptable2.addCell(writeValue("Location :", ifont2, 1, 1, iheight2, 0, ibodera));
			ptable2.addCell(writeValue("Hong Keung Street Public Toilet", ifont2, 1, 1, iheight3, 1, iboderb));
			ptable2.addCell(writeValue("Description:", ifont2, 1, 1, iheight2, 0, ibodera));
			ptable2.addCell(writeValue("Manhole Location", ifont2, 1, 1, iheight3, 1, iboderb));
			ptable2.addCell(writeValue("Remark:", ifont2, 1, 1, iheight2, 0, ibodera));
			ptable2.addCell(writeValue("", ifont2, 1, 1, iheight3, 1, iboderb));
			ptable2.addCell(writeValue("", null, 1, 2, 28, 1, border2)); // 插入空行
			ptable2.addCell(writeValue("", null, 8, 1, 0, 0, border2)); // 图片位置
			ptable2.addCell(writeValue("Photograph No.:", ifont2, 1, 1, iheight2, 0, ibodera));
			ptable2.addCell(writeValue("S51 - P01", ifont2, 1, 1, iheight3, 1, iboderb));
			ptable2.addCell(writeValue("Location :", ifont2, 1, 1, iheight2, 0, ibodera));
			ptable2.addCell(writeValue("Hong Keung Street Public Toilet", ifont2, 1, 1, iheight3, 1, iboderb));
			ptable2.addCell(writeValue("Description:", ifont2, 1, 1, iheight2, 0, ibodera));
			ptable2.addCell(writeValue("Manhole Location", ifont2, 1, 1, iheight3, 1, iboderb));
			ptable2.addCell(writeValue("Remark:", ifont2, 1, 1, iheight2, 0, ibodera));
			ptable2.addCell(writeValue("", ifont2, 1, 1, iheight3, 1, iboderb));

			document.add(ptable2);
			System.out.println("完成");
			document.close();// 关闭文档
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}

	}

	/** 创建字体 */
	private Font getFont(int size, int bold, BaseColor color) {
		try {
			BaseFont baseFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);
			Font font = new Font(baseFont, size, bold);
			if (color != null)
				font.setColor(color);
			return font;
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
			return null;
		}
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

	// 写值
	private PdfPCell writeValue(String text, Font font, int row, int col, int height, int algin, int border[]) {
		text = text == null ? "" : text;
		Paragraph paragraph = new Paragraph(text, font);
		PdfPCell cell = new PdfPCell(paragraph);
		cell.setUseAscender(true);
		cell.setHorizontalAlignment(algin);
		cell.setVerticalAlignment(5);
		cell.setRowspan(row);
		cell.setColspan(col);
		if (height != 0)
			cell.setFixedHeight(height);
		if (border[0] == 0)
			cell.setBorderWidthTop(0);
		if (border[1] == 0)
			cell.setBorderWidthRight(0);
		if (border[2] == 0)
			cell.setBorderWidthBottom(0);
		if (border[3] == 0)
			cell.setBorderWidthLeft(0);
		return cell;
	}

	private PdfPCell iwriteValue(String text, Font font, int row, int col, int height, int algin, int border[]) {
		text = text == null ? "" : text;
		BaseColor backgroundColor = new BaseColor(216, 216, 216);
		Paragraph paragraph = new Paragraph(text, font);
		PdfPCell cell = new PdfPCell(paragraph);
		cell.setBackgroundColor(backgroundColor);
		cell.setUseAscender(true);
		cell.setHorizontalAlignment(algin);
		cell.setVerticalAlignment(5);
		cell.setRowspan(row);
		cell.setColspan(col);
		if (height != 0)
			cell.setFixedHeight(height);
		if (border[0] == 0)
			cell.setBorderWidthTop(0);
		if (border[1] == 0)
			cell.setBorderWidthRight(0);
		if (border[2] == 0)
			cell.setBorderWidthBottom(0);
		if (border[3] == 0)
			cell.setBorderWidthLeft(0);
		return cell;
	}

	public void writeImage(Document document, String name) {
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

	private void nestTable(PdfPTable table1, PdfPTable table2, int[] padding, int[] border, int col) {
		PdfPCell cell = new PdfPCell(table2);
		cell.setHorizontalAlignment(1);
		// 设置内边距
		cell.setPaddingTop(padding[0]);
		cell.setPaddingRight(padding[1]);
		cell.setPaddingBottom(padding[2]);
		cell.setPaddingLeft(padding[3]);
		// 设置边框
		cell.setBorderWidthTop(border[0]);
		cell.setBorderWidthRight(border[1]);
		cell.setBorderWidthBottom(border[2]);
		cell.setBorderWidthLeft(border[3]);
		// if (border[0] == 0)
		// cell.setBorderWidthTop(border[0]);
		// else
		// cell.setBorderWidthTop(1.2f);
		// if (border[1] == 0)
		// cell.setBorderWidthRight(border[1]);
		// else
		// cell.setBorderWidthRight(1.2f);
		// if (border[2] == 0)
		// cell.setBorderWidthBottom(border[2]);
		// else
		// cell.setBorderWidthBottom(1.2f);
		// if (border[3] == 0)
		// cell.setBorderWidthLeft(border[3]);
		// else
		// cell.setBorderWidthLeft(1.2f);
		if (col != 0)
			cell.setColspan(col);
		table1.addCell(cell);
	}

	public void onEndPage(PdfWriter writer, Document document) {
		try {
			BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
			Font font = new Font(bfChinese, 8, 0);
			Phrase phrase = new Phrase(writer.getPageNumber() + "", font);
			ColumnText.showTextAligned(writer.getDirectContent(), 1, phrase, 300, 12, 0);
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
	}
}
