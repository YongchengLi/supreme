package Network;

//import java.io.BufferedReader;
//import java.io.IOException;
import functionAlorithms.Algorithm;
import general.*;
import groupwork.SearchConstraint;
import groupwork.TestSerach;
import groupwork.pathSearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;



import rapidDataEvacuation.DataCenter;
import rapidDataEvacuation.DisasterRegion;
import subgraph.LinearRoute;

//import com.sun.java.util.jar.pack.Package.File;

public class Layer extends CommonObject{
    private HashMap<String,Node>nodelist=null;
    private HashMap<String,Link>linklist=null;
    private HashMap<String,Nodepair>nodepairlist=null;
  
	public HashMap<String, Node> getNodelist() {
		return nodelist;
	}
	public void setNodelist(HashMap<String, Node> nodelist) {
		this.nodelist = nodelist;
	}
	public HashMap<String, Link> getLinklist() {
		return linklist;
	}
	public void setLinklist(HashMap<String, Link> linklist) {
		this.linklist = linklist;
	}
	public HashMap<String, Nodepair> getNodepairlist() {
		return nodepairlist;
	}
	public void setNodepairlist(HashMap<String, Nodepair> nodepairlist) {
		this.nodepairlist = nodepairlist;
	}
	public Layer(String name, int index, String comments) {
		super(name, index, comments);
		// TODO Auto-generated constructor stub
      this.nodelist=new HashMap<String, Node>(40);
      this.linklist=new HashMap<String, Link>(100);
      this.nodepairlist=new HashMap<String, Nodepair>(800);
	}
	/*
	 * add node to the layer
	 */
	public void addNode(Node node){
		this.nodelist.put(node.getName(), node);
		node.setAssociatedLayer(this);
    }
	/*
	 * remove node from the layer and remvoe the link about the node in the same time
	 */
	public void removeNode(String nodename){
		Node node=this.nodelist.get(nodename);
		node.setAssociatedLayer(null);
		String linkname="";
		for(int i=0;i<node.getNeinodelist().size();i++){
			Node nodeB=node.getNeinodelist().get(i);
			if(node.getIndex()<nodeB.getIndex()){
				linkname=node.getName()+"-"+nodeB.getName();
				this.linklist.remove(linkname);
			}
			else{
				linkname=nodeB.getName()+"-"+node.getName();
				this.linklist.remove(linkname);
			}
		}
		this.nodelist.remove(nodename);
	}
	/*
	 * add link to the layer
	 */
	 public void addlink(Link link){
		 this.linklist.put(link.getName(), link);
		 link.setAssociatedLayer(this);
	 }
	 /*
	  * remove link from the layer
	  */
	 public void removeLink(String linkname){
		 this.linklist.get(linkname).setAssociatedLayer(null);
		 this.linklist.remove(linkname);
		  }
	 /*
	  * add nodepair to the layer
	  */
	 public void addNodepair(Nodepair nodepair){
		 this.nodepairlist.put(nodepair.getName(), nodepair);
		 nodepair.setAssociatedLayer(this);
	 }
	 /*
	  * remove nodepair from the layer
	  */
	 public void removeNodepair(String nodepairname){
		 this.nodepairlist.get(nodepairname).setAssociatedLayer(null);
		 this.nodepairlist.remove(nodepairname);
	 }
	 /*
	  * get number of all the nodes ,links,nodepairs
	  */
	 public int getNode_num(){
		 return this.nodelist.size();
	 }
	 public int getLink_num(){
		 return this.linklist.size();
	 }
	 public int getNodepair_num(){
		 return this.nodepairlist.size();
	 }
	 /*
	  * copy all the nodes 
	  */
	 public void copyNodes(Layer layer){
		 HashMap<String ,Node>map=layer.getNodelist();
		 Iterator<String>iter1=map.keySet().iterator();
		 while(iter1.hasNext()){
			 Node nodeA=(Node)(map.get(iter1.next()));
			 Node nodeB=new Node(nodeA.getName(),nodeA.getIndex(),"",this,nodeA.getX(),nodeA.getY());
	         this.addNode(nodeB);
		 }
		 
	 }
	 /*
	  * Create nodepair based on the existing nodelist
	  */
	 public void generateNodepaairs(){
		 HashMap<String,Node>map=this.getNodelist();
		 HashMap<String,Node>map2=this.getNodelist();
		 Iterator<String>iter1=map.keySet().iterator();
		 while(iter1.hasNext()){
			 Node node1=(Node)(map.get(iter1.next()));
			 Iterator<String>iter2=map2.keySet().iterator();
			 while(iter2.hasNext()){
				 Node node2=(Node)(map.get(iter2.next()));
				 if(!node1.equals(node2)){
					 if(node1.getIndex()<node2.getIndex()){
						 String name=node1.getName()+"-"+node2.getName();
						 int index=this.nodepairlist.size();
						 Nodepair nodepair=new Nodepair(name,index,"",this,node1,node2);
						 this.addNodepair(nodepair);
					 }
					
					 
				 }
			 }
		 }
	 }
	 public Link findlink(Node nodeA,Node nodeB){
				String name;
			    name = nodeA.getName()+"-"+nodeB.getName();
				return this.getLinklist().get(name);
			}
	 
