package kr.smhrd.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.smhrd.domain.SeniorVO;
import kr.smhrd.model.SeniorDAO;

public class SelectAllSeniorService implements Command{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		
		//SeniorDAO dao = new SeniorDAO();
		//ArrayList<SeniorVO> list = dao.seniorAllList();
		//request.setAttribute("list", list);
		
		return "seniorInfoList.jsp";
	}

}
