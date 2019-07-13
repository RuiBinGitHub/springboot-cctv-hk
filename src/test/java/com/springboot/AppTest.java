package com.springboot;

import java.text.DecimalFormat;

public class AppTest {

	public static void main(String[] args) {
		DecimalFormat foramt1 = new DecimalFormat("#0.00");
		System.out.println(foramt1.format(15.666));
	}
}
