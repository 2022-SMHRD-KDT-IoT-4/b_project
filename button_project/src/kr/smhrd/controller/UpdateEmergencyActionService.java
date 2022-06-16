package kr.smhrd.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.smhrd.domain.EmergencyVO;
import kr.smhrd.model.EmergencyDAO;

public class UpdateEmergencyActionService implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		
		int emergency_pk = Integer.parseInt(request.getParameter("emergency_pk"));
		String emergency_action = request.getParameter("emergency_action");
		//int button_id = Integer.parseInt(request.getParameter("button_id"));
		// int button_id = Integer.parseInt(request.getParameter("button_id"));
	// EmergencyVO vo = new EmergencyVO(emergency_pk, emergency_check, emergency_type, emergency_date, emergency_action, emergency_action_date, button_id);
		
		System.out.println("UpdateEmergencyActionService가 받은 pk : "+emergency_pk);
		System.out.println("UpdateEmergencyActionService가 받은 action : "+emergency_action);
		
		EmergencyVO vo = new EmergencyVO();
		vo.setEmergency_pk(emergency_pk);
		vo.setEmergency_action(emergency_action);
		
		EmergencyDAO dao = new EmergencyDAO();
		
		int row = dao.updateEmergency(vo);
		//System.out.println(button_id);
		/*
		 * if (row > 0) { System.out.println("조치 사항 수정 "); HttpSession session =
		 * request.getSession(); session.setAttribute("emergency_action", vo); }
		 */	
		if(row>0) {
	         System.out.println("조치 사항 세션 유지 성공");
	         EmergencyDAO dao2 = new EmergencyDAO();
	         //String emergency_action_date = dao2.selectEmergencyDate(button_id);
	         
	         HttpSession session = request.getSession();
	         //session.setAttribute("date", emergency_action_date);
	      }
	      PrintWriter out;
	      try {
	         out = response.getWriter();
	         out.print("");
	      } catch (IOException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	      
	      return "";
		
		
		
		
	}
	}
	
	
		
		
		
		
		
		
		
		
		
		
		
		
