package subgraph;
import general.CommonObject;

import java.util.ArrayList;
import Network.*;

public class Subgraph extends CommonObject{
    private ArrayList<Node>nodelist=null;
    private ArrayList<Link>linklist=null;
    private ArrayList<CommonObject>objetc=null;
    private ArrayList<Subgraph>routelist=null;
    
	public Subgraph(String name, int index, String comments) {
		super(name, index, comments);
		// TODO Auto-generated constructor stub
       this.nodelist=new ArrayList<Node>();
       this.linklist=new ArrayList<Link>();
       this.objetc=new ArrayList<CommonObject>();
       this.routelist=new ArrayList<Subgraph>();
	}
	public ArrayList<Node> getNodelist() {
		return nodelist;
	}
	public void setNodelist(ArrayList<Node> nodelist) {
		this.nodelist = nodelist;
	}
	public ArrayList<Link> getLinklist() {
		return linklist;
	}
	public void setLinklist(ArrayList<Link> linklist) {
		this.linklist = linklist;
	}
	public ArrayList<CommonObject> getObjetc() {
		return objetc;
	}
	public void setObjetc(ArrayList<CommonObject> objetc) {
		this.objetc = objetc;
	}
	public ArrayList<Subgraph> getRoutelist() {
		return routelist;
	}
	public void setRoutelist(ArrayList<Subgraph> routelist) {
		this.routelist = routelist;
	}
	public double getlength(){
		double sum=0;
		for(Link link:this.getLinklist()){
			sum+=link.getLength();
		}
		return sum;
	}
	
	public double getDistance(){
		double sum=0;
		for(Link link:this.getLinklist()){
			if(link.getNodeA().getIndex()<link.getNodeB().getIndex())
				sum+=link.getLength();
		}
		return sum;
	}
}
