package rapidDataEvacuation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import subgraph.LinearRoute;
import subgraph.RoutePair;

import Network.Layer;
import Network.Link;
import Network.Node;
import Network.Nodepair;
import functionAlorithms.*;
import general.Block;
import general.Constant;
import general.Constraint;
import general.Slot;
import general.Task;
import general.TimeSlot;
import general.file_out_put;
import groupwork.pathSearch;



public class ContentFragmentationDataEvacutation {
	

	
	
	
	
	
	//parameter
	public static int seed = 8;
	
	public static double totalDelay = 0;
	
	public static double totalMaxTime = 0;
	
	public static int EvacuatedContentNum = 0;
	
	public static double NumLimit = 800;
	
	public static double totalContentRisk = 0;
	
	public static int DeliveryedData = 0;
	
	
	public static int RandomSeed = 1;
	public static String ReadFile =  "USNET";
	public static String ReadFileName = ReadFile+".CSV";
//	public static String GenerateDataFileName =ReadFile+"_"+MaxNum+"_"+RandomSeed+"_Ten.dat";//生成的DataFile文件名称
	public static String GenerateDataFileName ="DelayResult/"+ReadFile+".dat";//生成的DataFile文件名称
	public static int K = 3; //The Number of shortest route
	public static int DELTA = 100000;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
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
		layer.readTopologywithDataCenter(ReadFileName);                                        //读取拓扑结构                                   
		layer.generateNodepaairs();
		//set the link capacity and link risk with a rand seed
		Functions_Rapid_DataEvacuation function = new Functions_Rapid_DataEvacuation();
		function.setLinkCapacity(layer,rand);   
		/************************k shortest route***************************/
		//Search Two shortest routes for each node pair   
    	Functions_Search_Route PathSearch = new Functions_Search_Route();
    	PathSearch.KshortestPath(layer,K);
		
	
		ArrayList<DataCenter> dataCenterlist = new ArrayList<DataCenter>();
		GenerateDataCenterList(layer,rand,dataCenterlist);
		
		ArrayList<Node> damagedNodelist = new ArrayList<Node>();
		ArrayList<Content> rapidConetentList =  new ArrayList<Content>();
		ArrayList<Content> ContentList = new ArrayList<Content>();
		
		TestFunctions test = new TestFunctions();
		GenerateConetentList_CF(ContentList,rand,dataCenterlist,Constant.k,Constant.r);
	
		Functions_Rapid_DataEvacuation.OutputInformation(layer);
		
		test.setRisk_CF(layer,ContentList, damagedNodelist,rapidConetentList,Constant.r);
		
		SortMaxtoMin(rapidConetentList);
    	
    	
    	EvacuatedContentNum = rapidConetentList.size();
    	System.out .println("test: "+rapidConetentList.size());
    	
    	
    	for(Content iCon:rapidConetentList)
		{
    	
    		System.out .println("=====================Conten: "+iCon.getIndex()+" "+iCon.getSize()+" "+iCon.getWeight()+"=============");
    		ArrayList<DataCenter> avaDataCenterlist = new ArrayList<DataCenter>();
    		CF_RDE(layer,iCon,dataCenterlist,avaDataCenterlist);
        	   
    	
		}
      //  OutputContentFragmentation(rapidConetentList);
  
    	System.out.println("totalMaxTime: "+totalMaxTime);
        
