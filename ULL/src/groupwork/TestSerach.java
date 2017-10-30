package groupwork;
import general.Constant;
import general.Constraint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import subgraph.*;

import Network.*;

public class TestSerach {
		public void Dijkstras(Node srcNode,Node desNode,Layer layer,LinearRoute newRoute){
			  
			    ArrayList<Node>visitedNodeList=new ArrayList<Node>();	
			    //file_out_put file=new file_out_put();
	            visitedNodeList.clear();
	            //��ʼ��
	            HashMap<String,Node>map=layer.getNodelist();
	            Iterator<String>iter=map.keySet().iterator();
	            while(iter.hasNext()){
	         	   Node node=(Node)(map.get(iter.next()));
	         	   node.setStatus(Constant.UNVISITED);
	         	   node.setParentnode(null);
	         	   node.setCost_from_src(Constant.maxium);//���������
	         	   node.setHop_from_src(Constant.maxium);
	            }
	            //�����ٽڵ�
	            Node currentNode=srcNode;//��ǰ�ڵ�
	            currentNode.setCost_from_src(0);
	            currentNode.setHop_from_src(0);
	            currentNode.setStatus(Constant.VISITED);
	            //System.out.println("�ڵ�·��  "+ currentNode.getName());
	            //file.filewrite("e:/t2.txt" , "�ڵ�·��  "+ currentNode.getName());
	            for(Node node:currentNode.getNeinodelist())//�����ٽڵ� 
	            {
	         	           if(node.getStatus()==Constant.UNVISITED){
	         			   Link link=layer.findlink(currentNode, node);
	         			   node.setCost_from_src(currentNode.getCost_from_src()+link.getCost());
	         			   node.setHop_from_src(currentNode.getHop_from_src()+1);
	         			   node.setParentnode(currentNode);
	         		   }
	         	   }
	            currentNode=this.getLowestCostNode(layer);//�ҵ���̵Ľڵ�
	          
	            if(currentNode!=null)//�ҵ������·���ڵ㲻Ϊ��
	            {
	         	   while(!currentNode.equals(desNode)){
	         	       for(Node node:currentNode.getNeinodelist())
	         	       //����
	         	    	   {
	         	    		 if(node.getStatus()==Constant.UNVISITED){
	         	    			   Link link=layer.findlink(currentNode, node);
	         	                   if(node.getCost_from_src() > currentNode.getCost_from_src() + link.getCost()){
	         	    			          node.setCost_from_src(currentNode.getCost_from_src()+link.getCost());
	         	    			          node.setHop_from_src(currentNode.getHop_from_src()+1);
	         	    			       //   System.out.println(node.getCost_from_src());
	         	    			          node.setParentnode(currentNode);
	                 			         // System.out.println("�ڵ�·��  "+ currentNode.getName());
	                 			         //file.filewrite("e:/t2.txt" , "�ڵ�·��  "+ currentNode.getName());
	         	                 }
	         	    		   }
	         	       }
	         	        currentNode =this.getLowestCostNode(layer);
	         	        //System.out.println("�ڵ�·��  "+ currentNode.getName());
	         	       //file.filewrite("e:/t2.txt" , "�ڵ�·��  "+ currentNode.getName());
	         	        if(currentNode==null)break;
	         	   }
	            }
	       newRoute.getNodelist().clear();
	       newRoute.getLinklist().clear();
	       currentNode=desNode;
	       if(desNode.getParentnode()!=null){
	     	  newRoute.getNodelist().add(0, currentNode);
	     	  while(currentNode!=srcNode){
	     		  Link link=layer.findlink(currentNode,currentNode.getParentnode());
	     		  newRoute.getLinklist().add(0, link);
	     		  currentNode=currentNode.getParentnode();
	     		  newRoute.getNodelist().add(0, currentNode);
	     	  }
	       }
		}
		public Node getLowestCostNode(Layer layer){
			
			Node currentnode = null;
			double current_cost_to_desc =Constant.maxium;
		    HashMap<String,Node>map=layer.getNodelist();
            Iterator<String>iter=map.keySet().iterator();
            while(iter.hasNext()){
         	   Node node=(Node)(map.get(iter.next()));
                if(node.getStatus()==Constant.UNVISITED){
				      if(node.getCost_from_src() < current_cost_to_desc){
					             currentnode = node;
					             current_cost_to_desc = node.getCost_from_src();
					            // System.out.println(node.getName()+"-"+node.getCost_from_src());
					             
				}			
			}
                /*else{
                	 System.out.println(node.getName()+"-"+node.getCost_from_src());
                }
	      */}
            if(currentnode!=null)
            	currentnode.setStatus(Constant.VISITED);
			return currentnode;		
		}
  
