package kr.smhrd.model;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.smhrd.database.SqlSessionManager;
import kr.smhrd.domain.MemberVO;

public class MemberDAO {

	private SqlSessionFactory sqlSessionFactory = SqlSessionManager.getSqlSession();
	private SqlSession sqlSession = null;

	// 1. 회원가입
	public int join(MemberVO vo) {
		int row = 0;
		try {
			sqlSession = sqlSessionFactory.openSession(true);
			row = sqlSession.insert("kr.smhrd.model.MemberDAO.memberinsert", vo);
			System.out.println("1. 여기가 오류?");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return row;
	}

	// 2. 로그인
	public MemberVO login(MemberVO vo) {
		MemberVO result = null;
		try {
			sqlSession = sqlSessionFactory.openSession(true);
			result = sqlSession.selectOne("kr.smhrd.model.MemberDAO.memberlogin", vo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return result;
	}

	// 3. 회원 정보 수정
	public int update(MemberVO vo) {
		int row = 0;
		try {
			sqlSession = sqlSessionFactory.openSession(true);
			// mapper에서 지정해준 이름, 가지고 갈 값
			row = sqlSession.update("kr.smhrd.model.MemberDAO.memberupdate", vo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return row;
	} // 회원 수정 끝

	// 4. 회원 삭제
	public int delete(String id) {
		int row = 0;
		try {
			sqlSession = sqlSessionFactory.openSession(true);
			row = sqlSession.delete("kr.smhrd.model.MemberDAO.memberupdate", id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return row;
	} // 회원 삭제 끝
	
	// 5. 응급 호출에 필요 : 멤버 메일 가져오기
		public String selectMemberMail(String member_id) {
			String member_mail = null;
			try {
				sqlSession = sqlSessionFactory.openSession(true);
				member_mail = sqlSession.selectOne("kr.smhrd.model.MemberDAO.membermailselect", member_id);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				sqlSession.close();
			}
			return member_mail;
		}
	
	
}
