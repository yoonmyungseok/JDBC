package com.kh.run;

import com.kh.view.MemberView;

public class Run {

	public static void main(String[] args) {
		//Properties 복습
		//Properties (Map 계열의 컬렉션: Key+value 세트로 담는 특징)
		//Properties는 주로 외부 설정파일을 읽어오는 또는 파일 형태로 값을 출력하고자 하는 목적으로 사용한다
		//=>파일 입출력 기능을 제공하기 때문에 Key랑 Value 모두 문자열 형태로 작성해야 한다
		
		//properties, xml 파일 형태로 내보내기
		/*Properties prop=new Properties();
		
		//Map 계열에 값을 추가하고자 할 경우 put() 메소드 사용
		//Properties의 경우 문자열 형태로 키 밸류를 넣을 수 있게끔 제공하는 setProperty("key","value") 메소드
		prop.setProperty("List", "ArrayList");
		prop.setProperty("Set", "HashSet");
		prop.setProperty("Map", "Properties");
		
		//파일 형식으로 내보낼 수 있는 메소드
		//prop.store(출력스트림객체,주석내용), prop.storeToXML(출력스트림객체,주석내용)
		try {
			prop.store(new FileOutputStream("resources/test.properties"), "test.properties");
			
			prop.storeToXML(new FileOutputStream("resources/test.xml"), "test.xml");
			//출력스트림: 프로그램 입장에서 데이터를 내보낼 수 있는 통로
			//			객체 생성 시 파일명을 없는 파일로 작성했을 경우 문제없이 생성됨
			//			Root 디렉토리를 지정한 경로가 아닐 경우 현재 내가 작업중인 프로젝트 폴더 내에 생성
			
			//resources 폴더
			//"자원"이라는 뜻, 주로 프로젝트 내의 외부 파일을 저장하는 역할
			//이미지, 스타일시트, 코드파일, 설정파일, ...
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		//properties 형식의 파일을 읽어들이기
		/*
		Properties prop=new Properties();
		
		//prop.load(입력스트림객체)
		try {
			prop.load(new FileInputStream("resources/driver.properties"));
			
			//입력스트림: 외부 매체로부터 데이터를 읽어들이는 통로
			//			없는 파일을 작성하면 무조건 오류
			//			Root 디렉토리를 제시 안했을 경우 현재 작업중인 자바 프로젝트 폴더로 시작점이 잡힘
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//출력
		//getProperty("key"): 해당 key값에 대한 value값을 리턴해주는 메소드
		//System.out.println(prop.getProperty("List"));
		//System.out.println(prop.getProperty("Set"));
		//System.out.println(prop.getProperty("Map"));
		//System.out.println(prop.getProperty("Properties"));
		//존재하지 않는 키값을 넘길 경우 null을 반환
		
		System.out.println(prop.getProperty("driver"));
		System.out.println(prop.getProperty("url"));
		System.out.println(prop.getProperty("username"));
		System.out.println(prop.getProperty("password"));
		*/
		
		//xml 형식의 파일을 읽어들이기
		/*
		Properties prop=new Properties();
		
		//prop.lodaFromXML(입력스트림객체)
		try {
			prop.loadFromXML(new FileInputStream("resources/query.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//출력
		//System.out.println(prop.getProperty("List"));
		//System.out.println(prop.getProperty("Set"));
		//System.out.println(prop.getProperty("Map"));
		
		System.out.println(prop.getProperty("select"));
		System.out.println(prop.getProperty("insert"));
		System.out.println(prop.getProperty("update"));
		*/
		
		//메인화면 띄우기
		new MemberView().mainMenu();
	}

}
