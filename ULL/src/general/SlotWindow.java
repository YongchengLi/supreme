package general;

import java.util.ArrayList;

import Network.Layer;
import Network.Link;

public class SlotWindow extends Layer{

	  
	private ArrayList<Link>Constraintlinklist=null;
	private ArrayList<Slot> SlotList=null;
	private int firstSlotNum=0;
	private int lastSlotNum=0;
	private Constraint constraint=null;
	private double RouteConstraintOSNR=0;
	
	public SlotWindow(String name, int index, String comments) {
		super(name, index, comments);
		// TODO Auto-generated constructor stub
		this.Constraintlinklist=new ArrayList<Link>();
		this.SlotList=new ArrayList<Slot>();
		this.constraint=new Constraint();
	}

	public ArrayList<Link> getConstraintlinklist() {
		return Constraintlinklist;
	}

	public void setConstraintlinklist(ArrayList<Link> constraintlinklist) {
		Constraintlinklist = constraintlinklist;
	}

	public ArrayList<Slot> getSlotList() {
		return SlotList;
	}

	public void setSlotList(ArrayList<Slot> slotList) {
		SlotList = slotList;
	}

	public int getFirstSlotNum() {
		return firstSlotNum;
	}

	public void setFirstSlotNum(int firstSlotNum) {
		this.firstSlotNum = firstSlotNum;
	}

	public int getLastSlotNum() {
		return lastSlotNum;
	}

	public void setLastSlotNum(int lastSlotNum) {
		this.lastSlotNum = lastSlotNum;
	}

	public Constraint getConstraint() {
		return constraint;
	}

	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	public double getRouteConstraintOSNR() {
		return RouteConstraintOSNR;
	}

	public void setRouteConstraintOSNR(double routeConstraintOSNR) {
		RouteConstraintOSNR = routeConstraintOSNR;
	}
	
}
