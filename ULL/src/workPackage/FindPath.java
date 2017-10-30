package workPackage;

import general.Constant;
import groupwork.TestSerach;

import java.util.HashMap;
import java.util.Iterator;

import subgraph.LinearRoute;

import Network.Layer;
import Network.Nodepair;

public class FindPath {
	  /**
     * 1,K shortest route Algorithm	 
     * @param layer
     */
	public  void KshortestPath(Layer layer, int RouteNum){
		
		
		HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
	   
	        TestSerach workpathsearch=new TestSerach();
	        workpathsearch.Kshortest(nodepair.getSrcNode(), nodepair.getDesNode(), layer, RouteNum, nodepair.getLinearlist());
		}
	}

}
