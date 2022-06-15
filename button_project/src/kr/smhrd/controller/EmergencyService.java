package kr.smhrd.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.smhrd.domain.MemberVO;
import kr.smhrd.model.MemberDAO;


public class EmergencyService implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		return "경로";

	}

}
