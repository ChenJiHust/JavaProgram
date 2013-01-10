package cj.test.animal;

public class cat {

	private int catNumber;
	public cat(int i){
		catNumber = i;
	}
	public void id(){
		System.out.println("cat #" + catNumber);
	}
	
	public String toString(){
		return "Cat address:" + super.toString() + "\n"; 
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		cat a = new cat(4);
		System.out.println(a.toString());

	}
	

}
