package groupwork;
import general.Constant;
import general.Constraint;


import general.SlotWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import subgraph.*;
import Network.*;

public class pathSearch {
	public void Dijkstras(Node srcNode,Node desNode,Layer layer,LinearRoute newRoute,SearchConstraint constraint){
               ArrayList<Node>visitedNodeList=new ArrayList<Node>();	
               visitedNodeList.clear();
               
               //初始化
               HashMap<String,Node>map=layer.getNodelist();
               Iterator<String>iter=map.keySet().iterator();
               while(iter.hasNext()){
            	   Node node=(Node)(map.get(iter.next()));
            	   node.setStatus(Constant.UNVISITED);
            	   node.setParentnode(null);
            	   node.setCost_from_src(Constant.maxium);
            	   node.setHop_from_src(Constant.maxium);
               }
               Node currentNode=srcNode;
               currentNode.setCost_from_src(0);
               currentNode.setHop_from_src(0);
               currentNode.setStatus(Constant.VISITEDTWICE);
               if(constraint==null){
            	   for(Node node:currentNode.getNeinodelist()){
            		   if(node.getStatus()==Constant.UNVISITED){
            			   Link link=layer.findlink(currentNode, node);
            			   node.setCost_from_src(currentNode.getCost_from_src()+link.getCost());
            			   node.setHop_from_src(currentNode.getHop_from_src()+1);
            			   node.setStatus(Constant.VISITED);
            			   node.setParentnode(currentNode);
            			   visitedNodeList.add(node);
            		   }
            	   }
               }
               else{
            	   for(Node node:currentNode.getNeinodelist()){
            		   if(!constraint.getExcludednodelist().contains(node)){
            			   if(node.getStatus()==Constant.UNVISITED){
            				   Link link=layer.findlink(currentNode, node);
            				   node.setCost_from_src(currentNode.getCost_from_src()+link.getCost());
            				   node.setHop_from_src(currentNode.getHop_from_src()+1);
            				   node.setStatus(Constant.VISITED);
            				   node.setParentnode(currentNode);
                			   visitedNodeList.add(node);
            			   }
            		   }
            	   }
               }
               currentNode=this.getLowestCostNode(visitedNodeList);
               if(currentNode!=null){
            	   while(currentNode.equals(desNode)){
            		   currentNode.setStatus(Constant.VISITEDTWICE);
            	       visitedNodeList.remove(currentNode);
            	       if(constraint==null){
            	    	   for(Node node:currentNode.getNeinodelist()){
            	    		   if(node.getStatus()==Constant.UNVISITED){
            	    			   Link link=layer.findlink(currentNode, node);
            	    			   node.setCost_from_src(currentNode.getCost_from_src()+link.getCost());
            	    			   node.setHop_from_src(currentNode.getHop_from_src()+1);
            	    			   node.setStatus(Constant.VISITED);
            	    			   node.setParentnode(currentNode);
                    			   visitedNodeList.add(node);
            	    		   }
            	    		   else if(node.getStatus()==Constant.VISITED){
            	    			   Link link=layer.findlink(currentNode, node);
            	    			   if(node.getCost_from_src()>currentNode.getCost_from_src()+link.getCost()){
            	    			   node.setCost_from_src( currentNode.getCost_from_src()+link.getCost());
            	    			   node.setHop_from_src(currentNode.getHop_from_src()+1);
            	    		       node.setParentnode(currentNode);  
            	    		       }
            	    		   }
            	    	    }
            	       }
            	       else{
            	    	   for(Node node:currentNode.getNeinodelist()){
            	    		   if(!constraint.getExcludednodelist().contains(node)){
            	    			   if(node.getStatus()==Constant.UNVISITED){
            	    				   Link link=layer.findlink(currentNode, node);
            	    				   if(!constraint.getExcludedlinklist().contains(link)){
            	    					   node.setCost_from_src(currentNode.getCost_from_src()+link.getCost());
            	    					   node.setHop_from_src(currentNode.getHop_from_src()+1);
            	    					   node.setStatus(Constant.VISITED);
            	    					   node.setParentnode(currentNode);
            	    					   visitedNodeList.add(node);
            	    				   }
            	    			   }
            	    				   else if(node.getStatus()==Constant.VISITED){
            	    					  Link link=layer.findlink(currentNode, node);
            	    					  if(!constraint.getExcludedlinklist().contains(link)){
            	    						  if(node.getCost_from_src()>currentNode.getCost_from_src()+link.getCost()){
            	    						  node.setCost_from_src( currentNode.getCost_from_src()+link.getCost());
            	    					      node.setHop_from_src(currentNode.getHop_from_src()+1);
            	    						  node.setParentnode(currentNode);
            	    					  }
            	    			   }
            	    		   }
            	    	   }
            	       }
            	   }
            	   currentNode =this.getLowestCostNode(visitedNodeList);
            	   if(currentNode==null);
            	   break;
               }
           }
          newRoute.getNodelist().clear();
          newRoute.getLinklist().clear();
          newRoute.getObjetc().clear();
          currentNode=desNode;
          if(desNode.getParentnode()!=null){
        	  newRoute.getNodelist().add(0, currentNode);
        	  newRoute.getObjetc().add(0, currentNode);
        	  
        	  while(currentNode!=srcNode){
        		  Link link=layer.findlink(currentNode,currentNode.getParentnode());
        		  newRoute.getObjetc().add(0, link);
        		  newRoute.getLinklist().add(0, link);
        		  
        		  currentNode=currentNode.getParentnode();
        		  newRoute.getNodelist().add(0, currentNode);
            	  newRoute.getObjetc().add(0, currentNode);
        	  }
          }
	}
	public Node getLowestCostNode(ArrayList<Node> visitedNodeList){
		Node currentNode=null;
		double current_cost_to_desc=Constant.maxium;
		for(Node node:visitedNodeList){
			if(node.getCost_from_src()<current_cost_to_desc){
				currentNode=node;
				current_cost_to_desc=node.getCost_from_src();
			}
		}
		return currentNode;
		
	}
	public void Dijkstras(String name1,String name2,Layer layer,LinearRoute newRoute,SearchConstraint constraint){
        Node srcNode=layer.findNode(name1, layer);
        Node desNode=layer.findNode(name2, layer);
		ArrayList<Node>visitedNodeList=new ArrayList<Node>();	
        visitedNodeList.clear();
        
        //初始化
        HashMap<String,Node>map=layer.getNodelist();
        Iterator<String>iter=map.keySet().iterator();
        while(iter.hasNext()){
     	   Node node=(Node)(map.get(iter.next()));
     	   node.setStatus(Constant.UNVISITED);
     	   node.setParentnode(null);
     	   node.setCost_from_src(Constant.maxium);
     	   node.setHop_from_src(Constant.maxium);
        }
        Node currentNode=srcNode;
        currentNode.setCost_from_src(0);
        currentNode.setHop_from_src(0);
        currentNode.setStatus(Constant.VISITEDTWICE);
        if(constraint==null){
     	   for(Node node:currentNode.getNeinodelist()){
     		   if(node.getStatus()==Constant.UNVISITED){
     			   Link link=layer.findlink(currentNode, node);
     			   node.setCost_from_src(currentNode.getCost_from_src()+link.getCost());
     			   node.setHop_from_src(currentNode.getHop_from_src()+1);
     			   node.setStatus(Constant.VISITED);
     			   node.setParentnode(currentNode);
     			   visitedNodeList.add(node);
     		   }
     	   }
        }
        else{
     	   for(Node node:currentNode.getNeinodelist()){
     		   if(!constraint.getExcludednodelist().contains(node)){
     			   if(node.getStatus()==Constant.UNVISITED){
     				   Link link=layer.findlink(currentNode, node);
     				  if(!constraint.getExcludedlinklist().contains(link)){
     				   node.setCost_from_src(currentNode.getCost_from_src()+link.getCost());
     				   node.setHop_from_src(currentNode.getHop_from_src()+1);
     				   node.setStatus(Constant.VISITED);
     				   node.setParentnode(currentNode);
         			   visitedNodeList.add(node);
     				  }
     			   }
     		   }
     	   }
        }
        currentNode=getLowestCostNode(visitedNodeList);
        if(currentNode!=null){
     	   while(currentNode.equals(desNode)){
     		   currentNode.setStatus(Constant.VISITEDTWICE);
     	       visitedNodeList.remove(currentNode);
     	       if(constraint==null){
     	    	   for(Node node:currentNode.getNeinodelist()){
     	    		   if(node.getStatus()==Constant.UNVISITED){
     	    			   Link link=layer.findlink(currentNode, node);
     	    			   node.setCost_from_src(currentNode.getCost_from_src()+link.getCost());
     	    			   node.setHop_from_src(currentNode.getHop_from_src()+1);
     	    			   node.setStatus(Constant.VISITED);
     	    			   node.setParentnode(currentNode);
             			   visitedNodeList.add(node);
     	    		   }
     	    		   else if(node.getStatus()==Constant.VISITED){
     	    			   Link link=layer.findlink(currentNode, node);
     	    			   if(node.getCost_from_src()>currentNode.getCost_from_src()+link.getCost()){
     	    			   node.setCost_from_src( currentNode.getCost_from_src()+link.getCost());
     	    			   node.setHop_from_src(currentNode.getHop_from_src()+1);
     	    		       node.setParentnode(currentNode);  
     	    		       }
     	    		   }
     	    	    }
     	       }
     	       else{
     	    	   for(Node node:currentNode.getNeinodelist()){
     	    		   if(!constraint.getExcludednodelist().contains(node)){
     	    			   if(node.getStatus()==Constant.UNVISITED){
     	    				   Link link=layer.findlink(currentNode, node);
     	    				   if(!constraint.getExcludedlinklist().contains(link)){
     	    					   node.setCost_from_src(currentNode.getCost_from_src()+link.getCost());
     	    					   node.setHop_from_src(currentNode.getHop_from_src()+1);
     	    					   node.setStatus(Constant.VISITED);
     	    					   node.setParentnode(currentNode);
     	    					   visitedNodeList.add(node);
     	    				   }
     	    			   }
     	    				   else if(node.getStatus()==Constant.VISITED){
     	    					  Link link=layer.findlink(currentNode, node);
     	    					  if(!constraint.getExcludedlinklist().contains(link)){
     	    						  if(node.getCost_from_src()>currentNode.getCost_from_src()+link.getCost()){
     	    						  node.setCost_from_src( currentNode.getCost_from_src()+link.getCost());
     	    					      node.setHop_from_src(currentNode.getHop_from_src()+1);
     	    						  node.setParentnode(currentNode);
     	    					
     	    						  }    	    			  
     	    					  }
     	    				   }    	    	  
     	    		   }    	      
     	    	   }    	   
     	       }    	
     	       currentNode =this.getLowestCostNode(visitedNodeList);   	     	 
     	       if(currentNode==null);
     	   	
     	       break;
     
     	   }
   
        }  
        newRoute.getNodelist().clear();  
        newRoute.getLinklist().clear();   
        newRoute.getObjetc().clear();  
        currentNode=desNode;   
        if(desNode.getParentnode()!=null){	  
        	newRoute.getNodelist().add(0, currentNode); 	  
        	newRoute.getObjetc().add(0, currentNode); 	  
        	while(currentNode!=srcNode){		  
        		Link link=layer.findlink(currentNode,currentNode.getParentnode());		  
        		newRoute.getObjetc().add(0, link);		  
        		newRoute.getLinklist().add(0, link);	   		
        		currentNode=currentNode.getParentnode(); 
        		newRoute.getNodelist().add(0, currentNode);	  
        		newRoute.getObjetc().add(0, currentNode);
 	        	}
         }
	}
	/**
	 * find K-disjoint shortest path routes
	 */
	public int Kshortest(Node srcNode, Node destNode, Layer layer, int k, ArrayList<LinearRoute> routelist) {
		
		routelist.clear();
		SearchConstraint constraint = new SearchConstraint(1000000, 100000);
		
		int num_found = 0; //number of found route
		while(true){
			LinearRoute newRoute = new LinearRoute("",	0, "", Constant.WORKING);
			this.Dijkstras(srcNode, destNode, layer, newRoute, constraint);
			if(newRoute.getLinklist().size()>0){
				routelist.add(newRoute);
				constraint.getExcludedlinklist().addAll(newRoute.getLinklist());
				num_found++;
				if(num_found == k)
					break;
			}
			else
				break;
		}
		return num_found; // the number of found routes
	}
	
	
	 
