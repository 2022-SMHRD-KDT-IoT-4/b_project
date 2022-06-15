package kr.smhrd.model;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.smhrd.database.SqlSessionManager;

public class EmergencyDAO {
	private SqlSessionFactory sqlSessionFactory = SqlSessionManager.getSqlSession();
	private SqlSession sqlSession = null;
	
	public String selectEmergencyDate(int button_id) {
		String emergency_date = null;
		try {
			sqlSession = sqlSessionFactory.openSession(true);
			emergency_date = sqlSession.selectOne("kr.smhrd.model.EmergencyDAO.emergencydateselect",button_id);
			emergency_date = emergency_date.substring(0, 19);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return emergency_date;
	}

}
