package kr.smhrd.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.smhrd.domain.QuestionVO;
import kr.smhrd.model.QuestionDAO;

public class QuestionListService implements Command{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		int question_id = Integer.parseInt(request.getParameter("question_id"));
//		String question_time = request.getParameter("question_time");
//		String question_data = request.getParameter("question_data");
		
		//QuestionDAO dao = new QuestionDAO();
		//ArrayList<QuestionVO> list = dao.selectQuestion(question_id);
		
		//request.setAttribute("list", list);
		return null;
	}

}
