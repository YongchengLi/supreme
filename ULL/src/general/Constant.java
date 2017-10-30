package general;

public class Constant {
     public static int maxium=100000000;
     public static int UNVISITED=0;
     public static int VISITED=1;
     public static int VISITEDTWICE=2;
     public static int WORKING=1;
     public static int PROTECT = 2;
     public static int UNBALACENED=0;
     public static int BALACENED=1;
     public static int arrivaltime=1;
     public static int releasetime=0;
     public static int Directional=1;
     public static int Directionless=0;
     public static int Color=1;
     public static int Colorless=0;
     
     public static double firstFEC=0.0669;
     public static double secondFEC=0.1334;
     public static double thirdFEC=0.212;
     
     public static double firstOSNR=14.5;
     public static double secondOSNR=12.6;
     public static double thirdOSNR=9.1;
    		 
     
     public static int f1=1;
     public static int f2=2;
     public static int f3=3;
     
    
     public static double lightSpeed = 200000;//km/s
    
     //*********************************Quais-CWDM********************************//
    
     public static String QPSK="QPSK";
     public static String BPSK="BPSK";
     public static String QAM ="QAM";
 	
 	
     public static int BPSK_DISTANCE_LIMIT =4000;     
     public static int QPSK_DISTANCE_LIMIT = 2000;                   //距离限制   
     public static int QAM_DISTANCE_LIMIT =1000;
 		 
     public static double BPSK_COST_OF_REGEN =0.5;//0.7;//0.5;//1;                   //再生器价格
	 public static double BPSK_COST_OF_ROUTER_PORT = 1;//1.4;//1;//2;            //路由器价格		
     
     public static double QPSK_COST_OF_REGEN = 0.65;//0.9;//0.65;//1.3;                   //再生器价格	
	 public static double QPSK_COST_OF_ROUTER_PORT = 1.3;//1.8;//1.3;//2.6;            //路由器价格
		
	 public static double QAM_COST_OF_REGEN = 0.75;//1;//0.75;//1.5;                   //再生器价格
	 public static double QAM_COST_OF_ROUTER_PORT = 1.5;//2;//1.5;//3;            //路由器价格
     
	 public static int SERVED_TYPE_ONE = 1;
	 public static int SERVED_TYPE_TWO = 2;
	 public static int SERVED_TYPE_THREE = 3;
	 
	 public static double BPSK_CAPACITY = 350;                            //BPSK容量-PMD
	 public static double QPSK_CAPACITY = 700;                            //QPSK容量-PMD
	 public static double QAM_CAPACITY  = 1050;                            //QAM容量-PMD
  
	 //********************************data********************************************//
	 //model of different strategy
	 public static String mode1 = "NRDE";
	 public static String mode2 = "LDRDE";
	 public static String mode3 = "MSCRDE";
	 
	 public static int disLimit = 2000;//unit km
	 public static int damagedDistance = 1000;
	
	 //number of sub-content
	 public static int subNumber = 2;
	 
	 //number of candidate Route;
	 public static int routeNumber = 3;
	 
	 //number of totalContent
	 public static int totalNumofConetnt = 3;
	
	 //number of contnet
	 public static int conNum = 100;
	
	 
	 public static int size = 10;
	
	 public static int Num = 79;
	 
	 //residual capacity
	 public static int maximumLinkCapacity = 1024; //1TB = 1024G
	 public static int minimumLinkCapacity = 500;//500;  //500G
	 public static double percentageLinkCapacity = 1.0; //percent
	 
	 //data center free stroage
	 public static int maximumStorage = 1024000; //100 TB
	 public static int minimumStorage = 102400; //10 TB
	 public static double percentageStorage = 0.6; //percent
	 
	 //content size
	 public static int maximumSize = 500; //GB
	 public static int minimumSize = 200; //GB
	
	 //replicas of the contents
	 public static int minimumReplicas = 2;//
	 public static int maximumReplicas = 3;//
	 
	 //Num of data center
	 public static int miniIndex = 0;
	 public static int maxiIndex = 8;//8;//6
	 
	 //weight of the contents
	 public static int minimumWeight = 1;
	 public static int maximumWeight = 10;
	 
	 //parameters of delay calculation
	 public static int processingDelay = 10;//us
	 public static int propgagationDelay = 5;//us/km
	 public static int swicthconfigurationDelay = 15000;//us
	 

	 //a small weight value
	 public static double weightValue = 3;
	 
	 //Content Fragmentation
	 public static int k = 4;
	 public static int r = 2;
	 
	 
	 
	 //***********************ULL*************************//
	 
	 public  static int ULL_Fiber_per_Day = 30 ;//每天铺设的光纤公里数
	 
	 
}
