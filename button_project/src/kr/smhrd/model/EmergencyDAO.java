package kr.smhrd.model;

import java.util.ArrayList;

import javax.websocket.Session;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.smhrd.database.SqlSessionManager;
import kr.smhrd.domain.EmergencySenVO;
import kr.smhrd.domain.EmergencyVO;
import kr.smhrd.domain.SeniorVO;

public class EmergencyDAO {
	private SqlSessionFactory sqlSessionFactory = SqlSessionManager.getSqlSession();
	private SqlSession sqlSession = null;

	public String selectEmergencyDate(int button_id) {
		String emergency_date = null;
		try {
			sqlSession = sqlSessionFactory.openSession(true);
			emergency_date = sqlSession.selectOne("kr.smhrd.model.EmergencyDAO.emergencydateselect", button_id);
			emergency_date = emergency_date.substring(0, 19);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return emergency_date;
	}

	// 조치 사항 변경
	public int updateEmergency(EmergencyVO vo) {
		System.out.println(vo);
		int row = 0;
		try {
			sqlSession = sqlSessionFactory.openSession(true);
			row = sqlSession.update("kr.smhrd.model.EmergencyDAO.emergencyactionupdate", vo);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return row;
	}
	
	// 긴급 도움 테이블 조회
	public ArrayList<EmergencySenVO> emergencyAllList(){
		System.out.print("DB안쪽 : ");
		ArrayList<EmergencySenVO> list = new ArrayList<EmergencySenVO>();
		try {
			sqlSession = sqlSessionFactory.openSession(true);
			list = (ArrayList)sqlSession.selectList("kr.smhrd.model.EmergencyDAO.emergencylist");
			
			System.out.println(list);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return list;
	}

}
