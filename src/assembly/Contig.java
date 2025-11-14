package assembly;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Contig implements Sequence{

	private int len ;
	private String contig ;
	private int nb_fusions ;
	
	@Override
	public int getLength() {
		return this.len;
	}

	public int getNbFusion(){ // permet d'utiliser nb_fusion en dehors de la Class Contig 
		return this.nb_fusions;
	}

	@Override
	public String getSeq() {
		return this.contig;
	}

	public Contig() {
		contig = "azertyuiopqsdfghjklmwxcvbnazertyuiopdfghjklmqsdfghjklmllllkjhgfdsqsdfgaaaaaaaaaaacccccccccccccccccccccctttttttttttttttttttddddddddddddddddddhjklm"; 
		len = contig.length();
		nb_fusions = 0;
	}

	public Contig(String s) {
		contig = s; 
		len = contig.length();
		nb_fusions = 0;
	}
	
	public Contig(Read r) {
		contig = r.getSeq(); 
		len = contig.length();
		nb_fusions = 0;
	}
	

	@Override
	public String fastaFormat() {
		StringBuilder sb = new StringBuilder();

		// ligne d'en-tete
		sb.append(">contig_").append(nb_fusions).append("\n"); 

		// 60 nucleotide par ligne 
		for (int i = 0; i < contig.length(); i += 60) {
			int end = Math.min(i + 60, contig.length());
			sb.append(contig, i, end).append("\n");
		}

		return sb.toString();
	}

	public int bestOverlap(Read r) {
		String seqRead = r.getSeq();
		int maxOverlap = 0;
		int maxPossible = Math.min(this.contig.length(), seqRead.length());

		// on teste tout les chevauchement possible 
		for (int k = 1; k <= maxPossible; k++) {
			String endContig = this.contig.substring(this.contig.length() - k);
			String startRead = seqRead.substring(0, k);

			if (endContig.equals(startRead)) {
				maxOverlap = k; // on garde le plus grand k trouvé
			}
		}
		return maxOverlap;
	}

	public int bestOverlapWithError(Read r, float perror) {
		String seqRead = r.getSeq();
		int maxOverlap = 0;
		int maxPossible = Math.min(this.contig.length(), seqRead.length());

		// on teste tout les chevauchement possible 
		for (int k = 1; k <= maxPossible; k++) {
			String endContig = this.contig.substring(this.contig.length() - k);
			String startRead = seqRead.substring(0, k);

			if (Read.nearlyEquals(endContig, startRead, perror)) {
				maxOverlap = k; // on garde le plus grand k trouvé
			}
		}
		return maxOverlap;
	}


	public int nextRead(List<Read> l) {
		int bestIndex = -1;
		int bestOverlap = 0;

		for (int i = 0; i < l.size(); i++) {
			Read current = l.get(i);
			int overlap = this.bestOverlap(current);

			if (overlap > bestOverlap) {
				bestOverlap = overlap;
				bestIndex = i;
			}
		}

		if (bestOverlap < 8) {
			return -1;
		}

		return bestIndex;
	}
	
	public int nextReadWithErrors(List<Read> l, float perror) {
		int bestIndex = -1;
		int bestOverlap = 0;

		for (int i = 0; i < l.size(); i++) {
			Read current = l.get(i);
			int overlap = this.bestOverlapWithError(current, perror);

			if (overlap > bestOverlap) {
				bestOverlap = overlap;
				bestIndex = i;
			}
		}

		if (bestOverlap < 8) {
			return -1;
		}

		return bestIndex;
	}


	public Contig fusion(Read r) {
		int overlap = this.bestOverlap(r);
		String newSeq = this.contig + r.getSeq().substring(overlap);

		Contig nouveau = new Contig(newSeq);
		nouveau.nb_fusions = this.nb_fusions + 1;

		return nouveau;
	}

	public Contig fusionWithError(Read r, float perror) {
    	int overlap = this.bestOverlapWithError(r, perror);
    	String newSeq = this.contig + r.getSeq().substring(overlap);
    	Contig nouveau = new Contig(newSeq);
    	nouveau.nb_fusions = this.nb_fusions + 1;
    	return nouveau;
	}


	public static void main(String[] args) throws IOException {
		System.out.println("=== TESTS DE LA CLASSE CONTIG ===");

		// ---------- Test 1 : constructeur ----------
		Read r1 = new Read("ACGTACGT");
		Contig c1 = new Contig(r1);

		System.out.println("Test constructeur : " + c1.getSeq());
		// attendu : ACGTACGT

		// ---------- Test 2 : bestOverlap ----------
		Read r2 = new Read("ACGTGGGG");
		int overlap = c1.bestOverlap(r2);

		System.out.println("Test bestOverlap (attendu = 4) : " + overlap);
		// ACGTACGT
		//     ACGTGGGG
		// overlap = ACGT = 4

		// ---------- Test 3 : fusion ----------
		Contig c2 = c1.fusion(r2);
		System.out.println("Test fusion : " + c2.getSeq());
		// attendu : ACGTACGT + GGGG = ACGTACGTGGGG

		// ---------- Test 4 : nextRead ----------
		LinkedList<Read> list = new LinkedList<>();
		list.add(new Read("CGTAAAA"));   // overlap 3
		list.add(new Read("ACGTGG"));    // overlap 4 (meilleur)
		list.add(new Read("TTTTTT"));    // overlap 0

		int idx = c1.nextRead(list);
		System.out.println("Test nextRead (attendu = 1) : " + idx);

		// ---------- Test 5 : bestOverlapWithError ----------
		Read r3 = new Read("ACCTGGGG");  // 1 erreur dans "ACGT" → 75% correct
		int overlapErr = c1.bestOverlapWithError(r3, 3.0f);

		System.out.println("Test bestOverlapWithError (>3 attendu) : " + overlapErr);

		// ---------- Test 6 : fusionWithError ----------
		Contig c3 = c1.fusionWithError(r3, 3.0f);
		System.out.println("Test fusionWithError : " + c3.getSeq());
		
    }

	
}
