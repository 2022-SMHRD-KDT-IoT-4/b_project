package kr.smhrd.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import kr.smhrd.domain.QuestionVO;
import kr.smhrd.model.QuestionDAO;

@WebServlet("/QuestionInfoService.do")
public class QuestionInfoService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		int button_id = Integer.parseInt(request.getParameter("button_id"));
		QuestionDAO dao = new QuestionDAO();
		List<QuestionVO> list = dao.selectQuestion(button_id);
		
		Gson g = new Gson();
		String json = g.toJson(list);
		
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out;
		out = response.getWriter();
		out.print(json);
	}
}
