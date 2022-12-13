import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Grammar grammar = new Grammar("C:\\Users\\16112001\\IdeaProjects\\FLCD\\Lab5\\g1.txt");
        try {
            grammar.initGrammar();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Parser p = new Parser(grammar);
        System.out.println(p.getFirstForNonterminals());
        System.out.println(p.getFollowForNonterminals());
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Print nonterminals");
            System.out.println("2. Print terminals.");
            System.out.println("3. Print productions");
            System.out.println("4. Print productions for a nonterminal");
            System.out.println("5. Check CFG");
            System.out.println("0. Exit\n");
            System.out.print("Enter option: ");
            int input = Integer.parseInt(scanner.nextLine());
            switch (input) {
                case 0 -> System.exit(0);
                case 1 -> System.out.println(grammar.getNonTerminals());
                case 2 -> System.out.println(grammar.getTerminals());
                case 3 -> {
                    Map<String, List<List<String>>> productions = grammar.getProductions();
                    StringBuilder sb = new StringBuilder();
                    for (String nonterminal : productions.keySet()) {
                        sb.append(nonterminal).append("->");
                        for (List<String> elems : productions.get(nonterminal))
                            sb.append(String.join("", elems)).append("|");
                        sb.deleteCharAt(sb.length()-1);
                        sb.append("\n");
                    }
                    System.out.println(sb);
                }
                case 4 -> {
                    System.out.print("Enter nonterminal: ");
                    String nonterminal = scanner.nextLine();
                    StringBuilder sb = new StringBuilder();
                    List<List<String>> prods = grammar.getProductionsForNonterminal(nonterminal.strip());
                    sb.append(nonterminal).append("->");
                    for (List<String> elems: prods)
                        sb.append(String.join("", elems)).append("|");
                    sb.deleteCharAt(sb.length()-1);
                    System.out.println(sb);
                }
                case 5 -> {
                    if (grammar.checkCFG())
                        System.out.println("Is CFG");
                    else
                        System.out.println("Not CFG");
                }
            }
        }
    }
}
