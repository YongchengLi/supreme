package workPackage;

import general.Constant;
import general.WavePlane;
import general.file_out_put;
import groupwork.TestSerach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import subgraph.LinearRoute;



import Network.Layer;
import Network.Link;
import Network.Node;
import Network.Nodepair;

public class LayerWavlengthRoutingAlgorithm {


	
	public int FindOccupy(Layer layer){
		
		int OccupyWavNum=0;
		HashMap<String,Link>map=layer.getLinklist();
    	Iterator<String>iter=map.keySet().iterator();
    	while(iter.hasNext()){
    	        Link link=(Link)(map.get(iter.next()));    
    	        if(link.getNodeA().getIndex()<link.getNodeB().getIndex()){
    	        	 for(int index=0;index<link.getWC().size();index++){
    	    	        	if(link.getWC().get(index).getStatus()==1)
    	    	        		OccupyWavNum++;
    	    	        }
    	        }    	       
    	}		
		return OccupyWavNum;	
	}
	
	
	public void WavePlaneAgorithm(Layer layer, ArrayList<String> nodepairlist,int WavelengthNum,int requiredNum){
	
		/*
		 * set up the waveplanelist for the layer
		 */
		ArrayList<WavePlane>WavePlanelist=new ArrayList<WavePlane>();		
		for(int i=0;i<WavelengthNum;i++)
		{
		    WavePlane currentWavePlane=new WavePlane(" ", i, " ");
		    currentWavePlane.setNodelist(layer.getNodelist());
		    currentWavePlane.setLinklist(layer.getLinklist());
		    currentWavePlane.setNodepairlist(layer.getNodepairlist());
		    currentWavePlane.setAssociatedWavelength(i);
		    WavePlanelist.add(currentWavePlane);
		}
		/*
		 * Init the wavelength status of all the links
		 */
		HashMap<String,Link>map=layer.getLinklist();
    	Iterator<String>iter=map.keySet().iterator();
    	while(iter.hasNext()){
    		Link link=(Link)(map.get(iter.next()));
    		link.add_W(WavelengthNum);
    		link.Init_W();
    	}
    	//assgin the wavelength for each demand
		RWAForLayerWithWavePlane(layer,nodepairlist,WavePlanelist,WavelengthNum,requiredNum);
	}
	
	public void RWAForLayerWithWavePlane(Layer layer,ArrayList<String> nodepairlist,ArrayList<WavePlane> WavePlanelist,int WavelengthNum,int requiredNum){
		
    	int KeyValue=requiredNum;
    	
    	while(KeyValue>0){   		
    		
    		for(int ListIndex=0;ListIndex<nodepairlist.size();ListIndex++){
    			
    			String name=(String) nodepairlist.get(ListIndex);
        		Nodepair FindNodepair=null;
        		
        		
        		HashMap<String,Nodepair>mapRWA=layer.getNodepairlist();           
        		Iterator<String>iterRWA=mapRWA.keySet().iterator();     		
        		while(iterRWA.hasNext()){     		
        			Nodepair nodepair=(Nodepair)(mapRWA.get(iterRWA.next()));     			
        			if(nodepair.getName().endsWith(name)){
        				FindNodepair=nodepair;
        				break;
        			}
        		}
        		
        		if(FindNodepair!=null){    				
        			RWAforNodepairWithWavePlane(layer,WavePlanelist, WavelengthNum,requiredNum,FindNodepair);
        		}
        		
        		else{

        		}
    		}
    		KeyValue--;
    		
    	}
	}

	public void RWAforNodepairWithWavePlane(Layer layer,ArrayList<WavePlane> WavePlanelist,int WavelengthNum,int requiredNum,Nodepair nodepair){
		LinearRoute Route=null;
        TestSerach workSearch=new TestSerach();
		int WavelengthIndex=-1;
		for(WavePlane currentWavePlane:WavePlanelist)
		{
			LinearRoute newRoute=new LinearRoute("",	0, "", Constant.WORKING);
			
			currentWavePlane.InitWavePlaneLinks();
			
			workSearch.Dijkstras(nodepair.getSrcNode(), nodepair.getDesNode(),currentWavePlane, newRoute);
	
			if(newRoute.getLinklist().size()>0)
			{
				currentWavePlane.RemoveConstrantLinks();
				currentWavePlane.addConstrantLinks(newRoute);
				Route=newRoute;
				WavelengthIndex=currentWavePlane.getAssociatedWavelength();
				break;
			}
			else
			{
				currentWavePlane.RemoveConstrantLinks();
			}
		}
		if(WavelengthIndex>=0)
		{
			AssginmentWavelength_WP(layer,Route,WavelengthIndex);
		}
	}
	public static void AssginmentWavelength_WP(Layer layer,LinearRoute Route,int Wavelength)
	{
		
    	Node currentnode=Route.getNodelist().get(0);
	    for(Node node:Route.getNodelist()){
	    	if(!node.getName().equals(currentnode.getName())){		
	    		layer.findlink(currentnode, node).getWC().get(Wavelength).setStatus(1);
				layer.findlink(node, currentnode).getWC().get(Wavelength).setStatus(1);
				currentnode=node;
	    	}
	    }
	   
	}
}
