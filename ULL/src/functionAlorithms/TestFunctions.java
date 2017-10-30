package functionAlorithms;

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

import Network.DemandUnit;
import Network.Layer;
import Network.LightPath;
import Network.Link;
import Network.Node;
import Network.Nodepair;

public class TestFunctions {

	/**
	 * test the wavelength 
	 * @param Physical_layer
	 * @param wavelengthNum
	 */
	public void TestWavelength(Layer Physical_layer)
	{
		HashMap<String,Link>map=Physical_layer.getLinklist();
    	Iterator<String>iter=map.keySet().iterator();
    	while(iter.hasNext()){
    		Link link=(Link)(map.get(iter.next()));
    	    System.out.println("Num of wavelength on each link: "+link.getWC().size());
    	}
	}

	/**
	 * 
	 * @param layer
	 * @param k
	 */
	public void  TestKshortestPath(Layer layer){
	
		HashMap<String,Nodepair>map1=layer.getNodepairlist();
	    Iterator<String>iter1=map1.keySet().iterator();

	    while(iter1.hasNext()){ 			
	    	
	    	
	    	Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
	        System.out.println("====="+nodepair.getName()+"=====");
	    	for(int i = 0; i<nodepair.getLinearlist().size();i++)
	        {
	    		System.out.println("=="+i+"==");
	        	LinearRoute route = nodepair.getLinearlist().get(i);
	        	route.OutputRoute_link(route);
	        }
	    }	
	}
   
	public void TestWavelengthAssign(ArrayList<DemandUnit> demandlist){
		
		for(DemandUnit demand:demandlist)
		{
		   OutputNodeName(demand.getNodepair());
		   
		   if(demand.isWorkPathStatus())
		   {
			   OutputGuard("workPath");
			   demand.getWorkRoute().OutputRoute_link();
			   OutputMiniGuard("wave");
			   System.out.println(demand.getWork_lightpath().getAssociatedWavelength());
		   }
		   if(demand.isProtectPathStatus())
		   {
			   OutputGuard("ProtectPath");
			   demand.getProtectRoute().OutputRoute_link();
			   OutputMiniGuard("wave");
			   System.out.println(demand.getProtect_lightpath().getAssociatedWavelength());
		   }
		}
	}
	
	public void OutputNodeName(Nodepair nodePair){
		
		 System.out.println("============="+nodePair.getName()+"============");
		 
	}
	public void OutputNodeNameToFile(file_out_put file,Nodepair nodePair,String fileName){
		
		file.filewrite(fileName,"============="+nodePair.getName()+"============"); 
		 
	}
	
	public void OutputGuard(String strName){
		 
		System.out.println("======"+strName+"=====");
		 
	}
	public void OutputGuardToFile(file_out_put file,String strName,String fileName){
		
		file.filewrite(fileName,"======"+strName+"=====");
		
	}
	public void OutputMiniGuard(String strName){
		
		 System.out.println("=="+strName+"==");
		 
	}
	public void OutputMiniGuardToFile(file_out_put file,String strName,String fileName){
		
		file.filewrite(fileName,"=="+strName+"=="); 
		
		 
	}
	public void OutputResultIntoFiles(ArrayList<DemandUnit> demandlist,String fileName){
		
		file_out_put file=new file_out_put();
		OutputDemandInformation(file, demandlist, fileName);
		
		
	}
	
