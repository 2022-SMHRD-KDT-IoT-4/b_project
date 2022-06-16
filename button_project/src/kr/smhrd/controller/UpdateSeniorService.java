package kr.smhrd.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.smhrd.domain.SeniorVO;
import kr.smhrd.model.SeniorDAO;

public class UpdateSeniorService implements Command {
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
//		System.out.println("들어옴");
//		int senior_num = Integer.parseInt(request.getParameter("senior_num"));
		System.out.println("들어옴1");
		String senior_name = request.getParameter("senior_name");
		System.out.println("들어옴2");
		String senior_address = request.getParameter("senior_address");
		System.out.println("들어옴2");
		String disease = request.getParameter("disease");
		System.out.println("들어옴3");
		String gender = request.getParameter("gender");
		System.out.println("들어옴4");
		int weight = Integer.parseInt(request.getParameter("weight"));
		System.out.println("들어옴5");
		int age = Integer.parseInt(request.getParameter("age"));
		
		//SeniorVO vo = new SeniorVO(0, senior_name, senior_address, disease, gender, weight, age);
		
		SeniorDAO dao = new SeniorDAO();
		//int row = dao.updateSenior(vo);
		
		//if(row>0) {
		//	System.out.println("노인 정보 수정 성공");
		//	HttpSession session = request.getSession();
		//	session.setAttribute("senior", vo);
		//}
		return "infoSenior.jsp";
	}

}
