package kr.smhrd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencySenVO {
	private String senior_name;
	private int emergency_pk;
	private String emergency_check;
	private String emergency_date;
	private String emergency_action;
	private String emergency_action_date;
	private int button_id;
}
