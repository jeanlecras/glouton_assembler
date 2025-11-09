package assembly;

public class Read implements Sequence{

	private int len ;
	private String seq ;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getSeq();
	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return len;
	}

	@Override
	public String getSeq() {
		// TODO Auto-generated method stub
		return seq;
	}

	private String fastaFormat() {
		StringBuilder sb = new StringBuilder();

		// ligne d'en-tete
		sb.append(">sequence\n"); // id ?

		// 60 nucleotide par ligne 
		for (int i = 0; i < seq.length(); i += 60) {
			int end = Math.min(i + 60, seq.length());
			sb.append(seq, i, end).append("\n");
		}

		return sb.toString();
	}
	
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
