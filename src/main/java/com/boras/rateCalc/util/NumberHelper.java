package com.boras.rateCalc.util;

import java.text.DecimalFormat;

public class NumberHelper {

	public static String addComma(long number) {
		String rvt = "";
		
		DecimalFormat decFormat = new DecimalFormat("###,###");
		
		rvt = decFormat.format(number);
		
		return rvt;
	}
}
