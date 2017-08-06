import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;

import java.util.Comparator;


public class SAP {

    private static final Comparator<Ancestor> DIST_ORDER =
            (Ancestor o1, Ancestor o2) -> o1.compareTo(o2);

    private final Digraph G;

    private class Ancestor implements Comparable<Ancestor> {
        private final Integer dist;
        private final Integer word;

        Ancestor(final int word, final int dist) {
            this.word = word;
            this.dist = dist;
        }

        int getDist() {
            return dist;
        }

        int getWord() {
            return word;
        }

        @Override
        public int compareTo(final Ancestor o) {
            return this.dist.compareTo(o.dist);
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(final Digraph G) {
        if (G == null) throw new NullPointerException();
        this.G = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new IndexOutOfBoundsException();
        final BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        final BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        final MinPQ<Ancestor> ancestors = new MinPQ<Ancestor>();

        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int dist = bfsV.distTo(i) + bfsW.distTo(i);
                ancestors.insert(new Ancestor(i, dist));
            }
        }

        if (ancestors.isEmpty())
            return -1;

        return ancestors.min().getDist();
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new IndexOutOfBoundsException();
        final BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        final BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        final MinPQ<Ancestor> ancestors = new MinPQ<Ancestor>();

        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int dist = bfsV.distTo(i) + bfsW.distTo(i);
                ancestors.insert(new Ancestor(i, dist));
            }
        }

        if (ancestors.isEmpty())
            return -1;

        return ancestors.min().getWord();
    }
    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertex(v);
        validateVertex(w);
        final BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        final BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        final MinPQ<Ancestor> ancestors = new MinPQ<Ancestor>();

        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int dist = bfsV.distTo(i) + bfsW.distTo(i);
                ancestors.insert(new Ancestor(i, dist));
            }
        }

        if (ancestors.isEmpty())
            return -1;

        return ancestors.min().getDist();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertex(v);
        validateVertex(w);
        final BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        final BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        final MinPQ<Ancestor> ancestors = new MinPQ<Ancestor>();

        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int dist = bfsV.distTo(i) + bfsW.distTo(i);
                ancestors.insert(new Ancestor(i, dist));
            }
        }

        if (ancestors.isEmpty())
            return -1;

        return ancestors.min().getWord();
    }

    private void validateVertex(final Iterable<Integer> v) {
        if (v == null)
            throw new NullPointerException();
        for (Integer i : v) {
            if (i < 0 || i >= G.V())
                throw new IndexOutOfBoundsException();
        }
    }

    // do unit testing of this class
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