    	System.out.println("DeliveryedData: "+DeliveryedData);
       

    
	}
	
	
	/**
	 * 
	 */
	public static boolean useStatus(int[] work,int n){
		boolean key = true;
		for(int i = 0; i<work.length;i++)
		{
			if(work[i]==n)
			{
				key = false;
			}
		}
		return key;
	}
	/**
	 * Generate the content list
	 * @param contentList
	 * @param rand
	 * @param dataCenterlist
	 */
	public static void GenerateConetentList(ArrayList<Content> contentList,Random rand,ArrayList<DataCenter> dataCenterlist){
	
		for(int i = 0; i<Constant.conNum;i++)
		{
			int replicasNum =rand.nextInt(Constant.maximumReplicas-Constant.minimumReplicas+1)+Constant.minimumReplicas;
			int weight = rand.nextInt(Constant.maximumWeight-Constant.minimumWeight+1)+Constant.minimumWeight;
			int size = rand.nextInt(Constant.maximumSize-Constant.minimumSize+1)+Constant.minimumSize;
			
			System.out.println(replicasNum+" "+weight+" "+size);
			
			Content iCon = new Content("Con", i, "Con");
			iCon.setSize(size);
			iCon.setWeight(weight);
			iCon.setReplicasNum(replicasNum);
			contentList.add(iCon);
			
			
		}
		System.out.println("==============================");
		//uniformly distributed the content to the data centers
		int iDataCenter = 0;
		
		for(Content iCon:contentList)
		{
			int  replicasNum = 2;// iCon.getReplicasNum();
			boolean status = false;
			int[] base = new int[replicasNum];
		    
			int index = 0;
			
			while(!status){
				
				iDataCenter = rand.nextInt(Constant.maxiIndex-Constant.miniIndex+1)+Constant.miniIndex;
				
				if(dataCenterlist.get(iDataCenter).getEmptyStorage()>iCon.getSize()&&useStatus(base,iDataCenter)){
					
					iCon.setAssociatedDataCenter(dataCenterlist.get(iDataCenter));
					
					dataCenterlist.get(iDataCenter).getContentlist().add(iCon);
					
					int currentEmptyStorage = dataCenterlist.get(iDataCenter).getEmptyStorage();
					
					dataCenterlist.get(iDataCenter).setEmptyStorage(currentEmptyStorage-iCon.getSize());
					replicasNum--;
					
					if(!iCon.getDataCenterList().contains(dataCenterlist.get(iDataCenter)))
						iCon.getDataCenterList().add(dataCenterlist.get(iDataCenter));
					
					base[index] = iDataCenter;
					index++;
					//iDataCenter++;
					
					System.out.println(iCon.getIndex()+" "+replicasNum+" "+iDataCenter);
					if(iDataCenter >=dataCenterlist.size())
						iDataCenter = 0;
					
					if(replicasNum==0)
					{
						status = true;
						break;
					}
					
				}
				else{
					
					iDataCenter++;
				}
			}
			
			if(status){				
				if(iDataCenter >=dataCenterlist.size())
					iDataCenter = 0;
			}
			else{
				
				break;
			
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
	 * Generate the content list
	 * @param contentList
	 * @param rand
	 * @param dataCenterlist
	 */
	public static void GenerateConetentListEqual(ArrayList<Content> contentList,Random rand,ArrayList<DataCenter> dataCenterlist){
	
		for(int i = 0; i<Constant.conNum;i++)
		{
			int replicasNum =rand.nextInt(Constant.maximumReplicas-Constant.minimumReplicas)+Constant.minimumReplicas;
			int weight = rand.nextInt(Constant.maximumWeight-Constant.minimumWeight)+Constant.minimumWeight;
			int size = Constant.size;
			
			Content iCon = new Content("Con", i, "Con");
			iCon.setSize(size);
			iCon.setWeight(weight);
			iCon.setReplicasNum(replicasNum);
			contentList.add(iCon);
			
			
		}
	
		//uniformly distributed the content to the data centers
		int iDataCenter = 0;
		for(Content iCon:contentList)
		{
			int  replicasNum = iCon.getReplicasNum();
			boolean status = false;
			 
			for(DataCenter dataCenter: dataCenterlist)
			{
				if(dataCenter.isDamagedStatus())
				{
	
					iCon.setAssociatedDataCenter(dataCenterlist.get(iDataCenter));
					
					dataCenterlist.get(iDataCenter).getContentlist().add(iCon);
					
					int currentEmptyStorage = dataCenterlist.get(iDataCenter).getEmptyStorage();
					
					dataCenterlist.get(iDataCenter).setEmptyStorage(currentEmptyStorage-iCon.getSize());
					
					GenerateSub(iCon,dataCenterlist,iDataCenter);
					replicasNum--;
					iCon.getDataCenterList().add(dataCenterlist.get(iDataCenter));
				}
			}
		}
	}
	
	public static void GenerateSub(Content iCon,ArrayList<DataCenter> dataCenterlist,int iDataCenter)
	{
		for(int i = 0; i<Constant.Num;i++)
		{
			
			Content iNewCon = new Content(iCon.getName(), iCon.getIndex(), iCon.getComments());
			
			
			iNewCon.setSize(iCon.getSize());
			iNewCon.setWeight(iCon.getWeight());
			
			
			iNewCon.setAssociatedDataCenter(dataCenterlist.get(iDataCenter));
			
			dataCenterlist.get(iDataCenter).getContentlist().add(iNewCon);
			
			int currentEmptyStorage = dataCenterlist.get(iDataCenter).getEmptyStorage();
			
			dataCenterlist.get(iDataCenter).setEmptyStorage(currentEmptyStorage-iNewCon.getSize());
			
			iNewCon.getDataCenterList().add(dataCenterlist.get(iDataCenter));
			
			
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
				
				node.setDataCenter(iDataCenter);
				iDataCenter.setAssociatedNode(node);
				dataCenterlist.add(iDataCenter);
				
			}
		}
	}
	
	/**
	 * Divide the Content
	 * @param contentList
	 */
	public static void ContentDivision(ArrayList<Content> contentList)
	{
		for(Content iCon:contentList)
		{
			for(int i = 0; i < Constant.subNumber; i++)
			{
				
				Content con = new Content(iCon.getName(), iCon.getIndex(), iCon.getComments());
				con.setAssociatedDataCenter(iCon.getAssociatedDataCenter());
				con.setDataCenterList(iCon.getDataCenterList());
				con.setParentContent(iCon);
				con.setReplicasNum(iCon.getReplicasNum());
				con.setWeight(iCon.getWeight());
				con.setSize(iCon.getSize()/2);
				iCon.getSubContentList().add(con);
				
			}
			
		}
	}
	
	/**
	 * Sort the content list from max to min
	 * @param contentList
	 */
	public static void SortMaxtoMin(ArrayList<Content> contentList)
	{
		Content text;
		int i;
        for(int j=contentList.size()-1;j>=0;j--){
	            
        	i=0;
            
        	while(i<j){
	 
	        
        		if(contentList.get(i).getWeight()<contentList.get(i+1).getWeight()){
		                         text=null;
		                         text=contentList.get(i);
		                         contentList.set(i, contentList.get(i+1));
		                         contentList.set(i+1, text);
		                        
	                    }
	                    i++;
                  }
        }

	}
	
	
	
	/**
	 * 
	 * Calculate the delay of the path
	 * @param route
	 * @param conSize
	 * @param linkCapacity
	 * @return
	 */
	public static double Delay(LinearRoute route,int conSize,int linkCapacity)
	{
		//connection step
		//1.control-message processing delay
		double control_Message_Processing_delay = (route.getLinklist().size()+1)*Constant.processingDelay;
		
		double control_Message_Propagation_delay = route.getlength()*Constant.propgagationDelay;
	
		double switch_Configuration_delay = (route.getLinklist().size()+1)*Constant.swicthconfigurationDelay;
		
		double transmission_delay = (double)conSize*8/linkCapacity*1000000;
		
	    double progragation_delay = route.getlength()*Constant.propgagationDelay;
	    
		double Delay = control_Message_Processing_delay+control_Message_Propagation_delay
				+switch_Configuration_delay+transmission_delay+progragation_delay;			
		
		Delay = Delay/1000000;
	
		return Delay;
	}
	
	
	/**
	 * Calculate Delay for each path
	 * @param route
	 * @param conSize
	 * @param linkCapacity
	 * @return
	 */
	public static double  CalculateDleayForPath(LinearRoute route,int conSize,int linkCapacity){
		
		
		
		double delayTotal= 0;
		double delayTx = Delay(route,conSize,linkCapacity);	//calcuate the delay of this path

		
		double reuqirdDelay = 0;
		
		for(Link link:route.getLinklist())
		{
			
			
			reuqirdDelay = Math.max(reuqirdDelay, link.getLinkdelay());
	
		}
		
		
		delayTotal = delayTx + reuqirdDelay;
		
		
		
		return delayTotal;
	}
	
	/**
	 * Select the src data center
	 * @param con
	 * @param dataCenterlist
	 * @param AvadataCenterlist
	 */
	public static void SelectAvaSrcDataCenter(Content con,ArrayList<DataCenter> AvadataCenterlist){
	    
	//
		for(DataCenter dataCenter:con.getDataCenterList())
		{
			if(!dataCenter.isDamagedStatus())
			{
				AvadataCenterlist.add(dataCenter);//if the storage is enough for the data 			
			}
			
		}
	
	}
	/**
	 * Select the average data center
	 * @param con
	 * @param dataCenterlist
	 * @param AvadataCenterlist
	 */
	public static void SelectAvaDataCenter(Content con,ArrayList<DataCenter> dataCenterlist,ArrayList<DataCenter> AvadataCenterlist){
	    
	//
	for(DataCenter dataCenter:dataCenterlist)
	{
		if(!dataCenter.isDamagedStatus())
		{
			//the empty stogre is 
			if(con.getSize()<dataCenter.getEmptyStorage())
			{
				AvadataCenterlist.add(dataCenter);//if the storage is enough for the data 
			}
			
		}
		
	}
		
		
	}

	/**
	 * Select the average data center
	 * @param con
	 * @param dataCenterlist
	 * @param AvadataCenterlist
	 */
	public static void SelectAvaDataCenter_CF(Content con,ArrayList<DataCenter> dataCenterlist,ArrayList<DataCenter> AvadataCenterlist){ 
	//
		for(DataCenter dataCenter:dataCenterlist)
		{
			if(!dataCenter.isDamagedStatus())
			{
				//the empty stogre is 
				if(con.getBlockSize()<dataCenter.getEmptyStorage())
				{
					AvadataCenterlist.add(dataCenter);//if the storage is enough for the data 
				}
				
			}
			
		}
	}
	/**
	 * Delivery Content
	 * @param layer
	 * @param task
	 */
	public static void deliveryContent(Layer layer,Task task)
	{
		
		for(Link link:task.getRoute().getLinklist())
		{
			
			link.setLinkdelay(task.getTotalTime());
			Link anotherlink = layer.findlink(link.getNodeB(), link.getNodeA());
			anotherlink.setLinkdelay(task.getTotalTime());
			link.getTasklist().add(task);
			anotherlink.getTasklist().add(task);
			
			link.setServedContent(link.getServedContent()+task.getContent().getSize());
			anotherlink.setServedContent(anotherlink.getServedContent()+task.getContent().getSize());
		}
		int emptyStorage = task.getDataCenter().getEmptyStorage()-task.getContent().getSize();
		task.getDataCenter().setEmptyStorage(emptyStorage);
	}
	
	/**
	 * Delivery Content
	 * @param layer
	 * @param task
	 */
	public static void deliveryContent_CF(Layer layer,Task task)
	{
		
		for(Link link:task.getRoute().getLinklist())
		{
			
			link.setLinkdelay(task.getTotalTime());
			Link anotherlink = layer.findlink(link.getNodeB(), link.getNodeA());
			anotherlink.setLinkdelay(task.getTotalTime());
			link.getTasklist().add(task);
			anotherlink.getTasklist().add(task);
			
			link.setServedContent(link.getServedContent()+task.getContent().getBlockSize());
			anotherlink.setServedContent(anotherlink.getServedContent()+task.getContent().getBlockSize());
		}
		int emptyStorage = task.getDataCenter().getEmptyStorage()-task.getContent().getBlockSize();
		task.getDataCenter().setEmptyStorage(emptyStorage);
	}
	
	
	public static void deliveryContentWithSubContent(Layer layer,Task task)
	{
		LinearRoute firstpath = task.getRoutepair().getFirstRoute();
		LinearRoute secondpath = task.getRoutepair().getSecondRoute();
		
		int firstsize = task.getFirstSize();
		int secondsize = task.getSecondSize();
		
		for(Link link:firstpath.getLinklist())
		{
			
			link.setLinkdelay(task.getFirstPathDelay());
			Link anotherlink = layer.findlink(link.getNodeB(), link.getNodeA());
			anotherlink.setLinkdelay(task.getFirstPathDelay());
			link.getTasklist().add(task);
			anotherlink.getTasklist().add(task);
			link.setServedContent(link.getServedContent()+firstsize);
			anotherlink.setServedContent(anotherlink.getServedContent()+firstsize);
		}
		
		for(Link link:secondpath.getLinklist())
		{
			link.setLinkdelay(task.getSecondPathDelay());
			Link anotherlink = layer.findlink(link.getNodeB(), link.getNodeA());
			anotherlink.setLinkdelay(task.getSecondPathDelay());
			link.getTasklist().add(task);
			anotherlink.getTasklist().add(task);
			link.setServedContent(link.getServedContent()+secondsize);
			anotherlink.setServedContent(anotherlink.getServedContent()+secondsize);
			
		}
		int firstemptyStorage = task.getFirstDataCenter().getEmptyStorage()-task.getFirstSize();
		task.getFirstDataCenter().setEmptyStorage(firstemptyStorage);
		
		int secondemptyStorage = task.getSecondDataCenter().getEmptyStorage()-task.getSecondSize();
		task.getSecondDataCenter().setEmptyStorage(secondemptyStorage);
	}
	/**
	 * Strategy one: To nearest Data center
	 * @param layer
	 * @param con
	 * @param AvadataCenterlist
	 */
	public static void Strategy_NearestDataCenter(Layer layer,Content con,ArrayList<DataCenter> AvadataCenterlist)
	{
		 DataCenter selectDataCenter = null;
		 
		 LinearRoute selectedPath = null;
		 double finalDelay = 0;
		 double finaldelayTx = 0;
		 double finalDistance = 0;
		 int index = 0;
		 
		 for(DataCenter srcDataCenter:con.getDataCenterList())
		{
			 for(DataCenter dataCenter:AvadataCenterlist)
			 {
				 Node srcNode =srcDataCenter.getAssociatedNode();//find the srcNode 
				 Node desNode = dataCenter.getAssociatedNode();
				 Nodepair nodepair = layer.findNodepair(srcNode, desNode);
				 LinearRoute path = nodepair.getLinearlist().get(0);
				 double distance = path.getDistance();
				 
				 if(index==0)
				 {
					 finalDistance = distance;	 
					 selectDataCenter = dataCenter;
					 selectedPath = path;
					 
					 
				 }
				 else if(distance<finalDistance)
				 {
					 finalDistance = distance;
					 selectDataCenter = dataCenter;
					 selectedPath = path;
				 }	
				 index++; 
			 }	
		}
		 
		 
		 //
		 int leastCapacity = 10000000;
		 for(Link link:selectedPath.getLinklist())
		 {
			   leastCapacity = (int) Math.min(leastCapacity, link.getResidualCapacity());
		 }
		  
		 finaldelayTx = Delay(selectedPath,con.getSize(),leastCapacity);
		 finalDelay = CalculateDleayForPath(selectedPath,con.getSize(),leastCapacity);
		 
		 Task currentTask = new Task();
		 
		 currentTask.setContent(con);
		 currentTask.setDataCenter(selectDataCenter);
		 currentTask.setDelayTx(finaldelayTx);
		 currentTask.setTotalTime(finalDelay);
		 currentTask.setRoute(selectedPath);
		   
		 deliveryContent(layer,currentTask);
		 double outputResult = finalDelay;
		 System.out.println("selected DataCenter: "+selectDataCenter.getAssociatedNode().getName());
	     System.out.println("develivery Con: "+con.getSize());
		 System.out.println("Time: "+outputResult);
		 if(totalMaxTime==0)
			 totalMaxTime=outputResult;
		 else if(totalMaxTime<outputResult)
			 totalMaxTime=outputResult;	
	}
	
	
	/**
	 * Select the fast path for the content f 
	 * @param layer
	 * @param con
	 * @param AvadataCenterlist
	 */
	public static void Strategy_SinglePath_SingleDestinationNode(Layer layer,Content con,ArrayList<DataCenter> AvadataCenterlist){
	
	   DataCenter selectDataCenter = null;
	   LinearRoute selectedPath = null;
	   double finalDelay = 0;
	   double finaldelayTx = 0;
	   int index = 0;
	   for(DataCenter dataCenter:AvadataCenterlist)
	   {
		   double textDelay = 0;
		    
		   Node srcNode = con.getAssociatedDataCenter().getAssociatedNode();//find the srcNode 
		   Node desNode = dataCenter.getAssociatedNode();
		   Nodepair nodepair = layer.findNodepair(srcNode, desNode);
		   LinearRoute path = nodepair.getLinearlist().get(0);
		   
		   int leastCapacity = 10000000;
		   for(Link link:path.getLinklist())
		   {
			   leastCapacity = (int) Math.min(leastCapacity, link.getResidualCapacity());
		   }
		  
		   double delayTx  = Delay(path,con.getSize(),leastCapacity);
		
		   textDelay = CalculateDleayForPath(path,con.getSize(),leastCapacity);
		   
		   if(index==0)
		   {
			   finalDelay = textDelay;
			   selectDataCenter = dataCenter;
			   selectedPath = path;
			   finaldelayTx = delayTx;
		   }
		   else
		   {
			   if(textDelay<finalDelay)
			   {
				   finalDelay = textDelay;
				   selectDataCenter = dataCenter;
				   selectedPath = path;
				   finaldelayTx = delayTx;
			   }
		   } 
		   
		   index++;
	   }
	   Task currentTask = new Task();
	 
	   currentTask.setContent(con);
	   currentTask.setDataCenter(selectDataCenter);
	   currentTask.setDelayTx(finaldelayTx);
	   currentTask.setTotalTime(finalDelay);
	   currentTask.setRoute(selectedPath);
	   
	   deliveryContent(layer,currentTask);
	   double outputResult = finalDelay;
	   System.out.println("selected DataCenter: "+selectDataCenter.getAssociatedNode().getName());
	
      System.out.println("develivery Con: "+con.getSize());
	   System.out.println("Time: "+outputResult);
	   if(totalMaxTime==0)
			 totalMaxTime=outputResult;
		 else if(totalMaxTime<outputResult)
			 totalMaxTime=outputResult;	
	
	}
		
	/**
	 * Compare the best delay time from Multipath 
	 * @param layer
	 * @param con
	 * @param AvadataCenterlist
	 */
	public static void Strategy_MultiPath_SingleDestinationNode(Layer layer,Content con,ArrayList<DataCenter> AvadataCenterlist){
		  
		DataCenter selectDataCenter = null;	
		LinearRoute selectedPath = null;
		double finalDelay = 0;
		double finaldelayTx = 0;
		int index = 0;	

		int finalLeastCapcity = 0;
		for(DataCenter srcDataCenter:con.getDataCenterList())
		{
			System.out.println("test：  "+srcDataCenter.getAssociatedNode().getName());
			for(DataCenter dataCenter:AvadataCenterlist)
			{ 
				//System.out.println("====="+dataCenter.getAssociatedNode().getName()+"=====");
				Node srcNode = srcDataCenter.getAssociatedNode();//find the srcNode
				Node desNode = dataCenter.getAssociatedNode();
				Nodepair nodepair = layer.findNodepair(srcNode, desNode);
				for(LinearRoute path:nodepair.getLinearlist())
				{
					//path.OutputRoute_link();
					double textDelay = 0;
					int leastCapacity = 10000000;  
					for(Link link:path.getLinklist())				
					{
						
						leastCapacity = (int) Math.min(leastCapacity, link.getResidualCapacity());
					}	
					//System.out.println(leastCapacity);
					double delayTx  = Delay(path,con.getSize(),leastCapacity);
					textDelay = CalculateDleayForPath(path,con.getSize(),leastCapacity);
				    //System.out.println(textDelay+" ---");
					
					if(index==0)
					{			
						finalDelay = textDelay;
						selectDataCenter = dataCenter;
						selectedPath = path;
						finaldelayTx = delayTx;  
						finalLeastCapcity = leastCapacity;
						index++;
					}
					else
					{
						if(textDelay<finalDelay)
						{ 
							 finalDelay = textDelay;
							 selectDataCenter = dataCenter;
							 selectedPath = path;
							 finaldelayTx = delayTx;
							 finalLeastCapcity = leastCapacity;
							 index++;
						}
					}       
					
				}
				
			}		 
		}
		Task currentTask = new Task();		   		
		 
		currentTask.setContent(con);
		currentTask.setDataCenter(selectDataCenter);
		currentTask.setDelayTx(finaldelayTx);
		currentTask.setTotalTime(finalDelay);		
		currentTask.setRoute(selectedPath);
		currentTask.setLeastCapacity(finalLeastCapcity);
		deliveryContent(layer,currentTask);	
		//double outputResult = finalDelay/1000000;
		
		System.out.println("selected DataCenter: "+selectDataCenter.getAssociatedNode().getName());
		currentTask.getRoute().OutputRoute_link();	
		System.out.println("develivery Con: "+con.getSize());
		//System.out.println("Time: "+outputResult);
		double outputResult = currentTask.getTotalTime();
		System.out.println("Time: "+outputResult);
		 if(totalMaxTime==0)
			 totalMaxTime=outputResult;
		 else if(totalMaxTime<outputResult)
			 totalMaxTime=outputResult;
		
		 totalContentRisk = totalContentRisk + con.getSize()*selectedPath.getRisk();
			
	}
	
	/**
	 * Compare the best delay time with risk 
	 * @param layer
	 * @param con
	 * @param AvadataCenterlist
	 */
	public static void Strategy_LDR_RDE(Layer layer,Content con,ArrayList<DataCenter> AvadataCenterlist){
		  
		Functions_Rapid_DataEvacuation workFunction = new Functions_Rapid_DataEvacuation();
		DataCenter selectDataCenter = null;	
		LinearRoute selectedPath = null;
		double finalDelay = 0;
		double finaldelayTx = 0;
		int index = 0;	

		int finalLeastCapcity = 0;
		for(DataCenter srcDataCenter:con.getDataCenterList())
		{
			System.out.println("test：  "+srcDataCenter.getAssociatedNode().getName());
			for(DataCenter dataCenter:AvadataCenterlist)
			{ 
				//System.out.println("====="+dataCenter.getAssociatedNode().getName()+"=====");
				Node srcNode = srcDataCenter.getAssociatedNode();//find the srcNode
				Node desNode = dataCenter.getAssociatedNode();
				Nodepair nodepair = layer.findNodepair(srcNode, desNode);
				for(LinearRoute path:nodepair.getLinearlist())
				{
					//path.OutputRoute_link();
					double textDelay = 0;
					
					double risk = path.getRisk();
					
					int leastCapacity = 10000000;  
					for(Link link:path.getLinklist())				
					{
						
						leastCapacity = (int) Math.min(leastCapacity, link.getResidualCapacity());
					}	
					//System.out.println(leastCapacity);
					double delayTx  = Delay(path,con.getSize(),leastCapacity);
					textDelay = CalculateDleayForPath(path,con.getSize(),leastCapacity);
				    //System.out.println(textDelay+" ---");
					textDelay = (double)(Math.round(textDelay*1000)/1000.0);
					if(index==0)
					{			
						finalDelay = textDelay;
						selectDataCenter = dataCenter;
						selectedPath = path;
						finaldelayTx = delayTx;  
						finalLeastCapcity = leastCapacity;
						index++;
					}
					else
					{
						if(workFunction.costEquation(textDelay,risk)<workFunction.costEquation(finalDelay,selectedPath.getRisk()))
						{ 
							 finalDelay = textDelay;
							 selectDataCenter = dataCenter;
							 selectedPath = path;
							 finaldelayTx = delayTx;
							 finalLeastCapcity = leastCapacity;
							 index++;
						}
					}       
				}
			}		 
		}
		Task currentTask = new Task();		   		
		 
		currentTask.setContent(con);
		currentTask.setDataCenter(selectDataCenter);
		currentTask.setDelayTx(finaldelayTx);
		currentTask.setTotalTime(finalDelay);		
		currentTask.setRoute(selectedPath);
		currentTask.setLeastCapacity(finalLeastCapcity);
		deliveryContent(layer,currentTask);	
		//double outputResult = finalDelay/1000000;
		
		System.out.println("selected DataCenter: "+selectDataCenter.getAssociatedNode().getName());
		currentTask.getRoute().OutputRoute_link();	
		System.out.println("develivery Con: "+con.getSize());
		//System.out.println("Time: "+outputResult);
		double outputResult = currentTask.getTotalTime();
		System.out.println("Time: "+outputResult);
		 if(totalMaxTime==0)
			 totalMaxTime=outputResult;
		 else if(totalMaxTime<outputResult)
			 totalMaxTime=outputResult;
		 totalContentRisk = totalContentRisk + con.getSize()*selectedPath.getRisk();
		
			
	}
	
	/**
	 * SubContent
	 * @param layer
	 * @param con
	 * @param AvadataCenterlist
	 */
	public static void Strategy_MultiPath_SubContent_SingleDestinationNode(Layer layer,Content con,ArrayList<DataCenter> AvadataCenterlist){
		
		ArrayList<Task>finalTaskList = new ArrayList<Task>();	
		double finalminiTime = 0;
		int finalIndex = 0;
		System.out.println("======");
		for(DataCenter dataCenter:AvadataCenterlist)
		{
			System.out.println("=="+dataCenter.getAssociatedNode().getName()+"==");
			ArrayList<Task> selectedTaskList = new ArrayList<Task>();
			
			
			Node srcNode = con.getAssociatedDataCenter().getAssociatedNode();//find the srcNode
			Node desNode = dataCenter.getAssociatedNode();
			Nodepair nodepair = layer.findNodepair(srcNode, desNode);
			
			
			
			//System.out.println("到节点: "+desNode.getName());
			int subConIndex = 0;
			int subRouteIndex = 0;
			double MaxiTime = 0;
			double TotalTime = 0;
			int unfinishedContent = con.getSize();
			int routeCapacity = 0;
			
			
			
			
			for(Content subCon:con.getSubContentList())
			{
				System.out.println(con.getSubContentList().size());
				LinearRoute route = nodepair.getLinearlist().get(subRouteIndex);
				route.calRouteCapacity();
				routeCapacity= routeCapacity+route.getRouteCpacity();
				subCon.setRoute(route);
				double Linkdelay = 0;
				for(Link link:route.getLinklist())
				{
					System.out.println(link.getName()+" "+ link.getLinkdelay());
					Linkdelay = Math.max(Linkdelay, link.getLinkdelay());
				}
				subCon.setStartTime(Linkdelay);
				subRouteIndex++;
				System.out.println("Linkdelay: "+Linkdelay);
				MaxiTime = Math.max(MaxiTime, Linkdelay);
				TotalTime = TotalTime+Linkdelay;
				System.out.println("MaxiTime: "+MaxiTime);
			}
			con.setStartTime(MaxiTime);

			
			boolean keyAll = false;
			int subContent = 0;
			for(Content subCon:con.getSubContentList())
			{
				
				if(con.getStartTime()==0)//time delay of subContent eauqls to zero, divide the content into equal size
				{
					if(keyAll) 
					{
						System.out.println("divide the content into equal size");
						int subSize = con.getSize()-subContent;
						subCon.setSize(subSize);
						System.out.println(subSize);
						unfinishedContent = unfinishedContent - subSize;
					}
					else
					{
						System.out.println("divide the content into equal size");
						int subSize =  (int)(con.getSize()/con.getSubContentList().size());
						subCon.setSize(subSize);
						System.out.println(subSize);
						unfinishedContent = unfinishedContent - subSize;
						subContent=subSize;
						keyAll = true;
					}
				}
				else
				{
					if(unfinishedContent !=0)
					{
						int subSize = (int)(con.getSize()*subCon.getStartTime()/TotalTime);
						subCon.setSize(subSize);
						unfinishedContent = unfinishedContent - subSize;
						
					}
					else{
						subCon.setSize(0);
					}
					/*
					if(subCon.getStartTime()==0)
					{
						System.out.println("use the entire path to evacuate the content");
						subCon.setSize(con.getSize());
						unfinishedContent = 0;
					}
					else
					{
						if(unfinishedContent !=0)
						{
							int subSize = (int)(con.getSize()*subCon.getStartTime()/TotalTime);
							subCon.setSize(subSize);
							unfinishedContent = unfinishedContent - subSize;
							
						}
						else{
							subCon.setSize(0);
						}
					}
					*/
				}
			}
			
			
			for(Content subCon:con.getSubContentList())
			{
				if(subCon.getSize()!=0)
				{
					int index = 0;	
					DataCenter currentDataCenter = null;	
					LinearRoute currentPath = null;
					double currentDelay = 0;
					double currentdelayTx = 0;
					ArrayList<LinearRoute> routelist = new ArrayList<LinearRoute>();
					LinearRoute path = nodepair.getLinearlist().get(subConIndex);
					//System.out.println("第 "+subConIndex+"个路由");
					subConIndex++;
					
				//	for(LinearRoute path:nodepair.getLinearlist())
					{
						
						//System.out.println(nodepair.getLinearlist().size()+" "+subCon.getSize());
						double textDelay = 0;
						int leastCapacity = 10000000;  					
						for(Link link:path.getLinklist())				
						{
							leastCapacity = (int) Math.min(leastCapacity, link.getResidualCapacity());
							
						}	
					
						double delayTx  = Delay(path,subCon.getSize(),leastCapacity);
						textDelay = CalculateDleayForPath(path,subCon.getSize(),leastCapacity);
						System.out.println(textDelay);
						if(index==0)
						{			
							currentDelay = textDelay;
							currentDataCenter = dataCenter;
							currentPath = path;
							currentdelayTx = delayTx;   
						}
						else
						{
							if(textDelay<currentDelay)
							{ 
								currentDelay = textDelay;
								currentDataCenter = dataCenter;
								currentPath = path;
								currentdelayTx = delayTx;   
							}
						}
						index++;
					}
					routelist.add(currentPath);
					Task currentTask = new Task();		   		 
					currentTask.setContent(subCon);
					currentTask.setDataCenter(currentDataCenter);
					currentTask.setDelayTx(currentdelayTx);
					currentTask.setTotalTime(currentDelay);		
					currentTask.setRoute(currentPath);
					selectedTaskList.add(currentTask);
					System.out.println("DelayTxTime: "+currentTask.getDelayTx());	
					System.out.println("TotalTime: "+currentTask.getTotalTime());	
				}
				
			}
			//find the miniTime
			int taskIndex = 0;
			double maxiTime = 0;
			for(Task task:selectedTaskList)
			{
				if(taskIndex==0)
					maxiTime = task.getTotalTime();
				else
				{
					if(maxiTime<task.getTotalTime())
						maxiTime = task.getTotalTime();
				}
				taskIndex++;
			}
			//compare with the finaltime
			if(finalIndex==0)
			{
				finalminiTime = maxiTime;
				finalTaskList = selectedTaskList;
			}
			else{
				if(finalminiTime>maxiTime)
				{
					finalminiTime = maxiTime;
					finalTaskList = selectedTaskList;
				}
			}
			finalIndex++;
			
		}	
		
		//delivery the task
		for(Task task:finalTaskList)
		{
			if(task.getContent().getSize()!=0)
			{
				deliveryContent(layer,task);
				System.out.println("selected DataCenter: "+task.getDataCenter().getAssociatedNode().getName());
				System.out.println("develivery Con: "+task.getContent().getSize());
				task.getRoute().OutputRoute_link();
				double outputResult = task.getTotalTime();
				System.out.println("Time: "+outputResult);
				 if(totalMaxTime==0)
					 totalMaxTime=outputResult;
				 else if(totalMaxTime<outputResult)
					 totalMaxTime=outputResult;
			}
		
		}
	
	//	double outputResult = finalminiTime/1000000;
	//	System.out.println("Time: "+outputResult);
	}
	/**
	 * SubContent
	 * @param layer
	 * @param con
	 * @param AvadataCenterlist
	 */
	public static void TestStrategy_MultiPath_SubContent_SingleDestinationNode(Layer layer,Content con,ArrayList<DataCenter> AvadataCenterlist){
		
		ArrayList<Task>finalTaskList = new ArrayList<Task>();	
		double finalminiTime = 0;
		int finalIndex = 0;
		LinearRoute selectedPath = null;
		DataCenter selectDataCenter = null;
		RoutePair selectedPathPair = null;
		double finalDelay = 1000000;
		double finalfirstDelay = 0;
		double finalSecondDelay = 0;
		
		int finalfirstSize = 0;
		int finalsecondSize = 0;
		
		double finalfirstPathDelayTx = 0;
		double finalsecondPathDelayTx = 0;
		
		RoutePair finalRoutePair = null;
		
		double finaldelayTx = 0;
		boolean subkey = false; 
		//System.out.println("======");
		ArrayList<RoutePair> routepairList =GenerateAllRoutePairSet(layer,con,AvadataCenterlist);
		System.out.println("routepair size: "+routepairList.size());
		
		for(DataCenter srcDataCenter:con.getDataCenterList())
		{
			for(DataCenter dataCenter:AvadataCenterlist)
			{ 
				System.out.println("====="+dataCenter.getAssociatedNode().getName()+"=====");
				Node srcNode = srcDataCenter.getAssociatedNode();//find the srcNode
				Node desNode = dataCenter.getAssociatedNode();
				Nodepair nodepair = layer.findNodepair(srcNode, desNode);
				//System.out.println("到节点: "+desNode.getName());
				for(LinearRoute path:nodepair.getLinearlist())
				{
					double textDelay = 0;
					int leastCapacity = 10000000;  
					for(Link link:path.getLinklist())				
					{
						leastCapacity = (int) Math.min(leastCapacity, link.getResidualCapacity());
					}	
					double delayTx  = Delay(path,con.getSize(),leastCapacity);
					textDelay = CalculateDleayForPath(path,con.getSize(),leastCapacity);
					if(finalIndex==0)
					{			
						finalDelay = textDelay;
						selectDataCenter = dataCenter;
						selectedPath = path;
						finaldelayTx = delayTx;   
						finalIndex++;
					}
					else
					{
						if(textDelay<finalDelay)
						{ 
							 finalDelay = textDelay;
							 selectDataCenter = dataCenter;
							 selectedPath = path;
							 finaldelayTx = delayTx;
							 finalIndex++;
						}
					}       
				}
		
			}
		}
			
		for(RoutePair routepair:routepairList)
		{
			double MaximumDelay = 0;
			
			LinearRoute firstpath = routepair.getFirstRoute();
			LinearRoute secondpath = routepair.getSecondRoute();
			
			firstpath.calRouteCapacity();
			secondpath.calRouteCapacity();  	
			
			double firstpathDelay = firstpath.calRouteDelay();
			double secondpathDelay = secondpath.calRouteDelay();
			
			//divide the content into different size
			int firstpathSize =(int) (con.getSize()*firstpath.getRouteCpacity()/(firstpath.getRouteCpacity()+secondpath.getRouteCpacity()));
			
			int secondpathSize = con.getSize()-firstpathSize;
			
			double firstPathDelay = 0;	
			double firstPathDelayTx  = Delay(firstpath,firstpathSize,firstpath.getRouteCpacity());
			firstPathDelay = CalculateDleayForPath(firstpath,firstpathSize,firstpath.getRouteCpacity());
			
			double secondPathDelay = 0;	
			double secondPathDelayTx  = Delay(secondpath,secondpathSize,secondpath.getRouteCpacity());
			secondPathDelay = CalculateDleayForPath(secondpath,secondpathSize,secondpath.getRouteCpacity());
			MaximumDelay = Math.max(firstPathDelay, secondPathDelay);
			
			//System.out.println("firstpath: "+firstpathSize+" "+firstPathDelay);
			//System.out.println("secondpath: "+secondpathSize+" "+secondPathDelay);
			
			if(finalIndex==0)
			{			
				
				finalDelay = MaximumDelay;
			//	selectDataCenter = routepair;
				selectedPathPair = routepair;
				finalfirstDelay = firstPathDelay;
				finalSecondDelay = secondPathDelay;
				
				finalfirstSize =firstpathSize;
				finalsecondSize = secondpathSize;
				
				finalfirstPathDelayTx = firstPathDelayTx;
				finalsecondPathDelayTx = secondPathDelayTx;
				
				finalRoutePair = routepair;
				subkey = true;
				finalIndex++;
			}
			else
			{
				if(MaximumDelay<finalDelay)
				{ 
				//	System.out.println(dataCenter.getAssociatedNode().getName());
					finalDelay = MaximumDelay;
				//	selectDataCenter = dataCenter;
					selectedPathPair = routepair;
					finalfirstDelay = firstPathDelay;
					finalSecondDelay = secondPathDelay;
					
					finalfirstSize =firstpathSize;
					finalsecondSize = secondpathSize;
					
					finalfirstPathDelayTx = firstPathDelayTx;
					finalsecondPathDelayTx = secondPathDelayTx;
					finalRoutePair = routepair;
					subkey = true;
					finalIndex++;
				}
			} 	
		}	 
			
		Task currentTask = new Task();		   		
		if(subkey){
			currentTask.setContent(con);
		//	currentTask.setDataCenter(selectDataCenter);
			currentTask.setRoutepair(finalRoutePair);
			
			currentTask.setFirstDataCenter(finalRoutePair.getFirstRouteAssociatedDataCenter());
			currentTask.setSecondDataCenter(finalRoutePair.getSecondRouteAssociatedDataCenter());
		
			currentTask.setDelayTx(finaldelayTx);
			currentTask.setTotalTime(finalDelay);	
			currentTask.setRoutepair(selectedPathPair);
		    currentTask.setFirstPathDelay(finalfirstDelay);
		    currentTask.setSecondPathDelay(finalSecondDelay);
			currentTask.setFirstPathDelayTx(finalfirstPathDelayTx);
			currentTask.setSecondPathDelayTx(finalsecondPathDelayTx);
		    currentTask.setFirstSize(finalfirstSize);
			currentTask.setSecondSize(finalsecondSize);
			System.out.println("first selected DataCenter: "+currentTask.getFirstDataCenter().getAssociatedNode().getName());
			deliveryContentWithSubContent(layer,currentTask);	
			//double outputResult = finalDelay/1000000;
			
		//	System.out.println("selected DataCenter: "+selectDataCenter.getAssociatedNode().getName());
		//	currentTask.getRoute().OutputRoute_link();	
			currentTask.getRoutepair().getFirstRoute().OutputRoute_link();	
			
			System.out.println(currentTask.getFirstSize());
			System.out.println(currentTask.getFirstPathDelay());
			System.out.println("======================");
			System.out.println("second selected DataCenter: "+currentTask.getSecondDataCenter().getAssociatedNode().getName());
			currentTask.getRoutepair().getSecondRoute().OutputRoute_link();	
			System.out.println(currentTask.getSecondSize());
			System.out.println(currentTask.getSecondPathDelay());
		}
		else
		{
			currentTask.setContent(con);
			currentTask.setDataCenter(selectDataCenter);
			currentTask.setDelayTx(finaldelayTx);
			currentTask.setTotalTime(finalDelay);		
			currentTask.setRoute(selectedPath);
		
			deliveryContent(layer,currentTask);	
			//double outputResult = finalDelay/1000000;
			
			System.out.println("selected DataCenter: "+selectDataCenter.getAssociatedNode().getName());
			currentTask.getRoute().OutputRoute_link();	
		}
		System.out.println("develivery Con: "+con.getSize());
		//System.out.println("Time: "+outputResult);
		double outputResult = currentTask.getTotalTime();
		System.out.println("Time: "+outputResult);
		 if(totalMaxTime==0)
			 totalMaxTime=outputResult;
		 else if(totalMaxTime<outputResult)
			 totalMaxTime=outputResult;	
	}
	/**
	 * SubContent-Test
	 * @param layer
	 * @param con
	 * @param AvadataCenterlist
	 */
	public static void TestStrategy(Layer layer,Content con,ArrayList<DataCenter> AvadataCenterlist){
		
		ArrayList<Task>finalTaskList = new ArrayList<Task>();	
		double finalminiTime = 0;
		int finalIndex = 0;
		LinearRoute selectedPath = null;
		DataCenter selectDataCenter = null;
		RoutePair selectedPathPair = null;
		double finalDelay = 1000000;
		double finalfirstDelay = 0;
		double finalSecondDelay = 0;
		
		int finalfirstSize = 0;
		int finalsecondSize = 0;
		
		double finalfirstPathDelayTx = 0;
		double finalsecondPathDelayTx = 0;
		
		RoutePair finalRoutePair = null;
		
		double finaldelayTx = 0;
		boolean subkey = false; 
		//System.out.println("======");
		ArrayList<RoutePair> routepairList =GenerateAllRoutePairSet(layer,con,AvadataCenterlist);
		System.out.println("routepair size: "+routepairList.size());
		
		for(DataCenter srcDataCenter:con.getDataCenterList())
		{
			for(DataCenter dataCenter:AvadataCenterlist)
			{ 
				System.out.println("====="+dataCenter.getAssociatedNode().getName()+"=====");
				Node srcNode = srcDataCenter.getAssociatedNode();//find the srcNode
				Node desNode = dataCenter.getAssociatedNode();
				Nodepair nodepair = layer.findNodepair(srcNode, desNode);
				//System.out.println("到节点: "+desNode.getName());
				for(LinearRoute path:nodepair.getLinearlist())
				{
					double textDelay = 0;
					int leastCapacity = 10000000;  
					for(Link link:path.getLinklist())				
					{
						leastCapacity = (int) Math.min(leastCapacity, link.getResidualCapacity());
					}	
					double delayTx  = Delay(path,con.getSize(),leastCapacity);
					textDelay = CalculateDleayForPath(path,con.getSize(),leastCapacity);
					if(finalIndex==0)
					{			
						finalDelay = textDelay;
						selectDataCenter = dataCenter;
						selectedPath = path;
						finaldelayTx = delayTx;   
						finalIndex++;
					}
					else
					{
						if(textDelay<finalDelay)
						{ 
							 finalDelay = textDelay;
							 selectDataCenter = dataCenter;
							 selectedPath = path;
							 finaldelayTx = delayTx;
							 finalIndex++;
						}
					}       
				}
		
			}
		}
		selectedPath.OutputRoute_link();
		for(RoutePair routepair:routepairList)
		{
			if(routepair.getFirstRoute()==selectedPath||routepair.getSecondRoute()==selectedPath)
			{
				double MaximumDelay = 0;
				
				LinearRoute firstpath = null;
				LinearRoute secondpath = null;
				
				if(routepair.getFirstRoute()==selectedPath)
				{
					firstpath = routepair.getFirstRoute();
					secondpath = routepair.getSecondRoute();
				}
				else
				{
					firstpath  = routepair.getSecondRoute();
					secondpath = routepair.getFirstRoute();
					
				}
				
				firstpath.calRouteCapacity();
				secondpath.calRouteCapacity();  	
				
				double firstpathDelay = firstpath.calRouteDelay();
				double secondpathDelay = secondpath.calRouteDelay();
				
				//divide the content into different size
				int firstpathSize =(int) (con.getSize()*0.5);
				
				int secondpathSize = con.getSize()-firstpathSize;
				
				double firstPathDelay = 0;	
				double firstPathDelayTx  = Delay(firstpath,firstpathSize,firstpath.getRouteCpacity());
				firstPathDelay = CalculateDleayForPath(firstpath,firstpathSize,firstpath.getRouteCpacity());
				
				double secondPathDelay = 0;	
				double secondPathDelayTx  = Delay(secondpath,secondpathSize,secondpath.getRouteCpacity());
				secondPathDelay = CalculateDleayForPath(secondpath,secondpathSize,secondpath.getRouteCpacity());
				MaximumDelay = Math.max(firstPathDelay, secondPathDelay);
				
				//System.out.println("firstpath: "+firstpathSize+" "+firstPathDelay);
				//System.out.println("secondpath: "+secondpathSize+" "+secondPathDelay);
				
				if(finalIndex==0)
				{			
					
					finalDelay = MaximumDelay;
				//	selectDataCenter = routepair;
					selectedPathPair = routepair;
					finalfirstDelay = firstPathDelay;
					finalSecondDelay = secondPathDelay;
					
					finalfirstSize =firstpathSize;
					finalsecondSize = secondpathSize;
					
					finalfirstPathDelayTx = firstPathDelayTx;
					finalsecondPathDelayTx = secondPathDelayTx;
					
					finalRoutePair = routepair;
					subkey = true;
					finalIndex++;
				}
				else
				{
					if(MaximumDelay<finalDelay)
					{ 
					//	System.out.println(dataCenter.getAssociatedNode().getName());
						finalDelay = MaximumDelay;
					//	selectDataCenter = dataCenter;
						selectedPathPair = routepair;
						finalfirstDelay = firstPathDelay;
						finalSecondDelay = secondPathDelay;
						
						finalfirstSize =firstpathSize;
						finalsecondSize = secondpathSize;
						
						finalfirstPathDelayTx = firstPathDelayTx;
						finalsecondPathDelayTx = secondPathDelayTx;
						finalRoutePair = routepair;
						subkey = true;
						finalIndex++;
					}
				} 	
			}
			
		}	 
			
		Task currentTask = new Task();		   		
		if(subkey){
			currentTask.setContent(con);
		//	currentTask.setDataCenter(selectDataCenter);
			currentTask.setRoutepair(finalRoutePair);
			
			currentTask.setFirstDataCenter(finalRoutePair.getFirstRouteAssociatedDataCenter());
			currentTask.setSecondDataCenter(finalRoutePair.getSecondRouteAssociatedDataCenter());
		
			currentTask.setDelayTx(finaldelayTx);
			currentTask.setTotalTime(finalDelay);	
			currentTask.setRoutepair(selectedPathPair);
		    currentTask.setFirstPathDelay(finalfirstDelay);
		    currentTask.setSecondPathDelay(finalSecondDelay);
			currentTask.setFirstPathDelayTx(finalfirstPathDelayTx);
			currentTask.setSecondPathDelayTx(finalsecondPathDelayTx);
		    currentTask.setFirstSize(finalfirstSize);
			currentTask.setSecondSize(finalsecondSize);
			System.out.println("first selected DataCenter: "+currentTask.getFirstDataCenter().getAssociatedNode().getName());
			deliveryContentWithSubContent(layer,currentTask);	
			//double outputResult = finalDelay/1000000;
			
		//	System.out.println("selected DataCenter: "+selectDataCenter.getAssociatedNode().getName());
		//	currentTask.getRoute().OutputRoute_link();	
			currentTask.getRoutepair().getFirstRoute().OutputRoute_link();	
			
			System.out.println(currentTask.getFirstSize());
			System.out.println(currentTask.getFirstPathDelay());
			System.out.println("======================");
			System.out.println("second selected DataCenter: "+currentTask.getSecondDataCenter().getAssociatedNode().getName());
			currentTask.getRoutepair().getSecondRoute().OutputRoute_link();	
			System.out.println(currentTask.getSecondSize());
			System.out.println(currentTask.getSecondPathDelay());
		}
		else
		{
			currentTask.setContent(con);
			currentTask.setDataCenter(selectDataCenter);
			currentTask.setDelayTx(finaldelayTx);
			currentTask.setTotalTime(finalDelay);		
			currentTask.setRoute(selectedPath);
		
			deliveryContent(layer,currentTask);	
			//double outputResult = finalDelay/1000000;
			
			System.out.println("selected DataCenter: "+selectDataCenter.getAssociatedNode().getName());
			currentTask.getRoute().OutputRoute_link();	
		}
		System.out.println("develivery Con: "+con.getSize());
		//System.out.println("Time: "+outputResult);
		double outputResult = currentTask.getTotalTime();
		System.out.println("Time: "+outputResult);
		 if(totalMaxTime==0)
			 totalMaxTime=outputResult;
		 else if(totalMaxTime<outputResult)
			 totalMaxTime=outputResult;	
	}
	/**
	 * SubContent
	 * @param layer
	 * @param con
	 * @param AvadataCenterlist
	 */
	public static void TestStrategy_MultiReplica_SubContent_SingleDestinationNode(Layer layer,Content con,ArrayList<DataCenter> AvadataCenterlist){
		
		ArrayList<Task>finalTaskList = new ArrayList<Task>();	
		double finalminiTime = 0;
		int finalIndex = 0;
		LinearRoute selectedPath = null;
		DataCenter selectDataCenter = null;
		RoutePair selectedPathPair = null;
		double finalDelay = 0;
		double finalfirstDelay = 0;
		double finalSecondDelay = 0;
		
		int finalfirstSize = 0;
		int finalsecondSize = 0;
		
		double finaldelayTx = 0;
		boolean subkey = false; 
		//System.out.println("======");
		ArrayList<RoutePair> routepairList =GenerateAllRoutePairSet(layer,con,AvadataCenterlist);
		System.out.println("routepair size: "+routepairList.size());
		for(DataCenter dataCenter:AvadataCenterlist)
		{
			//System.out.println("=="+dataCenter.getAssociatedNode().getName()+"==");
			ArrayList<Task> selectedTaskList = new ArrayList<Task>();
			
			
			Node srcNode = con.getAssociatedDataCenter().getAssociatedNode();//find the srcNode
			Node desNode = dataCenter.getAssociatedNode();
			Nodepair nodepair = layer.findNodepair(srcNode, desNode);
			
		//ArrayList<RoutePair> routepairList = GenerateRoutePairSet( nodepair,  con);
	  // System.out.println("routepair size: "+routepairList.size());
		
			for(LinearRoute path:nodepair.getLinearlist())
			{
				double textDelay = 0;
				int leastCapacity = 10000000;  
				for(Link link:path.getLinklist())				
				{
					leastCapacity = (int) Math.min(leastCapacity, link.getResidualCapacity());
				}	
				double delayTx  = Delay(path,con.getSize(),leastCapacity);
				textDelay = CalculateDleayForPath(path,con.getSize(),leastCapacity);
				if(finalIndex==0)
				{			
					finalDelay = textDelay;
					selectDataCenter = dataCenter;
					selectedPath = path;
					finaldelayTx = delayTx;   
					finalIndex++;
				}
				else
				{
					if(textDelay<finalDelay)
					{ 
						 finalDelay = textDelay;
						 selectDataCenter = dataCenter;
						 selectedPath = path;
						 finaldelayTx = delayTx;
						 finalIndex++;
					}
				}       
				
			}
			
	
			//System.out.println("到节点: "+desNode.getName());
			
			for(RoutePair routepair:routepairList)
			{
				double MaximumDelay = 0;
				
				LinearRoute firstpath = routepair.getFirstRoute();
				LinearRoute secondpath = routepair.getSecondRoute();
				
				firstpath.calRouteCapacity();
				secondpath.calRouteCapacity();  	
				
				double firstpathDelay = firstpath.calRouteDelay();
				double secondpathDelay = secondpath.calRouteDelay();
				
				int firstpathSize =con.getSize();
				
				int secondpathSize = con.getSize();
				
				double firstPathDelay = 0;	
				double firstPathDelayTx  = Delay(firstpath,firstpathSize,firstpath.getRouteCpacity());
				firstPathDelay = CalculateDleayForPath(firstpath,firstpathSize,firstpath.getRouteCpacity());
				
				double secondPathDelay = 0;	
				double secondPathDelayTx  = Delay(secondpath,secondpathSize,secondpath.getRouteCpacity());
				secondPathDelay = CalculateDleayForPath(secondpath,secondpathSize,secondpath.getRouteCpacity());
				MaximumDelay = Math.max(firstPathDelay, secondPathDelay);
				
				//System.out.println("firstpath: "+firstpathSize+" "+firstPathDelay);
				//System.out.println("secondpath: "+secondpathSize+" "+secondPathDelay);
				
				if(finalIndex==0)
				{			
					finalDelay = MaximumDelay;
					selectDataCenter = dataCenter;
					selectedPathPair = routepair;
					finalfirstDelay = firstPathDelay;
					finalSecondDelay = secondPathDelay;
					
					finalfirstSize =firstpathSize;
					finalsecondSize = secondpathSize;
					
					subkey = true;
					finalIndex++;
				}
				else
				{
					if(MaximumDelay<finalDelay)
					{ 
						finalDelay = MaximumDelay;
						selectDataCenter = dataCenter;
						selectedPathPair = routepair;
						finalfirstDelay = firstPathDelay;
						finalSecondDelay = secondPathDelay;
						
						finalfirstSize =firstpathSize;
						finalsecondSize = secondpathSize;
						
						subkey = true;
						finalIndex++;
					}
				} 
				
			}
		}		 
			
		Task currentTask = new Task();		   		
		if(subkey){
			currentTask.setContent(con);
			currentTask.setDataCenter(selectDataCenter);
			currentTask.setDelayTx(finaldelayTx);
			currentTask.setTotalTime(finalDelay);	
			currentTask.setRoutepair(selectedPathPair);
		    currentTask.setFirstPathDelay(finalfirstDelay);
		    currentTask.setSecondPathDelay(finalSecondDelay);
			
		    currentTask.setFirstSize(finalfirstSize);
			currentTask.setSecondSize(finalsecondSize);
			
			deliveryContentWithSubContent(layer,currentTask);	
			//double outputResult = finalDelay/1000000;
			
			System.out.println("selected DataCenter: "+selectDataCenter.getAssociatedNode().getName());
		//	currentTask.getRoute().OutputRoute_link();	
			currentTask.getRoutepair().getFirstRoute().OutputRoute_link();	
			System.out.println(currentTask.getFirstSize());
			System.out.println(currentTask.getFirstPathDelay());
			System.out.println("======================");
			currentTask.getRoutepair().getSecondRoute().OutputRoute_link();	
			System.out.println(currentTask.getSecondSize());
			System.out.println(currentTask.getSecondPathDelay());
		}
		else
		{
			currentTask.setContent(con);
			currentTask.setDataCenter(selectDataCenter);
			currentTask.setDelayTx(finaldelayTx);
			currentTask.setTotalTime(finalDelay);		
			currentTask.setRoute(selectedPath);
		
			deliveryContent(layer,currentTask);	
			//double outputResult = finalDelay/1000000;
			
			System.out.println("selected DataCenter: "+selectDataCenter.getAssociatedNode().getName());
			currentTask.getRoute().OutputRoute_link();	
		}
		System.out.println("develivery Con: "+con.getSize());
		//System.out.println("Time: "+outputResult);
		double outputResult = currentTask.getTotalTime();
		System.out.println("Time: "+outputResult);
		 if(totalMaxTime==0)
			 totalMaxTime=outputResult;
		 else if(totalMaxTime<outputResult)
			 totalMaxTime=outputResult;
		
	}
	
	/**
	 * scheme of applying the multipath vs multi-destination vs sub-Content
	 * @param layer
	 * @param con
	 * @param AvadataCenterlist
	 */
	public static void Strategy_MultiPath_MultiDestinationNode(Layer layer,Content con,ArrayList<DataCenter> AvadataCenterlist){
		
		ArrayList<Task> finalTaskList = new ArrayList<Task>();	
		for(Content subCon:con.getSubContentList())
		{
			
			DataCenter selectDataCenter = null;	
			LinearRoute selectedPath = null;
			double finalDelay = 0;
			double finaldelayTx = 0;
			int index = 0;	
			//System.out.println(subCon.getAssociatedDataCenter().getAssociatedNode().getName());
			for(DataCenter dataCenter:AvadataCenterlist)
			{
				Node srcNode = con.getAssociatedDataCenter().getAssociatedNode();//find the srcNode
				Node desNode = dataCenter.getAssociatedNode();
				Nodepair nodepair = layer.findNodepair(srcNode, desNode);
				
				for(LinearRoute path:nodepair.getLinearlist())
				{
					
					if(finalTaskList.size()==0||!sharedLink(finalTaskList, path))
					{
						double textDelay = 0;
						int leastCapacity = 10000000;  
						for(Link link:path.getLinklist())				
						{
							leastCapacity = (int) Math.min(leastCapacity, link.getResidualCapacity());
						}	
						double delayTx  = Delay(path,subCon.getSize(),leastCapacity);
					
						textDelay = CalculateDleayForPath(path,subCon.getSize(),leastCapacity);
						
						if(index==0)
						{			
							finalDelay = textDelay;
							selectDataCenter = dataCenter;
							selectedPath = path;
							finaldelayTx = delayTx;   
						}
						else
						{
							if(textDelay<finalDelay)
							{ 
								 finalDelay = textDelay;
								 selectDataCenter = dataCenter;
								 selectedPath = path;
								 finaldelayTx = delayTx;
							}
						}       
					}
				}
				index++;
			}
			if(selectDataCenter!=null)
			{
				Task currentTask = new Task();		   		
				
				currentTask.setContent(subCon);
				currentTask.setDataCenter(selectDataCenter);
				currentTask.setDelayTx(finaldelayTx);
				currentTask.setTotalTime(finalDelay);		
				currentTask.setRoute(selectedPath);
				finalTaskList.add(currentTask);
			

			}
			
		}
		//delivery the task
		for(Task task:finalTaskList)
		{
					
			deliveryContent(layer,task);
			System.out.println("selected DataCenter: "+task.getDataCenter().getAssociatedNode().getName());			
			System.out.println("develivery Con: "+task.getContent().getSize());
			task.getRoute().OutputRoute_link();
			double outputResult = task.getTotalTime();
			System.out.println("Time: "+outputResult);
		}
		
		
	//	System.out.println("Time: "+outputResult);
	
	}	
	
	/**
	 * Share link
	 * @param finalTaskList
	 * @param path
	 * @return
	 */
	public static boolean sharedLink(ArrayList<Task> finalTaskList,LinearRoute path)
	{
		boolean common = false;
		
		for(Task task:finalTaskList)
		{
		   if(task.getRoute()!=null)
		   {
			   LinearRoute selectedPath = task.getRoute();
			  
			   if(selectedPath.shareCommonLink(path))
			   {
				   common = true;
				   break;
			   }
		   }
		}
		return common;
		
	}


	/**
	 * Generate Route pair set
	 * @param nodepair
	 * @param con
	 * @return
	 */
    public static ArrayList<RoutePair> GenerateRoutePairSet(Nodepair nodepair, Content con)
    {
    	ArrayList<RoutePair> routepairList = new ArrayList<RoutePair>();
    	for(int i = 0;i<nodepair.getLinearlist().size();i++)
    	{
    		
    		LinearRoute firstRoute = nodepair.getLinearlist().get(i);
    		for(int j = i+1;j<nodepair.getLinearlist().size();j++) 	
    		{
    			RoutePair routePair = new RoutePair();
    			LinearRoute SecondRoute = nodepair.getLinearlist().get(j);
    			routePair.setFirstRoute(firstRoute);
    			routePair.setSecondRoute(SecondRoute);
    			routepairList.add(routePair);
    		}
    	}
    	return routepairList;
    
    }
    
    /**
     * Generate All Route Pair Set
     * @param layer
     * @param con
     * @param AvadataCenterlist
     * @return
     */
    public static ArrayList<RoutePair> GenerateAllRoutePairSet(Layer layer,Content con,ArrayList<DataCenter> AvadataCenterlist)
    {
    	ArrayList<LinearRoute> routeList = new ArrayList<LinearRoute>();
    	ArrayList<RoutePair> routepairList = new ArrayList<RoutePair>();
    	for(DataCenter datacenter:con.getDataCenterList())
    	{
    		Node srcNode = datacenter.getAssociatedNode();//find the srcNode
        	for(int i = 0; i<AvadataCenterlist.size();i++)
        	{
        		DataCenter currentDataCenter = AvadataCenterlist.get(i);
        		
    			Node desNode = currentDataCenter.getAssociatedNode();
    			Nodepair nodepair = layer.findNodepair(srcNode, desNode);
    			
    			for(int k = 0;k<nodepair.getLinearlist().size();k++)
    	    	{
    				nodepair.getLinearlist().get(k).setDesDataCenter(currentDataCenter);
    				routeList.add(nodepair.getLinearlist().get(k));
    	    	}
        	}
        	
        	for(int i = 0;i<routeList.size();i++)
        	{
        		
        		LinearRoute firstRoute = routeList.get(i);
        		for(int j = i+1;j<routeList.size();j++) 	
        		{
        			RoutePair routePair = new RoutePair();
        			
        			
        			LinearRoute SecondRoute = routeList.get(j);
        			if(!firstRoute.shareCommonLink(SecondRoute))
        			{
        				routePair.setFirstRoute(firstRoute);
            			routePair.setSecondRoute(SecondRoute);
            			routePair.setFirstRouteAssociatedDataCenter(firstRoute.getDesDataCenter());
            			routePair.setSecondRouteAssociatedDataCenter(SecondRoute.getDesDataCenter());
            			routepairList.add(routePair);
        				
        			}
        		}
        	}
    	}
    	
    	
    	
    	
    	return routepairList;
    
    }

    
    public static void OuputDataDistribution(Layer layer,String mode, file_out_put result){
    	int textSize = 0;  
    	int avarageSize = 0;
    	Link textlink =null;
    	HashMap<String,Link>LinkMap=layer.getLinklist();
	    Iterator<String>iter_Link=LinkMap.keySet().iterator();
	    while(iter_Link.hasNext()){ 
			  Link link=(Link)(LinkMap.get(iter_Link.next()));	
			 
			  if(link.getNodeA().getIndex()<link.getNodeB().getIndex())
			  { 
				  
				
				  if(link.getServedContent()>textSize)
				  {
					  textSize = link.getServedContent();
					  textlink = link;
				  }
				  avarageSize = avarageSize+link.getServedContent();
				 // result.filewrite("Result/result_"+mode+".txt", "totalMaxTime: "+totalMaxTime);
			  }
		  }
		  System.out.println(textlink.getName()+" "+textSize);
		  System.out.println("Average"+" "+avarageSize/layer.getLink_num());
		  System.out.println("TotalRisk: "+totalContentRisk);
		  result.filewrite("Result/result_"+seed+".csv", textlink.getName()+", "+textSize);
		  result.filewrite("Result/result_"+seed+".csv", "Average"+", "+avarageSize/layer.getLink_num());
		  
		
    }
    /**
     * 
     * @param layer
     * @param con
     * @param AvadataCenterlist
     */
    public static void Strategy_CombinationContent_SingleDestinationNode(Layer layer,Content con,ArrayList<DataCenter> AvadataCenterlist,int z,ArrayList<Content> rapidConetentList){
    
    	Content addCon = null;
    	for(int index = 0;index < rapidConetentList.size(); index++)
    	{
    		Content textCon = rapidConetentList.get(index);
    		if(!textCon.isSafeStatus()&&con!=textCon)
    		{
    			if(con.getAssociatedDataCenter().getAssociatedNode()==textCon.getAssociatedDataCenter().getAssociatedNode())
    			{
    				
    			}
    		}

    	}
    	
    	DataCenter selectDataCenter = null;	
		LinearRoute selectedPath = null;
		double finalDelay = 0;
		double finaldelayTx = 0;
		int index = 0;	
		for(DataCenter dataCenter:AvadataCenterlist)
		{ 
			System.out.println("====="+dataCenter.getAssociatedNode().getName()+"=====");
			Node srcNode = con.getAssociatedDataCenter().getAssociatedNode();//find the srcNode
			Node desNode = dataCenter.getAssociatedNode();
			Nodepair nodepair = layer.findNodepair(srcNode, desNode);
			for(LinearRoute path:nodepair.getLinearlist())
			{
				double textDelay = 0;
				int leastCapacity = 10000000;  
				for(Link link:path.getLinklist())				
				{
					leastCapacity = (int) Math.min(leastCapacity, link.getResidualCapacity());
				}	
				double delayTx  = Delay(path,con.getSize(),leastCapacity);
				textDelay = CalculateDleayForPath(path,con.getSize(),leastCapacity);
				if(index==0)
				{			
					finalDelay = textDelay;
					selectDataCenter = dataCenter;
					selectedPath = path;
					finaldelayTx = delayTx;   
					index++;
				}
				else
				{
					if(textDelay<finalDelay)
					{ 
						 finalDelay = textDelay;
						 selectDataCenter = dataCenter;
						 selectedPath = path;
						 finaldelayTx = delayTx;
						 index++;
					}
				}       
				
			}
			
		}		 
		Task currentTask = new Task();		   		
		 
		currentTask.setContent(con);
		currentTask.setDataCenter(selectDataCenter);
		currentTask.setDelayTx(finaldelayTx);
		currentTask.setTotalTime(finalDelay);		
		currentTask.setRoute(selectedPath);
		deliveryContent(layer,currentTask);	
		//double outputResult = finalDelay/1000000;
		
		System.out.println("selected DataCenter: "+selectDataCenter.getAssociatedNode().getName());
		currentTask.getRoute().OutputRoute_link();	
		System.out.println("develivery Con: "+con.getSize());
		//System.out.println("Time: "+outputResult);
		double outputResult = currentTask.getTotalTime();
		System.out.println("Time: "+outputResult);
		 if(totalMaxTime==0)
			 totalMaxTime=outputResult;
		 else if(totalMaxTime<outputResult)
			 totalMaxTime=outputResult;
    }	
    
    
    /**
	 * Compare the best delay time from Multipath 
	 * @param layer
	 * @param con
	 * @param AvadataCenterlist
	 */
	public static void TestStrategy_MultiPath_SingleDestinationNode(Layer layer,Content con,ArrayList<DataCenter> AvadataCenterlist){
		  
		DataCenter selectDataCenter = null;	
		LinearRoute selectedPath = null;
		double finalDelay = 0;
		double finaldelayTx = 0;
		int index = 0;	
		double finalstartTime = 0;
		double finalendTime = 0;
		boolean finalKeyShare = false;
		double finalLeastCapacity = 0;
		for(DataCenter srcDataCenter:con.getDataCenterList())
		{
			System.out.println("test：  "+srcDataCenter.getAssociatedNode().getName());
			for(DataCenter dataCenter:AvadataCenterlist)
			{ 
				System.out.println("====="+dataCenter.getAssociatedNode().getName()+"=====");
				Node srcNode = srcDataCenter.getAssociatedNode();//find the srcNode
				Node desNode = dataCenter.getAssociatedNode();
				Nodepair nodepair = layer.findNodepair(srcNode, desNode);
				for(LinearRoute path:nodepair.getLinearlist())
				{
					boolean keyShare = false;
					double startTime = 0;
					double endTime = 0;
					double textDelay = 0;
					int leastCapacity = 10000000;  
					double avaStartTime = 0;
					for(Link link:path.getLinklist())
					{
				
						avaStartTime = Math.max(avaStartTime,link.getTimeCapacity().getAvaStartTime());
					}
					startTime = avaStartTime;
					System.out.println("startTime "+startTime);
					
					double avaEndTime = 100000;
					for(Link link:path.getLinklist())
					{
				
						avaEndTime = Math.min(avaEndTime,link.getTimeCapacity().getExistEndTime());
					}
					System.out.println("avaEndTime "+avaEndTime);
					
					for(Link link:path.getLinklist())				
					{
					
						leastCapacity = (int) Math.min(leastCapacity, link.getTimeCapacity().getMinimumResidualCapacity());
					}			
					System.out.println(leastCapacity);
					
					double delayTx = 100000;
					if(leastCapacity>0)
					{
						delayTx  = Delay(path,con.getSize(),leastCapacity);
						
					}
					textDelay = avaStartTime + delayTx;
					endTime = textDelay;
					System.out.println(endTime+" ---"+avaEndTime);
					if(textDelay<avaEndTime)//it is possible to groom the content in the link;
					{
						
						keyShare = true;
						System.out.println("可以赛");
						
					}
					else//use the new time slot to calculate least delay 
					{
						//System.out.println("不可以赛");
						avaStartTime = 0;
						for(Link link:path.getLinklist())
						{
							avaStartTime = Math.max(avaStartTime,link.getLinkdelay());
						}
						leastCapacity = 10000000;  
						for(Link link:path.getLinklist())				
						{
							leastCapacity = (int) Math.min(leastCapacity, link.getResidualCapacity());
						}	
						delayTx  = Delay(path,con.getSize(),leastCapacity);
						textDelay = CalculateDleayForPath(path,con.getSize(),leastCapacity);
						endTime = textDelay;
						
					}
					
					if(index==0)
					{			
						finalDelay = textDelay;
						finalKeyShare = keyShare;
						finalstartTime = startTime;
						finalendTime = endTime;
						finalLeastCapacity = leastCapacity;
						selectDataCenter = dataCenter;
						selectedPath = path;
						finaldelayTx = delayTx;   
						index++;
					}
					else
					{
						if(textDelay<finalDelay)
						{ 
							finalstartTime = startTime;
							finalendTime = endTime;
							finalKeyShare = keyShare;
							 finalDelay = textDelay;
							 finalLeastCapacity = leastCapacity;
							 selectDataCenter = dataCenter;
							 selectedPath = path;
							 finaldelayTx = delayTx;
							 index++;
						}
					}       
					
				}
				
			}		 
		}
		
		Task currentTask = new Task();	
		
		currentTask.setContent(con);
		currentTask.setDataCenter(selectDataCenter);
		currentTask.setDelayTx(finaldelayTx);
		currentTask.setTotalTime(finalDelay);		
		currentTask.setRoute(selectedPath);
		currentTask.setStartTime(finalstartTime);
		currentTask.setEndTime(finalendTime);
		currentTask.setLeastCapacity(finalLeastCapacity);
		System.out.println(finalstartTime+"---"+finalendTime+" "+con.getSize());
		
		if(finalKeyShare)
		{
			System.out.println("success");
			deliveryContentWithTrafficGromming(layer,currentTask);	
		}
		else
		{
			System.out.println("fail");
			deliveryContentWithoutTrafficGromming(layer,currentTask);	
		}
		//double outputResult = finalDelay/1000000;
		
		System.out.println("selected DataCenter: "+selectDataCenter.getAssociatedNode().getName());
		currentTask.getRoute().OutputRoute_link();	
		System.out.println("develivery Con: "+con.getSize());
		//System.out.println("Time: "+outputResult);
		double outputResult = currentTask.getTotalTime();
		System.out.println("Time: "+outputResult);
		 if(totalMaxTime==0)
			 totalMaxTime=outputResult;
		 else if(totalMaxTime<outputResult)
			 totalMaxTime=outputResult;		
	}
	
	
	
	
	public boolean test(LinearRoute path)
	{
		int leastCapacity = 100000000;
		for(Link link:path.getLinklist())				
		{
			leastCapacity = (int) Math.min(leastCapacity, link.getTimeCapacity().getMinimumResidualCapacity());
		}
		int leastDleay = 100000000;
		for(Link link:path.getLinklist())				
		{
			leastDleay = (int) Math.min(leastDleay, link.getTimeCapacity().getTimeDelay());
		}
		
		return true;
	}
	
	/**
	 * Delivery Content
	 * @param layer
	 * @param task
	 */
	public static void deliveryContentWithTrafficGromming(Layer layer,Task task)
	{
		
		for(Link link:task.getRoute().getLinklist())
		{
			if(link.getTimeCapacity().getExistEndTime()<Constant.maxium)
			{
				double startTime = LinkStatusUpdate(link, task);
				
				link.getTimeCapacity().setAvaStartTime(startTime);
				
				Link anotherlink = layer.findlink(link.getNodeB(), link.getNodeA());
			
				link.getTasklist().add(task);
				anotherlink.getTasklist().add(task);
				anotherlink.getTimeCapacity().setAvaStartTime(startTime);
				
				link.setServedContent(link.getServedContent()+task.getContent().getSize());
				anotherlink.setServedContent(anotherlink.getServedContent()+task.getContent().getSize());
			}
			else{
				
				TimeSlot timeslot = new TimeSlot();
			    timeslot.setStartTime(task.getStartTime());
			    timeslot.setEndTime(task.getEndTime());
			    timeslot.setOccupyCapacity(task.getLeastCapacity());
			  
			    if(link.getResidualCapacity()-timeslot.getOccupyCapacity()==0)
			    {
			    	link.getTimeCapacity().setMinimumResidualCapacity((int)link.getResidualCapacity());
			    	link.getTimeCapacity().setAvaStartTime(task.getEndTime());
			    	link.getTimeCapacity().setExistEndTime(Constant.maxium);
			    	
			    	
			    }
			  
			    else if(link.getResidualCapacity()-timeslot.getOccupyCapacity()>0
			    		&&link.getResidualCapacity()-timeslot.getOccupyCapacity()<link.getTimeCapacity().getMinimumResidualCapacity())
			    {
			    	link.getTimeCapacity().setMinimumResidualCapacity((int)(link.getResidualCapacity()-timeslot.getOccupyCapacity()));
			    	link.getTimeCapacity().setExistEndTime(task.getEndTime());
			    }
			    
			    System.out.println(link.getTimeCapacity().getMinimumResidualCapacity());
			    
			    
			   
			    link.getTimeCapacity().getTimeSlot().add(timeslot);
			    
			    link.setLinkdelay(task.getTotalTime());
				Link anotherlink = layer.findlink(link.getNodeB(), link.getNodeA());
				anotherlink.setLinkdelay(task.getTotalTime());
				 if(anotherlink.getResidualCapacity()-timeslot.getOccupyCapacity()==0)
				 {
					 anotherlink.getTimeCapacity().setMinimumResidualCapacity((int)anotherlink.getResidualCapacity());
				 
					 anotherlink.getTimeCapacity().setExistEndTime(Constant.maxium);
				 }
				 
				else if(anotherlink.getResidualCapacity()-task.getContent().getSize()>0&&anotherlink.getResidualCapacity()-task.getContent().getSize()<anotherlink.getTimeCapacity().getMinimumResidualCapacity())
				{
					anotherlink.getTimeCapacity().setMinimumResidualCapacity((int)anotherlink.getResidualCapacity()-task.getContent().getSize());
					anotherlink.getTimeCapacity().setExistEndTime(task.getEndTime());
				
				}
				
				 
				 
				 anotherlink.getTimeCapacity().getTimeSlot().add(timeslot);
				
				link.getTasklist().add(task);
				anotherlink.getTasklist().add(task);
				
				link.setServedContent(link.getServedContent()+task.getContent().getSize());
				anotherlink.setServedContent(anotherlink.getServedContent()+task.getContent().getSize());
			}
			
			
		}
		int emptyStorage = task.getDataCenter().getEmptyStorage()-task.getContent().getSize();
		task.getDataCenter().setEmptyStorage(emptyStorage);
	}
	
	/**
	 * Delivery Content
	 * @param layer
	 * @param task
	 */
	public static void deliveryContentWithoutTrafficGromming(Layer layer,Task task)
	{
		
		for(Link link:task.getRoute().getLinklist())
		{
		   
			
			TimeSlot timeslot = new TimeSlot();
		    timeslot.setStartTime(task.getStartTime());
		    timeslot.setEndTime(task.getEndTime());
		    timeslot.setOccupyCapacity(task.getLeastCapacity());
		  
		    if(link.getResidualCapacity()-timeslot.getOccupyCapacity()==0)
		    {
		    
		    	link.getTimeCapacity().setAvaStartTime(task.getEndTime());
		    	link.getTimeCapacity().setExistEndTime(Constant.maxium);
		    	
		    	
		    }
		  
		    else if(link.getResidualCapacity()-timeslot.getOccupyCapacity()>0
		    		&&link.getResidualCapacity()-timeslot.getOccupyCapacity()<link.getTimeCapacity().getMinimumResidualCapacity())
		    {
		    	link.getTimeCapacity().setMinimumResidualCapacity((int)(link.getResidualCapacity()-timeslot.getOccupyCapacity()));
		    	link.getTimeCapacity().setExistEndTime(task.getEndTime());
		    }
		    
		    System.out.println(link.getTimeCapacity().getMinimumResidualCapacity());
		    
		    
		   
		    link.getTimeCapacity().getTimeSlot().add(timeslot);
		    
		    link.setLinkdelay(task.getTotalTime());
			
		    Link anotherlink = layer.findlink(link.getNodeB(), link.getNodeA());
			anotherlink.setLinkdelay(task.getTotalTime());
			 if(anotherlink.getResidualCapacity()-timeslot.getOccupyCapacity()==0)
			 {
				 anotherlink.getTimeCapacity().setMinimumResidualCapacity((int)anotherlink.getResidualCapacity());
			 
				 anotherlink.getTimeCapacity().setExistEndTime(Constant.maxium);
			 }
			 
			else if(anotherlink.getResidualCapacity()-task.getContent().getSize()>0&&anotherlink.getResidualCapacity()-task.getContent().getSize()<anotherlink.getTimeCapacity().getMinimumResidualCapacity())
			{
				anotherlink.getTimeCapacity().setMinimumResidualCapacity((int)anotherlink.getResidualCapacity()-task.getContent().getSize());
				anotherlink.getTimeCapacity().setExistEndTime(task.getEndTime());
			
			}
			
			 
			 
			 
			anotherlink.getTimeCapacity().getTimeSlot().add(timeslot);
			
			link.getTasklist().add(task);
			anotherlink.getTasklist().add(task);
			
			link.setServedContent(link.getServedContent()+task.getContent().getSize());
			anotherlink.setServedContent(anotherlink.getServedContent()+task.getContent().getSize());
		}
		int emptyStorage = task.getDataCenter().getEmptyStorage()-task.getContent().getSize();
		task.getDataCenter().setEmptyStorage(emptyStorage);
	}
	
	public static double LinkStatusUpdate(Link link, Task task)
	{
		double startTime = 0;
		double endTime = task.getEndTime();
		System.out.println(task.getEndTime());
		for(TimeSlot timeslot:link.getTimeCapacity().getTimeSlot())
		{
			System.out.println(timeslot.getStartTime()+"---"+timeslot.getEndTime());
			if(timeslot.getStartTime()<=endTime)
			{
				startTime = timeslot.getEndTime();
				break;
			}
		}
		System.out.println(link.getName()+" "+startTime);
		return startTime;
		
	}
	public static void updateLinkCost(Layer layer,int contentSize)
	{
		HashMap<String,Link>mapLink=layer.getLinklist();
	    Iterator<String>iterLink=mapLink.keySet().iterator();
	    while(iterLink.hasNext()){ 
	    
	    	Link link = (Link)(mapLink.get(iterLink.next()));
	    	
	    	double cost = 1;//contentSize/link.getResidualCapacity();//+(contentSize/link.getResidualCapacity());
	    	
	    	link.setCost(cost);
	    }
	    	
	}
	/**
	 * 
	 * @param layer
	 * @param con
	 * @param AvadataCenterlist
	 */
	public static void AdaptiveRapidEvacuatuion(Layer layer,Content con,ArrayList<DataCenter> AvadataCenterlist)
	{
		updateLinkCost(layer,con.getSize());
		DataCenter selectDataCenter = null;	
		LinearRoute selectedPath = null;
		double finalDelay = 0;
		double finaldelayTx = 0;
		int index = 0;	
		double finalstartTime = 0;
		double finalendTime = 0;
		boolean finalKeyShare = false;
		double finalLeastCapacity = 0;
	
		for(DataCenter srcDataCenter:con.getDataCenterList())
		{
			System.out.println("test：  "+srcDataCenter.getAssociatedNode().getName());
			for(DataCenter dataCenter:AvadataCenterlist)
			{ 
				System.out.println("====="+dataCenter.getAssociatedNode().getName()+"=====");
				Node srcNode = srcDataCenter.getAssociatedNode();//find the srcNode
				Node desNode = dataCenter.getAssociatedNode();
				Nodepair nodepair = layer.findNodepair(srcNode, desNode);
				
				LinearRoute path = new LinearRoute("",	0, "", Constant.WORKING);
			    Constraint constraint = new Constraint();
			    pathSearch workpathsearch=new pathSearch();
			    workpathsearch.SearchPathWithDijkstras(nodepair.getSrcNode(), nodepair.getDesNode(), layer, path,constraint);
			    path.OutputRoute_link();
			    double textDelay = 0;
				int leastCapacity = 10000000;  
				for(Link link:path.getLinklist())				
				{
					
					leastCapacity = (int) Math.min(leastCapacity, link.getResidualCapacity());
				}	
				
				System.out.println(leastCapacity);
				double delayTx  = Delay(path,con.getSize(),leastCapacity);
				textDelay = CalculateDleayForPath(path,con.getSize(),leastCapacity);
				System.out.println(textDelay+" ---");
				if(index==0)
				{			
					finalDelay = textDelay;
					selectDataCenter = dataCenter;
					selectedPath = path;
					finaldelayTx = delayTx;  
					finalLeastCapacity = leastCapacity;
					index++;
				}
				else
				{
					if(textDelay<finalDelay)
					{ 
						 finalDelay = textDelay;
						 selectDataCenter = dataCenter;
						 selectedPath = path;
						 finaldelayTx = delayTx;
						 finalLeastCapacity = leastCapacity;
						 index++;
					}
				}       
			}
		}
		Task currentTask = new Task();		   		
		 
		currentTask.setContent(con);
		currentTask.setDataCenter(selectDataCenter);
		currentTask.setDelayTx(finaldelayTx);
		currentTask.setTotalTime(finalDelay);		
		currentTask.setRoute(selectedPath);
		currentTask.setLeastCapacity(finalLeastCapacity);
		deliveryContent(layer,currentTask);	
		//double outputResult = finalDelay/1000000;
		
		System.out.println("selected DataCenter: "+selectDataCenter.getAssociatedNode().getName());
		currentTask.getRoute().OutputRoute_link();	
		System.out.println("develivery Con: "+con.getSize());
		//System.out.println("Time: "+outputResult);
		double outputResult = currentTask.getTotalTime();
		System.out.println("Time: "+outputResult);
		 if(totalMaxTime==0)
			 totalMaxTime=outputResult;
		 else if(totalMaxTime<outputResult)
			 totalMaxTime=outputResult;
	}
	

	/**
	 * Divide the content into Fragmentations
	 * @param contentList
	 * @param k
	 * @param r
	 */
	public static void ContentFragmentation(ArrayList<Content> contentList,int k, int r)
	{
		//divide the content into equal size with k and r
		for(Content iCon:contentList)
		{
			int size = iCon.getSize()/k;
			iCon.setBlockSize(size);
			int weight = iCon.getWeight();
			int replicasNum = iCon.getReplicasNum();
			
			for(int i = 0; i < k;i++)
			{
				Content subNewCon = new Content("Con", i, "Con");
				subNewCon.setSize(size);
				subNewCon.setWeight(weight);
				subNewCon.setReplicasNum(replicasNum);
				subNewCon.setDataCenterList(iCon.getDataCenterList());
				iCon.getSubContentList().add(subNewCon);
			}
			for(int j = 0; j < r; j++)
			{
				Content subNewCon = new Content("Con", k+j, "Con");
				subNewCon.setSize(size);
				subNewCon.setWeight(weight);
				subNewCon.setReplicasNum(replicasNum);
				subNewCon.setParityData(true);
				subNewCon.setDataCenterList(iCon.getDataCenterList());
				iCon.getSubContentList().add(subNewCon);
			}
			
		}
	}
	
	/**
	 * 
	 * @param contentList
	 */
	public static void OutputContentFragmentation(ArrayList<Content> contentList)
	{
		int FinalTotalsize = 0;
		//divide the content into equal size with k and r
		int total  = 0;
		for(Content iCon:contentList)
		{
			System.out.println("The "+iCon.getIndex()+" Content: "+iCon.getBlockSize()+" "+iCon.getAddR());
			
			int Totalsize =0;
				
			for(int i = 0; i < iCon.getBlocklist().size();i++)
			{
				System.out.print(i+"  ");
			}
			System.out.println();
			for(int i = 0; i < iCon.getBlocklist().size();i++)
			{
			
				if(iCon.getBlocklist().get(i).isSafe())
					System.out.print(1+"  ");
				else
					System.out.print(0+"  ");
					
			}
			System.out.println();
			
	    //	if(iCon.getAssociatedDataCenter().isDamagedStatus())
			{
				Totalsize = iCon.getBlockSize()*(iCon.getAddR());
				/*
				for(int i = 0; i < iCon.getAssociatedDataCenter().getBlocklist().size();i++)
				{
					if(iCon.getAssociatedDataCenter().getBlocklist().get(i).getOrdinaryContent()==iCon)
					{ 
						int size = iCon.getAssociatedDataCenter().getBlocklist().get(i).getSize();
						Totalsize = Totalsize + size;			
						
					}
				}
				*/
	
			}
		
			if(Totalsize > 0)
			{
				FinalTotalsize = FinalTotalsize + Totalsize;
			}
			total = total + iCon.getSize();
		}
		System.out.println("FinalTotalsize: "+FinalTotalsize);
		System.out.println("Totalsize: "+total);
	}
	
	public static void OutputContentSie(ArrayList<Content> contentList)
	{
		int FinalTotalsize = 0;
		//divide the content into equal size with k and r
		for(Content iCon:contentList)
		{
			
			System.out.println("size: "+iCon.getSize());
				
			FinalTotalsize = FinalTotalsize + iCon.getSize();
			
			
		}
		System.out.println("FinalTotalsize: "+FinalTotalsize);
	}
	
	public static void CF_RDE(Layer layer,Content con,ArrayList<DataCenter> dataCenterlist,ArrayList<DataCenter> AvadataCenterlist)
	{
		int index = con.getAddR();
		while(index>0)
		{	
			System.out.println(" "+index+" ");
			SelectAvaDataCenter_CF(con,dataCenterlist,AvadataCenterlist);
			CF_LDR_RDE(layer,con,AvadataCenterlist);
			index--;
		}
	}
	
	
	public static void CF_LDR_RDE(Layer layer,Content con,ArrayList<DataCenter> AvadataCenterlist){
		  
		Functions_Rapid_DataEvacuation workFunction = new Functions_Rapid_DataEvacuation();
		DataCenter selectSrcDataCenter = null;
		DataCenter selectDataCenter = null;	
		LinearRoute selectedPath = null;
		double finalDelay = 0;
		double finaldelayTx = 0;
		int index = 0;	

		int finalLeastCapcity = 0;
		ArrayList<DataCenter> srcdataCenterlist = new ArrayList<DataCenter>();
		for(Block block:con.getBlocklist())
		{
			if(block.getDataCenter().isDamagedStatus())
			{
				if(!block.isSafe())
				{
					srcdataCenterlist.add(block.getDataCenter());
				}
			}
		}
		
		
		for(DataCenter srcDataCenter:srcdataCenterlist)
		{
			System.out.println("test：  "+srcDataCenter.getAssociatedNode().getName());
			for(DataCenter dataCenter:AvadataCenterlist)
			{ 
				//System.out.println("====="+dataCenter.getAssociatedNode().getName()+"=====");
				Node srcNode = srcDataCenter.getAssociatedNode();//find the srcNode
				Node desNode = dataCenter.getAssociatedNode();
				Nodepair nodepair = layer.findNodepair(srcNode, desNode);
				
				for(LinearRoute path:nodepair.getLinearlist())
				{
					//path.OutputRoute_link();
					double textDelay = 0;
					
					double risk = path.getRisk();
					
					int leastCapacity = 10000000;  
					for(Link link:path.getLinklist())				
					{
						
						leastCapacity = (int) Math.min(leastCapacity, link.getResidualCapacity());
					}	
					//System.out.println(leastCapacity);
					double delayTx  = Delay(path,con.getSize(),leastCapacity);
					textDelay = CalculateDleayForPath(path,con.getBlockSize(),leastCapacity);
			
					textDelay = (double)(Math.round(textDelay*1000)/1000.0);
					if(index==0)
					{			
						finalDelay = textDelay;
						selectSrcDataCenter = srcDataCenter;
						selectDataCenter = dataCenter;
						selectedPath = path;
						finaldelayTx = delayTx;  
						finalLeastCapcity = leastCapacity;
						index++;
					}
					else
					{
						if(textDelay<finalDelay)
						{ 
							 finalDelay = textDelay;
							 selectSrcDataCenter = srcDataCenter;
							 selectDataCenter = dataCenter;
							 selectedPath = path;
							 finaldelayTx = delayTx;
							 finalLeastCapcity = leastCapacity;
							 index++;
						}
					}       
				}
			}		 
		}
		Block Finalblock = null;
		for(Block block:con.getBlocklist())
		{
			
			if(block.getDataCenter().isDamagedStatus())
			{
				if(!block.isSafe())
				{
					
					if(block.getDataCenter()==selectSrcDataCenter)
					{
						block.setSafe(true);
						Finalblock = block;
						break;
					}
				}
			}
		}
		
		
		Task currentTask = new Task();		   		
		selectedPath.OutputRoute_link();
		currentTask.setContent(con);
		currentTask.setDataCenter(selectDataCenter);
		currentTask.setDelayTx(finaldelayTx);
		currentTask.setTotalTime(finalDelay);		
		currentTask.setRoute(selectedPath);
		currentTask.setLeastCapacity(finalLeastCapcity);
		currentTask.setFinishBlock(Finalblock);
		
		
		DeliveryedData = DeliveryedData + Finalblock.getSize();
		
		
		
		deliveryContent(layer,currentTask);	
		//double outputResult = finalDelay/1000000;
		
		System.out.println("selected DataCenter: "+selectDataCenter.getAssociatedNode().getName());
		currentTask.getRoute().OutputRoute_link();	
		System.out.println("develivery Con: "+con.getBlockSize());
		//System.out.println("Time: "+outputResult);
		double outputResult = currentTask.getTotalTime();
		System.out.println("Time: "+outputResult);
		 if(totalMaxTime==0)
			 totalMaxTime=outputResult;
		 else if(totalMaxTime<outputResult)
			 totalMaxTime=outputResult;
		
		
			
	}
}
