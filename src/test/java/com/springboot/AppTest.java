package com.springboot;

import java.text.DecimalFormat;

public class AppTest {

	public static void main(String[] args) {
		DecimalFormat format = new DecimalFormat("#000");
		System.out.println(format.format("1"));
	}
}
