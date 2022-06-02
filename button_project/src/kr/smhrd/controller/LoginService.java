package kr.smhrd.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.smhrd.domain.MemberVO;
import kr.smhrd.model.MemberDAO;


public class LoginService implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		
		MemberVO vo = new MemberVO(id, pw, null, null, null, null, null, null);
		
		MemberDAO dao = new MemberDAO();
		MemberVO result = dao.login(vo);
		
		if(result != null) {
			HttpSession session = request.getSession();
			session.setAttribute("member", result);
			System.out.println("로그인 성공!");
		}
		// return값 변경하기
		return "main.html";
	}

}
