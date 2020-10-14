/* *****************************************************************************
 *  Topic:     Shortest Ancestral Path
 *             An ancestral path between two vertices v and w in a digraph is a
 *             directed path from v to a common ancestor x, together with a directed
 *             path from w to the same ancestor x.
 *             A shortest ancestral path is an ancestral path of minimum total length.
 *             We refer to the common ancestor in a shortest ancestral path as a
 *             shortest common ancestor.
 *  @author:   Ying Chu
 **************************************************************************** */


import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph G;
    private BreadthFirstDirectedPaths ancestorV, ancestorW;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validate(w);
        validate(w);

        ancestorV = new BreadthFirstDirectedPaths(G, v);
        ancestorW = new BreadthFirstDirectedPaths(G, w);

        int minDist = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (ancestorV.hasPathTo(i) && ancestorW.hasPathTo(i)) {
                int currentDist = ancestorV.distTo(i) + ancestorW.distTo(i);
                minDist = Math.min(minDist, currentDist);
            }
        }
        if (minDist == Integer.MAX_VALUE) return -1;
        return minDist;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validate(w);
        validate(w);

        ancestorV = new BreadthFirstDirectedPaths(G, v);
        ancestorW = new BreadthFirstDirectedPaths(G, w);

        int minDist = Integer.MAX_VALUE;
        int leastAnc = -1;
        for (int i = 0; i < G.V(); i++) {
            if (ancestorV.hasPathTo(i) && ancestorW.hasPathTo(i)) {
                int currentDist = ancestorV.distTo(i) + ancestorW.distTo(i);
                if (currentDist < minDist) {
                    minDist = currentDist;
                    leastAnc = i;
                }
            }
        }
        if (minDist == Integer.MAX_VALUE) return -1;
        return leastAnc;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);

        ancestorV = new BreadthFirstDirectedPaths(G, v);
        ancestorW = new BreadthFirstDirectedPaths(G, w);

        int minDist = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (ancestorV.hasPathTo(i) && ancestorW.hasPathTo(i)) {
                int currentDist = ancestorV.distTo(i) + ancestorW.distTo(i);
                minDist = Math.min(minDist, currentDist);
            }
        }
        if (minDist == Integer.MAX_VALUE) return -1;
        return minDist;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);

        ancestorV = new BreadthFirstDirectedPaths(G, v);
        ancestorW = new BreadthFirstDirectedPaths(G, w);

        int minDist = Integer.MAX_VALUE;
        int leastAnc = -1;
        for (int i = 0; i < G.V(); i++) {
            if (ancestorV.hasPathTo(i) && ancestorW.hasPathTo(i)) {
                int currentDist = ancestorV.distTo(i) + ancestorW.distTo(i);
                if (currentDist < minDist) {
                    minDist = currentDist;
                    leastAnc = i;
                }
            }
        }
        if (minDist == Integer.MAX_VALUE) return -1;
        return leastAnc;
    }


    // check vertex validity
    private void validate(int v) {
        if (v < 0 || v > G.V()) {
            throw new IllegalArgumentException();
        }
    }

    private void validate(Iterable<Integer> v) {
        if (v == null) {
            throw new IllegalArgumentException();
        }
        for (Integer i: v) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}