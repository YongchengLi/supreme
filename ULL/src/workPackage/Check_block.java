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
    	  
    	   //System.out.println(" ����Ƿ����");
	       int route_find=0;
	       int block_key=0;
	       //����Ƿ����
	       HashMap<String,Nodepair>map1=layer.getNodepairlist();
	       Iterator<String>iter1=map1.keySet().iterator();
		   while(iter1.hasNext()){
			            
			          
		                Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
		                //�ҵ�nodepair
		                if(nodepair.getName().endsWith(serach_nodepair.getName())){
		                	     
		                	     //�ҵ�route
		                         route_find=nodepair.getLinearlist().size();
	                             LinearRoute route=nodepair.getLinearlist().get(route_find-1);
	                             //������
	                             Node currentnode=route.getNodelist().get(0);
		                         for(Node node:route.getNodelist()){
					                    if(!node.getName().equals(currentnode.getName())){
					                           int weight=layer.findlink(currentnode, node).getW();
						                       // System.out.println("weight:"+weight);
					                           if(weight==0){
					                                //  System.out.println(layer.findlink(currentnode, node).getName()+"-"+"����");
					                                  block_key=1; //������event �����������䲨��,block_number++
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
    	  
    	  
   	   //System.out.println(" ����Ƿ����");
	     
	       int block_key=0;
	       //����Ƿ����
	       HashMap<String,Nodepair>map2=layer.getNodepairlist();
	       Iterator<String>iter2=map2.keySet().iterator();
		   while(iter2.hasNext()){
			            
			          
		                Nodepair nodepair=(Nodepair)(map2.get(iter2.next()));
		                //�ҵ�nodepair
		                if(nodepair.getName().endsWith(serach_nodepair.getName())){
		                	     
		                	     //�ҵ�route
		                	    
	                             Node currentnode=serach_nodepair.getLinearlist().get(route_index).getNodelist().get(0);
		                         for(Node node:serach_nodepair.getLinearlist().get(route_index).getNodelist()){
	      
			  
		                        	 if(!node.getName().equals(currentnode.getName())){
	          
				 
		                        		 int weight=layer.findlink(currentnode, node).getW();
						                       // System.out.println("weight:"+weight);
			       
		                        		 if(weight==0){
					                                //  System.out.println(layer.findlink(currentnode, node).getName()+"-"+"����");
			
			    	  
		                        			 block_key=1; //������event �����������䲨��,block_number++
					   
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
    	  
   	       //System.out.println(" ����Ƿ����");
	       int route_find=0;
	       int block_key=1;
	       //����Ƿ����
	       HashMap<String,Nodepair>map1=layer.getNodepairlist();
	       Iterator<String>iter1=map1.keySet().iterator();
		   while(iter1.hasNext()){
			            
			          
		                Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
		                //�ҵ�nodepair
		                if(nodepair.getName().endsWith(serach_nodepair.getName())){
		                	     
		                	     //�ҵ�route
		                         route_find=nodepair.getLinearlist().size();
	                             LinearRoute route=nodepair.getLinearlist().get(route_find-1);
	                             //������
	                           
	                             int index=0;
	                             while(index<number){
	                            	 
	                            	 int key=0;
	                            	 Node currentnode=route.getNodelist().get(0);
	                            
	                            	 for(Node node:route.getNodelist()){
					                    if(!node.getName().equals(currentnode.getName())){
					                    	   //ͳ���ж���δ�õĲ���
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
	                            	 //�ڴ��������б�ռ��
	                            	 if(key==1){
	                            		 index++;
	                            	 }
	                            	 //�������Ĳ�������
	                            	 else{
	                            	//	System.out.println("��������"+index);
	                            		block_key=0; //δ���� 
	                            		break; //����ѭ��
	                            	 }
					                         						                      					                       
		                         }
		                     
		                }
		                
		   }
		return block_key;
     }
   
    
      public int CheckSlotUsedOfRoute(Layer layer, LinearRoute route,int TotalSlotNum, Time currentTime){
	      
    	  Algorithm workAlgorithm=new Algorithm();
	      //��ʼĬ��block_keyΪ1������
    	  
    	  int block_key=1;
    	  //����routeռ�õ�slot
    	  int slotNum=workAlgorithm.CalculateRouteSlot(route);
    	  
    	  
    	  //����Ƿ����
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
    			
    				  //���ڿ��õ�
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
         	 //�ڴ��������б�ռ��
         	 if(key==1){
         		firstindex=failedSlotIndex+1;
         		
         	 }
         	 //�����Ĳ�������
         	 else{
         		int endIndex=firstindex+slotNum-1;
         		currentTime.setOccpyfirstSlot(firstindex);
         		
         		currentTime.setOccpyendSlot(endIndex);
         		System.out.println("����slot  "+currentTime.getOccpyfirstSlot()+"--"+currentTime.getOccpyendSlot());
         		block_key=0; //δ���� 
         		break; //����ѭ��
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
