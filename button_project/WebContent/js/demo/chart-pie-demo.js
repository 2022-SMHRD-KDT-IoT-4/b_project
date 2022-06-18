Chart.defaults.global.defaultFontFamily = 'Nunito',
'-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

   console.log('파이차트에 들어왔다');
   var ctx = document.getElementById("myPieChart");
   
   var myPieChart = new Chart(ctx, {
      type : 'doughnut',
      data : {
         labels : [ "안녕히 주무셨어요?", "식사는 하셨나요?", "약은 드셨나요?", , "혹시 아픈 데가 있으신가요?",
         "운동은 하셨나요?", "피곤하진 않으세요?", "친구는 만나셨나요", "주무실 준비는 하셨나요?", "무응답" ],
         datasets : [ {
            data : [ 30, 55, 20, 1, 15, 5, 20, 30, 10 ],
            backgroundColor : [ '#1cc88a', '#f6c23e', '#ffcccc',  '#33ffdd', '#19c2ed',  '#8e7cc3',
            '#8fce00', '#ff8100', '#003153', '#284ff0', '#21333d']
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