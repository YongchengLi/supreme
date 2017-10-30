package rapidDataEvacuation;

import java.util.ArrayList;

import Network.Node;
import general.Block;
import general.CommonObject;

public class DataCenter extends CommonObject{
	
	
	public DataCenter(String name, int index, String comments) {
		super(name, index, comments);
		// TODO Auto-generated constructor stub
	}
	
	private Node associatedNode = null;
	
	private int Storage = 0;
	
	private int emptyStorage = 0;
	
	private double damagedRisk = 0;
	
	private boolean damagedStatus = false;
	
	private ArrayList<Content> contentlist = new ArrayList<Content>();
	private ArrayList<Block> blocklist = new ArrayList<Block>();
	
	public Node getAssociatedNode() {
		return associatedNode;
	}
	public void setAssociatedNode(Node associatedNode) {
		this.associatedNode = associatedNode;
	}
	public int getStorage() {
		return Storage;
	}
	public void setStorage(int storage) {
		Storage = storage;
	}
	public ArrayList<Content> getContentlist() {
		return contentlist;
	}
	public void setContentlist(ArrayList<Content> contentlist) {
		this.contentlist = contentlist;
	}
	public double getDamagedRisk() {
		return damagedRisk;
	}
	public void setDamagedRisk(double damagedRisk) {
		this.damagedRisk = damagedRisk;
	}
	public boolean isDamagedStatus() {
		return damagedStatus;
	}
	public void setDamagedStatus(boolean damagedStatus) {
		this.damagedStatus = damagedStatus;
	}
	public int getEmptyStorage() {
		return emptyStorage;
	}
	public void setEmptyStorage(int emptyStorage) {
		this.emptyStorage = emptyStorage;
	}
	public ArrayList<Block> getBlocklist() {
		return blocklist;
	}
	public void setBlocklist(ArrayList<Block> blocklist) {
		this.blocklist = blocklist;
	}
	

}
