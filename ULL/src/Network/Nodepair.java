package Network;

import general.CommonObject;
import general.Constant;
import general.Modulation;
import general.SlotWindow;

import java.util.ArrayList;

import subgraph.*;

public class Nodepair extends CommonObject{
    private Layer associatedLayer=null;
    private Node  srcNode=null;
    private Node  desNode=null;
    private ArrayList<LinearRoute>linearlist=null;	
	
    private int   keydata=Constant.UNBALACENED;
	
	private int slotNum=0;
	private int overheadSotNum=0;
	
	
	private double NodepairProbability=0;
	
	
	public double getNodepairProbability() {
		return NodepairProbability;
	}
	public void setNodepairProbability(double nodepairProbability) {
		NodepairProbability = nodepairProbability;
	}
	
	
	public Nodepair(String name, int index, String comments,Layer associatedLayer,Node srcNode,Node desNode) {
		super(name, index, comments);
		// TODO Auto-generated constructor stub
		this.associatedLayer=associatedLayer;
		this.srcNode=srcNode;
		this.desNode=desNode;
		this.linearlist=new ArrayList<LinearRoute>();
		
	}
	public Layer getAssociatedLayer() {
		return associatedLayer;
	}
	public void setAssociatedLayer(Layer associatedLayer) {
		this.associatedLayer = associatedLayer;
	}
	public Node getSrcNode() {
		return srcNode;
	}
	public void setSrcNode(Node srcNode) {
		this.srcNode = srcNode;
	}
	public Node getDesNode() {
		return desNode;
	}
	public void setDesNode(Node desNode) {
		this.desNode = desNode;
	}
	public ArrayList<LinearRoute> getLinearlist() {
		return linearlist;
	}
	public void setLinearlist(ArrayList<LinearRoute> linearlist) {
		this.linearlist = linearlist;
	}
	public int getKeydata() {
		return keydata;
	}
	public void setKeydata(int keydata) {
		this.keydata = keydata;
	}
   /*
    * add a route to the routelist;
    */
	public void addRoute(LinearRoute route){
		this.linearlist.add(route);
	}
	/*
	 * remove route from the linearlist;
	 */
	public void removeRoute(LinearRoute route){
		this.linearlist.remove(route);
	}
	public void removeRoute(int index){
		for(int i=0;i<this.linearlist.size();i++){
			if(this.linearlist.get(i).getIndex()==index){
				this.linearlist.remove(i);
				break;
			}
		}
	}
	public void removeRoute(String name){
		for(int i=0;i<this.linearlist.size();i++){
		    if(this.linearlist.get(i).getName().equals(name)){
		    	this.linearlist.remove(i);
		    	break;
		    }
		}
	}
	public int getSlotNum() {
		return slotNum;
	}
	public void setSlotNum(int slotNum) {
		this.slotNum = slotNum;
	}
	public int getOverheadSotNum() {
		return overheadSotNum;
	}
	public void setOverheadSotNum(int overheadSotNum) {
		this.overheadSotNum = overheadSotNum;
	}
	
	/********************Quasi-WDM*****************************/
	public double TrafficLoad  = 0;


	public double getTrafficLoad() {
		return TrafficLoad;
	}
	public void setTrafficLoad(double trafficLoad) {
		TrafficLoad = trafficLoad;
	}
	
	public double servedCapacity = 0;
	public double unservedCapacity = 0;


	public double getServedCapacity() {
		return servedCapacity;
	}
	public void setServedCapacity(double servedCapacity) {
		this.servedCapacity = servedCapacity;
	}
	public double getUnservedCapacity() {
		return unservedCapacity;
	}
	public void setUnservedCapacity(double unservedCapacity) {
		this.unservedCapacity = unservedCapacity;
	}
	
	
	//*****************************Desgined by Quasi-WDM***************************//
	private ArrayList<LightPath> associatedLightPathList = new ArrayList<LightPath>();


	public ArrayList<LightPath> getAssociatedLightPathList() {
		return associatedLightPathList;
	}
	public void setAssociatedLightPathList(
			ArrayList<LightPath> associatedLightPathList) {
		this.associatedLightPathList = associatedLightPathList;
	}
	
	 //*********************Quasi-CWDM***********************//
	 private double costOfRegenWithBPSK = 0;
	 private double costOfIpRouterWithBPSK = 0;
	 
	 private double costOfRegenWithQPSK = 0;
	 private double costOfIpRouterWithQPSK = 0;
	 
	 private double costOfRegenWithQAM = 0;
	 private double costOfIpRouterWithQAM = 0;
	 
     private double GenerateLightPathCapacity = 0;
	 private double LightPathOccuipedCapacity = 0;
	 
	 private boolean AvailableSlotRecourse = true;
	 private int     NumofRegen_BPSK = 0;
	 private int     NumofIProuter_BPSK = 0;
	 
	 private int     NumofRegen_QPSK = 0;
	 private int     NumofIProuter_QPSK = 0;
	 
	 private int     NumofRegen_QAM = 0;
	 private int     NumofIProuter_QAM = 0;
	 
