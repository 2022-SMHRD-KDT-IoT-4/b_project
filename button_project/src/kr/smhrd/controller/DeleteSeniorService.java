package kr.smhrd.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.smhrd.model.SeniorDAO;

public class DeleteSeniorService implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		int senior_num = Integer.parseInt(request.getParameter("senior_num"));

		SeniorDAO dao = new SeniorDAO();
		int row = dao.deleteSenior(senior_num);

		if (row > 0) {
			System.out.println("노인 정보 삭제 성공");
		}

		return "index.jsp";
	}

}
