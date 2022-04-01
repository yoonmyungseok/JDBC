package com.kh.model.vo;

import java.sql.Date;

/*
 * VO(Value Object)
 * DB 테이블의 한 행에 대한 데이터를 기록할 수 있는 저장용 객체(=DTO: Data Transfer Object)
 * 
 * VO조건
 * 1) 반드시 캡슐화 적용
 * 2) 기본생성자 및 매개변수 생성자를 작성할 것(기본 생성자 만큼은 반드시 필수)
 * 3) 모든 필드에 대해 getter / setter 메소드를 만들 것
 */

public class Member {
	//필드부: 필드들이 모여있는 곳
	//필드는 DB 컬럼 정보와 유사하게 만들 것!
	private int userNo;
	private String userId;
	private String userPwd;
	private String userName;
	private String gender;
	private int age;
	private String email;
	private String phone;
	private String address;
	private String hobby;
	private Date enrollDate; //Date 타입의 경우는 java.sql.Date로 사용 할 것
	/*
	USERNO NUMBER PRIMARY KEY,
    USERID VARCHAR2(15) NOT NULL UNIQUE,
    USERPWD VARCHAR2(20) NOT NULL,
    USERNAME VARCHAR2(20) NOT NULL,
    GENDER CHAR(1) CHECK (GENDER IN ('M','F')),
    AGE NUMBER,
    EMAIL VARCHAR2(30),
    PHONE CHAR(11),
    ADDRESS VARCHAR2(100),
    HOBBY VARCHAR2(50),
    ENROLLDATE DATE DEFAULT SYSDATE NOT NULL
	*/
	
	
	//생성자부: 생성자들이 모여있는 곳
	//기본생성자는 꼭 만들 것
	public Member() {
		super();
	}
	public Member(int userNo, String userId, String userPwd, String userName, String gender, int age, String email,
			String phone, String address, String hobby, Date enrollDate) {
		super();
		this.userNo = userNo;
		this.userId = userId;
		this.userPwd = userPwd;
		this.userName = userName;
		this.gender = gender;
		this.age = age;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.hobby = hobby;
		this.enrollDate = enrollDate;
	}
	//회원 추가용 생성자
	public Member(String userId, String userPwd, String userName, String gender, int age, String email, String phone,
			String address, String hobby) {
		super();
		this.userId = userId;
		this.userPwd = userPwd;
		this.userName = userName;
		this.gender = gender;
		this.age = age;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.hobby = hobby;
	}
	
	//메소드부: 메소드들이 모여있는 부분
	//getter/setter, toString 오버라이딩
	public int getUserNo() {
		return userNo;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public Date getEnrollDate() {
		return enrollDate;
	}
	public void setEnrollDate(Date enrollDate) {
		this.enrollDate = enrollDate;
	}
	
	@Override
	public String toString() {
		return "Member [userNo=" + userNo + ", userId=" + userId + ", userPwd=" + userPwd + ", userName=" + userName
				+ ", gender=" + gender + ", age=" + age + ", email=" + email + ", phone=" + phone + ", address="
				+ address + ", hobby=" + hobby + ", enrollDate=" + enrollDate + "]";
	}

	
	
}
