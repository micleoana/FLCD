import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

//ll1
public class Grammar {
    private List<String> nonTerminals;
    private List<String> terminals;
    private String startSymbol;
    private Map<List<String>, List<List<String>>> productions;
    private String inputFile;


    public Grammar(String file) {
        nonTerminals = new ArrayList<>();
        terminals = new ArrayList<>();
        startSymbol = "";
        productions = new HashMap<>();
        inputFile = file;

    }


    public void initGrammar() throws FileNotFoundException {
        File input = new File(inputFile);
        Scanner scanner = new Scanner(input);
        while (scanner.hasNext()) {
            String[] tokens = scanner.nextLine().split("=");
            switch (tokens[0]) {
                case "N" -> nonTerminals = Arrays.stream(tokens[1].split(",")).toList();
                case "E" -> terminals = Arrays.stream(tokens[1].split(",")).toList();
                case "S" -> startSymbol = tokens[1].trim();
            }
        }
    }

    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public Map<List<String>, List<List<String>>> getProductions() {
        return productions;
    }
}
