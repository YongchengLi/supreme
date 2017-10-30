package subgraph;

import rapidDataEvacuation.DataCenter;

public class RoutePair {

	private LinearRoute FirstRoute = null;
	private LinearRoute SecondRoute = null;
	
	private DataCenter firstRouteAssociatedDataCenter = null;
	private DataCenter secondRouteAssociatedDataCenter = null;
	
	public LinearRoute getFirstRoute() {
		return FirstRoute;
	}
	public void setFirstRoute(LinearRoute firstRoute) {
		FirstRoute = firstRoute;
	}
	public LinearRoute getSecondRoute() {
		return SecondRoute;
	}
	public void setSecondRoute(LinearRoute secondRoute) {
		SecondRoute = secondRoute;
	}
	public DataCenter getFirstRouteAssociatedDataCenter() {
		return firstRouteAssociatedDataCenter;
	}
	public void setFirstRouteAssociatedDataCenter(
			DataCenter firstRouteAssociatedDataCenter) {
		this.firstRouteAssociatedDataCenter = firstRouteAssociatedDataCenter;
	}
	public DataCenter getSecondRouteAssociatedDataCenter() {
		return secondRouteAssociatedDataCenter;
	}
	public void setSecondRouteAssociatedDataCenter(
			DataCenter secondRouteAssociatedDataCenter) {
		this.secondRouteAssociatedDataCenter = secondRouteAssociatedDataCenter;
	}
	
	
}
