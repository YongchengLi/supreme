package rapidDataEvacuation;

import general.CommonObject;

public class Earthquake extends CommonObject{
	
	
	private int x = 0;
	private int y = 0;
	private double strength = 0;
	
	public Earthquake(String name, int index, String comments) {
		super(name, index, comments);
		// TODO Auto-generated constructor stub
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public double getStrength() {
		return strength;
	}

	public void setStrength(double strength) {
		this.strength = strength;
	}

	
	

}