	public void SearchPathWithDijkstras(Node srcNode,Node desNode,Layer layer,LinearRoute newRoute,Constraint constraint)
	    
	 {  	
	    	
		 ArrayList<Node>visitedNodeList=new ArrayList<Node>();		    	
		 visitedNodeList.clear();
	    	//初始化      
	    	HashMap<String,Node>map=layer.getNodelist();       
	    	Iterator<String>iter=map.keySet().iterator();     
	    	while(iter.hasNext()){      	
	    		Node node=(Node)(map.get(iter.next()));    	   
	    		node.setStatus(constraint.getUNVISITED());      	  
	    		node.setParentnode(null);     
	    		node.setCost_from_src(constraint.getMaxium());//设置无穷大      	  
	    		node.setHop_from_src(constraint.getMaxium());        
	    	}          
	    	//查找临节点         
	    	Node currentNode=srcNode;//当前节点       
	    	currentNode.setCost_from_src(0);       
	    	currentNode.setHop_from_src(0);         
	    	currentNode.setStatus(constraint.VISITEDTWICE);
	    	//如果当前节点满足级数在限制范围之内且该维度上节点数满足要求则沿着该节点寻找子节点    
	    	for(Node node:currentNode.getNeinodelist())//遍历临节点         	        	
	    	{   		
	    		if(!constraint.getExcludedNodelist().contains(node)){ 		            			
	    			if(node.getStatus()==constraint.getUNVISITED())	                		
	    			{      			  	                		
	    				Link link=layer.findlink(currentNode, node); 	                		
	    				//System.out.println(link.getName()+" "+link.getCost());
	    				if(!constraint.getExcludedLinklist().contains(link)){	                			
	    					node.setCost_from_src(currentNode.getCost_from_src()+link.getCost());      				                    		
	    					node.setHop_from_src(currentNode.getHop_from_src()+1);       				                    		
	    					node.setParentnode(currentNode);  	                    		
	    					node.setStatus(constraint.VISITED);	        					
	    					visitedNodeList.add(node);	                			
	    				}        	    	                		
	    			}       			            		 			    			
	    		}      
	    	}      
	        	
	    	
	    
	    	currentNode=this.getLowestCostNode(visitedNodeList);//找到最短的节点              

	    	if(currentNode!=null)//找到的最短路劲节点不为空       
	    	{       	
	    		while(!currentNode.equals(desNode))
	    		{    	    			
	    			//set the current node double visited
					currentNode.setStatus(constraint.VISITEDTWICE);
					//remove the node from the visited node list
					visitedNodeList.remove(currentNode);	
					//如果当前节点满足级数在限制范围之内且该维度上节点数满足要求则沿着该节点寻找子节点
					
						
					for(Node node : currentNode.getNeinodelist())						
					{							
						if(!constraint.getExcludedNodelist().contains(node))
							
						{					
								
							if(node.getStatus() == constraint.UNVISITED)
			
							{ //if the neighbor node is not visited		
								
								Link link = layer.findlink(currentNode,node);
								
								if(!constraint.getExcludedLinklist().contains(link)){	                			
									node.setCost_from_src(currentNode.getCost_from_src()+link.getCost());
									node.setHop_from_src(currentNode.getHop_from_src()+1);
									node.setStatus(constraint.VISITED);
									node.setParentnode(currentNode);
									visitedNodeList.add(node);

								}						
								
							
							}
								
							else if(node.getStatus() == constraint.VISITED){ //if the neighbor node is first visited
								String name;
								if(currentNode.getIndex()<node.getIndex())
									name = currentNode.getName()+"-"+node.getName();
								else
									name = node.getName()+"-"+currentNode.getName();
								Link link = layer.getLinklist().get(name);
								if(!constraint.getExcludedLinklist().contains(link)){	                			
									if(node.getCost_from_src() > currentNode.getCost_from_src() + link.getCost()){
										//update the node status
										node.setCost_from_src(currentNode.getCost_from_src() + link.getCost());
										node.setHop_from_src(currentNode.getHop_from_src()+1);
										node.setParentnode(currentNode);						
									}									
								}											
							}			        										
						}													
					}        
					currentNode =this.getLowestCostNode(visitedNodeList);	       	        
					if(currentNode==null)break;
	    		
	    		}
	          
	    	
	    	}
	     
	    	newRoute.getNodelist().clear();     
	    	newRoute.getLinklist().clear();    
	    	currentNode=desNode;   
	    	if(desNode.getParentnode()!=null)
	    	{ 	  
	    		newRoute.getNodelist().add(0, currentNode);  	 
	    		while(currentNode!=srcNode){ 		  
	    			Link link=layer.findlink(currentNode,currentNode.getParentnode());  		
	    			Link srclink = layer.findSymLink(link);
	    			newRoute.getLinklist().add(0, srclink);   		 
	    			currentNode=currentNode.getParentnode();  		 
	    			newRoute.getNodelist().add(0, currentNode);   	  
	    		}   
	    	}    	
	    }
	 
	
	/**
	 * search the timeleast path
	 * @param srcNode
	 * @param desNode
	 * @param layer
	 * @param newRoute
	 * @param constraint
	 */
	/*
	public void AdaptivePathWithDijkstras(Node srcNode,Node desNode,Layer layer,LinearRoute newRoute,Constraint constraint)
    
	 {  	
	    	
		 ArrayList<Node>visitedNodeList=new ArrayList<Node>();		    	
		 visitedNodeList.clear();
	    	//初始化      
	    	HashMap<String,Node>map=layer.getNodelist();       
	    	Iterator<String>iter=map.keySet().iterator();     
	    	while(iter.hasNext()){      	
	    		Node node=(Node)(map.get(iter.next()));    	   
	    		node.setStatus(constraint.getUNVISITED());      	  
	    		node.setParentnode(null);     
	    		node.setCost_from_src(constraint.getMaxium());//设置无穷大      	  
	    		node.setHop_from_src(constraint.getMaxium());        
	    	}          
	    	//查找临节点         
	    	Node currentNode=srcNode;//当前节点       
	    	currentNode.setCost_from_src(0);       
	    	currentNode.setHop_from_src(0);         
	    	currentNode.setStatus(constraint.VISITEDTWICE);
	    	
	    	for(Node node:currentNode.getNeinodelist())//遍历临节点         	        	
	    	{   		
	    		if(!constraint.getExcludedNodelist().contains(node)){ 		            			
	    			if(node.getStatus()==constraint.getUNVISITED())	                		
	    			{      			  	                		
	    				Link link=layer.findlink(currentNode, node); 	                		
	    				//System.out.println(link.getName()+" "+link.getCost());
	    				if(!constraint.getExcludedLinklist().contains(link)){	                			
	    					double startTime = Math.min(link.getResidualCapacity(),node.getAvaStartTime());
	    					double avaCapacity = Math.min(link.getResidualCapacity(),currentNode.getAvaCapacity());
	    					double cost = startTime+20/avaCapacity;
	    					
	    					node.setCost_from_src(cost);      				                    		
	    					node.setHop_from_src(currentNode.getHop_from_src()+1);       				                    		
	    					
	    					node.setAvaCapacity(avaCapacity);
	    					
	    					
	    					node.setParentnode(currentNode);  	                    		
	    					node.setStatus(constraint.VISITED);	        					
	    					visitedNodeList.add(node);	                			
	    				}        	    	                		
	    			}       			            		 			    			
	    		}      
	    	}      
	        	
	    	
	    
	    	currentNode=this.getLowestCostNode(visitedNodeList);//找到最短的节点              

	    	if(currentNode!=null)//找到的最短路劲节点不为空       
	    	{       	
	    		while(!currentNode.equals(desNode))
	    		{    	    			
	    			//set the current node double visited
					currentNode.setStatus(constraint.VISITEDTWICE);
					//remove the node from the visited node list
					visitedNodeList.remove(currentNode);	
					//如果当前节点满足级数在限制范围之内且该维度上节点数满足要求则沿着该节点寻找子节点
					
						
					for(Node node : currentNode.getNeinodelist())						
					{							
						if(!constraint.getExcludedNodelist().contains(node))
							
						{					
								
							if(node.getStatus() == constraint.UNVISITED)
			
							{ //if the neighbor node is not visited		
								
								Link link = layer.findlink(currentNode,node);
								
								if(!constraint.getExcludedLinklist().contains(link)){	                			
									node.setCost_from_src(currentNode.getCost_from_src()+link.getCost());
									node.setHop_from_src(currentNode.getHop_from_src()+1);
									node.setStatus(constraint.VISITED);
									node.setParentnode(currentNode);
									visitedNodeList.add(node);

								}						
								
							
							}
								
							else if(node.getStatus() == constraint.VISITED){ //if the neighbor node is first visited
								String name;
								if(currentNode.getIndex()<node.getIndex())
									name = currentNode.getName()+"-"+node.getName();
								else
									name = node.getName()+"-"+currentNode.getName();
								Link link = layer.getLinklist().get(name);
								if(!constraint.getExcludedLinklist().contains(link)){	                			
									if(node.getCost_from_src() > currentNode.getCost_from_src() + link.getCost()){
										//update the node status
										node.setCost_from_src(currentNode.getCost_from_src() + link.getCost());
										node.setHop_from_src(currentNode.getHop_from_src()+1);
										node.setParentnode(currentNode);						
									}									
								}											
							}			        										
						}													
					}        
					currentNode =this.getLowestCostNode(visitedNodeList);	       	        
					if(currentNode==null)break;
	    		
	    		}
	          
	    	
	    	}
	     
	    	newRoute.getNodelist().clear();     
	    	newRoute.getLinklist().clear();    
	    	currentNode=desNode;   
	    	if(desNode.getParentnode()!=null)
	    	{ 	  
	    		newRoute.getNodelist().add(0, currentNode);  	 
	    		while(currentNode!=srcNode){ 		  
	    			Link link=layer.findlink(currentNode,currentNode.getParentnode());  		 
	    			newRoute.getLinklist().add(0, link);   		 
	    			currentNode=currentNode.getParentnode();  		 
	    			newRoute.getNodelist().add(0, currentNode);   	  
	    		}   
	    	}    	
	    }
	 
	*/
	 
	 
	 public void leastDelayTime(Node currentNode,Layer layer)
	 {
		 
	 }
	
