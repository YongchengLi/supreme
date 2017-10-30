package dataWrittenForILP;


import functionAlorithms.Functions_Rapid_DataEvacuation;
import functionAlorithms.Functions_Search_Route;
import functionAlorithms.TestFunctions;
import general.Block;
import general.Constant;
import general.file_out_put;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import rapidDataEvacuation.Content;
import rapidDataEvacuation.DataCenter;
import subgraph.LinearRoute;

import Network.*;


/**
 * @author Yongcheng Li
 * This program is desgined for the project named Quasi-WDM with MuiltlFormatModule
 * The code is to write data file
 */
public class WriteDataDocForRapidDataEvacuation {
	
	


	public static int RandomSeed = 1;
	public static String ReadFile =  "USNET";
	public static String ReadFileName = ReadFile+".CSV";
//	public static String GenerateDataFileName =ReadFile+"_"+MaxNum+"_"+RandomSeed+"_Ten.dat";//生成的DataFile文件名称
	public static String GenerateDataFileName ="DelayResult/"+ReadFile+"_"+Constant.conNum+"_"+Constant.r+".dat";//生成的DataFile文件名称
	public static int K = 3; //The Number of shortest route
	public static int DELTA = 100000;
	
	
	/**
	 * This main function is desgined for the project named Quasi-WDM with MuiltlFormatModule
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		for(int i=1;i<=RandomSeed;i++)
		{
			Run(RandomSeed);
		}
		
	
	}
	
	/**
	 * Run the function
	 * @param RandomSeed
	 */
	public static void Run(int RandomSeed)
	{
		
		Random rand = new Random(RandomSeed);
		/***********************read the Topology file**********************/
		System.out.println("read the file："+ReadFileName);		
		Layer layer=new Layer("worklayer", 0, "");
		ReadInputNetwork(layer);
		//set the link capacity and link risk with a rand seed
		Functions_Rapid_DataEvacuation function = new Functions_Rapid_DataEvacuation();
		function.setLinkCapacity(layer,rand);   
		/************************k shortest route***************************/
		RouteSearch(layer,K);
		
	
		ArrayList<DataCenter> dataCenterlist = new ArrayList<DataCenter>();
		GenerateDataCenterList(layer,rand,dataCenterlist);
		
		ArrayList<Node> damagedNodelist = new ArrayList<Node>();
		ArrayList<Content> rapidConetentList =  new ArrayList<Content>();
		ArrayList<Content> ContentList = new ArrayList<Content>();
		
		TestFunctions test = new TestFunctions();
		GenerateConetentList_CF(ContentList,rand,dataCenterlist,Constant.k,Constant.r);
	
		Functions_Rapid_DataEvacuation.OutputInformation(layer);
		
		test.setRisk_CF(layer,ContentList, damagedNodelist,rapidConetentList,Constant.r);
		/***********************Generate the datafile**********************/
		WriteFunction(RandomSeed,layer,GenerateDataFileName,ContentList);
		/********************************************************************/
		System.out.println("The file"+GenerateDataFileName+" has generated");
	}
	
	/**
	 * Read the input 
	 * @param layer
	 */
	public static void ReadInputNetwork(Layer layer)
	{
		/***********************Input the file*****************************/
		layer.readTopologywithDataCenter(ReadFileName);                                        //读取拓扑结构                                   
		layer.generateNodepaairs();
		
		/********************************************************************/
	}
	
	/**
	 * Search k shortest route for each node pair
	 * @param layer
	 * @param K
	 */
	public static void RouteSearch(Layer layer,int K)
	{
		if(K==1)
		{
			layer.shortestPath();
		}
		else{
			
			Functions_Search_Route searchPath = new Functions_Search_Route();
			searchPath.KshortestPath(layer,K);
		}
	}
	
	
	
	/**
	 * Generate Data center list
	 * @param layer
	 * @param rand
	 * @param dataCenterlist
	 */
	public static void GenerateDataCenterList(Layer layer, Random rand,ArrayList<DataCenter> dataCenterlist)
	{
		
		HashMap<String ,Node>map=layer.getNodelist();
		Iterator<String>iterNode=map.keySet().iterator();		
		while(iterNode.hasNext()){

			Node node=(Node)(map.get(iterNode.next()));
			if(node.getIsDataCenter()){
				DataCenter iDataCenter = new DataCenter("datacenter", 0, node.getName());
			
				int stroage = rand.nextInt(Constant.maximumStorage-Constant.minimumStorage+1)+Constant.minimumStorage;
				
				stroage = (int)(stroage*Constant.percentageStorage);
				iDataCenter.setStorage(stroage);
				iDataCenter.setEmptyStorage(stroage);
				System.out.println(node.getName()+"  "+iDataCenter.getEmptyStorage());
				node.setDataCenter(iDataCenter);
				iDataCenter.setAssociatedNode(node);
				dataCenterlist.add(iDataCenter);
				
			}
		}
	}
	
	
	