		public void Dijkstras(String name1,String name2,Layer layer,LinearRoute newRoute){
	        Node srcNode=layer.findNode(name1, layer);
	        Node desNode=layer.findNode(name2, layer);
	        //file_out_put file=new file_out_put();
            ArrayList<Node>visitedNodeList=new ArrayList<Node>();	
            visitedNodeList.clear();
            
            //��ʼ��
            HashMap<String,Node>map=layer.getNodelist();
            Iterator<String>iter=map.keySet().iterator();
            while(iter.hasNext()){
         	   Node node=(Node)(map.get(iter.next()));
         	   node.setStatus(Constant.UNVISITED);
         	   node.setParentnode(null);
         	   node.setCost_from_src(Constant.maxium);//���������
         	   node.setHop_from_src(Constant.maxium);
            }
            //�����ٽڵ�
            Node currentNode=srcNode;//��ǰ�ڵ�
            currentNode.setCost_from_src(0);
            currentNode.setHop_from_src(0);
            currentNode.setStatus(Constant.VISITED);
            //System.out.println("�ڵ�·��  "+ currentNode.getName());
            //file.filewrite("e:/t2.txt" , "�ڵ�·��  "+ currentNode.getName());
            for(Node node:currentNode.getNeinodelist())//�����ٽڵ� 
            {
         	           if(node.getStatus()==Constant.UNVISITED){
         			   Link link=layer.findlink(currentNode, node);
         			   node.setCost_from_src(currentNode.getCost_from_src()+link.getCost());
         			   node.setHop_from_src(currentNode.getHop_from_src()+1);
         			   node.setParentnode(currentNode);
         		   }
         	   }
            if(currentNode.getNeinodelist().size()!=0){
                 currentNode=this.getLowestCostNode(layer);//�ҵ���̵Ľڵ�
                 //System.out.println("�ڵ�·��  "+ currentNode.getName());
                 //file.filewrite("e:/t2.txt" , "�ڵ�·��  "+ currentNode.getName());
                 if(currentNode!=null)//�ҵ������·���ڵ㲻Ϊ��
                {
         	          while(!currentNode.equals(desNode)){
         		          if(currentNode.getNeinodelist().size()==0){
         			              currentNode=null;
         			              break;
         		            }
         		          else{
         	                     for(Node node:currentNode.getNeinodelist())
         	       //��         	     
         	                    	 {
         	    		              if(node.getStatus()==Constant.UNVISITED){
         	    			                 Link link=layer.findlink(currentNode, node);
         	                                 if(node.getCost_from_src() > currentNode.getCost_from_src() + link.getCost()){
         	    			                 node.setCost_from_src(currentNode.getCost_from_src()+link.getCost());
         	    			                 node.setHop_from_src(currentNode.getHop_from_src()+1);
         	    			                 //System.out.println(node.getCost_from_src());
         	    			                //file.filewrite("e:/t2.txt" ,node.getCost_from_src() );
         	    			                 node.setParentnode(currentNode);
         	                                 }
         	    		               }
         	                    	 }
         		          
         	                          currentNode =this.getLowestCostNode(layer);
         	                          //System.out.println("�ڵ�·��  "+ currentNode.getName());
         	                         // file.filewrite("e:/t2.txt","�ڵ�·��  "+ currentNode.getName());
         	                          if(currentNode==null)break;
         		   }
         	   }
            }
            }//�������·��
       newRoute.getNodelist().clear();
       newRoute.getLinklist().clear();
       currentNode=desNode;
       if(desNode.getParentnode()!=null){
     	  newRoute.getNodelist().add(0, currentNode);
     	  while(currentNode!=srcNode){
     		  Link link=layer.findlink(currentNode.getParentnode(),currentNode);
     		  newRoute.getLinklist().add(0, link);
     		  currentNode=currentNode.getParentnode();
     		  newRoute.getNodelist().add(0, currentNode);
     	  }
       }
	}
  
  
  
		/**
	 
	 * find K-disjoint shortest path routes
	 */
	
