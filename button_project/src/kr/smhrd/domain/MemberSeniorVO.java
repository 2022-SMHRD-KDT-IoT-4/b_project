package kr.smhrd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberSeniorVO {
	private int member_senior_pk;
	private String member_id;
	private int senior_num;
	private String member_senior_info;
}
