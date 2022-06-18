package kr.smhrd.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.smhrd.model.MemberDAO;


public class DeleteService implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String member_id = request.getParameter("member_id");
		System.out.println(member_id);

		MemberDAO dao = new MemberDAO();
		int row = dao.delete(member_id);

		if (row > 0) {
			System.out.println("회원 삭제 성공!!");
		}		
		// return값 변경하기
		return "infoSenior.jsp";
	}

}
