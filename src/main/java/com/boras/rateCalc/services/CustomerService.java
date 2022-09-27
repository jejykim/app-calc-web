package com.boras.rateCalc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boras.rateCalc.mapper.CustomerMapper;
import com.boras.rateCalc.vo.CustomerVO;

@Service
public class CustomerService implements CustomerMapper {

	@Autowired
	CustomerMapper customerMapper;

	@Override
	public int insertCustomerTel(CustomerVO customerVO) {
		return customerMapper.insertCustomerTel(customerVO);
	}

	@Override
	public List<CustomerVO> selectCustomerList() {
		return customerMapper.selectCustomerList();
	}
	
}
