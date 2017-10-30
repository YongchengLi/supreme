package slotWorkPackage;

import functionAlorithms.Algorithm;
import general.Constant;
import general.Modulation;
import general.SlotWindow;
import general.file_out_put;
import groupwork.pathSearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

import subgraph.LinearRoute;

import Network.Layer;
import Network.LightPath;
import Network.Link;
import Network.Node;
import Network.Nodepair;

public class FreSlotFunctions {

	public ArrayList<String> GenerateDemandlist(Layer layer)
	{
		ArrayList<String> demandlist=new ArrayList<String>(200);
		HashMap<String,Nodepair>map=layer.getNodepairlist();
        Iterator<String>iter=map.keySet().iterator();
		while(iter.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map.get(iter.next()));
			demandlist.add(nodepair.getName());
		}
		return demandlist;
		
	}
	
	//****************************Sets for the Model*******************************************//
	
	/**
	 * write the links informations into the data file
	 * @param layer
	 * @param writename
	 */
	public void OutSetOfLink(Layer layer, String writename){
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
	 * write the demand informations into the data file
	 * @param layer
	 * @param writename
	 */
	public void OutSetOfDemand(Layer layer,ArrayList<String> demandlist,int DemandNum,String writename){
		file_out_put file=new file_out_put();
		file.filewriteContinuous(writename,"set Demand :=");

		int num=1;
		for(int i=0;i<demandlist.size();i++)
		{	
			String demand=demandlist.get(i);
			for(int j=0;j<DemandNum;j++)
			{
				file.filewriteContinuous(writename,demand+"_"+num+" ");
				if(num==DemandNum)
					num=1;
				else
					num++;
			}
		} 
		file.filewriteContinuous(writename, ";"+"\r\n");
		file.filewriteContinuous(writename, "\r\n");
	}
	
	/**
	 * Write the route infortmation for each nodepair
	 * @param layer
	 * @param writename
	 */
	public void OutSetOfCandidateRoute(Layer layer,ArrayList<String> demandlist,int DemandNum, String writename)
	{
		file_out_put file=new file_out_put();
		int num=1;
		for(int i=0;i<demandlist.size();i++)
		{
			String demand=demandlist.get(i);
			HashMap<String,Nodepair>map1=layer.getNodepairlist();
	        Iterator<String>iter1=map1.keySet().iterator();
			while(iter1.hasNext()){ 
				Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
				if(nodepair.getName().equals(demand))
				{
					for(int j=0;j<DemandNum;j++)
					{
						file.filewriteContinuous(writename,"set Candidate_Routes["+nodepair.getName()+"_"+num+"] :=");
						file.filewriteContinuous(writename, "\r\n");
						for(LinearRoute k_Route:nodepair.getLinearlist())
						{
							OutputRoute(k_Route, writename);
							file.filewriteContinuous(writename, "\r\n");
						}
						if(num==DemandNum)
							num=1;
						else
							num++;
						file.filewriteContinuous(writename, ";"+"\r\n");
						file.filewriteContinuous(writename, "\r\n");
					}
					
				}
				
			}
			
		}
	}
	
	/**
	 * fuctions used for last function
	 * @param newroute
	 * @param writename
	 */
	public void OutputRoute(LinearRoute newroute,String writename){
		file_out_put file=new file_out_put();

	     int i=1;
		 for(Node node:newroute.getNodelist()){
	    	       
			 if(i<newroute.getNodelist().size())			
				 file.filewriteContinuous(writename,node.getName()+"-");
			 else
				 file.filewriteContinuous(writename,node.getName());   
			 i++;		           
	      }
	}
	//****************************Sets for the Model*******************************************//
 
	//****************************Parameters for the Model*******************************************//
	/**
	 * write the slot required for each candidate path
	 * @param layer
	 * @param SlotRequiredNum
	 * @param writename
	 */
	public void OutParamOfDemandRequriedSlosNumOnPath(Layer layer,ArrayList<String> demandlist,int DemandNum,String writename){
		
		file_out_put file=new file_out_put();
    	file.filewrite(writename,"param  RequriedSlotOnPath:=");
    	file.filewriteContinuous(writename, "\r\n");
    	int num=1;
    	for(int i=0;i<demandlist.size();i++)
		{
			String demand=demandlist.get(i);
			HashMap<String,Nodepair>map1=layer.getNodepairlist();
	        Iterator<String>iter1=map1.keySet().iterator();
			while(iter1.hasNext()){ 
				Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
				if(nodepair.getName().equals(demand))
				{
					for(int j=0;j<DemandNum;j++)
					{
						for(LinearRoute k_Route:nodepair.getLinearlist())
			    		{			  				 					
			    			file.filewriteContinuous(writename,nodepair.getName()+"_"+num+",");
			    			OutputRoute(k_Route, writename);
							file.filewriteContinuous(writename," ");
				//	    	file.filewrite(writename,(int)Math.ceil(nodepair.getSlotNum()*CalcualtePathSlotRequirmentForMiniNetwork(k_Route)));		
							file.filewrite(writename,(int)Math.ceil(nodepair.getSlotNum()*CalculatedPathSlotRequirment(k_Route)));		
			
			    		}
						if(num==DemandNum)
							num=1;
						else
							num++;
					}
					
					
				}
			}
		}
    	file.filewriteContinuous(writename, ";"+"\r\n");
		file.filewriteContinuous(writename, "\r\n");
	}

	/**
	 * write the slot required for each demand
	 * @param layer
	 * @param SlotRequiredNum
	 * @param writename
	 */
	public void OutParamOfDemandRequriedSlosNumOnDemand(Layer layer,ArrayList<String> demandlist,int DemandNum,int MiddleNum,int DaynamicRange,String writename){
		
		
		file_out_put file=new file_out_put();
    	file.filewrite(writename,"param  RequriedSlotOnDemand:=");
    	file.filewriteContinuous(writename, "\r\n");
    	int num=1;
    	for(int i=0;i<demandlist.size();i++)
		{
			String demand=demandlist.get(i);
			HashMap<String,Nodepair>map=layer.getNodepairlist();
	    	Iterator<String>iter=map.keySet().iterator();
	    	while(iter.hasNext()){ 
	    		Nodepair nodepair=(Nodepair)(map.get(iter.next()));
	    		if(nodepair.getName().equals(demand))
				{
	    			for(int j=0;j<DemandNum;j++)
					{
	    				//int slotNum=GenerateRandom(MiddleNum, DaynamicRange);
	    				//int slotNum=MiddleNum;
	    				//nodepair.setSlotNum(slotNum);
	    				int slotNum=nodepair.getSlotNum();
	    				file.filewrite(writename,nodepair.getName()+"_"+num+" "+slotNum);
	    				if(num==DemandNum)
							num=1;
						else
							num++;
					}
	    			
				}
	    	}
		}
    	file.filewriteContinuous(writename, ";"+"\r\n");
		file.filewriteContinuous(writename, "\r\n");
	}
	
	/**
     * 写入每个节点对见业务数
     * @param layer
     * @param writename
     */
    public void OutParamOfCmaxDemandNumberBetweenNodepair(Layer layer,String writename,int numDemand){
    	file_out_put file=new file_out_put();
    	file.filewrite(writename,"param  CmaxDemandNumber:=");
    	HashMap<String,Nodepair>map=layer.getNodepairlist();
    	Iterator<String>iter=map.keySet().iterator();
    	while(iter.hasNext()){ 
    		Nodepair nodepair=(Nodepair)(map.get(iter.next()));
    		
    		file.filewrite(writename,nodepair.getName()+" "+numDemand);
    	}
    	file.filewrite(writename, ";");
    }
	
	/**
	 * wirte the binary for the each link compared with every path
	 * @param layer
	 * @param writename
	 */
    public void OutParamOfSigma(Layer layer, String writename)
    {
    	file_out_put file=new file_out_put();

		file.filewriteContinuous(writename,"param Sigma :=");
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
       					file.filewriteContinuous(writename,link.getName()+",");
       					file.filewriteContinuous(writename,nodepair.getName()+",");
       					OutputRoute(k_Route, writename);
       					//file.filewriteContinuous(writename,",");
       				    //file.filewriteContinuous(writename," ");
       					
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
    
    
    public void OutParamOfSigma2(Layer layer,ArrayList<String> demandlist,int DemandNum, String writename)
    {
    	file_out_put file=new file_out_put();

		file.filewriteContinuous(writename,"param Sigma :=");
		file.filewrite(writename, "\r\n");
		HashMap<String,Link>map=layer.getLinklist();
        Iterator<String>iter=map.keySet().iterator();
        while(iter.hasNext()){
       	    Link link=(Link)(map.get(iter.next()));
       	    if(link.getNodeA().getIndex()<link.getNodeB().getIndex())
       	    {
       	    	int num=1;
       	    	for(int i=0;i<demandlist.size();i++)   		
       	    	{ 			
       	    		String demand=demandlist.get(i);
        			HashMap<String,Nodepair>map1=layer.getNodepairlist();
        	    	Iterator<String>iter1=map1.keySet().iterator();
        	    	while(iter1.hasNext()){ 
        	    		Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
        	    		if(nodepair.getName().equals(demand))
        				{
        	    			for(int j=0;j<DemandNum;j++)
        					{
        	    				for(LinearRoute k_Route:nodepair.getLinearlist())
                   				{   	
                   					file.filewriteContinuous(writename,link.getName()+",");
                   					file.filewriteContinuous(writename,nodepair.getName()+"_"+num+",");
                   					OutputRoute(k_Route, writename);
                   					//file.filewriteContinuous(writename,",");
                   				    //file.filewriteContinuous(writename," ");
                   					
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
        	    				if(num==DemandNum)
        	        				num=1;
        	        			else
        	        				num++;
        					}
        				}	
        	    	}
       	    	}
       	    }
        }
        file.filewrite(writename, ";");	       
        
    }
    
    /**
	 * 7,输出波长集合
	 * @param WavelengthNum
	 * @param writename
	 */
    public void OutParamOfCmaxSlot(int CmaxSlot, String writename){
    	file_out_put file=new file_out_put();

		file.filewriteContinuous(writename,"param CmaxSlot :=");
		
		file.filewriteContinuous(writename,""+CmaxSlot);
		
		file.filewriteContinuous(writename, ";"+"\r\n");
		file.filewriteContinuous(writename, "\r\n");
    }
   
  //****************************Parameters for the Model*******************************************//
	
  
  //***************************employed functions for the Model*******************************************//
	
	
	
	/**
	 * 计算每个路由对应的slot需求
	 * @param Route
	 * @return
	 */
	public double CalculatedPathSlotRequirment(LinearRoute Route)
	{
		
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
    public double CalcualtePathSlotRequirmentForMiniNetwork(LinearRoute Route){
		
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
    public  double CalculateRouteOSNR(LinearRoute route){
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
    	//转换单位为dB
    	RouteOSNR=10*Math.log10(RouteOSNR)-2.5;
    	return RouteOSNR;
    }

    /**
     * 计算每个路由的OSNR
     * @param currentlink
     * @return
     */
    public double CalculateFirstLinkOSNRofRoute(Link currentlink){
		
    
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
     * 判断链路是否在路由上
     * @param layer
     * @param route
     * @param link
     * @return
     */
    public boolean LinkToRoute(Layer layer,LinearRoute route,Link link){
    	boolean key=false;
		for(Link link1:route.getLinklist()){       	  
    		if(link1.getName().equals(link.getName())||link1.getName().equals(layer.findlink(link.getNodeB(), link.getNodeA()).getName())){
    			key=true;
    			break;	
    		}
		}
		return key;
	}
    
    /**
     * 产生（MiddleNum-DaynamicRange，MiddleNum+DaynamicRange）随机数
     * @param MiddleNum
     * @param DaynamicRange
     * @return
     */
    public int GenerateRandom(int MiddleNum,int DaynamicRange)
    {
		int Result=0;
		Random rand=new Random();
		Result=rand.nextInt(2*DaynamicRange+1)+MiddleNum-DaynamicRange;
    	return Result;
    	
    }
    
    public int ReadSlotNum(Layer layer, String writename)
    {
		String[] data = new String[10];
		File file = new File(writename);
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
			boolean Nodepair = false;
			while((line = bufRdr.readLine()) != null){
				StringTokenizer st = new StringTokenizer(line,",");
				while (st.hasMoreTokens()){
					data[col] = st.nextToken();
					col++;
					
				}
				col=0;
				String name = data[0];
			//	System.out.println(data[0]);
				if(name.equals("Nodepair")){
					Nodepair=true;						
				}
				//read nodes
				if(Nodepair)//node operation
				{ //link operation
					if(!(name.equals("Nodepair"))){
					    Nodepair currentnodepair=layer.getNodepairlist().get(data[0]);
						int slotNum=Integer.parseInt(data[1]);
				//		System.out.println(data[0]+"  "+slotNum);
						currentnodepair.setSlotNum(slotNum);
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
		
    	
    	
    	return 0;
    }

    /**
     * Init the slot window plane
     * @param layer
     * @param CmaxSlot
     */
    public void GenerateSlot(Layer layer, int CmaxSlot){
    	
    	HashMap<String,Link>map=layer.getLinklist();
     	Iterator<String>iter=map.keySet().iterator();
     	while(iter.hasNext()){
     		Link link=(Link)(map.get(iter.next()));
     		link.addSlot(CmaxSlot);
     	}
    }

    /**
     * 初始化
     * @param layer
     * @param CmaxSlot
     */
   public void InitSlot(Layer layer, int CmaxSlot){
    	
    	HashMap<String,Link>map=layer.getLinklist();
     	Iterator<String>iter=map.keySet().iterator();
     	while(iter.hasNext()){
     		Link link=(Link)(map.get(iter.next()));
     		link.InitSlot();
     	}
    }

    
    /**
     * 搜集可使用的slotwindow集合
     * @param layer
     * @param LimitSoltNum
     * @param TotalSlotNum
     * @return
     */
    public ArrayList<SlotWindow> GenerateSlotWindow(Layer layer,int LimitSoltNum,int TotalSlotNum,Modulation mod)
    {
		 
   	 ArrayList<SlotWindow> SlotWindowList=new ArrayList<SlotWindow>(10000);
   	 
   	 for(int i=0;i<=TotalSlotNum;i++)
   	 {
   		 if(TotalSlotNum-i+1<LimitSoltNum)
   		 {
   			 break;
   		 }
   		 else
   		 {
   			 SlotWindow  NewSlotWindow=new SlotWindow("", i, "");
   			 NewSlotWindow.setFirstSlotNum(i);
   			 NewSlotWindow.setLastSlotNum(i+LimitSoltNum-1);
   			 NewSlotWindow.setRouteConstraintOSNR(mod.getOSNR_Limit());
   			 
   			 for(int j=i;j<=i+LimitSoltNum-1;j++)
   			 {
   					
   				 HashMap<String,Link>map=layer.getLinklist();   			     
   				 Iterator<String>iter=map.keySet().iterator();    			     
   				 while(iter.hasNext()){  			     
   					 Link currentlink=(Link)(map.get(iter.next()));
   					 if(currentlink.getNodeA().getIndex()<currentlink.getNodeB().getIndex())
   					 {
   						 //slot is occupied 
   						 if(currentlink.getSlotList().get(j).getStatus()==1)
   						 {
   							 NewSlotWindow.getConstraintlinklist().add(currentlink);
   							 NewSlotWindow.getConstraint().getExcludedLinklist().add(currentlink);
   							 Link antherLink=layer.findlink(currentlink.getNodeB(), currentlink.getNodeA());
   							 NewSlotWindow.getConstraintlinklist().add(antherLink);
   							 NewSlotWindow.getConstraint().getExcludedLinklist().add(antherLink);
   						 }
   						 //slot isn't occupied
   						 else
   						 {
   							 
   						 }
   					 }
   					 
   				 }
   			 }
   			 SlotWindowList.add(NewSlotWindow);
   		 }
   	 }
   	 
   	 
   	 
   	 return SlotWindowList;
   	 
    }
    
    public boolean AvailableSlotWindow(Layer layer,Nodepair nodepair,LightPath lightpath,ArrayList<SlotWindow> SlotWindowlist,Modulation mod)
    {
   	 
    	
    	SlotWindow slotwindow=FindSlotwindowWithLessHop(layer, nodepair,lightpath,SlotWindowlist); //找到可用的hops最少的slotwindow     
    	//存在可用的slotwindow
    	
    	if(slotwindow!=null)        //如果slotwindow存在则分配
    	{ 
    		

    		System.out.println("find the route");
    		
    		AssginSlot(layer,lightpath.getPhysicPath(),slotwindow);
    	
    		
    		lightpath.setAssociatedslotwindow(slotwindow);
    		nodepair.setOSNRblockStatus(false);
    		return true;
    	}
    	else
    	{
    		nodepair.setOSNRblockStatus(true);
    		return false;
    	}
    }
    
    
    public SlotWindow FindSlotwindowWithLessHop(Layer layer,Nodepair nodepair,LightPath lightpath,ArrayList<SlotWindow> SlotWindowList)
    {
    	boolean OSNRblockStatus = false;
    	LinearRoute finalRoute=new LinearRoute("",0,"",Constant.WORKING);
    	SlotWindow FindSlotWindow=null;
    	int finalhops=100000;
    	FreSlotFunctions TestOSNR=new FreSlotFunctions();
    	for(int i=0;i<SlotWindowList.size();i++)
    	{
    		SlotWindow slotwindow=SlotWindowList.get(i);
    		LinearRoute newRoute=new LinearRoute("",0,"",Constant.WORKING);
    		pathSearch workRouteSearch=new pathSearch();
    		workRouteSearch.SearchPathWithDijkstras(nodepair.getSrcNode(), nodepair.getDesNode(), layer, newRoute, slotwindow.getConstraint());

    		//如果路由存在且符合slotwindow的OSNR限制
    		if(newRoute.getLinklist().size()!=0&&TestOSNR.CalculateRouteOSNR(newRoute)>=slotwindow.getRouteConstraintOSNR())
    		{
    			OSNRblockStatus = false;
    			if(newRoute.getLinklist().size()<finalhops)
    			{
    				FindSlotWindow=slotwindow;
    				finalhops=newRoute.getLinklist().size();
    				finalRoute = newRoute;
    			}
    		}
    		//blocked by the OSNR
    		else if((newRoute.getLinklist().size()!=0&&TestOSNR.CalculateRouteOSNR(newRoute)<slotwindow.getRouteConstraintOSNR()))
    		{
    			OSNRblockStatus = true;
    		}
    	}
    	finalRoute.OutputRoute_link();
    	lightpath.setPhysicPath(finalRoute);
    	lightpath.setOSNRblockStatus(OSNRblockStatus);
    	return FindSlotWindow;
    } 
    
    public void AssginSlot(Layer layer,LinearRoute Route,SlotWindow slotwindow)
    {
    	for(int i=0;i<Route.getLinklist().size();i++)
    	{
    		Link link=Route.getLinklist().get(i);
    		Link anthorlink=layer.findlink(link.getNodeB(), link.getNodeA());
    		for(int slotnum=slotwindow.getFirstSlotNum();slotnum<=slotwindow.getLastSlotNum();slotnum++)
    		{

    			link.getSlotList().get(slotnum).setStatus(1);
    			anthorlink.getSlotList().get(slotnum).setStatus(1);
    		}

    	}
    }
    
    public void releaseSlot(LightPath lightpath, int StartSlotIndex,int requiredSlotNum)
	  {
	    	for(Link link:lightpath.getPhysicPath().getLinklist())
			{
	    		int Index = StartSlotIndex; 
	        	while(Index<StartSlotIndex+requiredSlotNum)
	        	{
	        		link.getSlotList().get(Index).setStatus(0);
	        		Index++;
	        	}
			}
	    }

}
