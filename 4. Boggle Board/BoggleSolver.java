/* *****************************************************************************
 *  Topic:      Boggle Solver
 *              Used to finds all valid words in a given Boggle board, using a
 *              given dictionary.
 *  @author:    Ying Chu
 **************************************************************************** */

import edu.princeton.cs.algs4.SET;


public class BoggleSolver {
    private static final int R = 26;  // A-Z
    private boolean[][] visited;
    private char[][] boardChar;
    private Adjacent[] adjacents;
    private int row;
    private int col;
    private Node root;

    private static class Node
    {
        private boolean isWord;
        private Node[] next = new Node[R];
    }

    private static class Adjacent
    {
        private int n = 0;
        private int[] neighbor = new int[8];
    }



    public BoggleSolver(String[] dictionary) {
        if (dictionary == null)
            throw new java.lang.IllegalArgumentException("the string[] is null");


        for (int i = 0; i < dictionary.length; i++)
            addToDictionary(dictionary[i]);
    }


    private void addToDictionary(String word) {
        if (word == null)
            throw new java.lang.IllegalArgumentException("the string is null");

        root = add(root, word, 0);
    }


    private Node add(Node x, String word, int d) {
        if (x == null)
            x = new Node();

        if (d == word.length())
            x.isWord = true;
        else {
            char c = word.charAt(d);
            x.next[c - 'A'] = add(x.next[c - 'A'], word, d + 1);
        }

        return x;
    }



    private boolean contains(String word) {
        if (word == null)
            throw new java.lang.IllegalArgumentException("the word is null");

        Node x = get(root, word, 0);
        if (x == null)
            return false;
        else
            return x.isWord;

    }



    private Node get(Node node, String word, int d) {
        if (node == null)
            return null;
        else {
            if (d < word.length()) {
                char c = word.charAt(d);
                return get(node.next[c - 'A'], word, d + 1);
            }
            else
                return node;
        }
    }


    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null)
            throw new java.lang.IllegalArgumentException("the board is null");


        if (row != board.rows() || col != board.cols()) {
            row = board.rows();
            col = board.cols();
            visited = new boolean[row][col];
            boardChar = new char[row][col];
            computeAdj();
        }

        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                boardChar[i][j] = board.getLetter(i, j);

        SET<String> words = DFS();
        return words;
    }


    private void computeAdj() {
        adjacents = new Adjacent[row * col];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int index = i * col + j;
                adjacents[index] = new Adjacent();

                if (i > 0) // above
                {
                    // upper left
                    if (j > 0)
                        adjacents[index].neighbor[adjacents[index].n++] = (i - 1) * col + j - 1;
                    // above
                    adjacents[index].neighbor[adjacents[index].n++] = (i - 1) * col + j;
                    // upper right
                    if (j < col - 1)
                        adjacents[index].neighbor[adjacents[index].n++] = (i - 1) * col + j + 1;
                }
                // right
                if (j < col - 1)
                    adjacents[index].neighbor[adjacents[index].n++] = i * col + j + 1;
                if (i < row - 1) // below
                {
                    // bottom right
                    if (j < col - 1) {
                        adjacents[index].neighbor[adjacents[index].n++] = (i + 1) * col + j + 1;
                    }
                    // bottom
                    adjacents[index].neighbor[adjacents[index].n++] = (i + 1) * col + j;
                    // bottom left
                    if (j > 0)
                        adjacents[index].neighbor[adjacents[index].n++] = (i + 1) * col + j - 1;
                }
                // left
                if (j > 0)
                    adjacents[index].neighbor[adjacents[index].n++] = i * col + j - 1;
            }
        }
    }


    private SET<String> DFS() {
        SET<String> words = new SET<>();

        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                DFS(i, j, new StringBuilder(), words, root);

        return words;
    }


    private void DFS(int indexi, int indexj, StringBuilder pre, SET<String> words, Node node) {
        char c = boardChar[indexi][indexj];
        Node next = node.next[c - 'A'];
        if (c == 'Q' && next != null)
            next = next.next['U' - 'A'];

        if (next == null)
            return;

        if (c == 'Q')
            pre.append("QU");
        else
            pre.append(c);

        String string = pre.toString();

        if (pre.length() > 2 && next.isWord)
            words.add(string);

        visited[indexi][indexj] = true;

        for (int i = 0; i < adjacents[indexi * col + indexj].n; i++) {
            int indexk = adjacents[indexi * col + indexj].neighbor[i];
            int indexrow = indexk / col;
            int indexcol = indexk % col;
            if (!visited[indexrow][indexcol])
                DFS(indexrow, indexcol, new StringBuilder(pre), words, next);
        }
        visited[indexi][indexj] = false;
    }


    public int scoreOf(String word) {
        if (word == null)
            throw new java.lang.IllegalArgumentException("the word is null");

        if (!contains(word))
            return 0;

        if (word.length() <= 2)
            return 0;
        else if (word.length() <= 4)
            return 1;
        else if (word.length() == 5)
            return 2;
        else if (word.length() == 6)
            return 3;
        else if (word.length() == 7)
            return 5;
        else
            return 11;
    }
}