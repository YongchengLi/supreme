package functionAlorithms;

import general.Constant;
import general.Constraint;
import groupwork.pathSearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import subgraph.LinearRoute;

import Network.DemandUnit;
import Network.Layer;
import Network.LightPath;
import Network.Link;
import Network.Node;
import Network.Nodepair;


public class Functions_Quasi_CWDM {

	/**
	 * Generate IPLayer
	 * @param physicalLayer //physical
	 * @param IpLayer       //IP
	 */
    public void Generate_IpLayer(Layer physicalLayer,Layer IpLayer)
    {
    	//add the node and nodepair list to the IP layer
    	IpLayer.setNodelist(physicalLayer.getNodelist());
    	IpLayer.setNodepairlist(physicalLayer.getNodepairlist());
    	
    	
    	//add the vitrual link to the layer
    	int index = 0;
    	HashMap<String,Nodepair>NodepairMap=physicalLayer.getNodepairlist();
        Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();
		while(iter_Nodepair.hasNext()){ 
			Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));	
			
		    Node nodeA = nodepair.getSrcNode();
			Node nodeB = nodepair.getDesNode();
			
			String name1=nodeA.getName();
			String name2=nodeB.getName();
			
			Link name1_link=new Link(name1+"-"+name2,index,"",IpLayer,nodepair.getSrcNode(), nodepair.getDesNode(), 1, 1);
			IpLayer.addlink(name1_link);
			Link name2_link=new Link(name2+"-"+name1,++index,"",IpLayer,nodepair.getDesNode(),nodepair.getSrcNode(),1, 1);
			IpLayer.addlink(name2_link);
			index++;
			//update the neighbor node list
			nodeA.addVirtualNeiNode(nodeB);
			nodeB.addVirtualNeiNode(nodeA);
		}	
    	
    }
	
    /**
	 * Generate random traffic load between node pair
	 * @param layer
	 * @param rand
	 * @param max
	 * @param min
	 */
	public void Generate_Random_TrafficLoad_BetweenNodepair_Based_Distance(Layer layer, Random rand,int max,int min,ArrayList<DemandUnit> demandlist){
		
		
		double sum=0;
		
		HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
		Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();
		while(iter_Nodepair.hasNext()){ 			
			  
			Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));	
			double trafficload = 0;

			trafficload = rand.nextInt(max)%(max-min+1)+min;				
			LinearRoute route = nodepair.getLinearlist().get(0);
			
		   if(route.getlength()>Constant.QAM_DISTANCE_LIMIT
		    		&&route.getlength()<=Constant.QPSK_DISTANCE_LIMIT)
			   trafficload = (int)trafficload/2;
		    else if(route.getlength()>Constant.QPSK_DISTANCE_LIMIT
		    		&&route.getlength()<=Constant.BPSK_DISTANCE_LIMIT)
		    	trafficload = (int)trafficload/3;
		 
		 
			nodepair.setTrafficLoad(trafficload);	
			sum = sum + trafficload;
			double CostOfRegen_BPSK = NumberOfRegen(nodepair,Constant.BPSK_DISTANCE_LIMIT)*Constant.BPSK_COST_OF_REGEN;     
		    double CostOfIprouter_BPSK = Constant.BPSK_COST_OF_ROUTER_PORT;
			nodepair.setCostOfRegenWithBPSK(CostOfRegen_BPSK);
			nodepair.setCostOfIpRouterWithBPSK(CostOfIprouter_BPSK);
			      
			      
			double CostOfRegen_QPSK = NumberOfRegen(nodepair,Constant.QPSK_DISTANCE_LIMIT)*Constant.QPSK_COST_OF_REGEN;     
	        double CostOfIprouter_QPSK = Constant.QPSK_COST_OF_ROUTER_PORT;
		    nodepair.setCostOfRegenWithQPSK(CostOfRegen_QPSK);
		    nodepair.setCostOfIpRouterWithQPSK(CostOfIprouter_QPSK);
			      
		    double CostOfRegen_QAM = NumberOfRegen(nodepair,Constant.QAM_DISTANCE_LIMIT)*Constant.QAM_COST_OF_REGEN;     
		    double CostOfIprouter_QAM = Constant.QAM_COST_OF_ROUTER_PORT;
		    nodepair.setCostOfRegenWithQAM(CostOfRegen_QAM);
		    nodepair.setCostOfIpRouterWithQAM(CostOfIprouter_QAM);  
		    
		    DemandUnit CurrentDemand = new DemandUnit(nodepair.getName(), nodepair.getIndex(), "");
		    CurrentDemand.setNodepair(nodepair);
		    CurrentDemand.setTrafficRequest(trafficload);
		    demandlist.add(CurrentDemand);
		    System.out.println(nodepair.getName()+":  "+trafficload+"  "+route.getlength()+" "+nodepair.getLinearlist().get(1).getlength());
		}	
		System.out.println(sum);
	}
	
	
	
	
	
	/**
	 * Calculate the Number of regen for each lightapth between a certain node pair
	 * @param nodepair
	 * @param distanceLimit
	 * @return
	 */
    public static int NumberOfRegen(Nodepair nodepair,double distanceLimit)
	{
    	double LightPath_Distance = 0;
		int NumofRegen = 0;
		int size = 0;
		for(Link currentLink:nodepair.getLinearlist().get(0).getLinklist())
		{
			LightPath_Distance = LightPath_Distance+currentLink.getLength();
		    if(LightPath_Distance>distanceLimit)
		    {
		    	NumofRegen++;
		    	LightPath_Distance = currentLink.getLength();
		    }
		    else if(LightPath_Distance==distanceLimit&&size!=nodepair.getLinearlist().get(0).getLinklist().size())
		    {
		    	LightPath_Distance = 0;
		    }
		    size++;
		}
		return NumofRegen;
	}
    
    /**
     * Sort the node pair list from maximum to minimum distance
     * @param layer
     */
	public void MaxToMinDistance(Layer layer,ArrayList<String> FromHighToLowList,ArrayList<String> FromLowToHighList)
	{
		
		ArrayList<String> nodepairlist=new ArrayList<String>(1000);
		for(int i = 0; i < layer.getNodepairlist().size(); i++)
    	{
    		Nodepair Maxnodepair = null; 	
	    	HashMap<String,Nodepair>map=layer.getNodepairlist();
	        Iterator<String>iter=map.keySet().iterator();
			while(iter.hasNext())
			{ 
				Nodepair nodepair=(Nodepair)(map.get(iter.next()));
				if(!nodepair.isInNodeList(nodepairlist))
				{
					if(Maxnodepair!=null)
					{
						double NumberOfRegenForMaxnodepair = NumberOfRegen(Maxnodepair,Constant.QPSK_DISTANCE_LIMIT);
						double NumberOfRegenFornodepair = NumberOfRegen(nodepair,Constant.QAM_DISTANCE_LIMIT);
						if(NumberOfRegenForMaxnodepair<NumberOfRegenFornodepair)
							Maxnodepair = nodepair;
					}
					else
					{
						Maxnodepair = nodepair;
					}
				}	
			}
			nodepairlist.add(Maxnodepair.getName());
    	}
		int Max = (int)(Math.ceil(0.05*nodepairlist.size()));
		for(int i = 0; i < Max; i++)
		{
			System.out.print("aa: "+nodepairlist.get(i));
			FromHighToLowList.add(nodepairlist.get(i));
		}
		for(int i = nodepairlist.size()-1; i >= nodepairlist.size()-Max; i--)
		{
			System.out.print("bb: "+nodepairlist.get(i));
			FromLowToHighList.add(nodepairlist.get(i));
		}
		
	}

	/**
	 * Initialize the wavelength for each link on physical layer
	 * @param Physical_layer
	 * @param wavelengthNum
	 */
	public void InitWavelengthForLayer(Layer Physical_layer, int wavelengthNum)
	{
		HashMap<String,Link>map=Physical_layer.getLinklist();
    	Iterator<String>iter=map.keySet().iterator();
    	while(iter.hasNext()){
    		Link link=(Link)(map.get(iter.next()));
    		link.add_W(wavelengthNum);
    		link.Init_W();
    	}
    	Physical_layer.setWavelengthNum(wavelengthNum);
	}


   
	/**
	 * Assign the wavelength for the demandlist
	 * @param Physical_layer
	 * @param rand
	 * @param ShuffleNumber
	 * @param DemandList
	 */
	public void AssignWavelengthForDemandList(Layer Physical_layer, Random rand, int ShuffleNumber, ArrayList<DemandUnit> demandlist,int serveType)
	{
		
		TestFunctions test = new TestFunctions();
		int currentShuffleIndex = 0;
		
		while(currentShuffleIndex<ShuffleNumber)
		{
			System.out.println("==================================="+currentShuffleIndex+"=======================");
			
			Collections.shuffle(demandlist,rand);//shuffle the demandlist
			Physical_layer.clear();
		
			// serve the demand
			for(DemandUnit demand:demandlist)
			{
				ServeDemand(Physical_layer,demand,serveType);
			}
			
			//check
		
			currentShuffleIndex++;
			String result =test.OutputDemandInformation(demandlist);
			System.out.println(result);
			clearAllDemand(demandlist);
		}   
		
	}
	public void clearAllDemand(ArrayList<DemandUnit> demandlist)
	{
		for(DemandUnit demand:demandlist)
		{	
			demand.setWorkRoute(null);
			demand.setWork_lightpath(null);
			demand.setWorkPathStatus(false);
			
			demand.setProtectRoute(null);
			demand.setProtect_lightpath(null);
			demand.setProtectPathStatus(false);
		}
	}
	
	
	
	
	/**
	 * 
	 * @param layer
	 * @param demand
	 * @param servedType
	 */
	public void ServeDemand(Layer layer, DemandUnit demand, int servedType){
		
		
		switch(servedType){
		case 1:
			ServeDemandTypeOne(layer,demand);
			break;
		case 2:
			ServeDemandTypeTwo(layer,demand);
			break;
		case 3:
			ServeDemandTypeThree(layer,demand);
			break;
		}
	}
	
	
	
	/**
	 * serve the demand with type one in which the moudlation format of work and protection route must be the same
	 * @param demand
	 */
	public void ServeDemandTypeOne(Layer layer, DemandUnit demand)
	{
		Nodepair nodepair = demand.getNodepair();//find the associated nodepair
	
		
		//get the first path as workpath
		LinearRoute workRoute = nodepair.getLinearlist().get(0);
		LinearRoute protectRoute = nodepair.getLinearlist().get(1);
		
		String modulatinFormat = AvaModulationFormatforAllRoutes(workRoute,protectRoute);
		
		
		LightPath work_lightpath = new LightPath("workpath", 0, " ", nodepair);
		
		
		if(AssginWavelengthForRoute(layer,workRoute,work_lightpath))//Successfully 
		{
			work_lightpath.setFormatModule_Name(modulatinFormat);
			SetLightPathCapacity(work_lightpath, modulatinFormat);
			
			demand.setWorkRoute(workRoute);
			demand.setWork_lightpath(work_lightpath);
			demand.setWorkPathStatus(true);
			
			LightPath protect_lightpath = new LightPath("protectPath", 0, " ", nodepair);
			if(AssginWavelengthForRoute(layer,protectRoute,protect_lightpath))//Successfully 
			{
				protect_lightpath.setFormatModule_Name(modulatinFormat);
				SetLightPathCapacity(protect_lightpath,modulatinFormat);
				
				demand.setProtectRoute(protectRoute);
				demand.setProtect_lightpath(protect_lightpath);
				demand.setProtectPathStatus(true);
			}
			else{
				
				int WorkassginWaveIndex = work_lightpath.getAssociatedWavelength();
				for(int j = 0;j<workRoute.getLinklist().size();j++)
	    		{
	    			//current link
					workRoute.getLinklist().get(j).getWC().get(WorkassginWaveIndex).setStatus(1);
	    			//sym link
	    			layer.findSymLink(workRoute.getLinklist().get(j)).getWC().get(WorkassginWaveIndex).setStatus(1);
	    			
	    			
	    		}
				work_lightpath.setAssociatedWavelength(-1);
				
				demand.setWorkRoute(null);
				demand.setWork_lightpath(null);
				demand.setWorkPathStatus(false);
			}
		}
		
		
	}
	
	
	/**
	 * serve the demand with adaptive modulation format of each lighptah 
	 * @param demand
	 */
	public void ServeDemandTypeTwo(Layer layer, DemandUnit demand)
	{
		Nodepair nodepair = demand.getNodepair();//find the associated nodepair
		System.out.println(nodepair.getName());
		
		//get the first path as workpath
		LinearRoute workRoute = nodepair.getLinearlist().get(0);
		LinearRoute protectRoute = nodepair.getLinearlist().get(1);
		

		
		
		LightPath work_lightpath = new LightPath("workpath", 0, " ", nodepair);
		
		
		if(AssginWavelengthForRoute(layer,workRoute,work_lightpath))//Successfully 
		{
			String workModulatinFormat = AvaModulationFormatforRoute(workRoute);
			work_lightpath.setFormatModule_Name(workModulatinFormat);
			SetLightPathCapacity(work_lightpath,workModulatinFormat);
			demand.setWorkRoute(workRoute);
			demand.setWork_lightpath(work_lightpath);
			demand.setWorkPathStatus(true);
			
			LightPath protect_lightpath = new LightPath("protectPath", 0, " ", nodepair);
			if(AssginWavelengthForRoute(layer,protectRoute,protect_lightpath))//Successfully 
			{
				String protectModulatinFormat = AvaModulationFormatforRoute(protectRoute);
				protect_lightpath.setFormatModule_Name(protectModulatinFormat);
				SetLightPathCapacity(protect_lightpath,protectModulatinFormat);
				
				demand.setProtectRoute(protectRoute);
				demand.setProtect_lightpath(protect_lightpath);
				demand.setProtectPathStatus(true);
			}
			else
			{
				int WorkassginWaveIndex = work_lightpath.getAssociatedWavelength();
				for(int j = 0;j<workRoute.getLinklist().size();j++)
	    		{
	    			//current link
					workRoute.getLinklist().get(j).getWC().get(WorkassginWaveIndex).setStatus(1);
	    			//sym link
	    			layer.findSymLink(workRoute.getLinklist().get(j)).getWC().get(WorkassginWaveIndex).setStatus(1);
	    			
	    			
	    		}
				work_lightpath.setAssociatedWavelength(-1);
				
				demand.setWorkRoute(null);
				demand.setWork_lightpath(null);
				demand.setWorkPathStatus(false);
			}
		}
		
		
	}
	/**
	 * serve the demand with adaptive modulation format of each lighptah 
	 * @param demand
	 */
	public void TestServeDemandTypeThree(Layer layer, DemandUnit demand)
	{
		Nodepair nodepair = demand.getNodepair();//find the associated nodepair
		System.out.println(nodepair.getName());
		
		//get the first path as workpath
		LinearRoute workRoute = nodepair.getLinearlist().get(0);
		LinearRoute protectRoute = nodepair.getLinearlist().get(1);
		

		
		
		LightPath work_lightpath = new LightPath("workpath", 0, " ", nodepair);
		
		
		if(AssginWavelengthForRoute(layer,workRoute,work_lightpath))//Successfully 
		{
			String workModulatinFormat = AvaModulationFormatforRoute(workRoute);
			work_lightpath.setFormatModule_Name(workModulatinFormat);
			SetLightPathCapacity(work_lightpath,workModulatinFormat);
			demand.setWorkRoute(workRoute);
			demand.setWork_lightpath(work_lightpath);
			demand.setWorkPathStatus(true);
			
			LightPath protect_lightpath = new LightPath("protectPath", 0, " ", nodepair);
			if(AssginWavelengthForRoute(layer,protectRoute,protect_lightpath))//Successfully 
			{
				String protectModulatinFormat = AvaModulationFormatforRoute(protectRoute);
				protect_lightpath.setFormatModule_Name(protectModulatinFormat);
				SetLightPathCapacity(protect_lightpath,protectModulatinFormat);
				
				demand.setProtectRoute(protectRoute);
				demand.setProtect_lightpath(protect_lightpath);
				demand.setProtectPathStatus(true);
			}
		}
		
		
	}
	
	/**
	 * serve the demand with adaptive modulation format and multiple protectRoute of each lighptah 
	 * @param demand
	 */
	public void ServeDemandTypeThree(Layer layer, DemandUnit demand)
	{
		Nodepair nodepair = demand.getNodepair();//find the associated nodepair
		System.out.println(nodepair.getName());
		
		//get the first path as workpath
		LinearRoute workRoute = nodepair.getLinearlist().get(0);
		LinearRoute protectRoute = nodepair.getLinearlist().get(1);
		
		String workModulationFormat = AvaModulationFormatforRoute(workRoute);
		String firstProtectModulatinFormat = AvaModulationFormatforRoute(protectRoute);
        
		double workLightPathCapacity = GetLightPathCapacity(workModulationFormat);
		double firstProtectLightPathCapacity =  GetLightPathCapacity(firstProtectModulatinFormat);
		
		boolean isMultiProtectRoute = false;
		if(workLightPathCapacity>firstProtectLightPathCapacity)//need more than one protect route
			isMultiProtectRoute = true;
			
			
		
		LightPath work_lightpath = new LightPath("workpath", 0, " ", nodepair);
		
		
		if(AssginWavelengthForRoute(layer,workRoute,work_lightpath))//Successfully 
		{
			
			work_lightpath.setFormatModule_Name(workModulationFormat);
			
			SetLightPathCapacity(work_lightpath,workModulationFormat);
			
			demand.setWorkRoute(workRoute);
			demand.setWork_lightpath(work_lightpath);
			demand.setWorkPathStatus(true);
			
			LightPath protect_lightpath = new LightPath("protectPath", 0, " ", nodepair);
			if(AssginWavelengthForRoute(layer,protectRoute,protect_lightpath))//Successfully 
			{
				
			
				SetLightPathCapacity(protect_lightpath,firstProtectModulatinFormat);
				
				demand.setProtectRoute(protectRoute);
				demand.setProtect_lightpath(protect_lightpath);
				demand.setProtectPathStatus(true);
				
				double remainderCapacity = workLightPathCapacity - firstProtectLightPathCapacity;
				Constraint constraint = new Constraint();
				constraint.addConstraintLink(layer, workRoute);
				constraint.addConstraintLink(layer, protectRoute);
				MultiProtectRoutesDaynamic(layer,demand,constraint,isMultiProtectRoute,remainderCapacity);
				
			}
			else
			{
				int WorkassginWaveIndex = work_lightpath.getAssociatedWavelength();
				for(int j = 0;j<workRoute.getLinklist().size();j++)
	    		{
	    			//current link
					workRoute.getLinklist().get(j).getWC().get(WorkassginWaveIndex).setStatus(1);
	    			//sym link
	    			layer.findSymLink(workRoute.getLinklist().get(j)).getWC().get(WorkassginWaveIndex).setStatus(1);
	    			
	    			
	    		}
				work_lightpath.setAssociatedWavelength(-1);
				
				demand.setWorkRoute(null);
				demand.setWork_lightpath(null);
				demand.setWorkPathStatus(false);
			}
		}
		
		
	}
	
	/**
	 * Get the capacity of the moudlation foramt
	 * @param modulationFormat
	 * @return
	 */
	public double GetLightPathCapacity(String modulationFormat)
	{
		
		if(modulationFormat==Constant.BPSK)
			return Constant.BPSK_CAPACITY;
		else if(modulationFormat==Constant.QPSK)
			return Constant.QPSK_CAPACITY;	
		else if(modulationFormat==Constant.QAM)
			return Constant.QAM_CAPACITY;
		return 0;
	
	}
	
	/**
	 * Set the capcity of the lightpath with a ceratin modulation format
	 * @param lightPath
	 * @param modulationFormat
	 */
	public void SetLightPathCapacity(LightPath lightPath, String modulationFormat)
	{
		if(modulationFormat==Constant.BPSK)
			lightPath.setFormatModule_Capacity(Constant.BPSK_CAPACITY);
		else if(modulationFormat==Constant.QPSK)
			lightPath.setFormatModule_Capacity(Constant.QPSK_CAPACITY);
		else if(modulationFormat==Constant.QAM)
			lightPath.setFormatModule_Capacity(Constant.QAM_CAPACITY);	
		//System.out.println(lightPath.getFormatModule_Name()+"  "+lightPath.getFormatModule_Capacity());
	}
	
	/**
	 * Assgin the modulation format for between work route and protect route
	 * @param workRoute
	 * @param protectRoute
	 * @return
	 */
	public String AvaModulationFormatforAllRoutes(LinearRoute workRoute,LinearRoute protectRoute){
		
		LinearRoute matchRoute = null;
		
		if(workRoute.getlength()>=protectRoute.getlength())
			matchRoute = workRoute;
		else
			matchRoute = protectRoute;
		
	
		String avaModulationFormat = AvaModulationFormatforRoute(matchRoute);
		return avaModulationFormat;
		
	}
	
	/**
	 * Assgin the modulation format for route
	 * @param currentRoute
	 * @return
	 */
	public String AvaModulationFormatforRoute(LinearRoute currentRoute)
	{
		
		if(currentRoute.getlength()>Constant.QPSK_DISTANCE_LIMIT)
    	{
    		return Constant.BPSK;	
    	}
    	else if(currentRoute.getlength()>Constant.QAM_DISTANCE_LIMIT
    			&&currentRoute.getlength()<=Constant.QPSK_DISTANCE_LIMIT)//&&nodepair.getUnservedCapacity()<capacity_QPSK)
    	{
    		return Constant.QPSK;
    	}
    	else if(currentRoute.getlength()<=Constant.QAM_DISTANCE_LIMIT)
    	{
    		return Constant.QAM;
    	}
		return null;
	}
	
	/**
	 * Based on the requireTraffic to select available modulation format
	 * @param demand
	 * @return
	 */
	public String AvaModulationFormatforRoute(DemandUnit demand)
	{
		
		if(demand.getTrafficRequest()<=Constant.BPSK_CAPACITY)
    	{
    		return Constant.BPSK;	
    	}
    	else if(demand.getTrafficRequest()>Constant.BPSK_CAPACITY
    			&&demand.getTrafficRequest()<=Constant.QPSK_CAPACITY)//&&nodepair.getUnservedCapacity()<capacity_QPSK)
    	{
    		return Constant.QPSK;
    	}
    	else
    	{
    		return Constant.QAM;
    	}
	}
	
	/**
	 * Assign the wavelength for route
	 * @param layer
	 * @param route
	 */
	public boolean AssginWavelengthForRoute(Layer layer,LinearRoute route,LightPath lightpath)
	{
		int assginWaveIndex  = -1;
		if(layer.getWavelengthNum()>0)
		{
			// firstfit 
			for(int currentWaveIndex = 0; currentWaveIndex < layer.getWavelengthNum();currentWaveIndex++)
	    	{
				boolean key = true;
	    		for(int j = 0;j<route.getLinklist().size();j++)
	    		{
	    			if(route.getLinklist().get(j).getWC().get(currentWaveIndex).getStatus()==1)
	    			{
	    				key  = false;
	    				break;
	    			}
	    		}
	    		if(key)
	    		{
	    			assginWaveIndex = currentWaveIndex;
	    			break;
	    		}
	    	}
			if(assginWaveIndex>=0)
	    	{
	    	//	System.out.println("assgin: "+assginWaveIndex );
	    		for(int j = 0;j<route.getLinklist().size();j++)
	    		{
	    			//current link
	    			route.getLinklist().get(j).getWC().get(assginWaveIndex).setStatus(1);
	    			//sym link
	    			layer.findSymLink(route.getLinklist().get(j)).getWC().get(assginWaveIndex).setStatus(1);
	    			
	    			
	    		}
	    		lightpath.setAssociatedWavelength(assginWaveIndex);
	    	    return true;
	    	}
	    	else
	    		return false;
		}
		else
		{
		//	System.out.println("there is no wavelength resource in this layer");
			return false;
		}
	}
	
	/**
	 * search the protect route
	 * @param layer
	 * @param workRoute
	 */
	public LinearRoute SearchProtectRoute(Layer layer,Nodepair nodepair, Constraint constraint){
		
		LinearRoute newRoute = new LinearRoute("",	0, "", Constant.PROTECT);
		pathSearch workpathsearch=new pathSearch();
		workpathsearch.SearchPathWithDijkstras(nodepair.getSrcNode(), nodepair.getDesNode(),layer, newRoute,constraint);
		if(newRoute.getLinklist().size()>0)
			return newRoute;
		else
			return null;
	}
	
	public void MultiProtectRouteFixed(){
		
	}
	
	/**
	 *
	 * @param layer
	 * @param demand
	 * @param constraint
	 * @param isMultiProtectRoute
	 * @param remainderCapacityGap
	 */
	public void MultiProtectRoutesDaynamic(Layer layer, DemandUnit demand, Constraint constraint,boolean isMultiProtectRoute,	double remainderCapacityGap){
		
		int index =1;
		ArrayList<LinearRoute> protectRouteList = new ArrayList<LinearRoute>();
		ArrayList<LightPath> protectLightPathList = new ArrayList<LightPath>();
		demand.setProtectRouteList(protectRouteList);
		demand.setProtectLightPathList(protectLightPathList);
		while(isMultiProtectRoute){
		
			Nodepair nodepair = demand.getNodepair();
			LightPath currentProtectLightPath = new LightPath("protectPath", index, " ", nodepair);
			LinearRoute newProtectRoute = SearchProtectRoute(layer,nodepair,constraint);
			if(newProtectRoute!=null)//exist available protect route
			{
				
				String currentProtectModulatinFormat = AvaModulationFormatforRoute(newProtectRoute);
				double currentProtectLightPathCapacity =  GetLightPathCapacity(currentProtectModulatinFormat);
				
				currentProtectLightPath.setFormatModule_Capacity(currentProtectLightPathCapacity);
				
				protectRouteList.add(newProtectRoute);
				protectLightPathList.add(currentProtectLightPath);
			  
				remainderCapacityGap = 	 remainderCapacityGap - currentProtectLightPathCapacity;
				if(remainderCapacityGap<=0)
					isMultiProtectRoute = false;	
			}
			else
				isMultiProtectRoute = false;
		}	
	}
	
	public void protectRouteFailed(LightPath work_lightpath,LinearRoute workRoute, DemandUnit demand, Layer layer)
	{
		int WorkassginWaveIndex = work_lightpath.getAssociatedWavelength();
		for(int j = 0;j<workRoute.getLinklist().size();j++)
		{
			//current link
			workRoute.getLinklist().get(j).getWC().get(WorkassginWaveIndex).setStatus(1);
			//sym link
			layer.findSymLink(workRoute.getLinklist().get(j)).getWC().get(WorkassginWaveIndex).setStatus(1);
			
			
		}
		work_lightpath.setAssociatedWavelength(-1);
		
		demand.setWorkRoute(null);
		demand.setWork_lightpath(null);
		demand.setWorkPathStatus(false);
	}
}
