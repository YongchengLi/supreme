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
		
		System.out.println("�����ȡ��Topology�ļ���csv�ļ�����");
		String open_name = scannerobject.next();                              //���������ļ�
		
		//System.out.println("�������ݽ��д���ļ���txt�ļ���:");
		//String writename = scannerobject.next();                             //�������ݽ��
		
		
		Layer layer;
		layer=new Layer("worklayer", 0, "");
		layer.readTopology(open_name);                                        //��ȡ���˽ṹ                                   
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
	 * �������
	 * @param layer
	 * @param trafficload
	 * @param ����Trafficload�����ֵ  relativeValue  
	 * @param ѭ������ CycleTimes
	 */
	public static void simulationWork(Layer layer, int trafficload,int relativeValue,int CycleTimes){
		 
		int b=trafficload;int i=0;
		Time array[]=new Time[10000];		
		TimeLine timeline=new TimeLine("timeline",1,"",layer.getNodepairlist().size());
	    
		 Algorithm workAlgorithm=new Algorithm();
		
		//ʱ�����ʼ��
		Random rand=new Random(1);
		HashMap<String,Nodepair>map=layer.getNodepairlist();
        Iterator<String>iter=map.keySet().iterator();
		while(iter.hasNext()){
             Nodepair nodepair=(Nodepair)(map.get(iter.next()));
	   	     array[i]=new Time(nodepair.getName(),((-Math.log(rand.nextDouble())/2)*relativeValue),1,null);
		     i++;
		}
		
		// ʱ�����Դ�С���� 
		Range range=new Range();
		array=range.rangeTimeFrom_min_to_max(array,timeline.array_num(array));
		timeline.setTimearray(array);	
		timeline.OutputTime(array);	
	
		int k=1;//event ��
		int block_number=0;//block ��		
		Algorithm Find=new Algorithm();
		while(k<=CycleTimes){
			//workAlgorithm.Output_link_slot_status(layer);
			System.out.println("============The name of the nodepair "+array[0].getName()+"================="+k+"========="); 
			Nodepair nodepair=Find.FindNodepair(layer, array[0].getName());
			LinearRoute route=nodepair.getLinearlist().get(0);
			//route.OutputRoute_link(route);
			int block_key=0; 	 
			//�����¼�
			if(array[0].getKeytime()==Constant.arrivaltime){											
				//�ж��Ƿ����		
				Check_block check=new Check_block();
		        block_key=check.CheckSlotUsedOfRoute(layer, route,1000,array[0]);
				
						
		        if(block_key==1){							 
		        	//block_numberͳ��					
		        	System.err.println("����,�����䲨��");									
		        	block_number++;							       							
		        	double next_arrivaltime=(-Math.log(rand.nextDouble())/2)*relativeValue;							 
		        	Time next_arrival=new Time(array[0].getName(),next_arrivaltime,1,null);		 
		        	timeline.remove_arrivalTime(array);		 
		        	array[timeline.array_num(array)]=next_arrival;			                   		
		        	k++;
			        //����ʱ����    							 
		        	array=range.rangeTimeFrom_min_to_max(array,timeline.array_num(array));  		 
		        						 
		        }
						 
		        //δ����,���䲨		
		        else{							 
		        	System.out.println("δ����,����slot");		 		        		        										 
		        	//���䲨�� 		        
		        	workAlgorithm.assginSlots(layer, route, array[0].getOccpyfirstSlot());														 
		        	double next_arrivaltime=(-Math.log(rand.nextDouble())/2)*relativeValue;//arrival time 0-2.5 ���							 
		        	double releasetime=(-Math.log(rand.nextDouble())/2)*b;//releasetime 0-5��� 							
		        	Time next_arrival=new Time(array[0].getName(),next_arrivaltime,1, null);       	               
		        	Time release=new Time(array[0].getName(),releasetime,0,null);        	                         	               		             	                     		        	
		        	release.setOccpyfirstSlot(array[0].getOccpyfirstSlot());
		        	release.setOccpyendSlot(array[0].getOccpyendSlot());
		        	//����ʱ����       	           
		        	timeline.remove_arrivalTime(array);                        
		        	array[timeline.array_num(array)]=next_arrival;        	          
		        	array[timeline.array_num(array)]=release;        	         
		        	array=range.rangeTimeFrom_min_to_max(array,timeline.array_num(array));   	        
		        	k++;//event+1��	
		        	System.out.println();
		        }					 
			}					 
			//�ͷ��¼�					
			else{										
				System.out.println("�ͷ�slot :"+array[0].getOccpyfirstSlot()+"--"+array[0].getOccpyendSlot());						
				workAlgorithm.DepartureSlots(layer, route, array[0].getOccpyfirstSlot());						
				timeline.remove_releaseTime(array,timeline);				
				int size=timeline.array_num(array);			        				
				array=range.rangeTimeFrom_min_to_max(array,size);	
				System.out.println();
			}
				
		}
	
		double result=(double)block_number/k;
		System.out.println("block_number��"+block_number);
		System.out.println("block_probility:"+result);	
	}
}


