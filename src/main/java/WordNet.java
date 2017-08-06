import edu.princeton.cs.algs4.*;

public class WordNet {

    private ST<String, Bag<Integer>> word2Ids;
    private ST<Integer, String> id2Word;
    private Digraph G;
    private final SAP sap;


    public WordNet(final String synsets, final String hypernyms) {
        word2Ids = new ST<>();
        id2Word = new ST<>();
        parseSynsets(synsets, word2Ids, id2Word);
        parseHypernyms(hypernyms);

        validateRootedGraph(G);
        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return word2Ids.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new NullPointerException();
        return word2Ids.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)  throw new NullPointerException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        return sap.length(word2Ids.get(nounA), word2Ids.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)  throw new NullPointerException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        final int ancestor = sap.ancestor(word2Ids.get(nounA), word2Ids.get(nounB));
        return id2Word.get(ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        final String sysnet = "src/main/resources/synsets.txt";
        final String hypernyms = "src/main/resources/hypernyms.txt";
        WordNet wordnet = new WordNet(sysnet, hypernyms);

        final String nounA = "anamorphosis";
        final String nounB = "zebra";

        StdOut.println(wordnet.isNoun(nounA));
        StdOut.println(wordnet.isNoun(nounB));
        StdOut.println(wordnet.distance(nounA, nounB));
    }

    // Private
    // Parse synsets
    private void parseSynsets(final String synsets, ST<String, Bag<Integer>> word2Ids, ST<Integer, String> id2Word) {
        final In in = new In(synsets);
        while (!in.isEmpty()) {
            final String line = in.readLine();
            final String[] fields = line.split(",");
            final Integer id = Integer.parseInt(fields[0]);
            final String[] nouns = fields[1].split("\\s+");
            for (String noun : nouns) {
                id2Word.put(id, noun);
                if (word2Ids.contains(noun)) {
                    word2Ids.get(noun).add(id);
                } else {
                    Bag<Integer> ids = new Bag<>();
                    ids.add(id);
                    word2Ids.put(noun, ids);
                }
            }
        }
    }

    // Parse hypernyms
    private void parseHypernyms(final String hypernyms) {
        final In in = new In(hypernyms);
        final String[] lines = in.readAllLines();
        G = new Digraph(id2Word.size());
        for (String line : lines) {
            final String[] fields = line.split(",");

            final int sysnet = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                final int hypernym = Integer.parseInt(fields[i]);
                G.addEdge(sysnet, hypernym);
            }
        }
    }

    private void validateRootedGraph(final Digraph G) {
        final DirectedCycle directedCycle = new DirectedCycle(G);
        if (directedCycle.hasCycle())
            throw new IllegalArgumentException();

        int roots = 0;
        for (int v = 0; v < G.V(); v++) {
            if (G.outdegree(v) == 0  && G.indegree(v) > 0)
                roots++;
        }

        if (roots != 1)
            throw new IllegalArgumentException();
    }
}