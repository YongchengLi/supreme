package general;

import general.Time;

public class Range {
     public Time[] rangeTimeFrom_min_to_max(Time[] time,int number){
            int i;
	        Time text;
	        for(int j=number-1;j>=0;j--){
		              i=0;
                      while(i<j){
    	 
		                    if(time[i].getTime()>time[i+1].getTime()){
			                         text=null;
			                         text=time[i];
			                         time[i]=time[i+1];
			                         time[i+1]=text;
		                    }
		                    i++;
                      }
	        }
    
    	    return time;
    	
    }
     
     
     public int[] rangeFrom_min_to_max(int[] time,int number){
         int i;	        
         int text;       
         for(int j=number-1;j>=0;j--){
		             
        	 i=0;      
        	 while(i<j){
 	           
        		 if(time[i]>time[i+1]){
			                         
        			 text=0;
			         
        			 text=time[i];
			         
        			 time[i]=time[i+1];
			         
        			 time[i+1]=text;
		             
        		 }           
        		 i++;               
        	 }  
         }
         return time;
 	
 
     }
}
