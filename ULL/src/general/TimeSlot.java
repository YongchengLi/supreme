package general;

public class TimeSlot {

	private double startTime = 0;
	private double endTime = 0;
	private double occupyCapacity = 0;
	private Task task = null;
	public double getStartTime() {
		return startTime;
	}
	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}
	public double getEndTime() {
		return endTime;
	}
	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}
	
	public double getOccupyCapacity() {
		return occupyCapacity;
	}
	public void setOccupyCapacity(double occupyCapacity) {
		this.occupyCapacity = occupyCapacity;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	
	
}
