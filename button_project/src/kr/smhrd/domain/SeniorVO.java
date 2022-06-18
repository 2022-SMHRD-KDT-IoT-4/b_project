package kr.smhrd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeniorVO {
	private int senior_num;
	private String senior_name;
	private String senior_address;
	private String disease;
	private String gender;
	private int weight;
	private int age;
}
