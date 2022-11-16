package scanner;

import fa.FA;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {
    private ST st;
    private List<Entry> pif;
    private String programFileName;
    private List<String> separators;
    private List<String> operators;
    private List<String> reservedKeywords;
    private int lineNr = 0;
    private FA identifierFA;
    private FA intConstFA;


    public Scanner(String programFileName) {
        st = new ST(50);
        pif = new ArrayList<>();
        separators = new ArrayList<>(Arrays.asList("(", ")", "[", "]", "{", "}", ",", ";", "\"", " ", "\n"));
        operators = new ArrayList<>(Arrays.asList("+", "-", "*", "/", "%", "<", ">", "<=", ">=", "==", "!=", "=", "AND", "OR"));
        reservedKeywords = new ArrayList<>(Arrays.asList("integer", "string", "verify", "otherwise", "loopFor", "loopWhile", "read", "print"));
        this.programFileName = programFileName;
        identifierFA = new FA("C:\\Users\\16112001\\IdeaProjects\\FLCD\\Lab4\\identifierFA.in");
        intConstFA = new FA("C:\\Users\\16112001\\IdeaProjects\\FLCD\\Lab4\\intConstFA.in");
        try {
            intConstFA.initFA();
            identifierFA.initFA();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public boolean isIdentifier(String token) throws FileNotFoundException {

        return identifierFA.isAccepted(token);
        //return Pattern.matches("^[a-zA-z][a-zA-z0-9]*", token);

    }

    public boolean isIntConst(String token) {
       /* Pattern p = Pattern.compile("0|[+|-]?[1-9][0-9]*");
        Matcher m = p.matcher(token);
        return m.matches();*/
        return intConstFA.isAccepted(token);
    }

    public boolean isStringConst(String token) {
        Pattern p = Pattern.compile("\"[a-zA-Z0-9 ]*\"");
        Matcher m = p.matcher(token);
        return m.matches();
    }

    public void scan() throws IOException {
        File file = new File(programFileName);
        java.util.Scanner sc = new java.util.Scanner(file);
        while (sc.hasNextLine()) {
            lineNr += 1;
            String line = sc.nextLine();
            String[] tokens = line.split("((?<=[^a-zA-Z0-9])|(?=[^a-zA-Z0-9]))");
            for (int i = 0; i < tokens.length; i++) {
                tokens[i] = tokens[i].trim();
                if (!tokens[i].equals("")) {
                    if (tokens[i].equals("\"")) {
                        int j = i + 1;
                        while (j < tokens.length) {
                            if (!Objects.equals(tokens[j], "\"")) {
                                tokens[i] += tokens[j];
                            } else {
                                tokens[i] += tokens[j];
                                break;
                            }
                            j++;
                        }
                        if (isStringConst(tokens[i])) {
                            int pos = st.put(tokens[i]);
                            pif.add(new Entry("constant", pos));
                            i = j + 1;
                        } else
                            throw new RuntimeException("Lexical error on line " + lineNr + " invalid token " + tokens[i]);

                    } else if (separators.contains(tokens[i]) || reservedKeywords.contains(tokens[i])) {
                        pif.add(new Entry(tokens[i], -1));
                    } else if (operators.contains(tokens[i] + tokens[i + 1])) {
                        pif.add(new Entry(tokens[i] + tokens[i + 1], -1));
                        i += 1;
                    } else if (tokens[i].equals("-") || tokens[i].equals("+")) {
                        int j = i - 1;
                        boolean ok = false;
                        while (j > 0) {
                            if (isIntConst(tokens[j]) || isIdentifier(tokens[j])) {
                                ok = true;
                                break;
                            } else if (tokens[j].equals(""))
                                j--;
                            else break;
                        }
                        if (isIntConst(tokens[i + 1]) && !ok) {
                            tokens[i] += tokens[i + 1];
                            int pos = st.put(tokens[i]);
                            pif.add(new Entry("const", pos));
                            i++;
                        } else pif.add(new Entry(tokens[i], -1));

                    } else if (operators.contains(tokens[i]))
                        pif.add(new Entry(tokens[i], -1));
                    else if (isIdentifier(tokens[i])) {
                        int pos = st.put(tokens[i]);
                        pif.add(new Entry("identifier", pos));
                    } else if (isIntConst(tokens[i])) {
                        int pos = st.put(tokens[i]);
                        pif.add(new Entry("constant", pos));
                    } else
                        throw new RuntimeException("Lexical error on line " + lineNr + " invalid token " + tokens[i]);
                }
            }
        }
        System.out.println("Lexically correct");
        FileWriter stWriter = new FileWriter("C:\\Users\\16112001\\IdeaProjects\\FLCD\\Lab4\\ST.out");
        stWriter.write("ST implemented using hash table\n\n");
        stWriter.write(st.toString());
        stWriter.close();
        FileWriter pifWriter = new FileWriter("C:\\Users\\16112001\\IdeaProjects\\FLCD\\Lab4\\PIF.out");
        pifWriter.write("");
        pifWriter.append("TOKEN").append("  ").append("ST_POS\n");
        for (Entry entry : pif) {
            pifWriter.append(entry.getKey()).append("   ").append(String.valueOf(entry.getValue())).append("\n");
        }
        pifWriter.close();
    }
}
