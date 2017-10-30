package Network;

import general.Ampifier;
import general.CommonObject;
import general.Constant;
import general.Cwavelength;
import general.Slot;
import general.Task;
import general.TimeCapacity;
import general.W_wc;

import java.util.ArrayList;

import rapidDataEvacuation.Content;
import rapidDataEvacuation.risk;


public class Link extends CommonObject{
    private  Layer   associatedLayer=null;
    private  Node    nodeA=null;
    private  Node    nodeB=null;
    private  double  length=0;
    private  double  cost=0;
    private  double  workhop=0;
    private  double  initialcost=0; 
	private  int     status=Constant.UNVISITED; 
	private  double     routevisited=0;
    private  int     w=0;
    private  ArrayList<Cwavelength>WC=null;
    private  int     AmplifierNum=0;
    private  double  OSNR=0;
    private double AmpifierGain=0;
    private  double[] SetUpRate=null;
    private  double freeProbability=0.9;
    private  double[] CapacityDistribution=null;
    private double Amp_Gain=0;
    private int TraversedNum = 0;
	private ArrayList<Ampifier> Ampifierlist=null;
    private int OSNRblockNum=0;
    
    //**********************************************//
	private Content usedCon = null;
    private boolean workStatus = false;
	private double linkdelay = 0;
	private double residualCapacity = 0;
	private ArrayList<Task> tasklist = new ArrayList<Task>();
	
	
	public double[] getCapacityDistribution() {
		return CapacityDistribution;
	}
	public void setCapacityDistribution(double[] capacityDistribution) {
		CapacityDistribution = capacityDistribution;
	}
	public double getFreeProbability() {
		return freeProbability;
	}
	public void setFreeProbability(double freeProbability) {
		this.freeProbability = freeProbability;
	}
	
