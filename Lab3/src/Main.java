import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
      Scanner scanner = new Scanner("C:\\Users\\16112001\\IdeaProjects\\FLCD\\Lab3\\src\\p2.in");
        try {
            scanner.scan();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
