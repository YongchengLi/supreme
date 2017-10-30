package general;

import java.util.ArrayList;

import subgraph.LinearRoute;


import Network.Layer;
import Network.Link;

public class WavePlane  extends Layer {

	
	
	
	private int associatedWavelength=0; 
    private ArrayList<Link>Constraintlinklist=null;
	
	public int getAssociatedWavelength() {
		return associatedWavelength;
	}
	public void setAssociatedWavelength(int associatedWavelength) {
		this.associatedWavelength = associatedWavelength;
	}
	public ArrayList<Link> getConstraintlinklist() {
		return Constraintlinklist;
	}
	public void setConstraintlinklist(ArrayList<Link> constraintlinklist) {
		Constraintlinklist = constraintlinklist;
	}

	public WavePlane(String name, int index, String comments) {
		super(name, index, comments);
		// TODO Auto-generated constructor stub
		this.Constraintlinklist=new ArrayList<Link>();
	}
	
	public void RemoveConstrantLinks()
	{
		for(Link routeLinks:this.Constraintlinklist)
		{
			this.addlink(routeLinks);
			routeLinks.getNodeA().addNeiNode(routeLinks.getNodeB());
		}
	}
	
	public void InitWavePlaneLinks()
	{
		for(Link routeLinks:this.Constraintlinklist)
		{
			this.removeLink(routeLinks.getName());
			routeLinks.getNodeA().removeNeiNode(routeLinks.getNodeB());
		}
	}
	
	public void addConstrantLinks(LinearRoute Route)
	{
		for(Link routeLinks:Route.getLinklist())
		{
			this.Constraintlinklist.add(routeLinks);
		
			Link AnothetLink=this.findlink(routeLinks.getNodeB(), routeLinks.getNodeA());
			this.Constraintlinklist.add(AnothetLink);
		}
	}
	
	public void OutputConstraintlinks()
	{
		System.out.println("Constraintlinks: ");
		for(Link link:this.Constraintlinklist)
		{
			System.out.print(link.getName()+" ");
		}
		System.out.println();
	}
	

}
