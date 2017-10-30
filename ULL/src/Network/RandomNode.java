package Network;

public class RandomNode extends Node{

	
	private int data;
	
	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	public RandomNode(String name, int index, String comments,
			Layer associatedLayer, int x, int y,int data) {
		super(name, index, comments, associatedLayer, x, y);
		// TODO Auto-generated constructor stub
		this.data=data;
	}
	

}
