package general;

import rapidDataEvacuation.Content;
import rapidDataEvacuation.DataCenter;
import subgraph.LinearRoute;
import subgraph.RoutePair;

public class Task {
	
	private double delayTx = 0;
	private double TotalTime = 0;
	private Content content = null;
	private DataCenter dataCenter = null;
	private LinearRoute route = null;
	private RoutePair routepair = null;
	
	private double firstPathDelay = 0;
	private double secondPathDelay = 0;
	private double firstPathDelayTx = 0;
	private double secondPathDelayTx = 0;
	
	private DataCenter firstDataCenter = null;
	private DataCenter secondDataCenter = null;
	
	private RoutePair selectedRoutePair = null;
	private double leastCapacity = 0;
	private int firstSize = 0;
	private int secondSize = 0;
	
	private double startTime = 0;
	private double endTime = 0;
	
	private Block FinishBlock = null;
	
	public double getDelayTx() {
		return delayTx;
	}
	public void setDelayTx(double delayTx) {
		this.delayTx = delayTx;
	}
	public double getTotalTime() {
		return TotalTime;
	}
	public void setTotalTime(double totalTime) {
		TotalTime = totalTime;
	}
	public Content getContent() {
		return content;
	}
	public void setContent(Content content) {
		this.content = content;
	}
	public DataCenter getDataCenter() {
		return dataCenter;
	}
	public void setDataCenter(DataCenter dataCenter) {
		this.dataCenter = dataCenter;
	}
	public LinearRoute getRoute() {
		return route;
	}
	public void setRoute(LinearRoute route) {
		this.route = route;
	}
	public RoutePair getRoutepair() {
		return routepair;
	}
	public void setRoutepair(RoutePair routepair) {
		this.routepair = routepair;
	}
	public double getFirstPathDelay() {
		return firstPathDelay;
	}
	public void setFirstPathDelay(double firstPathDelay) {
		this.firstPathDelay = firstPathDelay;
	}
	public double getSecondPathDelay() {
		return secondPathDelay;
	}
	public void setSecondPathDelay(double secondPathDelay) {
		this.secondPathDelay = secondPathDelay;
	}
	public int getFirstSize() {
		return firstSize;
	}
	public void setFirstSize(int firstSize) {
		this.firstSize = firstSize;
	}
	public int getSecondSize() {
		return secondSize;
	}
	public void setSecondSize(int secondSize) {
		this.secondSize = secondSize;
	}
	public double getFirstPathDelayTx() {
		return firstPathDelayTx;
	}
	public void setFirstPathDelayTx(double firstPathDelayTx) {
		this.firstPathDelayTx = firstPathDelayTx;
	}
	public double getSecondPathDelayTx() {
		return secondPathDelayTx;
	}
	public void setSecondPathDelayTx(double secondPathDelayTx) {
		this.secondPathDelayTx = secondPathDelayTx;
	}
	public DataCenter getFirstDataCenter() {
		return firstDataCenter;
	}
	public void setFirstDataCenter(DataCenter firstDataCenter) {
		this.firstDataCenter = firstDataCenter;
	}
	public DataCenter getSecondDataCenter() {
		return secondDataCenter;
	}
	public void setSecondDataCenter(DataCenter secondDataCenter) {
		this.secondDataCenter = secondDataCenter;
	}
	public RoutePair getSelectedRoutePair() {
		return selectedRoutePair;
	}
	public void setSelectedRoutePair(RoutePair selectedRoutePair) {
		this.selectedRoutePair = selectedRoutePair;
	}
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
	public double getLeastCapacity() {
		return leastCapacity;
	}
	public void setLeastCapacity(double leastCapacity) {
		this.leastCapacity = leastCapacity;
	}
	public Block getFinishBlock() {
		return FinishBlock;
	}
	public void setFinishBlock(Block finishBlock) {
		FinishBlock = finishBlock;
	}
	
	
	
	

}
