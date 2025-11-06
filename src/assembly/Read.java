
public class Read implements Sequence{

	int len ;
	String seq ;
	
	public static void main(String[] args) {
		Read r1 = new Read("azertyuiop");
		Read r2 = new Read("ertyuiopuui");
	//	System.out.println(r1.bestOverlap(r2));
	}
	
	public Read() {
		seq = "azertyuiopqsdfghjklmwxcvbnazertyuiopdfghjklmqsdfghjklmllllkjhgfdsqsdfgaaaaaaaaaaacccccccccccccccccccccctttttttttttttttttttddddddddddddddddddhjklm"; 
		len = seq.length();
	}

	public Read(String s) {
		seq = s; 
		len = seq.length();
	}
	

	
}
