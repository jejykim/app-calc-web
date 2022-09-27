package com.boras.rateCalc.controller;

import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.boras.rateCalc.api.RestApiHandler;
import com.boras.rateCalc.services.CustomerService;
import com.boras.rateCalc.util.ApiStoreHelper;
import com.boras.rateCalc.util.CalcHelper;
import com.boras.rateCalc.util.NumberHelper;
import com.boras.rateCalc.vo.CustomerVO;
import com.boras.rateCalc.vo.RateVO;

@RestController
@RequestMapping("/v1/api")
public class ApiController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

	@Autowired
	private CustomerService customerService;
	
	@Value("classpath:static/data/makerList.json")
	Resource rMakerListFile;
	
	@Value("classpath:static/data/brandList.json")
	Resource rBrandListFile;
	
	@Value("${apistore.api.store.id}")
	String kakaoClientId;
	
	@Value("${apistore.api.store.key}")
	String kakaoStoreKey;
	
	CalcHelper calcHelper;
	
	ApiStoreHelper apiStoreHelper = new ApiStoreHelper();
	
	/**
	 * 연락처 수집
	 * @param req
	 * @param customerVO
	 * @return
	 */
	@PostMapping(value = "/collect/tel")
	public ResponseEntity<JSONObject> insertCustomerTel(HttpServletRequest req, @RequestBody CustomerVO customerVO) {
		JSONObject jobj = new JSONObject();
		
		if(customerVO.getTel() == null) {
			jobj.put("code", "imsi001");
			jobj.put("msg", "연락처가 픽요합니다.");
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jobj);
		}

		try {
			customerService.insertCustomerTel(customerVO);
			
			jobj.put("result", "success");
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			jobj.put("result", "fail");
		}
		
		try {
			String customerMsg = "계약했어도 출고 전이라면 리스 금리, 총비용은 잇카에서 바꿔드리겠습니다.\r\n"
					+ "\r\n"
					+ "차량 제조사 : " + customerVO.getCarMaker() + "\r\n"
					+ "차량 종류 : " + customerVO.getCarBrand() + "\r\n"
					+ "취득원가 : " + NumberHelper.addComma(customerVO.getAcquisitionCost()) + "원\r\n"
					+ "월 리스료 : " + NumberHelper.addComma(customerVO.getLeaseCost()) + "원\r\n"
					+ "리스 기간 : " + customerVO.getLeaseMonth() + "개월\r\n"
					+ "보증금 : " + NumberHelper.addComma(customerVO.getDeposit2()) + "원\r\n"
					+ "선수금 : " + NumberHelper.addComma(customerVO.getAdvancePayment()) + "원\r\n"
					+ "잔존가치 : " + NumberHelper.addComma(customerVO.getResidualValue()) + "원\r\n"
					+ "월 자동차세 : " + NumberHelper.addComma(customerVO.getExtra()) + "원\r\n"
					+ "금리 : " + customerVO.getRate() + "%\r\n";
			
			apiStoreHelper.sendKakaoMsgDetail(
					customerVO.getTel().replaceAll("-", ""), "01058275827", "", customerMsg
					, "API003", "LMS", "잇카 리스금리계산기", customerMsg
					, "웹링크,웹링크", "리스금리계산하기 단톡방,리스금리계산하기 카페", "https://open.kakao.com/o/gM2LRkte,https://cafe.naver.com/gud154", ""
					, req, kakaoClientId, kakaoStoreKey);
			
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
			
			String mgMsg = "고객 연락처 : " +customerVO.getTel()  + "\r\n"
					+ "접수 일자 : " + simpleDateFormat.format(date) + "\r\n"
					+ "\r\n"
					+ "차량 제조사 : " + customerVO.getCarMaker() + "\r\n"
					+ "차량 종류 : " + customerVO.getCarBrand() + "\r\n"
					+ "취득원가 : " + NumberHelper.addComma(customerVO.getAcquisitionCost()) + "원\r\n"
					+ "월 리스료 : " + NumberHelper.addComma(customerVO.getLeaseCost()) + "원\r\n"
					+ "리스 기간 : " + customerVO.getLeaseMonth() + "개월\r\n"
					+ "보증금 : " + NumberHelper.addComma(customerVO.getDeposit2()) + "원\r\n"
					+ "선수금 : " + NumberHelper.addComma(customerVO.getAdvancePayment()) + "원\r\n"
					+ "잔존가치 : " + NumberHelper.addComma(customerVO.getResidualValue()) + "원\r\n"
					+ "월 자동차세 : " + NumberHelper.addComma(customerVO.getExtra()) + "원";
			
			apiStoreHelper.sendKakaoMsgDetail(
					"01058275827", "01058275827", "", mgMsg
					, "API002", "LMS", "잇카 리스금리계산기", mgMsg
					, "", "", "", ""
					, req, kakaoClientId, kakaoStoreKey);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(jobj);
	}
	
	/**
	 * 수집된 연락처 조회 (임시 - 페이징 x)
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/get/collected/tel")
	public ResponseEntity<JSONObject> selectCustomerTel(HttpServletRequest req) {
		JSONObject jobj = new JSONObject();
		
		try {
			List<CustomerVO> list = customerService.selectCustomerList();
			
			jobj.put("result", "success");
			jobj.put("list", list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			jobj.put("result", "fail");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(jobj);
	}
	
	/**
	 * 금리 계산
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/rate/calc")
	public ResponseEntity<JSONObject> rateCalc(HttpServletRequest req, @RequestBody RateVO rateVO) {
		JSONObject jobj = new JSONObject();
		
		try {
			double rate = calcHelper.rate(
					rateVO.getLeaseMonth()
					, -rateVO.getLeaseCost()-(rateVO.getAdvancePayment()/rateVO.getLeaseMonth())
					, rateVO.getAcquisitionCost()
					, -rateVO.getResidualValue()-rateVO.getDeposit())*12*100;
			
			jobj.put("result", "success");
			jobj.put("rate", rate);
			
			List<JSONObject> list = new ArrayList<>();
			int totalInterestCost = 0;
			
			long leftCharge = rateVO.getAcquisitionCost();
			
			for(int i = 0; i <= rateVO.getLeaseMonth(); i++) {
				JSONObject count = new JSONObject();

				count.put("count", i);
				count.put("leftCharge", leftCharge);
				
				if(i == 0) {
					count.put("leaseCostForMonth", 0);
					count.put("interestCost", 0);
					count.put("orgCost", 0);
					count.put("extra", 0);
					count.put("totalPayment", 0);
				}else if(i == rateVO.getLeaseMonth()) {
					double bRateTemp = rate / 100;
					
					int interestCost = (int) Math.round(leftCharge * bRateTemp / 12);
					
					totalInterestCost = totalInterestCost + interestCost;
					
					count.put("interestCost", interestCost);
					
					long orgCost = leftCharge;
					
					count.put("orgCost",orgCost);
					count.put("extra", rateVO.getExtra());
					count.put("totalPayment", orgCost + interestCost + rateVO.getExtra());
					
					count.put("leaseCostForMonth", rateVO.getLeaseCost());
					
					leftCharge = 0;
					count.put("leftCharge", leftCharge);
				}else {
					count.put("leaseCostForMonth", rateVO.getLeaseCost());
					
					double bRateTemp = rate / 100;
					
					int interestCost = (int) Math.round(leftCharge * bRateTemp / 12);
					
					totalInterestCost = totalInterestCost + interestCost;
					
					count.put("interestCost", interestCost);
					
					long orgCost = rateVO.getLeaseCost() - interestCost;
					
					count.put("orgCost",orgCost);
					count.put("extra", rateVO.getExtra());
					count.put("totalPayment", rateVO.getLeaseCost() + rateVO.getExtra());
					
					leftCharge = leftCharge - orgCost;
				}
				
				
				list.add(count);
			}
			
			jobj.put("totalInterestCost", totalInterestCost);
			
			jobj.put("list", list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			jobj.put("result", "fail");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(jobj);
	}
	
	/**
	 * 차량 제조사 조회
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/get/car_info/maker")
	public ResponseEntity<JSONObject> getCarInfoMaker(HttpServletRequest req) {
		JSONObject jobj = new JSONObject();
		
		try {
			JSONArray makerJarray = (JSONArray) new JSONParser().parse(new InputStreamReader(rMakerListFile.getInputStream(), "UTF-8"));
			
			jobj.put("result", "success");
			jobj.put("makerList", makerJarray);
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			jobj.put("result", "fail");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(jobj);
	}
	
	/**
	 * 브랜드 조회
	 * @param req
	 * @return
	 */
	@ResponseBody
	@GetMapping(value = "/get/car_info/brand/{maker}")
	public ResponseEntity<JSONObject> getCarInfoBrand(HttpServletRequest req, @PathVariable String maker) {
		JSONObject jobj = new JSONObject();
		
		if(maker == null) {
			jobj.put("code", "imsi002");
			jobj.put("msg", "제조사가 필요합니다.");
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jobj);
		}
		
		try {
			JSONObject brandJobj = (JSONObject) new JSONParser().parse(new InputStreamReader(rBrandListFile.getInputStream(), "UTF-8"));
			
			JSONArray brandJarray = (JSONArray) brandJobj.get(maker);
			
			jobj.put("result", "success");
			jobj.put("maker", maker);
			jobj.put("brandList", brandJarray);
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			jobj.put("result", "fail");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(jobj);
	}
}
