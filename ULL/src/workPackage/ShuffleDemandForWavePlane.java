package workPackage;

import functionAlorithms.Algorithm;
import general.Constant;
import general.WavePlane;
import general.file_out_put;
import groupwork.TestSerach;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import subgraph.LinearRoute;



import Network.Layer;
import Network.Link;
import Network.Node;
import Network.Nodepair;

public class ShuffleDemandForWavePlane {

	private static int BasedHop=1; 
	private static int BasedOSNR=0;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scannerobject=new Scanner(System.in);
		System.out.println("�����ȡ��Topology�ļ���csv�ļ�����");
		String open_name = scannerobject.next();     
		
		Layer layer;
		layer=new Layer("worklayer", 0, "");
		layer.readTopology(open_name);                                        //��ȡ���˽ṹ                                   
		layer.generateNodepaairs();
		
		shortestPath(layer);//���·������
		CalculateLinkOSNR(layer);
		
		int WaveNum=80;
		
		for(int i=5;i<=5;i++)
		{
			System.out.println("======"+i+"=====");
			ArrayList<String> nodepairlist=new ArrayList<String>(1000);
			for(int j=0;j<i;j++)
			{
				HashMap<String,Nodepair>map=layer.getNodepairlist();
		        Iterator<String>iter=map.keySet().iterator();
				while(iter.hasNext()){ 
					Nodepair nodepair=(Nodepair)(map.get(iter.next()));
					nodepairlist.add(nodepair.getName());
				}
			}
			
			for(int k=0;k<nodepairlist.size();k++)
			{
				System.out.print(nodepairlist.get(k)+",");
			}
			System.out.println();
			
			int shuffleNumber=0;
			Random rand=new Random(100);
			while(shuffleNumber<1000)
			{
				
				System.out.println(shuffleNumber);
				Collections.shuffle(nodepairlist,rand);
				//���ò�ƽ�漯��
				ArrayList<WavePlane>WavePlanelist=new ArrayList<WavePlane>();
				for(int index=0;index<WaveNum;index++)
				{
				    WavePlane currentWavePlane=new WavePlane(" ", index, " ");
				    currentWavePlane.setNodelist(layer.getNodelist());
				    currentWavePlane.setLinklist(layer.getLinklist());
				    currentWavePlane.setNodepairlist(layer.getNodepairlist());
				    currentWavePlane.setAssociatedWavelength(index);
				    WavePlanelist.add(currentWavePlane);
				}
				
				WavePlaneAlgorithmBasedShuffle(layer,WavePlanelist,WaveNum,nodepairlist,i);
				
				shuffleNumber++;
			}
			
			
		}
		
		
		
		
		
		
	}
	/**
	 * ���·���㷨
	 * @param layer
	 */
	public static void shortestPath(Layer layer){
		HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			String name1=nodepair.getSrcNode().getName();
	        String name2=nodepair.getDesNode().getName();
	        LinearRoute newRoute=new LinearRoute(name1+"---"+name2,0,"",Constant.WORKING);
	        TestSerach workpathsearch=new TestSerach();
	        workpathsearch.Dijkstras(nodepair.getSrcNode(), nodepair.getDesNode(), layer, newRoute);
	        nodepair.addRoute(newRoute);
		}
	}
	
	
	/**
	 * 
	 * @param layer
	 * @param WavelengthNum
	 */
	public static void WavePlaneAlgorithmBasedShuffle(Layer layer,ArrayList<WavePlane> WavePlanelist,int WavelengthNum,ArrayList<String> nodepairlist,int DemandNum){
		
		
		for(int i=0;i<WavelengthNum;i++)
		{
		    WavePlanelist.get(i).getConstraintlinklist().removeAll( WavePlanelist.get(i).getConstraintlinklist());
		}
		
		//���ղ�ƽ����䲨��
		AssginmentForDemandBasedOnWavePlaneBasedShuffle(layer,WavePlanelist,nodepairlist,WavelengthNum,BasedHop,DemandNum);
		
	}
	
	 /**
     * ������·��OSNR
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
	       	    	//����ÿ������//
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
	      	    			//ת����λ����
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
	 * ��ƽ������㷨--����·���������ѡ��
	 * @param layer
	 * @param WavePlanelist
	 * @param DemandNum
	 * @param W_number
	 */
	public static void AssginmentForDemandBasedOnWavePlaneBasedShuffle(Layer layer,ArrayList<WavePlane> WavePlanelist,ArrayList<String> nodepairlist,int W_number,int BasedType,int DemandNum){

		file_out_put file=new file_out_put();
		double AllDemand=nodepairlist.size();
		double SumOverhead=0;
		int TotalDemand=0;
		
		int DemandType1=0;
		int DemandType2=0;
		int DemandType3=0;
		
		//int KeyValue=nodepairlist.size();
		double WeightNum=0;
		
		HashMap<String,Link>map=layer.getLinklist();
    	Iterator<String>iter=map.keySet().iterator();
    	while(iter.hasNext()){
    		Link link=(Link)(map.get(iter.next()));
    		link.add_W(W_number);
    		link.Init_W();
    	}
     		 
    		
    	for(int k=0;k<nodepairlist.size();k++)
		{
			String NodepairName=nodepairlist.get(k);
			Nodepair nodepair=layer.getNodepairlist().get(NodepairName);
	
			
			LinearRoute Route=null;//�ڲ�ƽ������Ѱ���·��
            TestSerach workSearch=new TestSerach();
			int WavelengthIndex=-1;
			
			int WaveRouteHops=1000000;
			double WaveRouteOSNR=-100000;
			int WavelengthMiddleIndex=-1;
			LinearRoute MiddleRoute=null;
			WavePlane AssginWavePlane=null;
			
			for(WavePlane currentWavePlane:WavePlanelist)
			{
				currentWavePlane.InitWavePlaneLinks();
			
				if(currentWavePlane.getConstraintlinklist().size()==0&&WavelengthMiddleIndex==-1)
				{
					//System.out.println("TEST1");
					MiddleRoute=nodepair.getLinearlist().get(0);
					WavelengthMiddleIndex=currentWavePlane.getAssociatedWavelength();
					AssginWavePlane=currentWavePlane;
					break;
				}
				else
				{
					//System.out.println("TEST2");
					LinearRoute newRoute=new LinearRoute("",	0, "", Constant.WORKING);
    			
    				
    				workSearch.Dijkstras(nodepair.getSrcNode(), nodepair.getDesNode(),currentWavePlane, newRoute);
    				
    				if(newRoute.getLinklist().size()>0&&CalculateRouteOSNR(newRoute)>=9.1)
    				{
    					if(BasedType==BasedHop)
    					{
    						if(newRoute.getLinklist().size()<WaveRouteHops)	
    						{
    							WaveRouteHops=newRoute.getLinklist().size();
    							MiddleRoute=newRoute;    						
    							WavelengthMiddleIndex=currentWavePlane.getAssociatedWavelength();   						
    							AssginWavePlane=currentWavePlane;
    						}
    					}
    					if(BasedType==BasedOSNR)
    					{
    						if(CalculateRouteOSNR(newRoute)>WaveRouteOSNR)	
    						{
    							WaveRouteOSNR=CalculateRouteOSNR(newRoute);
    							MiddleRoute=newRoute;
    							WavelengthMiddleIndex=currentWavePlane.getAssociatedWavelength();    						
    							AssginWavePlane=currentWavePlane;
    						}
    					}
    				}
    				currentWavePlane.RemoveConstrantLinks();
				}
			}
			//���·�ɲ�ƽ��ɹ��ҵ�
			if(WavelengthMiddleIndex>=0)
			{
				
				Route=MiddleRoute;
				double overhead=CalculateOverHead(Route);
				if(overhead!=0)
				{
					AssginWavePlane.addConstrantLinks(Route);
					WavelengthIndex=AssginWavePlane.getAssociatedWavelength();
					
					AssginmentWavelength_WP(layer,Route,WavelengthIndex);
    				SumOverhead=SumOverhead+overhead;
    				//System.out.println(SumOverhead+"  "+overhead);
    				WeightNum=WeightNum+nodepair.getLinearlist().get(0).getLinklist().size();
    				TotalDemand++;
    				if(CalculateRouteOSNR(Route)>=14.5)
		        		DemandType1++;
			    	else if(CalculateRouteOSNR(Route)>=12.6)
			    		DemandType2++;		
			    	else if(CalculateRouteOSNR(Route)>=9.1)
			    		DemandType3++;		
				}
			} 							
		}
    		
    	double TotalOverHead=SumOverhead;
    	//System.out.println(TotalDemand+"  "+TotalOverHead);
        //System.out.println(TotalDemand+"  "+String.format("%.8f", TotalOverHead)+"  "+String.format("%.8f", TotalDemand-TotalOverHead));
    	//file.filewrite("f:/Result_Waveplane.txt","Ӧ���ҵ������"+AllDemand+"�����ҵ������"+TotalDemand);
    	file.filewrite("f:/FECNEW/Result_1000_"+DemandNum+".csv",TotalDemand+","+DemandType1+","+DemandType2+","+DemandType3+","+String.format("%.8f", TotalOverHead)+","+String.format("%.8f", TotalDemand-TotalOverHead));
    	//file.filewrite("f:/Result_Waveplane.txt","��Ȩ�ص����ҵ������"+WeightNum);
    	//file.filewrite("f:/Result_Waveplane.txt","=================================================");
    	
	}
	
	
	/**
	 * ���������������·��䲨��
	 * @param layer
	 * @param Route
	 * @param Wavelength
	 */
	public static void AssginmentWavelength_WP(Layer layer,LinearRoute Route,int Wavelength)
	{
		
    	Node currentnode=Route.getNodelist().get(0);
	    for(Node node:Route.getNodelist()){
	    	if(!node.getName().equals(currentnode.getName())){
	    		//�ر���·�ϵĲ���
	    		
	    		layer.findlink(currentnode, node).getWC().get(Wavelength).setStatus(1);
				layer.findlink(node, currentnode).getWC().get(Wavelength).setStatus(1);
				currentnode=node;
	    	}
	    }  
	}
	
	 /**
     * ����ÿ��·���ϵ�OSNR
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
    			currentLinkOSNR= CalculateFirstLinkOSNRofRoute(currentlink);
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
    	//ת����λΪdB
    	RouteOSNR=10*Math.log10(RouteOSNR)-2.5;
    	return RouteOSNR;
    }
    
    /**
     * ����ÿ��·�ɵ�OSNR
     * @param currentlink
     * @return
     */
    public static double CalculateFirstLinkOSNRofRoute(Link currentlink){
		
    
    	int number=(int) Math.ceil(currentlink.getCost()/80);   		
	    	
    	int AmpifierNumber=number-1;   		
	    
    	AmpifierNumber=AmpifierNumber+2;	        
	    
    
	    
	    
    	//����ÿ������//
	    
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
	    			//ת����λ����
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
     * ����ҵ��ı�ͷ
     * @param Route
     * @return
     */
    public static double CalculateOverHead(LinearRoute Route){
		
    	if(CalculateRouteOSNR(Route)>=14.5)    	
    		return 0.0669;
    	//	return 0.17491749;
    	else if(CalculateRouteOSNR(Route)>=12.6)
    		return 0.1334;
    	//	return 0.17491749;
    	else if(CalculateRouteOSNR(Route)>=9.1)
    		return 0.212;
    	else
    		return 0;
    }

  public static ArrayList<String> ShuffleNodepairList(Layer layer, int demandNum){

      ArrayList<String> nodepairlist=new ArrayList<String>(1000);
	  HashMap<String,Nodepair>map=layer.getNodepairlist();
      Iterator<String>iter=map.keySet().iterator();
	  while(iter.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map.get(iter.next()));
			nodepairlist.add(nodepair.getName());
		}
	  
	  Collections.shuffle(nodepairlist);
	  
	  ArrayList<String> ReturnNodepairlist=new ArrayList<String>(1000);
	  for(int i=0;i<nodepairlist.size();i++)
	  {
		  for(int j=0;j<demandNum;j++)
		  {
			  ReturnNodepairlist.add(nodepairlist.get(i));
		  }
	  }
	  
	  return  ReturnNodepairlist;
    	
    }

  
}

