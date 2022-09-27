package com.boras.rateCalc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.boras.rateCalc.session.WebSessionListener;
import com.boras.rateCalc.util.CalcHelper;
import com.boras.rateCalc.vo.RateVO;

@Controller
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	CalcHelper calcHelper;

	/*
	 * context root
	 */
	@GetMapping(value = "/")
	public String root(Model model, HttpServletRequest req, HttpServletResponse resp) {
    	String result = "redirect:/rate-calc";
		return result;
	}
	
	/*
	 * main page
	 */
	@GetMapping(value = "/rate-calc")
	public String rateCalc(Model model, HttpServletRequest req, HttpServletResponse resp) {
		String result = "main/main";
		try {
			String userIp = req.getRemoteHost();
			
			try {
				if(WebSessionListener.userList.containsKey(userIp)) {
					JSONObject userInfo = (JSONObject) WebSessionListener.userList.get(userIp);
					model.addAttribute("rateVO", userInfo.get("rateVO"));
				}
			} catch (Exception e2) {
				logger.error(e2.getMessage());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
    	
		return result;
	}
	
	/*
	 * 금리계산 page
	 */
	@GetMapping(value = "/rate-calc/result")
	public String rateCalcResult(Model model, HttpServletRequest req, HttpServletResponse resp, @ModelAttribute("rateVO") RateVO rateVO) {
    	String result = "result/result";
    	
    	JSONObject jobj = new JSONObject();
    	
    	String userIp = req.getRemoteHost();
    	
    	jobj.put("rateVO", rateVO);
    	
    	WebSessionListener.userList.put(userIp, jobj);
    	
    	try {
			double rate = calcHelper.rate(
					rateVO.getLeaseMonth()
					, -rateVO.getLeaseCost()-(rateVO.getAdvancePayment()/rateVO.getLeaseMonth())
					, rateVO.getAcquisitionCost()
					, -rateVO.getResidualValue()-rateVO.getDeposit())*12*100;
			
			model.addAttribute("rate", rate);
			
			List<JSONObject> list = new ArrayList<>();
			
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
					
					count.put("interestCost", interestCost);
					
					long orgCost = rateVO.getLeaseCost() - interestCost;
					
					count.put("orgCost",orgCost);
					count.put("extra", rateVO.getExtra());
					count.put("totalPayment", rateVO.getLeaseCost() + rateVO.getExtra());
					
					leftCharge = leftCharge - orgCost;
				}
				
				
				list.add(count);
			}
			
			// 총 이용금액 ( 2.총리스료 + 3.보증금 + 4.선수금 + 6.만기시인수금액 )
			long totalUseCost = (rateVO.getLeaseCost() * rateVO.getLeaseMonth()) + rateVO.getDeposit2() + rateVO.getAdvancePayment() + (rateVO.getResidualValue() - rateVO.getDeposit2());
			
			// 총 이자
			long totalInterestCost = totalUseCost - rateVO.getAcquisitionCost();
			
			model.addAttribute("totalUseCost", totalUseCost);
			model.addAttribute("totalInterestCost", totalInterestCost);
			model.addAttribute("list", list);
			model.addAttribute("rateVO", rateVO);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
    	
		return result;
	}
	
	/*
	 * 상환스캐쥴표 page
	 */
	@GetMapping(value = "/rate-calc/schedule")
	public String rateCalcSchedule(Model model, HttpServletRequest req, HttpServletResponse resp, @ModelAttribute("rateVO") RateVO rateVO) {
    	String result = "schedule/schedule";
    	
    	JSONObject jobj = new JSONObject();
    	
    	String userIp = req.getRemoteHost();
    	
    	jobj.put("rateVO", rateVO);
    	
    	WebSessionListener.userList.put(userIp, jobj);
    	
    	try {
			double rate = calcHelper.rate(
					rateVO.getLeaseMonth()
					, -rateVO.getLeaseCost()-(rateVO.getAdvancePayment()/rateVO.getLeaseMonth())
					, rateVO.getAcquisitionCost()
					, -rateVO.getResidualValue()-rateVO.getDeposit())*12*100;
			
			model.addAttribute("rate", rate);
			
			List<JSONObject> list = new ArrayList<>();
			
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
					
					count.put("interestCost", interestCost);
					
					long orgCost = rateVO.getLeaseCost() - interestCost;
					
					count.put("orgCost",orgCost);
					count.put("extra", rateVO.getExtra());
					count.put("totalPayment", rateVO.getLeaseCost() + rateVO.getExtra());
					
					leftCharge = leftCharge - orgCost;
				}
				
				
				list.add(count);
			}
			
			// 총 이용금액 ( 2.총리스료 + 3.보증금 + 4.선수금 + 6.만기시인수금액 )
			long totalUseCost = (rateVO.getLeaseCost() * rateVO.getLeaseMonth()) + rateVO.getDeposit2() + rateVO.getAdvancePayment() + (rateVO.getResidualValue() - rateVO.getDeposit2());
			
			// 총 이자
			long totalInterestCost = totalUseCost - rateVO.getAcquisitionCost();
			
			model.addAttribute("totalUseCost", totalUseCost);
			model.addAttribute("totalInterestCost", totalInterestCost);
			model.addAttribute("list", list);
			model.addAttribute("rateVO", rateVO);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
    	
		return result;
	}
}
