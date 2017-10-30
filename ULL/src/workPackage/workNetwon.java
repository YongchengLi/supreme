package workPackage;

import functionAlorithms.Algorithm;

public class workNetwon {
	
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			Algorithm test=new Algorithm();
		
		
			for(int x=1200;x<4000;)
			{
				double OSNR_TYPE_1=test.Netwton("F:/daihua/11.csv",9,x);//6.69%
				x=x+100;
				System.out.println(x+" "+OSNR_TYPE_1);
			}
			
			
		}
}
