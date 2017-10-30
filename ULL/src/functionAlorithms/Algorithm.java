package functionAlorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import subgraph.LinearRoute;

import Network.Layer;
import Network.Link;
import Network.Node;
import Network.Nodepair;


public class Algorithm {

	public double Netwton(String fileName,int Num,double X){
		
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
	public 	void Netwton2(String fileName,int Num,double X){
		
		double[] x=new double[100];
		double[][] f=new double[100][100];
		
		
		ReadFile(fileName, x, f, Num);
		for(int i=0;i<Num;i++)
		{
			System.out.println("x :"+x[i]+"   "+"y :"+f[0][i]);
		}
		
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
		System.out.println(newton);
	}
	public void ReadFile(String fileName,double[] x,double[][] f,int Num){
		
		String[] data = new String[Num+1];
		File file = new File(fileName);
		BufferedReader bufRdr = null;
		try {
			bufRdr = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		int col = 0;
		try {
			line = bufRdr.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//read the first title line
		//read each line of text file
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
				//read nodes
				if(!y)//node operation
				{
					int Xcounter=1;
					while(Xcounter<=Num)
					{
						x[Xcounter-1]=Double.parseDouble(data[Xcounter]);
						
						//System.out.println(Xcounter+"    "+x[Xcounter]);
						Xcounter++;
					}			
				}
				else{ //link operation
					if(!(name.equals("y"))){
						int Ycounter=1;
						while(Ycounter<=Num)
						{
							f[0][Ycounter-1]=Double.parseDouble(data[Ycounter]);
							//System.out.println(Ycounter+"   "+f[0][Ycounter]);
							Ycounter++;
						}			
					}					
				}				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		try {
			bufRdr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}

	/**
	 * 计算链路上的OSNR
	 * @param layer
	 */
	public void CalculateLinkOSNR(Layer layer)  {
    	int totalAmpifierNum=0;
    	HashMap<String,Link>map=layer.getLinklist();
        Iterator<String>iter=map.keySet().iterator();
        while(iter.hasNext()){
       	    Link currentlink=(Link)(map.get(iter.next()));
       	    if(currentlink.getNodeA().getIndex()<currentlink.getNodeB().getIndex())
       	    {
       	 
       	    	int number=(int) Math.ceil(currentlink.getCost()/80);   		
       	    	int AmpifierNumber=number-1;   		
       	    	AmpifierNumber=AmpifierNumber+2;	        
       	    	totalAmpifierNum+=AmpifierNumber;
       	    	currentlink.setAmplifierNum(AmpifierNumber);
       	    	layer.findlink(currentlink.getNodeB(), currentlink.getNodeA()).setAmplifierNum(AmpifierNumber);
       	    	//计算每个噪音//
       	    	double KmperAmpifier=currentlink.getCost()/number;
       	    	double AmpifierGain=KmperAmpifier*0.25;
       	    	currentlink.setAmpifierGain(AmpifierGain);
       	    	layer.findlink(currentlink.getNodeB(), currentlink.getNodeA()).setAmpifierGain(AmpifierGain);
       	    	Algorithm work=new Algorithm();
       	    	double NF0=7.12;
       	       // System.out.println("NF0: "+NF0);
       	    	double NF=0;
       	    	if(AmpifierGain<16)
       	    	{
       	 	    
       	    		NF=work.Netwton("F:/EDFA_TYPE/15.csv",23,AmpifierGain);
       		    	
       	    	}    		    
       	    	else
       		    
       	    	{
       	    		NF=work.Netwton("F:/EDFA_TYPE/22.csv",16,AmpifierGain);
       	    		//NF=5.46;
       	    	}
      
       	    	double LinkOSNR=0;
       	    	for(int i=1;i<=AmpifierNumber;i++)
       	    	{
       	    		double ReducedUnit=0;
       	    		if(i==1){
      	    			//转换单位过程
       	    			ReducedUnit=-58+15+NF0;
       	    			ReducedUnit=Math.pow(10, ReducedUnit/10);	
       	    			ReducedUnit=1/ReducedUnit;
       	    		} 			
       	    		else{
       	    			ReducedUnit=-58+AmpifierGain+NF;
       	    			ReducedUnit=Math.pow(10, ReducedUnit/10);
       	    			ReducedUnit=1/ReducedUnit;
       	    		}
       	    		
   	    			LinkOSNR=LinkOSNR+1/ReducedUnit;
       	    	}
       	    	currentlink.setOSNR(1/LinkOSNR);
       	    	layer.findlink(currentlink.getNodeB(), currentlink.getNodeA()).setOSNR(1/LinkOSNR);
       	    }  
        }
        System.out.println("Toatal Number of ampifier: "+totalAmpifierNum);
    }

	/**
	 * 计算路由OSNR
	 * @param route
	 */
	public double CalculateRouteOSNR(LinearRoute route)	
	{	       
		 double RouteOSNR=0;	       
		 int keyValue=0;	    
		 for(Link currentlink:route.getLinklist()){  	    
			 double currentLinkOSNR=0;
			 if(keyValue==0)
			 {
				 currentLinkOSNR= CalculateLinkOSNRofRoute(currentlink);	
				 //System.out.println("0:"+currentLinkOSNR);
			 }
			 else
			 {
				 currentLinkOSNR=currentlink.getOSNR();
	    		//System.out.println("1:"+currentLinkOSNR);
	    	
			 }	
			 keyValue++;
			 RouteOSNR+=1/currentLinkOSNR;	
		 }
		 RouteOSNR=1/RouteOSNR;
		 //System.out.println("mw OSNR:"+RouteOSNR);
		 //转换单位为dB	    
		 RouteOSNR=10*Math.log10(RouteOSNR)-2.5;  
		 return RouteOSNR;
	}

	/**
	 * 
	 * @param currentlink
	 * @return
	 */
	public static double CalculateLinkOSNRofRoute(Link currentlink){
	
    	int number=(int) Math.ceil(currentlink.getCost()/80);   		
	    	
    	int AmpifierNumber=number-1;   		
	    
    	AmpifierNumber=AmpifierNumber+2;	        
    	//计算每个噪音//
	    
    	double KmperAmpifier=currentlink.getCost()/number;
	    
    	double AmpifierGain=KmperAmpifier*0.25;
	    
    	Algorithm work=new Algorithm();

    	double NF0=10.54;
	    //System.out.println("NF0: "+NF0);
    	double NF=0;
	    
    	if(AmpifierGain<16)
	    
    	{
	    
    		NF=work.Netwton("F:/EDFA_TYPE/15.csv",23,AmpifierGain);
	    	
    	}
	    
    	else
	    
    	{
    		NF=work.Netwton("F:/EDFA_TYPE/22.csv",16,AmpifierGain);
    		//NF=5.46;
    	}
    	// System.out.println("NF: "+NF);
	    	
    	double LinkOSNR=0;
	    
    	for(int i=1;i<=AmpifierNumber;i++)
	    
    	{
	    
    		double ReducedUnit=0;
	    		
    		if(i==1){
	    			//转换单位过程
	    			ReducedUnit=-58+10+NF0;
	    			ReducedUnit=Math.pow(10, ReducedUnit/10);	
	    			ReducedUnit=1/ReducedUnit;
	    		} 			
	    		else{
	    			ReducedUnit=-58+AmpifierGain+NF;
	    			ReducedUnit=Math.pow(10, ReducedUnit/10);
	    			ReducedUnit=1/ReducedUnit;
	    		}
	    		
   			
	    		LinkOSNR=LinkOSNR+1/ReducedUnit;
	    	
	   
	    	
    	}
    	return 1/LinkOSNR;
    }

	/**
	 * 输出所有路由的的OSNR
	 * @param layer
	 */
	public  void OutputAllRouteOSNR(Layer layer){
	    	HashMap<String,Nodepair>map1=layer.getNodepairlist();
	        Iterator<String>iter1=map1.keySet().iterator();
			while(iter1.hasNext()){ 
				Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
				System.out.println("*************"+nodepair.getName()+"******************");
				int counter=1;
				for(LinearRoute Route:nodepair.getLinearlist())
				{
					double RouteOSNR=this.CalculateRouteOSNR(Route);
					//double RouteOSNR=CalculateRouteOSNRwithRandomAmpifier(Route);
					System.out.println("第"+counter+"条路由OSNR:"+RouteOSNR+"  RouteSlot: "+CalculateRouteSlot(Route));
					counter++;
				}
				System.out.println("============================================");
			   
			}
	    }

    public Nodepair FindNodepair(Layer layer, String name){
    	
    	Nodepair nodepair=null;
    	HashMap<String,Nodepair>map=layer.getNodepairlist();           
		Iterator<String>iter=map.keySet().iterator();     		
		while(iter.hasNext()){
			Nodepair currentnodepair=(Nodepair)(map.get(iter.next()));
			if(currentnodepair.getName().endsWith(name))
			{
				nodepair=currentnodepair;
				break;
			}
		}
		return nodepair;
    }
    
    
    public void InitSlotOfLayer(Layer layer, int SlotNumber){
    	
    	
    	HashMap<String,Link>map=layer.getLinklist();
    	Iterator<String>iter=map.keySet().iterator();
    	while(iter.hasNext()){
    	        Link link=(Link)(map.get(iter.next()));
    	        link.addSlot(SlotNumber);
    	}
	}
    
    /**
     * 
     * @param route
     * @return
     */
    public int CalculateRouteSlot(LinearRoute route){
    	
    	if(CalculateRouteOSNR(route)>14.5)    	
    		return 27;
    	else if(CalculateRouteOSNR(route)>12)
    		return 28;
    	else if(CalculateRouteOSNR(route)>9)
    		return 30;
		return 0;
    }
    
 
    public int TestCalculateRouteSlot(LinearRoute route){
    	
    	if(route.getLinklist().size()<2)    	
    		return 3;
    	else if(route.getLinklist().size()<3)
    		return 4;
		return 5;
    }
    
    /**
     * 分配波长
     * @param layer
     * @param route
     * @param firstindex
     */
    public void assginSlots(Layer layer,LinearRoute route,int firstindex){
    	
    	System.out.println("分配slot ");
    	int slotNum=this.CalculateRouteSlot(route);	 
    	int lastIndex=firstindex+slotNum;
    	Node currentnode=route.getNodelist().get(0);    
        for(Node node:route.getNodelist()){	
        	if(!node.getName().equals(currentnode.getName())){	
        		System.out.print(layer.findlink(currentnode, node).getName()+"   ");
        		//关闭链路上的波长	
        		for(int currentIndex=firstindex;currentIndex<lastIndex;currentIndex++)
        		{
        			layer.findlink(currentnode, node).getSlotList().get(currentIndex).setStatus(1);            		
    				layer.findlink(node, currentnode).getSlotList().get(currentIndex).setStatus(1);
    				
    				System.out.print(currentIndex+" "); 
        		}       	
				currentnode=node;
				System.out.println();
        	}    
        }
        
        //System.out.println();
    }
    
 
    public void DepartureSlots(Layer layer,LinearRoute route,int firstindex){
    	
    	System.out.println("释放slot ");
    	int slotNum=this.CalculateRouteSlot(route);	 
    	int lastIndex=firstindex+slotNum;
    	
        Node currentnode=route.getNodelist().get(0);    
        for(Node node:route.getNodelist()){	
        	if(!node.getName().equals(currentnode.getName())){	
        		System.out.print(layer.findlink(currentnode, node).getName()+"   ");
        		//关闭链路上的波长	
        		for(int currentIndex=firstindex;currentIndex<lastIndex;currentIndex++)
        		{
        			layer.findlink(currentnode, node).getSlotList().get(currentIndex).setStatus(0);            		
    				layer.findlink(node, currentnode).getSlotList().get(currentIndex).setStatus(0);
    				
    				System.out.print(currentIndex+" "); 
        		}       	
				currentnode=node;	
				System.out.println();
        	}    
        }
       // System.out.println();
    }
    
    
    /**
     * 输出所有路由的OSNR
     * @param layer
     */
    public  void OutputAllRoute(Layer layer){
    	HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			System.out.println("*************"+nodepair.getName()+"******************");
			int counter=1;
			for(LinearRoute Route:nodepair.getLinearlist())
			{
				Route.OutputRoute_link(Route);
				
				counter++;
			}
			System.out.println("============================================");
		   
		}
    }
    public void Output_link_slot_status(Layer layer){
		 
    	HashMap<String,Link>map=layer.getLinklist();
        Iterator<String>iter=map.keySet().iterator();
       
        while(iter.hasNext()){
       	 Link link=(Link)(map.get(iter.next()));
       	 
       	 int index=0;
       	 if(link.getNodeA().getIndex()<link.getNodeB().getIndex()){
       	 
       		
       		 while(index<link.getSlotList().size())
       		 {
       			 if(link.getSlotList().get(index).getStatus()==1)
       				 System.out.println(link.getName()+" "+index+"  "+link.getSlotList().get(index).getStatus());
       			 index++;
       		 }      	
       	 }
        
        }
	}
}
