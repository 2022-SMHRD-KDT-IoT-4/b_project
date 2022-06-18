package kr.smhrd.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.smhrd.domain.SeniorVO;
import kr.smhrd.model.SeniorDAO;

public class UpdateSeniorService implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		int senior_num = Integer.parseInt(request.getParameter("senior_num"));
		System.out.println(senior_num);
		String senior_name = request.getParameter("senior_name");
		String senior_address = request.getParameter("senior_address");
		String disease = request.getParameter("disease");
		String gender = request.getParameter("gender");
		int weight = Integer.parseInt(request.getParameter("weight"));
		int age = Integer.parseInt(request.getParameter("age"));
		
		String member_id = request.getParameter("member_id");
		
		SeniorVO vo = new SeniorVO(senior_num, senior_name, senior_address, disease, gender, weight, age);
		System.out.println(member_id);
		SeniorDAO dao = new SeniorDAO();
		int row = dao.updateSenior(vo);
		
		if(row>0) {
			System.out.println("노인 정보 수정 성공");
			SeniorDAO dao2 = new SeniorDAO();
			ArrayList<SeniorVO> list = dao2.seniorAllList(member_id);			
			
			HttpSession session = request.getSession();
			session.setAttribute("Senior", vo);
			session.setAttribute("list", list);
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
