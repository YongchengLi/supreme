package workPackage;

import general.ReadFile;

public class testWork {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ReadFile readDemandFile=new ReadFile();
		String  newText=readDemandFile.readTxtFileFunction("f:/readfile.txt");
		System.out.println(newText);
	}

}