	public double[] getSetUpRate() {
		return SetUpRate;
	}
	public void setSetUpRate(double[] setUpRate) {
		SetUpRate = setUpRate;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public ArrayList<Cwavelength> getWC() {
		return WC;
	}
	public void setWC(ArrayList<Cwavelength> wc) {
		WC = wc;
	}
	public double getRoutevisited() {
		return routevisited;
	}
	public void setRoutevisited(double  routevisited) {
		this.routevisited = routevisited;
	}
	public double getWorkhop() {
		return workhop;
	}
	public void setWorkhop(double workhop) {
		this.workhop = workhop;
	}
	public double getInitialcost() {
		return initialcost;
	}
	public void setInitialcost(double initialcost) {
		this.initialcost = initialcost;
	}
	public Layer getAssociatedLayer() {
		return associatedLayer;
	}
	public void setAssociatedLayer(Layer associatedLayer) {
		this.associatedLayer = associatedLayer;
	}
	public Node getNodeA() {
		return nodeA;
	}
	public void setNodeA(Node nodeA) {
		this.nodeA = nodeA;
	}
	public Node getNodeB() {
		return nodeB;
	}
	public void setNodeB(Node nodeB) {
		this.nodeB = nodeB;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	 public double getCost() {
			return cost;
	}
		public void setCost(double cost) {
			this.cost = cost;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	

	public int getAmplifierNum() {
		return AmplifierNum;
	}
	public void setAmplifierNum(int amplifierNum) {
		AmplifierNum = amplifierNum;
	}
	
	
	public double getAmpifierGain() {
		return AmpifierGain;
	}
	public void setAmpifierGain(double ampifierGain) {
		AmpifierGain = ampifierGain;
	}
	public double getOSNR() {
		return OSNR;
	}
	public void setOSNR(double oSNR) {
		OSNR = oSNR;
	}
	
	
	
	public ArrayList<Ampifier> getAmpifierlist() {
		return Ampifierlist;
	}
	public void setAmpifierlist(ArrayList<Ampifier> ampifierlist) {
		Ampifierlist = ampifierlist;
	}
	public Link(String name, int index, String comments,Layer associatedLayer,Node nodeA,Node nodeB,double length,double cost) {
		super(name,index,comments);
		// TODO Auto-generated constructor stub
		this.associatedLayer=associatedLayer;
		this.nodeA=nodeA;
		this.nodeB=nodeB;
		this.length=length;
	    this.cost=cost;
	    this.initialcost=cost;
	    this.WC=new ArrayList<Cwavelength>();
	    this.Ampifierlist=new ArrayList<Ampifier>();
	    this.slotList=new ArrayList<Slot>();
	    this.timeCapacity = new TimeCapacity();
	}

	public void clear_routevisited(){
		this.routevisited=0;
	}
	public void  add_W(int number){
		for(int i_number=0;i_number< number;i_number++){
			Cwavelength W_n=new Cwavelength();
			//记录标号
			W_n.setVaule(i_number);//add
			this.WC.add(W_n);
		}
	}
	
	public void Output_W_Ocppuy(){
		for(int i=0;i<this.WC.size();i++){
			System.out.print(this.WC.get(i).getStatus());
			
		}
		System.out.println();
	}
	
	public int Free_index(){
		int Free_index=Constant.maxium;
		for(int i=0;i<this.WC.size();i++){
			if(this.WC.get(i).equals(0)){
				Free_index=i;
				break;
			}
		}
		return Free_index;
		
	}
	public void Init_W(){
		for(int i=0;i<this.WC.size();i++){
			this.WC.get(i).setStatus(0);
		}
	}
	public void AddAmpifier(Ampifier ampifier){
		this.Ampifierlist.add(ampifier);
	}

	
	
	/*
	 * slot of each links
	 */
	 private  ArrayList<Slot>slotList=null;

	public ArrayList<Slot> getSlotList() {
		return slotList;
	}
	public void setSlotList(ArrayList<Slot> slotList) {
		this.slotList = slotList;
	}
	public void  addSlot(int number){
		for(int i_number=0;i_number<=number;i_number++){
			Slot slot=new Slot();
			//记录标号
			int index=this.slotList.size();
			slot.setVaule(i_number);//add
			slot.setStatus(0);
			slot.setAssociatedIndex(index);
			this.slotList.add(slot);
		}
	}
	public void  InitSlot(){
		for(int i_number=0;i_number<this.slotList.size();i_number++){
			this.slotList.get(i_number).setStatus(0);
		}
	}
	
	
	/***************************Quasi-CWDM*******************************/
	private double UsedFs = 0;

	public double getUsedFs() {
		return UsedFs;
	}
	public void setUsedFs(double usedFs) {
		UsedFs = usedFs;
	}
    
	public int MaxUsedIndex = -1;





	public int getMaxUsedIndex() {
		return MaxUsedIndex;
	}
	public void setMaxUsedIndex(int maxUsedIndex) {
		MaxUsedIndex = maxUsedIndex;
	}
	public Content getUsedCon() {
		return usedCon;
	}
	public void setUsedCon(Content usedCon) {
		this.usedCon = usedCon;
	}
	public boolean isWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(boolean workStatus) {
		this.workStatus = workStatus;
	}
	public double getLinkdelay() {
		return linkdelay;
	}
	public void setLinkdelay(double linkdelay) {
		this.linkdelay = linkdelay;
	}
	public double getResidualCapacity() {
		return residualCapacity;
	}
	public void setResidualCapacity(double residualCapacity) {
		this.residualCapacity = residualCapacity;
	}
	public ArrayList<Task> getTasklist() {
		return tasklist;
	}
	public void setTasklist(ArrayList<Task> tasklist) {
		this.tasklist = tasklist;
	}
	
	/*****************************Evacuation*********************************/
	private ArrayList<Task> evacuationTaskList =new ArrayList<Task>();
	private int servedContent = 0;
	private TimeCapacity timeCapacity = null;
	private double risk = 0;
	
	private ArrayList<risk> risklist = new ArrayList<risk>();
	
	public ArrayList<Task> getEvacuationTaskList() {
		return evacuationTaskList;
	}
	public void setEvacuationTaskList(ArrayList<Task> evacuationTaskList) {
		this.evacuationTaskList = evacuationTaskList;
	}
	public int getServedContent() {
		return servedContent;
	}
	public void setServedContent(int servedContent) {
		this.servedContent = servedContent;
	}
	public TimeCapacity getTimeCapacity() {
		return timeCapacity;
	}
	public void setTimeCapacity(TimeCapacity timeCapacity) {
		this.timeCapacity = timeCapacity;
	}
	public double getRisk() {
		return risk;
	}
	public void setRisk(double risk) {
		this.risk = risk;
	}
	public ArrayList<risk> getRisklist() {
		return risklist;
	}
	public void setRisklist(ArrayList<risk> risklist) {
		this.risklist = risklist;
	}
	public double getAmp_Gain() {
		return Amp_Gain;
	}
	public void setAmp_Gain(double amp_Gain) {
		Amp_Gain = amp_Gain;
	}
	public int getTraversedNum() {
		return TraversedNum;
	}
	public void setTraversedNum(int traversedNum) {
		TraversedNum = traversedNum;
	}
	public int getOSNRblockNum() {
		return OSNRblockNum;
	}
	public void setOSNRblockNum(int oSNRblockNum) {
		OSNRblockNum = oSNRblockNum;
	}
	
	
	/******************************ULL fiber*************************************/
	
	private int repairTime = 0;


	public int getRepairTime() {
		return repairTime;
	}
	public void setRepairTime(int repairTime) {
		this.repairTime = repairTime;
	}
	
	
	
}