		public int Kshortest(Node srcNode, Node destNode, Layer layer, int k, ArrayList<LinearRoute> routelist) {
		
		routelist.clear();
		ArrayList<Link>RemoveLinklist=new ArrayList<Link>();
		Layer searchLayer=layer;
		int num_found = 0; //number of found route
		while(true){
			LinearRoute newRoute = new LinearRoute("",	0, "", Constant.WORKING);
			this.Dijkstras(srcNode, destNode, searchLayer, newRoute);
		   // System.out.println("*****");
		  //	newRoute.OutputRoute_link(newRoute);
			
			Layer newLayer=new Layer("Textlayer", 0, "");
			if(newRoute.getLinklist().size()>0){
				routelist.add(newRoute);
				
				newLayer.setNodelist(searchLayer.getNodelist());
				newLayer.setLinklist(searchLayer.getLinklist());
				newLayer.setNodepairlist(searchLayer.getNodepairlist());
				
				 
				//System.out.println(newLayer.getLink_num());
				for(Link routeLinks:newRoute.getLinklist())
				{
					newLayer.removeLink(routeLinks.getName());
					String AnotherName=routeLinks.getNodeB().getName()+"-"+routeLinks.getNodeA().getName();
					Link AnothetLink=newLayer.findlink(routeLinks.getNodeB(), routeLinks.getNodeA());
					newLayer.removeLink(AnotherName);
					
					
					routeLinks.getNodeA().removeNeiNode(routeLinks.getNodeB());
					routeLinks.getNodeB().removeNeiNode(routeLinks.getNodeA());
					
					
			
					
					
					
					RemoveLinklist.add(routeLinks);
					RemoveLinklist.add(AnothetLink);
				}
				//System.out.println(newLayer.getLink_num());
				
				
				
			
		        
				searchLayer=newLayer;
				num_found++;
				
				if(num_found == k)
					break;
				else if( srcNode.getNeinodelist().size()==0||destNode.getNeinodelist().size()==0)
					break;
			}
			else
				break;
		}
		
		for(Link routeLinks:RemoveLinklist)
		{
			layer.addlink(routeLinks);
			
			routeLinks.getNodeA().addNeiNode(routeLinks.getNodeB());

		
		}
		return num_found; // the number of found routes
	}

		
	//************************************************************************//
	/**
	 * ��ip�����·������
	 * @param srcNode
	 * @param desNode
	 * @param layer
	 * @param newRoute
	 */
	public void SearchPathWithDijkstras(Node srcNode,Node desNode,Layer layer,LinearRoute newRoute,Constraint constraint)
	{  	
		    		
		ArrayList<Node>visitedNodeList=new ArrayList<Node>();		    				 
		visitedNodeList.clear();		    	
		//��ʼ��      
		HashMap<String,Node>map=layer.getNodelist();       
    	Iterator<String>iter=map.keySet().iterator();     
    	while(iter.hasNext()){      	
    		Node node=(Node)(map.get(iter.next()));    	   
    		node.setStatus(constraint.getUNVISITED());      	  
    		node.setParentnode(null);     
    		node.setCost_from_src(constraint.getMaxium());//���������      	  
    		node.setHop_from_src(constraint.getMaxium());        
    	}          
		    	
    	//�����ٽڵ�         
    	Node currentNode=srcNode;//��ǰ�ڵ�       
    	currentNode.setCost_from_src(0);       
    	currentNode.setHop_from_src(0);         
    	currentNode.setStatus(constraint.VISITEDTWICE);
    	//�����ǰ�ڵ����㼶�������Ʒ�Χ֮���Ҹ�ά���Ͻڵ�������Ҫ�������Ÿýڵ�Ѱ���ӽڵ�    
    	for(Node node:currentNode.getVirtualNeiNodelist())//�����ٽڵ�         	        	
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
    	currentNode=this.getLowestCostNode(visitedNodeList);//�ҵ���̵Ľڵ�              

    	if(currentNode!=null)//�ҵ������·���ڵ㲻Ϊ��       
    	{       	
    		while(!currentNode.equals(desNode))
    		{    	    			
    			//set the current node double visited
				currentNode.setStatus(constraint.VISITEDTWICE);
				//remove the node from the visited node list
				visitedNodeList.remove(currentNode);	
				//�����ǰ�ڵ����㼶�������Ʒ�Χ֮���Ҹ�ά���Ͻڵ�������Ҫ�������Ÿýڵ�Ѱ���ӽڵ�				
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
		
		
	/**
	 * �ҵ���Сcost
	 * @param visitedNodeList
	 * @return
	 */
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
}
