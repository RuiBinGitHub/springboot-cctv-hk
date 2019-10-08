package com.springboot;

import java.util.ArrayList;
import java.util.List;

public class AppTest {

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("0001");
		list.add("0002");
		list.add("0003");
		list.add("0004");
		list.add("0005");
		// System.out.println(list);
		list.remove(0);
		System.out.println(list);
		list.remove(0);
		System.out.println(list);
		list.remove(0);
		System.out.println(list);
		
	}
}
