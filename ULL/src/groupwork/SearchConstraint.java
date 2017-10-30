package groupwork;

import general.Constant;

import java.util.ArrayList;

import Network.*;

public class SearchConstraint {
           private ArrayList<Node>excludednodelist=null;
           private ArrayList<Link>excludedlinklist=null;
           private double max_length=Constant.maxium;
           private int    max_hop=Constant.maxium;
		public ArrayList<Node> getExcludednodelist() {
			return excludednodelist;
		}
		public void setExcludednodelist(ArrayList<Node> excludednodelist) {
			this.excludednodelist = excludednodelist;
		}
		public ArrayList<Link> getExcludedlinklist() {
			return excludedlinklist;
		}
		public void setExcludedlinklist(ArrayList<Link> excludedlinklist) {
			this.excludedlinklist = excludedlinklist;
		}
		public double getMax_length() {
			return max_length;
		}
		public void setMax_length(double max_length) {
			this.max_length = max_length;
		}
		public int getMax_hop() {
			return max_hop;
		}
		public void setMax_hop(int max_hop) {
			this.max_hop = max_hop;
		}
		public   SearchConstraint(double max_length){
			super();
			this.excludedlinklist=new ArrayList<Link>();
			this.excludednodelist=new ArrayList<Node>();
			this.max_length=max_length;
		}
		public SearchConstraint(double max_length,int max_hop){
			super();
			this.excludedlinklist=new ArrayList<Link>();
			this.excludednodelist=new ArrayList<Node>();
			this.max_length=max_length;
			this.max_hop   =max_hop;
		} 
}
