<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true"
    pageEncoding="UTF-8"%>
<div class="popup" style="display: none">
    <div class="pop-warp">
        <div class="pop-head">
            <p>자동차는 바꿀 수 없지만 리스상품은 계약했어도</p>
            <h5>총 이용금액, 이자만큼은 <br> 바꿀 수 있습니다.</h5>
        </div>
        <div class="pop-body">
            <div class="num">
                <input type="text" maxlength="13" placeholder="전화번호(010-0000-0000)" id="mTel" onkeyup="Result.Tel_validation(this);">
            </div>
            <div class="check">
                <label class="control control--checkbox">연락처 수집 동의
                    <input type="checkbox" id="checkTerm"/>
                    <div class="control__indicator"></div>
                </label>
	                <span class="terms" id="spanTerms">
	                	<a class="kakao" href='https://carmart25.com/pp' target="_blank">
		                    개인정보 수집 동의 약관
	                	</a>
	                </span>
            </div>
        </div>
        <div class="pop-footer">
            <div class="btn">
                <button class="dis-btn" id="btnCancelPopup">취소</button>
                <button class="main-btn" id="btnColTel">연락처 남기기</button>
            </div>
        </div>
    </div>
</div>