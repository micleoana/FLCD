import java.util.*;

public class Parser {
    private HashMap<String, Set<String>> firstForNonterminals;
    private HashMap<String, Set<String>> followForNonterminals;
    private Grammar grammar;


    public Parser(Grammar grammar) {
        firstForNonterminals = new HashMap<>();
        followForNonterminals = new HashMap<>();
        this.grammar = grammar;
        generateFirstSet();
        follow();
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

    public HashMap<String, Set<String>> getFirstForNonterminals() {
        return firstForNonterminals;
    }

    public HashMap<String, Set<String>> getFollowForNonterminals() {
        return followForNonterminals;
    }
}
