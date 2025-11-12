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

		List<Read> list_reads = new LinkedList<Read>() ;

		BufferedReader br = new BufferedReader(new FileReader(monFichierTexte)) ;
		String line ;
		while ((line = br.readLine()) != null) {
			Read r1 = new Read(line) ;
			list_reads.add(r1) ;
		}
		br.close() ;

		Contig contig1 = new Contig(list_reads.get(0));
		System.out.println(contig1.fastaFormat());
		
	}
}
