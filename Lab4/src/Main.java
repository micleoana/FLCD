import java.io.FileNotFoundException;
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

        System.out.println(fa.checkDFA());
        while (true){
            System.out.println("\n1. Print states");
            System.out.println("2. Print alphabet.");
            System.out.println("3. Print initial state");
            System.out.println("4. Print final states.");
            System.out.println("5. Print transitions.");
            System.out.println("0. Exit\n");
            int input = scanner.nextInt();
            switch (input){
                case 0:
                    System.exit(0);
                    break;
                case 1:
                    System.out.println(fa.getStates());
                    break;
                case 2:
                    System.out.println(fa.getAlphabet());
                    break;
                case 3:
                    System.out.println(fa.getInitialState());
                    break;
                case 4:
                    System.out.println(fa.getFinalStates());
                    break;
                case 5:
                    Map<Entry,String> transitions = fa.getTransitions();
                    for (Map.Entry<Entry, String> entry: transitions.entrySet()){
                        System.out.println("δ("+entry.getKey().getKey()+", "+entry.getKey().getValue()+") = "+entry.getValue());
                    }
                    break;
            }

        }
    }
}
