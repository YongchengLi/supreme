package general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import subgraph.LinearRoute;

import Network.Layer;
import Network.Link;
import Network.Node;

public class Constraint {
	   public int maxium=100000000;
	   
	   public int UNVISITED=0;
	   
	   public int VISITED=1;
	   
	   public int VISITEDTWICE=2;
	   
	   public int WORKING=1;
	   
	   public int UNBALACENED=0;
	   
	   public int BALACENED=1;
	   
		     
	   
	   private ArrayList<Link>ExcludedLinklist=null;	  
	   private ArrayList<Node>ExcludedNodelist=null;
	     
	     
	   public Constraint() {	 		    		 	
		   this.ExcludedNodelist = new ArrayList<Node>();	 		
		   this.ExcludedLinklist = new ArrayList<Link>(); 	
	   }		
	   public ArrayList<Link> getExcludedLinklist() {	
		   return ExcludedLinklist;		
	   }	 		
	   public void setExcludedLinklist(ArrayList<Link> excludedLinklist) {		
		   ExcludedLinklist = excludedLinklist;		
	   }	
	   public ArrayList<Node> getExcludedNodelist() {		
		   return ExcludedNodelist;		
	   }		
	   public void setExcludedNodelist(ArrayList<Node> excludedNodelist) {		
		   ExcludedNodelist = excludedNodelist;		
	   }
	
	   public int getMaxium() {
			return maxium;
		}
		public void setMaxium(int maxium) {
			this.maxium = maxium;
		}
		public int getUNVISITED() {
			return UNVISITED;
		}
		public void setUNVISITED(int uNVISITED) {
			UNVISITED = uNVISITED;
		}
		public int getVISITED() {
			return VISITED;
		}
		public void setVISITED(int vISITED) {
			VISITED = vISITED;
		}
		public int getVISITEDTWICE() {
			return VISITEDTWICE;
		}
		public void setVISITEDTWICE(int vISITEDTWICE) {
			VISITEDTWICE = vISITEDTWICE;
		}
		public int getWORKING() {
			return WORKING;
		}
		public void setWORKING(int wORKING) {
			WORKING = wORKING;
		}
		public int getUNBALACENED() {
			return UNBALACENED;
		}
		public void setUNBALACENED(int uNBALACENED) {
			UNBALACENED = uNBALACENED;
		}
		public int getBALACENED() {
			return BALACENED;
		}
		public void setBALACENED(int bALACENED) {
			BALACENED = bALACENED;
		}
		
		//********************functions*********************//
		/**
		 * add a route to the constraint 
		 * @param layer
		 * @param consrtaintRoute
		 */
		public void addConstraintLink(Layer layer,LinearRoute consrtaintRoute)
		{
			for(Link link:consrtaintRoute.getLinklist())
			{
				this.getExcludedLinklist().add(link);
				Link anotherlink = layer.findlink(link.getNodeB(), link.getNodeA());
				this.getExcludedLinklist().add(anotherlink);
			}
		}
		
		public void addCostraintLink(Layer layer)
		{
			  
		 
		    HashMap<String,Link>mapLink=layer.getLinklist();
	        Iterator<String>iterLink=mapLink.keySet().iterator();
	        while(iterLink.hasNext()){
	       	
	        	Link link=(Link)(mapLink.get(iterLink.next()));
	        	if(!this.getExcludedLinklist().contains(link))
	        	{
	        		//check the status that more than one idle wavlength in the link
	        		boolean avaWave = false;
	        		for(Cwavelength wave:link.getWC())
	        		{
	        			if(wave.getStatus()==0)
	        			{
	        				avaWave = true;
	        				break;
	        			}
	        		}
	        		if(!avaWave)
	        		{
	        			this.getExcludedLinklist().add(link);
	    				Link anotherlink = layer.findlink(link.getNodeB(), link.getNodeA());
	    				this.getExcludedLinklist().add(anotherlink);
	        		}
	        	}
	        }
		}
	   
	   
}
