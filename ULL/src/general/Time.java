package general;

import java.util.ArrayList;

import subgraph.LinearRoute;
import Network.Layer;
import Network.LightPath;
import Network.Nodepair;

public class Time {
	   String name;
	   double time;
	   int keytime;
	   LinearRoute route;
	   int    route_index;
	   int    Occpy_W=Constant.maxium;
	   int    occpyfirstSlot=0;
	   int    occpyendSlot=0;
	   LightPath lightpath = null;
	   ArrayList<LightPath>  lightpathlist = null;
	   boolean trafficgrooming = false;
	   double capacity = 0;
	   int requiredSlotNum = 0;
	   Nodepair associatednodepair = null;
	   
	   
	public Time(String name,double time,int keytime, LinearRoute route){
			// TODO Auto-generated constructor stub
        this.name=name;
        this.time=time;
        this.keytime=keytime;
        this.route=route;
       
		}
	
	 public int getOccpy_W() {
		return Occpy_W;
	}

	public void setOccpy_W(int occpy_W) {
		Occpy_W = occpy_W;
	}

	public int getRoute_index() {
			return route_index;
		}

		public void setRoute_index(int route_index) {
			this.route_index = route_index;
		}

	public LinearRoute getRoute() {
		return route;
	}



	public void setRoute(LinearRoute route) {
		this.route = route;
	}



	public int getKeytime() {
		return keytime;
	}



	public void setKeytime(int keytime) {
		this.keytime = keytime;
	}



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	
	private int Unit;
	public int getUnit() {
		return Unit;
	}
	public void setUnit(int unit) {
		Unit = unit;
	}
	
	private int[] WaveSetList;
	public int[] getWaveSetList() {
		return WaveSetList;
	}

	public void setWaveSetList(int[] waveSetList) {
		WaveSetList = waveSetList;
	}

	public int getOccpyfirstSlot() {
		return occpyfirstSlot;
	}

	public void setOccpyfirstSlot(int occpyfirstSlot) {
		this.occpyfirstSlot = occpyfirstSlot;
	}

	public int getOccpyendSlot() {
		return occpyendSlot;
	}

	public void setOccpyendSlot(int occpyendSlot) {
		this.occpyendSlot = occpyendSlot;
	}

	public ArrayList<LightPath> getLightpathlist() {
		return lightpathlist;
	}

	public void setLightpathlist(ArrayList<LightPath> lightpathlist) {
		this.lightpathlist = lightpathlist;
	}

	public boolean isTrafficgrooming() {
		return trafficgrooming;
	}

	public void setTrafficgrooming(boolean trafficgrooming) {
		this.trafficgrooming = trafficgrooming;
	}

	public LightPath getLightpath() {
		return lightpath;
	}

	public void setLightpath(LightPath lightpath) {
		this.lightpath = lightpath;
	}

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public int getRequiredSlotNum() {
		return requiredSlotNum;
	}

	public void setRequiredSlotNum(int requiredSlotNum) {
		this.requiredSlotNum = requiredSlotNum;
	}

	public Nodepair getAssociatednodepair() {
		return associatednodepair;
	}

	public void setAssociatednodepair(Nodepair associatednodepair) {
		this.associatednodepair = associatednodepair;
	}	
	
}
