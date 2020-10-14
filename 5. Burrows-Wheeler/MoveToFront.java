
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.LinkedList;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {

        LinkedList<Character> asciiList = new LinkedList<>();
        for (char c = 0; c < 256; c++) {
            asciiList.add(c);
        }

        while (!BinaryStdIn.isEmpty()) {
            // We expect each character to be 8 bit.
            // Make the character equal to the extended ASCII character
            char character = BinaryStdIn.readChar();

            // get the location of such character in the list
            int index = asciiList.indexOf(character);

            // record the 8 least-significant bits of the character's location
            BinaryStdOut.write(index, 8);

            // move the character to the front
            asciiList.remove(index);
            asciiList.addFirst(character);

        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        // Initialize an ordered sequence of 256 ascii characters
        LinkedList<Character> asciiList = new LinkedList<>();
        for (char c = 0; c < 256; c++) {
            asciiList.add(c);
        }

        while (!BinaryStdIn.isEmpty()) {

            // the character is treated as an index
            int index = BinaryStdIn.readChar();

            // record the index's corresponding character in the sequence
            char character = asciiList.get(index);
            BinaryStdOut.write(character);

            // move the character to the front
            asciiList.remove(index);
            asciiList.addFirst(character);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if ("-".equals(args[0])) {
            MoveToFront.encode();
        }
        else if ("+".equals(args[0])) {
            MoveToFront.decode();
        }
    }
}
