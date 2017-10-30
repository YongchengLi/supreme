package general;





public class W_wc{

	private int[] W=null;
	public  int[] getW() {
		return W;
	}

	public void setW(int[] w) {
		W = w;
	}

	public W_wc(int number) {
		
		// TODO Auto-generated constructor stub
		this.W=new int[number];
		
	}
	public void Init_W(){
		 for(int i=0;i<this.W.length;i++){
			this.W[i]=0;
		}
	}
	public int Static_W(){
		int Remain_number=0;
		for(int i=0;i<this.W.length-1;i++){
			if(this.W[i]==0){
				 Remain_number++;
			}
		}
		return Remain_number;
	}
	public int Check_Free(){
		int  Free_number=0;
		int i=0;
		while(i<this.W.length){
			if(this.W.equals(1)){
				i++;
			}
			else{
				Free_number=i;
				break;
			}
		}
		return Free_number;
	}
	

}
