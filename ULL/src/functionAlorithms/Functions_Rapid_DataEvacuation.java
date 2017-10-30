package functionAlorithms;

import general.Block;
import general.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

import Network.Layer;
import Network.Link;
import Network.Node;
import Network.Nodepair;
import rapidDataEvacuation.Content;
import rapidDataEvacuation.DataCenter;
import rapidDataEvacuation.DisasterRegion;
import rapidDataEvacuation.Earthquake;
import rapidDataEvacuation.risk;
import subgraph.LinearRoute;

public class Functions_Rapid_DataEvacuation {

	
	public void setLinkCapacity(Layer layer,Random rand)
	{
		 HashMap<String,Link>map=layer.getLinklist();
		 Iterator<String>iterLink=map.keySet().iterator();
		 while(iterLink.hasNext()){
			 Link link=(Link)(map.get(iterLink.next()));
			 int linkCapacity = 0;
			 if(link.getNodeA().getIndex()<link.getNodeB().getIndex())
			 {
				 linkCapacity = rand.nextInt(Constant.maximumLinkCapacity-Constant.minimumLinkCapacity+1)+Constant.minimumLinkCapacity;
				 System.out.println(link.getName()+" "+linkCapacity);
				 linkCapacity = (int) (linkCapacity*Constant.percentageLinkCapacity);
				 link.setResidualCapacity(linkCapacity);
				 layer.findlink(link.getNodeB(), link.getNodeA()).setResidualCapacity(linkCapacity);
				 
				 link.getTimeCapacity().setMinimumResidualCapacity(linkCapacity);
				
			
				 layer.findlink(link.getNodeB(), link.getNodeA()).getTimeCapacity().setMinimumResidualCapacity(linkCapacity);
				 
				 System.out.println(link.getName()+" "+linkCapacity);
			 }
			 
		 }
	}
	/**
	 * Set the risk
	 * @param layer
	 * @param rand
	 */
	public void setLinkRisk(Layer layer,Random rand)
	{
		 HashMap<String,Link>map=layer.getLinklist();
		 Iterator<String>iterLink=map.keySet().iterator();
		 while(iterLink.hasNext()){
			 Link link=(Link)(map.get(iterLink.next()));
			 
			 if(link.getNodeA().getIndex()<link.getNodeB().getIndex())
			 {
				 double risk = (double)rand.nextInt(1000)/3000;
				 risk = (double)(Math.round(risk*1000)/1000.0);
				 link.setRisk(risk);
				 layer.findlink(link.getNodeB(), link.getNodeA()).setRisk(risk);
			 }
			 
		 }
	}
	/**
	 * Output the link risk
	 * @param layer
	 * @param rand
	 */
	public void outputLinkRisk(Layer layer,Random rand)
	{
		 HashMap<String,Link>map=layer.getLinklist();
		 Iterator<String>iterLink=map.keySet().iterator();
		 while(iterLink.hasNext()){
			 Link link=(Link)(map.get(iterLink.next()));
			 
			 if(link.getNodeA().getIndex()<link.getNodeB().getIndex())
			 {
				 System.out.println(link.getName()+" "+link.getRisk());
				 
			 }
			 
		 }
	}
	/**
	 * calculate the risk for all the route
	 * @param layer
	 */
	public void calRouteRiskAllRoute(Layer layer)
	{
		 HashMap<String ,Nodepair>map=layer.getNodepairlist();
		 Iterator<String>iterNodepair=map.keySet().iterator();
		 while(iterNodepair.hasNext()){
			 Nodepair nodepair=(Nodepair)(map.get(iterNodepair.next()));
			 
			 if(nodepair.getSrcNode().getIsDataCenter()&&nodepair.getDesNode().getIsDataCenter())
			 {
				
				 for(int i = 0; i<nodepair.getLinearlist().size();i++)
				 {
					 
					 LinearRoute route = nodepair.getLinearlist().get(i);
					 calRouteRiskRoute(route); 
				 
					
				 }
				// System.out.println();
			 }
		 }
	}
	
	/**
	 * calculate the risk for the route
	 * @param route
	 */
	public void calRouteRiskRoute(LinearRoute route)
	{
		double risk = 0;
		
		for(Link link:route.getLinklist())
		{
				
			//equation to calculate the protected for each link
			risk = risk+link.getRisk();
			
			
		}
		
	    
		// 2 
		risk = (double)(Math.round(risk*1000)/1000.0);
		
		route.setRisk(risk);
	}
	
