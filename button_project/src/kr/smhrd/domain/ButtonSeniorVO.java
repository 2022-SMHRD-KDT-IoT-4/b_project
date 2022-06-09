package kr.smhrd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ButtonSeniorVO {
	private int button_id;
	private int senior_num;
	private String button_senior_info;
}
