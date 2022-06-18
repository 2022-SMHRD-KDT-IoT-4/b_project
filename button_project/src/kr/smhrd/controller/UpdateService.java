package kr.smhrd.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.smhrd.domain.MemberVO;
import kr.smhrd.model.MemberDAO;


public class UpdateService implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String member_id = request.getParameter("member_id");
		String member_pw = request.getParameter("member_pw");
		String member_mail= request.getParameter("member_mail");
		String member_phone = request.getParameter("member_phone");
		String member_address = request.getParameter("member_address");

		MemberVO vo = new MemberVO(member_id, member_pw, member_mail, member_phone, null, member_address, null, null);

		MemberDAO dao = new MemberDAO();
		int row = dao.update(vo);

		if (row > 0) {
			System.out.println("회원 정보 수정 성공");
			HttpSession session = request.getSession();
			session.setAttribute("member", vo);
		}
		// return값 변경하기
		return "login.jsp";
	}

}
