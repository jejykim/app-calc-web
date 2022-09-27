<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
 	
    <!-- Required meta tags -->
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
        integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

    <!-- Custom styles -->
    <link rel="stylesheet" href="/static/assets/css/style.css">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css?family=Titillium+Web:200,300,600" rel="stylesheet">

    <!-- Ionic icons -->
    <link href="https://unpkg.com/ionicons@4.5.5/dist/css/ionicons.min.css" rel="stylesheet">
    
    <script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
    <script src="/static/assets/js/main/main.js"></script>
    
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
    
	<title>BORAS 금리 계산기</title>
	
</head>
<body>
	<section class="bg-light">
        <div class="container">
            <div class="heading-block">
                <h1>금리 계산기</h1>
                <p>지금 바로 편리한 금리 계산기를 이용하여 확인하세요!</p>
            </div>
        </div>
    </section>

    <section class="bg-dark divider">
        <div class="container">
            <form class="form topmargin-lg" action="/rate-calc/result" method="get" name="rateForm" id="rateForm">
                <div class="row">
                    <div class="col-md-3">
                        <div class="form-group">
                            <select class="form-control custom-select" id="selCarMaker" name="">
					    		<option selected="selected">차량 제조사</option>
					    	</select>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <select class="form-control custom-select" id="selCarBrand" name="">
					    		<option selected="selected">차량 종류</option>
					    	</select>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <input type="number" class="form-control text-light" id="acquisitionCost" name="acquisitionCost" value="${rateVO.acquisitionCost}" placeholder="취득원가"/>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <input type="number" class="form-control text-light" id="residualValue" name="residualValue" value="${rateVO.residualValue}" placeholder="잔존가치"/>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <input type="number" class="form-control text-light" id="advancePayment" name="advancePayment" value="${rateVO.advancePayment}" placeholder="선수금"/>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <input type="number" class="form-control text-light" id="deposit" name="deposit" value="${rateVO.deposit}" placeholder="유예금"/>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <input type="number" class="form-control text-light" id="deposit2" name="deposit2" value="${rateVO.deposit2}" placeholder="보증금"/>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <input type="number" class="form-control text-light" id="leaseMonth" name="leaseMonth" value="${rateVO.leaseMonth}" placeholder="리스기간"/>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <input type="number" class="form-control text-light" id="leaseCost" name="leaseCost" value="${rateVO.leaseCost}" placeholder="월 리스료"/>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <input type="number" class="form-control text-light" id="extra" name="extra" value="${rateVO.extra}" placeholder="자동차세"/>
                        </div>
                    </div>
                    <div class="col-md-3">
                    	<button type="button" class="primary-btn" id="btnRateCalc">금리 계산</button>
	    				<button type="button" class="secondary-btn bg-light" id="btnReset">초기화</button>
                    </div>
                </div><!-- End row -->
            </form>
        </div>
    </section>
    
</body>
</html>