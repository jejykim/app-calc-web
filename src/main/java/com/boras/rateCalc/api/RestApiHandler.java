package com.boras.rateCalc.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boras.rateCalc.util.RestApiConnection;


public class RestApiHandler {

	private static final Logger logger = LoggerFactory.getLogger(RestApiHandler.class);
	
	RestApiConnection restApiConnection = new RestApiConnection();
	
	private static final String HEADER_ACCEPT_JSON = "application/json";
	private static final String HEADER_ACCEPT_ALL = "*/*";
	private static final String HEADERJSON = "application/json; charset=utf-8";
	private static final String HEADERFORM = "multipart/form-data";
	private static final String USER_AGENT_VALUE = "Mozilla/5.0";
	private static final String AUTHORIZATION_PREFIX = "Bearer ";
	private static final String KO_KR = "ko-KR";
	
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String ACCEPT = "Accept";
	private static final String USER_AGENT = "User-Agent";
	private static final String ACCEPT_LANGUAGE = "Accept-Language";
	
	public JSONObject callApi(String url, HttpServletRequest req, Map<String, Object> header,JSONObject param, boolean bGet) {
		JSONObject result = new JSONObject();
		JSONParser jp = new JSONParser();
		
		logger.info("rest api url : " + url);
		
		try {
			if(bGet) {
				result = (JSONObject) jp.parse(restApiConnection.requestHttpGET(url, header, req));
			}else {
				result = (JSONObject) jp.parse(restApiConnection.requestHttpPOST(url, param, header, req));
			}
		} catch (Exception e) {
			if(bGet) {
				logger.error("[ERROR : JSONParser]");
			}else {
				logger.error("[ERROR : JSONParser]");
			}
		}
		
		return result;
	}
	
	public JSONObject callHttpsApi(String url, HttpServletRequest req, Map<String, Object> header,JSONObject param, boolean bGet) {
		JSONObject result = new JSONObject();
		JSONParser jp = new JSONParser();
		
		logger.info("rest api url : " + url);
		
		try {
			if(bGet) {
				result = (JSONObject) jp.parse(restApiConnection.requestHttpsGET(url, header, req));
			}else {
				result = (JSONObject) jp.parse(restApiConnection.requestHttpsPOST(url, param, header, req));
			}
		} catch (Exception e) {
			if(bGet) {
				logger.error("[ERROR : JSONParser]");
			}else {
				logger.error("[ERROR : JSONParser]");
			}
		}
		
		return result;
	}
	
	// 카카오 알림톡 보내기
	public JSONObject sendKakaoMsg(HttpServletRequest req, JSONObject param, String kakaoClientId, String kakaoStoreKey) {
		JSONObject result = new JSONObject();
		Map<String, Object> header = new HashMap<String, Object>();
		header.put(CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("x-waple-authorization", kakaoStoreKey);
		
		String strUrl = "http://api.apistore.co.kr/kko/1/msg/" + kakaoClientId;
		
		result = callApi(strUrl, req, header, param, false);
		
		return result;
	}
}