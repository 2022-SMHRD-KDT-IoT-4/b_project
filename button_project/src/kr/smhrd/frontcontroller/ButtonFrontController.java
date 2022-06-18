package kr.smhrd.frontcontroller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.smhrd.controller.Command;
import kr.smhrd.controller.DeleteSeniorService;
import kr.smhrd.controller.DeleteService;
import kr.smhrd.controller.InputSeniorService;
import kr.smhrd.controller.JoinService;
import kr.smhrd.controller.LoginService;
import kr.smhrd.controller.LogoutService;
import kr.smhrd.controller.SelectAllSeniorService;
import kr.smhrd.controller.UpdateEmergencyActionService;
import kr.smhrd.controller.UpdateSeniorService;
import kr.smhrd.controller.UpdateService;

@WebServlet("*.do")
public class ButtonFrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("[FrontController]");

		// 어느 서블릿으로 요청이 들어왔는지 확인
		String uri = request.getRequestURI();
		System.out.println(uri);

		// 프로젝트 이름
		String project = request.getContextPath();
		System.out.println(project);

		// 요청 들어온 servlet 이름만 확인
		String reqURL = uri.substring(project.length()+1);
		System.out.println(reqURL);
		request.setCharacterEncoding("UTF-8");
		
		String moveURL = "";
		Command sc = null;
		
		if(reqURL.equals("JoinService.do")) {
			// 1. 회원가입 기능
			sc = new JoinService();
		}else if (reqURL.equals("LoginService.do")) {
			// 2. 로그인 기능
			sc = new LoginService();
		}else if (reqURL.equals("LogoutService.do")) {
			// 3. 로그아웃 기능
			sc = new LogoutService();
		}else if (reqURL.equals("DeleteService.do")) {
			// 4. 회원 삭제 기능
			sc = new DeleteService();
		}else if (reqURL.equals("UpdateService.do")) {
			// 5. 회원 정보 수정
			sc = new UpdateService();
		}else if (reqURL.equals("InputSeniorService.do")) {
			/// 6. 노인 정보 기입 기능
			sc = new InputSeniorService();
		}else if (reqURL.equals("DeleteSeniorService.do")) {
			// 7. 노인 정보 삭제 기능
			sc = new DeleteSeniorService();
		}else if (reqURL.equals("UpdateSeniorService.do")) {
			// 8. 노인 정보 수정 기능
			sc = new UpdateSeniorService();
		}else if(reqURL.equals("UpdateEmergencyActionService.do")) {
			// 9. 조치사항, 조시 시간 업데이트
			sc = new UpdateEmergencyActionService();
		}
		
		moveURL = sc.execute(request, response);
		RequestDispatcher rd = request.getRequestDispatcher(moveURL);
		rd.forward(request, response);

	}

}
