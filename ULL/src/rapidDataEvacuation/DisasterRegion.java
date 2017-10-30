package rapidDataEvacuation;

import general.CommonObject;

import java.util.ArrayList;

import Network.*;



public class DisasterRegion extends CommonObject {
	
	
	private ArrayList<Node> riskNodelist = new ArrayList<Node>();
	private ArrayList<Link> riskLinklist = new ArrayList<Link>();
	private int x = 0;
	private int y = 0;
	
	
	public DisasterRegion(String name, int index, String comments) {
		super(name, index, comments);
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<Node> getRiskNodelist() {
		return riskNodelist;
	}
	public void setRiskNodelist(ArrayList<Node> riskNodelist) {
		this.riskNodelist = riskNodelist;
	}
	public ArrayList<Link> getRiskLinklist() {
		return riskLinklist;
	}
	public void setRiskLinklist(ArrayList<Link> riskLinklist) {
		this.riskLinklist = riskLinklist;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	

}
