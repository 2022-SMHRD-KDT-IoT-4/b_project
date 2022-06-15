package kr.smhrd.model;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.smhrd.database.SqlSessionManager;
import kr.smhrd.domain.MemberSeniorVO;

public class MemberSeniorDAO {

	private SqlSessionFactory sqlSessionFactory = SqlSessionManager.getSqlSession();
	private SqlSession sqlSession = null;
	
	// 1. 회원 별 관리노인 조회
	public ArrayList<MemberSeniorVO> memberSeniorList(){
		ArrayList<MemberSeniorVO> list = new ArrayList<MemberSeniorVO>();
		try {
			sqlSession = sqlSessionFactory.openSession(true);
			list = (ArrayList)sqlSession.selectList("kr.smhrd.model.MemberSeniorDAO.memberseniorlist");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return list;
	}
	
	// 2. 응급 호출에 필요 : 멤버 아이디 구하기
		public String selectMemberId(int senior_num) {
			String member_id = null;
			try {
				sqlSession = sqlSessionFactory.openSession(true);
				member_id = sqlSession.selectOne("kr.smhrd.model.MemberSeniorDAO.memberidselect", senior_num);
				System.out.println(member_id);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				sqlSession.close();
			}
			return member_id;
		}
}
