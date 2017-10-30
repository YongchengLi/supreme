package workPackage;

import functionAlorithms.Algorithm;
import general.Ampifier;
import general.Constant;
import general.file_out_put;
import groupwork.TestSerach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import subgraph.LinearRoute;

import Network.Layer;
import Network.Link;
import Network.Node;
import Network.Nodepair;

public class WriteDataDoc {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
	
		Scanner scannerobject=new Scanner(System.in);
		
		System.out.println("输入读取的Topology文件（csv文件）：");
		String open_name = scannerobject.next();                              //输入拓扑文件
		
	//	System.out.println("输入数据结果写入文件（txt文件）:");
	//	String writename = scannerobject.next();                             //输入数据结果
		
		String writename = "f:/Data_new/80_5.dat";
		
		Layer layer;
		layer=new Layer("worklayer", 0, "");
		layer.readTopology(open_name);                                        //读取拓扑结构                                   
		layer.generateNodepaairs();
		System.out.println("Num of node:"+layer.getNodelist().size());
		System.out.println("Num of link:"+layer.getLink_num()/2);
	
		KshortestPath(layer);  //we set the k is 3 that there is 3 route can be found for each nodepair		
		CalculateLinkOSNR(layer);
		RemoveLowOSNRroute(layer);
		
		//RandomAmpifierofLink(layer);
		

	    //OutputAllRouteOSNR(layer);
		//OutputAllLinkGain(layer);
		
	
		//OutPutLinkInformationOfAmpifier(layer);
		

