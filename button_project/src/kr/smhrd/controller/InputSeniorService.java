package kr.smhrd.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.smhrd.domain.SeniorVO;
import kr.smhrd.model.SeniorDAO;

public class InputSeniorService implements Command {
	public String execute(HttpServletRequest request, HttpServletResponse response) {

	String senior_name = request.getParameter("senior_name");
	String senior_address = request.getParameter("senior_address");
	String disease = request.getParameter("disease");
	String gender = request.getParameter("gender");
	int weight = Integer.parseInt(request.getParameter("weight"));
	int age = Integer.parseInt(request.getParameter("age"));
	
	SeniorVO vo = new SeniorVO(0, senior_name, senior_address, disease, gender, weight, age);
	
	SeniorDAO dao = new SeniorDAO();
	int row = dao.inputSenior(vo);
	
	String moveURL = "";
	
	if (row > 0) {
		request.setAttribute("vo", vo);
		System.out.println("노인 정보 입력 성공!");
	} else {
		System.out.println("노인 정보 입력 실패");
	}
	
	// return값 변경하기
	moveURL = "inputSenior.jsp";
	return moveURL;
	
	}
}