	 public Link findSymLink(Link link){
		 return this.findlink(link.getNodeB(), link.getNodeA());
	 }
	 
	 
	 public Node findNode(String name,Layer layer){
		  HashMap<String,Node>map=layer.getNodelist();
          Iterator<String>iter=map.keySet().iterator();
          Node currentnode=null;
          while(iter.hasNext()){
       	    currentnode=(Node)(map.get(iter.next()));
       	   if(currentnode.getName().endsWith(name))break;
          }
		 return currentnode; 
	 }
	 
	 public Node findNode(String name){
		  HashMap<String,Node>map=this.getNodelist();
         Iterator<String>iter=map.keySet().iterator();
         Node currentnode=null;
         while(iter.hasNext()){
      	    currentnode=(Node)(map.get(iter.next()));
      	   if(currentnode.getName().endsWith(name))break;
         }
		 return currentnode; 
	 }
	 public Nodepair findNodepair(String name){
		 HashMap<String, Nodepair>map=this.getNodepairlist();
         Iterator<String>iter=map.keySet().iterator();
         Nodepair currentnodepair=null;
         while(iter.hasNext()){
      	    currentnodepair=(Nodepair)(map.get(iter.next()));
      	   if(currentnodepair.getName().endsWith(name))break;
         }
		 return currentnodepair; 
	 }
	 public Nodepair findNodepair(Node nodeA,Node nodeB){
		 HashMap<String, Nodepair>map=this.getNodepairlist();
         Iterator<String>iter=map.keySet().iterator();
         Nodepair currentnodepair=null;
         while(iter.hasNext()){
      	    currentnodepair=(Nodepair)(map.get(iter.next()));
      	   if(currentnodepair.getSrcNode()==nodeA&&currentnodepair.getDesNode()==nodeB||currentnodepair.getSrcNode()==nodeB&&currentnodepair.getDesNode()==nodeA)break;
         }
		 return currentnodepair; 
	 }
	 
	 
	public void Output_shortpath(){
		 HashMap<String, Nodepair>map=this.getNodepairlist();
         Iterator<String>iter=map.keySet().iterator();
         Nodepair currentnodepair=null;
         while(iter.hasNext()){
        	currentnodepair=(Nodepair)(map.get(iter.next()));
        	System.out.println("===================="+currentnodepair.getName()+"=============");
       	    LinearRoute newroute= currentnodepair.getLinearlist().get(currentnodepair.getLinearlist().size()-1);
       	    newroute.OutputRoute_node(newroute);
           }
	}
	public void OutputAllLink(){
		 HashMap<String, Link>map=this.getLinklist();
        Iterator<String>iter=map.keySet().iterator();
       
        while(iter.hasNext()){
       	Link link=(Link)(map.get(iter.next()));
       	System.out.println(link.getName()+" "+link.getCost());
          }
	}
	public void SetCostofAllLink(){
		 HashMap<String, Link>map=this.getLinklist();
       Iterator<String>iter=map.keySet().iterator();
      
       while(iter.hasNext()){
      	Link link=(Link)(map.get(iter.next()));
      	double cost = 10000/link.getResidualCapacity();
      	link.setCost(cost);
         }
	}
     public void Output_shortpath(String write_name ){
    		 HashMap<String, Nodepair>map=this.getNodepairlist();
             Iterator<String>iter=map.keySet().iterator();
             Nodepair currentnodepair=null;
             file_out_put file=new file_out_put();
             while(iter.hasNext()){
            	currentnodepair=(Nodepair)(map.get(iter.next()));
            	System.out.println("===================="+currentnodepair.getName()+"=============");
            	file.filewrite(write_name,"===================="+currentnodepair.getName()+"=============");
           	    LinearRoute newroute= currentnodepair.getLinearlist().get(currentnodepair.getLinearlist().size()-1);
           	    newroute.OutputRoute_node(newroute,write_name);
               }
             
	}
	public void clear_cost(){
		HashMap<String,Node>map=this.getNodelist();
        Iterator<String>iter=map.keySet().iterator();
        while(iter.hasNext()){
       	    Node node=(Node)(map.get(iter.next()));
			node.setCost_from_src(Constant.maxium);
		}
	}
	public void Output_link(String write_name){
		 HashMap<String,Link>map=this.getLinklist();
         Iterator<String>iter=map.keySet().iterator();
         file_out_put file=new file_out_put();
         while(iter.hasNext()){
        	 Link link=(Link)(map.get(iter.next()));
        	 if(link.getNodeA().getIndex()<link.getNodeB().getIndex()){
        	 System.out.println(link.getName()+"_"+link.getCost());
        	 file.filewrite(write_name,link.getName()+"_"+link.getCost());
        	 }
         }

	}
	
	
	
	
	
	
	
