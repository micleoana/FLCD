import java.util.*;

public class Parser {
    private HashMap<String, Set<String>> firstForNonterminals;
    private HashMap<String, Set<String>> followForNonterminals;
    private Grammar grammar;
    private HashMap<Map.Entry<String, String>, Map.Entry<String, Integer>> parseTable;


    public Parser(Grammar grammar) {
        firstForNonterminals = new HashMap<>();
        followForNonterminals = new HashMap<>();
        parseTable = new HashMap<>();
        this.grammar = grammar;
        generateFirstSet();
        follow();
        buildParseTable();
    }

    public void generateFirstSet() {

        for (String nonTerminal : grammar.getNonTerminals()) {
            firstForNonterminals.put(nonTerminal, new HashSet<>());
            List<List<String>> productionsForNonTerminal = grammar.getProductionsForNonterminal(nonTerminal);
            for (List<String> production : productionsForNonTerminal) {
                if (grammar.getTerminals().contains(production.get(0)) || production.get(0).equals("epsilon")) {
                    firstForNonterminals.get(nonTerminal).add(production.get(0));
                }
            }
        }
        var isDifferent = true;
        while (isDifferent) {
            isDifferent = false;
            HashMap<String, Set<String>> newIteration = new HashMap<>();

            for (String nonTerminal : grammar.getNonTerminals()) {
                List<List<String>> productionsForNonterminal = grammar.getProductionsForNonterminal(nonTerminal);
                Set<String> concatenatedSet = new HashSet<>(firstForNonterminals.get(nonTerminal));
                for (List<String> production : productionsForNonterminal) {
                    List<String> rightNonTerminals = new ArrayList<>();
                    String rightFoundTerminal = null;
                    for (String item : production)
                        if (this.grammar.getNonTerminals().contains(item))
                            rightNonTerminals.add(item);
                        else {
                            rightFoundTerminal = item;
                            break;
                        }
                    concatenatedSet.addAll(concatenationOfLengthOne(rightNonTerminals, rightFoundTerminal));
                }
                if (!concatenatedSet.equals(firstForNonterminals.get(nonTerminal))) {
                    isDifferent = true;
                }
                newIteration.put(nonTerminal, concatenatedSet);
            }
            firstForNonterminals = newIteration;
        }
    }

    private Set<String> concatenationOfLengthOne(List<String> nonTerminals, String terminal) {
        if (nonTerminals.size() == 0)
            return new HashSet<>();
        if (nonTerminals.size() == 1) {
            return firstForNonterminals.get(nonTerminals.get(0));
        }
        Set<String> concatenation = new HashSet<>();
        var i = 0;

        while (i < nonTerminals.size()) {
            boolean isEpsilon = false;
            for (String s : firstForNonterminals.get(nonTerminals.get(i)))
                if (s.equals("epsilon"))
                    isEpsilon = true;
                else
                    concatenation.add(s);
            if (isEpsilon)
                i++;
            else
                break;
        }

        var allNonTerminalsHaveEpsilon = true;
        for (String nonTerminal : nonTerminals) {
            if (!firstForNonterminals.get(nonTerminal).contains("epsilon")) {
                allNonTerminalsHaveEpsilon = false;
            }
        }
        if (allNonTerminalsHaveEpsilon) {
            concatenation.add(Objects.requireNonNullElse(terminal, "epsilon"));
        }

        return concatenation;
    }


    public void follow() {
        for (String nonterminal : grammar.getNonTerminals()) {
            followForNonterminals.put(nonterminal, new HashSet<>());
        }
        followForNonterminals.get(grammar.getStartSymbol()).add("epsilon");
        boolean repeat = true;
        while (repeat) {
            repeat = false;
            for (String nonterminal : grammar.getNonTerminals()) {
                Set<String> prev = new HashSet<>(followForNonterminals.get(nonterminal));
                Map<String, List<List<String>>> productionsContainingNonterminal = getProductionsContainingNonterminal(nonterminal);
                for (Map.Entry<String, List<List<String>>> production : productionsContainingNonterminal.entrySet()) {
                    for (List<String> elem : production.getValue()) {
                        if (elem.indexOf(nonterminal) == elem.size() - 1)
                            followForNonterminals.get(nonterminal).addAll(followForNonterminals.get(production.getKey()));
                        else {
                            String next = elem.get(elem.indexOf(nonterminal) + 1);
                            if (grammar.getTerminals().contains(next))
                                followForNonterminals.get(nonterminal).add(next);
                            else {
                                for (String terminal : firstForNonterminals.get(next)) {
                                    if (terminal.equals("epsilon"))
                                        followForNonterminals.get(nonterminal).addAll(followForNonterminals.get(production.getKey()));
                                    else
                                        followForNonterminals.get(nonterminal).addAll(firstForNonterminals.get(next));
                                }
                            }
                        }
                    }
                }
                if (!prev.equals(followForNonterminals.get(nonterminal)))
                    repeat = true;
            }
        }
    }


    public Map<String, List<List<String>>> getProductionsContainingNonterminal(String nonterminal) {
        HashMap<String, List<List<String>>> result = new HashMap<>();
        for (Map.Entry<String, List<List<String>>> prod : grammar.getProductions().entrySet()) {
            for (List<String> elem : prod.getValue()) {
                if (elem.contains(nonterminal)) {
                    if (result.get(prod.getKey()) == null)
                        result.put(prod.getKey(), new ArrayList<>());
                    result.get(prod.getKey()).add(elem);
                }
            }
        }
        return result;
    }

