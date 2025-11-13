public class TestWErrors {
    public static void main(String[] args) throws IOException {
		System.out.println(System.getProperty("user.dir"));

		String filenamewitherror = "/src/assembly/my_reads_with_sequencing_errors.txt";

		File monFichierTexte = new File(System.getProperty("user.dir") + filename) ;

		// test check file with error
		if (monFichierTextewitherror.exists()) {
			System.out.println("The file " + filenamewitherror + " is present in the given directory\n");
		} else {
			System.out.println("The file " + filenamewitherror + " is NOT present in the given directory ") ;
		}

		LinkedList<Read> list_readsWithError = new LinkedList<Read>() ;

		BufferedReader brwitherror = new BufferedReader(new FileReader(monFichierTextewitherror)) ;
		String linewitherror;
		while ((linewitherror = brwitherror.readLine()) != null) {
			Read r2 = new Read(linewitherror);
			list_readsWithError.add(r2);
		}
		brwitherror.close();

        // WITH ERROR

		System.out.println("========WITH SEQUENCING ERROR========");

		Contig contigWitherror = new Contig(list_readsWithError.get(0));
		float perror = 10.0f;
		list_readsWithError.remove(0);

		//assemblage glouton
		while (true) {
			int idx = contigWitherror.nextReadWithErrors(list_readsWithError, perror);

			if (idx == -1) {
				break;
			}

			// recuperer le meilleur read
			Read best = list_readsWithError.get(idx);
			contigWitherror = contigWitherror.fusionWithError(best, perror);
			list_readsWithError.remove(idx);

			System.out.println("Fusion with " + idx + ", still " + list_readsWithError.size() + "reads to assemble... work in process");
		}

		System.out.println("\nContig obtained with " + contig.nb_fusions + " reads (with sequencing errors)");
		System.out.println(contig.fastaFormat());


        
}