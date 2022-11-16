package fa;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FA fa = new FA("C:\\Users\\16112001\\IdeaProjects\\FLCD\\Lab4\\FA.in");
        try {
            fa.initFA();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.println("\n1. Print states");
            System.out.println("2. Print alphabet.");
            System.out.println("3. Print initial state");
            System.out.println("4. Print final states.");
            System.out.println("5. Print transitions.");
            System.out.println("6. Check sequence");
            System.out.println("0. Exit\n");
            System.out.print("Enter option: ");
            int input = Integer.parseInt(scanner.nextLine());
            switch (input) {
                case 0 -> System.exit(0);
                case 1 -> System.out.println(fa.getStates());
                case 2 -> System.out.println(fa.getAlphabet());
                case 3 -> System.out.println(fa.getInitialState());
                case 4 -> System.out.println(fa.getFinalStates());
                case 5 -> {
                    Map<Entry, List<String>> transitions = fa.getTransitions();
                    for (Entry entry : transitions.keySet()) {
                        for (String result : transitions.get(entry))
                            System.out.println("δ(" + entry.getKey() + ", " + entry.getValue() + ") = " + result);
                    }
                }
                case 6 -> {
                    System.out.print("Enter sequence: ");
                    String sequence = scanner.nextLine();
                    if (fa.isAccepted(sequence))
                        System.out.println("Accepted");
                    else System.out.println("Not accepted");
                }
            }
        }
    }
}
