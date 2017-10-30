package Network;

import java.util.ArrayList;

import general.CommonObject;
import general.SlotWindow;
import subgraph.LinearRoute;

/**
 * Used for the project of Quasi-WDM
 * @author Yongcheng
 *
 */
public class LightPath extends CommonObject {

	private LinearRoute physicPath  = null; //物理路径
	private LinearRoute ipPath      = null; //ip路径
	private SlotWindow associatedslotwindow = null;
	
	private ArrayList<LightPath> TrafficGroomingLighPathlist = new ArrayList<LightPath>();
	
	private Nodepair    AssociatedNodepair    = null;//光路所在的节点对
	private double occupiedCapacity = 0;
	
	private double CostOfRegen = 0;
	private int NumOfRegen = 0;
	
	private double CostofIpRouterPort = 0;
	private int NumOfIpRouterPort = 0;
	
	private String formatModule_Name = null;
	private double formatModule_Capacity = 0;
	
	private boolean TrafficGrooming = true;
	private double cost = 0;
	
	private int  associatedWavelength = -1;
	private double associatedSoltUnit = 0;
	private LightPath asssociatedLightpath = null;
	private ArrayList<LightPath> asssociatedLightpathlist = new ArrayList<LightPath>();;
	private ArrayList<DemandUnit> DemandUnitlist = new ArrayList<DemandUnit>();
	
	private int OccpyfirstSlot = -1;  //光路所占用的第一个FS的index
	private int RequiredSlotNum =-1;  //光路需要的FS个数

	private boolean OSNRblockStatus = false;
	public LightPath(String name, int index, String comments,Nodepair associatedNodepair) {
		super(name, index, comments);
		AssociatedNodepair = associatedNodepair;
	}

	public LinearRoute getPhysicPath() {
		return physicPath;
	}

	public void setPhysicPath(LinearRoute physicPath) {
		this.physicPath = physicPath;
	}

	public LinearRoute getIpPath() {
		return ipPath;
	}

	public void setIpPath(LinearRoute ipPath) {
		this.ipPath = ipPath;
	}

	public Nodepair getAssociatedNodepair() {
		return AssociatedNodepair;
	}

	public void setAssociatedNodepair(Nodepair associatedNodepair) {
		AssociatedNodepair = associatedNodepair;
	}

	public double getOccupiedCapacity() {
		return occupiedCapacity;
	}

	public void setOccupiedCapacity(double occupiedCapacity) {
		this.occupiedCapacity = occupiedCapacity;
	}

	public ArrayList<LightPath> getTrafficGroomingLighPathlist() {
		return TrafficGroomingLighPathlist;
	}

	public void setTrafficGroomingLighPathlist(
			ArrayList<LightPath> trafficGroomingLighPathlist) {
		TrafficGroomingLighPathlist = trafficGroomingLighPathlist;
	}

	public double getCostOfRegen() {
		return CostOfRegen;
	}

	public void setCostOfRegen(double costOfRegen) {
		CostOfRegen = costOfRegen;
	}

	public int getNumOfRegen() {
		return NumOfRegen;
	}

	public void setNumOfRegen(int numOfRegen) {
		NumOfRegen = numOfRegen;
	}

	public double getCostofIpRouterPort() {

		return CostofIpRouterPort;
	}

	public void setCostofIpRouterPort(double costofIpRouterPort) {
		CostofIpRouterPort = costofIpRouterPort;
	}

	public int getNumOfIpRouterPort() {
		return NumOfIpRouterPort;
	}

	public void setNumOfIpRouterPort(int numOfIpRouterPort) {
		NumOfIpRouterPort = numOfIpRouterPort;
	}

