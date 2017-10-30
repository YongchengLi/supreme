package ULL;

import functionAlorithms.Functions_Rapid_DataEvacuation;
import functionAlorithms.Functions_Search_Route;
import functionAlorithms.Functions_ULL;
import functionAlorithms.TestFunctions;
import general.Constant;
import general.file_out_put;


import java.io.IOException;
import java.util.*;

import subgraph.LinearRoute;


import Network.*;


public class WriteDataFileofPathArc {
	
	
	public static int RandomSeed = 1;
	public static String ReadFile =  "6";
	public static String ReadFileName = ReadFile+".CSV";
//	public static String GenerateDataFileName =ReadFile+"_"+MaxNum+"_"+RandomSeed+"_Ten.dat";//生成的DataFile文件名称

	public static int K = 6; //The Number of shortest route

	public static double Q = 0.8;
	
	public static int F = 100;
	public static String GenerateDataFileName ="Result/"+ReadFile+"_"+Q+"_"+K+".dat";//生成的DataFile文件名称	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		 Run();
	
	}
	
	public static void Run()
	{
		
		Random rand = new Random(RandomSeed);
		/***********************read the Topology file**********************/
		System.out.println("read the file："+ReadFileName);		
		Layer layer=new Layer("worklayer", 0, "");
		layer.readTopology(ReadFileName);
		layer.generateNodepaairs();
	
		//FindRouteForNodeopair(layer,rand);
		FindRouteForNodeopairForAllRoute(layer, rand);
		
		/***********************Generate the datafile**********************/
		WriteFunction(layer,GenerateDataFileName);
		/********************************************************************/
		
	}
	public static void FindRouteForNodeopairForAllRoute(Layer layer,Random rand)
	{
		
		
		
		 HashMap<String,Nodepair> nodepairlist=layer.getNodepairlist();
         Iterator<String> iter=nodepairlist.keySet().iterator();
         while(iter.hasNext()){
        	
        	Nodepair nodepair=(Nodepair)(nodepairlist.get(iter.next()));
        	
        	Functions_Search_Route searchPath = new Functions_Search_Route();
     //   	searchPath.Kshortest(nodepair, layer, K);
        	
        	ArrayList<LinearRoute>  routelist = new ArrayList<LinearRoute>(); 
        	searchPath.findAllRoute(nodepair.getSrcNode(), nodepair.getDesNode(), layer, K, routelist);

        	//只将满足OSNR最低限制的路径存入
        	Functions_ULL ull=new Functions_ULL();
        	ArrayList<LinearRoute> newroutelist = new ArrayList<LinearRoute>();
        	
        	for(LinearRoute route:routelist)
        	{
        		
        		double c=ull.CalculateRouteOSNR(route);
        		
        		if(c>=14.03)
        		{
        			newroutelist.add(route);
        		}

        	}
        	nodepair.setLinearlist(newroutelist);

         }
         Collection<String> keyset= nodepairlist.keySet();  
	     List<String> list = new ArrayList<String>(keyset); 
	     
	     Collections.sort(list);  
	       
	       
	     for (int i = 0; i < list.size(); i++) {  
	  
	         int trafficLoad=rand.nextInt(391)+10;//分配带宽.R:bandwidth 10-400
	         nodepairlist.get(list.get(i)).setTrafficLoad(trafficLoad);
	         
	     }  


	}
	
	public static void FindRouteForNodeopair(Layer layer,Random rand)
	{
		 HashMap<String,Nodepair> nodepairlist=layer.getNodepairlist();
         Iterator<String> iter=nodepairlist.keySet().iterator();
         while(iter.hasNext()){
        	
        	Nodepair nodepair=(Nodepair)(nodepairlist.get(iter.next()));
        	int trafficLoad=rand.nextInt(391)+10;//分配带宽.R:bandwidth 10-400
        	nodepair.setTrafficLoad(trafficLoad);
        	Functions_Search_Route searchPath = new Functions_Search_Route();
         	searchPath.Kshortest(nodepair, layer, K);
        	
        
        	//只将满足OSNR最低限制的路径存入
        	Functions_ULL ull=new Functions_ULL();
        	ArrayList<LinearRoute> routelist = new ArrayList<LinearRoute>();
        	
        	for(LinearRoute route:nodepair.getLinearlist())
        	{
        		
        		double c=ull.CalculateRouteOSNR(route);
        		
        		if(c>=14.03)
        		{
        			routelist.add(route);
        		}

        	}
        	nodepair.setLinearlist(routelist);

         }
         
	}

	public static void WriteFunction(Layer layer, String DataFileName){   
        /*******************Sets***********************/
		GenerateDataFile_Sets(layer,DataFileName);
		/**********************************************/
	
		/*******************Parameters***********************/
		GenerateDataFile_Parameters(layer,DataFileName);
		/**********************************************/    	
	}

	

	public static void GenerateDataFile_Sets(Layer layer, String DataFileName){
		
		WriteSet_Links( layer,  DataFileName);
		WriteSet_Route( layer,  DataFileName);
		WriteSet_Demand( layer, DataFileName);
		WriteSet_Modulation(layer, DataFileName);
		
		
	}

	public static void GenerateDataFile_Parameters(Layer layer, String DataFileName){
		//Traffic load between each nodepair
		
		WriteParameters_Epsilon(layer,DataFileName);
		
		WriteParameters_Gamma(layer,DataFileName);
		//WriteParameters_Requirement(layer,DataFileName);
		
		WriteParameters_SlotNumForNodepair( layer, DataFileName);
		
		WriteParameters_OSNR_ULL(layer,DataFileName);
		WriteParameters_OSNR(layer,DataFileName);
		WriteParameters_OSNR_M(layer,DataFileName);
		//WriteParameters_Capacity(DataFileName);
		WriteParameters_LinkLength(layer,DataFileName);
		
		
		WriteParameters_Q(Q,DataFileName);
		WriteParameters_F(F,DataFileName);
	}
	
	public static void WriteSet_Links(Layer layer, String DataFileName){
		
		
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"set Links :=");
		
		HashMap<String,Link>LinkMap=layer.getLinklist();
        Iterator<String>iter_Link=LinkMap.keySet().iterator();
        while(iter_Link.hasNext()){
       	    Link currentLink=(Link)(LinkMap.get(iter_Link.next()));
       	
       	    if(currentLink.getNodeA().getIndex()<currentLink.getNodeB().getIndex())
       	    {
       	    	file.filewriteContinuous(DataFileName,currentLink.getName()+" ");
       	    	
       	    }
		}
        file.filewriteContinuous(DataFileName, ";"+"\r\n");
        file.filewriteContinuous(DataFileName, "\r\n");
		
		
	}
	
	public static void WriteSet_Demand(Layer layer,String DataFileName){
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"set Demand:=");

		HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
		Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();
		while(iter_Nodepair.hasNext()){

			Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));

			file.filewriteContinuous(DataFileName,nodepair.getName()+" ");


		}

		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
	}

	public static void WriteSet_Route(Layer layer, String DataFileName)
	{
		file_out_put file=new file_out_put();
		HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
		Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();
		while(iter_Nodepair.hasNext()){

			Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));
			
			file.filewriteContinuous(DataFileName,"set Routes["+nodepair.getName()+"]:=");
			file.filewriteContinuous(DataFileName, "\r\n");
			
			for(LinearRoute route:nodepair.getLinearlist())
	    	{
	    		int size = 1;
	    		for(Node node:route.getNodelist())
	    		{
	    			if(size<route.getNodelist().size())
	    				file.filewriteContinuous(DataFileName,node.getName()+"-");
	    			else
	    				file.filewriteContinuous(DataFileName,node.getName());
	    			size++;
	    		}
	    	    file.filewriteContinuous(DataFileName, "\r\n");
	    	}
	    	file.filewriteContinuous(DataFileName, ";"+"\r\n");
	    	file.filewriteContinuous(DataFileName, "\r\n");
			
		}
		
	}

	public static void WriteSet_Modulation(Layer layer, String DataFileName)
	{
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"set Modulation :=");
		
		file.filewriteContinuous(DataFileName,"BPSK"+" "+"QPSK"+" "+"8QAM"+" "+"16QAM");
		file.filewriteContinuous(DataFileName, "\r\n");
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
    	file.filewriteContinuous(DataFileName, "\r\n");
	}



	public static void WriteParameters_Epsilon(Layer layer,String DataFileName){
	    	file_out_put file=new file_out_put();
	    	file.filewrite(DataFileName,"param epsilon:=");
	    	file.filewriteContinuous(DataFileName, "\r\n");
	    

	    	HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
		    Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();    
		    while(iter_Nodepair.hasNext()){ 			
		    	
		    	
		    	Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));
		
		    	for(LinearRoute route:nodepair.getLinearlist())
		    	{    		
		    		HashMap<String,Nodepair>SecondNodepairMap=layer.getNodepairlist();
		    		Iterator<String>iter_SecondNodepair=SecondNodepairMap.keySet().iterator();    
		    		while(iter_SecondNodepair.hasNext()){ 			

		    			Nodepair Secondnodepair=(Nodepair)(SecondNodepairMap.get(iter_SecondNodepair.next()));

		    			for(LinearRoute Secondroute:Secondnodepair.getLinearlist())
		    			{
		    				int size = 1;
		    				file.filewriteContinuous(DataFileName,nodepair.getName()+",");	
	    					for(Node node:route.getNodelist())
	    					{
	    						if(size<route.getNodelist().size())
	    							file.filewriteContinuous(DataFileName,node.getName()+"-");	    			    			
	    						else    			    			
	    							file.filewriteContinuous(DataFileName,node.getName()+",");	    			    			
	    						size++;	    			    		
	    					}
	    					file.filewriteContinuous(DataFileName,Secondnodepair.getName()+",");	
	    					size = 1;

	    					for(Node node:Secondroute.getNodelist())
	    					{
	    						if(size<Secondroute.getNodelist().size())
	    							file.filewriteContinuous(DataFileName,node.getName()+"-");	    			    			
	    						else    			    			
	    							file.filewriteContinuous(DataFileName,node.getName()+",");	    			    			
	    						size++;	    			    		
	    					}
	    					if(route.shareCommonLinkWithoutDirection(Secondroute))
	    					{	         	    
	    						file.filewriteContinuous(DataFileName, 1+"");		           	    
	    					}  		           	   
	    					else    		           	  
	    					{	           	    		           	 
	    						file.filewriteContinuous(DataFileName, 0+"");	    		           	  
	    					}	 


	    					file.filewriteContinuous(DataFileName, "\r\n");
		    			}
		    		}
		    	}
		 
		 	    	    			
			}
	     file.filewrite(DataFileName, ";");
	   	 file.filewriteContinuous(DataFileName, "\r\n");
	    }


	public static void WriteParameters_Gamma(Layer layer,String DataFileName){
		 
	
		 file_out_put file=new file_out_put();
		 file.filewrite(DataFileName,"param gamma:=");
		 file.filewriteContinuous(DataFileName, "\r\n");


		 HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
		 Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();    
		 while(iter_Nodepair.hasNext()){ 			


			 Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));

			 for(LinearRoute route:nodepair.getLinearlist())       	//遍历d路径				
			 {
				 HashMap<String,Link>LinkMap=layer.getLinklist();
				 Iterator<String>iter_Link=LinkMap.keySet().iterator();
				 while(iter_Link.hasNext()){
					 Link currentLink=(Link)(LinkMap.get(iter_Link.next()));	
					 if(currentLink.getNodeA().getIndex()<currentLink.getNodeB().getIndex())
					 {
						 file.filewriteContinuous(DataFileName, currentLink.getName()+",");
						 file.filewriteContinuous(DataFileName, nodepair.getName()+",");
						 int size = 1;
						 for(Node node:route.getNodelist())
						 {
							 if(size<route.getNodelist().size())
								 file.filewriteContinuous(DataFileName,node.getName()+"-");
							 else
								 file.filewriteContinuous(DataFileName,node.getName()+",");
							 size++;
						 }

						
						 if(route.traverseLink(currentLink))
						 {

							 file.filewriteContinuous(DataFileName, 1+"");
						 }
						 else
						 {	           	
							 file.filewriteContinuous(DataFileName, 0+"");
						 }   	 
						 file.filewriteContinuous(DataFileName, "\r\n");
					 }
				 }
			 }
		 }
		 file.filewrite(DataFileName, ";");
		 file.filewriteContinuous(DataFileName, "\r\n");
	 
	 }


	 public static void WriteParameters_Requirement(Layer layer,String DataFileName) 
	 {
		  file_out_put file=new file_out_put();
		  file.filewrite(DataFileName,"param  BandwidthRequirement:=");
		  file.filewriteContinuous(DataFileName, "\r\n");
		  
		  HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
		  Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();    
		  while(iter_Nodepair.hasNext()){ 			


			  Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));
			  file.filewrite(DataFileName,nodepair.getName()+","+nodepair.getTrafficLoad());
			  file.filewriteContinuous(DataFileName, "\r\n");    

		  }
		  file.filewrite(DataFileName, ";");
		  file.filewriteContinuous(DataFileName, "\r\n");
	 }
		
	 
	 public static void WriteParameters_SlotNumForNodepair(Layer layer,String DataFileName) 
	 {
		  file_out_put file=new file_out_put();
		  file.filewrite(DataFileName,"param  f:=");
		  file.filewriteContinuous(DataFileName, "\r\n");
		  
		  HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
		  Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();    
		  while(iter_Nodepair.hasNext()){ 			


			  Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));
			
			
			  
			  double num1 = nodepair.getTrafficLoad()/25;
			  int slotnum1 = (int) Math.ceil(num1);
			  file.filewrite(DataFileName,nodepair.getName()+","+"BPSK"+","+slotnum1);
			  file.filewriteContinuous(DataFileName, "\r\n");    
			  
			  double num2 = nodepair.getTrafficLoad()/50;
			  int slotnum2 = (int) Math.ceil(num2);
			  file.filewrite(DataFileName,nodepair.getName()+","+"QPSK"+","+slotnum2);
			  file.filewriteContinuous(DataFileName, "\r\n");    
			  
			  double num3 = nodepair.getTrafficLoad()/75;
			  int slotnum3 = (int) Math.ceil(num3);
			  file.filewrite(DataFileName,nodepair.getName()+","+"8QAM"+","+slotnum3);
			  file.filewriteContinuous(DataFileName, "\r\n");    
			  
			  double num4 = nodepair.getTrafficLoad()/90;
			  int slotnum4 = (int) Math.ceil(num4);
			  file.filewrite(DataFileName,nodepair.getName()+","+"16QAM"+","+slotnum4);
			  file.filewriteContinuous(DataFileName, "\r\n");    

		  }
		  file.filewrite(DataFileName, ";");
		  file.filewriteContinuous(DataFileName, "\r\n");
	 } 
	 
	 public static void WriteParameters_OSNR_ULL(Layer layer, String DataFileName) 
	 {
		 file_out_put file=new file_out_put();
		 file.filewrite(DataFileName,"param  OSNR_S1:=");
		 file.filewriteContinuous(DataFileName, "\r\n");
		 Functions_ULL  calculate=new Functions_ULL();

		 HashMap<String,Link>LinkMap=layer.getLinklist();
		 Iterator<String>iter_Link=LinkMap.keySet().iterator();
		 while(iter_Link.hasNext()){
			 Link currentLink=(Link)(LinkMap.get(iter_Link.next()));
			 double linkOSNR= calculate.CalculateLinkOSNRofRoute_ULL(currentLink,0.168);
			 if(currentLink.getNodeA().getIndex()<currentLink.getNodeB().getIndex())
			 {
				 file.filewriteContinuous(DataFileName,currentLink.getName()+","+linkOSNR);
				 file.filewriteContinuous(DataFileName, "\r\n");
			 }
			
		 }

		 
		 file.filewriteContinuous(DataFileName, ";"+"\r\n");
		 file.filewriteContinuous(DataFileName, "\r\n");
	 }

	 
	 public static void WriteParameters_OSNR(Layer layer, String DataFileName) 
	 {
		 file_out_put file=new file_out_put();
		 file.filewrite(DataFileName,"param  OSNR_S0:=");
		 file.filewriteContinuous(DataFileName, "\r\n");
		 
		 
		 Functions_ULL  calculate=new Functions_ULL();

		 HashMap<String,Link>LinkMap=layer.getLinklist();
		 Iterator<String>iter_Link=LinkMap.keySet().iterator();
		 while(iter_Link.hasNext()){
			 Link currentLink=(Link)(LinkMap.get(iter_Link.next()));
			 double linkOSNR= calculate.CalculateLinkOSNRofRoute(currentLink);
			 if(currentLink.getNodeA().getIndex()<currentLink.getNodeB().getIndex())
			 {
				 file.filewriteContinuous(DataFileName,currentLink.getName()+","+linkOSNR);
				 file.filewriteContinuous(DataFileName, "\r\n");
			 }
			
		 }
		 file.filewriteContinuous(DataFileName, ";"+"\r\n");
		 file.filewriteContinuous(DataFileName, "\r\n");
	 }

	 public static void WriteParameters_OSNR_M(Layer layer, String DataFileName) 
	 {
		 file_out_put file=new file_out_put();
		 file.filewrite(DataFileName,"param  OSNR_M:=");
		 file.filewriteContinuous(DataFileName, "\r\n");
		 
		 double a1=1/Math.pow(10, ((14.03+2.5)/10));
		 file.filewriteContinuous(DataFileName,"BPSK"+","+a1);
		 file.filewriteContinuous(DataFileName, "\r\n");
		 
		 double a2=1/Math.pow(10, ((17.01+2.5)/10));
		 file.filewriteContinuous(DataFileName,"QPSK"+","+a2);
		 file.filewriteContinuous(DataFileName, "\r\n");
		 
		 double a3=1/Math.pow(10, ((20.37+2.5)/10));
		 file.filewriteContinuous(DataFileName,"8QAM"+","+a3);
		 file.filewriteContinuous(DataFileName, "\r\n");
		
		 double a4=1/Math.pow(10, ((22.4+2.5)/10));
		 file.filewriteContinuous(DataFileName,"16QAM"+","+a4);
		 file.filewriteContinuous(DataFileName, "\r\n");
		 
		 file.filewriteContinuous(DataFileName, ";"+"\r\n");
		 file.filewriteContinuous(DataFileName, "\r\n");
	 }

	 
	 
	 public static void WriteParameters_Capacity(String DataFileName) 
	 {
		 file_out_put file=new file_out_put();
		 file.filewriteContinuous(DataFileName,"param Capacity_M:=");
		 file.filewriteContinuous(DataFileName, "\r\n");
		
		 file.filewriteContinuous(DataFileName,"BPSK"+","+"25");
		 file.filewriteContinuous(DataFileName, "\r\n");
		 
		 file.filewriteContinuous(DataFileName,"QPSK"+","+"50");
		 file.filewriteContinuous(DataFileName, "\r\n");
		 file.filewriteContinuous(DataFileName,"8AQM"+","+"75");
		 file.filewriteContinuous(DataFileName, "\r\n");
		
		 file.filewriteContinuous(DataFileName,"16AQM"+","+"90");
		 file.filewriteContinuous(DataFileName, "\r\n");
		 
		 file.filewriteContinuous(DataFileName, ";"+"\r\n");
		 file.filewriteContinuous(DataFileName, "\r\n");
	 }

	 public static void WriteParameters_Limit(String DataFileName) 
	 {
		 file_out_put file=new file_out_put();
		 file.filewriteContinuous(DataFileName,"param OSNR_M:=");
		 file.filewriteContinuous(DataFileName, "\r\n");
		
		 file.filewriteContinuous(DataFileName,"BPSK"+","+"14.03");
		 file.filewriteContinuous(DataFileName, "\r\n");
		 
		 file.filewriteContinuous(DataFileName,"QPSK"+","+"17.01");
		 file.filewriteContinuous(DataFileName, "\r\n");
		 file.filewriteContinuous(DataFileName,"8AQM"+","+"20.37");
		 file.filewriteContinuous(DataFileName, "\r\n");
		
		 file.filewriteContinuous(DataFileName,"16AQM"+","+"22.4");
		 file.filewriteContinuous(DataFileName, "\r\n");
		 
		 file.filewriteContinuous(DataFileName, ";"+"\r\n");
		 file.filewriteContinuous(DataFileName, "\r\n");
	 }
	 public static void WriteParameters_LinkLength(Layer layer, String DataFileName) 
	 {
		 file_out_put file=new file_out_put();
		 file.filewriteContinuous(DataFileName,"param L:=");
		 file.filewriteContinuous(DataFileName, "\r\n");
		
		 HashMap<String,Link>LinkMap=layer.getLinklist();
		 Iterator<String>iter_Link=LinkMap.keySet().iterator();
		 while(iter_Link.hasNext()){
			 Link currentLink=(Link)(LinkMap.get(iter_Link.next()));
			 if(currentLink.getNodeA().getIndex()<currentLink.getNodeB().getIndex())
			 {
				 file.filewriteContinuous(DataFileName,currentLink.getName()+","+currentLink.getLength() );
			
				 file.filewriteContinuous(DataFileName, "\r\n");
			 }
		 }
		 file.filewriteContinuous(DataFileName, ";"+"\r\n");
		 file.filewriteContinuous(DataFileName, "\r\n");
	 }


	 public static void WriteParameters_Q(double i, String writename)
	 {
		 file_out_put file=new file_out_put();
		 file.filewriteContinuous(writename,"param Q :=");
		 file.filewriteContinuous(writename,""+i);
		 file.filewriteContinuous(writename, ";"+"\r\n");
	 }



	 public static void WriteParameters_F(int i, String writename)
	 {
		 file_out_put file=new file_out_put();
		 file.filewriteContinuous(writename,"param F :=");
		 file.filewriteContinuous(writename,""+i);
		 file.filewriteContinuous(writename, ";"+"\r\n");
	 }

	 
}
