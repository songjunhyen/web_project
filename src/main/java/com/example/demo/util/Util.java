package com.example.demo.util;

public class Util {
	public static boolean isEmpty(Object object) {
		if(object.equals("")||object.equals(null)) {
			return true;
		}else {
			return false;
		}		
	}
}