	/**
	 * set the stroage for data center 
	 * @param layer
	 */
	public void setDataCenter(Layer layer){
		
		 int index = 0;
		 int maximumStroage = 102400;
		 int minimumStroage = 10240;
		 Random rand = new Random(1);
		 Random randStroage = new Random(2);
		 HashMap<String ,Node>map=layer.getNodelist();
		 Iterator<String>iterNode=map.keySet().iterator();
		 while(iterNode.hasNext()){
			 Node node=(Node)(map.get(iterNode.next()));
			 if(node.getIsDataCenter())
			 {
				 DataCenter dataCenter = new DataCenter("dataCenter", index, "the datacenter in current node");
			     node.setDataCenter(dataCenter);
			     int stroage = randStroage.nextInt(maximumStroage)%(maximumStroage-minimumStroage+1)+minimumStroage;
			     dataCenter.setStorage(stroage);
			     dataCenter.setAssociatedNode(node);
			     setContentForDataCenter(dataCenter,rand);
			     index++;
			 } 
		 }
	}

	/**
	 * set the storage for data center
	 * @param layer
	 */
	public void setStorageDataCenter(Layer layer){
		
		 int index = 0;
		 int maximumStroage = Constant.maximumStorage;
		 int minimumStroage = Constant.minimumStorage;
		 Random rand = new Random(1);
		 Random randStroage = new Random(2);
		 HashMap<String ,Node>map=layer.getNodelist();
		 Iterator<String>iterNode=map.keySet().iterator();
		 while(iterNode.hasNext()){
			 Node node=(Node)(map.get(iterNode.next()));
			 if(node.getIsDataCenter())
			 {
				 DataCenter dataCenter = node.getDataCenter();
			     int stroage = randStroage.nextInt(maximumStroage)%(maximumStroage-minimumStroage+1)+minimumStroage;
			     stroage = (int)(stroage*0.4);
			     dataCenter.setStorage(stroage);
			     dataCenter.setEmptyStorage(stroage);
			     dataCenter.setAssociatedNode(node);
			     setContentForDataCenter(dataCenter,rand);
			     index++;
			 } 
		 }
	}
	

	 /**
	  * set the content for data center
	  * @param dataCenter
	  * @param rand
	  */
	public void setContentForDataCenter(DataCenter dataCenter, Random rand){
		
		int maximumSize = Constant.maximumSize;
		int minimumSize = Constant.minimumSize;
		
		
		for(int i = 0; i < Constant.totalNumofConetnt; i++){
			
			Content content = new Content("content", i, " ");
			int aveSize = rand.nextInt(maximumSize)%(maximumSize-minimumSize+1)+minimumSize;
			content.setSize(aveSize);
			content.setAssociatedDataCenter(dataCenter);
			dataCenter.getContentlist().add(content);
		}
		
		
	}
	
	
	public double costEquation(double Timedelay,double risk)
	{
		double cost = 0;
		cost = Timedelay+Constant.weightValue*risk;
		cost = (double)(Math.round(cost*100)/100.0);
		System.out.println(Timedelay+" "+risk+" "+cost);
		return cost;
	}
	
	
	
	
	
	public void InputEarthquake(Earthquake earthquake,int x,int y,int strength){
		
		
		earthquake.setX(x);
		earthquake.setY(y);
		earthquake.setStrength(strength);
		
	}
	
	public void LocateDamagedDataCenter(Layer layer,Earthquake earthquake, ArrayList<Node> nodelist, ArrayList<DataCenter> dataCenterlist){
		
		HashMap<String ,Node>map=layer.getNodelist();
		
		Iterator<String>iterNode=map.keySet().iterator();
		
		while(iterNode.hasNext()){

			Node node=(Node)(map.get(iterNode.next()));
			 
			int xEar = earthquake.getX();
			int yEar = earthquake.getY();
			
			int xNo = node.getX();
			int yNo = node.getY();
			
			
			double Dis = Math.sqrt((xEar-xNo)*(xEar-xNo)+(yEar-yNo)*(yEar-yNo));
		    if(Dis<Constant.damagedDistance){
		    	
		    	nodelist.add(node);
		    	if(node.getIsDataCenter())
		    		dataCenterlist.add(node.getDataCenter());
		    	
		    }
		}
		
		
		
		
		
	
	}
	
	/**
	 * 
	 * @param layer
	 * @param earthquake
	 */
	public void CalculateDataCenterRisk(Layer layer,Earthquake earthquake){
		
		 HashMap<String ,Node>map=layer.getNodelist();
		 Iterator<String>iterNode=map.keySet().iterator();
		 while(iterNode.hasNext()){
			 Node node=(Node)(map.get(iterNode.next()));
			 
			 //check the node have a datacenter
			 if(node.getIsDataCenter())
			 {
				 //calcaute the risk data center
				 CalcuateRiskForNodeBasedDistance(earthquake, node,Constant.disLimit);
			 }
		 }
			 
			 
	}
	
	public void CalcuateRiskForNodeBasedDistance(Earthquake earthquake, Node node, int disLimit)
	{
		//calcuate the distance from the center of earthquake
		int xEar = earthquake.getX();
		int yEar = earthquake.getY();
		
		int xNo = node.getX();
		int yNo = node.getY();
		
		
		double Dis = Math.sqrt((xEar-xNo)*(xEar-xNo)+(yEar-yNo)*(yEar-yNo));
		double damaedrisk = Dis/disLimit;
		node.setDamageRisk(damaedrisk);
		
	}
		 
