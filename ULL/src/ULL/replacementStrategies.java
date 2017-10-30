package ULL;

import general.Constant;
import general.Modulation;
import general.Range;
import general.SlotWindow;
import general.Time;
import general.TimeLine;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;


import slotWorkPackage.FreSlotFunctions;
import subgraph.LinearRoute;



import Network.*;

public class replacementStrategies {

	//*********************************************************//
	public static int  MaxSlotNum = 320;          //最大slot个数
	public static int  arrive = 2;                //arrive参数值
	public static double  departure =8  ;        //departure参数值
	public static int  EventNumber = 1000;         //eventNumber 
	public static double blockDemand = 0;
	public static double allDmenad = 0;
	public static int  block_number = 0;          //阻塞次数
	public static int  k_index = 0;               //index
	public static int  MaxNum = 1000;
	public static int  MinNum = 500;
	
	
	
	
	
	/**
	 * based on the physical length of each link
	 * @param Replace_percentage
	 * @param layer
	 */
	public ArrayList<Link> Physical_Length_based(double Replace_percentage, Layer layer){
		
		
		int replaceNum = (int) Math.ceil(layer.getLink_num()*Replace_percentage/2);
		System.out.println(layer.getLink_num()/2+" "+Replace_percentage+" "+replaceNum);
		
		ArrayList<Link> linklist = new ArrayList<Link>();
		
		for(int i = 0; i<replaceNum;i++)
		{
			Link findLink = null;
			
			HashMap<String,Link>LinkMap=layer.getLinklist();
	        Iterator<String>iter_Link=LinkMap.keySet().iterator();
	        while(iter_Link.hasNext()){
	       	    Link currentLink=(Link)(LinkMap.get(iter_Link.next()));
	       	
	       	    if(currentLink.getNodeA().getIndex()<currentLink.getNodeB().getIndex())
	       	    {
	       	    	if(!linklist.contains(currentLink))
	       	    	{
	       	    		if(findLink==null)
		       	    		findLink = currentLink;
		       	    	else
		       	    	{
		       	    		if(findLink.getLength()<currentLink.getLength())
		       	    			findLink = currentLink;
		       	    	}
	       	    		
	       	    	}
	       	    
	       	   
	       	    }
			}
	        if(findLink!=null)
	        	linklist.add(findLink);
		}
		for(Link link:linklist)
		{
			System.out.println(link.getName()+" "+link.getLength());
		}
		
		return linklist;
		
	}


	/**
	 * based on the traversed time of each link
	 * @param Replace_percentage
	 * @param layer
	 * @return
	 */
	public ArrayList<Link> Shortest_Route_Traverse_based(double Replace_percentage, Layer layer){
		
		int replaceNum = (int) Math.ceil(layer.getLink_num()*Replace_percentage/2);
		System.out.println(layer.getLink_num()/2+" "+Replace_percentage+" "+replaceNum);
		ArrayList<Link> linklist = new ArrayList<Link>();
		
		layer.setLinkTraversedNum();
		
		for(int i = 0; i<replaceNum;i++)
		{
			Link findLink = null;
			
			HashMap<String,Link>LinkMap=layer.getLinklist();
	        Iterator<String>iter_Link=LinkMap.keySet().iterator();
	        while(iter_Link.hasNext()){
	       	    Link currentLink=(Link)(LinkMap.get(iter_Link.next()));
	       	
	       	    if(currentLink.getNodeA().getIndex()<currentLink.getNodeB().getIndex())
	       	    {
	       	    	if(!linklist.contains(currentLink))
	       	    	{
	       	    		if(findLink==null)
		       	    		findLink = currentLink;
		       	    	else
		       	    	{
		       	    		if(findLink.getTraversedNum()<currentLink.getTraversedNum())
		       	    			findLink = currentLink;
		       	    	}
	       	    		
	       	    	}
	       	    
	       	   
	       	    }
			}
	        if(findLink!=null)
	        	linklist.add(findLink);
		}
		System.out.println("==============================");
		for(Link link:linklist)
		{
			System.out.println(link.getName()+" "+link.getTraversedNum());
		}
		
		return linklist;
	}

	
	/**
	 * based on the blocking probability of each link
	 * @param Replace_percentage
	 * @param layer
	 * @return
	 */
	public ArrayList<Link> OpticalBlocking_based(double Replace_percentage, Layer layer){
		
		
		ArrayList<Link> linklist = new ArrayList<Link>();
		ArrayList<Modulation> modlist = new ArrayList<Modulation>();
	
		InputModlist(modlist);	
		OB(layer,MaxSlotNum,modlist);
		
		return linklist;
	}
	
