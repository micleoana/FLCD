import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Scanner;

public class FA {

    private String inputFile;
    private String initialState;
    private List<String> states;
    private List<String> alphabet;
    private List<String> finalStates;
    private Map<Entry,String> transitions;


    public FA(String inputFile) {
        this.inputFile = inputFile;
        initialState = "";
        states = new ArrayList<>();
        alphabet = new ArrayList<>();
        finalStates = new ArrayList<>();
        transitions = new HashMap<>();
    }

    public void initFA() throws FileNotFoundException {
        File input = new File(inputFile);
        Scanner scanner = new Scanner(input);
        while (scanner.hasNext()){
            String[] tokens = scanner.nextLine().split("=");
            switch (tokens[0]) {
                case "Q" -> states = Arrays.stream(tokens[1].split(",")).toList();
                case "E" -> alphabet = Arrays.stream(tokens[1].split(",")).toList();
                case "F" -> finalStates = Arrays.stream(tokens[1].split(",")).toList();
                case "q0" -> initialState = tokens[1];
                case "T" -> {
                    String[] transitionList = tokens[1].split(",");
                    for (String transition : transitionList) {
                        String[] components = transition.replaceAll("[()]", "").split(";");
                        transitions.put(new Entry(components[0], components[1]), components[2]);
                    }
                }
            }
        }
    }

    public boolean checkDFA(){
        Map<Entry,Integer> occurence = new HashMap<>();
        for (Entry entry: transitions.keySet()){
           occurence.put(entry,0);
        }
        return true;
    }

    public String getInitialState() {
        return initialState;
    }

    public List<String> getStates() {
        return states;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public List<String> getFinalStates() {
        return finalStates;
    }

    public Map<Entry, String> getTransitions() {
        return transitions;
    }
}
