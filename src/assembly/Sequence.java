package assembly;

public interface Sequence {
	
	public abstract String toString() ;
	
	public abstract int getLength();
	public abstract String getSeq();
	
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
	
}
