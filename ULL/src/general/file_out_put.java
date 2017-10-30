package general;

import java.io.*;
import java.util.StringTokenizer;

public class file_out_put {
	
  public  void filewrite(String filename,String result){
		 
	  FileOutputStream fos=null;
      try{
		  fos=new FileOutputStream(filename,true);
		  byte[] buffer1=result.getBytes();
		  fos.write(buffer1);
		  String str="\r\n";
		 
		  byte[] buffer=str.getBytes();
		  fos.write(buffer);
		  fos.close();
      }
      catch(FileNotFoundException e1){
    	  System.out.println(e1);	
    	}
    	catch(IOException e1){
    		 System.out.println(e1);	
    	}
  }
  public  void filewrite(String filename,double result){
	  FileOutputStream fos=null;
  try{
	  fos=new FileOutputStream(filename,true);
	  String str1=String.valueOf(result);
	  byte[] buffer1=str1.getBytes();
	  fos.write(buffer1);
	  String str="\r\n";
	  byte[] buffer=str.getBytes();
	  fos.write(buffer);
	  fos.close();
	  
}
  catch(FileNotFoundException e1){
	  System.out.println(e1);	
	}
	catch(IOException e1){
		 System.out.println(e1);	
	}
}
  public  void filewriteContinuous(String filename,String result){
	  
	  FileOutputStream fos=null;
  
	  try{
	  
		  fos=new FileOutputStream(filename,true);
		  byte[] buffer1=result.getBytes(); 
		  fos.write(buffer1);
		  String str="";
	  
		  byte[] buffer=str.getBytes();  
		  fos.write(buffer);
		  fos.close();
	  }
	  catch(FileNotFoundException e1){ 
		  System.out.println(e1);	
	  }
	  catch(IOException e1){	
		  System.out.println(e1);
	  }

  }
}
