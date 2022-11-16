package fa;

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
    private Map<Entry, List<String>> transitions;


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
        while (scanner.hasNext()) {
            String[] tokens = scanner.nextLine().split("=");
            switch (tokens[0]) {
                case "Q" -> states = Arrays.stream(tokens[1].split(",")).toList();
                case "E" -> alphabet = Arrays.stream(tokens[1].split(",")).toList();
                case "F" -> {
                    finalStates = Arrays.stream(tokens[1].split(",")).toList();
                    for (String state: finalStates)
                        if (!states.contains(state))
                            throw new RuntimeException("FA def: invalid final state "+state);
                }
                case "q0" -> {
                    if (states.contains(tokens[1]) && tokens[1].length()==1)
                        initialState = tokens[1];
                    else throw new RuntimeException("FA def: invalid initial state "+tokens[1]);
                }
                case "T" -> {
                    String[] transitionList = tokens[1].split(",");
                    for (String transition : transitionList) {
                        String[] components = transition.replaceAll("[()]", "").split(";");
                        Entry tran = new Entry(components[0], components[1]);
                        if (!states.contains(components[0])|| !states.contains(components[2]) || !alphabet.contains(components[1]))
                            throw new RuntimeException("FA def: invalid transition element");
                        if (!transitions.containsKey(tran))
                            transitions.put(tran, new ArrayList<>());
                        transitions.get(tran).add(components[2]);
                    }
                }
            }
        }
    }

    public boolean isDFA() {
        for (Map.Entry<Entry, List<String>> entry : transitions.entrySet()) {
            if (transitions.get(entry.getKey()).size() != 1)
                return false;
        }
        return true;
    }

    public boolean isAccepted(String sequence) {
        if (!isDFA())
            throw new RuntimeException("FA.FA is not deterministic");
        String state = initialState;
        String[] chars = sequence.split("");
        for (String ch : chars) {
            if (transitions.get(new Entry(state, ch)) != null)
                state = transitions.get(new Entry(state, ch)).get(0);
            else return false;
        }
        return finalStates.contains(state);
    }

        public String getInitialState () {
            return initialState;
        }

        public List<String> getStates () {
            return states;
        }

        public List<String> getAlphabet () {
            return alphabet;
        }

        public List<String> getFinalStates () {
            return finalStates;
        }

        public Map<Entry, List<String>> getTransitions () {
            return transitions;
        }
    }
