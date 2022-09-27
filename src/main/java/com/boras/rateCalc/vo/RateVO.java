package com.boras.rateCalc.vo;

public class RateVO {

	// 기간
	private int leaseMonth;
	
	// 리스료
	private long leaseCost;
	
	// 장기선수금
	private long advancePayment = 0;
	
	// 취득원가
	private long acquisitionCost;
	
	// 잔존가치
	private long residualValue = 0;
	
	// 유예금
	private long deposit = 0;
	
	// 보증금
	private long deposit2 = 0;
	
	// 자동차세
	private long extra = 0;
	
	// 차량 제조사
	private String carMaker;
	
	// 차량 종류
	private String carBrand;
	
	// 금리
	private String rate;

	public int getLeaseMonth() {
		return leaseMonth;
	}

	public void setLeaseMonth(int leaseMonth) {
		this.leaseMonth = leaseMonth;
	}

	public long getLeaseCost() {
		return leaseCost;
	}

	public void setLeaseCost(long leaseCost) {
		this.leaseCost = leaseCost;
	}

	public long getAdvancePayment() {
		return advancePayment;
	}

	public void setAdvancePayment(long advancePayment) {
		this.advancePayment = advancePayment;
	}

	public long getAcquisitionCost() {
		return acquisitionCost;
	}

	public void setAcquisitionCost(long acquisitionCost) {
		this.acquisitionCost = acquisitionCost;
	}

	public long getResidualValue() {
		return residualValue;
	}

	public void setResidualValue(long residualValue) {
		this.residualValue = residualValue;
	}

	public long getDeposit() {
		return deposit;
	}

	public void setDeposit(long deposit) {
		this.deposit = deposit;
	}

	public long getExtra() {
		return extra;
	}

	public long getDeposit2() {
		return deposit2;
	}

	public void setDeposit2(long deposit2) {
		this.deposit2 = deposit2;
	}

	public String getCarMaker() {
		return carMaker;
	}

	public void setCarMaker(String carMaker) {
		this.carMaker = carMaker;
	}

	public String getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}

	public void setExtra(long extra) {
		this.extra = extra;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

}
