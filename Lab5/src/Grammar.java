import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

//ll1
public class Grammar {
    private List<String> nonTerminals;
    private List<String> terminals;
    private String startSymbol;
    private Map<String, List<List<String>>> productions;
    private List<List<String>> productionsRHSOrdered;
    private String inputFile;


    public Grammar(String file) {
        nonTerminals = new ArrayList<>();
        terminals = new ArrayList<>();
        startSymbol = "";
        productions = new HashMap<>();
        productionsRHSOrdered = new ArrayList<>();
        inputFile = file;

    }


    public void initGrammar() throws FileNotFoundException {
        File input = new File(inputFile);
        Scanner scanner = new Scanner(input);
        while (scanner.hasNext()) {
            String[] tokens = scanner.nextLine().split("=", 2);
            switch (tokens[0].strip()) {
                case "N" -> {
                    nonTerminals = Arrays.stream(tokens[1].strip().split(",")).toList();
                }
                case "E" -> {
                    terminals = Arrays.stream(tokens[1].split(" ")).toList();
                }
                case "S" -> startSymbol = tokens[1].trim();
                default -> {
                    String[] components = tokens[0].strip().split("->");
                    String nonTerminal = components[0].strip();
                    String[] result = components[1].strip().split("\\|");
                    if (!nonTerminals.contains(nonTerminal)) {
                        System.out.println(nonTerminal);
                        throw new RuntimeException("Grammar def: invalid production element");
                    }

                    if (!productions.containsKey(nonTerminal))
                        productions.put(nonTerminal, new ArrayList<>());
                    for (String elem : result) {
                        List<String> prodRHS = new ArrayList<>(List.of(elem.split(" ")));
                        productions.get(nonTerminal).add(prodRHS);
                        if (prodRHS.equals(new ArrayList<>(List.of("epsilon"))))
                            prodRHS.add(nonTerminal);
                        productionsRHSOrdered.add(prodRHS);
                    }
                }
            }
        }
        System.out.println(productionsRHSOrdered);
    }

    public boolean checkCFG() {
        for (String nonterminal : productions.keySet())
            if (nonterminal.contains(" "))
                return false;
        return true;
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

    public Map<String, List<List<String>>> getProductions() {
        return productions;
    }

    public List<List<String>> getProductionsForNonterminal(String nonterminal) {
        return productions.get(nonterminal);
    }

    public List<List<String>> getProductionsRHSOrdered() {
        return productionsRHSOrdered;
    }
}
