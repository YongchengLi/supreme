package groupwork;

import general.Constant;
import groupwork.TestSerach;
import java.util.HashMap;
import java.util.Iterator;

import subgraph.LinearRoute;
import Network.Layer;
import Network.Nodepair;

public class Serach_test_once_new {
	public void  Serach_once(Layer layer,String write_name){
		HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
            Serach_src_to_des_with_output(layer,nodepair,write_name);
           
		}
		    
    }
    public static void Serach_src_to_des_with_output(Layer layer,Nodepair nodepair,String write_name){
        String name1=nodepair.getSrcNode().getName();
        String name2=nodepair.getDesNode().getName();
        LinearRoute newRoute=new LinearRoute(name1+"---"+name2,0,"",Constant.WORKING);
        TestSerach workpathsearch=new TestSerach();
        workpathsearch.Dijkstras(name1, name2, layer,newRoute);
        if(nodepair.getLinearlist().size()>2){
        	nodepair.removeRoute(0);
        }
        nodepair.addRoute(newRoute);
        layer.clear_cost();
      
    }
}
