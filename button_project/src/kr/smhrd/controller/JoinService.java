package kr.smhrd.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.smhrd.domain.MemberVO;
import kr.smhrd.model.MemberDAO;

public class JoinService implements Command {
	public String execute(HttpServletRequest request, HttpServletResponse response) {

		String member_id = request.getParameter("member_id");
		String member_pw = request.getParameter("member_pw");
		String member_mail = request.getParameter("member_mail");
		String member_phone = request.getParameter("member_phone");
		String member_name = request.getParameter("member_name");
		String member_address = request.getParameter("member_address");
		String member_check = request.getParameter("member_check");
		String member_license = request.getParameter("member_license");

		MemberVO vo = new MemberVO(member_id, member_pw, member_mail, member_phone, member_name, member_address, member_check, member_license);

		MemberDAO dao = new MemberDAO();
		int row = dao.join(vo);

		String moveURL = "";

		if (row > 0) {
			request.setAttribute("member_id", member_id);
			System.out.println("회원가입 성공!");
		} else {
			System.out.println("회원가입 실패");
		}
		// return값 변경하기
		moveURL = "login.jsp";
		return moveURL;
	}

}
