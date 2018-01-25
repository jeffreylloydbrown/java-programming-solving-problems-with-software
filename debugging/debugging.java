
/**
 * Write a description of debugging here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class debugging {
    public void findAbc(String input) {
        System.out.println("input = "+input);
        int index = input.indexOf("abc");
        while (true) {
            if (index == -1 || index >= input.length() - 3) {
                break;
            }
            //System.out.println("index = "+index);
            String found = input.substring(index+1, index+4);

            System.out.println(found);
            index = input.indexOf("abc", index+3);
            //System.out.println("index after updating = "+index);
        }
        System.out.println("done");
    }

    public void test() {
        //findAbc("abcd");
        //findAbc("abcdabc");
        //findAbc("woiehabchi");
        //findAbc("abcdkfjsksioehgjfhsdjfhksdfhuwabcabcajfieowj");
        findAbc("abcabcabcabca");
    }
}
