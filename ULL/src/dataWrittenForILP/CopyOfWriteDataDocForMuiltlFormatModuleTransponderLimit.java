package dataWrittenForILP;


import general.Constant;
import general.file_out_put;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import subgraph.LinearRoute;

import Network.*;


/**
 * @author Yongcheng Li
 * This program is desgined for the project named Quasi-WDM with MuiltlFormatModule
 * The code is to write data file
 */
public class CopyOfWriteDataDocForMuiltlFormatModuleTransponderLimit {
	
	
	public static int formatModule_Capacity = 100;                         //容量
    public static double Nodepair_trafficLoad = 300;
    
    public static int wavelengthNum  =20;
    
	public static String formatModule_QPSK="QPSK";
	public static String formatModule_BPSK="BPSK";
	public static String formatModule_QAM ="QAM";
	
	public static int formatModule__QPSK_DistanceLimit = 2000;                   //距离限制
	public static int formatModule_BPSK_DistanceLimit =4000;           
	public static int formatModule_QAM_DistanceLimit =1000;
	

	
	
	public static double capacity_QPSK = 350;                            //QPSK容量
	public static double capacity_BPSK = 175;                            //BPSK容量
	public static double capacity_QAM  =525;                            //QAM容量      
	
	public static double formatModule_QPSK_CostOfRegen = 1.3;                   //再生器价格
	public static double formatModule_QPSK_CostOfIpRouterPort = 2.6;            //路由器价格
	
	public static double formatModule_BPSK_CostOfRegen =1;                   //再生器价格
	public static double formatModule_BPSK_CostOfIpRouterPort = 2;            //路由器价格
	
	public static double formatModule_QAM_CostOfRegen = 1.5;                   //再生器价格
	public static double formatModule_QAM_CostOfIpRouterPort = 3;            //路由器价格
	
	public static int MaxNum = 1000;
	public static int MinNum = 200;
	public static int RandomSeed = 1;
	public static String ReadFile =  "NODE6_1";
	public static String ReadFileName = ReadFile+".CSV";
//	public static String GenerateDataFileName =ReadFile+"_"+MaxNum+"_"+RandomSeed+"_Ten.dat";//生成的DataFile文件名称
	
