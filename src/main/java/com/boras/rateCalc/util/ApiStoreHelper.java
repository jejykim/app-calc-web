package com.boras.rateCalc.util;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boras.rateCalc.api.RestApiHandler;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ApiStoreHelper {

	private static final Logger logger = LoggerFactory.getLogger(ApiStoreHelper.class);
	
	RestApiHandler restApiHandler = new RestApiHandler();
	
	/**
	 * 카카오 알림톡 API
	 * @param phone 수신할 핸드폰 번호
	 * @param callback 발신자 전화번호
	 * @param reqdate 발송시간(없을 경우 즉시 발송)
	 * @param msg 전송할 메세지
	 * @param templatCode 카카오 알림톡 템플릿 코드
	 * @param failedType 카카오 알림톡 전송 실패 시 전송할 메시지 타입
	 * @param failedSubject 카카오 알림톡 전송 실패 시 전송할 제목
	 * @param failedMsg 카카오 알림톡 전송 실패 시 전송할 내용
	 * @param btnTypes 버튼이 여러개일때 버튼타입배열 ,(콤마)로 구분합니다.
	 * @param btnTxts 버튼이 여러개일때 버튼명배열 ,(콤마)로 구분합니다.
	 * @param btnUrls1 버튼이 여러개일때 URL1배열 ,(콤마)로 구분합니다.
	 * @param btnUrls2 버튼이 여러개일때 URL2배열 ,(콤마)로 구분합니다.
	 * @return
	 */
	public boolean sendKakaoMsgDetail(String phone, String callback, String reqdate, String msg, String templatCode
			, String failedType, String failedSubject, String failedMsg, String btnTypes, String btnTxts, String btnUrls1, String btnUrls2
			, HttpServletRequest req, String kakaoClientId, String kakaoStoreKey) {
		boolean flag = false;
		
		HttpResponse response = null;
		
		try {
			response = Unirest.post("http://api.apistore.co.kr/kko/1/msg/" + kakaoClientId)
					.header("x-waple-authorization", kakaoStoreKey)
					.field("phone", phone)
					.field("callback", callback)
					.field("reqdate", reqdate)
					.field("msg", msg)
					.field("template_code", templatCode)
					.field("failed_type", failedType)
					.field("failed_subject", failedSubject)
					.field("failed_msg", failedMsg)
					.field("btn_types", btnTypes)
					.field("btn_txts", btnTxts)
					.field("btn_urls1", btnUrls1)
					.field("btn_urls2", btnUrls2)
					.asJson();
		} catch (UnirestException e) {
			logger.error(e.getMessage());
		}
		
		if(response.getStatus() == 200) {
			flag = true;
			logger.info(response.getBody().toString());
		}else {
			logger.warn(response.getBody().toString());
		}
		
		/*
		JSONObject params = new JSONObject();
		
		params.put("PHONE", phone);
		params.put("CALLBACK", callback);
		if(!reqdate.isEmpty()) {
			params.put("REQDATE", reqdate);
		}
		params.put("MSG", msg);
		params.put("TEMPLATE_CODE", templatCode);
		params.put("FAILED_TYPE", failedType);
		params.put("FAILED_SUBJECT", failedSubject);
		params.put("FAILED_MSG", failedMsg);
		if(!btnTypes.isEmpty()) {
			params.put("BTN_TYPES", btnTypes);
		}
		if(!btnTxts.isEmpty()) {
			params.put("BTN_TXTS", btnTxts);
		}
		if(!btnUrls1.isEmpty()) {
			params.put("BTN_URLS1", btnUrls1);
		}
		if(!btnUrls2.isEmpty()) {
			params.put("BTN_URLS2", btnUrls2);
		}
		
		JSONObject repJobj = new JSONObject();
		
		repJobj = restApiHandler.sendKakaoMsg(req, params, kakaoClientId, kakaoStoreKey);
		
		if(repJobj.get("responseCode").toString() == "200") {
			flag = true;
		}else {
			logger.warn(repJobj.get("respParam").toString());
		}
		*/
		
		return flag;
	}
	
	public HttpResponse sendApiStoreRegNumber(String sendnumber, String comment, String pintype, String pincode, String kakaoClientId, String kakaoStoreKey) {
		HttpResponse response = null;
		
		try {
			response = Unirest.post("http://api.apistore.co.kr/kko/2/sendnumber/save/" + kakaoClientId)
					.header("x-waple-authorization", kakaoStoreKey)
					.field("sendnumber", sendnumber)
					.field("comment", comment)
					.field("pintype", pintype)
					.field("pincode", pincode)
					.asJson();
		} catch (UnirestException e) {
			logger.error(e.getMessage());
		}
		
		if(response.getStatus() == 200) {
			logger.info(response.getBody().toString());
		}else {
			logger.warn(response.getBody().toString());
		}
		
		return response;
	}
}
