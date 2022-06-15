package kr.smhrd.model;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.smhrd.database.SqlSessionManager;

public class ButtonSeniorDAO {
	private SqlSessionFactory sqlSessionFactory = SqlSessionManager.getSqlSession();
	private SqlSession sqlSession = null;

	// 버튼 아이디 이용해 시니어 넘버 구하기
	public int selectSeniorNum(int button_id) {
		int senior_num = 0;
		try {
			sqlSession = sqlSessionFactory.openSession(true);
			senior_num = sqlSession.selectOne("kr.smhrd.model.ButtonDAO.selectSeniorNum", button_id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return senior_num;
	}
}
