import edu.duke.*;

/**
 * Add a gene processor to our tools.  It
 * - prints all the genes longer than 9 nucleotides
 * - prints the count of genes longer than 9 nucleotides
 * - prints the genes with CG ratio > 0.35
 * - prints the count of high CG ratio genes 
 * - prints the length of the longest gene
 * 
 * @author Jeff Brown
 * @version 1
 */
public class Part3 {
    private String INDENT = "  ";
    
    /** Given a StorageResource `sr` of genes, run the following analyses:
     *  - print all the genes longer than 9 characters and their count
     *  - print all the genes with a high cgRatio (> 0.35) and their count
     *  - print the length of the longest gene
     * 
     * @param sr    the collection of genes to analyze.
     * @param minLength     print genes longer than this length
     */
    public void processGenes (StorageResource sr, int minLength) {
        genesLongerThan(sr, minLength);
        genesHighCGRatio(sr);
        genesLongestLength(sr);
    }
    
    // Helper for processGenes(), prints all genes with length > `minLength`, and their count.
    private void genesLongerThan (StorageResource sr, int minLength) {
        int count = 0;
        System.out.println("Genes longer than "+minLength+" nucleotides:");
        for (String gene : sr.data()) {
            if (gene.length() > minLength) {
                count = count + 1;
                System.out.println(INDENT+gene);
            }
        }
        System.out.println(INDENT+"Total = "+count);
    }
    
    // Helper for processGenes(), prints all genes with a high CG ratio, and their count.
    private void genesHighCGRatio (StorageResource sr) {
        int count = 0;
        Part2 part2 = new Part2(); // to use code there
        System.out.println("Genes with CG ratio > 0.35:");
        for (String gene : sr.data()) {
            if (part2.cgRatio(gene) > 0.35) {
                count = count + 1;
                System.out.println(INDENT+gene);
            }
        }
        System.out.println(INDENT+"Total = "+count);
    }
    
    // Helper for processGenes(), prints the length of the longest gene.
    private void genesLongestLength (StorageResource sr) {
        int longest = 0;  // no gene will be less than zero length
        
        for (String gene : sr.data()) {
            int len = gene.length();
            if (len > longest) longest = len;
        }
        System.out.println("Longest gene is "+longest+" nucleotides.");
    }
    
    /** Test driver for processGenes(). */
    public void testProcessGenes () {
        Part1 part1 = new Part1();  // to use getAllGenes()
        
        System.out.println("Test 1:  empty dna, so no genes found and counts are zero.");
        processGenes(part1.getAllGenes(""), 9);
        System.out.println("");
        
        System.out.println("Test 2:  dna with no genes, so no genes found and counts are zero.");
        processGenes(part1.getAllGenes("CCCCCCCCCCCCC"), 9);
        System.out.println("");
        
        System.out.println("Test 3:  dna with 1 gene of 6, low CG, counts zero but longest 6.");
        processGenes(part1.getAllGenes("ATGTAA"), 9);
        System.out.println("");
        
        System.out.println("Test 4:  dna with gene length 9, high CG, so 0 longer than 9, 1 high CG, longest 9.");
        processGenes(part1.getAllGenes("ATGCCCTAA"), 9);
        System.out.println("");
        
        System.out.println("Test 5:  2 genes, 1 length 6 1 length 12, low CG, so 1 longer than 9, 0 high CG, longest 12.");
        processGenes(part1.getAllGenes("ATGTAAbATGTTTCCCTAA"), 9);
        System.out.println("");
        
        System.out.println("Test 6:  2 genes length 12 & 15, 1 low CG 1 high.  2 longer than 9, 1 high CG, longest 15.");
        processGenes(part1.getAllGenes("ATGATAATATAAbATGCCCCCCCCCTAA"), 9);
        System.out.println("");
        
        // Passed quiz with 100%, question for this file showed 1 gene longer than 60 and 1 high CG.
        System.out.println("dna downloaded from Duke, 1 gene over 60 and it has high CG, so both counts are 1.");
        FileResource fr = new FileResource("dna/brca1line.fa");
        //FileResource fr = new FileResource("dna/jeff.fa");
        //FileResource fr = new FileResource();   // go pick the file.
        String dna = fr.asString().toUpperCase();
        System.out.println("dna length = "+dna.length());
        processGenes(part1.getAllGenes(dna), 60);
        System.out.println("");        
    }
    
}  // class Part3
