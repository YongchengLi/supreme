package workPackage;


import general.Constant;
import general.file_out_put;
import groupwork.TestSerach;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import subgraph.LinearRoute;


import Network.Layer;
import Network.Node;
import Network.Nodepair;

public class FindRoute {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Layer layer;
		layer=new Layer("worklayer", 0, "");
		file_out_put file=new file_out_put();
		
		Scanner scannerobject=new Scanner(System.in);
		
		
		System.out.println("*************输入写入文件的名字*************");
		String write_name = scannerobject.next();
		
		System.out.println("*************输入打开文件的名字*************");
		String open_name = scannerobject.next();
		layer.readTopology(open_name);

	//	System.out.println("=============the relationship================");
	  //  file.filewrite(write_name,"=============the relationship================" );
		
	    //layer初始化
	    HashMap<String,Node>map=layer.getNodelist();
        Iterator<String>iter=map.keySet().iterator();
        while(iter.hasNext()){
       	    Node node=(Node)(map.get(iter.next()));
       	    System.out.println("the neinode of "+node.getName());
       	   // file.filewrite(write_name, "the neinode of "+node.getName());
			for(int j=0;j<node.getNeinodelist().size();j++){
		          System.out.println(node.getNeinodelist().get(j).getName());
		       //   file.filewrite(write_name,node.getNeinodelist().get(j).getName());
			}
		}
        
        //生成layer所有节点对；
    	layer.generateNodepaairs();
    	KshortestPath(layer);
    	
    	OutputKshortestRoute(layer,write_name);
    	
    	
    	
	}
	
	public static void KshortestPath(Layer layer){
		
		
		HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			String name1=nodepair.getSrcNode().getName();
	        String name2=nodepair.getDesNode().getName();
	       
	        System.out.println("==================="+nodepair.getName()+"================");
	        LinearRoute newRoute=new LinearRoute(name1+"---"+name2,0,"",Constant.WORKING);
			
	        TestSerach workpathsearch=new TestSerach();
	        workpathsearch.Kshortest(nodepair.getSrcNode(), nodepair.getDesNode(), layer, 3, nodepair.getLinearlist());
		}
	}
	public static void OutputKshortestRoute(Layer layer,String writename){
		file_out_put file=new file_out_put();
		HashMap<String,Nodepair>map1=layer.getNodepairlist();
        Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			file.filewrite(writename,"==================="+nodepair.getName()+"================");
			file.filewrite(writename,"Number of shortest Route: "+nodepair.getLinearlist().size());
			for(LinearRoute k_Route:nodepair.getLinearlist())
			{
				
				file.filewrite(writename,"**********************");
				k_Route.OutputRoute_node(k_Route, writename);
				
			}
		}
	}

}
