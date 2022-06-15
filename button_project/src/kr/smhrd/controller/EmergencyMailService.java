package kr.smhrd.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.smhrd.mail.SendEmail;
import kr.smhrd.model.ButtonSeniorDAO;
import kr.smhrd.model.EmergencyDAO;
import kr.smhrd.model.MemberDAO;
import kr.smhrd.model.MemberSeniorDAO;
import kr.smhrd.model.SeniorDAO;

@WebServlet("/EmergencyMailService.do")
public class EmergencyMailService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int button_id = Integer.parseInt(request.getParameter("button_id"));
		String emergency_check = request.getParameter("emergency_check");
		String result = request.getParameter("result");
		
		if (emergency_check != null) {

			// 버튼시니어테이블에서 노인번호 가져오기
			ButtonSeniorDAO bDao = new ButtonSeniorDAO();
			int senior_num = bDao.selectSeniorNum(button_id);

			// 시니어테이블에서 노인 이름 가져오기
			SeniorDAO sDao = new SeniorDAO();
			String senior_name = sDao.selectSeniorName(senior_num);

			// 멤버시니어 테이블에서 멤버 아이디 가져오기
			MemberSeniorDAO msDao = new MemberSeniorDAO();
			String member_id = msDao.selectMemberId(senior_num);

			// 멤버 테이블에서 멤버 메일 가져오기
			MemberDAO mDao = new MemberDAO();
			String member_mail = mDao.selectMemberMail(member_id);

			// 이멀전시 테이블에서 응급 호출 시간 가져오기
			EmergencyDAO eDAO = new EmergencyDAO();
			String emergency_date = eDAO.selectEmergencyDate(button_id);

			// 메일 보내기
			SendEmail sd = new SendEmail();
			sd.send(member_mail, senior_name, emergency_date);
			
			// 아두이노로 메일 보냈다고 아무거나 데이터 주기
			request.setAttribute("result", emergency_date);
			RequestDispatcher rd = request.getRequestDispatcher("testPage.jsp");
			rd.forward(request, response);
		}


	}

}
