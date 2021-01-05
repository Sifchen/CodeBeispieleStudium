package huffman;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Ein Wald von Bäumen ({@link Tree}).
 * 
 * @author kar, mhe, Daniel Pigotow
 *
 */
public class Forest {

    private static Comparator<Tree> treeCompareByRoot = new Comparator<Tree>() {
        @Override
        public int compare(Tree o1, Tree o2) {
            return Long.compare(o1.getCount(), o2.getCount());
        }
    };

    private final Queue<Tree> forest = new PriorityQueue<>(treeCompareByRoot);

    /**
     * Fügt den übergebenen Baum in diesen Wald ein.
     * 
     * @param tree Der einzufügende Baum, darf nicht null sein
     */
    public void insert(Tree tree) {
        if (tree == null) {
            throw new IllegalArgumentException("tree darf nicht null sein");
        }
        forest.add(tree);
    }

    /**
     * Wandelt den aktuellen Wald, wenn er nicht leer ist, in einen einzelnen Baum um und gibt
     * diesen zurück. Der Wald wird verändert und besteht anschließend nur noch aus einem Baum.
     * 
     * Wenn der Wald leer ist, dann wird der leere Baum zurückgegeben.
     * 
     * @return Der resultierende Baum
     */
    public Tree toTree() {

        if (this.forest.isEmpty()) {
            return Tree.empty();
        } else {

            // Schleife bis nur noch ein Element in der Queue vorhanden ist
            while (this.forest.size() > 1) {

                // Nimmt zwei Trees aus der Queue (sind sortiert, also die kleinste Anzahl)
                Tree left = this.forest.poll();
                Tree right = this.forest.poll();

                // Erstelle ein Node und hänge die Trees(Nodes oder Leafs) aus der Queue an die
                // Node an
                // Addiere die Vorkommen der Kinder und schreibe diese in die erstellte Node
                Tree parentNode = Tree.merge(left, right);
                // Fuege die Node (den gerade erstllten Tree) zurueck in die Queue ein
                this.forest.add(parentNode);
            }
            return this.forest.poll();
        }
    }

    @Override
    // Nur zum Testen der Blaetter in der Queue
    public String toString() {
        String result = "[";
        for (Tree t : this.forest) {
            result += " [ " + t.getClass().getSimpleName() + " Occs:" + t.getCount() + " Symbol:"
                    + t.getByte() + " ]";
        }
        return result + " ]";
    }

}