		//OutParamOfMiniNetworkOverHead(layer, writename);//测试用的网络头写入
   
 
		WriteFunction(layer,writename,80,5);
    	
	}
	
	/**
	 * write the set and parameters into the data file
	 * @param layer         the topology of test network
	 * @param writename     the output data file
	 * @param wavelengthNum the total wavelength of test network
	 * @param NumDemand     the demand number of the node pair 
	 */
	public static void WriteFunction(Layer layer, String writename, int wavelengthNum, int NumDemand){
		
		   
        /**
         * the Set  we written	
         */
		OutSetOfLink(layer, writename);                                         //写入链路集合
    	OutSetOfDemandType(layer, writename);                                      //写入节点队集合
    	OutSetOfCandidateRoute(layer, writename);                                  //写入每个节点对间路由
    	OutSetOfWavelength(wavelengthNum,writename);                                          //写入波长资源的集合
    	OutSetOfRouteTypeOne(layer, writename);
    	OutSetOfRouteTypeTwo(layer, writename);
    	OutSetOfRouteTypeThree(layer, writename);
    	
	    /**
	     * the Parameters we written
	     */
     	OutParamOfDelta(layer, writename);
     	OutParamOfRouteTypeWithOne(layer, writename);
     	OutParamOfRouteTypeWithTwo(layer, writename);
     	OutParamOfRouteTypeWithThree(layer, writename);
    	OutParamOfNumDemandBetweenNodepair(layer, writename,NumDemand);//
    	OutParamOfHopBetweenNodepair(layer, writename);
    	OutParamOfOverHead(layer, writename);
	    OutParamofOSNR(layer, writename);
	    	
	}
	
	
	
	
	
    //method for k shortest routes
    /**
     * 1,K shortest route Algorithm	 
     * @param layer
     */
	public static void KshortestPath(Layer layer){
		
		
		HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			String name1=nodepair.getSrcNode().getName();
	        String name2=nodepair.getDesNode().getName();
	      
	        LinearRoute newRoute=new LinearRoute(name1+"---"+name2,0,"",Constant.WORKING);
			
	        TestSerach workpathsearch=new TestSerach();
	        workpathsearch.Kshortest(nodepair.getSrcNode(), nodepair.getDesNode(), layer, 3, nodepair.getLinearlist());
		}
	}
	/**
	 * 2,Output K shortestRoute
	 * @param layer
	 * @param writename
	 */
	public static void OutputKshortestRoute(Layer layer,String writename){
		file_out_put file=new file_out_put();
		HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			file.filewrite(writename,"==================="+nodepair.getName()+"================");
			file.filewrite(writename,"Number of shortest Route: "+nodepair.getLinearlist().size());
			for(LinearRoute k_Route:nodepair.getLinearlist())
			{
				
				file.filewrite(writename,"**********************");
				k_Route.OutputRoute_node(k_Route, writename);
				
			}
		}
	}
	
	//method for writting Sets
	/**
	 * 3,写入链路集合
	 * @param layer
	 * @param writename
	 */
	public static void OutSetOfLink(Layer layer, String writename){
		file_out_put file=new file_out_put();

		file.filewriteContinuous(writename,"set Links :=");
		HashMap<String,Link>map=layer.getLinklist();
        Iterator<String>iter=map.keySet().iterator();
        while(iter.hasNext()){
       	    Link link=(Link)(map.get(iter.next()));
       	    if(link.getNodeA().getIndex()<link.getNodeB().getIndex())
       	    	file.filewriteContinuous(writename,link.getName()+" ");
		}
        file.filewriteContinuous(writename, ";"+"\r\n");
        file.filewriteContinuous(writename, "\r\n");
        
	}
	/**
	 * 4,写入节点对集合
	 * @param layer
	 * @param writename
	 */	
	public static void OutSetOfDemandType(Layer layer,String writename){
		file_out_put file=new file_out_put();
		
		file.filewriteContinuous(writename,"set Demand :=");
		HashMap<String,Nodepair>map=layer.getNodepairlist();
        Iterator<String>iter=map.keySet().iterator();
		while(iter.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map.get(iter.next()));
			file.filewriteContinuous(writename,nodepair.getName()+" ");
		}
		  
		file.filewriteContinuous(writename, ";"+"\r\n");
		file.filewriteContinuous(writename, "\r\n");
	}
	/**
	 * 5,写入每个节点对的路由集合
	 * @param layer
	 * @param writename
	 */
	public static void OutSetOfCandidateRoute(Layer layer, String writename)
	{
		file_out_put file=new file_out_put();
		HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			file.filewriteContinuous(writename,"set Candidate_Routes["+nodepair.getName()+"] :=");
			file.filewriteContinuous(writename, "\r\n");
			for(LinearRoute k_Route:nodepair.getLinearlist())
			{
				OutputRoute(k_Route, writename);
				file.filewriteContinuous(writename, "\r\n");
			}
			file.filewriteContinuous(writename, ";"+"\r\n");
			file.filewriteContinuous(writename, "\r\n");
		}
	}
	
	public static void OutSetOfRouteTypeOne(Layer layer, String writename)
	{
		file_out_put file=new file_out_put();
		file.filewriteContinuous(writename,"set RouteTypeOne:=");
		file.filewriteContinuous(writename, "\r\n");
		HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			
			for(LinearRoute k_Route:nodepair.getLinearlist())
			{
				if(CalculateRouteOSNR(k_Route)>=14.5)					
				{
					OutputRoute(k_Route, writename);
					file.filewriteContinuous(writename, "\r\n");
				}
			}
		}
		file.filewriteContinuous(writename, ";"+"\r\n");
		file.filewriteContinuous(writename, "\r\n");
	}
	public static void OutSetOfRouteTypeTwo(Layer layer, String writename)
	{
		file_out_put file=new file_out_put();
		file.filewriteContinuous(writename,"set RouteTypeTwo:=");
		file.filewriteContinuous(writename, "\r\n");
		HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			
			for(LinearRoute k_Route:nodepair.getLinearlist())
			{
				if(CalculateRouteOSNR(k_Route)>=12.6&&CalculateRouteOSNR(k_Route)<14.5)					
				{
					OutputRoute(k_Route, writename);
					file.filewriteContinuous(writename, "\r\n");
				}
			}
		}
		file.filewriteContinuous(writename, ";"+"\r\n");
		file.filewriteContinuous(writename, "\r\n");
	}
	public static void OutSetOfRouteTypeThree(Layer layer, String writename)
	{
		file_out_put file=new file_out_put();
		file.filewriteContinuous(writename,"set RouteTypeThree:=");
		file.filewriteContinuous(writename, "\r\n");
		HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			
			for(LinearRoute k_Route:nodepair.getLinearlist())
			{
				if(CalculateRouteOSNR(k_Route)<12.6&&CalculateRouteOSNR(k_Route)>=9.1)					
				{
					OutputRoute(k_Route, writename);
					file.filewriteContinuous(writename, "\r\n");
				}
			}
		}
		file.filewriteContinuous(writename, ";"+"\r\n");
		file.filewriteContinuous(writename, "\r\n");
	}
	
	/**
	 * 6,输出路由链路、被函数5调用
	 * @param newroute
	 * @param writename
	 */
	public static void OutputRoute(LinearRoute newroute,String writename){
		file_out_put file=new file_out_put();
	
		 Node currentnode=null;
	     int i=1;
		 for(Node node:newroute.getNodelist()){
	    	       
			 currentnode=node;
			 if(i<newroute.getNodelist().size())			
				 file.filewriteContinuous(writename,node.getName()+"-");
			 else
				 file.filewriteContinuous(writename,node.getName());   
			 i++;		           
	      }
	}

	/**
	 * 7,输出波长集合
	 * @param WavelengthNum
	 * @param writename
	 */
    public static void OutSetOfWavelength(int WavelengthNum, String writename){
    	file_out_put file=new file_out_put();

		file.filewriteContinuous(writename,"set Wavelength :=");
		for(int i=0;i<WavelengthNum;i++)
		{
			file.filewriteContinuous(writename,"W"+i+" ");
		}
		file.filewriteContinuous(writename, ";"+"\r\n");
		file.filewriteContinuous(writename, "\r\n");
    }
   
    
    
    
    //method for writting parameter
    /**
     * 1,RouteTypeWithOne参数输入文件
     * @param layer
     * @param writename
     */
    public static void OutParamOfRouteTypeWithOne(Layer layer, String writename)
    {
    	file_out_put file=new file_out_put();

		file.filewriteContinuous(writename,"param RouteTypeWithOne :=");
		file.filewrite(writename, "\r\n");
		
		
		HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			
			for(LinearRoute k_Route:nodepair.getLinearlist())
			{
				file.filewriteContinuous(writename,nodepair.getName()+",");
				OutputRoute(k_Route, writename);
				if(CalculateRouteOSNR(k_Route)>=14.5)					
				{
					file.filewriteContinuous(writename,"            1");
					
				}
				else
					{
						file.filewriteContinuous(writename,"            0");
					}
				file.filewriteContinuous(writename, "\r\n");
			}
		}
		file.filewriteContinuous(writename, ";"+"\r\n");
		file.filewriteContinuous(writename, "\r\n");
    }
    
    public static void OutParamOfRouteTypeWithTwo(Layer layer, String writename)
    {
    	file_out_put file=new file_out_put();

		file.filewriteContinuous(writename,"param RouteTypeWithTwo :=");
		file.filewrite(writename, "\r\n");
		
		
		HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			
			for(LinearRoute k_Route:nodepair.getLinearlist())
			{
				file.filewriteContinuous(writename,nodepair.getName()+",");
				OutputRoute(k_Route, writename);
				if(CalculateRouteOSNR(k_Route)>=12.6&&CalculateRouteOSNR(k_Route)<14.5)					
				{
					file.filewriteContinuous(writename,"            1");
					
				}
				else
					{
						file.filewriteContinuous(writename,"            0");
					}
				file.filewriteContinuous(writename, "\r\n");
			}
		}
		file.filewriteContinuous(writename, ";"+"\r\n");
		file.filewriteContinuous(writename, "\r\n");
    }
    
    
    public static void OutParamOfRouteTypeWithThree(Layer layer, String writename)
    {
    	file_out_put file=new file_out_put();

		file.filewriteContinuous(writename,"param RouteTypeWithThree :=");
		file.filewrite(writename, "\r\n");
		
		
		HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			
			for(LinearRoute k_Route:nodepair.getLinearlist())
			{
				file.filewriteContinuous(writename,nodepair.getName()+",");
				OutputRoute(k_Route, writename);
				if(CalculateRouteOSNR(k_Route)<12.6&&CalculateRouteOSNR(k_Route)>=9.1)					
				{
					file.filewriteContinuous(writename,"            1");
					
				}
				else
					{
						file.filewriteContinuous(writename,"            0");
					}
				file.filewriteContinuous(writename, "\r\n");
			}
		}
		file.filewriteContinuous(writename, ";"+"\r\n");
		file.filewriteContinuous(writename, "\r\n");
    }
    
    public static void OutParamOfDelta(Layer layer, String writename)
    {
    	file_out_put file=new file_out_put();

		file.filewriteContinuous(writename,"param Delta :=");
		file.filewrite(writename, "\r\n");
		HashMap<String,Link>map=layer.getLinklist();
        Iterator<String>iter=map.keySet().iterator();
        while(iter.hasNext()){
       	    Link link=(Link)(map.get(iter.next()));
       	    if(link.getNodeA().getIndex()<link.getNodeB().getIndex())
       	    {
       	    	//find the link i
       	    	HashMap<String,Nodepair>map1=layer.getNodepairlist();
       	        Iterator<String>iter1=map1.keySet().iterator();
       			while(iter1.hasNext()){ 
       				Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
       				
       				for(LinearRoute k_Route:nodepair.getLinearlist())
       				{   				 
       					file.filewriteContinuous(writename,nodepair.getName()+",");
       					OutputRoute(k_Route, writename);
       					file.filewriteContinuous(writename,",");
       					file.filewriteContinuous(writename,link.getName()+" ");
       					
       					if(LinkToRoute(layer,k_Route,link))
       					{
       						file.filewriteContinuous(writename,"            1");
       					}
       					else
       					{
       						file.filewriteContinuous(writename,"            0");
       					}
       					file.filewriteContinuous(writename, "\r\n");
       				}
       			}
       	    }
		}
        file.filewrite(writename, ";");	       
        
    }
    
    
    /**
     * 判断链路是否在路由上
     * @param layer
     * @param route
     * @param link
     * @return
     */
    public static boolean LinkToRoute(Layer layer,LinearRoute route,Link link){
    	boolean key=false;
		for(Link link1:route.getLinklist()){       	  
    		if(link1.getName().endsWith(link.getName())||link1.getName().endsWith(layer.findlink(link.getNodeB(), link.getNodeA()).getName())){
    			key=true;
    			break;	
    		}
		}
		return key;
	}
    /**
     * 写入每个节点对见业务数
     * @param layer
     * @param writename
     */
    public static void OutParamOfNumDemandBetweenNodepair(Layer layer,String writename,int numDemand){
    	file_out_put file=new file_out_put();
    	file.filewrite(writename,"param  NodepairDemand:=");
    	HashMap<String,Nodepair>map=layer.getNodepairlist();
    	Iterator<String>iter=map.keySet().iterator();
    	while(iter.hasNext()){ 
    		Nodepair nodepair=(Nodepair)(map.get(iter.next()));
    		
    		file.filewrite(writename,nodepair.getName()+" "+numDemand);
    	}
    	file.filewrite(writename, ";");
    }
    /**
     * 写入每个节点对间的权重
     * @param layer
     * @param writename
     */
    public static void OutParamOfHopBetweenNodepair(Layer layer, String writename){
    	file_out_put file=new file_out_put();
    	file.filewrite(writename,"param  Hop:=");
    	HashMap<String,Nodepair>map=layer.getNodepairlist();
    	Iterator<String>iter=map.keySet().iterator();
    	while(iter.hasNext()){ 
    		Nodepair nodepair=(Nodepair)(map.get(iter.next()));
    		
    		int Hops=nodepair.getLinearlist().get(0).getLinklist().size();
    		//找到最小的路由跳数
    		for(LinearRoute Route:nodepair.getLinearlist())
			{
    			if(Hops>=Route.getLinklist().size())
    				Hops=Route.getLinklist().size();
			}
    		file.filewrite(writename,nodepair.getName()+" "+Hops);//写入文件中
    	}
    	file.filewrite(writename, ";");
    }
    
    /**
     * overhead每个节点对间
     * @param layer
     * @param writename
     */
    public static void OutParamOfOverHead(Layer layer,String writename){
    	file_out_put file=new file_out_put();
    	file.filewrite(writename,"param  OverHead:=");
    	HashMap<String,Nodepair>map=layer.getNodepairlist();
    	Iterator<String>iter=map.keySet().iterator();
    	while(iter.hasNext()){ 
    		Nodepair nodepair=(Nodepair)(map.get(iter.next()));  				
    		for(LinearRoute k_Route:nodepair.getLinearlist())
    		{			  				 					
    			file.filewriteContinuous(writename,nodepair.getName()+",");
    			OutputRoute(k_Route, writename);
				file.filewriteContinuous(writename," ");
				file.filewrite(writename,CalcualteOverHeadByRoute(k_Route));
				
    		}
					
    	}
    	 file.filewrite(writename, ";");	       
        
    }
   
    /**
     * 给测试时用得到简单OverHead设定
     * @param layer
     * @param writename
     */
    public static void OutParamOfMiniNetworkOverHead(Layer layer,String writename){
    	file_out_put file=new file_out_put();
    	file.filewrite(writename,"param  OverHead:=");
    	HashMap<String,Nodepair>map=layer.getNodepairlist();
    	Iterator<String>iter=map.keySet().iterator();
    	while(iter.hasNext()){ 
    		Nodepair nodepair=(Nodepair)(map.get(iter.next()));  				
    		for(LinearRoute k_Route:nodepair.getLinearlist())
    		{			  				 					
    			file.filewriteContinuous(writename,nodepair.getName()+",");
    			OutputRoute(k_Route, writename);
				file.filewriteContinuous(writename," ");
				
				file.filewrite(writename,CalcualteOverHeadByRouteForMiniNetwork(k_Route));	
    		}
					
    	}
    	 file.filewrite(writename, ";");	       
        
    }
    
   /**
    * output the OSNR for each nodepair
    * @param layer
    * @param writename
    */
    public static void OutParamofOSNR(Layer layer,String writename){
    	
    	file_out_put file=new file_out_put();
    	file.filewrite(writename,"param  OSNR:=");
    	HashMap<String,Nodepair>map=layer.getNodepairlist();
    	Iterator<String>iter=map.keySet().iterator();
    	while(iter.hasNext()){ 
    		Nodepair nodepair=(Nodepair)(map.get(iter.next()));  				
    		for(LinearRoute k_Route:nodepair.getLinearlist())
    		{			  				 					
    			file.filewriteContinuous(writename,nodepair.getName()+",");
    			OutputRoute(k_Route, writename);
				file.filewriteContinuous(writename," ");
				file.filewrite(writename,CalculateRouteOSNR(k_Route));
				
    		}
					
    	}
    	 file.filewrite(writename, ";");	 
    }
    
    /**
     * 选择路由业务的overhead
     * @param route
     * @return
     */
    public static double CalcualteOverHeadByRoute(LinearRoute Route){
		
    	if(CalculateRouteOSNR(Route)>=14.5)    	
    		return 0.0669;
    	else if(CalculateRouteOSNR(Route)>=12.6)
    		return 0.1334;
    	else if(CalculateRouteOSNR(Route)>=9.1)
    		return 0.212;
    	else
    		return 0;
    }
 
    /**
     * 为小网络做的测试写入
     * @param Route
     * @return
     */
    public static double CalcualteOverHeadByRouteForMiniNetwork(LinearRoute Route){
		
    	if(Route.getLinklist().size()>=3)    	
    		return 0.3;
    	else if(Route.getLinklist().size()>=2)
    		return 0.2;
    	else
    		return 0.1;
    }
    
    /**
     * 计算每个路由上的OSNR
     * @param layer
     * @param route
     * @return
     */
    public static double CalculateRouteOSNR(LinearRoute route){
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
     * 移除无法满足OSNR要求的路由
     * @param layer
     */
    public static void RemoveLowOSNRroute(Layer layer)
    {
    	HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			//System.out.println("*************"+nodepair.getName()+"******************");
			int n=0;
			int[] counter=new int[100];
			int num=0;
			for(LinearRoute Route:nodepair.getLinearlist())
			{
				
				double RouteOSNR=CalculateRouteOSNR(Route);
				if(RouteOSNR<9.1)
				{
					counter[n]=num;
					n++;
				}
				num++;
			}
			if(n>0)
			{
				for(int i = 0;i<=n;i++)
				{
					nodepair.getLinearlist().remove(counter[i]);
				}
			}	
		}
    }
    
    /**
     * 计算每个路由的OSNR
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
	    
    	if(AmpifierGain<=15)
	    
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
     * 计算每个链路上的OSNR
     * @param layer
     */
    public static void CalculateLinkOSNR(Layer layer)
    {
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
       	    	if(AmpifierGain<=15)
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
     * 输出所有路由的OSNR
     * @param layer
     */
    public static void OutputAllRouteOSNR(Layer layer){
    	HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			System.out.println("*************"+nodepair.getName()+"******************");
			int counter=1;
			for(LinearRoute Route:nodepair.getLinearlist())
			{
				double RouteOSNR=CalculateRouteOSNR(Route);
				//double RouteOSNR=CalculateRouteOSNRwithRandomAmpifier(Route);
				System.out.println("第"+counter+"条路由OSNR:"+RouteOSNR);
				counter++;
			}
			System.out.println("============================================");
		   
		}
    }
    
    /**
     * 输出所有链路上的每个放大器的Gain
     * @param layer
     */
    public static void OutputAllLinkGain(Layer layer)
    {
    	Algorithm work=new Algorithm();
    	HashMap<String,Link>map=layer.getLinklist();
    	Iterator<String>iter=map.keySet().iterator();
    	while(iter.hasNext()){ 
            Link link=(Link)(map.get(iter.next()));
            System.out.println("link name "+link.getName());
            System.out.println("Gain "+link.getAmpifierGain());
            System.out.println("NF "+work.Netwton("F:/EDFA_TYPE/22.csv",16,link.getAmpifierGain()));
            
    	 }
    }
    
    
    public static void RandomAmpifierofLink(Layer layer)
    {
        HashMap<String,Link>map=layer.getLinklist();
        Iterator<String>iter=map.keySet().iterator();
        while(iter.hasNext()){
       	    Link currentlink=(Link)(map.get(iter.next()));
       	    ArrayList<Ampifier> ampifierlist=new ArrayList<Ampifier>();
       	 
       	    if(currentlink.getNodeA().getIndex()<currentlink.getNodeB().getIndex())
     	    {
       	     int number=(int) Math.ceil(currentlink.getCost()/80);   		
 	    	 int AmpifierNumber=number-1;   		
 	    	 AmpifierNumber=AmpifierNumber+2;	        
 	    	
 	    	double KmperAmpifier=currentlink.getCost()/number;
 	    	
 	    	Algorithm work=new Algorithm();
 	    	double LinkDistance=0;
 	    	double LinkOSNR=0;
 	    	//第一个Ampifier;
 	    	Ampifier ampifier=new Ampifier();
 	    	double NF0=7.12;
 	    	ampifier.setAmpifierKm(0);
 	    	ampifier.setAmpifierGain(15);
 	    	ampifier.setAverageAmpifierKm(KmperAmpifier);
 	    	ampifier.setNF(NF0);
 	    	ampifier.setDistance(0);
 	    	ampifierlist.add(ampifier);
 	    	
 	        //余下的Ampifiers//
 	    	Random random=new Random();	
 	    	int min=(int)KmperAmpifier-20;
 	    	int max=(int)KmperAmpifier+20;
 	    	while(LinkDistance<currentlink.getCost())
 	    	{
 	    		double NF=0;
 	    		int Currentkm = random.nextInt(max)%(max-min+1) + min;
 	    		double AmpifierGain= Currentkm*0.25;
 	    		if(AmpifierGain<16)
 	    		    NF=work.Netwton("F:/EDFA_TYPE/15.csv",23,AmpifierGain);     	 	
 	    		else if(AmpifierGain<22)
 	    			NF=work.Netwton("F:/EDFA_TYPE/22.csv",16,AmpifierGain);
 	    		else
 	    			NF=5.46;
 	    		Ampifier Newampifier=new Ampifier();
 	    		
 	    		Newampifier.setAmpifierKm(Currentkm);
 	    		Newampifier.setAmpifierGain(AmpifierGain);
 	    		Newampifier.setAverageAmpifierKm(KmperAmpifier);
 	    		Newampifier.setNF(NF);
 	    		
 	    		ampifierlist.add(Newampifier);
     	    	LinkDistance+=Currentkm;
     	    	Newampifier.setDistance(LinkDistance);
     	    	if((currentlink.getCost()-LinkDistance)<=max){
     	    		break;
     	    	}
 	    	}
 	    	//最后一个Ampifier
 	    	double Lastkm=currentlink.getCost()-LinkDistance;
 	    	double LastAmpifierGain= Lastkm*0.25;
 	        double LastNF=0;
 	    	if(LastAmpifierGain<=15)
 	    		LastNF=work.Netwton("F:/EDFA_TYPE/15.csv",23,LastAmpifierGain);     	 	
 	    	else if(LastAmpifierGain<=22)
 	    		LastNF=work.Netwton("F:/EDFA_TYPE/22.csv",16,LastAmpifierGain);
 	    	else
 	    		LastNF=5.46;
 	 
 	    	Ampifier Lastampifier=new Ampifier();
  		
     	
 	    	Lastampifier.setAmpifierKm(Lastkm);
 	    	Lastampifier.setAmpifierGain(LastAmpifierGain);
 	    	Lastampifier.setAverageAmpifierKm(KmperAmpifier);
 	    	Lastampifier.setNF(LastNF);
 	    	Lastampifier.setDistance(currentlink.getCost());
 	    	ampifierlist.add(Lastampifier);
 	    
 	    	 
 	    	currentlink.setAmpifierlist(ampifierlist);
 	    	layer.findlink(currentlink.getNodeB(), currentlink.getNodeA()).setAmpifierlist(ampifierlist);
     	    
     	    
     	    }
       	   
       	 
       	   
        }	    
    }
    
    public static double CalculateRouteOSNRwithRandomAmpifier(LinearRoute route){
    	int LinkNum=0;
    	double RouteOSNR=0;
    	for(Link currentlink:route.getLinklist()){  
    		
    		double currentLinkOSNR=0;
    		if(LinkNum==0)
    		{
    			if(currentlink.getNodeA().getIndex()<currentlink.getNodeB().getIndex())        	
        		{ 
    				
    				currentLinkOSNR=CalculateFirstLinkOSNR(currentlink);
    				 
        		}
    			else
    			{
    				currentLinkOSNR=CalculatereverseFirstLinkOSNR(currentlink);
    			}
    			RouteOSNR+=1/currentLinkOSNR;
    			LinkNum++;
    		}
    		else
    		{
    			if(currentlink.getNodeA().getIndex()<currentlink.getNodeB().getIndex())        	
        		{ 
    				currentLinkOSNR=CalculateNormalLinkOSNR(currentlink);
        		}
    			else
    			{
    				currentLinkOSNR=CalculatereverseFormalLinkOSNR(currentlink);
    			}
    			LinkNum++;
    		}
    		RouteOSNR+=1/currentLinkOSNR;
    		
    	}
    	RouteOSNR=1/RouteOSNR;
    	//System.out.println("mw OSNR:"+RouteOSNR);
    	//转换单位为dB
    	RouteOSNR=10*Math.log10(RouteOSNR)+2.5;
    	return RouteOSNR;
    }
  
    public static double  CalculateFirstLinkOSNR(Link currentLink){
	     
	  
	   int counterAmpifier=0;
	   double LinkOSNR=0;
	   for(Ampifier currentAmpifier:currentLink.getAmpifierlist()) 	    
	   {
		   double ReducedUnit=0;
		   if(counterAmpifier==0)
		   {
			   //转换单位过程
			   ReducedUnit=-58+10+10.54;
			   
			   ReducedUnit=Math.pow(10, ReducedUnit/10);	
			   ReducedUnit=1/ReducedUnit; 
			   counterAmpifier++;
		   }
		   else
		   {
			   double AmpifierGain=currentAmpifier.getAmpifierGain();
			   double NF=currentAmpifier.getNF();
			   ReducedUnit=-58+AmpifierGain+NF;
			  
			   ReducedUnit=Math.pow(10, ReducedUnit/10);
			   ReducedUnit=1/ReducedUnit;
			   counterAmpifier++;
		   }
		   LinkOSNR=LinkOSNR+1/ReducedUnit;
	   }
	   return 1/LinkOSNR;
   }
    
   
    public static double CalculateNormalLinkOSNR(Link currentLink){
	
	 
	   double LinkOSNR=0;
	   for(Ampifier currentAmpifier:currentLink.getAmpifierlist()) 	    
	   {
		   double ReducedUnit=0;
		
			   
		   double AmpifierGain=currentAmpifier.getAmpifierGain();
		   double NF=currentAmpifier.getNF();
		   ReducedUnit=-58+AmpifierGain+NF;
		  
		   ReducedUnit=Math.pow(10, ReducedUnit/10);
		   ReducedUnit=1/ReducedUnit;
		   LinkOSNR=LinkOSNR+1/ReducedUnit;
	   }
	   return 1/LinkOSNR;
   }
    
    
    public static double CalculatereverseFirstLinkOSNR(Link currentLink){

    	int counterAmpifier=0; 	   
    	double LinkOSNR=0;   	
  	    for(int num=currentLink.getAmpifierlist().size()-1;num>=0;num--){	    	
  	    		    	
  	    	double ReducedUnit=0;  	    	
  	    	if(counterAmpifier==0)
  	    	{
  	    		 //转换单位过程
 			   ReducedUnit=-58+10+10.54;
 			  
 			   ReducedUnit=Math.pow(10, ReducedUnit/10);	
 			   ReducedUnit=1/ReducedUnit; 		   
 			   counterAmpifier++; 
  	    	}
  	    	else
  	    	{
  	    		double AmpifierGain=currentLink.getAmpifierlist().get(num+1).getAmpifierGain();
  	  	        double NF=currentLink.getAmpifierlist().get(num+1).getNF(); 
  	    		ReducedUnit=-58+AmpifierGain+NF;
  	    	
  	    		ReducedUnit=Math.pow(10, ReducedUnit/10);
  	    		ReducedUnit=1/ReducedUnit; 
  	    		counterAmpifier++;	
  	    	} 	    
  		   LinkOSNR=LinkOSNR+1/ReducedUnit;
  	   }
  	   return 1/LinkOSNR;	
    }
    public static double CalculatereverseFormalLinkOSNR(Link currentLink){

    	int counterAmpifier=0; 	   
    	double LinkOSNR=0;   	
  	    for(int num=currentLink.getAmpifierlist().size()-1;num>=0;num--){	    	
  	    		    	
  	    	double ReducedUnit=0;  	    	
  	    	if(counterAmpifier==0)
  	    	{
  	    		 //转换单位过程
 			   ReducedUnit=-58+15+7.12;
 			  
 			   ReducedUnit=Math.pow(10, ReducedUnit/10);	
 			   ReducedUnit=1/ReducedUnit; 		   
 			   counterAmpifier++; 
  	    	}
  	    	else
  	    	{
  	    		double AmpifierGain=currentLink.getAmpifierlist().get(num+1).getAmpifierGain();
  	  	        double NF=currentLink.getAmpifierlist().get(num+1).getNF(); 
  	    		ReducedUnit=-58+AmpifierGain+NF;
  	    	
  	    		ReducedUnit=Math.pow(10, ReducedUnit/10);
  	    		ReducedUnit=1/ReducedUnit; 
  	    		counterAmpifier++;	
  	    	} 	    
  		   LinkOSNR=LinkOSNR+1/ReducedUnit;
  	   }
  	   return 1/LinkOSNR;	
    }
    
    public static void OutPutLinkInformationOfAmpifier(Layer layer){
    	int counter=0;  
    	HashMap<String,Link>map=layer.getLinklist();          
    	Iterator<String>iter=map.keySet().iterator();       
    	while(iter.hasNext()){       
    		Link currentlink=(Link)(map.get(iter.next()));         	    		         	
    		if(currentlink.getNodeA().getIndex()<currentlink.getNodeB().getIndex())        	
    		{
    			System.out.println("********"+currentlink.getName()+"************");
          	    int i=0;
          	    for(Ampifier currentAmpifier:currentlink.getAmpifierlist())
          	    {
          	    	 System.out.println("第"+i+"个放大器的位置"+currentAmpifier.getDistance());
          	         i++;
          	         counter++;
          	    }        	       	
    		}
    	}          
    	System.out.println("Toatal Number of ampifier: "+counter);
    }
}
