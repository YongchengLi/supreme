package workPackage;

import functionAlorithms.Algorithm;

public class CalculateQandOSNR {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Algorithm test=new Algorithm();
		
		double OSNR_TYPE_1=test.Netwton("F:/FEC_TYPE/1.csv",16,11.2);//6.69%
		double OSNR_TYPE_2=test.Netwton("F:/FEC_TYPE/2.csv",16,8.67);//6.69%
		double OSNR_TYPE_3=test.Netwton("F:/FEC_TYPE/3.csv",16,8.7);//118%
		double OSNR_TYPE_4=test.Netwton("F:/FEC_TYPE/4.csv",16,8.79);//124.48%
		double OSNR_TYPE_5=test.Netwton("F:/FEC_TYPE/5.csv",16,6.00);//123.6%
		double OSNR_TYPE_6=test.Netwton("F:/FEC_TYPE/6.csv",16,5.2);//121.2%
		double OSNR_TYPE_7=test.Netwton("F:/FEC_TYPE/7.csv",16,9.0);//113.34%
	
	    System.out.println("1: "+OSNR_TYPE_1+"  2: "+OSNR_TYPE_2+"   3: "+OSNR_TYPE_3+"  4: "+OSNR_TYPE_4+"  5: "+OSNR_TYPE_5+"  6: "+OSNR_TYPE_6+"  7: "+OSNR_TYPE_7);
		
	    
	    //double Test=test.Netwton("F:/1.csv", 16, 1.0E-12);
		//System.out.println(Test);
	}
}
