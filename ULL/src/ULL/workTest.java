package ULL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import subgraph.LinearRoute;
import functionAlorithms.Functions_Search_Route;
import functionAlorithms.Functions_ULL;

import Network.Layer;
import Network.Link;
import Network.Nodepair;



public class workTest {

	public static int RandomSeed = 1;
	public static String ReadFile =  "6";
	public static String ReadFileName = ReadFile+".CSV";
//	public static String GenerateDataFileName =ReadFile+"_"+MaxNum+"_"+RandomSeed+"_Ten.dat";//生成的DataFile文件名称

	public static int K = 3; //The Number of shortest route

	public static double Q = 0.8;
	
	public static int F = 100;
	public static String GenerateDataFileName ="Result/"+ReadFile+"_"+Q+"_"+K+".dat";//生成的DataFile文件名称	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("read the file："+ReadFileName);		
		Layer layer=new Layer("worklayer", 0, "");
		layer.readTopology(ReadFileName);
		layer.generateNodepaairs();
		layer.CalculateLinkOSNR();	
	//	FindRouteForNodeopairForAllRoute(layer);
		replacementStrategies workReplacement = new replacementStrategies();
	
		workReplacement.OpticalBlocking_based(0.2, layer);
		
		
	
	}

	public static void FindRouteForNodeopairForAllRoute(Layer layer)
	{
		
		
		
		 HashMap<String,Nodepair> nodepairlist=layer.getNodepairlist();
         Iterator<String> iter=nodepairlist.keySet().iterator();
         while(iter.hasNext()){
        	
        	Nodepair nodepair=(Nodepair)(nodepairlist.get(iter.next()));
        	
        	Functions_Search_Route searchPath = new Functions_Search_Route();
     //   	searchPath.Kshortest(nodepair, layer, K);
        	
        	ArrayList<LinearRoute>  routelist = new ArrayList<LinearRoute>(); 
        	searchPath.findAllRoute(nodepair.getSrcNode(), nodepair.getDesNode(), layer, K, routelist);

        	//只将满足OSNR最低限制的路径存入
        	Functions_ULL ull=new Functions_ULL();
        	ArrayList<LinearRoute> newroutelist = new ArrayList<LinearRoute>();
        	
        	for(LinearRoute route:routelist)
        	{
        		
        		double c=ull.CalculateRouteOSNR(route);
        		
        		if(c>=14.03)
        		{
        			newroutelist.add(route);
        		}

        	}
        	nodepair.setLinearlist(newroutelist);

         }
	}
}
