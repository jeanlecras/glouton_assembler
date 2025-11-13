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
		String seqContig = this.contig;

		int max
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

	public int nextRead(LinkedList<Read> l) {
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
	
	public int nextReadWithErrors(LinkedList<Read> l, float perror) {

	}


	public Contig fusion(Read r) {
		int overlap = this.bestOverlap(r);
		String newSeq = this.contig + r.getSeq().substring(overlap);

		Contig nouveau = new Contig(newSeq);
		nouveau.nb_fusions = this.nb_fusions + 1;

		return nouveau;
	}


	public static void main(String[] args) throws IOException {
		System.out.println(System.getProperty("user.dir"));

		String filename = "/src/assembly/my_reads.txt" ;
		File monFichierTexte = new File(System.getProperty("user.dir") + filename) ;

		// Simple test to verify that the file exists .
		if (monFichierTexte.exists()) {
			System.out.println("The file " + filename + " is present in the given directory\n") ;
		} else {
			System.out.println("The file " + filename + " is NOT present in the given directory ") ;
		}

		LinkedList<Read> list_reads = new LinkedList<Read>() ;

		BufferedReader br = new BufferedReader(new FileReader(monFichierTexte)) ;
		String line ;
		while ((line = br.readLine()) != null) {
			Read r1 = new Read(line) ;
			list_reads.add(r1) ;
		}
		br.close() ;

		Contig contig = new Contig(list_reads.get(0));
		list_reads.remove(0);

		// assemblage glouton
		while (true) {
			int idx = contig.nextRead(list_reads);
			
			if (idx == -1) {
				break;
			}

			// Récuperer le meilleur read
			Read best = list_reads.get(idx);

			contig = contig.fusion(best);

			list_reads.remove(idx);

			System.out.println("Fusion with " + idx + ", still " + list_reads.size() + "reads to assemble... work in process");

		}

		System.out.println("\nContig obtained with " + contig.nb_fusions + " reads");
		System.out.println(contig.fastaFormat());
		
	}
}
