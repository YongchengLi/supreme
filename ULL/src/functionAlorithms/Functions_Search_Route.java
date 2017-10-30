package functionAlorithms;

import general.Constant;
import general.Constraint;
import general.Message;

import groupwork.SearchConstraint;
import groupwork.pathSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import subgraph.LinearRoute;
import Network.Layer;
import Network.Link;
import Network.Node;
import Network.Nodepair;

public class Functions_Search_Route {
	
	
	/**
	 * 
	 * @param layer
	 * @param k
	 */
	public void  KshortestPath(Layer layer,int k){
		
	
		
		HashMap<String,Nodepair>map1=layer.getNodepairlist();
	    Iterator<String>iter1=map1.keySet().iterator();

	    
	    while(iter1.hasNext()){ 			
	    	
	    	Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
	
	    	Kshortest(nodepair, layer, k);
	    	/*
	        for(LinearRoute route:nodepair.getLinearlist())
	        {
	        	if(nodepair.getSrcNode().getDamageRisk()==1||nodepair.getDesNode().getDamageRisk()==1)
	        	{
	        		if(nodepair.getSrcNode().getIsDataCenter()&&nodepair.getDesNode().getIsDataCenter())
	        		{
	        			System.out.println("=========="+nodepair.getName()+"==========");	
	        		
	        			route.OutputRoute_link();
		        		System.out.println("=========");
	        		}
	        	}
	        	  
	        }
	      */
	    }	
	}
	
	
	
	/**
	 * find K-disjoint shortest path routes
	 */
	public static int Kshortest(Nodepair nodepair, Layer layer, int k) {
		
		
		pathSearch workpathsearch=new pathSearch();
		Constraint constraint = new Constraint();
		
		int num_found = 0; //number of found route
		while(true){
			
			LinearRoute newRoute = new LinearRoute("",	0, "", Constant.WORKING);
		
			workpathsearch.SearchPathWithDijkstras(nodepair.getSrcNode(), nodepair.getDesNode(),layer, newRoute,constraint);
			if(newRoute.getLinklist().size()>0){
			
	            nodepair.addRoute(newRoute);
				constraint.getExcludedLinklist().addAll(newRoute.getLinklist());
				
				
				//System.out.println(newLayer.getLink_num());
				for(Link routeLinks:newRoute.getLinklist())
				{
					
					String AnotherName=routeLinks.getNodeB().getName()+"-"+routeLinks.getNodeA().getName();
					Link AnothetLink=layer.findlink(routeLinks.getNodeB(), routeLinks.getNodeA());
					constraint.getExcludedLinklist().add(AnothetLink);
				}
				num_found++;
				if(num_found == k)
					break;
			}
			else
				break;
		}
		return num_found; // the number of found routes
	}
	
public int findAllRoute(Node nodeA, Node nodeB, Layer layer,int k, ArrayList<LinearRoute> routelist) {
		
		int num_route = 0;
		int hoplimit = 100000;

		ArrayList<Message> messageList = new ArrayList<Message>(); //the list of messages that exist in the current network
		
		Message newMessage = new Message(nodeA);
				
		messageList.add(newMessage);
		
		while(!messageList.isEmpty())
		 {
			//get the header message
			Message currentmessage = messageList.remove(0);
			Node currentNode = currentmessage.getCurrentNode();
			
			if(currentNode == nodeB){
				//find one route
				LinearRoute newRoute = new LinearRoute("",	0, "", Constant.WORKING);
				newRoute.getNodelist().addAll(currentmessage.getVisitedNodelist());//
				routelist.add(newRoute);
				newRoute.ConvertfromNodeListtoLinkandObjectList(layer);	
				if(routelist.size()==k)
					break;
			}
			else{
				if(currentmessage.getVisitedNodelist().size()-1 < hoplimit)
					for(Node neinode : currentNode.getNeinodelist()){
						if(!currentmessage.getVisitedNodelist().contains(neinode)){
							
							newMessage = new Message(neinode);
							newMessage.getVisitedNodelist().addAll(0, currentmessage.getVisitedNodelist());
							messageList.add(newMessage);
							
						}							
					}				
			}
			
			
		}
	
		return routelist.size();
		
	}

}
