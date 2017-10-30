package Network;

import java.util.ArrayList;

import subgraph.LinearRoute;
import general.CommonObject;

public class DemandUnit extends CommonObject{

	public Nodepair nodepair = null;
	public double servicedTraffic = 0;
	
	private LinearRoute workRoute = null;
	private LinearRoute protectRoute = null;
	private ArrayList<LinearRoute> protectRouteList = null;
	
	private LightPath work_lightpath = null;
	private LightPath protect_lightpath = null;
	private ArrayList<LightPath> protectLightPathList = null;
	
	private boolean workPathStatus = false;
	private boolean protectPathStatus = false;
	
	private double trafficRequest = 0;
	
	public DemandUnit(String name, int index, String comments) {
		super(name, index, comments);
		// TODO Auto-generated constructor stub
	}


	public Nodepair getNodepair() {
		return nodepair;
	}

	public void setNodepair(Nodepair nodepair) {
		this.nodepair = nodepair;
	}

	public double getServicedTraffic() {
		return servicedTraffic;
	}

	public void setServicedTraffic(double servicedTraffic) {
		this.servicedTraffic = servicedTraffic;
	}


	public LinearRoute getWorkRoute() {
		return workRoute;
	}


	public void setWorkRoute(LinearRoute workRoute) {
		this.workRoute = workRoute;
	}


	public LinearRoute getProtectRoute() {
		return protectRoute;
	}


	public void setProtectRoute(LinearRoute protectRoute) {
		this.protectRoute = protectRoute;
	}


	public LightPath getWork_lightpath() {
		return work_lightpath;
	}


	public void setWork_lightpath(LightPath work_lightpath) {
		this.work_lightpath = work_lightpath;
	}


	public LightPath getProtect_lightpath() {
		return protect_lightpath;
	}


	public void setProtect_lightpath(LightPath protect_lightpath) {
		this.protect_lightpath = protect_lightpath;
	}


	public boolean isWorkPathStatus() {
		return workPathStatus;
	}


	public void setWorkPathStatus(boolean workPathStatus) {
		this.workPathStatus = workPathStatus;
	}


	public boolean isProtectPathStatus() {
		return protectPathStatus;
	}


	public void setProtectPathStatus(boolean protectPathStatus) {
		this.protectPathStatus = protectPathStatus;
	}


	public double getTrafficRequest() {
		return trafficRequest;
	}


	public void setTrafficRequest(double trafficRequest) {
		this.trafficRequest = trafficRequest;
	}


	public ArrayList<LinearRoute> getProtectRouteList() {
		return protectRouteList;
	}


	public void setProtectRouteList(ArrayList<LinearRoute> protectRouteList) {
		this.protectRouteList = protectRouteList;
	}


	public ArrayList<LightPath> getProtectLightPathList() {
		return protectLightPathList;
	}


	public void setProtectLightPathList(ArrayList<LightPath> protectLightPathList) {
		this.protectLightPathList = protectLightPathList;
	}

	
	
}
