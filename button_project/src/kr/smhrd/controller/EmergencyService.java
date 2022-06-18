package kr.smhrd.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.smhrd.domain.EmergencyVO;
import kr.smhrd.mail.SendEmail;
import kr.smhrd.model.ButtonSeniorDAO;
import kr.smhrd.model.EmergencyDAO;
import kr.smhrd.model.MemberDAO;
import kr.smhrd.model.MemberSeniorDAO;
import kr.smhrd.model.SeniorDAO;

@WebServlet("/EmergencyService.do")
public class EmergencyService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int button_id = Integer.parseInt(request.getParameter("button_id"));
		String emergency_check = request.getParameter("emergency_check");

		System.out.println("기기에서 EmergencyService로 들어온 button_id: " + button_id);
		System.out.println("기기에서 EmergencyService로 들어온 check : " + emergency_check);

		if (emergency_check != null) {
			emergency_check = "Y";

			// 1. 응급 데이터에 추가
			EmergencyVO vo = new EmergencyVO(0, emergency_check, null, null, null, button_id);
			EmergencyDAO dao = new EmergencyDAO();
			int row = dao.insert(vo);

			if (row > 0) {
				System.out.println("emergency insert 성공!");
			}

			// 2. 메일 전송 기능
			// 2-1. 버튼시니어테이블에서 노인번호 가져오기
			ButtonSeniorDAO bDao = new ButtonSeniorDAO();
			int senior_num = bDao.selectSeniorNum(button_id);

			// 2-2. 시니어테이블에서 노인 이름 가져오기
			SeniorDAO sDao = new SeniorDAO();
			String senior_name = sDao.selectSeniorName(senior_num);

			// 2-3. 멤버시니어 테이블에서 멤버 아이디 가져오기
			MemberSeniorDAO msDao = new MemberSeniorDAO();
			String member_id = msDao.selectMemberId(senior_num);

			// 2-4. 멤버 테이블에서 멤버 메일 가져오기
			MemberDAO mDao = new MemberDAO();
			String member_mail = mDao.selectMemberMail(member_id);

			// 2-5. 이멀전시 테이블에서 응급 호출 시간 가져오기
			EmergencyDAO eDAO = new EmergencyDAO();
			String emergency_date = eDAO.selectEmergencyDate(button_id);
			System.out.println(emergency_date);
			

			// 2-6. 메일 보내기
			SendEmail sd = new SendEmail();
			sd.send(member_mail, senior_name, emergency_date);

			// 2-7. 아두이노로 메일 보냈다고 아무거나 데이터 주기
			request.setAttribute("result", emergency_date);
			RequestDispatcher rd = request.getRequestDispatcher("testPage.jsp");
			rd.forward(request, response);
		}
	}
}
