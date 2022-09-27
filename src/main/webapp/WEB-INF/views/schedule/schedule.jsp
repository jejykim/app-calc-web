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
    
    <script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
    <script src="/static/assets/js/schedule/schedule.js"></script>
</head>

<body>
    <div class="wrap">
        <header>
            <div class="head">
                <div class="back">
                    <i class="i-back" id="iBack"></i>
                </div>
                <div class="title">
                    <h2>상환 스케줄표</h2>
                </div>
            </div>
        </header>
        <section>
            <div class="lease-price">
                <p>월 리스료</p>
                <p class="price"><b><fmt:formatNumber value="${rateVO.leaseCost}" pattern="#,###"/></b>원</p>
            </div>
            <div class="schedule-table">
                <table>
                	<colgroup>
                		<col width="8%">
                		<col width="20%">
                		<col width="17%">
                		<col width="14%">
                		<col width="20%">
                		<col width="">
                	</colgroup>
                    <thead>
                        <tr>
                            <th>회차</th>
                            <th>원금</th>
                            <th>이자</th>
                            <th>자동차세</th>
                            <th>납입금액</th>
                            <th>잔액</th>
                        </tr>
                    </thead>
                    <tbody>
                    	<c:forEach var="list" items="${list }" varStatus="status">
                    		<tr>
	                            <td>${list.count }</td>
	                            <td><fmt:formatNumber value="${list.orgCost}" pattern="#,###"/></td>
	                            <td><fmt:formatNumber value="${list.interestCost}" pattern="#,###"/></td>
	                            <td><fmt:formatNumber value="${list.extra}" pattern="#,###"/></td>
	                            <td><fmt:formatNumber value="${list.orgCost + list.interestCost + list.extra}" pattern="#,###"/></td>
	                            <td class="custom-padding-right"><fmt:formatNumber value="${list.leftCharge}" pattern="#,###"/></td>
	                        </tr>
                    	</c:forEach>
                    </tbody>
                </table>
            </div>
            <!-- <div class="i-down"></div> -->
        </section>
        
        <!-- footer -->
        <jsp:include page="../common/footer.jsp" />
        <!-- //footer -->
        
    </div>
</body>

</html>
