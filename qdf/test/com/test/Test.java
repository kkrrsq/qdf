package com.test;

public class Test {

	
	public static void main(String[] args) throws Exception{

		try {
			throw new RuntimeException("error1");
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				int i = 1/0;
			} catch (Exception e2) {
				System.out.println(e2.toString());
			}
		}
	}
}
