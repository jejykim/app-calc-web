package com.boras.rateCalc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boras.rateCalc.vo.CustomerVO;

@Mapper
public interface CustomerMapper {

	/**
	 * 사용자 연락처 수집
	 */
	public int insertCustomerTel(CustomerVO customerVO);
	
	/**
	 * 등록된 모든 사용자 조회
	 * @return
	 */
	public List<CustomerVO> selectCustomerList();
	
}
