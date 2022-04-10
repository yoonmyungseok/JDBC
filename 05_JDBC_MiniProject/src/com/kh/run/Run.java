package com.kh.run;

import com.kh.view.ProductView;

public class Run {

	public static void main(String[] args) {
		
		// properties, xml 파일 형태로 내보내기
		/*
		Properties prop = new Properties();

		try {
			prop.store(new FileOutputStream("resources/driver.properties"), "driver.properties");
			
			prop.storeToXML(new FileOutputStream("resources/query.xml"), "query.xml");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		new ProductView().mainMenu();
	}
}