	 /**
		 * 在ip层进行路由搜索
		 * @param srcNode
		 * @param desNode
		 * @param layer
		 * @param newRoute
		 */
		public void SearchPathWithDijkstrasInIpLayer(Node srcNode,Node desNode,Layer layer,LinearRoute newRoute,Constraint constraint)
		{  	
			    		
			ArrayList<Node>visitedNodeList=new ArrayList<Node>();		    				 
			visitedNodeList.clear();		    	
			//初始化      
			HashMap<String,Node>map=layer.getNodelist();       
	    	Iterator<String>iter=map.keySet().iterator();     
	    	while(iter.hasNext()){      	
	    		Node node=(Node)(map.get(iter.next()));    	   
	    		node.setStatus(constraint.getUNVISITED());      	  
	    		node.setParentnode(null);     
	    		node.setCost_from_src(constraint.getMaxium());//设置无穷大      	  
	    		node.setHop_from_src(constraint.getMaxium());        
	    	}          
			    	
	    	//查找临节点         
	    	Node currentNode=srcNode;//当前节点       
	    	currentNode.setCost_from_src(0);       
	    	currentNode.setHop_from_src(0);         
	    	currentNode.setStatus(constraint.VISITEDTWICE);
	    	//如果当前节点满足级数在限制范围之内且该维度上节点数满足要求则沿着该节点寻找子节点    
	    	for(Node node:currentNode.getVirtualNeiNodelist())//遍历临节点         	        	
	    	{   		
	    		if(!constraint.getExcludedNodelist().contains(node)){ 		            			
	    			if(node.getStatus()==constraint.getUNVISITED())	                		
	    			{      			  	                		
	    				Link link=layer.findlink(currentNode, node); 	                		
	    				if(!constraint.getExcludedLinklist().contains(link)){	                			
	    					node.setCost_from_src(currentNode.getCost_from_src()+link.getCost());      				                    		
	    					node.setHop_from_src(currentNode.getHop_from_src()+1);       				                    		
	    					node.setParentnode(currentNode);  	                    		
	    					node.setStatus(constraint.VISITED);	        					
	    					visitedNodeList.add(node);	                			
	    				}        	    	                		
	    			}       			            		 			    			
	    		}      
	    	}          
	    	currentNode=this.getLowestCostNode(visitedNodeList);//找到最短的节点              

	    	if(currentNode!=null)//找到的最短路劲节点不为空       
	    	{       	
	    		while(!currentNode.equals(desNode))
	    		{    	    			
	    			//set the current node double visited
					currentNode.setStatus(constraint.VISITEDTWICE);
					//remove the node from the visited node list
					visitedNodeList.remove(currentNode);	
					//如果当前节点满足级数在限制范围之内且该维度上节点数满足要求则沿着该节点寻找子节点				
					for(Node node : currentNode.getVirtualNeiNodelist())						
					{							
						if(!constraint.getExcludedNodelist().contains(node))					
						{											
							if(node.getStatus() == constraint.UNVISITED)	
							{ //if the neighbor node is not visited								
								Link link = layer.findlink(currentNode,node);						
								if(!constraint.getExcludedLinklist().contains(link)){	                			
									node.setCost_from_src(currentNode.getCost_from_src()+link.getCost());
									node.setHop_from_src(currentNode.getHop_from_src()+1);
									node.setStatus(constraint.VISITED);
									node.setParentnode(currentNode);
									visitedNodeList.add(node);
								}																			
							}			
							else if(node.getStatus() == constraint.VISITED){ //if the neighbor node is first visited
								String name;
								if(currentNode.getIndex()<node.getIndex())
									name = currentNode.getName()+"-"+node.getName();
								else
									name = node.getName()+"-"+currentNode.getName();
								Link link = layer.getLinklist().get(name);
								if(!constraint.getExcludedLinklist().contains(link)){	                			
									if(node.getCost_from_src() > currentNode.getCost_from_src() + link.getCost()){
										//update the node status
										node.setCost_from_src(currentNode.getCost_from_src() + link.getCost());
										node.setHop_from_src(currentNode.getHop_from_src()+1);
										node.setParentnode(currentNode);						
									}									
								}											
							}			        										
						}													
					}        
					currentNode =this.getLowestCostNode(visitedNodeList);	       	        
					if(currentNode==null)break;		
	    		}
	    	}  
	    	newRoute.getNodelist().clear();     
	    	newRoute.getLinklist().clear();    
	    	currentNode=desNode;   
	    	if(desNode.getParentnode()!=null)
	    	{ 	  
	    		newRoute.getNodelist().add(0, currentNode);  	 
	    		while(currentNode!=srcNode){ 		  
	    			Link link=layer.findlink(currentNode,currentNode.getParentnode());  		 
	    			newRoute.getLinklist().add(0, link);   		 
	    			currentNode=currentNode.getParentnode();  		 
	    			newRoute.getNodelist().add(0, currentNode);   	  
	    		}   
	    	}    	
	    }
}
