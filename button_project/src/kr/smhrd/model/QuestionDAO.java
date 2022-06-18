package kr.smhrd.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.smhrd.database.SqlSessionManager;
import kr.smhrd.domain.QuestionVO;
import kr.smhrd.domain.SeniorVO;

public class QuestionDAO {

	private SqlSessionFactory sqlSessionFactory = SqlSessionManager.getSqlSession();
	private SqlSession sqlSession = null;
	
	// 버튼 아이디 이용해 질문 번호, 질문 시간, 질문 내용 가져오기
	public List<QuestionVO> selectQuestion(int button_id) {
		
		List<QuestionVO> list= null;
	    sqlSession = sqlSessionFactory.openSession(true);
		list = sqlSession.selectList("kr.smhrd.model.QuestionDAO.questionlist", button_id);
	    sqlSession.close();

		return list;
	}
	
}
