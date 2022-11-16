package scanner;

public class Main {
    public static void main(String[] args) {
      Scanner scanner = new Scanner("C:\\Users\\16112001\\IdeaProjects\\FLCD\\Lab4\\src\\scanner\\p1.in");
        try {
            scanner.scan();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
