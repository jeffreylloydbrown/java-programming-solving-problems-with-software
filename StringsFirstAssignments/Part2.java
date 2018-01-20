
/**
 * Finding a Gene - Using the Simplified Algorithm Reorganized
 * 
 * @author Jeff Brown
 * @version 1
 */
public class Part2 {
    /** Extract the gene sequence from the DNA strand.
     * 
     * @param dna  a String of nucleotides that may or may not contain a gene.
     * @param startCodon   the start codon for the gene, e.g. "ATG"
     * @param stopCodon    the stop codon for the gene, e.g. "TAA"
     * @returns     the first gene found, including start and stop codon.  If 
     *              no gene found, returns an empty string.
     */
    public String findSimpleGene (String dna, String startCodon, String stopCodon) {
        // Make sure we got passed actual strings to work with.
        // if not, no gene so return an empty string.
        if (dna.length() == 0 || startCodon.length() == 0 || stopCodon.length() == 0) {
            return "";
        }
        
        // The first letter of the dna determines if we work in uppercase
        // or lowercase.  Look at it, then adjust all 3 arguments to match.
        if (Character.isUpperCase(dna.charAt(0))) {
            dna = dna.toUpperCase();
            startCodon = startCodon.toUpperCase();
            stopCodon = stopCodon.toUpperCase();
        } else {
            dna = dna.toLowerCase();
            startCodon = startCodon.toLowerCase();
            stopCodon = stopCodon.toLowerCase();
        }
        // Find the first occurrence of the start codon
        // if we don't find it, then we don't have a gene
        // so return an empty string.
        int startIndex = dna.indexOf(startCodon);
        if (startIndex == -1) {
            return "";
        }
        
        // Find the first occurrence of the stop codon
        // after the start codon.  Remember we know the stop
        // codon cannot appear within the start codon, so 
        // start looking 3 characters (length of START_CODON)
        // past the start index.  If we don't find the stop codon,
        // we also don't have a gene so return an empty string.
        int stopIndex = dna.indexOf(stopCodon, startIndex+3);
        if (stopIndex == -1) {
            return "";
        }
        
        // Now confirm the gene has the correct length.  It has
        // to be a multiple of 3.  If not, return an empty string.
        if ((stopIndex+3 - startIndex) % 3 != 0) {
            return "";
        }
        
        // Pull the gene out of the DNA string.  Remember we need
        // to include the full stop codon, so we go 3 past the stop
        // index.        
        return dna.substring(startIndex, stopIndex+3);
    }
    
    /** Test driver for findSimpleGene(). */
    void testSimpleGene() {
        String dna;
        String START_CODON = "ATG";
        String STOP_CODON = "TAA";
        
        // Test 1: no start codon, so no gene
        dna = "TAAGTA";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna, START_CODON, STOP_CODON) + "' expect ''");

        // Test 2: no stop stop codon, so no gene
        dna = "ATGTTTCCCTAT";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna, START_CODON, STOP_CODON) + "' expect ''");
        
        // Test 3: no codons at all, so no gene
        dna = "ATAGGGACG";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna, START_CODON, STOP_CODON) + "' expect ''");
        
        // Test 4: valid codons, but no other nucleitides.  Will be seen
        // as valid, for now.
        dna = "ATGTAA";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna, START_CODON, STOP_CODON) + "' expect ATGTAA");
        
        // Test 5: valid codons, but not in expected order.  No gene.
        dna = "TAAATGTAC";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna, START_CODON, STOP_CODON) + "' expect ''");       
        
        // Test 6:  valid codons, but gene isn't right length.  No gene.
        dna = "AATGCCTAAC";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna, START_CODON, STOP_CODON) + "' expect ''");
        
        // Test 7:  valid codons, correct length.  Result is ATGCCCTAA.
        dna = "CATGCCCTAAG";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna, START_CODON, STOP_CODON) + "' expect ATGCCCTAA");
        
        // Test 8:  no dna, so no gene.  
        dna = "";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna, START_CODON, STOP_CODON) + "' expect ''");
        
        // Test 9:  no start codon, so no gene.  
        dna = "CATGCCCTAAG";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna, "", STOP_CODON) + "' expect ''");
        
        // Test 10:  no stop codon, so no gene.  
        dna = "CATGCCCTAAG";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna, START_CODON, "") + "' expect ''");
        
        // Test 11:  lowercase first letter means lowercase result 
        dna = "cATGCCCTAAG";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna, START_CODON, STOP_CODON) + "' expect atgccctaa");
        
        // Test 12:  uppercase first letter means uppercase result 
        dna = "Catgccctaag";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna, START_CODON, STOP_CODON) + "' expect ATGCCCTAA");
    }
}
