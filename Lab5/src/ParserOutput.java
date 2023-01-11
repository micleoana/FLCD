import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ParserOutput {
    private Grammar grammar;
    private List<Integer> productions;
    private List<Node> tree = new ArrayList<>();

    public ParserOutput(List<Integer> productionsIndexList, Grammar grammar) {
        this.grammar = grammar;
        this.productions = productionsIndexList;
        buildParseTree();
    }

    public void buildParseTree() {

        Stack<Node> nodes = new Stack<>();
        int currentNodeIndex = 1;
        Node node = new Node(currentNodeIndex,grammar.getStartSymbol(),0,0);
        nodes.push(node);
        tree.add(node);
        currentNodeIndex++;

        for (int currentProd=0;currentProd < productions.size() && !nodes.isEmpty();currentProd++) {
            Node currentNode = nodes.peek();
            if (grammar.getTerminals().contains(currentNode.getInfo()) || currentNode.getInfo().contains("epsilon")) {
                while (nodes.size() > 0 && nodes.peek().getSibling() == 0) {
                    nodes.pop();
                }
            }
            currentNode = nodes.peek();

            List<String> production = grammar.getProductionsRHSOrdered().get(productions.get(currentProd) - 1);
            if (production.contains("epsilon") && production.size() > 1)
                production.remove(1);
            List<Node> children= new ArrayList<>();
            children.add(new Node(currentNodeIndex,production.get(0),currentNode.getIndex(),0));
            currentNodeIndex++;
            for (int i = 1;i<production.size();i++){
                children.add(new Node(currentNodeIndex,production.get(i),currentNode.getIndex(),currentNodeIndex-1));
                currentNodeIndex++;
            }

            for (int i = children.size()-1;i>=0;i--){
                nodes.push(children.get(i));
                tree.add(children.get(i));
            }
        }
    }

    public void writeToFile() {
        try {
            FileWriter outWriter = new FileWriter("C:\\Users\\16112001\\IdeaProjects\\FLCD\\Lab5\\out.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(outWriter);

            bufferedWriter.write("Index\t\tInfo\t\tParent\t\tRight sibling" + "\n");
            for (int i = 1; i <= tree.size(); i++) {
                for (Node node : tree)
                    if (node.getIndex() == i) {
                        if (node.getInfo().equals("epsilon"))
                            node.setInfo("ε");
                        bufferedWriter.write(node.getIndex() + "\t\t\t" + node.getInfo() + "\t\t\t" + node.getParent() + "\t\t\t" + node.getSibling() + "\n");
                    }
            }
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}