	/**
	 * This main function is desgined for the project named Quasi-WDM with MuiltlFormatModule
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		for(int i=1;i<=RandomSeed;i++)
		{
			String GenerateDataFileName ="DelayResult/"+ReadFile+"_"+MaxNum+"_"+wavelengthNum+"_Ten.dat";//生成的DataFile文件名称
			System.out.println("read the file："+ReadFileName);		
			/***********************Input the file*****************************/
			Layer layer;
			layer=new Layer("worklayer", 0, "");
			layer.readTopology(ReadFileName);                                        //读取拓扑结构                                   
			layer.generateNodepaairs();
			layer.shortestPath();
			/********************************************************************/
			HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
	        Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();
			while(iter_Nodepair.hasNext()){ 
				Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));
				System.out.println(nodepair.getName());
				nodepair.getLinearlist().get(0).OutputRoute_node(nodepair.getLinearlist().get(0));
			}
			
			
			
			/***********************Generate the datafile**********************/
			WriteFunction(i,layer,GenerateDataFileName);
			/********************************************************************/
			System.out.println("The file"+GenerateDataFileName+" has generated");
		}
		
	
	}
	
	/**
	 * 
	 * write the set and parameters into the data file
	 * @param layer         the topology of test network
	 * @param writename     the output data file
	 */
	public static void WriteFunction(int i,Layer layer, String DataFileName){   
        /*******************Sets***********************/
		GenerateDataFile_Sets(layer,DataFileName);
		/**********************************************/
		
		/*******************Parameters***********************/
		GenerateDataFile_Parameters(i,layer,DataFileName);
		/**********************************************/    	
	}
	//***********************formulation of Sets*********************************************//
	/**
	 * Generate the Sets of data file
	 * @param layer
	 * @param DataFileName
	 */
	public static void GenerateDataFile_Sets(Layer layer,String DataFileName){
		WriteSet_Nodes(layer,DataFileName);//write the data file of nodes set
		WriteSet_NeiNodes(layer,DataFileName);
		WriteSet_FormatModules(layer,DataFileName);
		WriteSet_Wavelength(wavelengthNum,DataFileName);
	}
	
	/**
	 * Set1: Nodes
	 * @param layer
	 * @param writename
	 */
	public static void WriteSet_Nodes(Layer layer, String DataFileName){
		
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"set NODES :=");
		HashMap<String,Node>NodeMap=layer.getNodelist();
        Iterator<String>iter_Node=NodeMap.keySet().iterator();
        while(iter_Node.hasNext()){
       	    Node currentNode=(Node)(NodeMap.get(iter_Node.next()));	
       	    file.filewriteContinuous(DataFileName,currentNode.getName()+" ");
		}
        file.filewriteContinuous(DataFileName, ";"+"\r\n");
        file.filewriteContinuous(DataFileName, "\r\n");
	}
	
	/**
	 * 
	 * @param layer
	 * @param DataFileName
	 */
	public static void WriteSet_NeiNodes(Layer layer,String DataFileName){
		
		file_out_put file=new file_out_put();
		HashMap<String,Node>NodeMap=layer.getNodelist();
        Iterator<String>iter_Node=NodeMap.keySet().iterator();
		while(iter_Node.hasNext()){ 
			Node node=(Node)(NodeMap.get(iter_Node.next()));
			file.filewriteContinuous(DataFileName,"set Nei"+"["+node.getName()+"]"+" :=");
			for(int i = 0;i<node.getNeinodelist().size();i++)
			{
				Node currentNode = node.getNeinodelist().get(i);
				file.filewriteContinuous(DataFileName,currentNode.getName()+" ");
			}
			file.filewriteContinuous(DataFileName, ";"+"\r\n");
	        file.filewriteContinuous(DataFileName, "\r\n");
		}
		
	}
	
	
	/**
	 * Set2: FormatModules
	 * @param layer
	 * @param DataFileName
	 */
	public static void WriteSet_FormatModules(Layer layer, String DataFileName){
		file_out_put file=new file_out_put();

		file.filewriteContinuous(DataFileName,"set B :=");
		
		//将所有FormatModules放入生成
		file.filewriteContinuous(DataFileName, formatModule_BPSK+" ");
		file.filewriteContinuous(DataFileName, formatModule_QPSK+" ");	
		file.filewriteContinuous(DataFileName, formatModule_QAM+" ");
		
        file.filewriteContinuous(DataFileName, ";"+"\r\n");
        file.filewriteContinuous(DataFileName, "\r\n");
	}
	
	
	
	/**
	 * Set3: Wavelength Num
	 * @param WavelengthNum
	 * @param DataFileName
	 */
	public static void WriteSet_Wavelength(int WavelengthNum,String DataFileName)
	{
		file_out_put file=new file_out_put();

		file.filewriteContinuous(DataFileName,"set W :=");
		
		for(int i =0 ;i < WavelengthNum;i++)
		{
			file.filewriteContinuous(DataFileName, "W"+i+" ");
		}
		
        file.filewriteContinuous(DataFileName, ";"+"\r\n");
        file.filewriteContinuous(DataFileName, "\r\n");
	}
	//***********************formulation of Sets*********************************************//
	
	//***********************formulation of Parameter*********************************************//
	/**
	 * Generate Parameters of data file
	 * @param layer
	 * @param DataFileName
	 */
	public static void GenerateDataFile_Parameters(int i,Layer layer,String DataFileName){
		//Traffic load between each nodepair
		//WriteParameters_TrafficLoad(layer,DataFileName);
		Random rand =  new Random(i);
		WriteParameter_Random_TrafficLoad(layer,rand,MaxNum,MinNum,DataFileName);
		//Number of regen in each lightpath
		writeParameter_NumofRegen(layer,DataFileName);
		//Capacity of current FormatModule
		writeParameter_Capacity(layer,DataFileName);
		//CostOfRegen of current FormatModule
		writeParameter_CostOfRegen(layer,DataFileName);
		//CostOfIpRouterPort of current FormatModule
		writeParameter_CostOfIpRouterPort(layer,DataFileName);
		//StatusOfLinkwithLightpath
		writeParameter_StatusOfLinkwithLightpath(layer,DataFileName);
		//factor
		writeParameter_Factor(0.1,DataFileName);
		
		//Ter
		writeParameter_Ter(layer,DataFileName);
		
	}
	
	/**
	 * 随机产生业务需求
	 * @param layer
	 * @param rand
	 * @param max
	 * @param min
	 * @param DataFileName
	 */
	public static void WriteParameter_Random_TrafficLoad(Layer layer, Random rand,int max,int min,String DataFileName){
		
		/*******************param TrafficLoad :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param Lambda :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
		int index = 0;
		HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
        Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();
		while(iter_Nodepair.hasNext()){ 
			Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));
			double trafficload = 0;
		
			 int a = rand.nextInt(max)%(max-min+1)+min;
		
			 trafficload = a;
			/*
			 if(index<=5)
				index++;
			else
			{
				trafficload=0;
				index++;
			}
			*/
			file.filewriteContinuous(DataFileName,nodepair.getSrcNode().getName()+","+nodepair.getDesNode().getName()+" "+trafficload+"\r\n");
			file.filewriteContinuous(DataFileName,nodepair.getDesNode().getName()+","+nodepair.getSrcNode().getName()+" "+trafficload+"\r\n");
			
			
		}
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
	}
	
	/**
	 * param 1:TrafficLoad
	 * @param layer
	 * @param DataFileName
	 */
	public static void WriteParameters_TrafficLoad(Layer layer,String DataFileName){
		
		/*******************param TrafficLoad :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param Lambda :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
		
		HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
        Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();
		while(iter_Nodepair.hasNext()){ 
			Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));
			file.filewriteContinuous(DataFileName,nodepair.getSrcNode().getName()+","+nodepair.getDesNode().getName()+" "+Nodepair_trafficLoad+"\r\n");
			file.filewriteContinuous(DataFileName,nodepair.getDesNode().getName()+","+nodepair.getSrcNode().getName()+" "+Nodepair_trafficLoad+"\r\n");
			
			
		}
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
		
		
		
	}
	
	/**
	 * param 2: NumofRegen
	 * Generate NumofRegen of data file
	 * @param layer
	 * @param DataFileName
	 */
	public static void writeParameter_NumofRegen(Layer layer,String DataFileName){
		/*******************param TrafficLoad :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param R :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
		
		HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
        Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();
		while(iter_Nodepair.hasNext()){ 
			Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));
			double LightPath_Distance1 = 0;
			double LightPath_Distance2 = 0;
			double LightPath_Distance3 = 0;
			int NumofRegen_BPSK = 0;
			int NumofRegen_QPSK = 0;
			int NumofRegen_QAM = 0;
			int size = 0;
			for(Link currentLink:nodepair.getLinearlist().get(0).getLinklist())
			{
				LightPath_Distance1 = LightPath_Distance1+currentLink.getLength();
				if(LightPath_Distance1>formatModule_BPSK_DistanceLimit)
			    {
				   NumofRegen_BPSK++;
			       LightPath_Distance1 = currentLink.getLength();
			      
			    }
			    else if(LightPath_Distance1==formatModule_BPSK_DistanceLimit&&size!=nodepair.getLinearlist().get(0).getLinklist().size())
			    {
			       LightPath_Distance1 = 0;
		        }
				LightPath_Distance2= LightPath_Distance2+currentLink.getLength();
				if(LightPath_Distance2>formatModule__QPSK_DistanceLimit)
			    {
				   NumofRegen_QPSK++;
			       LightPath_Distance2 = currentLink.getLength();
			      
			    }
			    else if(LightPath_Distance2==formatModule__QPSK_DistanceLimit&&size!=nodepair.getLinearlist().get(0).getLinklist().size())
			    {
			       LightPath_Distance2 = 0;
		        }
				
				LightPath_Distance3= LightPath_Distance3+currentLink.getLength();
				if(LightPath_Distance3>formatModule_QAM_DistanceLimit)
			    {
				   NumofRegen_QAM++;
			       LightPath_Distance3 = currentLink.getLength();
			      
			    }
			    else if(LightPath_Distance3==formatModule_QAM_DistanceLimit&&size!=nodepair.getLinearlist().get(0).getLinklist().size())
			    {
			       LightPath_Distance3 = 0;
		        }
			      size++;
			}
					
			//int NumofRegen_BPSK=(int) Math.ceil(LightPath_Distance/formatModule_BPSK_DistanceLimit);   		
			//NumofRegen_BPSK=NumofRegen_BPSK-1;  
			//NumofRegen_BPSK = NumofRegen_BPSK*2;
   	    	file.filewriteContinuous(DataFileName,formatModule_BPSK+","+nodepair.getSrcNode().getName()+","+nodepair.getDesNode().getName()+" "+NumofRegen_BPSK+"\r\n");
   	    	file.filewriteContinuous(DataFileName,formatModule_BPSK+","+nodepair.getDesNode().getName()+","+nodepair.getSrcNode().getName()+" "+NumofRegen_BPSK+"\r\n");
		
   	    	//int NumofRegen_QPSK=(int) Math.ceil(LightPath_Distance/formatModule__QPSK_DistanceLimit);   		
   	    	//NumofRegen_QPSK=NumofRegen_QPSK-1;
   	    	//NumofRegen_QPSK = NumofRegen_QPSK*2;
   	    	file.filewriteContinuous(DataFileName,formatModule_QPSK+","+nodepair.getSrcNode().getName()+","+nodepair.getDesNode().getName()+" "+NumofRegen_QPSK+"\r\n");
   	    	file.filewriteContinuous(DataFileName,formatModule_QPSK+","+nodepair.getDesNode().getName()+","+nodepair.getSrcNode().getName()+" "+NumofRegen_QPSK+"\r\n");
		
   	    //	int NumofRegen_QAM=(int) Math.ceil(LightPath_Distance/formatModule_QAM_DistanceLimit);   		
   	    //	NumofRegen_QAM=NumofRegen_QAM-1; 
   	    	//NumofRegen_QAM = NumofRegen_QAM*2;
   	    	file.filewriteContinuous(DataFileName,formatModule_QAM+","+nodepair.getSrcNode().getName()+","+nodepair.getDesNode().getName()+" "+NumofRegen_QAM+"\r\n");
   	    	file.filewriteContinuous(DataFileName,formatModule_QAM+","+nodepair.getDesNode().getName()+","+nodepair.getSrcNode().getName()+" "+NumofRegen_QAM+"\r\n");
		
		
		
		}
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
	}
	
	/**
	 * param 3: Capacity
	 * @param layer
	 * @param DataFileName
	 */
	public static void writeParameter_Capacity(Layer layer,String DataFileName){
		/*******************param Capacity :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param  C:=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
		file.filewriteContinuous(DataFileName,formatModule_BPSK+" "+capacity_BPSK+"\r\n");
		file.filewriteContinuous(DataFileName,formatModule_QPSK+" "+capacity_QPSK+"\r\n");
		file.filewriteContinuous(DataFileName,formatModule_QAM+" "+capacity_QAM+"\r\n");
		
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
	}
	
	/**
	 * param 4：CostOfRegen
	 * @param layer
	 * @param DataFileName
	 */
	public static void writeParameter_CostOfRegen(Layer layer,String DataFileName){
		/*******************param CostOfRegen :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param Cr :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
		file.filewriteContinuous(DataFileName,formatModule_BPSK+" "+formatModule_BPSK_CostOfRegen+"\r\n");
		file.filewriteContinuous(DataFileName,formatModule_QPSK+" "+formatModule_QPSK_CostOfRegen+"\r\n");
		file.filewriteContinuous(DataFileName,formatModule_QAM+" "+formatModule_QAM_CostOfRegen+"\r\n");
		
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
	}
	
	/**
	 * param5:CostOfIpRouterPort
	 * @param layer
	 * @param DataFileName
	 */
	public static void writeParameter_CostOfIpRouterPort(Layer layer,String DataFileName){
		/*******************param CostOfIpRouterPort :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param Co :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
		file.filewriteContinuous(DataFileName,formatModule_BPSK+" "+formatModule_BPSK_CostOfIpRouterPort+"\r\n");
		file.filewriteContinuous(DataFileName,formatModule_QPSK+" "+formatModule_QPSK_CostOfIpRouterPort+"\r\n");
		file.filewriteContinuous(DataFileName,formatModule_QAM+" "+formatModule_QAM_CostOfIpRouterPort+"\r\n");
		
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
	}

	/**
	 * param6:StatusOfLinkwithLightpath
	 * @param layer
	 */
	public static void writeParameter_StatusOfLinkwithLightpath(Layer layer,String DataFileName)
	{
		/*******************param Sigma :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param Sigma :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
		HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
        Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();
		while(iter_Nodepair.hasNext()){ 
			Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));
			
			
			ArrayList<Link> firstLinklist =new ArrayList<Link>();
			ArrayList<Link> secondLinklist =new ArrayList<Link>();
			for(int i = 0; i < nodepair.getLinearlist().get(0).getNodelist().size();i++)
			{
				if(i!=0)
				{
					Node nodeA = nodepair.getLinearlist().get(0).getNodelist().get(i-1);
					Node nodeB = nodepair.getLinearlist().get(0).getNodelist().get(i);
					Link firstlink = layer.findlink(nodeA, nodeB);
					Link secondlink = layer.findlink(nodeB, nodeA);
					firstLinklist.add(firstlink);
					secondLinklist.add(secondlink);
				}
			}
			HashMap<String,Link>LinkMap=layer.getLinklist();
	        Iterator<String>iter_Link=LinkMap.keySet().iterator();
			while(iter_Link.hasNext()){ 
				Link link=(Link)(LinkMap.get(iter_Link.next()));
				int key1=0;
				for(int i = 0;i<firstLinklist.size();i++)
				{
					if(link.equals(firstLinklist.get(i))||link.equals(secondLinklist.get(i)))
					{
						key1=1;
						break;
					}
				}
				if(key1==1)
				{
					file.filewriteContinuous(DataFileName,nodepair.getSrcNode().getName()+","+nodepair.getDesNode().getName()+","+link.getNodeA().getName()+","+link.getNodeB().getName()+" "+1+"\r\n");
					file.filewriteContinuous(DataFileName,nodepair.getDesNode().getName()+","+nodepair.getSrcNode().getName()+","+link.getNodeA().getName()+","+link.getNodeB().getName()+" "+1+"\r\n");
				}
				else
				{
					file.filewriteContinuous(DataFileName,nodepair.getSrcNode().getName()+","+nodepair.getDesNode().getName()+","+link.getNodeA().getName()+","+link.getNodeB().getName()+" "+0+"\r\n");
					file.filewriteContinuous(DataFileName,nodepair.getDesNode().getName()+","+nodepair.getSrcNode().getName()+","+link.getNodeA().getName()+","+link.getNodeB().getName()+" "+0+"\r\n");	
				}
			}
		}
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
		file.filewriteContinuous(DataFileName, "\r\n");
	}
	
	
	
	
	public static void writeParameter_Factor(double factor,String DataFileName)
	{
		/*******************param factor :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param Alpha :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
		file.filewriteContinuous(DataFileName,factor+"\r\n");
		
		
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
	}
	
	
	public static void writeParameter_Ter(Layer layer,String DataFileName)
	{
		/*******************param factor :=**************/
		file_out_put file=new file_out_put();
		file.filewriteContinuous(DataFileName,"param Ter :=");
		file.filewriteContinuous(DataFileName, "\r\n");
		/****************************************************/
		HashMap<String,Nodepair>NodepairMap=layer.getNodepairlist();
        Iterator<String>iter_Nodepair=NodepairMap.keySet().iterator();
		while(iter_Nodepair.hasNext()){ 
			Nodepair nodepair=(Nodepair)(NodepairMap.get(iter_Nodepair.next()));
		   
			LinearRoute path = nodepair.getLinearlist().get(0);// searchPath
			double pathDistance = path.getlength();
			
			double ter = pathDistance/Constant.lightSpeed;
			
			file.filewriteContinuous(DataFileName,nodepair.getSrcNode().getName()+","+nodepair.getDesNode().getName()+" "+ter+"\r\n");
			file.filewriteContinuous(DataFileName,nodepair.getDesNode().getName()+","+nodepair.getSrcNode().getName()+" "+ter+"\r\n");
		}
		
		
		
				
		file.filewriteContinuous(DataFileName, ";"+"\r\n");
	}
	//***********************formulation of Parameter*********************************************//
	

}
