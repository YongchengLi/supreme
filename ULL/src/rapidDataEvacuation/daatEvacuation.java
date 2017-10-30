package rapidDataEvacuation;

import Network.Layer;
import functionAlorithms.Functions_Quasi_CWDM;
import functionAlorithms.TestFunctions;
import general.file_out_put;

public class daatEvacuation {
	
	//input file
	public static String ReadFile =  "6_1";
	public static String ReadFileName = ReadFile+".CSV";//input file name
	//input earthquake center
	public static double eqX = 111;
	public static double eqY = 111;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		//make sure the damaged data centre

	}

	public static void run(){


		//Generate Physical Layer
    	Layer phyLayer=new Layer("physicallayer", 0, "");  
        phyLayer.readTopology(ReadFileName);                                                                         
    	phyLayer.generateNodepaairs();
    	
    	//input earthquake
    	
    	
    	
		//functions
		Functions_Quasi_CWDM functions = new Functions_Quasi_CWDM();
		TestFunctions test = new TestFunctions();
	}
	
	public static void Strategy_SinglePath_SingleDestinationNode(){
		
	}
	
	public static void Strategy_MultiPath_SingleDestinationNode(){
		
	}
	
	
	public static void Strategy_MultiPath_MultiDestinationNode(){
		
	}
	
}
