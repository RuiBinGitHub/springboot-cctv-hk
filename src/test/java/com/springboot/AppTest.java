package com.springboot;

import java.text.DecimalFormat;
import java.text.Format;

import com.springboot.util.AppUtils;

public class AppTest {

	public static void main(String[] args) {
		Format foramt1 = new DecimalFormat("#0000");
		System.out.println(foramt1.format(1));
	}
}
