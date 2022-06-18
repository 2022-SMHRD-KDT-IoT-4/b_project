<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>관리 대상 등록</title>

    <!-- Custom fonts for this template-->
    <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link
        href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
        rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="css/sb-admin-2.min.css" rel="stylesheet">

</head>

<body>

    <div class="container">

        <div class="card o-hidden border-0 shadow-lg my-5">
            <div class="card-body p-0">
                <!-- Nested Row within Card Body -->
                <div class="row">
                    <div class="col-lg-5 d-none d-lg-block bg-register-image"></div>
                    <div class="col-lg-7">
                    
                       <!-- 입력폼양식 -->
                        <div class="p-5">
                            <div class="text-center">
                                <h1 class="h4 text-gray-900 mb-4">관리 대상 등록</h1>
                            </div>
                            <!-- 소문자 대문자로 변경 -->
                            <form class="user" action="InputSeniorService.do" method="post">
                                <div class="form-group">
                                    <input type="text" class="form-control form-control-user" name="senior_name"
                                        placeholder="보호할 대상의 성함을 입력해주세요">
                                </div>
                                <div class="form-group">
                                    <input type="text" class="form-control form-control-user" name="age"
                                        placeholder="보호할 대상의 연세를 입력해주세요">
                                </div>
                            
                                  <div class="form-group row">
                                    <div class="col-sm-6 mb-3 mb-sm-0">
                                        남성 <input type="radio" class="form-control form-control-user" name="gender" value="MAN" id="gender">
                                    </div>
                                    <div class="col-sm-6">
                                        여성 <input type="radio" class="form-control form-control-user" name="gender" value="WOMAN" id="gender">
                                   </div>
                                </div>
                                
                                <div class="form-group">
                                    <input type="text" class="form-control form-control-user"
                                        name="senior_address" placeholder="보호할 대상이 거주하는 주소를 입력해주세요">
                                </div>
                                        
                                
                                <div class="form-group">
                                    <input type="text" class="form-control form-control-user"
                                        name="weight" placeholder="보호할 대상의 몸무게를 입력해주세요">
                                </div>
                                
                                <div class="form-group">
                                    <input type="text" class="form-control form-control-user"
                                        name="disease" placeholder="보호할 대상이 앓고 있는 질병을 입력해주세요">
                                </div>
                                
                                
                                <!-- a태그를 input submit 태그로 변경 -->
                                <input type = "submit" class="btn btn-primary btn-user btn-block" value = "정보 등록">
                            </form>
                            <hr>
                            <div class="text-center">
                                <a class="small" href="login.jsp">로그인</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
    
     <!-- Footer -->
            <footer class="sticky-footer bg-white">
                <div class="container my-auto">
                    <div class="copyright text-center my-auto">
                        <span>Copyright &copy; Your Website 2020</span>
                    </div>
                </div>
            </footer>
            <!-- End of Footer -->

    <!-- Bootstrap core JavaScript-->
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    <!-- Core plugin JavaScript-->
    <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

    <!-- Custom scripts for all pages-->
    <script src="js/sb-admin-2.min.js"></script>

</body>

</html>