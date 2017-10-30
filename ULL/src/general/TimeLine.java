package general;

import general.CommonObject;
import general.Time;


public class TimeLine extends CommonObject {
    
	private Time[] timearray=null;
	
	public TimeLine(String name, int index, String comments,int number) {
		super(name, index, comments);
		// TODO Auto-generated constructor stub
	}

	public Time[] getTimearray() {
		return timearray;
	}

	public void setTimearray(Time[] timearray) {
		this.timearray = timearray;
	}
    public void remove_arrivalTime(Time[] array){
    	   int num=1;
     	   double     removetime=array[0].getTime();
      	   while(array[num]!=null){
      		  double relative_time=array[num].getTime();
      		  array[num].setTime(relative_time-removetime);
      		  array[num-1]=array[num];
 	    	  num++;	
 		   }
      	   array[num-1]=null;
    }
    public void remove_releaseTime(Time[] array,TimeLine timeline){
    	   int num=0;
   	       while(num<timeline.array_num(array)){
   	    	      
   		          array[num]=array[num+1];
	    	      num++;	
		   }
    }
	public int array_num(Time[] array){
		int array_num=1;
		while(array[array_num]!=null){
			array_num++;
		}
		return array_num;
	}
	public void OutputTime(Time[] array){
		   int num=0;
 	       while(array[num]!=null){
 		          System.out.println(array[num].getName()+"---"+array[num].getTime()+"-------"+array[num].getKeytime());
	    	      num++;	
		   }
	}
}
