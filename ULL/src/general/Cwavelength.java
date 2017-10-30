package general;

import Network.Nodepair;

public class Cwavelength {
       
	private  int vaule=0;
	private  int status=0;
	public Cwavelength(){
	}
	
	
	public int getVaule() {
		return vaule;
	}


	public void setVaule(int vaule) {
		this.vaule = vaule;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	} 
	
	
	//***************Quasi-WDM****************************//
	private Nodepair associatedNodepair = null;
	public  Nodepair getAssociatedNodepair() {
		return associatedNodepair;
	}


	public void setAssociatedNodepair(Nodepair associatedNodepair) {
		this.associatedNodepair = associatedNodepair;
	} 
}
