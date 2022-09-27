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
    <script src="/static/assets/js/result/result.js"></script>
</head>

<body>
    <div class="wrap">
        <header>
            <div class="head">
                <div class="back">
                    <i class="i-back" id="iBack"></i>
                </div>
                <div class="title">
                    <h2>계산 결과</h2>
                </div>
            </div>
        </header>
        <section>
            <div class="result-contents">
                <div class="result-nav">
                    <div class="car-info">
                        <p>${rateVO.carMaker }</p>
                        <h3>${rateVO.carBrand }</h3>
                    </div>
                    <table>
                        <tr>
                            <td class="f-red">1</td>
                            <td>취득원가<br><span class="guide">&#40;차량가 + 취등록세&#41;</span></td>
                            <td class="price"><b><fmt:formatNumber value="${rateVO.acquisitionCost}" pattern="#,###"/></b>원</td>
                        </tr>
                        <tr class="sub-content" id="trLeaseDetailShow">
                            <td class="f-blu">2</td>
                            <td><fmt:formatNumber value="${rateVO.leaseMonth}" pattern="#,###"/>개월 동안의 총 리스료<i class="i-arrow"></i></td>
                            <td class="price"><b><fmt:formatNumber value="${rateVO.leaseCost * rateVO.leaseMonth}" pattern="#,###"/></b>원</td>
                        </tr>
                        <tr class="sub-nav hide" id="trLeaseDetail">
                            <td colspan="3">
                                <div>
                                    <strong>리스기간</strong>
                                    <span><b><fmt:formatNumber value="${rateVO.leaseMonth}" pattern="#,###"/></b>개월</span>
                                </div>
                                <div>
                                    <strong>월 리스료</strong>
                                    <span><b><fmt:formatNumber value="${rateVO.leaseCost}" pattern="#,###"/></b>원</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="f-blu">3</td>
                            <td>보증금</td>
                            <td class="price"><b><fmt:formatNumber value="${rateVO.deposit2}" pattern="#,###"/></b>원</td>
                        </tr>
                        <tr>
                            <td class="f-blu">4</td>
                            <td>선수금</td>
                            <td class="price"><b><fmt:formatNumber value="${rateVO.advancePayment}" pattern="#,###"/></b>원</td>
                        </tr>
                        <tr>
                            <td>5</td>
                            <td>잔존가치</td>
                            <td class="price"><b><fmt:formatNumber value="${rateVO.residualValue}" pattern="#,###"/></b>원</td>
                        </tr>
                        <tr>
                            <td class="f-blu">6</td>
                            <td>만기시 인수금액<br><span class="guide">&#40;잔존가치 - 보증금&#41;</span></td>
                            <td class="price"><b><fmt:formatNumber value="${rateVO.residualValue - rateVO.deposit2}" pattern="#,###"/></b>원</td>
                        </tr>
                        <tr>
                            <td>7</td>
                            <td>월 자동차세</td>
                            <td class="price"><b><fmt:formatNumber value="${rateVO.extra}" pattern="#,###"/></b>원</td>
                        </tr>
                        <tr class="n-bottom">
                            <td class="f-red">8</td>
                            <td>총 이용금액</td>
                            <td class="price"><b><fmt:formatNumber value="${totalUseCost}" pattern="#,###"/></b>원 <br> </td>
                        </tr>
                        <tr>
                            <td colspan="3" class="price-guide">
                                <span>&#40;2.총리스료 + 3.보증금 + 4.선수금 + 6.만기시인수금액&#41;</span>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="result-total">
                    <div class="total">
                        <h3><i class="i-money"></i>총이자</h3>
                        <p class="total-price"><b><fmt:formatNumber value="${totalInterestCost}" pattern="#,###"/></b>원</p>
                        <p class="f-red">&#40;1.총 이용금액 - 8.취득원가&#41;</p>
                    </div>
                    <div class="total-nav">
                        <table>
                            <tr>
                                <td>연금리</td>
                                <td>연 <b><fmt:formatNumber value="${rate }" pattern=".00" /></b>%</td>
                            </tr>
                            <tr>
                                <td>연 평균이자</td>
                                <td>연 <b><fmt:formatNumber value="${totalInterestCost / rateVO.leaseMonth * 12}" pattern="#,###"/></b>원</td>
                            </tr>
                            <tr>
                                <td>월 평균이자</td>
                                <td>월 <b><fmt:formatNumber value="${totalInterestCost / rateVO.leaseMonth}" pattern="#,###"/></b>원</td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
            <div class="btn">
                <button class="main-btn" id="btnPopup">금리 인하</button>
                <button class="line-btn" id="btnSchedule">상환 스케줄표</button>
            </div>
            <div class="i-down"></div>
        </section>
        
        <!-- footer -->
        <jsp:include page="../common/footer.jsp" />
        <!-- //footer -->
        
    </div>
    
    <form action="/rate-calc/schedule" method="get" name="scheduleForm" id="scheduleForm">
		<input type="hidden" name="carMaker" id="carMaker" value="${rateVO.carMaker }" >
		<input type="hidden" name="carBrand" id="carBrand" value="${rateVO.carBrand }" >
		<input type="hidden" name="leaseMonth" id="leaseMonth" value="${rateVO.leaseMonth }" >
		<input type="hidden" name="leaseCost" id="leaseCost" value="${rateVO.leaseCost }" >
		<input type="hidden" name="advancePayment" id="advancePayment" value="${rateVO.advancePayment }" >
		<input type="hidden" name="acquisitionCost" id="acquisitionCost" value="${rateVO.acquisitionCost }" >
		<input type="hidden" name="residualValue" id="residualValue" value="${rateVO.residualValue }" >
		<input type="hidden" name="deposit" id="deposit" value="${rateVO.deposit }" >
		<input type="hidden" name="deposit2" id="deposit2" value="${rateVO.deposit2 }" >
		<input type="hidden" name="extra" id="extra" value="${rateVO.extra }" >
		<input type="hidden" name="rate" id="rate" value="<fmt:formatNumber value="${rate }" pattern=".00" />" >
    </form>
    
    <!-- modal -->
    <jsp:include page="../common/modal.jsp" />
    <!-- //modal -->
    
</body>

</html>
