package rapidDataEvacuation;

import java.util.ArrayList;

import subgraph.LinearRoute;

import Network.Node;
import general.Block;
import general.CommonObject;

public class Content extends CommonObject{

	private int size = 0;    // the size of content which is in terms of GB
	private int weight = 0;  // the weight of the content
	private int replicasNum = 0;
	private ArrayList<DataCenter> DataCenterList = new ArrayList<DataCenter>();

	private DataCenter associatedDataCenter = null; //the data center 
	
	private Content parentContent = null;
	private ArrayList<Content> subContentList = new ArrayList<Content>(); //content fragmentations
	
	private double StartTime = 0;
	private double TotalTime = 0;
	
	private LinearRoute route = null;
	private boolean safeStatus = false;
	
	private boolean parityData = false;
	
	private ArrayList<Block> blocklist = new ArrayList<Block>();
	
	private int addR = 0;
	private int blockSize = 0;
	public Content(String name, int index, String comments) {
		super(name, index, comments);
		// TODO Auto-generated constructor stub
	}

	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public DataCenter getAssociatedDataCenter() {
		return associatedDataCenter;
	}

	public void setAssociatedDataCenter(DataCenter associatedDataCenter) {
		this.associatedDataCenter = associatedDataCenter;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getReplicasNum() {
		return replicasNum;
	}

	public void setReplicasNum(int replicasNum) {
		this.replicasNum = replicasNum;
	}

	public ArrayList<DataCenter> getDataCenterList() {
		return DataCenterList;
	}

	public void setDataCenterList(ArrayList<DataCenter> dataCenterList) {
		DataCenterList = dataCenterList;
	}

	public Content getParentContent() {
		return parentContent;
	}

	public void setParentContent(Content parentContent) {
		this.parentContent = parentContent;
	}

	public ArrayList<Content> getSubContentList() {
		return subContentList;
	}

	public void setSubContentList(ArrayList<Content> subContentList) {
		this.subContentList = subContentList;
	}

	public double getStartTime() {
		return StartTime;
	}

	public void setStartTime(double startTime) {
		StartTime = startTime;
	}

	public double getTotalTime() {
		return TotalTime;
	}

	public void setTotalTime(double totalTime) {
		TotalTime = totalTime;
	}

	public LinearRoute getRoute() {
		return route;
	}

	public void setRoute(LinearRoute route) {
		this.route = route;
	}

	public boolean isSafeStatus() {
		return safeStatus;
	}

	public void setSafeStatus(boolean safeStatus) {
		this.safeStatus = safeStatus;
	}

	public boolean isParityData() {
		return parityData;
	}

	public void setParityData(boolean parityData) {
		this.parityData = parityData;
	}


	public ArrayList<Block> getBlocklist() {
		return blocklist;
	}


	public void setBlocklist(ArrayList<Block> blocklist) {
		this.blocklist = blocklist;
	}


	public int getAddR() {
		return addR;
	}


	public void setAddR(int addR) {
		this.addR = addR;
	}


	public int getBlockSize() {
		return blockSize;
	}


	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}


	

	

	
	
}
