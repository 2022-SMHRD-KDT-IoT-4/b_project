// bar1 : 온도 bar2 : 일교차 bar3 : 습도 bar4 : 공기오염도 bar5 : 종합생활환경점수
// field1 : 응급호출 / field2 : 초음파센서 / field3 : 온도 / field4 : 습도 / field5 : 미세먼지/  field6 : 질문응답

$(document).ready(function() {
	let today = new Date();
	let date = today.getDate();
	if (date < 10) { // api랑 형식 맞추기
		date = '0' + date;
	}

	$.ajax({
		type : 'get',
		url : 'https://api.thingspeak.com/channels/1757275/feed.json',
		success : function(result) {

			let tempSum = 0; //평균온도 구할 때 쓰는 변수
			let cnt = 0; //평균온도 구할 때 쓰는 변수
			let tempMax = 0;
			let tempMin = 0;
			let humidity = 0;
			
			// 일교차 구하기 : 금일 측정된 온도 중 하나로 임의값 주기 (field3 : 온도)
			let i = 0;
			while (true) {
				// cannot read properties of undefined(reading 'created_at').......help
				let createdAt = result.feeds[i].created_at;
				
				if (createdAt.substr(8, 2) == date) {
					if (result.feeds[i].field3 != null) {
						tempMax = Number(result.feeds[i].field3);
						tempMin = Number(result.feeds[i].field3);
						break;
					}
				}
				i++;
			}
			
			for (let j = 0; j < result.feeds.length; j++) {
				let createdAt = result.feeds[j].created_at;
				if (createdAt.substr(8, 2) == date) {
					// 온도, 일교차 구하기
					if (result.feeds[j].field3 != null) {
						cnt++;
						tempSum += Number(result.feeds[j].field3);

						if (tempMax < Number(result.feeds[j].field3)) {
							tempMax = result.feeds[j].field3;
						}
						if (tempMin > Number(result.feeds[j].field3)) {
							tempMin = result.feeds[j].field3;
						}
					}
					
					// 습도 구하기 (field4 : 습도)
					if (result.feeds[j].field4 != null) {
						humidity = result.feeds[j].field4;
					}
					
					// 공기오염도 구하기............................센서 다 오면 수정
					
					// 종합 생활 환경 점수도.........................센서 주문리스트보고 수정
					
					
					
				}
			}


			$('#tempAvg').text('평균 ' + parseInt(tempSum / cnt) + '도');
			$('.progress-bar.bg-danger').css({width:parseInt(tempSum/cnt)});
			$('#tempGap').text(Number(tempMax)-Number(tempMin)+'도(최고-최저)');
			$('.progress-bar.bg-warning').css({width:Number(tempMax)-Number(tempMin)});
			$('#humidityText').text(humidity+'%');
			$('#humidity').css({width:Number(humidity)});
			$('#air').text('센서 다 오면 수정'+'%');
			$('.progress-bar.bg-info').css({width:0}); //여기도 센서 다 오면 수정(공기오염도)
			
		
		},
		error : function() {
			alert('생활 환경 정보 progress bar 에러!');
		}
	});
});