	public void CalNumOfRegen(LinearRoute route,double formatModule_DistanceLimit) {
		
		double LightPath_Distance = 0;
		for(Link currentLink:route.getLinklist())
		{
			LightPath_Distance = LightPath_Distance+currentLink.getLength();
		}
			
		int NumofRegen=(int) Math.ceil(LightPath_Distance/formatModule_DistanceLimit);   		
	    	NumofRegen=NumofRegen-1; 
	    this.setNumOfRegen(NumofRegen);
	}

	
	public int NumOfRegenWithFormat(LinearRoute route,double formatModule_DistanceLimit) {
		
		double LightPath_Distance = 0;
		for(Link currentLink:route.getLinklist())
		{
			LightPath_Distance = LightPath_Distance+currentLink.getLength();
		}
			
		int NumofRegen=(int) Math.ceil(LightPath_Distance/formatModule_DistanceLimit);   		
	    	NumofRegen=NumofRegen-1; 
	    return NumofRegen;
	}
	
	public String getFormatModule_Name() {
		return formatModule_Name;
	}

	public void setFormatModule_Name(String formatModule_Name) {
		this.formatModule_Name = formatModule_Name;
	}

	public double getFormatModule_Capacity() {
		return formatModule_Capacity;
	}

	public void setFormatModule_Capacity(double formatModule_Capacity) {
		this.formatModule_Capacity = formatModule_Capacity;
	}

	public boolean isTrafficGrooming() {
		return TrafficGrooming;
	}

	public void setTrafficGrooming(boolean trafficGrooming) {
		TrafficGrooming = trafficGrooming;
	}

	public ArrayList<DemandUnit> getDemandUnitlist() {
		return DemandUnitlist;
	}

	public void setDemandUnitlist(ArrayList<DemandUnit> demandUnitlist) {
		DemandUnitlist = demandUnitlist;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public int getAssociatedWavelength() {
		return associatedWavelength;
	}

	public void setAssociatedWavelength(int associatedWavelength) {
		this.associatedWavelength = associatedWavelength;
	}

	public double getAssociatedSoltUnit() {
		return associatedSoltUnit;
	}

	public void setAssociatedSoltUnit(double associatedSoltUnit) {
		this.associatedSoltUnit = associatedSoltUnit;
	}

	public LightPath getAsssociatedLightpath() {
		return asssociatedLightpath;
	}

	public void setAsssociatedLightpath(LightPath asssociatedLightpath) {
		this.asssociatedLightpath = asssociatedLightpath;
	}

	public ArrayList<LightPath> getAsssociatedLightpathlist() {
		return asssociatedLightpathlist;
	}

	public void setAsssociatedLightpathlist(
			ArrayList<LightPath> asssociatedLightpathlist) {
		this.asssociatedLightpathlist = asssociatedLightpathlist;
	}

	public int getOccpyfirstSlot() {
		return OccpyfirstSlot;
	}

	public void setOccpyfirstSlot(int occpyfirstSlot) {
		OccpyfirstSlot = occpyfirstSlot;
	}

	public int getRequiredSlotNum() {
		return RequiredSlotNum;
	}

	public void setRequiredSlotNum(int requiredSlotNum) {
		RequiredSlotNum = requiredSlotNum;
	}


	//route of IP layer
	LinearRoute IPLayerRoute_Lightpath_Trafficgrooming = null;


	public LinearRoute getIPLayerRoute_Lightpath_Trafficgrooming() {
		return IPLayerRoute_Lightpath_Trafficgrooming;
	}

	public void setIPLayerRoute_Lightpath_Trafficgrooming(
			LinearRoute iPLayerRoute_Lightpath_Trafficgrooming) {
		IPLayerRoute_Lightpath_Trafficgrooming = iPLayerRoute_Lightpath_Trafficgrooming;
	}

	public SlotWindow getAssociatedslotwindow() {
		return associatedslotwindow;
	}

	public void setAssociatedslotwindow(SlotWindow associatedslotwindow) {
		this.associatedslotwindow = associatedslotwindow;
	}

	public boolean isOSNRblockStatus() {
		return OSNRblockStatus;
	}

	public void setOSNRblockStatus(boolean oSNRblockStatus) {
		OSNRblockStatus = oSNRblockStatus;
	}
	
}
