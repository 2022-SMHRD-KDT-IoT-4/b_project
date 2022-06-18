package kr.smhrd.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.smhrd.domain.SeniorVO;
import kr.smhrd.model.SeniorDAO;

public class DeleteSeniorService implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		int senior_num = Integer.parseInt(request.getParameter("senior_num"));
		String member_id = request.getParameter("member_id");

		SeniorDAO dao = new SeniorDAO();
		int row = dao.deleteSenior(senior_num);
		
		SeniorDAO dao2 = new SeniorDAO();
		ArrayList<SeniorVO> list = dao2.seniorAllList(member_id);

		if (row > 0) {
			HttpSession session = request.getSession();
			session.setAttribute("list", list);
			System.out.println("노인 정보 삭제 성공");
		}

		return "infoSenior.jsp";
	}

}
