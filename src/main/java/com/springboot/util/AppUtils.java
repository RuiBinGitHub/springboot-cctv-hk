package com.springboot.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Decoder;

public class AppUtils {

	/** 获取参数列表 */
	public static Map<String, Object> getMap(Object... values) {
		Map<String, Object> map = new HashMap<>();
		for (int i = 0; i < values.length; i += 2)
			map.put((String) values[i], values[i + 1]);
		return map;
	}

	/** 以指定格式获取当前时间格式字符串 */
	public static String getDate(String format) {
		if (format == null)
			format = "yyyy-MM-dd";
		Date date = new Date();
		SimpleDateFormat simple = new SimpleDateFormat(format);
		return simple.format(date);
	}

	/** 创建公司序列码 */
	public static String findCode() {
		String code = "", list = "1,2,3,4,5,6,7,8,9,0";
		for (char c = 'A'; c <= 'Z'; c++)
			list += String.valueOf("," + c);
		String codeArray[] = list.split(",");
		for (int i = 0; i < 20; i++) {
			code += codeArray[(int) (Math.random() * 35)];
			if (i == 4 || i == 9 || i == 14)
				code += "-";
		}
		return code;
	}

	/** 创建图片序列码 */
	public static String UUIDCode() {
		UUID uuid = UUID.randomUUID();
		String code = uuid.toString();
		return code.toUpperCase();
	}

	/** 获取session */
	public static HttpSession getHttpSession(){
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
		HttpSession session = request.getSession(true);
		return session;
	}
	
	/** 添加数据至session */
	public static void pushMap(String key, Object value) {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
		HttpSession session = request.getSession(true);
		session.setAttribute(key, value);
	}

	/** 从session获取数据 */
	public static Object findMap(String key) {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
		HttpSession session = request.getSession(true);
		return session.getAttribute(key);
	}

	/** 冲session移除数据 */
	public static void removeSession(String key) {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
		HttpSession session = request.getSession(true);
		session.removeAttribute(key);
	}

	/** 保存图片 */
	public static void saveImage(String data, String path, String name) {
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] bytes = decoder.decodeBuffer(data);
			for (int i = 0; i < bytes.length; i++)
				bytes[i] = bytes[i] < 0 ? bytes[i] += 256 : bytes[i];
			OutputStream output = new FileOutputStream(path + name + ".png");
			output.write(bytes);
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static XSSFWorkbook getWorkbook(MultipartFile file) {
		try {
			InputStream stream = file.getInputStream();
			XSSFWorkbook workbook = new XSSFWorkbook(stream);
			return workbook;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** 获取文本值 */
	public static String getString(XSSFCell cell) {
		try {
			return cell.getStringCellValue();
		} catch (Exception e) {
			return "";
		}
	}

	/** 获取小数值 */
	public static double getNumeric(XSSFCell cell) {
		try {
			return cell.getNumericCellValue();
		} catch (Exception e) {
			return 0;
		}
	}

	public static String getRawValue(XSSFCell cell) {
		try {
			return cell.getRawValue();
		} catch (Exception e) {
			return "--";
		}
	}
	
	public static double getValue(String text) {
		try {
			return Double.valueOf(text);
		} catch (Exception e) {
			return 0;
		}
	}

	public static String getDouble(XSSFCell cell) {
		try {
			return cell.getRawValue();
		} catch (Exception e) {
			return "0.0";
		}
	}
	
}
