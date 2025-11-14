package assembly;
import java.io.*;
import java.util.*;

public class TestWoErrors {



	
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

        // WITHOUT ERROR

		System.out.println("========WITHOUT SEQUENCING ERROR========");

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



		System.out.println("\nContig obtained with " + contig.getNbFusion() + " reads");
		System.out.println(contig.fastaFormat());
	}
}