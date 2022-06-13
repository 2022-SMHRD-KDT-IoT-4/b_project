package kr.smhrd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionVO {
	private int question_id;
	private int question_num;
	private String question_time;
	private int button_id;
	private String question_data;
}
