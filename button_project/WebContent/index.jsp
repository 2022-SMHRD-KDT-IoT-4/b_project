<%@page import="kr.smhrd.domain.EmergencySenVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="kr.smhrd.model.EmergencyDAO"%>
<%@page import="org.apache.ibatis.reflection.SystemMetaObject"%>
<%@page import="kr.smhrd.domain.MemberVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="en">
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>스마트 모니터링 서비스</title>

    <!-- Custom fonts for this template-->
    <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link
        href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
        rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="css/sb-admin-2.min.css" rel="stylesheet">
	
	
	<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
	
	
<script type="text/javascript">
//실내 활동량 그래프 
let str = [];
$(document).ready(function(){	
	if(${empty result}){
		   //$("#myModal").modal("show");
		   alert("로그인을 해야됩니다.");
		   location.href="login.jsp";
	}
	emergencyLoad();
	   
	$.ajax({
	      type: 'get',
	      url: 'https://api.thingspeak.com/channels/1757275/fields/2.json?results=12',
	      success: function(res) {
	         let value = [];
	         for (let i = 0; i < 12; i++) {
	            console.log(res.feeds[i].field2);
	            value.push(res.feeds[i].field2);
	            
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
	   
	   
	   // 생활 어
	let today = new Date();
	let date = today.getDate();
	if (date < 10) { // api랑 형식 맞추기
		date = '0' + date;
	}

	$.ajax({
		type : 'get',
		url : 'https://api.thingspeak.com/channels/1757275/feed.json',
		success : function(res) {
			let tempSum = 0; //평균온도 구할 때 쓰는 변수
			let cnt = 0; //평균온도 구할 때 쓰는 변수
			let tempMax = 0;
			let tempMin = 0;
			let humidity = 0;
			
			// 일교차 구하기 : 금일 측정된 온도 중 하나로 임의값 주기 (field3 : 온도)
			let i = 0;
			while (true) {
				// cannot read properties of undefined(reading 'created_at').......help
				let createdAt = res.feeds[i].created_at;
				
				if (createdAt.substr(8, 2) == date) {
					if (res.feeds[i].field3 != null) {
						tempMax = Number(res.feeds[i].field3);
						tempMin = Number(res.feeds[i].field3);
						break;
					}
				}
				i++;
			}
			
			for (let j = 0; j < res.feeds.length; j++) {
				let createdAt = res.feeds[j].created_at;
				if (createdAt.substr(8, 2) == date) {
					// 온도, 일교차 구하기
					if (res.feeds[j].field3 != null) {
						cnt++;
						tempSum += Number(res.feeds[j].field3);
						if (tempMax < Number(res.feeds[j].field3)) {
							tempMax = res.feeds[j].field3;
						}
						
						if (tempMin > Number(res.feeds[j].field3)) {
							tempMin = res.feeds[j].field3;
						}
					}
					
					// 습도 구하기 (field4 : 습도)
					if (res.feeds[j].field4 != null) {
						humidity = res.feeds[j].field4;
					}
				}
			}

			$('#tempAvg').text('평균 ' + parseInt(tempSum / cnt) + '도');
			$('.progress-bar.bg-danger').css({width:parseInt(tempSum/cnt)});
			$('#tempGap').text(Number(tempMax)-Number(tempMin)+'도(최고-최저)');
			$('.progress-bar.bg-warning').css({width:Number(tempMax)-Number(tempMin)});
			$('#humidityText').text(humidity+'%');
			$('#humidity').css({width:Number(humidity)});
		
		},
		error : function() {
			alert('생활 환경 정보 progress bar 에러!');
		}
	});
	
	
});

function emergencyLoad(){
    $.ajax({
          url : 'https://api.thingspeak.com/channels/1757275/fields/1.json?results=2',
          type : 'get',
          dataType : "json",
          success : function(emergencyList){
             console.log(emergencyList);
             for (let i = 0; i < emergencyList.feeds.length; i++) {
                 str = emergencyList.feeds[i].created_at;
                   console.log(str);
                 let strsp = str.split('T');
                 let strspt = strsp[1].split(':');
                 strspt[2] = strspt[2].split('Z');
                 console.log(strsp);
                  if (emergencyList.feeds[i].field1 == null){
                      $("#hour").text(strsp[0] + " " + (Number(strspt[0])+Number(9)) +':' + strspt[1]);
                      console.log(emergencyList.feeds[i].created_at);
                  }//
             }//	
          },
          error: function () {
              alert('응급호출api실패');
          }
      });
  }	
  
  
  
  
</script>
	

	
	</head>



<body id="page-top">

   <%
	MemberVO vo=(MemberVO)session.getAttribute("result");
    EmergencyDAO dao = new EmergencyDAO();
    ArrayList<EmergencySenVO> list = dao.emergencyAllList(vo.getMember_id());
    System.out.print("log:"+ list);
    String result = (String)pageContext.getAttribute("result");
    //System.out.println(list.get(0).getButton_id());
    %>


    <!-- Page Wrapper -->
    <div id="wrapper">

        <!-- Sidebar -->
        <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

            <!-- Sidebar - Brand -->
            <a class="sidebar-brand d-flex align-items-center justify-content-center" href="index.jsp">
                <div class="sidebar-brand-icon rotate-n-15">
                    <i class="fas fa-laugh-wink"></i>
                </div>
                <div class="sidebar-brand-text mx-3">Mr.HERO</div>
            </a>

            <!-- Divider -->
            <hr class="sidebar-divider my-0">

            <!-- Nav Item - Dashboard -->
            <li class="nav-item active">
                <a class="nav-link" href="index.jsp">
                    <i class="fas fa-fw fa-tachometer-alt"></i>
                    <span>SMART MONITERING</span></a>
            </li>

       

            <!-- Heading -->
            <div class="sidebar-heading">
                관리 메뉴
            </div>

            <!-- Nav Item - Pages Collapse Menu -->
            

             <!-- Nav Item - Tables -->
            <li class="nav-item">
                <a class="nav-link" href="infoSenior.jsp">
                    <i class="fas fa-fw fa-table"></i>
                    <span>모니터링 명단</span></a>
            </li>
            
            <li class="nav-item">
                <a class="nav-link" href="EmerBoard.jsp">
                    <i class="fas fa-fw fa-table"></i>
                    <span>긴급 호출 리스트</span></a>
            </li>

             <!-- Nav Item - Pages Collapse Menu -->
             <li class="nav-item">
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapsePages"
                    aria-expanded="true" aria-controls="collapsePages">
                    <i class="fas fa-fw fa-folder"></i>
                    <span>모니터링 대상 등록</span>
                </a>
                <div id="collapsePages" class="collapse" aria-labelledby="headingPages" data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <a class="collapse-item" href="inputSenior.jsp">등록</a>
                    </div>
                </div>
            </li>
            

            <!-- Divider -->
            <hr class="sidebar-divider d-none d-md-block">

            <!-- Sidebar Toggler (Sidebar) -->
            <div class="text-center d-none d-md-inline">
                <button class="rounded-circle border-0" id="sidebarToggle"></button>
            </div>

        </ul>
        <!-- End of Sidebar -->

        <!-- Content Wrapper -->
        <div id="content-wrapper" class="d-flex flex-column">

            <!-- Main Content -->
            <div id="content">

                <!-- Topbar -->
                <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">

                    <!-- Sidebar Toggle (Topbar) -->
                    <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
                        <i class="fa fa-bars"></i>
                    </button>

                    <!-- Topbar Search -->
                    <div class="input-group">
                        <div class="input-group-append"></div>
                    </div>






                    <!-- Topbar Navbar -->
                    <!-- 로그인 상태o -->
                    <c:if test="${!empty result}">
                    <ul class="navbar-nav ml-auto">
                        <!-- Nav Item - Search Dropdown (Visible Only XS) -->
                        <li class="nav-item dropdown no-arrow d-sm-none">
                            <a class="nav-link dropdown-toggle" href="#" id="searchDropdown" role="button"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="fas fa-search fa-fw"></i>
                            </a>
                            <!-- Dropdown - Messages -->
                            <div class="dropdown-menu dropdown-menu-right p-3 shadow animated--grow-in"
                                aria-labelledby="searchDropdown">
                                <form class="form-inline mr-auto w-100 navbar-search">
                                    <div class="input-group">
                                        <input type="text" class="form-control bg-light border-0 small"
                                            placeholder="Search for..." aria-label="Search"
                                            aria-describedby="basic-addon2">
                                        <div class="input-group-append">
                                            <button class="btn btn-primary" type="button">
                                                <i class="fas fa-search fa-sm"></i>
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </li>

                      

                        <div class="topbar-divider d-none d-sm-block"></div>
						
                        <!-- Nav Item - User Information -->
                        <li class="nav-item dropdown no-arrow">
                            <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                
                                
                                <!-- 이름 넣는 곳 -->
                                <span class="mr-2 d-none d-lg-inline text-gray-600 small">${result.member_name}님</span>
                                <img class="img-profile rounded-circle" src="img/undraw_profile.svg">
                                
                                
                            </a>
                            <!-- Dropdown - User Information -->
                            <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
                                aria-labelledby="userDropdown">
                                <a class="dropdown-item" href="profile.jsp">
                                    <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
                                    마이페이지
                                </a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="#" data-toggle="modal" data-target="#logoutModal">
                                    <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                                    로그아웃
                                </a>
                            </div>
                        </li>
                        </ul>
                        </c:if>

   
                </nav>
                <!-- End of Topbar -->





                <!-- Begin Page Content -->
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="d-sm-flex align-items-center justify-content-between mb-4">
                        <h1 class="h3 mb-0 text-gray-800">SMART MONITERING</h1>
                        <h4><form>
                        모니터링 대상 선택  :  <select>
                        
                        	<c:forEach var="list" items="${list}">
                            <option>${list.senior_name}</option>
                            </c:forEach>
                            
                        </select>
                            일자 선택  :  <input type="date">
                        </form></h4>
                    </div>

                    <!-- Content Row -->
                    <div class="row">

                        <!-- Earnings (Monthly) Card Example -->
                        <div class="col-xl-3 col-md-6 mb-4">
                            <div class="card border-left-danger shadow h-100 py-2">
                                <div class="card-body">
                                    <div class="row no-gutters align-items-center">
                                        <div class="col mr-2">
                                            <div class="text-xs font-weight-bold text-danger text-uppercase mb-1">
                                                <h5>최근 도움 요청 시각</h5>
                                            </div>
                                            <div id="hour" class="h5 mb-0 font-weight-bold text-gray-800">
                                            </div>
                                        </div>
                                        
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Earnings (Monthly) Card Example -->
                        <div class="col-xl-3 col-md-6 mb-4">
                            <div class="card border-left-success shadow h-100 py-2">
                                <div class="card-body">
                                    <div class="row no-gutters align-items-center">
                                        <div class="col mr-2">
                                            <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                                <h5>최근 안전 확인 시각</h5>
                                            </div>
                                            <div class="h5 mb-0 font-weight-bold text-gray-800"><%=list.get(0).getEmergency_action_date()%>
                                            </div>
                                        </div>
                                       
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Earnings (Monthly) Card Example -->
                       

                        <!-- Pending Requests Card Example -->
                        <div class="col-xl-3 col-md-6 mb-4">
                            <div class="card border-left-warning shadow h-100 py-2">
                                <div class="card-body">
                                    <div class="row no-gutters align-items-center">
                                        <div class="col mr-2">
                                            <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">
                                                <h5>도움 요청 횟수(월)</h5>
                                            </div>
                                            <div class="h5 mb-0 font-weight-bold text-gray-800">18</div>
                                        </div>
                                       
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Content Row -->

                    <div class="row">

                        <!-- Area Chart -->
                        <div class="col-xl-8 col-lg-7">
                            <div class="card shadow mb-4">
                                <!-- Card Header - Dropdown -->
                                <div
                                    class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                    <h5 class="m-0 font-weight-bold text-primary">실내 활동량 그래프(일)</h5>
                                    <div class="dropdown no-arrow">
                                        <a class="dropdown-toggle" href="#" role="button" id="dropdownMenuLink"
                                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            <i class="fas fa-ellipsis-v fa-sm fa-fw text-gray-400"></i>
                                        </a>
                                        <div class="dropdown-menu dropdown-menu-right shadow animated--fade-in"
                                            aria-labelledby="dropdownMenuLink">
                                            <div class="dropdown-header">Dropdown Header:</div>
                                            <a class="dropdown-item" href="#">Action</a>
                                            <a class="dropdown-item" href="#">Another action</a>
                                            <div class="dropdown-divider"></div>
                                            <a class="dropdown-item" href="#">Something else here</a>
                                        </div>
                                    </div>
                                </div>
                                <!-- Card Body -->
                                <div class="card-body">
                                    <div class="chart-area">
                                        <canvas id="myAreaChart"></canvas>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Pie Chart -->
                        <div class="col-xl-4 col-lg-5">
                            <div class="card shadow mb-4">
                                <!-- Card Header - Dropdown -->
                                <div
                                    class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                    <h5 class="m-0 font-weight-bold text-primary">설문 응답률(월) - 80%</h5>
                                    <div class="dropdown no-arrow">
                                        <a class="dropdown-toggle" href="#" role="button" id="dropdownMenuLink"
                                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            <i class="fas fa-ellipsis-v fa-sm fa-fw text-gray-400"></i>
                                        </a>
                                        <div class="dropdown-menu dropdown-menu-right shadow animated--fade-in"
                                            aria-labelledby="dropdownMenuLink">
                                            <div class="dropdown-header">Dropdown Header:</div>
                                            <a class="dropdown-item" href="#">Action</a>
                                            <a class="dropdown-item" href="#">Another action</a>
                                            <div class="dropdown-divider"></div>
                                            <a class="dropdown-item" href="#">Something else here</a>
                                        </div>
                                    </div>
                                </div>
                                
                                
                                <!-- Card Body -->
                                <div class="card-body">
                                    <div class="chart-pie pt-4 pb-2">
                                        <canvas id="myPieChart"></canvas>
                                    </div>
                                    <div class="mt-4 text-center small">
                                        <span class="mr-2">
                                            <i class="fas fa-circle" style="color : #1cc88a"></i> 기상
                                        </span>
                                        <span class="mr-2">
                                            <i class="fas fa-circle" style="color : #f6c23e"></i> 식사
                                        </span>
                                        <span class="mr-2">
                                            <i class="fas fa-circle" style="color : #ffcccc"></i> 약
                                        </span>
                                        <span class="mr-2">
                                            <i class="fas fa-circle" style="color : #33ffdd"></i> 건강
                                        </span>
                                        <span class="mr-2">
                                            <i class="fas fa-circle" style="color : #19c2ed"></i> 운동
                                        </span>
                                        <span class="mr-2">
                                            <i class="fas fa-circle" style="color : #8e7cc3"></i> 피로감
                                        </span>
                                        <span class="mr-2">
                                            <i class="fas fa-circle" style="color : #8fce00"></i> 외부활동
                                        </span>
                                        <span class="mr-2">
                                            <i class="fas fa-circle" style="color : #ff8100"></i> 취침
                                        </span>
                                        <span class="mr-2">
                                            <i class="fas fa-circle" style="color : #003153"></i> 거부(기한초과)
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                      </div>
                    <!-- Content Row -->
					<div class="row">

						<!-- Content Column -->
						<div class="col-lg-6 mb-4">

							<!-- Project Card Example -->
							<div class="card shadow mb-4">
								<div class="card-header py-3">
									<h5 class="m-0 font-weight-bold text-primary">생활 환경 정보(일)</h5>
								</div>
								
								
								
					   <!--  생활 환경 정보 프로그레스바  -->
                        <div class="card-body">
                           <h4 class="small font-weight-bold">
                              온도<span class="float-right" id="tempAvg"></span>
                           </h4>
                           <div class="progress mb-4">
                              <div class="progress-bar bg-danger" role="progressbar"
                                 style="width: 0%" aria-valuemin="0"
                                 aria-valuemax="50"></div>
                           </div>
                           <h4 class="small font-weight-bold">
                              일교차<span class="float-right" id="tempGap"></span>
                           </h4>
                           <div class="progress mb-4">
                              <div class="progress-bar bg-warning" role="progressbar"
                                 style="width: 0%" aria-valuenow="18" aria-valuemin="0"
                                 aria-valuemax="28"></div> <!-- aria-valuemax:한국 연교차 -->
                           </div>
                           <h4 class="small font-weight-bold">
                              습도<span class="float-right" id="humidityText"></span>
                           </h4>
                           <div class="progress mb-4">
                              <div class="progress-bar" role="progressbar" id="humidity"
                                 style="width: 0%" aria-valuenow="40" aria-valuemin="0"
                                 aria-valuemax="80"></div> <!-- aria-valuemax:최고상대습도 -->
                           </div>
                          
                           
                        </div>
								
							</div>

                            <!-- Color System -->
                            <div class="row">
                                <div class="col-lg-6 mb-4" style="display: none;">
                                    <div class="card bg-primary text-white shadow">
                                        <div class="card-body">
                                            Primary
                                            <div class="text-white-50 small">#4e73df</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-6 mb-4" style="display: none;">
                                    <div class="card bg-success text-white shadow">
                                        <div class="card-body">
                                            Success
                                            <div class="text-white-50 small">#1cc88a</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-6 mb-4" style="display: none;">
                                    <div class="card bg-info text-white shadow">
                                        <div class="card-body">
                                            Info
                                            <div class="text-white-50 small">#36b9cc</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-6 mb-4" style="display: none;">
                                    <div class="card bg-warning text-white shadow">
                                        <div class="card-body">
                                            Warning
                                            <div class="text-white-50 small">#f6c23e</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-6 mb-4" style="display: none;">
                                    <div class="card bg-danger text-white shadow">
                                        <div class="card-body">
                                            Danger
                                            <div class="text-white-50 small">#e74a3b</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-6 mb-4" style="display: none;">
                                    <div class="card bg-secondary text-white shadow">
                                        <div class="card-body">
                                            Secondary
                                            <div class="text-white-50 small">#858796</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-6 mb-4" style="display: none;">
                                    <div class="card bg-light text-black shadow">
                                        <div class="card-body">
                                            Light
                                            <div class="text-black-50 small">#f8f9fc</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-6 mb-4" style="display: none;">
                                    <div class="card bg-dark text-white shadow">
                                        <div class="card-body">
                                            Dark
                                            <div class="text-white-50 small">#5a5c69</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-6 mb-4">

                            <!-- Illustrations -->
                                                 <div class="card shadow mb-4">
                        <div class="card-header py-3">
                           <h6 class="m-0 font-weight-bold text-primary">설문 응답 결과 (최근
                              3개 항목)</h6>
                        </div>
                        <div class="card-body" id="showAnswer">

                           <sup><a href="#"
                              class="btn btn-success btn-circle btn-sm"> <i
                                 class="fas fa-check"></i>
                           </a></sup>
                           <h5 style="display: inline;">&nbsp; 응답</h5>
                           &nbsp;&nbsp; <sup><a href="#"
                              class="btn btn-warning btn-circle btn-sm"> <i
                                 class="fas fa-exclamation-triangle"></i>
                           </a></sup>
                           <h5 style="display: inline;">&nbsp; 보류</h5>
                        
                           <a href="#" class="btn btn-light btn-icon-split"> <span
                              class="icon text-gray-600"> <i
                                 class="fas fa-arrow-right"></i>
                           </span> <span class="text">질문 설정 및 등록</span>
                           </a>
                        </div>
                     </div>

                            <!-- Approach -->
                            <div class="card shadow mb-4" style="display: none;">
                                <div class="card-header py-3">
                                    <h6 class="m-0 font-weight-bold text-primary">Development Approach</h6>
                                </div>
                                <div class="card-body">
                                    <p>SB Admin 2 makes extensive use of Bootstrap 4 utility classes in order to reduce
                                        CSS bloat and poor page performance. Custom CSS classes are used to create
                                        custom components and custom utility classes.</p>
                                    <p class="mb-0">Before working with this theme, you should become familiar with the
                                        Bootstrap framework, especially the utility classes.</p>
                                </div>
                            </div>

                        </div>
                    </div>

                </div>
                <!-- /.container-fluid -->

            </div>
            <!-- End of Main Content -->

            <!-- Footer -->
            <footer class="sticky-footer bg-white">
                <div class="container my-auto">
                    <div class="copyright text-center my-auto">
                        <span>Copyright &copy; Your Website 2021</span>
                    </div>
                </div>
            </footer>
            <!-- End of Footer -->

        </div>
        <!-- End of Content Wrapper -->

    </div>
    <!-- End of Page Wrapper -->

    <!-- Scroll to Top Button-->
    <a class="scroll-to-top rounded" href="#page-top">
        <i class="fas fa-angle-up"></i>
    </a>

    <!-- Logout Modal-->
    <div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">정말 로그아웃 하시겠습니까?</h5>
                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-footer">
                    <a class="btn btn-primary" href="LogoutService.do?d">예</a>
                    <button class="btn btn-secondary" type="button" data-dismiss="modal">아니오</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap core JavaScript-->
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    <!-- Core plugin JavaScript-->
    <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

    <!-- Custom scripts for all pages-->
    <script src="js/sb-admin-2.min.js"></script>

    <!-- Page level plugins -->
    <script src="vendor/chart.js/Chart.min.js"></script>

    <!-- Page level custom scripts -->
    <script src="js/demo/chart-pie-demo.js"></script>
    
    
    
	<!-- 생활환경정보 프로그레스바 js 파일로 연결 -->

	<!-- 설문 응답 결과(최근 3개 항목) -->
	<script>
		let today = new Date(); // 오늘 구하기
		let date = today.getDate(); // 오늘 '일' 구하기
		let month = today.getMonth() + 1; // 오늘 '월' 구하기
		let year = today.getFullYear(); // 오늘 '연' 구하기
		// api 내 저장된 날짜 데이터랑 형식 맞춰주기
		if (month < 10) {
			month = '0' + month;
		}
		if (date < 10) {
			date = '0' + date;
		};
		let systemDate = year + '-' + month + '-' + date;

		// api 시간이랑 비교할 질문 시간 api 시간에 맞추기(-9시간)
		let questionTime = [ (systemDate - 1) + 'T22',
				(systemDate - 1) + 'T23', systemDate + 'T00',
				systemDate + 'T02', systemDate + 'T04', systemDate + 'T05',
				systemDate + 'T06', systemDate + 'T07', systemDate + 'T10',
				systemDate + 'T11', systemDate + 'T12' ];

		// 응답 데이터 넣어줄 배열 선언
		let answerYes = [ null, null, null, null, null, null, null, null, null,
				null, null ];

		// 질문 내용 저장
		let questionData = [ '안녕히 주무셨어요?', '아침 식사 하셨나요?', '아침 약 드셨나요?',
				'지금 아픈 데가 있나요?', '점심 식사 하셨나요?', '오늘 운동은 하셨나요?', '피곤하진 않으세요?',
				'오늘 친구 만나셨나요?', '저녁 식사 하셨나요?', '저녁 약 드셨나요?', '주무실 준비 하셨나요?' ];

		$.ajax({
					type : 'get',
					url : 'https://api.thingspeak.com/channels/1757275/fields/6.json',
					success : function(res) {

						for (let i = 0; i < res.feeds.length; i++) {
							for (let j = 0; j < questionTime.length; j++) {
								// api에 들어온 데이터 저장 시간이 질문할 시간과 일치할 때(질문 내용 판단)
								if (res.feeds[i].created_at
										.includes(questionTime[j])) {
									// 대답을 했다면 1, 안했다면 0 (아직 질문시간이 안됐다면 해당 answerYes는 null-> 최근 완료된 설문 질문과 응답 판단 가능)
									if (res.feeds[i].field6 != null) {
										answerYes[j] = 1;
									} else {
										answerYes[j] = 0;
									}
								}
							}
						}

						// 모니터링 페이지에 보기 좋게 형식 바꿔주기 (한국 시간으로 맞춰주기 +9시간)
						questionTime = [ systemDate + ' 7시',
								systemDate + ' 8시', systemDate + ' 9시',
								systemDate + ' 11시', systemDate + ' 13시',
								systemDate + ' 14시', systemDate + ' 15시',
								systemDate + ' 16시', systemDate + ' 19시',
								systemDate + ' 20시', systemDate + ' 21시' ];

						// 응답 데이터(1,0) 들어온 인덱스 번호 추출 (응답 데이터 들어온 answerYes, questionTime, questionData만 사용할 수 있게 유도)
						let idx = [];
						for (let i = 0; i < answerYes.length; i++) {
							if (answerYes[i] != null) {
								idx.push(i);
							}
						}

						// 응답 데이터가 들어온 최근 3개 내용만 모니터링 페이지에 표시
						let cnt = 0;
						let i = idx.length - 1;
						while (cnt < 3) {
							if (i < 0) {
								break;
							} else {
								if (answerYes[idx[i]] == 1) {
									$('#showAnswer')
											.append(
													'<div><h3 style="display:inline;">('
															+ questionTime[idx[i]]
															+ ')'
															+ questionData[idx[i]]
															+ '&nbsp;</h3><a class="btn btn-success btn-circle btn-sm" style="display:inline;"> <i class="fas fa-check"></i></a></div>');
								} else {
									$('#showAnswer')
											.append(
													'<div><h3 style="display:inline;">('
															+ questionTime[idx[i]]
															+ ')'
															+ questionData[idx[i]]
															+ '&nbsp;</h3><a class="btn btn-warning btn-circle btn-sm" style="display:inline;"><i class="fas fa-exclamation-triangle"></i></a></div>');
								}
								cnt++;
								i--;
							}
						}
					},
					error : function() {
						alert('설문 응답 결과 가져오기 error!');
					}
				});
	</script>
    
    

<div id="myModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title"></h4>
      </div>
      <div class="modal-body">
        <p>${result1}</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>

<script>
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
</script>


</body>

</html>