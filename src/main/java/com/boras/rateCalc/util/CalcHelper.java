package com.boras.rateCalc.util;

public class CalcHelper {

	/**
	 * rate 계산
	 * @param periods 개월
	 * @param payment (-리스료-(장기선수금/기간))
	 * @param present 취득원가 <>
	 * @param future -잔존가치-유예금
	 * @return
	 */
	public static double rate(double periods, double payment, double present, double future) {
		double rate = 0;
		
		double type = 0;
		double guess = 0.01;
		
		rate = guess;
		
		double epsMax = 1e-10;
		double iterMax = 10;
		
		double y, y0, y1, x0, x1, f, i;
		y = y0 = y1 = x0 = x1 = f = i = 0;
		
        if (Math.abs(rate) < epsMax) {
            y = present * (1 + periods * rate) + payment * (1 + rate * type) * periods + future;
        }else {
            f = (double)Math.exp((double)periods * Math.log(1 + (double)rate));
            y = present * f + payment * (1 / rate + type) * (f - 1) + future;
        }
        
        y0 = present + payment * periods + future;
        y1 = present * f + payment * (1 / rate + type) * (f - 1) + future;
        i = x0 = 0;
        x1 = rate;
        
        while ((Math.abs(y0 - y1) > epsMax) && (i < iterMax)) {
            rate = (y1 * x0 - y0 * x1) / (y1 - y0);
            x0 = x1;
            x1 = rate;
            
            if (Math.abs(rate) < epsMax) {
                y = present * (1 + periods * rate) + payment * (1 + rate * type) * periods + future;
            }else {
                f = (double)Math.exp((double)periods * Math.log(1 + (double)rate));
                y = present * f + payment * (1 / rate + type) * (f - 1) + future;
            }
            
            y0 = y1;
            y1 = y;
            ++i;
        }
		
		return rate;
	}
	
	/**
	 * IRR 계산
	 * @param arr
	 * @return
	 */
	public static int irr(double[] arr){
		double irrVal = 0.0;
		// 수익율
		double r = 0.1;
		while(true){						
		//f(x)			
		double f1 = 0;
		//f'(x)			
		double f2 = 0;
		for(int i=0;i<arr.length;i++){
			if(i != arr.length-1){
				f1 += (arr[i]/Math.pow(1+r, i));
			}				
			if(i >= 2){
				f2 += (arr[i]/Math.pow(1+r, i));
			}
		}
		// r
		double x1 = r - f1/f2;
		irrVal = x1;
		if((x1 - r) < 0.000001)
			break;
		r = x1;
		}
		//System.out.println("IRR : " + irrVal);
		return (int)(irrVal * 100);
	}
	
	public static void main(String[] args) {
		System.out.println(rate(60, -2750300-(0/60), 178414540, -50700000-0)*12*100);
	}
}
