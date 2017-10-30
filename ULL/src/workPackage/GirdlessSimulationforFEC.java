package workPackage;

import functionAlorithms.Algorithm;
import general.Constant;
import general.Range;
import general.Time;
import general.TimeLine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import subgraph.LinearRoute;





import Network.Layer;
import Network.Nodepair;

public class GirdlessSimulationforFEC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	
		Scanner scannerobject=new Scanner(System.in);
		
		System.out.println("输入读取的Topology文件（csv文件）：");
		String open_name = scannerobject.next();                              //输入拓扑文件
		
		//System.out.println("输入数据结果写入文件（txt文件）:");
		//String writename = scannerobject.next();                             //输入数据结果
		
		
		Layer layer;
		layer=new Layer("worklayer", 0, "");
		layer.readTopology(open_name);                                        //读取拓扑结构                                   
		layer.generateNodepaairs();
		System.out.println("Num of node:"+layer.getNodelist().size());
		System.out.println("Num of link:"+layer.getLink_num()/2);
	
		FindPath algorithm=new FindPath();
		algorithm.KshortestPath(layer,1);
		
		Algorithm workAlgorithm=new Algorithm();
		workAlgorithm.CalculateLinkOSNR(layer);
		workAlgorithm.OutputAllRouteOSNR(layer);		                                    		
		workAlgorithm.InitSlotOfLayer(layer, 1000);
		//workAlgorithm.OutputAllRoute(layer);
		simulationWork(layer,80,100,200);					
	}	
	/**
	 * 仿真程序
	 * @param layer
	 * @param trafficload
	 * @param 对于Trafficload的相对值  relativeValue  
	 * @param 循环次数 CycleTimes
	 */
	public static void simulationWork(Layer layer, int trafficload,int relativeValue,int CycleTimes){
		 
		int b=trafficload;int i=0;
		Time array[]=new Time[10000];		
		TimeLine timeline=new TimeLine("timeline",1,"",layer.getNodepairlist().size());
	    
		 Algorithm workAlgorithm=new Algorithm();
		
		//时间轴初始化
		Random rand=new Random(1);
		HashMap<String,Nodepair>map=layer.getNodepairlist();
        Iterator<String>iter=map.keySet().iterator();
		while(iter.hasNext()){
             Nodepair nodepair=(Nodepair)(map.get(iter.next()));
	   	     array[i]=new Time(nodepair.getName(),((-Math.log(rand.nextDouble())/2)*relativeValue),1,null);
		     i++;
		}
		
		// 时间轴以大小排列 
		Range range=new Range();
		array=range.rangeTimeFrom_min_to_max(array,timeline.array_num(array));
		timeline.setTimearray(array);	
		timeline.OutputTime(array);	
	
		int k=1;//event 数
		int block_number=0;//block 数		
		Algorithm Find=new Algorithm();
		while(k<=CycleTimes){
			//workAlgorithm.Output_link_slot_status(layer);
			System.out.println("============The name of the nodepair "+array[0].getName()+"================="+k+"========="); 
			Nodepair nodepair=Find.FindNodepair(layer, array[0].getName());
			LinearRoute route=nodepair.getLinearlist().get(0);
			//route.OutputRoute_link(route);
			int block_key=0; 	 
			//到达事件
			if(array[0].getKeytime()==Constant.arrivaltime){											
				//判断是否堵塞		
				Check_block check=new Check_block();
		        block_key=check.CheckSlotUsedOfRoute(layer, route,1000,array[0]);
				
						
		        if(block_key==1){							 
		        	//block_number统计					
		        	System.err.println("堵塞,不分配波长");									
		        	block_number++;							       							
		        	double next_arrivaltime=(-Math.log(rand.nextDouble())/2)*relativeValue;							 
		        	Time next_arrival=new Time(array[0].getName(),next_arrivaltime,1,null);		 
		        	timeline.remove_arrivalTime(array);		 
		        	array[timeline.array_num(array)]=next_arrival;			                   		
		        	k++;
			        //整理时间轴    							 
		        	array=range.rangeTimeFrom_min_to_max(array,timeline.array_num(array));  		 
		        						 
		        }
						 
		        //未堵塞,分配波		
		        else{							 
		        	System.out.println("未堵塞,分配slot");		 		        		        										 
		        	//分配波长 		        
		        	workAlgorithm.assginSlots(layer, route, array[0].getOccpyfirstSlot());														 
		        	double next_arrivaltime=(-Math.log(rand.nextDouble())/2)*relativeValue;//arrival time 0-2.5 随机							 
		        	double releasetime=(-Math.log(rand.nextDouble())/2)*b;//releasetime 0-5随机 							
		        	Time next_arrival=new Time(array[0].getName(),next_arrivaltime,1, null);       	               
		        	Time release=new Time(array[0].getName(),releasetime,0,null);        	                         	               		             	                     		        	
		        	release.setOccpyfirstSlot(array[0].getOccpyfirstSlot());
		        	release.setOccpyendSlot(array[0].getOccpyendSlot());
		        	//调整时间轴       	           
		        	timeline.remove_arrivalTime(array);                        
		        	array[timeline.array_num(array)]=next_arrival;        	          
		        	array[timeline.array_num(array)]=release;        	         
		        	array=range.rangeTimeFrom_min_to_max(array,timeline.array_num(array));   	        
		        	k++;//event+1；	
		        	System.out.println();
		        }					 
			}					 
			//释放事件					
			else{										
				System.out.println("释放slot :"+array[0].getOccpyfirstSlot()+"--"+array[0].getOccpyendSlot());						
				workAlgorithm.DepartureSlots(layer, route, array[0].getOccpyfirstSlot());						
				timeline.remove_releaseTime(array,timeline);				
				int size=timeline.array_num(array);			        				
				array=range.rangeTimeFrom_min_to_max(array,size);	
				System.out.println();
			}
				
		}
	
		double result=(double)block_number/k;
		System.out.println("block_number："+block_number);
		System.out.println("block_probility:"+result);	
	}
}