	public void Output_link(){
		 HashMap<String,Link>map=this.getLinklist();
        Iterator<String>iter=map.keySet().iterator();
        file_out_put file=new file_out_put();
        while(iter.hasNext()){
       	 Link link=(Link)(map.get(iter.next()));
       	 if(link.getNodeA().getIndex()<link.getNodeB().getIndex()){
       	 System.out.println(link.getName()+"_"+link.getCost());
       	 
       	 }
        }

	}
	public void Output_link_W(){
		 HashMap<String,Link>map=this.getLinklist();
         Iterator<String>iter=map.keySet().iterator();
        
         while(iter.hasNext()){
        	 Link link=(Link)(map.get(iter.next()));
        	 if(link.getNodeA().getIndex()<link.getNodeB().getIndex()){
        	 System.out.println(link.getName()+"_"+link.getW());
        	
        	 }
         }
	}
	public void Output_W(){
		HashMap<String,Link>map=this.getLinklist();
        Iterator<String>iter=map.keySet().iterator();
        while(iter.hasNext()){
       	Link link=(Link)(map.get(iter.next()));
       	if(link.getNodeA().getIndex()<link.getNodeB().getIndex()){
       		System.out.println(link.getName());
	        link.Output_W_Ocppuy();
       	 }
        }
	}
   public void readTopology(String filename){
		
		String[] data = new String[10];
		File file = new File(filename);
		BufferedReader bufRdr = null;
		try {
			bufRdr = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		int col = 0;
		try {
			line = bufRdr.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//read the first title line
		//read each line of text file
		try {
			boolean link = false;
			while((line = bufRdr.readLine()) != null){
				StringTokenizer st = new StringTokenizer(line,",");
				while (st.hasMoreTokens()){
					data[col] = st.nextToken();
					col++;
					
				}
				col=0;
				String name = data[0];
				if(name.equals("Link")){
					link=true;						
				}
				//read nodes
				if(!link)//node operation
				{
					int x = Integer.parseInt(data[1]);
					int y = Integer.parseInt(data[2]);					
					int index = this.getNodelist().size();
					Node newnode = new Node(name, index, "", this,x,y);
					this.addNode(newnode);
				
				}
				else{ //link operation
					if(!(name.equals("Link"))){
						Node nodeA = this.getNodelist().get(data[1]);						
						Node nodeB = this.getNodelist().get(data[2]);
						double length = Double.parseDouble(data[3]);
						double cost = Double.parseDouble(data[4]);
						int index = this.getLinklist().size();
						/*if(nodeA.getIndex()<nodeB.getIndex()){
							name = nodeA.getName()+"-"+nodeB.getName();
						}
						else{
							name = nodeB.getName()+"-"+nodeA.getName();
						}
						*/
						String name1=nodeA.getName();
						String name2=nodeB.getName();
						Link name1_link=new Link(name1+"-"+name2,index,"",this,this.getNodelist().get(name1),this.getNodelist().get(name2),length, cost);
						this.addlink(name1_link);
						Link name2_link=new Link(name2+"-"+name1,++index,"",this,this.getNodelist().get(name2),this.getNodelist().get(name1),length, cost);
					    this.addlink(name2_link);
						
						
						//update the neighbor node list
						nodeA.addNeiNode(nodeB);
						nodeB.addNeiNode(nodeA);
					}					
				}				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		try {
			bufRdr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
   
   public void readTopologywithDataCenter(String filename){
		
		String[] data = new String[10];
		File file = new File(filename);
		BufferedReader bufRdr = null;
		try {
			bufRdr = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		int col = 0;
		try {
			line = bufRdr.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//read the first title line
		//read each line of text file
		try {
			boolean link = false;
			while((line = bufRdr.readLine()) != null){
				StringTokenizer st = new StringTokenizer(line,",");
				while (st.hasMoreTokens()){
					data[col] = st.nextToken();
					col++;
					
				}
				col=0;
				String name = data[0];
				if(name.equals("Link")){
					link=true;						
				}
				//read nodes
				if(!link)//node operation
				{
					int x = Integer.parseInt(data[1]);
					int y = Integer.parseInt(data[2]);	
					int keyDataCenter = Integer.parseInt(data[3]);
					int damgedStatus = Integer.parseInt(data[4]);
					int index = this.getNodelist().size();
					Node newnode = new Node(name, index, "", this,x,y);
					if(keyDataCenter==1)
					{
						newnode.setIsDataCenter(true);
						DataCenter dataCenter = new DataCenter("dataCenter", index, "the datacenter in current node");
					    newnode.setDataCenter(dataCenter);		 
					    dataCenter.setAssociatedNode(newnode);
					
					}
					else
						newnode.setIsDataCenter(false);
					
					if(damgedStatus==1)
						newnode.setDamageRisk(1);
					else
						newnode.setDamageRisk(0);
						
					this.addNode(newnode);
				
				}
				else{ //link operation
					if(!(name.equals("Link"))){
						Node nodeA = this.getNodelist().get(data[1]);						
						Node nodeB = this.getNodelist().get(data[2]);
						double length = Double.parseDouble(data[3]);
						double cost = Double.parseDouble(data[4]);
						int index = this.getLinklist().size();
						/*if(nodeA.getIndex()<nodeB.getIndex()){
							name = nodeA.getName()+"-"+nodeB.getName();
						}
						else{
							name = nodeB.getName()+"-"+nodeA.getName();
						}
						*/
						String name1=nodeA.getName();
						String name2=nodeB.getName();
						Link name1_link=new Link(name1+"-"+name2,index,"",this,this.getNodelist().get(name1),this.getNodelist().get(name2),length, cost);
						this.addlink(name1_link);
						Link name2_link=new Link(name2+"-"+name1,++index,"",this,this.getNodelist().get(name2),this.getNodelist().get(name1),length, cost);
					    this.addlink(name2_link);
						
						
						//update the neighbor node list
						nodeA.addNeiNode(nodeB);
						nodeB.addNeiNode(nodeA);
					}					
				}				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		try {
			bufRdr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
   
   public void CopyLayer(Layer layer){
	    HashMap<String,Node>Copynodelist=new HashMap<String, Node>();
	    
	    HashMap<String,Node>map=layer.getNodelist();
        Iterator<String>iter=map.keySet().iterator();
        while(iter.hasNext()){
     	   Node node=(Node)(map.get(iter.next()));
     	   Copynodelist.put(node.getName(), node);
  		  
        }
	    this.setNodelist(Copynodelist);
	    
	    HashMap<String,Link>Copylinklist=new HashMap<String, Link>();
	    HashMap<String,Link>mapLink=this.getLinklist();
        Iterator<String>iterLink=mapLink.keySet().iterator();
      
        while(iterLink.hasNext()){
       	 Link link=(Link)(mapLink.get(iterLink.next()));
       	 
       	 Copylinklist.put(link.getName(), link);
        }
	    this.setLinklist(Copylinklist);
	    
	    
	    HashMap<String,Nodepair>nodepairlist=null;
   }
   
   
    /**
     * search the shortestpath
     */
	public void shortestPath(){
		HashMap<String,Nodepair>map1=this.getNodepairlist();
       Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			String name1=nodepair.getSrcNode().getName();
	        String name2=nodepair.getDesNode().getName();
	        LinearRoute newRoute=new LinearRoute(name1+"---"+name2,0,"",Constant.WORKING);
	        Constraint constraint = new Constraint();
	        pathSearch workpathsearch=new pathSearch();
	        workpathsearch.SearchPathWithDijkstras(nodepair.getSrcNode(), nodepair.getDesNode(), this, newRoute,constraint);
	        nodepair.addRoute(newRoute);
		}
	}
	
	
	  /**
     * search the shortestpath
     */
	public void kshortestPath(int K){
	   HashMap<String,Nodepair>map1=this.getNodepairlist();
       Iterator<String>iter1=map1.keySet().iterator();
		while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			String name1=nodepair.getSrcNode().getName();
	        String name2=nodepair.getDesNode().getName();
	       
	        ArrayList<LinearRoute> routelist = new ArrayList<LinearRoute>();
	        pathSearch workpathsearch=new pathSearch();
	        workpathsearch.Kshortest(nodepair.getSrcNode(), nodepair.getDesNode(), this, K,routelist);
	        nodepair.setLinearlist(routelist);
		}
	}
	
	
	/**
     * search the shortestpath in IpLayer
     */
	public LinearRoute shortestPathInIpLayer(Nodepair nodepair,Constraint constraint){
			
		String name1=nodepair.getSrcNode().getName();
        String name2=nodepair.getDesNode().getName();
        LinearRoute newRoute=new LinearRoute(name1+"---"+name2,0,"",Constant.WORKING);
        pathSearch workpathsearch=new pathSearch();
        workpathsearch.SearchPathWithDijkstrasInIpLayer(nodepair.getSrcNode(), nodepair.getDesNode(), this, newRoute,constraint);
        return newRoute;
		
	}
   
	public void clear(){
		this.setTotalcostOfIpRouterPort(0);
		this.setTotalcostOfRegen(0);
		HashMap<String,Nodepair>map1=this.getNodepairlist();
	    Iterator<String>iter1=map1.keySet().iterator();
	    while(iter1.hasNext()){ 
			Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
			nodepair.setAssociatedLightPathList(null);
			nodepair.setServedCapacity(0);
			nodepair.setUnservedCapacity(nodepair.getTrafficLoad());
			ArrayList<LightPath> associatedLightPathList = new ArrayList<LightPath>();
			nodepair.setAssociatedLightPathList(associatedLightPathList);
	    }  
	    
		HashMap<String,Link>mapLink=this.getLinklist();
	    Iterator<String>iterLink=mapLink.keySet().iterator();
	    while(iterLink.hasNext()){ 
	    
	    	Link link = (Link)(mapLink.get(iterLink.next()));
	    	link.Init_W();
	    	for(Slot slot:link.getSlotList())
	    	{
	    		slot.setStatus(0);
	    		slot.setAssociatedNodepair(null);
	    	}
	    	
	    	
	    }
	    
	}
	
	private double TotalcostOfIpRouterPort = 0;
	private double TotalcostOfRegen = 0;
    private double Totalcost = 0;
    private double TotalSum1 = 0;
    private double TotalSum2 = 0;
    private double TotalSum3 = 0;
    
	public double getTotalcostOfIpRouterPort() {
		return TotalcostOfIpRouterPort;
	}
	public void setTotalcostOfIpRouterPort(double totalcostOfIpRouterPort) {
		TotalcostOfIpRouterPort = totalcostOfIpRouterPort;
	}
	public double getTotalcostOfRegen() {
		return TotalcostOfRegen;
	}
	public void setTotalcostOfRegen(double totalcostOfRegen) {
		TotalcostOfRegen = totalcostOfRegen;
	}
	public double getTotalcost() {
		return Totalcost;
	}
	public void setTotalcost(double totalcost) {
		Totalcost = totalcost;
	}
	public double getTotalSum1() {
		return TotalSum1;
	}
	public void setTotalSum1(double totalSum1) {
		TotalSum1 = totalSum1;
	}
	public double getTotalSum2() {
		return TotalSum2;
	}
	public void setTotalSum2(double totalSum2) {
		TotalSum2 = totalSum2;
	}
	public double getTotalSum3() {
		return TotalSum3;
	}
	public void setTotalSum3(double totalSum3) {
		TotalSum3 = totalSum3;
	}
	
	  public void LayerCopy(Layer layer){
		  
		  this.getNodepairlist().clear();
		  HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
	      Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();
		  while(iter_Nodepair.hasNext()){ 
				Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));	
			    
				 Nodepair newnodepair=new Nodepair(nodepair.getName(),nodepair.getIndex(),"",this,nodepair.getSrcNode(),nodepair.getDesNode());
				newnodepair.setUnservedCapacity(nodepair.getUnservedCapacity());
				newnodepair.setTrafficLoad(nodepair.getTrafficLoad());
				 ArrayList<LightPath> newlightpathlist = new ArrayList<LightPath>();
				 for(int i = 0; i < nodepair.getAssociatedLightPathList().size(); i++)    			
				 {	    		
					 LightPath lightpath  = nodepair.getAssociatedLightPathList().get(i);	    	
					 LightPath newlightpath = new LightPath(lightpath.getName(), lightpath.getIndex(), lightpath.getComments(), newnodepair);
					 
					 newlightpath.setAssociatedNodepair(newnodepair);
					 newlightpath.setAssociatedWavelength(lightpath.getAssociatedWavelength());
					 newlightpath.setCost(lightpath.getCost());
					 newlightpath.setCostofIpRouterPort(lightpath.getCostofIpRouterPort());
					 newlightpath.setCostOfRegen(lightpath.getCostOfRegen());
					 
					 newlightpath.setFormatModule_Capacity(lightpath.getFormatModule_Capacity());
					 newlightpath.setFormatModule_Name(lightpath.getFormatModule_Name());
					 newlightpath.setAssociatedSoltUnit(lightpath.getAssociatedSoltUnit());
					 newlightpath.setNumOfIpRouterPort(lightpath.getNumOfIpRouterPort());
					 newlightpath.setNumOfRegen(lightpath.getNumOfRegen());
					 
					 newlightpath.setOccupiedCapacity(lightpath.getOccupiedCapacity());
					 newlightpath.setPhysicPath(lightpath.getPhysicPath());
					 newlightpath.setTrafficGrooming(lightpath.isTrafficGrooming());
					 newlightpath.setIPLayerRoute_Lightpath_Trafficgrooming(lightpath.getIPLayerRoute_Lightpath_Trafficgrooming());
					 newlightpathlist.add(newlightpath);
					 
				 }
				 
				 newnodepair.setAssociatedLightPathList(newlightpathlist);
				 newnodepair.setLinearlist(nodepair.getLinearlist());
				 newnodepair.setCostOfIpRouterWithBPSK(nodepair.getCostOfIpRouterWithBPSK());
				 newnodepair.setCostOfIpRouterWithQPSK(nodepair.getCostOfIpRouterWithQPSK());
				 newnodepair.setCostOfIpRouterWithQAM(nodepair.getCostOfIpRouterWithQAM());
				 newnodepair.setCostOfRegenWithBPSK(nodepair.getCostOfRegenWithBPSK());
				 newnodepair.setCostOfRegenWithQPSK(nodepair.getCostOfRegenWithQPSK());
				 newnodepair.setCostOfRegenWithQAM(nodepair.getCostOfRegenWithQAM());
				 newnodepair.setNumofIProuter_BPSK(nodepair.getNumofIProuter_BPSK());
				 newnodepair.setNumofIProuter_QPSK(nodepair.getNumofIProuter_QPSK());
				 newnodepair.setNumofIProuter_QAM(nodepair.getNumofIProuter_QAM());
				 newnodepair.setNumofRegen_BPSK(nodepair.getNumofRegen_BPSK());
				 newnodepair.setNumofRegen_QPSK(nodepair.getNumofRegen_QPSK());
				 newnodepair.setNumofRegen_QAM(nodepair.getNumofRegen_QAM());
				 
				 
				 this.addNodepair(newnodepair);
		    
	        }
		  this.setNodelist(layer.getNodelist());
		  this.getLinklist().clear();
		  
		  HashMap<String,Link>LinkMap=layer.getLinklist();
	      Iterator<String>iter_Link=LinkMap.keySet().iterator();
		  while(iter_Link.hasNext()){ 
			  Link link=(Link)(LinkMap.get(iter_Link.next()));	
			  
			  Link newlink = new Link(link.getName(), link.getIndex(), link.getComments(), this, link.getNodeA(), link.getNodeB(), link.getLength(), link.getCost());
			  ArrayList<Cwavelength> wavelist = new ArrayList<Cwavelength>();
			  for(int i = 0; i < link.getWC().size(); i++)
			  {
				  Cwavelength wave = new Cwavelength();
				  wave.setStatus(link.getWC().get(i).getStatus());
				  wavelist.add(wave);
			  }
			  newlink.setWC(wavelist);
			  this.addlink(newlink);
		  }
	   }
	  
		/**
		 * 
		 */
		public void InitWavelengthForLayer(int wavelengthNum)
		{
			HashMap<String,Link>mapLink=this.getLinklist();
		    Iterator<String>iterLink=mapLink.keySet().iterator();
		    while(iterLink.hasNext()){ 
	    		Link link=(Link)(mapLink.get(iterLink.next()));
	    		link.add_W(wavelengthNum);
	    		link.Init_W();
	    	}
		}
		
		public void InitSlotForLayer(int MaxSlotNum)
		{
		 
			HashMap<String,Link>map=this.getLinklist();
	    	Iterator<String>iter=map.keySet().iterator();	
	    	 while(iter.hasNext()){     		
	    		 Link link=(Link)(map.get(iter.next()));
	    		 link.addSlot(MaxSlotNum);
	    	 }
		}
		// delay time
		private double AverageDelayTime = 0;

		public double getAverageDelayTime() {
			return AverageDelayTime;
		}
		public void setAverageDelayTime(double averageDelayTime) {
			AverageDelayTime = averageDelayTime;
		}

		
		
		private int wavelengthNum = 0;

		public int getWavelengthNum() {
			return wavelengthNum;
		}
		public void setWavelengthNum(int wavelengthNum) {
			this.wavelengthNum = wavelengthNum;
		}
		
		//*********************Evacuation************************//
		public void updateLinkCost()
		{
			HashMap<String,Link>mapLink=this.getLinklist();
		    Iterator<String>iterLink=mapLink.keySet().iterator();
		    while(iterLink.hasNext()){ 
	    		Link link=(Link)(mapLink.get(iterLink.next()));
	    		int idle = 0;
	    		for(Cwavelength wave:link.getWC()){
	    			if(wave.getStatus()==0)
	    				idle++;
	    		}
	    		double cost = (link.getWC().size()-idle)/link.getWC().size();
	    		cost = (double)(Math.round(cost*100)/100.0);
	    		
	    		link.setCost(cost);
	    		
	    	}
		}
		
		
		
	private ArrayList<DisasterRegion> disasterRegionList = new ArrayList<DisasterRegion>();

	public ArrayList<DisasterRegion> getDisasterRegionList() {
		return disasterRegionList;
	}
	public void setDisasterRegionList(ArrayList<DisasterRegion> disasterRegionList) {
		this.disasterRegionList = disasterRegionList;
	}
	
	
	//*********************ULL*********************************//
	/**
	 * find the traversed num of each link 
	 */
	
	public void setLinkTraversedNum(){
		 
		
		HashMap<String, Nodepair>map=this.getNodepairlist();
        
		Iterator<String>iter=map.keySet().iterator();
        
		Nodepair currentnodepair=null;
        
		while(iter.hasNext()){
        
			currentnodepair=(Nodepair)(map.get(iter.next()));
        	
		//	System.out.println("===================="+currentnodepair.getName()+"=============");
       	   
			for(LinearRoute route: currentnodepair.getLinearlist())
			{
		//		route.OutputRoute_link();
				for(Link link:route.getLinklist())
				{
					
					link.setTraversedNum(link.getTraversedNum()+1);
				    this.findlink(link.getNodeB(), link.getNodeA()).setTraversedNum(this.findlink(link.getNodeB(), link.getNodeA()).getTraversedNum()+1);
			//		System.out.println(link.getName()+" "+link.getTraversedNum());
				}
			}
			break;
           
		}
	}
		
	
	public void CalculateLinkOSNR()
    {
    	int totalAmpifierNum=0;
    	HashMap<String,Link>map=this.getLinklist();
        Iterator<String>iter=map.keySet().iterator();
        while(iter.hasNext()){
       	    Link currentlink=(Link)(map.get(iter.next()));
       	    if(currentlink.getNodeA().getIndex()<currentlink.getNodeB().getIndex())
       	    {
       	 
       	    	int number=(int) Math.ceil(currentlink.getCost()/80);   		
       	    	int AmpifierNumber=number-1;   		
       	    	AmpifierNumber=AmpifierNumber+2;	        
       	    	totalAmpifierNum+=AmpifierNumber;
       	    	currentlink.setAmplifierNum(AmpifierNumber);
       	    	this.findlink(currentlink.getNodeB(), currentlink.getNodeA()).setAmplifierNum(AmpifierNumber);
       	    	//锟斤拷锟斤拷每锟斤拷锟斤拷锟斤拷//
       	    	double KmperAmpifier=currentlink.getCost()/number;
       	    	double AmpifierGain=KmperAmpifier*0.25;
       	    	currentlink.setAmpifierGain(AmpifierGain);
       	    	this.findlink(currentlink.getNodeB(), currentlink.getNodeA()).setAmpifierGain(AmpifierGain);
       	        Algorithm work=new Algorithm();
       	    	double NF0=7.12;
       	       // System.out.println("NF0: "+NF0);
       	    	double NF=0;
       	    	if(AmpifierGain<=15)
       	    	{
       	 	    
       	    		NF=work.Netwton("EDFA_TYPE/15.csv",23,AmpifierGain);
       		    	
       	    	}    		    
       	    	else
       		    
       	    	{
       	    		NF=work.Netwton("EDFA_TYPE/22.csv",16,AmpifierGain);
       	    		//NF=5.46;
       	    	}
      
       	    	double LinkOSNR=0;
       	    	for(int i=1;i<=AmpifierNumber;i++)
       	    	{
       	    		double ReducedUnit=0;
       	    		if(i==1){
      	    			//转锟斤拷锟斤拷位锟斤拷锟�
       	    			ReducedUnit=-58+15+NF0;
       	    			ReducedUnit=Math.pow(10, ReducedUnit/10);	
       	    			ReducedUnit=1/ReducedUnit;
       	    		} 			
       	    		else{
       	    			ReducedUnit=-58+AmpifierGain+NF;
       	    			ReducedUnit=Math.pow(10, ReducedUnit/10);
       	    			ReducedUnit=1/ReducedUnit;
       	    		}
       	    		
   	    			LinkOSNR=LinkOSNR+1/ReducedUnit;
       	    	}
       	    	currentlink.setOSNR(1/LinkOSNR);
       	    	this.findlink(currentlink.getNodeB(), currentlink.getNodeA()).setOSNR(1/LinkOSNR);
       	    }  
        }
        System.out.println("Toatal Number of ampifier: "+totalAmpifierNum);
    }
}
