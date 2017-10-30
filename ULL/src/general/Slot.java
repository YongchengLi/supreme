package general;
import Network.*;
public class Slot {
	private  int vaule=0;
	private  int status=0;
	private  int associatedIndex=0;
	//***************Quasi-CWDM***********************//
	private Nodepair associatedNodepair = null;
	
	
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


	public int getAssociatedIndex() {
		return associatedIndex;
	}


	public void setAssociatedIndex(int associatedIndex) {
		this.associatedIndex = associatedIndex;
	}
	//***************Quasi-CWDM***********************//

	public Nodepair getAssociatedNodepair() {
		return associatedNodepair;
	}


	public void setAssociatedNodepair(Nodepair associatedNodepair) {
		this.associatedNodepair = associatedNodepair;
	} 
	
	
}
