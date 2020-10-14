/* *****************************************************************************
 *  Topic:      Burrows Wheeler
 *              The Burrows–Wheeler data compression algorithm consists of three
 *              algorithmic components, including:
 *              1. Burrows-Wheeler Transform
 *              2. Move-to-Front Encoding
 *              3. Huffman Compression
 *  @author:    Ying Chu
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {

        // read the string
        String str = BinaryStdIn.readString();

        // make a circular suffix array
        CircularSuffixArray cs = new CircularSuffixArray(str);

        int first;
        int index;

        // search for row "first" in which original string end up
        for (int i = 0; i < str.length(); i++) {

            index = cs.index(i);
            if (index == 0) {
                first = i;
                BinaryStdOut.write(first);
                break;
            }
        }

        // find out the last column of the sorted suffixes
        // the index of the targeting character is index[i] - 1
        for (int i = 0; i < str.length(); i++) {
            index = cs.index(i);
            int targetID = index - 1;
            if (targetID < 0) {
                BinaryStdOut.write(str.charAt(str.length() - 1));
            } else {
                BinaryStdOut.write(str.charAt(targetID));
            }
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {

        // read the result of the t[] array
        int first = BinaryStdIn.readInt();
        String lastCol = BinaryStdIn.readString();

        int R = 256;
        int[] count = new int [R+1]; // used to count key-index
        int[] next = new int[lastCol.length()];
        char[] firstCol = new char[lastCol.length()];

        // count the character's frequency
        for (int i = 0; i < lastCol.length(); i++) {
            count[lastCol.charAt(i)+1]++;
        }

        for(int i = 0;i < R;i++) {
            count[i+1] += count[i];
        }

        for (int i = 0; i < lastCol.length(); i++) {
            int tmp = count[lastCol.charAt(i)]++;
            firstCol[tmp] = lastCol.charAt(i);
            next[tmp] = i;
        }

        for(int i = 0;i < lastCol.length();i++)
        {
            BinaryStdOut.write(firstCol[first]);
            first = next[first];
        }

        BinaryStdOut.close();

    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if ("-".equals(args[0])) {
            BurrowsWheeler.transform();
        }
        else if ("+".equals(args[0])) {
            BurrowsWheeler.inverseTransform();
        }
    }
}
