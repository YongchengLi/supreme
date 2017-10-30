package workPackage;
import general.ReadFile;
import general.random;

import java.util.Random;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 
		//Algorithm test=new Algorithm();
		
		//test.Netwton2("f:/4.csv",23,11.5);
		ReadFile readDemandFile=new ReadFile();
		String  data=readDemandFile.readTxtFileFunction("f:/readfile14.txt");
		

		
		
	}

}
