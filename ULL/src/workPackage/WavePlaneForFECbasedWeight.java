package workPackage;

import functionAlorithms.Algorithm;
import general.Constant;
import general.WavePlane;
import general.file_out_put;
import groupwork.TestSerach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import subgraph.LinearRoute;

import Network.Layer;
import Network.Link;
import Network.Node;
import Network.Nodepair;

public class WavePlaneForFECbasedWeight {

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
		for(int i=1;i<=5;i++)
			WavePlaneAlgorithm(layer,i,80);
		
		
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
	public static void WavePlaneAlgorithm(Layer layer,int DemandNum,int WavelengthNum){
		
		//���ò�ƽ�漯��
		ArrayList<WavePlane>WavePlanelist=new ArrayList<WavePlane>();
		
		for(int i=0;i<WavelengthNum;i++)
		{
		    WavePlane currentWavePlane=new WavePlane(" ", i, " ");
		    currentWavePlane.setNodelist(layer.getNodelist());
		    currentWavePlane.setLinklist(layer.getLinklist());
		    currentWavePlane.setNodepairlist(layer.getNodepairlist());
		    currentWavePlane.setAssociatedWavelength(i);
		    WavePlanelist.add(currentWavePlane);
		}
		
		//���ղ�ƽ����䲨��
		AssginmentForDemandBasedOnWavePlaneBasedWeight(layer,WavePlanelist,DemandNum,WavelengthNum);
		
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
	public static void AssginmentForDemandBasedOnWavePlaneBasedWeight(Layer layer,ArrayList<WavePlane> WavePlanelist,int DemandNum,int W_number){

		file_out_put file=new file_out_put();
		double AllDemand=(double)DemandNum*layer.getNodepairlist().size();
		double SumOverhead=0;
		int TotalDemand=0;
		int KeyValue=DemandNum;
		double WeightNum=0;
		
		HashMap<String,Link>map=layer.getLinklist();
    	Iterator<String>iter=map.keySet().iterator();
    	while(iter.hasNext()){
    		Link link=(Link)(map.get(iter.next()));
    		link.add_W(W_number);
    		link.Init_W();
    	}
    	while(KeyValue>0){   		 
    		HashMap<String,Nodepair>map2=layer.getNodepairlist();           
    		Iterator<String>iter2=map2.keySet().iterator();     		
    		while(iter2.hasNext()){
    			Nodepair nodepair=(Nodepair)(map2.get(iter2.next()));
    		
    			//System.out.println("nodepair: "+nodepair.getName());
    			LinearRoute Route=null;//�ڲ�ƽ������Ѱ���·��
                TestSerach workSearch=new TestSerach();
    			int WavelengthIndex=-1;
    			
    			int WaveRouteHops=1000000;
    			int WavelengthMiddleIndex=-1;
    			LinearRoute MiddleRoute=null;
    			WavePlane AssginWavePlane=null;
    			
    			for(WavePlane currentWavePlane:WavePlanelist)
    			{
    				currentWavePlane.InitWavePlaneLinks();
    			//	System.out.println("   "+currentWavePlane.getConstraintlinklist().size());
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
        				
        				if(newRoute.getLinklist().size()>0)
        				{
        					if(newRoute.getLinklist().size()<WaveRouteHops)
        					{
        						MiddleRoute=newRoute;
        						WavelengthMiddleIndex=currentWavePlane.getAssociatedWavelength();
        						AssginWavePlane=currentWavePlane;
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
    				}
    				
    			}
    		}
    		KeyValue--;
    	}
    	double TotalOverHead=SumOverhead;
    	//System.out.println(TotalDemand+"  "+TotalOverHead);
    	System.out.println(TotalDemand+"  "+String.format("%.8f", TotalOverHead)+"  "+String.format("%.8f", TotalDemand-TotalOverHead));
    	file.filewrite("f:/Result_Waveplane.txt","��ҵ����Ϊ"+DemandNum+"�����:");
    	file.filewrite("f:/Result_Waveplane.txt","Ӧ���ҵ������"+AllDemand+"�����ҵ������"+TotalDemand);
    	file.filewrite("f:/Result_Waveplane.txt","�ܱ�ͷ��  "+TotalOverHead);
    	file.filewrite("f:/Result_Waveplane.txt","��Ȩ�ص����ҵ������"+WeightNum);
    	file.filewrite("f:/Result_Waveplane.txt","=================================================");
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
		
    	if(CalculateRouteOSNR(Route)>14.23)    	
    		return 0.0627451;
    	//	return 0.17491749;
    	else if(CalculateRouteOSNR(Route)>12.05)
    		return 0.1531185;
    	//	return 0.17491749;
    	else if(CalculateRouteOSNR(Route)>=8.76)
    		return 0.17491749;
    	else
    		return 0;
    }
}
