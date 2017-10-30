package workPackage;

import functionAlorithms.Algorithm;
import general.Time;

import java.util.HashMap;
import java.util.Iterator;

import subgraph.LinearRoute;

import Network.Layer;
import Network.Node;
import Network.Nodepair;

public class Check_block {
      public int Check(Layer layer, Nodepair serach_nodepair){
    	  
    	   //System.out.println(" 检测是否堵塞");
	       int route_find=0;
	       int block_key=0;
	       //检测是否堵塞
	       HashMap<String,Nodepair>map1=layer.getNodepairlist();
	       Iterator<String>iter1=map1.keySet().iterator();
		   while(iter1.hasNext()){
			            
			          
		                Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
		                //找到nodepair
		                if(nodepair.getName().endsWith(serach_nodepair.getName())){
		                	     
		                	     //找到route
		                         route_find=nodepair.getLinearlist().size();
	                             LinearRoute route=nodepair.getLinearlist().get(route_find-1);
	                             //检测堵塞
	                             Node currentnode=route.getNodelist().get(0);
		                         for(Node node:route.getNodelist()){
					                    if(!node.getName().equals(currentnode.getName())){
					                           int weight=layer.findlink(currentnode, node).getW();
						                       // System.out.println("weight:"+weight);
					                           if(weight==0){
					                                //  System.out.println(layer.findlink(currentnode, node).getName()+"-"+"堵塞");
					                                  block_key=1; //堵塞，event 丢掉，不分配波长,block_number++
					                                  currentnode=node;
					                           }
					                           else{
					                        	   currentnode=node;
					                           }
					                    }
		                         }
		                     
		                }
		                
		   }
		return block_key;
      }
      public int Check(Layer layer, Nodepair serach_nodepair,int route_index){
    	  
    	  
   	   //System.out.println(" 检测是否堵塞");
	     
	       int block_key=0;
	       //检测是否堵塞
	       HashMap<String,Nodepair>map2=layer.getNodepairlist();
	       Iterator<String>iter2=map2.keySet().iterator();
		   while(iter2.hasNext()){
			            
			          
		                Nodepair nodepair=(Nodepair)(map2.get(iter2.next()));
		                //找到nodepair
		                if(nodepair.getName().endsWith(serach_nodepair.getName())){
		                	     
		                	     //找到route
		                	    
	                             Node currentnode=serach_nodepair.getLinearlist().get(route_index).getNodelist().get(0);
		                         for(Node node:serach_nodepair.getLinearlist().get(route_index).getNodelist()){
	      
			  
		                        	 if(!node.getName().equals(currentnode.getName())){
	          
				 
		                        		 int weight=layer.findlink(currentnode, node).getW();
						                       // System.out.println("weight:"+weight);
			       
		                        		 if(weight==0){
					                                //  System.out.println(layer.findlink(currentnode, node).getName()+"-"+"堵塞");
			
			    	  
		                        			 block_key=1; //堵塞，event 丢掉，不分配波长,block_number++
					   
		                        			 currentnode=node;
					                           
			       
		                        		 }
					                           
			       
		                        		 else{
					     
		                        			 currentnode=node;
			       
		                        		 }
			  
		                        	 }
		  
		                         }
		                }
		   }
		
		                         
		   return block_key;
      }
      public int Check_WC(Layer layer, Nodepair serach_nodepair,int number){
    	  
   	       //System.out.println(" 检测是否堵塞");
	       int route_find=0;
	       int block_key=1;
	       //检测是否堵塞
	       HashMap<String,Nodepair>map1=layer.getNodepairlist();
	       Iterator<String>iter1=map1.keySet().iterator();
		   while(iter1.hasNext()){
			            
			          
		                Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
		                //找到nodepair
		                if(nodepair.getName().endsWith(serach_nodepair.getName())){
		                	     
		                	     //找到route
		                         route_find=nodepair.getLinearlist().size();
	                             LinearRoute route=nodepair.getLinearlist().get(route_find-1);
	                             //检测堵塞
	                           
	                             int index=0;
	                             while(index<number){
	                            	 
	                            	 int key=0;
	                            	 Node currentnode=route.getNodelist().get(0);
	                            
	                            	 for(Node node:route.getNodelist()){
					                    if(!node.getName().equals(currentnode.getName())){
					                    	   //统计有多少未用的波长
					                    	   //System.out.println(layer.findlink(currentnode, node).getName());
					                    	   //layer.findlink(currentnode, node).Output_W_Ocppuy();
					                    	   if(layer.findlink(currentnode, node).getWC().get(index).getStatus()==0){
					                    		   currentnode=node;					                    		   
					                    	   }
					                    	   else{
					                    		   currentnode=node;	
					                    		   key=1;
					                    	   }
					                    }
	                            	 }
	                            	 //在次索引下有被占用
	                            	 if(key==1){
	                            		 index++;
	                            	 }
	                            	 //次索引的波长符合
	                            	 else{
	                            	//	System.out.println("符合索引"+index);
	                            		block_key=0; //未堵塞 
	                            		break; //跳出循环
	                            	 }
					                         						                      					                       
		                         }
		                     
		                }
		                
		   }
		return block_key;
     }
   
    
      public int CheckSlotUsedOfRoute(Layer layer, LinearRoute route,int TotalSlotNum, Time currentTime){
	      
    	  Algorithm workAlgorithm=new Algorithm();
	      //初始默认block_key为1既阻塞
    	  
    	  int block_key=1;
    	  //设置route占用的slot
    	  int slotNum=workAlgorithm.CalculateRouteSlot(route);
    	  
    	  
    	  //检测是否堵塞
	      int firstindex=0;
	      int lastindex=0;
    	  while(firstindex<TotalSlotNum){
    		  
    		  int key=0;
    		  Node currentnode=route.getNodelist().get(0);
    		  int textIndex=0;
    		  int failedSlotIndex=0;
    		  for(Node node:route.getNodelist()){ 			  
    			  if(!node.getName().equals(currentnode.getName())){
                 	  int currentLinkKey=0;
    				  lastindex=firstindex+slotNum-1;
    			
    				  //存在可用的
    				  if(lastindex<TotalSlotNum){
    					  
        				  int currentFailedSlotIndex=10000000;       				 
        				  if(checkSlotNumofLink(layer,firstindex,lastindex,currentnode,node)==0){
        					  currentnode=node;					                    		   
        				  }              	   
        				  else{   					
        					  currentFailedSlotIndex=FailedSlotIndex(layer,firstindex,lastindex,currentnode,node);
        					  currentnode=node;
        					  if(failedSlotIndex<currentFailedSlotIndex)
        						  failedSlotIndex=currentFailedSlotIndex;
        					  key=1;    					      					  
        				  }
    				  }
    				  else
    				  {
    					 
    					  failedSlotIndex=TotalSlotNum;
    					  key=1;
    					  break;
    				  }   				 
                 }
         	 }
         	 //在次索引下有被占用
         	 if(key==1){
         		firstindex=failedSlotIndex+1;
         		
         	 }
         	 //索引的波长符合
         	 else{
         		int endIndex=firstindex+slotNum-1;
         		currentTime.setOccpyfirstSlot(firstindex);
         		
         		currentTime.setOccpyendSlot(endIndex);
         		System.out.println("符合slot  "+currentTime.getOccpyfirstSlot()+"--"+currentTime.getOccpyendSlot());
         		block_key=0; //未堵塞 
         		break; //跳出循环
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
			  }
			  else{
				  currentLinkKey=1;
				  failedIndex=currentindex;
				  break;
			  }
		  }
    	  return failedIndex;
    	  
      }
}
