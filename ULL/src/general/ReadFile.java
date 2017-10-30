package general;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;



public class ReadFile {
	public static BufferedReader bufread;
    private static String readStr ="";
	public String[] ReadTxtFile(String filename){
		String[] stringlist = new String[100];
		File file = new File(filename);
		BufferedReader bufRdr = null;
		try {
			bufRdr = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		try {
			line = bufRdr.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//read the first title line
		//read each line of text file
		try {
			//boolean link = false;
			while((line = bufRdr.readLine()) != null){
				 stringlist=line.split(",");
			
			
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		try {
			bufRdr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stringlist;		
	}
	 public static String readTxtFileFunction(String filename){
	        String read;
	        FileReader fileread;
	        try {
	            fileread = new FileReader(filename);
	            bufread = new BufferedReader(fileread);
	            try {
	                while ((read = bufread.readLine()) != null) {
	                    readStr = readStr + read;
	                }
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        } catch (FileNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	       // System.out.println("文件内容是:"+ "\r\n" + readStr);
	        return readStr;
	    }
}
