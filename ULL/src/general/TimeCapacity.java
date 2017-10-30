package general;

import java.util.ArrayList;

public class TimeCapacity {
	
	private int capacity = 0;
	private double timeDelay = 0;
	private double avaStartTime = 0;
	private double existEndTime = Constant.maxium;
	private ArrayList<TimeSlot> timeSlot = new ArrayList<TimeSlot>();
	private ArrayList<TimeGap> timeGapList = new ArrayList<TimeGap>();
	private int minimumResidualCapacity = 0;
	
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public double getTimeDelay() {
		return timeDelay;
	}
	public void setTimeDelay(double timeDelay) {
		this.timeDelay = timeDelay;
	}
	public int getMinimumResidualCapacity() {
		return minimumResidualCapacity;
	}
	public void setMinimumResidualCapacity(int minimumResidualCapacity) {
		this.minimumResidualCapacity = minimumResidualCapacity;
	}
	public ArrayList<TimeSlot> getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(ArrayList<TimeSlot> timeSlot) {
		this.timeSlot = timeSlot;
	}
	public double getAvaStartTime() {
		return avaStartTime;
	}
	public void setAvaStartTime(double avaStartTime) {
		this.avaStartTime = avaStartTime;
	}
	public double getExistEndTime() {
		return existEndTime;
	}
	public void setExistEndTime(double existEndTime) {
		this.existEndTime = existEndTime;
	}
	
	

}