	public void OutputDemandInformation(file_out_put file,ArrayList<DemandUnit> demandlist, String fileName)
	{
		double sumWorkLighPathCapacity = 0;
		double sumProtectLighPathCapcity = 0;
		double totalTraffic = 0;
		for(DemandUnit demand:demandlist)
		{
		   //information of demand
		   OutputNodeNameToFile(file,demand.getNodepair(),fileName);
		   file.filewrite(fileName,"TrafficRequest: "+demand.getTrafficRequest());
		   totalTraffic = totalTraffic + demand.getTrafficRequest();
		   if(demand.isWorkPathStatus())//exist work path
		   {
			   OutputGuardToFile(file,"workPath",fileName);
			   //information of path
			   demand.getWorkRoute().OutputRouteLinkToFile(file,fileName);
			   file.filewrite(fileName,"distance length:"+ demand.getWorkRoute().getlength());
			   //information of lightpath
			   file.filewrite(fileName,"WavelengthIndex:"+demand.getWork_lightpath().getAssociatedWavelength());
			   file.filewrite(fileName,"ModulationFormat:"+demand.getWork_lightpath().getFormatModule_Name());
			   file.filewrite(fileName,"ModulationFormat:"+demand.getWork_lightpath().getFormatModule_Capacity());
			   sumWorkLighPathCapacity = sumWorkLighPathCapacity + demand.getWork_lightpath().getFormatModule_Capacity();
		   }
		   if(demand.isProtectPathStatus())//exist protect path
		   {
			   OutputGuardToFile(file,"ProtectPath",fileName);
			   //information of path
			   demand.getProtectRoute().OutputRouteLinkToFile(file,fileName);
			   file.filewrite(fileName,"distance length:"+ demand.getProtectRoute().getlength());
			   //information of lightpath
			   file.filewrite(fileName,"WavelengthIndex:"+demand.getProtect_lightpath().getAssociatedWavelength());
			   file.filewrite(fileName,"ModulationFormat:"+demand.getProtect_lightpath().getFormatModule_Name());
			   file.filewrite(fileName,"ModulationFormat:"+demand.getProtect_lightpath().getFormatModule_Capacity());
			   sumProtectLighPathCapcity = sumProtectLighPathCapcity + demand.getProtect_lightpath().getFormatModule_Capacity();
		   }
		}
		System.out.println("total traffic:" + totalTraffic);
		System.out.println("workCapacity:" + sumWorkLighPathCapacity);
		System.out.println("protectCapcity:" + sumProtectLighPathCapcity);
		
		
		
		
		
	}
	public String OutputDemandInformation(ArrayList<DemandUnit> demandlist)
	{
		double sumWorkLighPathCapacity = 0;
		
		double sumProtectLighPathCapcity = 0;
		double totalTraffic = 0;
		for(DemandUnit demand:demandlist)
		{
		   //information of demand
		
		   
		   if(demand.isWorkPathStatus())//exist work path
		   {
			   totalTraffic = totalTraffic + demand.getTrafficRequest();
			  
			   sumWorkLighPathCapacity = sumWorkLighPathCapacity + demand.getWork_lightpath().getFormatModule_Capacity();
			 
		   }
		   if(demand.isProtectPathStatus())//exist protect path
		   {
			 
			   //information of path
			   sumProtectLighPathCapcity = sumProtectLighPathCapcity + demand.getProtect_lightpath().getFormatModule_Capacity();
	
			   if(demand.getProtectLightPathList()!=null)
			   {
				   for(LightPath lighpath:demand.getProtectLightPathList())
				   {
					   sumProtectLighPathCapcity = sumProtectLighPathCapcity + lighpath.getFormatModule_Capacity();
				   }
			   }
		   }
		}
		System.out.println("total traffic:" + totalTraffic);
		System.out.println("workCapacity:" + sumWorkLighPathCapacity);
		System.out.println("protectCapcity:" + sumProtectLighPathCapcity);
		
		return totalTraffic+","+sumWorkLighPathCapacity+","+sumProtectLighPathCapcity;

	}



	public void OutputDataCenter(Layer layer){
		
		HashMap<String ,Node>map=layer.getNodelist();
		Iterator<String>iterNode=map.keySet().iterator();		
		while(iterNode.hasNext()){

			Node node=(Node)(map.get(iterNode.next()));
			if(node.getIsDataCenter())
			{
				System.out.println(node.getName()+": is a data center ");
				System.out.println("Storage: "+node.getDataCenter().getStorage());
			    
				for(int i = 0; i < node.getDataCenter().getContentlist().size();i++)
				{
					Content content = node.getDataCenter().getContentlist().get(i);
					System.out.println("the "+i+" content: "+content.getSize()+" ");
				}
			}
		}
	}
	
	public void OutputDataCenterInformation(Layer layer)
	{
		HashMap<String ,Node>map=layer.getNodelist();
		Iterator<String>iterNode=map.keySet().iterator();		
		while(iterNode.hasNext()){

			Node node=(Node)(map.get(iterNode.next()));
			if(node.getIsDataCenter()){
				System.out.println(node.getName()+": is a dataceneter node");
				System.out .println("storage: "+node.getDataCenter().getStorage());
				if(node.getDamageRisk()==1)
					System.out .println("This node will be damaged by the earthquake");
				for(Content iCon: node.getDataCenter().getContentlist())
				{
					System.out .println("Conten: "+iCon.getIndex()+" "+iCon.getSize()+" "+iCon.getWeight());
				}
			}
		}
	}

