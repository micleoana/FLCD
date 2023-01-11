import java.io.FileNotFoundException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Grammar g = new Grammar("C:\\Users\\16112001\\IdeaProjects\\FLCD\\Lab5\\g1.txt");
        try {
            g.initGrammar();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Parser p =new Parser(g);
        var result = p.parseSequence(List.of("a","*","(","a","+","a",")"));
        ParserOutput po = new ParserOutput(result,g);
        po.writeToFile();
    }
}
