package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.dao.MemberDao;
import com.kh.model.service.MemberService;
import com.kh.model.vo.Member;
import com.kh.view.MemberView;

/*
 * Controller: View를 통해서 요청받은 기능을 처리하는 담당
 * 				해당 메소드로 전달된 데이터(매개 변수)를 가공처리(VO 객체의 각 필드에 변수값들을 담겠다) 한 후
 * 				Dao 메소드를 호출할 때 전달(매개변수로 보내겠다)
 * 				Dao로 부터 반환받은 결과(리턴값)에 따라서 사용자가 보게 될 화면을 지정해주는 역할
 * 				(View의 응답화면을 결정해주는 역할=>View 응답화면에 해당되는 메소드를 호출)
 * 				
 */
public class MemberController {
	//회원 추가 요청을 처리해주는 메소드
	public void insertMember(String userId, String userPwd, String userName, String gender, int age, String email, String phone, String address, String hobby ) {
		//1) 전달된 데이터들을 Member 객체에 담기(Member 객체 생성)
		//매개변수 생성자를 이용하거나, 기본생성자로 생성 후 setter 메소드 이용
		Member m=new Member(userId, userPwd, userName, gender, age, email, phone, address, hobby);
		
		//2) Service의 메소드 호출(이 때, 위에서 가공된 Member 객체를 매개변수로 넘기겠다)
		int result=new MemberService().insertMember(m);
		//3) 성공/실패에 따라 응답화면을 지정(View의 메소드 호출)
		if(result>0) {//성공
			//회원 추가 성공 문구를 띄워주는 화면단으로 호출
			new MemberView().displaySuccess("회원 추가 성공");
		}else {//실패
			//실패 문구를 띄워주는 화면단으로 메소드 호출
			new MemberView().displayFail("회원 추가 실패");
		}
	}
	//회원 전체 조회 요청을 처리해주는 메소드
	public void selectAll() {
		//따로 넘겨받은 값이 없어서 VO 객체로 가공할 필요가 없음
		//2) DAO의 메소드 호출
		ArrayList<Member> list=new MemberService().selectAll();
		
		//3) 조회결과 여부에 따라 응답화면을 지정(View의 메소드 호출)
		if(list.isEmpty()) {//비어있는 리스트일 경우=>조회 결과가 없음
			new MemberView().displayNodata("전체 조회 결과가 없습니다.");
		}else {//비어있는 리스트가 아닐 경우=>조회 결과가 있음
			/*
			for(int i=0; i<list.size(); i++) {
				System.out.println(list.get(i));
			}
			*/
			new MemberView().displayList(list);
		}
	}
	
	//아이디로 검색 요청을 처리해주는 메소드
	public void selectByUserId(String userId) {
		//1) 어차피 값이 하나이기 때문에 VO 객체로 가공은 패스
		//Member m=new Member();
		//m.setUserId(userId);
		
		//2) DAO의 메소드 호출
		Member m=new MemberService().selectByUserId(userId);
		
		//3) 조회결과 여부에 따라 응답화면을 지정(View의 메소드 호출)
		//조회결과가 있다면 m!=null 없다면 m==null
		if(m==null) {//조회결과가 없는 경우
			new MemberView().displayNodata(userId+"에 해당하는 검색 결과가 없습니다.");
		}else {//조회결과가 있는 경우
			new MemberView().displayOne(m);
		}
	}
	
	//이름 키워드로 검색 요청을 처리해주는 메소드
	public void selectByUserName(String keyword) {
		//1) 어차피 값이 하나이기 때문에 VO 객체로 가공은 패스
		//Member m=new Member();
		//m.setUserName(userName);
		
		//2) DAO의 메소드 호출
		ArrayList<Member> list=new MemberService().selectByUserName(keyword);
		
		if(list.isEmpty()) {
			new MemberView().displayNodata("해당 키워드에 일치하는 이름의 회원이 없습니다.");
		}else {
			new MemberView().displayList(list);
		}
	}
	
	//사용자의 회원 정보 변경 요청시 처리해주는 메소드
	public void updateMember(String userId, String newPwd, String newEmail, String newPhone, String newAddress) {
		//1) VO 객체로 가공
		Member m=new Member();
		m.setUserId(userId);
		m.setUserPwd(newPwd);
		m.setEmail(newEmail);
		m.setPhone(newPhone);
		m.setAddress(newAddress);
		//2) DAO의 메소드 호출
		int result=new MemberService().updateMember(m); //UPDATE 구문 실행 예정=>int 형 반환(처리된 행의 개수)
		//3) 성공/실패 여부에 따라 응답뷰 지정
		if(result>0) {//성공
			new MemberView().displaySuccess("회원 정보 변경 성공");
		}else {//실패
			new MemberView().displayFail("회원 정보 변경 실패");
		}
	}
	
	//사용자의 회원 탈퇴 요청시 처리해주는 메소드
	public void deleteMember(String userId) {
		//1) VO 객체로 가공=>어차피 값이 한 개이기 때문에 패스

		//2) DAO의 메소드 호출
		int result=new MemberService().deleteMember(userId);
		
		//3)성공/실패 여부에 따라 응답뷰 지정
		if(result>0) { //성공
			new MemberView().displaySuccess("회원 탈퇴 성공");
		}else { //실패
			new MemberView().displayFail("회원 탈퇴 실패");
		}
	}
}
