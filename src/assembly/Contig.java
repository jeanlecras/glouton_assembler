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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSeq() {
		// TODO Auto-generated method stub
		return null;
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
	
	public static void main(String[] args) throws IOException {
		System.out.println(System.getProperty("user.dir"));

		String filename = "/scr/assembly/my_reads.txt" ;
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
	}
}
