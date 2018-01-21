
import edu.duke.*;

/**
 * Finding Web Links
 * 
 * @author Jeff Brown
 * @version 1
 */
public class Part4 {
    /** Accesses the web page at `url` and prints out each of the links to YouTube it contains.
     * 
     * @param url   The URL to search for links to YouTube
     */
    public void findYouTubeLinks (String url) {
        if (url.startsWith("http")) {
            URLResource ur = new URLResource(url);
            for (String word : ur.words()) {
                String lower = word.toLowerCase();
                int youtube = lower.indexOf("youtube.com");
                if (youtube != -1) {
                    int leftQuote = word.lastIndexOf("\"", youtube);
                    int rightQuote = word.indexOf("\"", youtube);
                    if (leftQuote != -1 && rightQuote != -1) {
                        System.out.println(word.substring(leftQuote+1, rightQuote));
                    }
                }
            }
        } else {
            System.out.println("'"+url+"' isn't a valid url.");
        }
    }

    /** Test driver for findYouTubeLinks */
    void testing() {
        System.out.println("Test 1: a page at Duke University");
        findYouTubeLinks("http://www.dukelearntoprogram.com/course2/data/manylinks.html");
        System.out.println("Test 2: an empty URL isn't valid");
        findYouTubeLinks("");
    }

}