	public void OutputEvacuatContentlist(ArrayList<Content> rapidConetentList)
	{
		//for(int i = 0;i)
	}
	
	
    public void addEvacuatedContents(ArrayList<Node> damagedNodelist,ArrayList<Content> rapidcontentList)
    {
    	for(Node node:damagedNodelist)
		{
			for(Content iCon:node.getDataCenter().getContentlist())
			{
				System.out.println("iCon Index: "+iCon.getIndex());
				boolean addkey = true;
				for(DataCenter dataCenter:iCon.getDataCenterList())
				{
					System.out.println("dataCenter Index: "+dataCenter.getIndex());
					System.out.println("dataCenter risk: "+dataCenter.getDamagedRisk());
					if(dataCenter.getDamagedRisk()<1)
					{
						addkey = false;
					}
				}
				if(addkey)
					rapidcontentList.add(iCon);
			}
			
		}
    }
	
	public void setRisk(Layer layer, ArrayList<Node> damagedNodelist,
			ArrayList<Content> rapidcontentList) {
		
		// TODO Auto-generated method stub
		double a = 0.1;
		HashMap<String ,Node>map=layer.getNodelist();
		Iterator<String>iterNode=map.keySet().iterator();		
		while(iterNode.hasNext()){

			Node node=(Node)(map.get(iterNode.next()));
			
			if(node.getIsDataCenter())
			{
				
				if(node.getDamageRisk()==1){
					System.out.println(node.getName()+" disaster");
					node.getDataCenter().setDamagedRisk(1);				
					node.getDataCenter().setDamagedStatus(true);
					damagedNodelist.add(node);
				}
				else{
					
					System.out.println(node.getDataCenter().getContentlist().size());
					for(Content con:node.getDataCenter().getContentlist())
					{
						
						con.setSafeStatus(true);
					}
					
				}
			}
		
		}
		

		
		
		for(Node node:damagedNodelist)
		{
			
			for(Content iCon:node.getDataCenter().getContentlist())
			{
				boolean addkey = true;
				for(DataCenter dataCenter:iCon.getDataCenterList())
				{	
					if(dataCenter.getDamagedRisk()<1)
					{
						addkey = false;
					}
				}
				if(addkey)
				{
					if(!rapidcontentList.contains(iCon)&&!iCon.isSafeStatus())
						rapidcontentList.add(iCon);
				}				
			}			
		}
	}
	
	public void setRisk_CF(Layer layer, ArrayList<Content> contentList, ArrayList<Node> damagedNodelist,
			ArrayList<Content> rapidcontentList,int r) {
		
		// TODO Auto-generated method stub
		double a = 0.1;
		HashMap<String ,Node>map=layer.getNodelist();
		Iterator<String>iterNode=map.keySet().iterator();		
		while(iterNode.hasNext()){

			Node node=(Node)(map.get(iterNode.next()));
			
			if(node.getIsDataCenter())
			{
				
				if(node.getDamageRisk()==1){
					System.out.println(node.getName()+" disaster");
					node.getDataCenter().setDamagedRisk(1);				
					node.getDataCenter().setDamagedStatus(true);
					damagedNodelist.add(node);
				}
				else{
					
					System.out.println(node.getDataCenter().getContentlist().size());
					for(Content con:node.getDataCenter().getContentlist())
					{
						
						con.setSafeStatus(true);
					}
					
				}
			}
		
		}
		
		
		for(Content iCon:contentList)
		{
			
			int index = 0;
			for(Node node:damagedNodelist)
			{
				
				for(Block block:node.getDataCenter().getBlocklist())
				{
					
					if(block.getOrdinaryContent()==iCon)
					{
						block.setSafe(false);
						
						index++;	
					}
				}			
			}
			if(index>r)
			{		
				
				iCon.setAddR(index-r);
				rapidcontentList.add(iCon);
			}			
		}
	}
		
	
}


