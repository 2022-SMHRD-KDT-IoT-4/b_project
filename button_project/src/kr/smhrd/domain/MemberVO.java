package kr.smhrd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO {

	// member 테이블 
	private String member_id;
	private String member_pw;
	private String member_mail;
	private String member_phone;
	private String member_name;
	private String member_address;
	private String member_check;
	private String member_license;
	
}
