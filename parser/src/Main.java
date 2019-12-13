import java.io.*;
import java.text.ParseException;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader correct = new BufferedReader(new FileReader("CorrectTests"));
        BufferedReader incorrect = new BufferedReader(new FileReader("IncorrectTests"));
        PrintWriter errors = new PrintWriter(new FileOutputStream("ErrorsReport"), true);

        Parser parser = new Parser();
        Tree tree;

        String str = "a**";

        try {
            tryToCreateDirectory("./gvFiles");
            tryToCreateDirectory("./trees");
        } catch (FileNotFoundException e) {
            return;
        }

        String regex;
        int counter = 0;
        while ((regex = correct.readLine()) != null) {
            try {
                tree = parser.parse(new ByteArrayInputStream(regex.getBytes()));
                TreeVisualiser treeVisualiser = new TreeVisualiser(tree);
                treeVisualiser.visualize(++counter);
            } catch (ParseException e) {
                System.out.println(e.getMessage() + " at position " + e.getErrorOffset());
            }
        }

        counter = 0;
        while ((regex = incorrect.readLine()) != null) {
            try {
                tree = parser.parse(new ByteArrayInputStream(regex.getBytes()));
                TreeVisualiser treeVisualiser = new TreeVisualiser(tree);
                treeVisualiser.visualize(++counter);
            } catch (ParseException e) {
                errors.println("Test #" + (++counter) + ". " + e.getMessage() + " at position " + e.getErrorOffset());
            }
        }
    }

    private static void tryToCreateDirectory(String dirName) throws FileNotFoundException {
        File dir = new File(dirName);
        if (dir.exists()) {
            File[] files = Objects.requireNonNull(dir.listFiles());
            for (File entry: files) {
                if (!entry.delete()) {
                    System.out.println("Failed to remove " + dirName + "/" + entry.getName());
                    throw new FileNotFoundException();
                }
            }
            if (!dir.delete()) {
                System.out.println("Failed to remove " + dirName + " directory");
                throw new FileNotFoundException();
            }
        }

        if (!dir.mkdir()) {
            System.out.println("Failed to create " + dirName + " directory");
            throw new FileNotFoundException();
        }
    }
}