	/**
	 * Generate the content list
	 * @param contentList
	 * @param rand
	 * @param dataCenterlist
	 */
	public static void GenerateConetentList_CF(ArrayList<Content> contentList,Random rand,ArrayList<DataCenter> dataCenterlist,int k,int r){
	
		
		for(int i = 0; i<Constant.conNum;i++)
		{
			int replicasNum =rand.nextInt(Constant.maximumReplicas-Constant.minimumReplicas+1)+Constant.minimumReplicas;
			int weight = rand.nextInt(Constant.maximumWeight-Constant.minimumWeight+1)+Constant.minimumWeight;
			int size = rand.nextInt(Constant.maximumSize-Constant.minimumSize+1)+Constant.minimumSize;
			
			Content iCon = new Content("Con"+i, i, "Con");
			iCon.setSize(size);
			iCon.setWeight(weight);
			iCon.setReplicasNum(replicasNum);
			contentList.add(iCon);
			
			
		}
	
		for(Content iCon:contentList)
		{
			int size = iCon.getSize()/k;
			iCon.setBlockSize(size);
			for(int i = 0; i < k;i++)
			{
				Block block = new Block();
				block.setName(i+"");
				block.setSize(size);
				block.setOrdinaryContent(iCon);
				block.setDatablock(true);
				iCon.getBlocklist().add(block);
				
			}
			for(int j = 0; j < r; j++)
			{
				Block block = new Block();
				int name = k+j;
				block.setName(name+"");
				block.setSize(size);
				block.setOrdinaryContent(iCon);
				block.setParityblock(true);
				iCon.getBlocklist().add(block);
			}
			
		}
		
		//uniformly distributed the content to the data centers
		int iDataCenter = 0;
		for(Content iCon:contentList)
		{
			
			for(int i=0;i<iCon.getBlocklist().size();i++)
			{
				Block block = iCon.getBlocklist().get(i);
				boolean status = false;
				
				while(!status){
					
					iDataCenter = rand.nextInt(Constant.maxiIndex-Constant.miniIndex)+Constant.miniIndex;

					System.out.println(iDataCenter+" "+dataCenterlist.get(iDataCenter).getAssociatedNode().getName());
					if(dataCenterlist.get(iDataCenter).getEmptyStorage()>block.getSize()){
						
						iCon.setAssociatedDataCenter(dataCenterlist.get(iDataCenter));
						
						dataCenterlist.get(iDataCenter).getContentlist().add(iCon);
						
						block.setDataCenter(dataCenterlist.get(iDataCenter));
						
						dataCenterlist.get(iDataCenter).getBlocklist().add(block);
						
						int currentEmptyStorage = dataCenterlist.get(iDataCenter).getEmptyStorage();
						dataCenterlist.get(iDataCenter).setEmptyStorage(currentEmptyStorage-block.getSize());
						status = true;
						break;
						
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * write the set and parameters into the data file
	 * @param layer         the topology of test network
	 * @param writename     the output data file
	 */
	public static void WriteFunction(int i,Layer layer, String DataFileName,ArrayList<Content> ContentList){   
        /*******************Sets***********************/
		GenerateDataFile_Sets(layer,DataFileName,ContentList);
		/**********************************************/
		
		/*******************Parameters***********************/
		GenerateDataFile_Parameters(layer,DataFileName,ContentList);
		/**********************************************/    	
	}
	//***********************formulation of Sets*********************************************//
	/**
	 * Generate the Sets of data file
	 * @param layer
	 * @param DataFileName
	 */
	public static void GenerateDataFile_Sets(Layer layer,String DataFileName,ArrayList<Content> ContentList){
		
		
		WriteSet_Nodes(layer,DataFileName);//write the nodes set into the data file	
		WriteSet_Links(layer,DataFileName);//write the links set into the data file
		
		WriteSet_Contents(ContentList,DataFileName);//write the content set
		
		
		
		WriteSet_DataCenterNodes(layer,DataFileName);//write the datacenter nodes set into the data file
		WriteSet_SafeDataCenterNodes(layer,DataFileName);//write the datacenter nodes set into the data file
		WriteSet_AffectedDataCenterNodes(layer,DataFileName);//write the datacenter nodes set into the data file
		
		
		WriteSet_CandidateRoute_Nodepair(layer,DataFileName);//write the candidate Route set for each node pair into the data file	
		WriteSet_CandidateRoute_Node(layer,DataFileName);//write the candidate Route set for each node
		
		
		WriteSet_Number_Fragments_DataCenter(layer,DataFileName,ContentList);
	
	}
	
	/**
	 * Nodes set
	 * @param layer
	 * @param writename
	 */
	public static void WriteSet_Nodes(Layer layer, String DataFileName){
		
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"set V :=");
		HashMap<String,Node>NodeMap=layer.getNodelist();
        Iterator<String>iter_Node=NodeMap.keySet().iterator();
        while(iter_Node.hasNext()){
       	    Node currentNode=(Node)(NodeMap.get(iter_Node.next()));	
       	    file.filewriteContinuous(DataFileName,currentNode.getName()+" ");
		}
        file.filewriteContinuous(DataFileName, ";"+"\r\n");
        file.filewriteContinuous(DataFileName, "\r\n");
	}
	
	
	/**
	 * Link set
	 * @param layer
	 * @param writename
	 */
	public static void WriteSet_Links(Layer layer, String DataFileName){
		
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"set E :=");
		HashMap<String,Link>LinkMap=layer.getLinklist();
        Iterator<String>iter_Link=LinkMap.keySet().iterator();
        while(iter_Link.hasNext()){
       	    Link currentLink=(Link)(LinkMap.get(iter_Link.next()));	
       	    file.filewriteContinuous(DataFileName,currentLink.getName()+" ");
		}
        file.filewriteContinuous(DataFileName, ";"+"\r\n");
        file.filewriteContinuous(DataFileName, "\r\n");
	}
	
	/**
	 * Content set
	 * @param ContentList
	 * @param DataFileName
	 */
	public static void WriteSet_Contents(ArrayList<Content> ContentList,String DataFileName)
	{
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"set C :=");
		for(Content con:ContentList)
		{
			file.filewriteContinuous(DataFileName,con.getName()+" ");
		}
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
	}
	
	/**
	 * CandidateRoute set for each nodepair
	 * @param layer
	 * @param DataFileName
	 */
	public static void WriteSet_CandidateRoute_Nodepair(Layer layer, String DataFileName)
	{
		
		file_out_put file=new file_out_put();
	
		HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
	    Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();    
	    while(iter_Nodepair.hasNext()){ 			
	    	
	    	
	    	Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));
	    	
	    	file.filewriteContinuous(DataFileName,"set PDS"+"["+nodepair.getSrcNode().getName()+","+nodepair.getDesNode().getName()+"]"+" :=");
			file.filewriteContinuous(DataFileName, "\r\n");
			
			
	    	for(LinearRoute route:nodepair.getLinearlist())
	    	{
	    		int size = 1;
	    		for(Node node:route.getNodelist())
	    		{
	    			if(size<route.getNodelist().size())
	    				file.filewriteContinuous(DataFileName,node.getName()+"-");
	    			else
	    				file.filewriteContinuous(DataFileName,node.getName());
	    			size++;
	    		}
	    	    file.filewriteContinuous(DataFileName, "\r\n");
	    	}
	    	file.filewriteContinuous(DataFileName, ";"+"\r\n");
	    	file.filewriteContinuous(DataFileName, "\r\n");
	    	
	    	
	    	file.filewriteContinuous(DataFileName,"set PDS"+"["+nodepair.getDesNode().getName()+","+nodepair.getSrcNode().getName()+"]"+" :=");
			file.filewriteContinuous(DataFileName, "\r\n");
			
			
	    	for(LinearRoute route:nodepair.getLinearlist())
	    	{
	    		
	    		for(int i=route.getNodelist().size()-1;i>=0;i--)
	    		{
	    			Node node = route.getNodelist().get(i);
	    			if(i>0)
	    				file.filewriteContinuous(DataFileName,node.getName()+"-");
	    			else
	    				file.filewriteContinuous(DataFileName,node.getName());
	    			
	    		}
	    	    file.filewriteContinuous(DataFileName, "\r\n");
	    	}
	    	file.filewriteContinuous(DataFileName, ";"+"\r\n");
	    	file.filewriteContinuous(DataFileName, "\r\n");
	    
	    }
	}
	
	
	/**
	 * CandidateRoute set for each node
	 * @param layer
	 * @param DataFileName
	 */
	public static void WriteSet_CandidateRoute_Node(Layer layer, String DataFileName)
	{
		file_out_put file=new file_out_put();
		
		HashMap<String,Node>NodeMap=layer.getNodelist();
        Iterator<String>iter_Node=NodeMap.keySet().iterator();
        while(iter_Node.hasNext()){
       	    Node currentNode=(Node)(NodeMap.get(iter_Node.next()));	
       	 
       	    file.filewriteContinuous(DataFileName,"set PD"+"["+currentNode.getName()+"]"+" :=");
			file.filewriteContinuous(DataFileName, "\r\n");
			
			HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
		    Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();    
		    while(iter_Nodepair.hasNext()){ 			
		    	Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));
		    	if(nodepair.getSrcNode()==currentNode)
		    	{
		    		for(LinearRoute route:nodepair.getLinearlist())
			    	{
			    		int size = 1;
			    		for(Node node:route.getNodelist())
			    		{
			    			if(size<route.getNodelist().size())
			    				file.filewriteContinuous(DataFileName,node.getName()+"-");
			    			else
			    				file.filewriteContinuous(DataFileName,node.getName());
			    			size++;
			    		}
			    	    file.filewriteContinuous(DataFileName, "\r\n");
			    	}		    		
		    	}
		    	else if(nodepair.getDesNode()==currentNode)
		    	{
		    		for(LinearRoute route:nodepair.getLinearlist())
			    	{
		    			for(int i=route.getNodelist().size()-1;i>=0;i--)
			    		{
			    			Node node = route.getNodelist().get(i);
			    			if(i>0)
			    				file.filewriteContinuous(DataFileName,node.getName()+"-");
			    			else
			    				file.filewriteContinuous(DataFileName,node.getName());
			    			
			    		}
			    	    file.filewriteContinuous(DataFileName, "\r\n");
		    			
			    	}
		    	}
		    }
	    	file.filewriteContinuous(DataFileName, ";"+"\r\n");
	    	file.filewriteContinuous(DataFileName, "\r\n");
	    
	    }
	}
	
	

	/**
	 * Datacenter nodes set
	 * @param layer
	 * @param DataFileName
	 */
	public static void WriteSet_DataCenterNodes(Layer layer, String DataFileName){
		
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"set D :=");
		HashMap<String,Node>NodeMap=layer.getNodelist();
        Iterator<String>iter_Node=NodeMap.keySet().iterator();
        while(iter_Node.hasNext()){
       	    Node currentNode=(Node)(NodeMap.get(iter_Node.next()));	
       		if(currentNode.getIsDataCenter()){
       			
       			file.filewriteContinuous(DataFileName,currentNode.getName()+" ");
       		}
       	    
		}
        file.filewriteContinuous(DataFileName, ";"+"\r\n");
        file.filewriteContinuous(DataFileName, "\r\n");
	}
	

	/**
	 * Safe datacenter nodes set
	 * @param layer
	 * @param DataFileName
	 */
	public static void WriteSet_SafeDataCenterNodes(Layer layer, String DataFileName){
		
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"set S :=");
		HashMap<String,Node>NodeMap=layer.getNodelist();
        Iterator<String>iter_Node=NodeMap.keySet().iterator();
        while(iter_Node.hasNext()){
       	    Node currentNode=(Node)(NodeMap.get(iter_Node.next()));	
       		if(currentNode.getIsDataCenter()){
       			if(currentNode.getDamageRisk()!=1){
       			
       				file.filewriteContinuous(DataFileName,currentNode.getName()+" ");
       			
       			}
       		}
       	    
		}
        file.filewriteContinuous(DataFileName, ";"+"\r\n");
        file.filewriteContinuous(DataFileName, "\r\n");
	}
	
	
	/**
	 * Affected datacenter nodes set
	 * @param layer
	 * @param DataFileName
	 */
	public static void WriteSet_AffectedDataCenterNodes(Layer layer, String DataFileName)
	{
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"set A :=");
		HashMap<String,Node>NodeMap=layer.getNodelist();
        Iterator<String>iter_Node=NodeMap.keySet().iterator();
        while(iter_Node.hasNext()){
       	    Node currentNode=(Node)(NodeMap.get(iter_Node.next()));	
       		if(currentNode.getIsDataCenter()){
       			if(currentNode.getDamageRisk()==1){
       			
       				file.filewriteContinuous(DataFileName,currentNode.getName()+" ");
       			
       			}
       		}
       	    
		}
        file.filewriteContinuous(DataFileName, ";"+"\r\n");
        file.filewriteContinuous(DataFileName, "\r\n");
	}
	
	
	/**
	 * Number_Fragments_DataCenter
	 * @param layer
	 * @param DataFileName
	 */
	public static void WriteSet_Number_Fragments_DataCenter(Layer layer,String DataFileName,ArrayList<Content> ContentList)
	{
		/*******************param Number_Fragments_DataCenter :=**************/
		file_out_put file=new file_out_put();
		
		/****************************************************/
		
		
		for(Content iCon:ContentList)
		{
			HashMap<String,Node>NodeMap=layer.getNodelist();
	        Iterator<String>iter_Node=NodeMap.keySet().iterator();
	        while(iter_Node.hasNext()){
	       	    Node currentNode=(Node)(NodeMap.get(iter_Node.next()));	

	       	  
	       	    if(currentNode.getIsDataCenter()){
	       	     
	       	    	file.filewriteContinuous(DataFileName,"set BETA"+"["+iCon.getName()+","+currentNode.getName()+"]"+" :=");
		       	    file.filewriteContinuous(DataFileName, "\r\n");
	       	    	int size = 0;
	       			for(Block block:currentNode.getDataCenter().getBlocklist())
	       			{
	       				if(block.getOrdinaryContent()==iCon)
	       				{
	       					file.filewriteContinuous(DataFileName,block.getName()+" ");	
	       				}
	       			}
	       			file.filewriteContinuous(DataFileName, ";"+"\r\n");
	       	        file.filewriteContinuous(DataFileName, "\r\n");
	       		}
	       	    
			}
		
		}
	}
	
	//*********************** Sets*********************************************//
	
	
	
	//***********************Parameter*********************************************//
	/**
	 * Generate Parameters of data file
	 * @param layer
	 * @param DataFileName
	 */
	public static void GenerateDataFile_Parameters(Layer layer,String DataFileName,ArrayList<Content> ContentList){
		//Traffic load between each nodepair
		
		//Weight Factor for each content
		WriteParameters_Alpha_Con(layer,DataFileName,ContentList);
		
		//Capacity
		//WriteParameters_PathCapacity(layer,DataFileName);
		//Path traverse link 
		//WriteParameters_Path_Traverse_Link(layer,DataFileName);	
		//Path share common link
		
		//WriteParameters_Path_Share_CommonLink(layer,DataFileName);
		
		
		//Number_Fragments_Content
		WriteParameter_Number_Fragments_Content(DataFileName,ContentList);
		//Number_Fragments_Recovered
		WriteParameter_Number_Fragments_Recovered(DataFileName,ContentList);		
	
		
		
		//Size_Fragments_Node_Content
		WriteParameter_Size_Fragments_Node_Content(layer,DataFileName,ContentList);
		
		
		
		//Storage_Safe_Node
		WriteParameter_Storage_Safe_Node(layer,DataFileName,ContentList);
		
		//Delay Time
		writeParameter_DleayTime(layer,DataFileName);
		
		//Delta
		writeParameter_Delta(DataFileName);
	}
	

	//***********************formulation of Parameter*********************************************//
	
	/**
	 * Parameter Alpha_Con
	 * @param layer
	 * @param DataFileName
	 * @param ContentList
	 */
	public static void WriteParameters_Alpha_Con(Layer layer,String DataFileName,ArrayList<Content> ContentList)
	{
		/*******************param Alpha_Con :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param ALPHA  :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
		for(Content con:ContentList)
		{
			file.filewriteContinuous(DataFileName,con.getName()+","+con.getWeight());
			file.filewriteContinuous(DataFileName, "\r\n");
		}
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
		
	}
	
	/**
	 * Path Capacity
	 * @param layer
	 * @param DataFileName
	 */
	public static void WriteParameters_PathCapacity(Layer layer,String DataFileName)
	{
		/*******************param PathCapacity :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param B :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
		
		HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
	    Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();    
	    while(iter_Nodepair.hasNext()){ 			
	    	
	    	
	    	Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));
			
	    	for(LinearRoute route:nodepair.getLinearlist())
	    	{
	    		int size = 1;
	    		for(Node node:route.getNodelist())
	    		{
	    			if(size<route.getNodelist().size())
	    				file.filewriteContinuous(DataFileName,node.getName()+"-");
	    			else
	    				file.filewriteContinuous(DataFileName,node.getName()+",");
	    			size++;
	    		}
	    		int leastCapacity = 10000000;  
				for(Link link:route.getLinklist())				
				{
					leastCapacity = (int) Math.min(leastCapacity, link.getResidualCapacity());
				}	
	    		
	    		
	    		file.filewriteContinuous(DataFileName, leastCapacity+"");
	    		file.filewriteContinuous(DataFileName, "\r\n");
	    	   
	    	}
	    	
	    	for(LinearRoute route:nodepair.getLinearlist())
	    	{
	    		
	    		for(int i=route.getNodelist().size()-1;i>=0;i--)
	    		{
	    			Node node = route.getNodelist().get(i);
	    			if(i>0)
	    				file.filewriteContinuous(DataFileName,node.getName()+"-");
	    			else
	    				file.filewriteContinuous(DataFileName,node.getName()+",");
	    			
	    		}
	    		int leastCapacity = 10000000;  
				for(Link link:route.getLinklist())				
				{
					leastCapacity = (int) Math.min(leastCapacity, link.getResidualCapacity());
				}	
	    		
	    		
	    		file.filewriteContinuous(DataFileName, leastCapacity+"");
	    		file.filewriteContinuous(DataFileName, "\r\n");
	    	}
	    }
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
		
	}
	
	
	
	
	/**
	 * Path traves link
	 * @param layer
	 * @param DataFileName
	 */
	public static void WriteParameters_Path_Traverse_Link(Layer layer,String DataFileName)
	{
		/*******************param Path_Traverse_Link :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param O :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
		
		HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
	    Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();    
	    while(iter_Nodepair.hasNext()){ 			
	    	
	    	
	    	Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));
	  //     if(nodepair.getSrcNode().getIsDataCenter()&&nodepair.getDesNode().getIsDataCenter())
	        {
	        	for(LinearRoute route:nodepair.getLinearlist())
		    	{    	
		    		
		    		HashMap<String,Link>LinkMap=layer.getLinklist();
		            Iterator<String>iter_Link=LinkMap.keySet().iterator();
		            while(iter_Link.hasNext()){
		           	    Link currentLink=(Link)(LinkMap.get(iter_Link.next()));	
		             
		           	    int size = 1;
			    		for(Node node:route.getNodelist())
			    		{
			    			if(size<route.getNodelist().size())
			    				file.filewriteContinuous(DataFileName,node.getName()+"-");
			    			else
			    				file.filewriteContinuous(DataFileName,node.getName()+",");
			    			size++;
			    		}
		           	    
		           	    file.filewriteContinuous(DataFileName, currentLink.getName()+",");
		           	    if(route.traverseLinkWithDirection(currentLink))
		           	    {

			           	    file.filewriteContinuous(DataFileName, 1+"");
		           	    }
		           	    else
		           	    {	           	
		           	    	file.filewriteContinuous(DataFileName, 0+"");
		           	    }   	 
		           	    file.filewriteContinuous(DataFileName, "\r\n");
		           	    
		                   	    
		           	    
		           	    for(int i=route.getNodelist().size()-1;i>=0;i--)
		           	    {
		           	    	Node node = route.getNodelist().get(i);
		           	    	if(i>0)
		           	    		file.filewriteContinuous(DataFileName,node.getName()+"-");
		           	    	else
		           	    		file.filewriteContinuous(DataFileName,node.getName()+",");

		           	    }
		           	  
		           	    file.filewriteContinuous(DataFileName, currentLink.getNodeA().getName()+"-"+currentLink.getNodeB().getName()+",");
		           	    if(route.traverseLinkWithBiDirection(currentLink))
		           	    {

			           	    file.filewriteContinuous(DataFileName, 1+"");
		           	    }
		           	    else
		           	    {	           	
		           	    	file.filewriteContinuous(DataFileName, 0+"");
		           	    }   
		           	    file.filewriteContinuous(DataFileName, "\r\n");
		    		}
		    	   
		    	}
	        }
	    	
	    }
		
		
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
		
	}
	
	
	/**
	 * Path share common link
	 * @param layer
	 * @param DataFileName
	 */
	public static void WriteParameters_Path_Share_CommonLink(Layer layer,String DataFileName)
	{
		/*******************param Path_Share_CommonLink :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param SL :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
		
		HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
	    Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();    
	    while(iter_Nodepair.hasNext()){ 			
	    	
	    	
	    	Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));
	    //	 if(nodepair.getSrcNode().getIsDataCenter()&&nodepair.getDesNode().getIsDataCenter())
	    	 {
	    		 for(LinearRoute route:nodepair.getLinearlist())
	 	    	{    		
	 	    		HashMap<String,Nodepair>SecondNodepairMap=layer.getNodepairlist();
	 	    	    Iterator<String>iter_SecondNodepair=SecondNodepairMap.keySet().iterator();    
	 	    	    while(iter_SecondNodepair.hasNext()){ 			
	 	    	    
	 	    	    	Nodepair Secondnodepair=(Nodepair)(SecondNodepairMap.get(iter_SecondNodepair.next()));
	 	           	    
	 	    	    	
	 	    	    	for(LinearRoute Secondroute:Secondnodepair.getLinearlist())
	 	    	    	{
	 	    	    		
	 	    	    		//if(route!=Secondroute)
	 	    	    		{
	 	    	    			
	 	    	    			  int size = 1;
	 	    			    		
	 	    	    			  for(Node node:route.getNodelist())
	 	    	    			  {
	 	    	    				  if(size<route.getNodelist().size())
	 	    	    					  file.filewriteContinuous(DataFileName,node.getName()+"-");	    			    			
	 	    	    				  else    			    			
	 	    	    					  file.filewriteContinuous(DataFileName,node.getName()+",");	    			    			
	 	    	    				  size++;	    			    		
	 	    	    			  }
	 	    	    			
	 	    	    			  size = 1;
	 	    			    		
	 	    	    			  for(Node node:Secondroute.getNodelist())
	 	    	    			  {
	 	    	    				  if(size<Secondroute.getNodelist().size())
	 	    	    					  file.filewriteContinuous(DataFileName,node.getName()+"-");	    			    			
	 	    	    				  else    			    			
	 	    	    					  file.filewriteContinuous(DataFileName,node.getName()+",");	    			    			
	 	    	    				  size++;	    			    		
	 	    	    			  }
	 	    	    			  if(route.shareCommonLinkWithDirection(Secondroute))
	 	    		           	  {	         	    
	 	    	    				  file.filewriteContinuous(DataFileName, 1+"");		           	    
	 	    		           	  }  		           	   
	 	    	    			  else    		           	  
	 	    	    			  {	           	    		           	 
	 	    	    				  file.filewriteContinuous(DataFileName, 0+"");	    		           	  
	 	    	    			  }	 
	 	    	    			  
	 	    	    			  
	 	    	    			  file.filewriteContinuous(DataFileName, "\r\n");  
	 	    	    			  
	 	    	    			  size = 1;
	 	    	    			  for(Node node:route.getNodelist())
	 	    	    			  {
	 	    	    				  if(size<route.getNodelist().size())
	 	    	    					  file.filewriteContinuous(DataFileName,node.getName()+"-");	    			    			
	 	    	    				  else    			    			
	 	    	    					  file.filewriteContinuous(DataFileName,node.getName()+",");	    			    			
	 	    	    				  size++;	    			    		
	 	    	    			  }
	 	    	    			  for(int i=Secondroute.getNodelist().size()-1;i>=0;i--)
	 	    	    			  {
	 	    	    				  Node node = Secondroute.getNodelist().get(i);
	 	    	    				  if(i>0)
	 	    	    					  file.filewriteContinuous(DataFileName,node.getName()+"-");
	 	    	    				  else
	 	    	    					  file.filewriteContinuous(DataFileName,node.getName()+",");			    		
	 	    	    			  }  
	 	    	    			  if(route.shareCommonLinkWithBiDirection(Secondroute))
	 	    		           	  {	         	    
	 	    	    				  file.filewriteContinuous(DataFileName, 1+"");		           	    
	 	    		           	  }  		           	   
	 	    	    			  else    		           	  
	 	    	    			  {	           	    		           	 
	 	    	    				  file.filewriteContinuous(DataFileName, 0+"");	    		           	  
	 	    	    			  }	 

	 	    	    			  file.filewriteContinuous(DataFileName, "\r\n");  
	 	    	    			  
	 	    	    			  
	 	    	    			  
	 	    	    			  
	 	    	    			  
	 	    	    			  
	 	    	    			  
	 	    	    			  
	 	    	    			  for(int i=route.getNodelist().size()-1;i>=0;i--)
	 	    	    			  {
	 	    	    				  Node node = route.getNodelist().get(i);
	 	    	    				  if(i>0)
	 	    	    					  file.filewriteContinuous(DataFileName,node.getName()+"-");
	 	    	    				  else
	 	    	    					  file.filewriteContinuous(DataFileName,node.getName()+",");

	 	    	    			  }
	 	    	    			  for(int j=Secondroute.getNodelist().size()-1;j>=0;j--)
	 	    	    			  {
	 	    	    				  Node node = Secondroute.getNodelist().get(j);
	 	    	    				  if(j>0)
	 	    	    					  file.filewriteContinuous(DataFileName,node.getName()+"-");
	 	    	    				  else
	 	    	    					  file.filewriteContinuous(DataFileName,node.getName()+",");			    		
	 	    	    			  }  
	 	    	    			  
	 	    	    			  if(route.shareCommonLinkWithDirection(Secondroute))
	 	    		           	  {	         	    
	 	    	    				  file.filewriteContinuous(DataFileName, 1+"");		           	    
	 	    		           	  }  		           	   
	 	    	    			  else    		           	  
	 	    	    			  {	           	    		           	 
	 	    	    				  file.filewriteContinuous(DataFileName, 0+"");	    		           	  
	 	    	    			  }	 

	 	    	    			  file.filewriteContinuous(DataFileName, "\r\n");  
	 	    	    			  
	 	    	    			  
	 	    	    			  
	 	    	    			  for(int i=route.getNodelist().size()-1;i>=0;i--)
	 	    	    			  {
	 	    	    				  Node node = route.getNodelist().get(i);
	 	    	    				  if(i>0)
	 	    	    					  file.filewriteContinuous(DataFileName,node.getName()+"-");
	 	    	    				  else
	 	    	    					  file.filewriteContinuous(DataFileName,node.getName()+",");

	 	    	    			  }
	 	    	    			  size = 1;
	 	    	    			  for(Node node:Secondroute.getNodelist())
	 	    	    			  {
	 	    	    				  if(size<Secondroute.getNodelist().size())
	 	    	    					  file.filewriteContinuous(DataFileName,node.getName()+"-");	    			    			
	 	    	    				  else    			    			
	 	    	    					  file.filewriteContinuous(DataFileName,node.getName()+",");	    			    			
	 	    	    				  size++;	    			    		
	 	    	    			  }
	 	    	    			  
	 	    	    			  if(Secondroute.shareCommonLinkWithBiDirection(route))
	 	    		           	  {	         	    
	 	    	    				  file.filewriteContinuous(DataFileName, 1+"");		           	    
	 	    		           	  }  		           	   
	 	    	    			  else    		           	  
	 	    	    			  {	           	    		           	 
	 	    	    				  file.filewriteContinuous(DataFileName, 0+"");	    		           	  
	 	    	    			  }	 

	 	    	    			  file.filewriteContinuous(DataFileName, "\r\n");  
	 	    	    			  
	 	    	    			  
	 	    	    			 			  	    	    			
	 	    	    		}	    	    		    	  	
	 	    	    	}    	 
	 	    		}
	 	    	   
	 	    	}
	    	 }
	    	
	    }
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
		
	}
	
	
	
	
	/**
	 * Number_Fragments_Content
	 * @param DataFileName
	 * @param ContentList
	 */
	public static void WriteParameter_Number_Fragments_Content(String DataFileName,ArrayList<Content> ContentList)
	{
		/*******************param Number_Fragments_Content :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param N :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
		
		int num = Constant.k+Constant.r;
		for(Content iCon:ContentList)
		{

			file.filewriteContinuous(DataFileName,iCon.getName()+","+num);	
			file.filewriteContinuous(DataFileName, "\r\n");
		
		}
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
		
	}
	
	
	/**
	 * Number_Fragments_Recovered
	 * @param DataFileName
	 * @param ContentList
	 */
	public static void WriteParameter_Number_Fragments_Recovered(String DataFileName,ArrayList<Content> ContentList)
	{
		/*******************param Number_Fragments_Recovered :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param R :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
		
		int num = Constant.k+Constant.r;
		for(Content iCon:ContentList)
		{

			file.filewriteContinuous(DataFileName,iCon.getName()+","+iCon.getAddR());	
			file.filewriteContinuous(DataFileName, "\r\n");
		
		}
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
		
	}
	
	
	/**
	 * Size_Fragments_Node_Content
	 * @param layer
	 * @param DataFileName
	 * @param ContentList
	 */
	public static void WriteParameter_Size_Fragments_Node_Content(Layer layer,String DataFileName,ArrayList<Content> ContentList)
	{
		/*******************param Size_Fragments_Node_Content :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param T :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
				
		HashMap<String,Node>NodeMap=layer.getNodelist();
        Iterator<String>iter_Node=NodeMap.keySet().iterator();
        while(iter_Node.hasNext()){
       	    Node currentNode=(Node)(NodeMap.get(iter_Node.next()));	
       		if(currentNode.getIsDataCenter()){      			
       			for(Block block:currentNode.getDataCenter().getBlocklist())
       			{
       				Content iCon = block.getOrdinaryContent();
       				file.filewriteContinuous(DataFileName,iCon.getName()+","+currentNode.getName()+","+block.getName()+","+block.getSize());	
       				file.filewriteContinuous(DataFileName, "\r\n");
       			}       				
       		}   	    
		}	
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
	}
	
	
	/**
	 * Storage_Safe_Node
	 * @param layer
	 * @param DataFileName
	 * @param ContentList
	 */
	public static void WriteParameter_Storage_Safe_Node(Layer layer,String DataFileName,ArrayList<Content> ContentList)
	{
		/*******************param Size_Fragments_Node_Content :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param VOL :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
				
		HashMap<String,Node>NodeMap=layer.getNodelist();
        Iterator<String>iter_Node=NodeMap.keySet().iterator();
        while(iter_Node.hasNext()){
       	    Node currentNode=(Node)(NodeMap.get(iter_Node.next()));	
       		if(currentNode.getIsDataCenter()){     
       			if(currentNode.getDamageRisk()!=1){
       			
       				file.filewriteContinuous(DataFileName,currentNode.getName()+","+currentNode.getDataCenter().getStorage());	
       				file.filewriteContinuous(DataFileName, "\r\n");
       				
       			}			
       		}   	    
		}	
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
	}
	
	
	
	/**
	 * DleayTime
	 * @param layer
	 * @param DataFileName
	 * @param ContentList
	 */
	public static void writeParameter_DleayTime(Layer layer,String DataFileName)
	{
		/*******************param DleayTime :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param tau :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
		
		HashMap<String,Node>NodeMap=layer.getNodelist();
        Iterator<String>iter_Node=NodeMap.keySet().iterator();
        while(iter_Node.hasNext()){
       	    Node currentNode=(Node)(NodeMap.get(iter_Node.next()));	
       	 
       	    if(currentNode.getDamageRisk()==1)
       	    {
       	       for(Block block:currentNode.getDataCenter().getBlocklist())
       	       {
       	    	
       	    	   HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();	    
       	    	   Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();    	    
       	    	   while(iter_Nodepair.hasNext()){ 				    	
       	    		   Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));	    	
       	    		   if(nodepair.getSrcNode()==currentNode)	    	
       	    		   {	    		
       	    			   for(LinearRoute route:nodepair.getLinearlist())	    	
       	    			   { 			
       	    				   int leastCapacity = 10000000;  			
       	    				   for(Link link:route.getLinklist())								
       	    				   {				
       	    					   leastCapacity = (int) Math.min(leastCapacity, link.getResidualCapacity());			
       	    				   }
       	    				   Functions_Rapid_DataEvacuation function  = new Functions_Rapid_DataEvacuation();
       	    				   double Pathdelay = function.Delay(route,block.getSize(),leastCapacity);
       	    				   
       	    			       Pathdelay = (double)(Math.round(Pathdelay*1000)/1000.0);
       	    				   
       	    				   file.filewriteContinuous(DataFileName,block.getOrdinaryContent().getName()+","+nodepair.getSrcNode().getName()+","+block.getName()+",");	
       	    				   

       	    				   int size = 1;
       	    				   for(Node node:route.getNodelist())
       	    				   {
       	    					   if(size<route.getNodelist().size())
       	    						   file.filewriteContinuous(DataFileName,node.getName()+"-");	    			    			
       	    					   else    			    			
       	    						   file.filewriteContinuous(DataFileName,node.getName()+",");	    			    			
       	    					   size++;	    			    		
       	    				   }
       	    				   file.filewriteContinuous(DataFileName,Pathdelay+"");	   
       	    				   file.filewriteContinuous(DataFileName, "\r\n");
       	    			   }
       	    		   }
       	    		   else if(nodepair.getDesNode()==currentNode)
       	    		   {
       	    			   for(LinearRoute route:nodepair.getLinearlist())
       	    			   {
       	    				   int leastCapacity = 10000000;  			
     	    				   for(Link link:route.getLinklist())								
     	    				   {				
     	    					   leastCapacity = (int) Math.min(leastCapacity, link.getResidualCapacity());			
     	    				   }
     	    				   Functions_Rapid_DataEvacuation function  = new Functions_Rapid_DataEvacuation();
     	    				   double Pathdelay = function.Delay(route,block.getSize(),leastCapacity);
     	    				   
     	    			       Pathdelay = (double)(Math.round(Pathdelay*1000)/1000.0);
     	    				   
     	    				   file.filewriteContinuous(DataFileName,block.getOrdinaryContent().getName()+","+nodepair.getDesNode().getName()+","+block.getName()+",");	
     	    				   

     	    				   int size = 1;
       	    				   
       	    				   
       	    				   for(int i=route.getNodelist().size()-1;i>=0;i--)
       	    				   {
       	    					   Node node = route.getNodelist().get(i);
       	    					   if(i>0)
       	    						   file.filewriteContinuous(DataFileName,node.getName()+"-");
       	    					   else
       	    						   file.filewriteContinuous(DataFileName,node.getName()+",");	

       	    				   }
       	    				   file.filewriteContinuous(DataFileName,Pathdelay+"");	   
     	    				   file.filewriteContinuous(DataFileName, "\r\n");

       	    			   }
       	    		   }

       	    	   }

       	       }	
       	   
       	    }      	   	    
	    }
	
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
	}
	
	
	
	public static void writeParameter_Delta(String DataFileName)
	{
		/*******************param Delta :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param DELTA :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
		file.filewriteContinuous(DataFileName,DELTA+"");
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
		
	}
	
	//***********************formulation of Parameter*********************************************//
	

}
