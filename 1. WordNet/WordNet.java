/* *****************************************************************************
 *  Topic:      WordNet
 *              Each vertex v is an integer that represents a synset, and each
 *              directed edge v→w represents that w is a hypernym of v.
 *              The WordNet digraph is a rooted DAG: it is acyclic and has one
 *              vertex—the root—that is an ancestor of every other vertex. However,
 *              it is not necessarily a tree because a synset can have more than
 *              one hypernym.
 *  @author:    Ying Chu
 **************************************************************************** */
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    private final Digraph G;
    // a hashmap that stores all nouns and their corresponding ids
    private final HashMap<String, ArrayList<Integer>> nounMap = new HashMap<String, ArrayList<Integer>>();
    // a hashmap that stores synsets
    private final HashMap<Integer, String> idMap = new HashMap<Integer, String>();

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        setSynset(synsets);
        G = new Digraph(idMap.size());
        setHypernyms(hypernyms, G);

        if (!isRootedDAG()) {
            throw new IllegalArgumentException("the argument does not form a rooted DAG");
        }
    }

    // build syntex hashmap: [0] == id; [1] == an array of nouns
    private void setSynset(String synsets) {
        if (synsets == null) {
            throw new IllegalArgumentException();
        }
        In in = new In(synsets);
        while (!in.isEmpty()) {

            // build idMap
            String line = in.readLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            idMap.put(id, parts[1]);

            // build map for each noun
            String[] nouns = parts[1].split(" ");
            for (String n: nouns) {
                if (!nounMap.containsKey(n)) {
                    ArrayList<Integer> list = new ArrayList<Integer>();
                    list.add(id);
                    nounMap.put(n, list);
                } else {
                    nounMap.get(n).add(id);
                }
            }
        }
    }

    private void setHypernyms(String hypernyms, Digraph digraph) {
        if (hypernyms == null) {
            throw new IllegalArgumentException();
        }

        if (digraph == null) {
            throw new IllegalArgumentException();
        }

        In in = new In(hypernyms);
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] parts = line.split(",");
            int v = Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                int w = Integer.parseInt((parts[i]));
                digraph.addEdge(v, w);
            }
        }
    }

    // is the graph a rooted DAG?
    private boolean isRootedDAG() {
        Topological topological = new Topological(G);
        return topological.hasOrder();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nounMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {

        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }

        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        ArrayList<Integer> idA = nounMap.get(nounA);
        ArrayList<Integer> idB = nounMap.get(nounB);
        SAP sap = new SAP(G);
        int length = sap.length(idA, idB);
        return length;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!nounMap.containsKey(nounA) || !nounMap.containsKey(nounB)) {
            throw new IllegalArgumentException("Noun is not in wordNet");
        }
        ArrayList<Integer> idA = nounMap.get(nounA);
        ArrayList<Integer> idB = nounMap.get(nounB);
        SAP sap = new SAP(G);
        int id = sap.ancestor(idA, idB);
        return idMap.get(id);
    }

}
