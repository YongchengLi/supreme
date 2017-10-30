package functionAlorithms;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import Network.*;


import subgraph.LinearRoute;




public class Functions_ULL {

public static double Netwton(String fileName,int Num,double X){
		double[] x=new double[100];
		double[][] f=new double[100][100];
		ReadFile(fileName, x, f, Num);
		for(int Xi=1;Xi<Num;Xi++)
		{
			for(int Xj=Xi;Xj<Num;Xj++)
			{	
				if(Xi>1)
					f[Xi][Xj]=(f[Xi-1][Xj]-f[Xi-1][Xj-1])/(x[Xj]-x[Xj-Xi]);
				else
					f[Xi][Xj]=(f[Xi-1][Xj]-f[Xi-1][Xj-1])/(x[Xj]-x[Xj-1]);
			}
		}
		double temp=1;
		double newton=f[0][0];    
	    
		
		for(int k=1;k<=Num;k++)
	    {   temp=temp*(X-x[k-1]);
	        newton=newton+f[k][k]*temp;
	    }
	    return newton;
	}
public static void ReadFile(String fileName,double[] x,double[][] f,int Num){
	
	String[] data = new String[Num+1];
	File file = new File(fileName);
	BufferedReader bufRdr = null;
	try {
		bufRdr = new BufferedReader(new FileReader(file));
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	String line = null;
	int col = 0;
	try {
		line = bufRdr.readLine();
	} catch (IOException e) {
		e.printStackTrace();
	}
	try {
		boolean y = false;
		while((line = bufRdr.readLine()) != null){
			StringTokenizer st = new StringTokenizer(line,",");
			while (st.hasMoreTokens()){
				data[col] = st.nextToken();
				col++;
			}
			col=0;
			String name = data[0];
			if(name.equals("y")){
				y=true;						
			}
			if(!y)
			{
				int Xcounter=1;
				while(Xcounter<=Num)
				{
					x[Xcounter-1]=Double.parseDouble(data[Xcounter]);
					Xcounter++;
				}			
			}
			else{ 
				if(!(name.equals("y"))){
					int Ycounter=1;
					while(Ycounter<=Num)
					{
						f[0][Ycounter-1]=Double.parseDouble(data[Ycounter]);
						Ycounter++;
					}			
				}					
			}				
		}
	} catch (IOException e) {
		e.printStackTrace();
	}	
	try {
		bufRdr.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
}
      public void CalculateLinkOSNR(Layer layer) {
		int totalAmp_Num = 0;
		HashMap<String, Link> map = layer.getLinklist();
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			Link currentlink = (Link) (map.get(iter.next()));
			if (currentlink.getNodeA().getIndex() < currentlink.getNodeB().getIndex()) {
				int num = (int) Math.ceil(currentlink.getLength() / 80);
				int Amp_Num = num - 1;
				Amp_Num = 2 + Amp_Num;
				totalAmp_Num = totalAmp_Num + Amp_Num;
				currentlink.setAmplifierNum(Amp_Num);
		    	layer.findlink(currentlink.getNodeB(), currentlink.getNodeA()).setAmplifierNum(Amp_Num);
				double KmperAmpifier=currentlink.getLength()/num;
				double Amp_Gain=KmperAmpifier*0.25;//aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
				currentlink.setAmp_Gain(Amp_Gain);
				layer.findlink(currentlink.getNodeB(), currentlink.getNodeA()).setAmp_Gain(Amp_Gain);
				double NF0=7.12;
				double NF=0;
				
				if(Amp_Gain<16)     
       	    	{
       	 	    
       	    		NF=Netwton("EDFA_TYPE/15.CSV",23,Amp_Gain);
       		    	
       	    	}    		    
       	    	else
       		    
       	    	{
       	    		NF=Netwton("EDFA_TYPE/22.CSV",16,Amp_Gain);
       	    	}
				double LinkOSNR=0;
       	    	for(int i=1;i<=Amp_Num;i++)
       	    	{
       	    		double P_ase=0;
       	    		if(i==1){
       	    			P_ase=-58+15+NF0;     
       	    			P_ase=Math.pow(10, P_ase/10);	
       	    			P_ase=1/P_ase;                     
       	    		} 			           
       	    		else{
       	    			P_ase=-58+Amp_Gain+NF;
       	    			P_ase=Math.pow(10, P_ase/10);
       	    			P_ase=1/P_ase;
       	    		}
       	    		
   	    			LinkOSNR=LinkOSNR+1/P_ase;       
       	    	}
       	    	currentlink.setOSNR(1/LinkOSNR);              
       	    	layer.findlink(currentlink.getNodeB(), currentlink.getNodeA()).setOSNR(1/LinkOSNR);
       	    }  
        }
    }
     
      public static double CalculateRouteOSNR(LinearRoute route){
	        double RouteOSNR=0;
	    	for(Link currentlink:route.getLinklist()){  
	    		double currentLinkOSNR=0;
	    		currentLinkOSNR= CalculateLinkOSNRofRoute(currentlink);
	    		RouteOSNR+=1/currentLinkOSNR;
	    	}
	    	RouteOSNR=1/RouteOSNR;
	    	//System.out.println("OSNR in mw:"+RouteOSNR);
	    	
	    	RouteOSNR=10*Math.log10(RouteOSNR)-2.5;
	    	return RouteOSNR;
	    }
     
	public static double CalculateLinkOSNRofRoute(Link currentlink){
    	int number=(int) Math.ceil(currentlink.getLength()/80);   		
    	int Amp_Num=number-1;   		
    	Amp_Num=Amp_Num+2;	        
    	double KmperAmpifier=currentlink.getLength()/number;
    	double Amp_Gain=KmperAmpifier*0.25;
    	
    	double NF0=10.54;
    	double NF=0;
	    
    	if(Amp_Gain<16)
	    
    	{
	    
    		Netwton("EDFA_TYPE/15.CSV",23,Amp_Gain);
	    	
    	}
	    
    	else
	    
    	{
    		NF=Netwton("EDFA_TYPE/22.CSV",16,Amp_Gain);
    		
    	}
    	
	    	
    	double LinkOSNR=0;
	    
    	for(int i=1;i<=Amp_Num;i++)
	    
    	{
	    
    		double P_ase=0;
	    		
    		if(i==1){
	    		
	    			P_ase=-58+10+NF0;
	    			P_ase=Math.pow(10, P_ase/10);	
	    			
	    		} 			
	    		else{
	    			P_ase=-58+Amp_Gain+NF;
	    			P_ase=Math.pow(10, P_ase/10);
	    			
	    		}
	    		
   			
	    		LinkOSNR=LinkOSNR+P_ase;
	    	
	   
	    	
    	}
    	return 1/LinkOSNR;//  link osnr 线性
    }
	public static double CalculateLinkOSNRofRoute_ULL(Link currentlink,double fiber){
    	int number=(int) Math.ceil(currentlink.getLength()/80);   		
    	int Amp_Num=number-1;   		
    	Amp_Num=Amp_Num+2;	        
    	double KmperAmpifier=currentlink.getLength()/number;
    	double Amp_Gain=KmperAmpifier*fiber;
   
    	double NF0=10.54;
    	double NF=0;
	    
    	if(Amp_Gain<16)
	    
    	{
	    
    		NF=Netwton("EDFA_TYPE/15.CSV",23,Amp_Gain);
	    	
    	}
	    
    	else
	    
    	{
    		NF=Netwton("EDFA_TYPE/22.CSV",16,Amp_Gain);
    		
    	}
    	
	    	
    	double LinkOSNR=0;
	    
    	for(int i=1;i<=Amp_Num;i++)
	    
    	{
	    
    		double P_ase=0;
	    		
    		if(i==1){
	    		
	    			P_ase=-58+10+NF0;
	    			P_ase=Math.pow(10, P_ase/10);	
	    			
	    		} 			
	    		else{
	    			P_ase=-58+Amp_Gain+NF;
	    			P_ase=Math.pow(10, P_ase/10);
	    			
	    		}
	    		
   			
	    		LinkOSNR=LinkOSNR+P_ase;
	    	
	   
	    	
    	}
    return 1/LinkOSNR;  
    }
	public  void OutputAllRouteOSNR(Layer layer){
		System.out.println("-----------------------------");
    	HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			System.out.println("^^^^^^^^"+nodepair.getName()+"^^^^^^^^^");
			int counter=1;
			for(LinearRoute Route:nodepair.getLinearlist())
			{
				double RouteOSNR=CalculateRouteOSNR(Route);
				System.out.println("第"+counter+"条路由OSNR:"+RouteOSNR);
				counter++;
			}
			System.out.println("-----------------------------");
		   
		}
    }
}
		
	