    public void buildParseTable() {
        List<String> rows = new ArrayList<>();
        rows.addAll(grammar.getNonTerminals());
        rows.addAll(grammar.getTerminals());
        rows.add("$");

        List<String> columns = new ArrayList<>();
        columns.addAll(grammar.getTerminals());
        columns.add("$");

        for (var row : rows)
            for (var col : columns)
                parseTable.put(Map.entry(row, col), Map.entry("err", -1));

        for (var col : columns)
            parseTable.put(Map.entry(col, col), Map.entry("pop", -1));

        parseTable.put(Map.entry("$", "$"), Map.entry("acc", -1));

        Map<String, List<List<String>>> productions = grammar.getProductions();
        List<List<String>> productionsR = grammar.getProductionsRHSOrdered();

        for (var entry : productions.entrySet()) {
            String key = entry.getKey();
            List<List<String>> productionsForNonterminal = entry.getValue();
            for (var production : productionsForNonterminal) {
                var firstSymbol = production.get(0);
                if (grammar.getTerminals().contains(firstSymbol))
                    if (parseTable.get(Map.entry(key, firstSymbol)).getKey().equals("err"))
                        parseTable.put(Map.entry(key, firstSymbol), Map.entry(String.join(" ", production), productionsR.indexOf(production) + 1));
                    else {
                        System.out.println("Conflict on " + key + "," + firstSymbol);
                        break;
                    }
                else if (grammar.getNonTerminals().contains(firstSymbol)) {
                    if (production.size() == 1)
                        for (var symbol : firstForNonterminals.get(firstSymbol))
                            if (parseTable.get(Map.entry(key, symbol)).getKey().equals("err"))
                                parseTable.put(Map.entry(key, symbol), Map.entry(String.join(" ", production), productionsR.indexOf(production) + 1));
                            else {
                                System.out.println("Conflict on " + key + "," + symbol);
                                break;
                            }
                    else {
                        int i = 1;
                        String nextSymbol = production.get(1);
                        Set<String> firstSetForFirstSymbol = firstForNonterminals.get(firstSymbol);

                        while (i < production.size() && grammar.getNonTerminals().contains(nextSymbol)) {
                            var firstSetForNextSymbol = firstForNonterminals.get(nextSymbol);
                            if (firstSetForFirstSymbol.contains("epsilon")) {
                                firstSetForFirstSymbol.remove("epsilon");
                                firstSetForFirstSymbol.addAll(firstSetForNextSymbol);
                            }

                            i++;
                            if (i < production.size())
                                nextSymbol = production.get(i);
                        }

                        for (var symbol : firstSetForFirstSymbol) {
                            if (symbol.equals("epsilon"))
                                symbol = "$";
                            if (parseTable.get(Map.entry(key, symbol)).getKey().equals("err"))
                                parseTable.put(Map.entry(key, symbol), Map.entry(String.join(" ", production), productionsR.indexOf(production) + 1));
                            else {
                                System.out.println("Conflict on " + key + "," + symbol);
                                break;
                            }
                        }
                    }
                } else {
                    var follow = followForNonterminals.get(key);
                    for (var symbol : follow) {
                        if (symbol.equals("epsilon")) {
                            if (parseTable.get(Map.entry(key, "$")).getKey().equals("err")) {
                                var prod = new ArrayList<>(List.of("epsilon", key));
                                parseTable.put(Map.entry(key, "$"), Map.entry("epsilon", productionsR.indexOf(prod) + 1));
                            } else {
                                System.out.println("Conflict on " + key + "," + symbol);
                                break;
                            }
                        } else if (parseTable.get(Map.entry(key, symbol)).getKey().equals("err")) {
                            var prod = new ArrayList<>(List.of("epsilon", key));
                            parseTable.put(Map.entry(key, symbol), Map.entry("epsilon", productionsR.indexOf(prod) + 1));
                        } else {
                            System.out.println("Conflict on " + key + "," + symbol);
                            break;
                        }
                    }
                }
            }
        }
    }

    public HashMap<String, Set<String>> getFirstForNonterminals() {
        return firstForNonterminals;
    }

    public HashMap<String, Set<String>> getFollowForNonterminals() {
        return followForNonterminals;
    }

    public List<Integer> parseSequence(List<String> sequence) {
        Stack<String> alpha = new Stack<>();
        Stack<String> beta = new Stack<>();
        List<Integer> result = new ArrayList<>();

        alpha.push("$");
        for (var i = sequence.size() - 1; i >= 0; --i)
            alpha.push(sequence.get(i));

        beta.push("$");
        beta.push(grammar.getStartSymbol());

        while (!(alpha.peek().equals("$") && beta.peek().equals("$"))) {
            String alphaPeek = alpha.peek();
            String betaPeek = beta.peek();
            var key = Map.entry(betaPeek, alphaPeek);
            var value = parseTable.get(key);

            if (!value.getKey().equals("err")) {
                if (value.getKey().equals("pop")) {
                    alpha.pop();
                    beta.pop();
                } else {
                    beta.pop();
                    if (!value.getKey().equals("epsilon")) {
                        String[] val = value.getKey().split(" ");
                        for (var i = val.length - 1; i >= 0; --i)
                            beta.push(val[i]);
                    }
                    result.add(value.getValue());
                }
            } else {
                System.out.println("Syntax error: " + key.getKey() + ", " + key.getValue());
                System.out.println("alpha = " + alpha);
                result = new ArrayList<>(List.of(-1));
                return result;
            }
        }
        System.out.println("Sequence accepted");
        return result;
    }
}