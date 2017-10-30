package rapidDataEvacuation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import subgraph.LinearRoute;

import Network.Layer;
import Network.Link;
import Network.Node;
import Network.Nodepair;
import functionAlorithms.Functions_Rapid_DataEvacuation;
import functionAlorithms.Functions_Search_Route;
import functionAlorithms.TestFunctions;
import general.Constant;

public class Test {

	//input file
		public static String ReadFile =  "24_1";
		public static String ReadFileName = ReadFile+".CSV";//input file name
		public static String ReadRiskFileName = "24_1_R.CSV";//
		//input earthquake fields
		public static int earX = 100;
		public static int earY = 100;
	public static int earStrength = 1;
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random rand = new Random(1);
		int DataCenter = rand.nextInt(Constant.maxiIndex-Constant.miniIndex)+Constant.miniIndex;
		System.out.println(DataCenter);
	}

}
