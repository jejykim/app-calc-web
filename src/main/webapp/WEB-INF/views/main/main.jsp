<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    
    <title>금리계산기</title>
    
    <link rel="stylesheet" href="/static/assets/css/common/common.css" />
    <link rel="stylesheet" href="https://fonts.googleapis.com/earlyaccess/nanumgothic.css" />
    
    <script type="text/javascript">
		var sCarMaker = "${rateVO.carMaker }";
		var sCarBrand = "${rateVO.carBrand }";
    </script>
    
    <!-- <scrtip src="/static/assets/js/common/jquery-3.3.1.min.js"></scrtip> -->
    <script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
    <script src="/static/assets/js/main/main.js"></script>
    
</head>

<body>
    <div class="wrap">
        <header>
            <div class="main-banner">
                <div class="contents">
                    <div class="logo"><i class="i-logo"></i><span>금리계산기</span></div>
                    <div class="banner">
                        지금 바로 편리한<br>
                        <strong class="f-ye">금리계산기</strong>를 <br>
                        이용하여 확인하세요!
                    </div>
                </div>
            </div>
        </header>
        <section>
     		<form action="/rate-calc/result" method="get" name="rateForm" id="rateForm">
	            <div class="input-box">
	                <div class="line">
	                    <select class="form-control custom-select" id="selCarMaker" name="carMaker">
				    		<option selected="selected">차량 제조사</option>
				    	</select>
	                </div>
	                <div class="line">
	                    <select class="form-control custom-select" id="selCarBrand" name="carBrand">
				    		<option selected="selected">차량 종류 (제조사를 선택해주세요)</option>
				    	</select>
	                </div>
	                <div class="line">
	                    <span class="title"><b class="f-red">*</b> 취득원가</span>
	                    <span class="num"><input type="text" maxlength="15" onkeyup="Main.comma(this);" onfocus="this.select()" placeholder="0" id="acquisitionCost" name="acquisitionCost" value="<fmt:formatNumber value="${rateVO.acquisitionCost}" pattern="#,###"/>"><span>원</span></span>
	                </div>
	                <div class="line">
	                    <span class="title"><b class="f-red">*</b> 월 리스료</span>
	                    <span class="num"><input type="text" maxlength="15" onkeyup="Main.comma(this);" onfocus="this.select()" placeholder="0" id="leaseCost" name="leaseCost" value="<fmt:formatNumber value="${rateVO.leaseCost}" pattern="#,###"/>"><span>원</span></span>
	                </div>
	                <div class="line">
	                    <span class="title"><b class="f-red">*</b> 리스 기간</span>
	                    <span class="num">
	                    	<select class="form-control custom-select" id="leaseMonth" name="leaseMonth">
					    		<option value="12" <c:if test="${rateVO.leaseMonth eq 12 }">selected="selected"</c:if>>12 개월 </option>
					    		<option value="24" <c:if test="${rateVO.leaseMonth eq 24 }">selected="selected"</c:if>>24 개월 </option>
					    		<option value="36" <c:if test="${rateVO.leaseMonth eq 36 }">selected="selected"</c:if>>36 개월 </option>
					    		<option value="48" <c:if test="${rateVO.leaseMonth eq 48 }">selected="selected"</c:if>>48 개월 </option>
					    		<option value="60" <c:if test="${rateVO.leaseMonth eq 60 }">selected="selected"</c:if>>60 개월 </option>
					    	</select>
				    	</span>
				    	<!-- <span class="num"> -->
	                    <%-- <input type="text" maxlength="2" onkeyup="Main.comma(this);" onfocus="this.select()" placeholder="0" id="leaseMonth" name="leaseMonth" value="<fmt:formatNumber value="${rateVO.leaseMonth}" pattern="#,###"/>"> --%>
	                    <!-- <span>개월</span></span> -->
	                </div>
	                <div class="line">
	                    <span class="title">보증금</span>
	                    <span class="num"><input type="text" maxlength="15" onkeyup="Main.comma(this);" onfocus="this.select()" placeholder="0" id="deposit2" name="deposit2" value="<fmt:formatNumber value="${rateVO.deposit2}" pattern="#,###"/>"><span>원</span></span>
	                </div>
	                <%-- <div class="line">
	                    <span class="title">유예금</span>
	                    <span class="num"><input type="text" maxlength="15" onkeyup="Main.comma(this);" onfocus="this.select()" placeholder="0" id="deposit" name="deposit" value="<fmt:formatNumber value="${rateVO.deposit}" pattern="#,###"/>"><span>원</span></span>
	                </div> --%>
	                <div class="line">
	                    <span class="title">선수금</span>
	                    <span class="num"><input type="text" maxlength="15" onkeyup="Main.comma(this);" onfocus="this.select()" placeholder="0" id="advancePayment" name="advancePayment" value="<fmt:formatNumber value="${rateVO.advancePayment}" pattern="#,###"/>"><span>원</span></span>
	                </div>
	                <div class="line">
	                    <span class="title">잔존가치</span>
	                    <span class="num"><input type="text" maxlength="15" onkeyup="Main.comma(this);" onfocus="this.select()" placeholder="0" id="residualValue" name="residualValue" value="<fmt:formatNumber value="${rateVO.residualValue}" pattern="#,###"/>"><span>원</span></span>
	                </div>
	                <div class="line">
	                    <span class="title">월 자동차세</span>
	                    <span class="num"><input type="text" maxlength="15" onkeyup="Main.comma(this);" onfocus="this.select()" placeholder="0" id="extra" name="extra" value="<fmt:formatNumber value="${rateVO.extra}" pattern="#,###"/>"><span>원</span></span>
	                </div>
	            </div>
	            <div class="btn">
	                <button type="button" class="dis-btn" id="btnReset">초기화</button>
	                <button type="button" class="main-btn" id="btnRateCalc">금리 계산</button>
	            </div>
			</form>  
        </section>
        
        <!-- footer -->
        <jsp:include page="../common/footer.jsp" />
        <!-- //footer -->
        
    </div>
</body>

</html>
