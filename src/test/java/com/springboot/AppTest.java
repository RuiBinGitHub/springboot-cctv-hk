package com.springboot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;

import com.springboot.util.AppUtils;

public class AppTest {

	public static void main(String[] args) {
		// System.out.println(AppUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		double w = 350;
		double h = 270;
		double b = w / h;
		System.out.println(b > 1.333);
		
		if (b < (4d / 3)) {
			System.out.println(270 * b);
			System.out.println("xi");
		}
		if (b > (4d / 3)) {
			System.out.println(360 / b);
			System.out.println("da");
		}

	}
}