	public void readAvaDisasterRegion(String filename,Layer layer)
	{
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
			boolean disaster = false;
			DisasterRegion disasterRegion = null;
			int index=0;
			while((line = bufRdr.readLine()) != null){
				StringTokenizer st = new StringTokenizer(line,",");
				while (st.hasMoreTokens()){
					data[col] = st.nextToken();
					col++;
					
				}
				col=0;
				String name = data[0];
				
				
				if(name.equals("M")){
					
					disaster = true;	
					disasterRegion = new DisasterRegion("M"+index,index," ");
					layer.getDisasterRegionList().add(disasterRegion);
					index++;
				}
				//read nodes
				if(disaster)//node operation
				{
					if(data[0].equals("Node"))
					{
						Node node = layer.getNodelist().get(data[1]);	
						disasterRegion.getRiskNodelist().add(node);
						risk Noderisk = new risk();
						Noderisk.setDisterRegion(disasterRegion);
						
						double RiskProbability = Double.parseDouble(data[2]);
						Noderisk.setRiskProbability(RiskProbability);
						
						node.getRisklist().add(Noderisk);
					}
					else if(data[0].equals("Link"))
					{

						Link link = layer.getLinklist().get(data[1]);	
						Link anthorLink = layer.findlink(link.getNodeB(), link.getNodeA());
						
						
						
						disasterRegion.getRiskLinklist().add(link);
						disasterRegion.getRiskLinklist().add(anthorLink);
						
						risk Linkrisk = new risk();
						Linkrisk.setDisterRegion(disasterRegion);
						
						double RiskProbability = Double.parseDouble(data[2]);
						Linkrisk.setRiskProbability(RiskProbability);
						
						link.getRisklist().add(Linkrisk);
						anthorLink.getRisklist().add(Linkrisk);
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
	public void setDisasterRisk(Layer phyLayer)
	{
		HashMap<String,Nodepair>map1=phyLayer.getNodepairlist();
	    Iterator<String>iter1=map1.keySet().iterator();
	    while(iter1.hasNext()){ 			
	    	
	    	Nodepair nodepair=(Nodepair)(map1.get(iter1.next()));
	    //	System.out.println(nodepair.getName());
	    	for(LinearRoute route: nodepair.getLinearlist())
	    	{
	    		//route 
	    	//	System.out.println("==============");
	    		//route.OutputRoute_link();
	    	    double risk = 1;
	    		for(Link link:route.getLinklist())
	    		{
	    			//link risk
	    			for(risk linkRisk:link.getRisklist())
	    			{
	    				risk = risk*(1-linkRisk.getRiskProbability());
	    			}
	    		}
	    		for(Node node:route.getNodelist())
	    		{
	    			//Node risk
	    			for(risk nodeRisk:node.getRisklist())
	    			{
	    				risk = risk*(1-nodeRisk.getRiskProbability());
	    			}
	    		}
	    		
	    		risk = 1 -risk;
	    		
	    		route.setRisk(risk);
	    	//	System.out.println(route.getRisk());
	    		//break;
	    	}
	    }
	}
	
	/**
	 * 
	 * Calculate the delay of the path
	 * @param route
	 * @param conSize
	 * @param linkCapacity
	 * @return
	 */
	public static double Delay(LinearRoute route,int conSize,int linkCapacity)
	{
		//connection step
		//1.control-message processing delay
		double control_Message_Processing_delay = (route.getLinklist().size()+1)*Constant.processingDelay;
		
		double control_Message_Propagation_delay = route.getlength()*Constant.propgagationDelay;
	
		double switch_Configuration_delay = (route.getLinklist().size()+1)*Constant.swicthconfigurationDelay;
		
		double transmission_delay = (double)conSize*8/linkCapacity*1000000;
		
	    double progragation_delay = route.getlength()*Constant.propgagationDelay;
	    
		double Delay = control_Message_Processing_delay+control_Message_Propagation_delay
				+switch_Configuration_delay+transmission_delay+progragation_delay;			
		
		Delay = Delay/1000000;
	
		return Delay;
	}
	
	
	public static void OutputInformation(Layer layer)
	{
		HashMap<String ,Node>map=layer.getNodelist();
		Iterator<String>iterNode=map.keySet().iterator();		
		while(iterNode.hasNext()){

			Node node=(Node)(map.get(iterNode.next()));
			System.out.println("=========="+node.getName()+"============");
			if(node.getIsDataCenter()){
				for(Block block:node.getDataCenter().getBlocklist())
				{
					System.out.println(block.getOrdinaryContent().getName()+" "+block.getName());
				}
				
				
				
			}
			
		}
	}
}
