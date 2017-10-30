package general;

public class Modulation {

	private String name = null;
	private double OSNR_Limit = 0;
	private double modRate = 0;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getOSNR_Limit() {
		return OSNR_Limit;
	}
	public void setOSNR_Limit(double oSNR_Limit) {
		OSNR_Limit = oSNR_Limit;
	}
	public double getModRate() {
		return modRate;
	}
	public void setModRate(double modRate) {
		this.modRate = modRate;
	}
	
	
	
}
