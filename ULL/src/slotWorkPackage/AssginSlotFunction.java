package slotWorkPackage;

import functionAlorithms.Algorithm;
import general.ComparedResult;
import general.Constant;
import general.SlotWindow;
import general.Time;
import general.file_out_put;
import groupwork.pathSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import subgraph.LinearRoute;
import workPackage.Check_block;

import Network.Layer;
import Network.Link;
import Network.Node;
import Network.Nodepair;

public class AssginSlotFunction {

	
	
	public ArrayList<String> GenerateDemandlist(Layer layer,int demandNum)
	{
		ArrayList<String> demandlist=new ArrayList<String>(200);
		HashMap<String,Nodepair>map=layer.getNodepairlist();
        Iterator<String>iter=map.keySet().iterator();
		while(iter.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map.get(iter.next()));
			for(int i=0;i<demandNum;i++)
				demandlist.add(nodepair.getName());
		}
		return demandlist;
	}
	
	
	
	   
	public int CheckSlotUsedOfRoute(Layer layer, LinearRoute route,int demandslotnum,int Cmaxslot,ExistParam param,Nodepair nodepair){
		      
	    	  
		Algorithm workAlgorithm=new Algorithm();		     	    	 	    	 
		//初始默认block_key为1既阻塞	    	      	 
		int block_key=1;    	
		//设置route占用的slot
		FreSlotFunctions calextendSlotFunction=new FreSlotFunctions();
		int slotNum=demandslotnum+(int)Math.ceil(demandslotnum*calextendSlotFunction.CalculatedPathSlotRequirment(route));
		nodepair.setOverheadSotNum((int)Math.ceil(demandslotnum*calextendSlotFunction.CalculatedPathSlotRequirment(route)));
		
		//检测是否堵塞
		System.out.println("需要分配的slot :"+slotNum);
		int firstindex=0;
		int lastindex=0;
		int finishedKey=0;
		while(firstindex<Cmaxslot){
			
			int key=0; 		  
			Node currentnode=route.getNodelist().get(0); 		  
			int textIndex=0; 		  
			int failedSlotIndex=0;
		//	System.out.println("test: "+firstindex);
			for(Node node:route.getNodelist()){ 			  		  
				if(!node.getName().equals(currentnode.getName())){
					int currentLinkKey=0;
					lastindex=firstindex+slotNum-1;
					if(lastindex<=Cmaxslot)
					{
					//	System.out.println(firstindex+" "+lastindex);
						int currentFailedSlotIndex=10000000;       				 
						//找到可用的slot 
						if(checkSlotNumofLink(layer,firstindex,lastindex,currentnode,node)==0){
        					  currentnode=node;					                    		   
						}   
						 
						else{   					
       					  
							currentFailedSlotIndex=FailedSlotIndex(layer,firstindex,lastindex,currentnode,node);     					  
						//	System.out.println("test: "+currentFailedSlotIndex);
							currentnode=node;      					 
							if(failedSlotIndex<currentFailedSlotIndex)       						 
								failedSlotIndex=currentFailedSlotIndex;     					 
							key=1;    					      					  
       				  
						}
					}
					else
					{
						finishedKey=1;
					}
				}
			}
			//索引未超出限定继续判断
			if(finishedKey==0)
			{
				//在次索引下有被占用
	        	 if(key==1){
	        		firstindex=failedSlotIndex+1;	
	        	 }
	        	 //索引的波长符合
	        	 else{
	        		int endIndex=firstindex+slotNum-1;
	        		
	                param.setEndSlotIndex(endIndex);
	                param.setStartSlotIndex(firstindex);
	        
	        		block_key=0; //未堵塞 
	        		break; //跳出循环
	        	 
	        	 }					
			}
			//已经超出限制，没有必要继续跳出循环
			else
			{
				break;
			}
		}	 
		
		return block_key;  	  
	      
	}
	      
	     
	 public int checkSlotNumofLink(Layer layer,int firstIndex,int lastIndex,Node currentnode,Node node){
   	  
   	  int currentLinkKey=0;
   	  
   	  for(int currentindex=firstIndex;currentindex<=lastIndex;currentindex++)
		  {
			  if(layer.findlink(currentnode, node).getSlotList().get(currentindex).getStatus()==0){
				  currentLinkKey=0;
			  }
			  else{
				  currentLinkKey=1;
				  break;
			  }
		  }
   	  return currentLinkKey;
   	  
     }
	      

	     
	 public int FailedSlotIndex(Layer layer,int firstIndex,int lastIndex,Node currentnode,Node node){
   	  
   	  int currentLinkKey=0;
   	  int failedIndex=0;
   	  for(int currentindex=firstIndex;currentindex<=lastIndex;currentindex++)
		  {
			  if(layer.findlink(currentnode, node).getSlotList().get(currentindex).getStatus()==0){
				  currentLinkKey=0;
			//	  System.out.println(" 链路"+layer.findlink(currentnode, node).getName()+"  index "+currentindex+"未被使用");
			  }
			  else{
				  currentLinkKey=1;
			//	  System.out.println(" 链路"+layer.findlink(currentnode, node).getName()+"  index "+currentindex+"被使用");
				  failedIndex=currentindex;
				  break;
			  }
		  }
   	  return failedIndex;
   	  
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
     public void AssginSlot(Layer layer,LinearRoute route,int firstIndex,int lastIndex){	 
    	Node currentnode=route.getNodelist().get(0); 		  				  
		for(Node node:route.getNodelist()){ 			  		  
			if(!node.getName().equals(currentnode.getName())){
				for(int currentindex=firstIndex;currentindex<=lastIndex;currentindex++)  
				{
					
				//	System.out.println(currentindex+" used");
		   		
					layer.findlink(currentnode, node).getSlotList().get(currentindex).setStatus(1);
		   			
					layer.findlink(node, currentnode).getSlotList().get(currentindex).setStatus(1);
				}
				System.out.println("为链路"+layer.findlink(currentnode, node).getName()+"分配slot: "+firstIndex+"to"+lastIndex);
				currentnode=node;
			}
    	
      	  
        }
     }
     //********************************Slot WINDOW************************************************//

     public void AssginmentSlotWindowForDemand(Layer layer,ArrayList<String> demandlist,int slotnum,int CmaxSlot)
 	{

 	

 		

 		int FinishedDemand=0;
 	
 		double WeightNum=0;
 		int blockNum=0;
 	
 		HashMap<String,Link>map=layer.getLinklist();
     	Iterator<String>iter=map.keySet().iterator();
     	while(iter.hasNext()){
     		Link link=(Link)(map.get(iter.next()));
     		link.addSlot(CmaxSlot);
     	}
     	
     	
     	for(int i=0;i<demandlist.size();i++)
     	{
     		String demand=demandlist.get(i);
     		HashMap<String,Nodepair>mapNodepair=layer.getNodepairlist();           
     		Iterator<String>iterNodepair=mapNodepair.keySet().iterator();     		
     		while(iterNodepair.hasNext()){
     			Nodepair nodepair=(Nodepair)(mapNodepair.get(iterNodepair.next()));
     		
     			if(nodepair.getName().equals(demand))
     			{
     				System.out.println("====================="+nodepair.getName()+"====================");
     				LinearRoute Route=nodepair.getLinearlist().get(0); 				 
     				double overhead=Route.getOverHead();    	    	   				
     				int block_key=0;
     				ExistParam param=new ExistParam();
     		//		block_key=CheckSlotWindowUsedOfRoute(layer, Route,slotnum,CmaxSlot,param);
     				if(block_key==0)
     				{
  
     					FinishedDemand++;
     					AssginSlot(layer, Route, param.getStartSlotIndex(), param.getEndSlotIndex());    			        	
     					WeightNum=WeightNum+Route.getLinklist().size();
     			       
     				
     				}
     				else
 					{
 						System.out.println("======"+"阻塞不分配slot"+"=======");
 						blockNum++;
 					}
     			}
     	}
     	//double TotalOverHead=SumOverhead;
     	System.out.println(FinishedDemand+" "+blockNum);
 	}
 	}
    
     
     
     /**
      * 搜集可使用的slotwindow集合
      * @param layer
      * @param LimitSoltNum
      * @param TotalSlotNum
      * @return
      */
     public ArrayList<SlotWindow> GenerateSlotWindow(Layer layer,int LimitSoltNum,int TotalSlotNum,double idendifiedOSNR)
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
    			 NewSlotWindow.setRouteConstraintOSNR(idendifiedOSNR);
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
 	  
     /**
      * 找到可用slotwindow
      * @param layer
      * @param nodepair
      * @param SlotWindowList
      * @return
      */
     public SlotWindow FindSlotwindow(Layer layer,Nodepair nodepair, ArrayList<SlotWindow> SlotWindowList)
     {
    	 SlotWindow FindSlotWindow=null;
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
    			 FindSlotWindow=slotwindow;
    			 break;
    		 }
    	 }
    	 return FindSlotWindow;
     } 
     public SlotWindow FindSlotwindowWithLessHop(Layer layer,Nodepair nodepair, ArrayList<SlotWindow> SlotWindowList)
     {
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
    			 if(newRoute.getLinklist().size()<finalhops)
    			 {
    				 FindSlotWindow=slotwindow;
    				 finalhops=newRoute.getLinklist().size();
    			 }
    		 }
    	 }
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
     
     
     public boolean AvailableSlotWindow(Layer layer,Nodepair nodepair, ArrayList<SlotWindow> SlotWindowlist)
     {
    //	 SlotWindow slotwindow=this.FindSlotwindow(layer, nodepair, SlotWindowlist); //找到可用的slotwindow      
    	 SlotWindow slotwindow=this.FindSlotwindowWithLessHop(layer, nodepair, SlotWindowlist); //找到可用的hops最少的slotwindow     
    	 //存在可用的slotwindow
         if(slotwindow!=null)        //如果slotwindow存在则分配
         { 
        	 LinearRoute newRoute=new LinearRoute("",0,"",Constant.WORKING);
    		 pathSearch workRouteSearch=new pathSearch();
    		 workRouteSearch.SearchPathWithDijkstras(nodepair.getSrcNode(), nodepair.getDesNode(), layer, newRoute, slotwindow.getConstraint());
    		 
    		 this.AssginSlot(layer,newRoute,slotwindow);
    	//	 newRoute.OutputRoute_link(newRoute);
    	//	 System.out.println(slotwindow.getFirstSlotNum()+" "+slotwindow.getLastSlotNum());
    		 return true;
         }
         else
         {
        	 return false;
         }
     }
     
     
     
     
     public void AssginSlotDemandFunction(Layer layer,int MiddleSlotNum,int DaynamicRange,int MaxSlotNum)
     {
    	 //初始化link,为每个link增加slot,0~MaxSlotNum
    	 HashMap<String,Link>map=layer.getLinklist();
    	 Iterator<String>iter=map.keySet().iterator();	
    	 while(iter.hasNext()){     		
    		 Link link=(Link)(map.get(iter.next()));
    		 link.addSlot(MaxSlotNum);
    	 }
    	 
    	 
    	 //为每个节点对找路由
    	 Constant FECtype=new Constant();           //FEC种类
//    	 int index=0;                               
    	 FreSlotFunctions workFunction=new FreSlotFunctions();    //功能函数
    	 int UnfinishedDemandNum=0;
    	 int overheadSlot=0;
    	 
    	 
    	 HashMap<String,Nodepair>map1=layer.getNodepairlist();
         Iterator<String>iter1=map1.keySet().iterator();
         while(iter1.hasNext()){ 			
        	 Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
        	// int slotNum=workFunction.GenerateRandom(MiddleSlotNum, DaynamicRange);  //随机生成一定范围内的slotNum
			 int slotNum=MiddleSlotNum;
        	 nodepair.setSlotNum(slotNum);
			
        	 
        	 System.out.println("========"+nodepair.getName()+"========");
 		    
        	 boolean Findkey=false;                                              //标记是否分配成功，初始化时默认为未成功
 		   
        	 LinearRoute Route=nodepair.getLinearlist().get(0);                  //找到该节点对间最短路由      	 
        	 int PreViewSlotSize=(int)Math.ceil(nodepair.getSlotNum()*workFunction.CalculatedPathSlotRequirment(Route));//最短路由对应的最大Slot需求
       
        	 int FirstSlotSize=(int)Math.ceil(nodepair.getSlotNum()*FECtype.firstFEC);      
        	 int FirstNeedSlotSize=slotNum+FirstSlotSize;                                                               //第一种FEC
           	         	  
        	 int SecondSlotSize=(int)Math.ceil(nodepair.getSlotNum()*FECtype.secondFEC);                                //第二种FEC
        	 int SecondNeedSlotSize=slotNum+SecondSlotSize;
        	       	 
        	 int ThirdSlotSize=(int)Math.ceil(nodepair.getSlotNum()*FECtype.thirdFEC);                                  //第三种FEC
        	 int ThirdNeedSlotSize=slotNum+ThirdSlotSize;      	 
       
           //System.out.println("因为PreViewSlotSize为"+PreViewSlotSize+"  选择第 "+PreViewSlotSize+"类slotwindow");
        	 if(PreViewSlotSize==FirstSlotSize)
 		     {
        		 nodepair.setOverheadSotNum(FirstSlotSize);
        		 ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,FirstNeedSlotSize,MaxSlotNum,FECtype.firstOSNR);//可使用FEC1的slotwindow集合
        		 Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);                                                 //判断能否分配业务
 		         
        		 if(!Findkey)
 		         {
        			 nodepair.setOverheadSotNum(SecondSlotSize);
 		        	 System.out.println("该slotwindow无法完成分配,采用第2类slotwindow"); 
 		        	 //System.out.println("======================================"); 
 		        	 ArrayList<SlotWindow> SecondSlotWindowlist=GenerateSlotWindow(layer,SecondNeedSlotSize,MaxSlotNum,FECtype.secondFEC);//可使用FEC2的slotwindow集合
 	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, SecondSlotWindowlist);                                                  //判断能否分配业务
 	 		         //output(layer);
 		         } 		         
 		         if(!Findkey)
		         {
 		        	 nodepair.setOverheadSotNum(ThirdSlotSize);
 		        	 System.out.println("该slotwindow无法完成分配,采用第3类slotwindow"); 
 		        	// System.out.println("======================================"); 
		        	 ArrayList<SlotWindow> ThirdSlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);//可使用FEC3的slotwindow集合
	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, ThirdSlotWindowlist);                                                 //判断能否分配业务
	 		    	 
		         }
 		         if(!Findkey)
		         {
		        	 System.out.println("该slotwindow无法完成分配");
		        	 nodepair.setOverheadSotNum(0);
		        	 UnfinishedDemandNum++;
		         }
 		         
 		         
 		     }
 		     else if(PreViewSlotSize==SecondSlotSize)
 		     {
 		    	nodepair.setOverheadSotNum(SecondSlotSize);
 		    	ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,SecondNeedSlotSize,MaxSlotNum,FECtype.secondFEC);     //可使用FEC2的slotwindow集合
 		    	Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);                                                        //判断能否分配业务
 		    	
 		        if(!Findkey)
		         {
 		        	 nodepair.setOverheadSotNum(ThirdSlotSize);
 		        	 System.out.println("该slotwindow无法完成分配,采用第3类slotwindow"); 
 		        	// System.out.println("======================================"); 
		        	 ArrayList<SlotWindow> ThirdSlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);//可使用FEC3的slotwindow集合
	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, ThirdSlotWindowlist);                                                 //判断能否分配业务
	 		       //  output(layer);
		         }
 		        if(!Findkey)
		         {
		        	 System.out.println("该slotwindow无法完成分配");
		        	 nodepair.setOverheadSotNum(0);
		        	 UnfinishedDemandNum++;
		         }
 		     } 		    
 		     else if(PreViewSlotSize==ThirdSlotSize)
		     {
 		    	nodepair.setOverheadSotNum(ThirdSlotSize);
 		    	ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);      //可使用FEC3的slotwindow集合
 		    	Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);//判断能否分配业务
 		       
 		        if(!Findkey)
		         {
		        	 System.out.println("该slotwindow无法完成分配");
		        	 nodepair.setOverheadSotNum(0);
		        	 UnfinishedDemandNum++;
		         }
		     }
        	 overheadSlot+= nodepair.getOverheadSotNum();
        //	 output(layer,MaxSlotNum);
        //	 index++;
         }
         System.out.println("finishedOverhead: "+overheadSlot);
         System.out.println("unfinished demand Num: "+UnfinishedDemandNum);
         System.out.println("total demand Num: "+layer.getNodepair_num());
     }
     
     
     
     
     
     public void AssginSlotDemandForMiniTopologyFunction(Layer layer,int MiddleSlotNum,int DaynamicRange,int MaxSlotNum)
     {
    	 //初始化link,为每个link增加slot,0~MaxSlotNum
    	 HashMap<String,Link>map=layer.getLinklist();
    	 Iterator<String>iter=map.keySet().iterator();	
    	 while(iter.hasNext()){     		
    		 Link link=(Link)(map.get(iter.next()));
    		 link.addSlot(MaxSlotNum);
    	 }
    	 
    	 
    	 //为每个节点对找路由
    	 Constant FECtype=new Constant();           //FEC种类
//    	 int index=0;                               
    	 FreSlotFunctions workFunction=new FreSlotFunctions();    //功能函数
    	 
    	 
    	 
    	 HashMap<String,Nodepair>map1=layer.getNodepairlist();
         Iterator<String>iter1=map1.keySet().iterator();
         while(iter1.hasNext()){ 			
        	 Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
        	// int slotNum=workFunction.GenerateRandom(MiddleSlotNum, DaynamicRange);  //随机生成一定范围内的slotNum
			 int slotNum=MiddleSlotNum;
        	 nodepair.setSlotNum(slotNum);
			
        	 
        	 System.out.println("========"+nodepair.getName()+"========");
 		    
        	 boolean Findkey=false;                                              //标记是否分配成功，初始化时默认为未成功
 		   
        	 LinearRoute Route=nodepair.getLinearlist().get(0);                  //找到该节点对间最短路由
//        	 int routeSize=nodepair.getLinearlist().get(0).getLinklist().size(); 
        
        	 
        	 int PreViewSlotSize=(int)Math.ceil(nodepair.getSlotNum()*workFunction.CalculatedPathSlotRequirment(Route));
        //	 int PreViewSlotSize=routeSize;
        //	 System.out.println(PreViewSlotSize);
        	 int FirstSlotSize=(int)Math.ceil(nodepair.getSlotNum()*FECtype.firstFEC);
        //	 int FirstSlotSize=FECtype.f1;
        	 int FirstNeedSlotSize=slotNum+FirstSlotSize;
        //	 System.out.println(FirstSlotSize);
        	 
        	 
        	 int SecondSlotSize=(int)Math.ceil(nodepair.getSlotNum()*FECtype.secondFEC);
        //	 int SecondSlotSize=FECtype.f2;
        	 int SecondNeedSlotSize=slotNum+SecondSlotSize;
       // 	 System.out.println(SecondSlotSize);
        	 
        	 
        	 int ThirdSlotSize=(int)Math.ceil(nodepair.getSlotNum()*FECtype.thirdFEC);
       // 	 int ThirdSlotSize=FECtype.f3;
        	 int ThirdNeedSlotSize=slotNum+ThirdSlotSize;
       // 	 System.out.println(ThirdSlotSize);
        	 
       // 	 System.out.println("因为PreViewSlotSize为"+PreViewSlotSize+"  选择第 "+PreViewSlotSize+"类slotwindow");
        	 if(PreViewSlotSize==FirstSlotSize)
 		     {
        		 ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,FirstNeedSlotSize,MaxSlotNum,FECtype.firstOSNR);
        		 Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);
 		         if(!Findkey)
 		         {
 		        	 System.out.println("该slotwindow无法完成分配,采用第2类slotwindow"); 
 		        	// System.out.println("======================================"); 
 		        	 ArrayList<SlotWindow> SecondSlotWindowlist=GenerateSlotWindow(layer,SecondNeedSlotSize,MaxSlotNum,FECtype.secondFEC);
 	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, SecondSlotWindowlist);
 	 		      //   output(layer);
 		         }
 		         
 		         if(!Findkey)
		         {
 		        	 System.out.println("该slotwindow无法完成分配,采用第3类slotwindow"); 
 		        	// System.out.println("======================================"); 
		        	 ArrayList<SlotWindow> ThirdSlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);
	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, ThirdSlotWindowlist);
	 		    	 
		         }
 		         if(!Findkey)
		         {
		        	 System.out.println("该slotwindow无法完成分配");
		         }
 		         
 		         
 		     }
 		     else if(PreViewSlotSize==SecondSlotSize)
 		     {
 		    	
 		    	ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,SecondNeedSlotSize,MaxSlotNum,FECtype.secondFEC);
 		    	Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);
 		    	
 		        if(!Findkey)
		         {
 		        	 System.out.println("该slotwindow无法完成分配,采用第3类slotwindow"); 
 		        	// System.out.println("======================================"); 
		        	 ArrayList<SlotWindow> ThirdSlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);
	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, ThirdSlotWindowlist);
	 		       //  output(layer);
		         }
 		        if(!Findkey)
		         {
		        	 System.out.println("该slotwindow无法完成分配");
		         }
 		     } 		    
 		     else if(PreViewSlotSize==ThirdSlotSize)
		     {
 		    	ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);
 		    	Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);
 		       
 		        if(!Findkey)
		         {
		        	 System.out.println("该slotwindow无法完成分配");
		         }
		     }
 		    	 
        //	 output(layer,MaxSlotNum);
        //	 index++;
        
         }
     }
     
     
     
     
     public void output(Layer layer,int MaxSlotNum)
 	{
 		 HashMap<String,Link>map2=layer.getLinklist();
 	     	Iterator<String>iter2=map2.keySet().iterator();
 	     	while(iter2.hasNext()){
 	     		Link link=(Link)(map2.get(iter2.next()));
 	     		if(link.getNodeA().getIndex()<link.getNodeB().getIndex())
 	     		{
 	     			System.out.println("======"+link.getName()+"=====");
 	     			for(int i=0;i<=MaxSlotNum;i++)
 	     			{
 	     				System.out.print(link.getSlotList().get(i).getStatus()+" ");
 	     			}
 	     			System.out.println();
 	     		}
 	     	}
 	}
     
     
    //***************************Shuffle the demandlist*************************************//
     public ComparedResult AssginSlotDemandwithDemandlistFunction(Layer layer,int MiddleSlotNum,int DaynamicRange,int MaxSlotNum,ArrayList<String> nodepairlist)
     {
    	 //初始化link,为每个link增加slot,0~MaxSlotNum
    	 HashMap<String,Link>map=layer.getLinklist();
    	 Iterator<String>iter=map.keySet().iterator();	
    	 while(iter.hasNext()){     		
    		 Link link=(Link)(map.get(iter.next()));
    		 link.InitSlot();
    	 }
    	 
    	 
    	 //为每个节点对找路由
    	 Constant FECtype=new Constant();           //FEC种类
//    	 int index=0;                               
    	 FreSlotFunctions workFunction=new FreSlotFunctions();    //功能函数
    	 int UnfinishedDemandNum=0;
    	 int overheadSlot=0;
    	 ComparedResult Result=new   ComparedResult();
    	 for(int i=0;i<nodepairlist.size();i++)
    	 {
    		 String nodepairName=nodepairlist.get(i);
    		 HashMap<String,Nodepair>map1=layer.getNodepairlist();
             Iterator<String>iter1=map1.keySet().iterator();
             while(iter1.hasNext()){ 			
            	 Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
            	 if(nodepair.getName().equals(nodepairName))
            	 {
            	//	int slotNum=workFunction.GenerateRandom(MiddleSlotNum, DaynamicRange);  //随机生成一定范围内的slotNum
        		//	 int slotNum=MiddleSlotNum;
                //	 nodepair.setSlotNum(slotNum);
            		 int slotNum=nodepair.getSlotNum();
              //  	 System.out.println("========"+nodepair.getName()+"========");
         		    
                	 boolean Findkey=false;                                              //标记是否分配成功，初始化时默认为未成功
         		   
                	 LinearRoute Route=nodepair.getLinearlist().get(0);                  //找到该节点对间最短路由      	 
                	 int PreViewSlotSize=(int)Math.ceil(nodepair.getSlotNum()*workFunction.CalculatedPathSlotRequirment(Route));//最短路由对应的最大Slot需求
               
                	 int FirstSlotSize=(int)Math.ceil(nodepair.getSlotNum()*FECtype.firstFEC);      
                	 int FirstNeedSlotSize=slotNum+FirstSlotSize;                                                               //第一种FEC
                   	         	  
                	 int SecondSlotSize=(int)Math.ceil(nodepair.getSlotNum()*FECtype.secondFEC);                                //第二种FEC
                	 int SecondNeedSlotSize=slotNum+SecondSlotSize;
                	       	 
                	 int ThirdSlotSize=(int)Math.ceil(nodepair.getSlotNum()*FECtype.thirdFEC);                                  //第三种FEC
                	 int ThirdNeedSlotSize=slotNum+ThirdSlotSize;      	 
               
                   //System.out.println("因为PreViewSlotSize为"+PreViewSlotSize+"  选择第 "+PreViewSlotSize+"类slotwindow");
                	 if(PreViewSlotSize==FirstSlotSize)
         		     {
                		 nodepair.setOverheadSotNum(FirstSlotSize);
                		 ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,FirstNeedSlotSize,MaxSlotNum,FECtype.firstOSNR);//可使用FEC1的slotwindow集合
                		 Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);                                                 //判断能否分配业务
         		         
                		 if(!Findkey)
         		         {
                			 nodepair.setOverheadSotNum(SecondSlotSize);
         		   //     	 System.out.println("该slotwindow无法完成分配,采用第2类slotwindow"); 
         		        	 //System.out.println("======================================"); 
         		        	 ArrayList<SlotWindow> SecondSlotWindowlist=GenerateSlotWindow(layer,SecondNeedSlotSize,MaxSlotNum,FECtype.secondFEC);//可使用FEC2的slotwindow集合
         	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, SecondSlotWindowlist);                                                  //判断能否分配业务
         	 		         //output(layer);
         		         } 		         
         		         if(!Findkey)
        		         {
         		        	 nodepair.setOverheadSotNum(ThirdSlotSize);
         		    //    	 System.out.println("该slotwindow无法完成分配,采用第3类slotwindow"); 
         		        	// System.out.println("======================================"); 
        		        	 ArrayList<SlotWindow> ThirdSlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);//可使用FEC3的slotwindow集合
        	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, ThirdSlotWindowlist);                                                 //判断能否分配业务
        	 		    	 
        		         }
         		         if(!Findkey)
        		         {
        		    //    	 System.out.println("该slotwindow无法完成分配");
        		        	 nodepair.setOverheadSotNum(0);
        		        	 UnfinishedDemandNum++;
        		         }
         		         
         		         
         		     }
         		     else if(PreViewSlotSize==SecondSlotSize)
         		     {
         		    	nodepair.setOverheadSotNum(SecondSlotSize);
         		    	ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,SecondNeedSlotSize,MaxSlotNum,FECtype.secondFEC);     //可使用FEC2的slotwindow集合
         		    	Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);                                                        //判断能否分配业务
         		    	
         		        if(!Findkey)
        		         {
         		        	 nodepair.setOverheadSotNum(ThirdSlotSize);
         		    //    	 System.out.println("该slotwindow无法完成分配,采用第3类slotwindow"); 
         		        	// System.out.println("======================================"); 
        		        	 ArrayList<SlotWindow> ThirdSlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);//可使用FEC3的slotwindow集合
        	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, ThirdSlotWindowlist);                                                 //判断能否分配业务
        	 		       //  output(layer);
        		         }
         		        if(!Findkey)
        		         {
        		     //   	 System.out.println("该slotwindow无法完成分配");
        		        	 nodepair.setOverheadSotNum(0);
        		        	 UnfinishedDemandNum++;
        		         }
         		     } 		    
         		     else if(PreViewSlotSize==ThirdSlotSize)
        		     {
         		    	nodepair.setOverheadSotNum(ThirdSlotSize);
         		    	ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);      //可使用FEC3的slotwindow集合
         		    	Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);//判断能否分配业务
         		       
         		        if(!Findkey)
        		         {
        		     //   	 System.out.println("该slotwindow无法完成分配");
        		        	 nodepair.setOverheadSotNum(0);
        		        	 UnfinishedDemandNum++;
        		         }
        		     }
                	 overheadSlot+= nodepair.getOverheadSotNum();
                	 if(nodepair.getOverheadSotNum()==FirstSlotSize)
                		 Result.setFinishedDemandNumFirstType(Result.getFinishedDemandNumFirstType()+1);
                	 else if(nodepair.getOverheadSotNum()==SecondSlotSize)	 
                		 Result.setFinishedDemandNumSecondType(Result.getFinishedDemandNumSecondType()+1);
                	 else if(nodepair.getOverheadSotNum()==ThirdSlotSize)	
                		 Result.setFinishedDemandNumThirdType(Result.getFinishedDemandNumThirdType()+1);
                //	 output(layer,MaxSlotNum);
                //	 index++;
            	 }
            	 
             }
    	 }
    	
         System.out.println("finishedOverhead: "+overheadSlot);
         System.out.println("finished demand Num: "+(layer.getNodepair_num()-UnfinishedDemandNum));
         System.out.println("total demand Num: "+layer.getNodepair_num());
         System.out.println("finisheddemandNumFirstType: "+Result.getFinishedDemandNumFirstType());
         System.out.println("finisheddemandNumSecondType: "+Result.getFinishedDemandNumSecondType());
         System.out.println("finisheddemandNumThirdType: "+Result.getFinishedDemandNumThirdType());
         
         
         Result.setFinishedDemandNum(layer.getNodepair_num()-UnfinishedDemandNum);
         Result.setUnfinishedDemandNum(UnfinishedDemandNum);  
         Result.setSumOverheadSlot(overheadSlot);
	
         return Result;
         
     }
     
     
}