	/**
	 * 
	 * @param modlist
	 */
	public void InputModlist(ArrayList<Modulation> modlist ){
		
		Modulation mod1= new Modulation();
		mod1.setName("16-QAM");
		mod1.setModRate(90);
		mod1.setOSNR_Limit(22.4);
		modlist.add(mod1);
		
		
		Modulation mod2= new Modulation();
		mod2.setName("8-QAM");
		mod2.setModRate(75);
		mod2.setOSNR_Limit(20.37);
		modlist.add(mod2);
		
		Modulation mod3= new Modulation();
		mod3.setName("QPSK");
		mod3.setModRate(50);
		mod3.setOSNR_Limit(17.01);
		modlist.add(mod3);
		
		Modulation mod4= new Modulation();
		mod4.setName("BPSK");
		mod4.setModRate(25);
		mod4.setOSNR_Limit(14.03);
		modlist.add(mod4);
	}
	
	public void OB(Layer layer,int MaxSlotNum,ArrayList<Modulation> modlist)
	{ 
		
		
		layer.InitSlotForLayer(MaxSlotNum);
		Random rand=new Random(1);
		    
	
		TimeLine timeline;
    	Time array[]=new Time[10000];
		int i=0;
		timeline=new TimeLine("timeline",1,"",layer.getNodepairlist().size());
      
		
		//********************时间轴初始化**************************************//
		//为每个节点对产生时间
		HashMap<String,Nodepair>map1=layer.getNodepairlist();
		Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			array[i]=new Time(nodepair.getName(),nextArravie(rand)*arrive,1,null);
			array[i].setAssociatednodepair(nodepair);
			double trafficload = rand.nextInt(MaxNum)%(MaxNum-MinNum+1)+MinNum;
			nodepair.setTrafficLoad(trafficload);
			i++;
		}
		// 时间轴上安大小排列 
		Range range=new Range();
		array=range.rangeTimeFrom_min_to_max(array,timeline.array_num(array));
		timeline.setTimearray(array);
		timeline.OutputTime(array);
		//********************时间轴初始化**************************************//
	
		layer.clear();
    
		
	
		while(k_index<EventNumber){	
    			
			Nodepair nodepair=array[0].getAssociatednodepair();


			if(k_index%1000==1)System.out.print(" "+k_index+" ");

			System.out.println("============The name of the nodepair "+array[0].getName()+"================="+k_index+"=========");

			if(array[0].getKeytime()==Constant.arrivaltime)
			{
				arrivalEvent(layer,modlist,nodepair,rand, timeline, array,range);
				k_index++;//event+1
				array=range.rangeTimeFrom_min_to_max(array,timeline.array_num(array));
			
			}
			else
			{
				System.out.println("departure event");
				departureEvent(layer,nodepair,timeline,array,range);
				//新的时间序列
				int size=timeline.array_num(array);  
				array=range.rangeTimeFrom_min_to_max(array,size);
			}
			
		}	
    		
