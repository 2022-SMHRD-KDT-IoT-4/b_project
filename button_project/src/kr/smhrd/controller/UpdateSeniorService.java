package kr.smhrd.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.smhrd.domain.SeniorVO;
import kr.smhrd.model.SeniorDAO;

public class UpdateSeniorService implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		int senior_num = Integer.parseInt(request.getParameter("senior_num"));
		String senior_name = request.getParameter("senior_name");
		String senior_address = request.getParameter("senior_address");
		String disease = request.getParameter("disease");
		String gender = request.getParameter("gender");
		int weight = Integer.parseInt(request.getParameter("weight"));
		int age = Integer.parseInt(request.getParameter("age"));
		
		SeniorVO vo = new SeniorVO(senior_num, senior_name, senior_address, disease, gender, weight, age);
		
		SeniorDAO dao = new SeniorDAO();
		int row = dao.updateSenior(vo);
		
		if(row>0) {
			System.out.println("노인 정보 수정 성공");
			HttpSession session = request.getSession();
			session.setAttribute("Senior", vo);
		}
		return "index.jsp";
	}

}
