package general;

import rapidDataEvacuation.Content;
import rapidDataEvacuation.DataCenter;



public class Block {
	
	private int size = 0;	//data size
	private boolean datablock = false; //indentify the datablock
	private boolean parityblock = false; //indentify the parityblock
	private Content ordinaryContent = null; //ordinaryContent
	private DataCenter dataCenter = null;
	private boolean safe = true;
	private String name = "";
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isDatablock() {
		return datablock;
	}

	public void setDatablock(boolean datablock) {
		this.datablock = datablock;
	}

	public boolean isParityblock() {
		return parityblock;
	}

	public void setParityblock(boolean parityblock) {
		this.parityblock = parityblock;
	}

	public Content getOrdinaryContent() {
		return ordinaryContent;
	}

	public void setOrdinaryContent(Content ordinaryContent) {
		this.ordinaryContent = ordinaryContent;
	}

	public DataCenter getDataCenter() {
		return dataCenter;
	}

	public void setDataCenter(DataCenter dataCenter) {
		this.dataCenter = dataCenter;
	}

	public boolean isSafe() {
		return safe;
	}

	public void setSafe(boolean safe) {
		this.safe = safe;
	}
	
	
	

}
