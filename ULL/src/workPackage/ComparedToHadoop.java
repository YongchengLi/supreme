package workPackage;

import general.ReadFile;
import general.file_out_put;
import groupwork.Serach_test_once_new;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import subgraph.LinearRoute;

import Network.Layer;
import Network.Nodepair;

public class ComparedToHadoop {
	
	public static void main(String[] args){
		//	Intialize  初始化
		long startTime=System.currentTimeMillis();   //获取开始时间
 
		file_out_put file=new file_out_put();
		
		
		ReadFile readDemandFile=new ReadFile();
		String  data=readDemandFile.readTxtFileFunction("f:/readfile14.txt");
		
		String[] newText=data.split(",");
		ArrayList<String> nodepairlist=new ArrayList<String>(100);
		for(int i=0;i<newText.length;i++){
			if(newText[i]!="")
			{
				nodepairlist.add(newText[i]);			
			//	System.out.println(i+"   "+newText[i]);
			}
		}
		System.out.println(newText.length);
		
		int shuffleNumber=0;
		int finalMinResult=1000000;
		while(shuffleNumber<10000)
		{
			
			String newsString =new String();
			Collections.shuffle(nodepairlist);
			
			
			Layer layer;	
	    	layer=new Layer("worklayer", 0, "");	
	    	layer.readTopology("f:/14.csv");
	    	layer.generateNodepaairs();
	    	
	    	
	    	Serach_test_once_new test=new Serach_test_once_new();
			test.Serach_once(layer, "f:/hadoop_write.txt");
			
			HashMap<String,Nodepair>mapRWA=layer.getNodepairlist();           
			Iterator<String>iterRWA=mapRWA.keySet().iterator();     		
			while(iterRWA.hasNext()){     		
				Nodepair nodepair=(Nodepair)(mapRWA.get(iterRWA.next()));  
				LinearRoute route= nodepair.getLinearlist().get(0);
			}
			

	    
	   
	    	
	    //	int OccupyWavNum=0;
	    	//routing wavelength assginment 
	    	LayerWavlengthRoutingAlgorithm workRunRWA=new LayerWavlengthRoutingAlgorithm();
	     //	workRunRWA.WavelengthRoutingAlgorithm(layer, nodepairList, 20, 3);//wavNum
	    	workRunRWA.WavePlaneAgorithm(layer, nodepairlist, 20, 3);
	    	int OccupyWavNum=workRunRWA.FindOccupy(layer);
	        String result="";
	        result=String.valueOf(OccupyWavNum);
			
	        if(OccupyWavNum<finalMinResult)
	        	finalMinResult=OccupyWavNum;
		
			
	
	        file.filewrite("f:/testforhadoop.txt",shuffleNumber+" "+result);
		
	        System.out.println(shuffleNumber);
			shuffleNumber++;
		}
		 file.filewrite("f:/testforhadoop.txt","finalMinResult:  "+finalMinResult);
	
		long endTime=System.currentTimeMillis(); //获取结束时间

		
		
		
		System.out.println("程序运行时间： "+ (endTime-startTime) +"ms"); 
	}
	
	
	
	
	
	

}