		System.out.println(block_number);
		
	}

	public static double nextArravie(Random rand)
	{
		return (-Math.log(rand.nextDouble()));
	}
	
	public static void arrivalEvent(Layer layer,ArrayList<Modulation> modlist,Nodepair nodepair,Random rand,TimeLine timeline,Time array[],Range range){
		
		 LightPath lightpath = new LightPath(nodepair.getName()+"lightpath", nodepair.getAssociatedLightPathList().size(), "   ", nodepair);			 
		 
		 
		int RequiredSlotNum=0;
		double trafficload = rand.nextInt(MaxNum)%(MaxNum-MinNum+1)+MinNum;//分配带宽.R:bandwidth 10-400

		System.out.println("!!!!new arrvial event!!!!!");

		boolean blockStatus = true;
		boolean linkOSNRblockStatus = false;
		for(Modulation mod:modlist)
		{
			
			double Modrate=mod.getModRate();
		

			if(nodepair.getTrafficLoad()%Modrate==0)
				RequiredSlotNum=(int)(nodepair.getTrafficLoad()/Modrate);//the number of FS. F
				else
					RequiredSlotNum=(int)(nodepair.getTrafficLoad()/Modrate)+1;
            System.out.println(nodepair.getTrafficLoad()+" "+"采用 "+mod.getName()+" "+"RequiredSlotNum: "+RequiredSlotNum);
			
            //判断是否该调制格式可用
			boolean successStatus=CheckSlotWindowPlane(layer,nodepair,lightpath,RequiredSlotNum,mod);
			System.out.println(successStatus);
		
			if(successStatus)
		    {
		    	blockStatus = false;
		    	break;
		    }   
		}
		if(blockStatus)
		{
		    System.out.println("无法分配资源，业务阻塞");
  		
			block_number++;//number+1;       
  		  
			blockDemand = blockDemand + nodepair.getTrafficLoad();
            //arrival time 0-2.5 随机产生next event;
            double next_arrivaltime=nextArravie(rand)*arrive;
            Time next_arrival=new Time(array[0].getName(),next_arrivaltime,1,null);
            next_arrival.setAssociatednodepair(nodepair);
           
            
            timeline.remove_arrivalTime(array);
            next_arrival.setKeytime(Constant.arrivaltime);
            array[timeline.array_num(array)]=next_arrival;   
			//blocked
			//在对短路由上block数加1
            
            
            //判断是否是因为OSNR限制导致的，在link增加数据
            if(lightpath.isOSNRblockStatus())
            {
            	 OSNRblockNumFuction(nodepair);
            }
		}
		else
		{
			System.out.println("分配资源，业务执行");
    		 
			
			//分配波长   
			double next_arrivaltime=nextArravie(rand)*arrive;//arrival time 0-2.5 随机            
			//  System.out.println("The next arrivaltime:"+next_arrivaltime);
			double releasetime=nextArravie(rand)*departure;//releasetime 0-5随机

			
			
			//在时间轴上加上next arrivaltime ,release time;
			Time next_arrival=new Time(array[0].getName(),next_arrivaltime,1, null);
			Time release=new Time(array[0].getName(),releasetime,0,null);

			next_arrival.setAssociatednodepair(nodepair);
			release.setAssociatednodepair(nodepair);
			
			
			next_arrival.setKeytime(Constant.arrivaltime);
			release.setKeytime(Constant.releasetime);

			int slotIndex = lightpath.getAssociatedslotwindow().getFirstSlotNum();
			
		    release.setLightpath(lightpath);
			release.setOccpyfirstSlot(slotIndex);
			release.setRequiredSlotNum(RequiredSlotNum);
		
			release.setOccpyendSlot(slotIndex+RequiredSlotNum-1);
			release.setCapacity(nodepair.getTrafficLoad());
			timeline.remove_arrivalTime(array);
			array[timeline.array_num(array)]=next_arrival;
			array[timeline.array_num(array)]=release;
		}
	}
	
	
	public static boolean CheckSlotWindowPlane(Layer layer, Nodepair nodepair,LightPath lightpath,int RequiredSlotNum,Modulation mod)
	{
		
		FreSlotFunctions function = new FreSlotFunctions();
		ArrayList<SlotWindow> SlotWindowlist=function.GenerateSlotWindow(layer,RequiredSlotNum,MaxSlotNum,mod);//
		 
		boolean Findkey=function.AvailableSlotWindow(layer, nodepair,lightpath,SlotWindowlist,mod); 
		return Findkey;
	}
	
	
	/**
	 * 释放slot
	 * @param layer
	 * @param nodepair
	 * @param timeline
	 * @param array
	 * @param range
	 */
	public static void departureEvent(Layer layer, Nodepair nodepair,TimeLine timeline,Time array[],Range range)
	{
		FreSlotFunctions function = new FreSlotFunctions();
		
		
		//拆除光通道
		function.releaseSlot(array[0].getLightpath(),array[0].getOccpyfirstSlot(),array[0].getRequiredSlotNum());
	
		// System.out.println("释放波长");
		timeline.remove_releaseTime(array,timeline);    
		
	}
	
	public static void OSNRblockNumFuction(Nodepair nodepair)
	{
		for(Link link:nodepair.getLinearlist().get(0).getLinklist()){
			int OSNRblockNum=link.getOSNRblockNum()+1;
			link.setOSNRblockNum(OSNRblockNum);
		}
	}
	
	
	/**
	 * set the repair Time
	 * @param linklist
	 */
	public static void SetRepairTime(ArrayList<Link> linklist){
		for(Link link:linklist)
		{
			//替换光纤所需的时间
			if(link.getLength()<=100)
				link.setRepairTime(1);
			else if(link.getLength()<=200)
				link.setRepairTime(2);
			else if(link.getLength()<=300)
				link.setRepairTime(3);
			else if(link.getLength()<=400)
				link.setRepairTime(4);
			else if(link.getLength()<=500)
				link.setRepairTime(6);
			else
				link.setRepairTime(7);
		}
	}
	
	
	/**
	 * 
	 * @param ULL_Fiber_linklist
	 */
	public static void ReplaceProcess(Layer layer,ArrayList<Link> ULL_Fiber_linklist){
		int Km_Per_Day = Constant.ULL_Fiber_per_Day;
		
		for(Link link:ULL_Fiber_linklist)
		{
			//finish the link
			int workTime = (int) Math.ceil(link.getLength()/Km_Per_Day);
			
			//workTime
			
		
			
		}
	}
		
	public static void ReplaceULL(Layer layer,Link link)
	{
		HashMap<String,Nodepair>nodepairMap=layer.getNodepairlist();
        Iterator<String>iter_nodepair=nodepairMap.keySet().iterator();
        while(iter_nodepair.hasNext()){
       	    Nodepair nodepair=(Nodepair)(nodepairMap.get(iter_nodepair.next()));
       	    
       	    LinearRoute route = nodepair.getUsedRoute();
       	    //如果替换的光纤被用到，重新选择调制格式
       	    if(route.Equal_link(link)==1)
       	    {
       	    	nodepair.get
       	    }
        }
	}
	
}
