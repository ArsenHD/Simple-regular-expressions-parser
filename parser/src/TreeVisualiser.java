import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

class TreeVisualiser {
    private Tree tree;
    private Map<Tree, Integer> labels;

    TreeVisualiser(Tree tree) {
        this.tree = tree;
        labels = new HashMap<>();
    }

    void visualize(int n) throws IOException {
        labels.clear();
        int counter = 0;
        PrintWriter writer = new PrintWriter(new FileOutputStream("gvFiles/graph" + n + ".gv"), true);
        writer.println("graph tree {");

        ArrayDeque<Tree> queue = new ArrayDeque<>();
        queue.addLast(tree);

        while (!queue.isEmpty()) {
            Tree node = queue.pollFirst();

            if (!labels.containsKey(node)) {
                writer.println("\t" + counter + " [label = \"" + node.getNode() + "\"];");
                labels.put(node, counter++);
            }

            for (Tree child : node.getChildren()) {
                queue.addLast(child);
                writer.println("\t" + counter + " [label = \"" + child.getNode() + "\"];");
                labels.put(child, counter++);
                writer.println("\t" + labels.get(node) + " -- " + labels.get(child) + ";");
            }
        }

        writer.println("}");

        Runtime.getRuntime().exec("dot -Tpng gvFiles/graph" + n + ".gv -o trees/graph" + n + ".png");
    }
}
