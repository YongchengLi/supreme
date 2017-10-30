package general;

import java.util.ArrayList;

import subgraph.LinearRoute;

import Network.Layer;
import Network.Link;

public class SlotPlane extends Layer{
	
	
	private int associatedslotindex=0; 
    private ArrayList<Link>Constraintlinklist=null;
	private Slot associatedSlot=null;
    
    
	
	public Slot getAssociatedSlot() {
		return associatedSlot;
	}
	public void setAssociatedSlot(Slot associatedSlot) {
		this.associatedSlot = associatedSlot;
	}
	
	public int getAssociatedslotindex() {
		return associatedslotindex;
	}
	public void setAssociatedslotindex(int associatedslotindex) {
		this.associatedslotindex = associatedslotindex;
	}
	public ArrayList<Link> getConstraintlinklist() {
		return Constraintlinklist;
	}
	public void setConstraintlinklist(ArrayList<Link> constraintlinklist) {
		Constraintlinklist = constraintlinklist;
	}

	public SlotPlane(String name, int index, String comments) {
		super(name, index, comments);
		// TODO Auto-generated constructor stub
		this.Constraintlinklist=new ArrayList<Link>();
		this.associatedSlot=new Slot();
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
