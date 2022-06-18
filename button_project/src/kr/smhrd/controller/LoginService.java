package kr.smhrd.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.smhrd.domain.MemberVO;
import kr.smhrd.domain.SeniorVO;
import kr.smhrd.model.MemberDAO;
import kr.smhrd.model.SeniorDAO;


public class LoginService implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response){
		
		String member_id = request.getParameter("member_id");
		String member_pw = request.getParameter("member_pw");
		
		MemberVO vo = new MemberVO(member_id, member_pw, null, null, null, null, null, null);
		
		MemberDAO dao = new MemberDAO();
		MemberVO result = dao.login(vo);

		
		SeniorDAO dao2 = new SeniorDAO();
		ArrayList<SeniorVO> list = dao2.seniorAllList(member_id);
		
		//System.out.println(list.size());		
		HttpSession session = request.getSession();
		
		if(result != null) {
			//HttpSession session2 = request.getSession();
			session.setAttribute("result", result);
			session.setAttribute("list", list);
			System.out.println("로그인 성공!");
		}else {
			session.setAttribute("result1", "로그인에 실패하셨습니다.");
			System.out.println("실패");
			return "login.jsp";
		}
		// return값 변경하기
		return "index.jsp";
	}

}
