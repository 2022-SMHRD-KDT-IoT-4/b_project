		Chart.defaults.global.defaultFontFamily = 'Nunito',
		'-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

$(document).ready(function() {
	let today = new Date();
	let month = today.getMonth() + 1; // 금월
	if (month < 10) { // api의 월 형식에 형식 맞춤
		month = '0' + month;
	}
	;
	let thisDate = today.getFullYear() + "-" + month; // 금해 금월
	let date = today.getDate();

	$.ajax({

		type : 'get',
		url : 'https://api.thingspeak.com/channels/1757275/fields/1.json',
		success : function(result) {

			var ctx = document.getElementById("myPieChart");
			
			let monthlyAnswer = 0; // 월별 응답한 값 누적할 변수
			let monthlyNotAnswer = 0; // 월별 응답 안한 값 누적할 변수
			
			for (let i = 0; i < result.feeds.length; i++) { // api 데이터들 중에
				let createdAt = result.feeds[i].created_at;
				if (createdAt.startsWith(thisDate) == true) { // api에서 생성된 데이터 연-월이 지금의 연-월이고,

					let tmpAnswer = 0;
					// if () {								// 설문시간(DB의 질문시점~ +5분)에 지금의 시-분이 포함될 때......help
					if (result.feeds[i].field1 != null) { // btn을 누르면 응답데이터로 더해주기
						tmpAnswer += 1;
					}
					// }
					if ( tmpAnswer == 1 ) {
						monthlyAnswer += 1;
					} else if ( tmpAnswer.equals == null ) {
						monthlyNotAnswer += 1;
					} else { // 근데 설문시간에 버튼을 두번 누르면 응급상황으로!
						console.log('응급상황!');
					}
				}
			}
			
			// 월별 응답값 누적하는 알고리즘이 좀 이상한 것 같음.................................help
			// 평소엔 누적을 하다가 달이 바뀌면 monthlyAnswer과 NotAnswer은 0으로 리셋되야하는 거 아닌가?
			if ( date == 1 ) {
				monthlyAnswer = 0;
				monthlyNotAnswer = 0;
			}
			
			console.log('monthlyAnswer :' + monthlyAnswer);
			console.log('monthlyNotAnswer : ' + monthlyNotAnswer);
			
			// percentage 다시 계산하기 : monthlyAnswer / 월별 총 질문수 * 100%  
			let percentage = (monthlyAnswer / date) * 100;
			$('#percentage').text('설문 응답률(월) - ' + percentage + '%');

			var myPieChart = new Chart(ctx, {
				type : 'doughnut',
				data : {
					labels : [ "응답 횟수 ", "무응답 횟수 " ],
					datasets : [ {
						data : [ monthlyAnswer, monthlyNotAnswer ],
						backgroundColor : [ '#1cc88a', '#f6c23e' ],
						hoverBackgroundColor : [ '#2e59d9', '#e74a3b' ],
						hoverBorderColor : "rgba(234, 236, 244, 1)",
					} ],
				},
				options : {
					maintainAspectRatio : false,
					tooltips : {
						backgroundColor : "rgb(255,255,255)",
						bodyFontColor : "#858796",
						borderColor : '#dddfeb',
						borderWidth : 1,
						xPadding : 15,
						yPadding : 15,
						displayColors : false,
						caretPadding : 10,
					},
					legend : {
						display : false
					},
					cutoutPercentage : 80,
				}
			})
		},
		error : function() {
			alert('파이 차트 에러!!');

		}
	})

})
