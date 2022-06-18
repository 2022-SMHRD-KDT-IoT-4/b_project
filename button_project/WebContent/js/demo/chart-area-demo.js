// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';


//실내 활동량 그래프 
let str = [];
$(document).ready(() => {
   $.ajax({
      type: 'get',
      url: 'https://api.thingspeak.com/channels/1757275/fields/2.json?results=12',
      success: function(result) {
         let value = [];
         for (let i = 0; i < 12; i++) {
            console.log(result.feeds[i].field2);
            value.push(result.feeds[i].field2);
            
            if (value[i] == null) {
               value[i] = 0;
            }

            console.log(value);
            var ctx = document.getElementById("myAreaChart");
            var myLineChart = new Chart(ctx, {
               type: 'line',
               data: {
                  labels: ["0", "2:00", "4:00", "6:00", "8:00", "10:00", "12:00", "14:00", "16:00", "18:00", "20:00", "22:00"],
                  datasets: [{
                     label: "활동량",
                     lineTension: 0.3,
                     backgroundColor: "rgba(78, 115, 223, 0.05)",
                     borderColor: "rgba(78, 115, 223, 1)",
                     pointRadius: 3,
                     pointBackgroundColor: "rgba(78, 115, 223, 1)",
                     pointBorderColor: "rgba(78, 115, 223, 1)",
                     pointHoverRadius: 3,
                     pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
                     pointHoverBorderColor: "rgba(78, 115, 223, 1)",
                     pointHitRadius: 10,
                     pointBorderWidth: 2,
                     data: [value[0], value[1], value[2], value[3], value[4], value[5], value[6], value[7], value[8],
                        value[9], value[10], value[11]
                     ]
                  }],
               },
               options: {
                  maintainAspectRatio: false,
                  layout: {
                     padding: {
                        left: 10,
                        right: 25,
                        top: 25,
                        bottom: 0
                     }
                  },
                  scales: {
                     xAxes: [{
                        time: {
                           unit: 'date'
                        },
                        gridLines: {
                           display: false,
                           drawBorder: false
                        },
                        ticks: {
                           maxTicksLimit: 7
                        }
                     }],
                     yAxes: [{
                        ticks: {
                           maxTicksLimit: 5,
                           padding: 10,
                           // Include a dollar sign in the ticks
                           callback: function(value, index, values) {
                              return "";
                           }
                        },
                        gridLines: {
                           color: "rgb(234, 236, 244)",
                           zeroLineColor: "rgb(234, 236, 244)",
                           drawBorder: false,
                           borderDash: [2],
                           zeroLineBorderDash: [2]
                        }
                     }],
                  },
                  legend: {
                     display: false
                  },

                  callbacks: {
                     label: function(tooltipItem, chart) {
                        var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                        return datasetLabel + ': $' + number_format(tooltipItem.yLabel);
                     }
                  }

               }
            });
         }
      },
      error: function() {
         alert('통신실패!❌');
      }
   })
});

// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

function number_format(number, decimals, dec_point, thousands_sep) {
   // *     example: number_format(1234.56, 2, ',', ' ');
   // *     return: '1 234,56'
   number = (number + '').replace(',', '').replace(' ', '');
   var n = !isFinite(+number) ? 0 : +number,
      prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
      sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
      dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
      s = '',
      toFixedFix = function(n, prec) {
         var k = Math.pow(10, prec);
         return '' + Math.round(n * k) / k;
      };
   // Fix for IE parseFloat(0.55).toFixed(0) = 0;
   s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
   if (s[0].length > 3) {
      s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
   }
   if ((s[1] || '').length < prec) {
      s[1] = s[1] || '';
      s[1] += new Array(prec - s[1].length + 1).join('0');
   }
   return s.join(dec);
}

// Area Chart Example