	 public double getCostOfRegenWithBPSK() {
	  return costOfRegenWithBPSK;
	 }
	 public void setCostOfRegenWithBPSK(double costOfRegenWithBPSK) {
	  this.costOfRegenWithBPSK = costOfRegenWithBPSK;
	 }
	 public double getCostOfIpRouterWithBPSK() {
	  return costOfIpRouterWithBPSK;
	 }
	 public void setCostOfIpRouterWithBPSK(double costOfIpRouterWithBPSK) {
	  this.costOfIpRouterWithBPSK = costOfIpRouterWithBPSK;
	 }
	 public double getCostOfRegenWithQPSK() {
	  return costOfRegenWithQPSK;
	 }
	 public void setCostOfRegenWithQPSK(double costOfRegenWithQPSK) {
	  this.costOfRegenWithQPSK = costOfRegenWithQPSK;
	 }
	 public double getCostOfIpRouterWithQPSK() {
	  return costOfIpRouterWithQPSK;
	 }
	 public void setCostOfIpRouterWithQPSK(double costOfIpRouterWithQPSK) {
	  this.costOfIpRouterWithQPSK = costOfIpRouterWithQPSK;
	 }
	 public double getCostOfRegenWithQAM() {
	  return costOfRegenWithQAM;
	 }
	 public void setCostOfRegenWithQAM(double costOfRegenWithQAM) {
	  this.costOfRegenWithQAM = costOfRegenWithQAM;
	 }
	 public double getCostOfIpRouterWithQAM() {
	  return costOfIpRouterWithQAM;
	 }
	 
	 public void setCostOfIpRouterWithQAM(double costOfIpRouterWithQAM) {
	  this.costOfIpRouterWithQAM = costOfIpRouterWithQAM;
	 }	
	 
	 
	 
	 public double getGenerateLightPathCapacity() {
		return GenerateLightPathCapacity;
	 }
	
	 public void setGenerateLightPathCapacity(double generateLightPathCapacity) {	
		 GenerateLightPathCapacity = generateLightPathCapacity;	
	 }
		 
    public double getLightPathOccuipedCapacity() {
		return LightPathOccuipedCapacity;
	}
	
    public void setLightPathOccuipedCapacity(double lightPathOccuipedCapacity) {
		LightPathOccuipedCapacity = lightPathOccuipedCapacity;
	}
	
	public boolean isAvailableSlotRecourse() {
		return AvailableSlotRecourse;
	}
	
	public void setAvailableSlotRecourse(boolean availableSlotRecourse) {
		AvailableSlotRecourse = availableSlotRecourse;
	}
	
	public boolean isInNodeList(ArrayList<String> nodepairlist)
    {
		 for(int i = 0; i <nodepairlist.size(); i++)
		 {
			 if(nodepairlist.get(i)==this.getName())
				 return true;
		 }
		return false;	 
	 }
	public int getNumofRegen_BPSK() {
		return NumofRegen_BPSK;
	}
	public void setNumofRegen_BPSK(int numofRegen_BPSK) {
		NumofRegen_BPSK = numofRegen_BPSK;
	}
	public int getNumofIProuter_BPSK() {
		return NumofIProuter_BPSK;
	}
	public void setNumofIProuter_BPSK(int numofIProuter_BPSK) {
		NumofIProuter_BPSK = numofIProuter_BPSK;
	}
	public int getNumofRegen_QPSK() {
		return NumofRegen_QPSK;
	}
	public void setNumofRegen_QPSK(int numofRegen_QPSK) {
		NumofRegen_QPSK = numofRegen_QPSK;
	}
	public int getNumofIProuter_QPSK() {
		return NumofIProuter_QPSK;
	}
	public void setNumofIProuter_QPSK(int numofIProuter_QPSK) {
		NumofIProuter_QPSK = numofIProuter_QPSK;
	}
	public int getNumofRegen_QAM() {
		return NumofRegen_QAM;
	}
	public void setNumofRegen_QAM(int numofRegen_QAM) {
		NumofRegen_QAM = numofRegen_QAM;
	}
	public int getNumofIProuter_QAM() {
		return NumofIProuter_QAM;
	}
	public void setNumofIProuter_QAM(int numofIProuter_QAM) {
		NumofIProuter_QAM = numofIProuter_QAM;
	}
	

	
	private SlotWindow assoicatedslotwindow=null;
    private boolean OSNRblockStatus = true;

    private LinearRoute usedRoute = null;
    private Modulation usedModulation = null;
    
	public SlotWindow getAssoicatedslotwindow() {
		return assoicatedslotwindow;
	}
	public void setAssoicatedslotwindow(SlotWindow assoicatedslotwindow) {
		this.assoicatedslotwindow = assoicatedslotwindow;
	}
	public boolean isOSNRblockStatus() {
		return OSNRblockStatus;
	}
	public void setOSNRblockStatus(boolean oSNRblockStatus) {
		OSNRblockStatus = oSNRblockStatus;
	}
	public LinearRoute getUsedRoute() {
		return usedRoute;
	}
	public void setUsedRoute(LinearRoute usedRoute) {
		this.usedRoute = usedRoute;
	}
	public Modulation getUsedModulation() {
		return usedModulation;
	}
	public void setUsedModulation(Modulation usedModulation) {
		this.usedModulation = usedModulation;
	}
	
	
	
}
