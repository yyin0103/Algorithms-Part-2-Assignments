/* *****************************************************************************
 *  Topic:      Circular Suffix Array
 *              The circular suffix array describes the abstraction of a sorted
 *              array of the n circular suffixes of a string of length n, which
 *              help efficiently implement the key component in the Burrows-
 *              Wheeler transform.
 *  @author:    Ying Chu
 **************************************************************************** */

import java.util.Arrays;

public class CircularSuffixArray {

    private final Integer[] index;
    private final int size;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }

        size = s.length();
        index = new Integer[size];

        for (int i = 0; i < size; i++) {
            index[i] = i;
        }

        Arrays.sort(index, (t1, t2) -> {
            for (int i = 0; i < size; i++) {
                char c1 = s.charAt((t1 + i) % size);
                char c2 = s.charAt((t2 + i) % size);

                if (c1 < c2) return -1;
                if (c1 > c2) return 1;
            }
            return t1.compareTo(t2);
        });

    }

    // length of s
    public int length() {
        return size;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if(i < 0 || i > length()-1) {
            throw new IllegalArgumentException("int out of boundary");
        }

        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";

        CircularSuffixArray cs = new CircularSuffixArray(s);
        for (int i = 0; i < cs.length(); i++) {
            System.out.println(cs.index(i));
        }

    }
}
