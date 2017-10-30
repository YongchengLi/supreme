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
		//��ʼĬ��block_keyΪ1������	    	      	 
		int block_key=1;    	
		//����routeռ�õ�slot
		FreSlotFunctions calextendSlotFunction=new FreSlotFunctions();
		int slotNum=demandslotnum+(int)Math.ceil(demandslotnum*calextendSlotFunction.CalculatedPathSlotRequirment(route));
		nodepair.setOverheadSotNum((int)Math.ceil(demandslotnum*calextendSlotFunction.CalculatedPathSlotRequirment(route)));
		
		//����Ƿ����
		System.out.println("��Ҫ�����slot :"+slotNum);
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
						//�ҵ����õ�slot 
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
			//����δ�����޶������ж�
			if(finishedKey==0)
			{
				//�ڴ��������б�ռ��
	        	 if(key==1){
	        		firstindex=failedSlotIndex+1;	
	        	 }
	        	 //�����Ĳ�������
	        	 else{
	        		int endIndex=firstindex+slotNum-1;
	        		
	                param.setEndSlotIndex(endIndex);
	                param.setStartSlotIndex(firstindex);
	        
	        		block_key=0; //δ���� 
	        		break; //����ѭ��
	        	 
	        	 }					
			}
			//�Ѿ��������ƣ�û�б�Ҫ��������ѭ��
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
			//	  System.out.println(" ��·"+layer.findlink(currentnode, node).getName()+"  index "+currentindex+"δ��ʹ��");
			  }
			  else{
				  currentLinkKey=1;
			//	  System.out.println(" ��·"+layer.findlink(currentnode, node).getName()+"  index "+currentindex+"��ʹ��");
				  failedIndex=currentindex;
				  break;
			  }
		  }
   	  return failedIndex;
   	  
     }
	      
	 /**
      * ΪС�������Ĳ���д��
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
				System.out.println("Ϊ��·"+layer.findlink(currentnode, node).getName()+"����slot: "+firstIndex+"to"+lastIndex);
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
 						System.out.println("======"+"����������slot"+"=======");
 						blockNum++;
 					}
     			}
     	}
     	//double TotalOverHead=SumOverhead;
     	System.out.println(FinishedDemand+" "+blockNum);
 	}
 	}
    
     
     
     /**
      * �Ѽ���ʹ�õ�slotwindow����
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
      * �ҵ�����slotwindow
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
    		 //���·�ɴ����ҷ���slotwindow��OSNR����
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
    		 //���·�ɴ����ҷ���slotwindow��OSNR����
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
    //	 SlotWindow slotwindow=this.FindSlotwindow(layer, nodepair, SlotWindowlist); //�ҵ����õ�slotwindow      
    	 SlotWindow slotwindow=this.FindSlotwindowWithLessHop(layer, nodepair, SlotWindowlist); //�ҵ����õ�hops���ٵ�slotwindow     
    	 //���ڿ��õ�slotwindow
         if(slotwindow!=null)        //���slotwindow���������
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
    	 //��ʼ��link,Ϊÿ��link����slot,0~MaxSlotNum
    	 HashMap<String,Link>map=layer.getLinklist();
    	 Iterator<String>iter=map.keySet().iterator();	
    	 while(iter.hasNext()){     		
    		 Link link=(Link)(map.get(iter.next()));
    		 link.addSlot(MaxSlotNum);
    	 }
    	 
    	 
    	 //Ϊÿ���ڵ����·��
    	 Constant FECtype=new Constant();           //FEC����
//    	 int index=0;                               
    	 FreSlotFunctions workFunction=new FreSlotFunctions();    //���ܺ���
    	 int UnfinishedDemandNum=0;
    	 int overheadSlot=0;
    	 
    	 
    	 HashMap<String,Nodepair>map1=layer.getNodepairlist();
         Iterator<String>iter1=map1.keySet().iterator();
         while(iter1.hasNext()){ 			
        	 Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
        	// int slotNum=workFunction.GenerateRandom(MiddleSlotNum, DaynamicRange);  //�������һ����Χ�ڵ�slotNum
			 int slotNum=MiddleSlotNum;
        	 nodepair.setSlotNum(slotNum);
			
        	 
        	 System.out.println("========"+nodepair.getName()+"========");
 		    
        	 boolean Findkey=false;                                              //����Ƿ����ɹ�����ʼ��ʱĬ��Ϊδ�ɹ�
 		   
        	 LinearRoute Route=nodepair.getLinearlist().get(0);                  //�ҵ��ýڵ�Լ����·��      	 
        	 int PreViewSlotSize=(int)Math.ceil(nodepair.getSlotNum()*workFunction.CalculatedPathSlotRequirment(Route));//���·�ɶ�Ӧ�����Slot����
       
        	 int FirstSlotSize=(int)Math.ceil(nodepair.getSlotNum()*FECtype.firstFEC);      
        	 int FirstNeedSlotSize=slotNum+FirstSlotSize;                                                               //��һ��FEC
           	         	  
        	 int SecondSlotSize=(int)Math.ceil(nodepair.getSlotNum()*FECtype.secondFEC);                                //�ڶ���FEC
        	 int SecondNeedSlotSize=slotNum+SecondSlotSize;
        	       	 
        	 int ThirdSlotSize=(int)Math.ceil(nodepair.getSlotNum()*FECtype.thirdFEC);                                  //������FEC
        	 int ThirdNeedSlotSize=slotNum+ThirdSlotSize;      	 
       
           //System.out.println("��ΪPreViewSlotSizeΪ"+PreViewSlotSize+"  ѡ��� "+PreViewSlotSize+"��slotwindow");
        	 if(PreViewSlotSize==FirstSlotSize)
 		     {
        		 nodepair.setOverheadSotNum(FirstSlotSize);
        		 ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,FirstNeedSlotSize,MaxSlotNum,FECtype.firstOSNR);//��ʹ��FEC1��slotwindow����
        		 Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);                                                 //�ж��ܷ����ҵ��
 		         
        		 if(!Findkey)
 		         {
        			 nodepair.setOverheadSotNum(SecondSlotSize);
 		        	 System.out.println("��slotwindow�޷���ɷ���,���õ�2��slotwindow"); 
 		        	 //System.out.println("======================================"); 
 		        	 ArrayList<SlotWindow> SecondSlotWindowlist=GenerateSlotWindow(layer,SecondNeedSlotSize,MaxSlotNum,FECtype.secondFEC);//��ʹ��FEC2��slotwindow����
 	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, SecondSlotWindowlist);                                                  //�ж��ܷ����ҵ��
 	 		         //output(layer);
 		         } 		         
 		         if(!Findkey)
		         {
 		        	 nodepair.setOverheadSotNum(ThirdSlotSize);
 		        	 System.out.println("��slotwindow�޷���ɷ���,���õ�3��slotwindow"); 
 		        	// System.out.println("======================================"); 
		        	 ArrayList<SlotWindow> ThirdSlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);//��ʹ��FEC3��slotwindow����
	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, ThirdSlotWindowlist);                                                 //�ж��ܷ����ҵ��
	 		    	 
		         }
 		         if(!Findkey)
		         {
		        	 System.out.println("��slotwindow�޷���ɷ���");
		        	 nodepair.setOverheadSotNum(0);
		        	 UnfinishedDemandNum++;
		         }
 		         
 		         
 		     }
 		     else if(PreViewSlotSize==SecondSlotSize)
 		     {
 		    	nodepair.setOverheadSotNum(SecondSlotSize);
 		    	ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,SecondNeedSlotSize,MaxSlotNum,FECtype.secondFEC);     //��ʹ��FEC2��slotwindow����
 		    	Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);                                                        //�ж��ܷ����ҵ��
 		    	
 		        if(!Findkey)
		         {
 		        	 nodepair.setOverheadSotNum(ThirdSlotSize);
 		        	 System.out.println("��slotwindow�޷���ɷ���,���õ�3��slotwindow"); 
 		        	// System.out.println("======================================"); 
		        	 ArrayList<SlotWindow> ThirdSlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);//��ʹ��FEC3��slotwindow����
	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, ThirdSlotWindowlist);                                                 //�ж��ܷ����ҵ��
	 		       //  output(layer);
		         }
 		        if(!Findkey)
		         {
		        	 System.out.println("��slotwindow�޷���ɷ���");
		        	 nodepair.setOverheadSotNum(0);
		        	 UnfinishedDemandNum++;
		         }
 		     } 		    
 		     else if(PreViewSlotSize==ThirdSlotSize)
		     {
 		    	nodepair.setOverheadSotNum(ThirdSlotSize);
 		    	ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);      //��ʹ��FEC3��slotwindow����
 		    	Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);//�ж��ܷ����ҵ��
 		       
 		        if(!Findkey)
		         {
		        	 System.out.println("��slotwindow�޷���ɷ���");
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
    	 //��ʼ��link,Ϊÿ��link����slot,0~MaxSlotNum
    	 HashMap<String,Link>map=layer.getLinklist();
    	 Iterator<String>iter=map.keySet().iterator();	
    	 while(iter.hasNext()){     		
    		 Link link=(Link)(map.get(iter.next()));
    		 link.addSlot(MaxSlotNum);
    	 }
    	 
    	 
    	 //Ϊÿ���ڵ����·��
    	 Constant FECtype=new Constant();           //FEC����
//    	 int index=0;                               
    	 FreSlotFunctions workFunction=new FreSlotFunctions();    //���ܺ���
    	 
    	 
    	 
    	 HashMap<String,Nodepair>map1=layer.getNodepairlist();
         Iterator<String>iter1=map1.keySet().iterator();
         while(iter1.hasNext()){ 			
        	 Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
        	// int slotNum=workFunction.GenerateRandom(MiddleSlotNum, DaynamicRange);  //�������һ����Χ�ڵ�slotNum
			 int slotNum=MiddleSlotNum;
        	 nodepair.setSlotNum(slotNum);
			
        	 
        	 System.out.println("========"+nodepair.getName()+"========");
 		    
        	 boolean Findkey=false;                                              //����Ƿ����ɹ�����ʼ��ʱĬ��Ϊδ�ɹ�
 		   
        	 LinearRoute Route=nodepair.getLinearlist().get(0);                  //�ҵ��ýڵ�Լ����·��
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
        	 
       // 	 System.out.println("��ΪPreViewSlotSizeΪ"+PreViewSlotSize+"  ѡ��� "+PreViewSlotSize+"��slotwindow");
        	 if(PreViewSlotSize==FirstSlotSize)
 		     {
        		 ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,FirstNeedSlotSize,MaxSlotNum,FECtype.firstOSNR);
        		 Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);
 		         if(!Findkey)
 		         {
 		        	 System.out.println("��slotwindow�޷���ɷ���,���õ�2��slotwindow"); 
 		        	// System.out.println("======================================"); 
 		        	 ArrayList<SlotWindow> SecondSlotWindowlist=GenerateSlotWindow(layer,SecondNeedSlotSize,MaxSlotNum,FECtype.secondFEC);
 	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, SecondSlotWindowlist);
 	 		      //   output(layer);
 		         }
 		         
 		         if(!Findkey)
		         {
 		        	 System.out.println("��slotwindow�޷���ɷ���,���õ�3��slotwindow"); 
 		        	// System.out.println("======================================"); 
		        	 ArrayList<SlotWindow> ThirdSlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);
	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, ThirdSlotWindowlist);
	 		    	 
		         }
 		         if(!Findkey)
		         {
		        	 System.out.println("��slotwindow�޷���ɷ���");
		         }
 		         
 		         
 		     }
 		     else if(PreViewSlotSize==SecondSlotSize)
 		     {
 		    	
 		    	ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,SecondNeedSlotSize,MaxSlotNum,FECtype.secondFEC);
 		    	Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);
 		    	
 		        if(!Findkey)
		         {
 		        	 System.out.println("��slotwindow�޷���ɷ���,���õ�3��slotwindow"); 
 		        	// System.out.println("======================================"); 
		        	 ArrayList<SlotWindow> ThirdSlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);
	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, ThirdSlotWindowlist);
	 		       //  output(layer);
		         }
 		        if(!Findkey)
		         {
		        	 System.out.println("��slotwindow�޷���ɷ���");
		         }
 		     } 		    
 		     else if(PreViewSlotSize==ThirdSlotSize)
		     {
 		    	ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);
 		    	Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);
 		       
 		        if(!Findkey)
		         {
		        	 System.out.println("��slotwindow�޷���ɷ���");
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
    	 //��ʼ��link,Ϊÿ��link����slot,0~MaxSlotNum
    	 HashMap<String,Link>map=layer.getLinklist();
    	 Iterator<String>iter=map.keySet().iterator();	
    	 while(iter.hasNext()){     		
    		 Link link=(Link)(map.get(iter.next()));
    		 link.InitSlot();
    	 }
    	 
    	 
    	 //Ϊÿ���ڵ����·��
    	 Constant FECtype=new Constant();           //FEC����
//    	 int index=0;                               
    	 FreSlotFunctions workFunction=new FreSlotFunctions();    //���ܺ���
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
            	//	int slotNum=workFunction.GenerateRandom(MiddleSlotNum, DaynamicRange);  //�������һ����Χ�ڵ�slotNum
        		//	 int slotNum=MiddleSlotNum;
                //	 nodepair.setSlotNum(slotNum);
            		 int slotNum=nodepair.getSlotNum();
              //  	 System.out.println("========"+nodepair.getName()+"========");
         		    
                	 boolean Findkey=false;                                              //����Ƿ����ɹ�����ʼ��ʱĬ��Ϊδ�ɹ�
         		   
                	 LinearRoute Route=nodepair.getLinearlist().get(0);                  //�ҵ��ýڵ�Լ����·��      	 
                	 int PreViewSlotSize=(int)Math.ceil(nodepair.getSlotNum()*workFunction.CalculatedPathSlotRequirment(Route));//���·�ɶ�Ӧ�����Slot����
               
                	 int FirstSlotSize=(int)Math.ceil(nodepair.getSlotNum()*FECtype.firstFEC);      
                	 int FirstNeedSlotSize=slotNum+FirstSlotSize;                                                               //��һ��FEC
                   	         	  
                	 int SecondSlotSize=(int)Math.ceil(nodepair.getSlotNum()*FECtype.secondFEC);                                //�ڶ���FEC
                	 int SecondNeedSlotSize=slotNum+SecondSlotSize;
                	       	 
                	 int ThirdSlotSize=(int)Math.ceil(nodepair.getSlotNum()*FECtype.thirdFEC);                                  //������FEC
                	 int ThirdNeedSlotSize=slotNum+ThirdSlotSize;      	 
               
                   //System.out.println("��ΪPreViewSlotSizeΪ"+PreViewSlotSize+"  ѡ��� "+PreViewSlotSize+"��slotwindow");
                	 if(PreViewSlotSize==FirstSlotSize)
         		     {
                		 nodepair.setOverheadSotNum(FirstSlotSize);
                		 ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,FirstNeedSlotSize,MaxSlotNum,FECtype.firstOSNR);//��ʹ��FEC1��slotwindow����
                		 Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);                                                 //�ж��ܷ����ҵ��
         		         
                		 if(!Findkey)
         		         {
                			 nodepair.setOverheadSotNum(SecondSlotSize);
         		   //     	 System.out.println("��slotwindow�޷���ɷ���,���õ�2��slotwindow"); 
         		        	 //System.out.println("======================================"); 
         		        	 ArrayList<SlotWindow> SecondSlotWindowlist=GenerateSlotWindow(layer,SecondNeedSlotSize,MaxSlotNum,FECtype.secondFEC);//��ʹ��FEC2��slotwindow����
         	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, SecondSlotWindowlist);                                                  //�ж��ܷ����ҵ��
         	 		         //output(layer);
         		         } 		         
         		         if(!Findkey)
        		         {
         		        	 nodepair.setOverheadSotNum(ThirdSlotSize);
         		    //    	 System.out.println("��slotwindow�޷���ɷ���,���õ�3��slotwindow"); 
         		        	// System.out.println("======================================"); 
        		        	 ArrayList<SlotWindow> ThirdSlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);//��ʹ��FEC3��slotwindow����
        	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, ThirdSlotWindowlist);                                                 //�ж��ܷ����ҵ��
        	 		    	 
        		         }
         		         if(!Findkey)
        		         {
        		    //    	 System.out.println("��slotwindow�޷���ɷ���");
        		        	 nodepair.setOverheadSotNum(0);
        		        	 UnfinishedDemandNum++;
        		         }
         		         
         		         
         		     }
         		     else if(PreViewSlotSize==SecondSlotSize)
         		     {
         		    	nodepair.setOverheadSotNum(SecondSlotSize);
         		    	ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,SecondNeedSlotSize,MaxSlotNum,FECtype.secondFEC);     //��ʹ��FEC2��slotwindow����
         		    	Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);                                                        //�ж��ܷ����ҵ��
         		    	
         		        if(!Findkey)
        		         {
         		        	 nodepair.setOverheadSotNum(ThirdSlotSize);
         		    //    	 System.out.println("��slotwindow�޷���ɷ���,���õ�3��slotwindow"); 
         		        	// System.out.println("======================================"); 
        		        	 ArrayList<SlotWindow> ThirdSlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);//��ʹ��FEC3��slotwindow����
        	 		    	 Findkey=AvailableSlotWindow(layer, nodepair, ThirdSlotWindowlist);                                                 //�ж��ܷ����ҵ��
        	 		       //  output(layer);
        		         }
         		        if(!Findkey)
        		         {
        		     //   	 System.out.println("��slotwindow�޷���ɷ���");
        		        	 nodepair.setOverheadSotNum(0);
        		        	 UnfinishedDemandNum++;
        		         }
         		     } 		    
         		     else if(PreViewSlotSize==ThirdSlotSize)
        		     {
         		    	nodepair.setOverheadSotNum(ThirdSlotSize);
         		    	ArrayList<SlotWindow> SlotWindowlist=GenerateSlotWindow(layer,ThirdNeedSlotSize,MaxSlotNum,FECtype.thirdOSNR);      //��ʹ��FEC3��slotwindow����
         		    	Findkey=AvailableSlotWindow(layer, nodepair, SlotWindowlist);//�ж��ܷ����ҵ��
         		       
         		        if(!Findkey)
        		         {
        		     //   	 System.out.println("��slotwindow�޷���ɷ���");
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
