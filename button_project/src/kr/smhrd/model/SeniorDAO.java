package kr.smhrd.model;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.smhrd.database.SqlSessionManager;
import kr.smhrd.domain.SeniorVO;

public class SeniorDAO {

	private SqlSessionFactory sqlSessionFactory = SqlSessionManager.getSqlSession();
	private SqlSession sqlSession = null;
	
	// 노인 정보 기입
	public int inputSenior(SeniorVO vo) {
		int row = 0;
		try {
			sqlSession = sqlSessionFactory.openSession(true);
			row = sqlSession.insert("kr.smhrd.model.SeniorDAO.seniorinsert", vo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return row;
	}// 노인 정보 기입 기능 끝
	
	// 노인 정보 삭제 기능
	public int deleteSenior(int senior_num) {
		int row = 0;
		try {
			sqlSession = sqlSessionFactory.openSession(true);
			row = sqlSession.delete("kr.smhrd.model.SeniorDAO.seniordelete", senior_num);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return row;
	}// 노인 정보 삭제 기능 끝
	
	// 노인 정보 수정 기능
	public int updateSenior(SeniorVO vo) {
		int row = 0;
		try {
			sqlSession = sqlSessionFactory.openSession(true);
			row = sqlSession.update("kr.smhrd.model.SeniorDAO.seniorupdate", vo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return row;
	}// 노인 정보 수정 기능 끝
	
	// 노인 정보 전체 조회 기능(기능확인용)
	public ArrayList<SeniorVO> seniorAllList(String member_id2){
		ArrayList<SeniorVO> list = new ArrayList<SeniorVO>();
		try {
			sqlSession = sqlSessionFactory.openSession(true);
			list = (ArrayList)sqlSession.selectList("kr.smhrd.model.SeniorDAO.seniorlist", member_id2);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return list;
		
	}
	
	// 5. 응급 호출 메일에 필요 : 노인이름 가져오기
	public String selectSeniorName(int senior_num) {
		String senior_name = null;
		try {
			sqlSession = sqlSessionFactory.openSession(true);
			senior_name = sqlSession.selectOne("kr.smhrd.model.SeniorDAO.seniornameselect", senior_num);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return senior_name;
	}

}
