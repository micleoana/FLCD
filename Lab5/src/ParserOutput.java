import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class ParserOutput {
    private Parser parser;
    private Grammar grammar;
    private List<Integer> productions;
    private Integer nodeNumber = 1;
    private Boolean hasErrors;
    private List<Node> nodeList = new ArrayList<>();
    private Node root;
    private String outputFile;

    public ParserOutput(Parser parser,Grammar grammar, List<String> sequence, String outputFile){
        this.parser = parser;
        this.grammar = grammar;
        this.productions = parser.parseSequence(sequence);
        this.hasErrors = this.productions.contains(-1);
        this.outputFile = outputFile;
        generateTree();
    }

    public void generateTree(){
        if(hasErrors)
            return;

        Stack<Node> nodeStack = new Stack<>();
        var productionsIndex = 0;
        //root
        Node node = new Node();
        node.setParent(0);
        node.setSibling(0);
        node.setHasRight(false);
        node.setIndex(nodeNumber);
        nodeNumber++;
        node.setValue(grammar.getStartSymbol());
        nodeStack.push(node);
        nodeList.add(node);
        this.root = node;

        while(productionsIndex < productions.size() && !nodeStack.isEmpty()){
            Node currentNode = nodeStack.peek(); //father
            if(grammar.getTerminals().contains(currentNode.getValue()) || currentNode.getValue().contains("epsilon")){
                while(nodeStack.size()>0 && !nodeStack.peek().getHasRight()) {
                    nodeStack.pop();
                }
                if(nodeStack.size() > 0)
                    nodeStack.pop();
                else
                    break;
                continue;
            }

            //children
            var production = grammar.getProductionsRHSOrdered().get(productions.get(productionsIndex)-1);
            nodeNumber+=production.size()-1;
            for(var i=production.size()-1;i>=0;--i){
                Node child = new Node();
                child.setParent(currentNode.getIndex());
                child.setValue(production.get(i));
                child.setIndex(nodeNumber);
                if(i==0)
                    child.setSibling(0);
                else
                    child.setSibling(nodeNumber-1);
                child.setHasRight(i != production.size() - 1);

                nodeNumber--;
                nodeStack.push(child);
                nodeList.add(child);
            }
            nodeNumber+=production.size()+1;
            productionsIndex++;
        }
    }

    public void printTree(){
        try {
            nodeList.sort(Comparator.comparing(Node::getIndex));
            File file = new File(outputFile);
            FileWriter fileWriter = new FileWriter(file, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write("Index | Value | Parent | Sibling"+ "\n");
            for (Node node : nodeList) {
                bufferedWriter.write(node.getIndex() + " | " + node.getValue() + " | " + node.getParent() + " | " + node.getSibling() + "\n");
            }
            bufferedWriter.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}