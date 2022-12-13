import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ParserTest {
    Parser parser;

    @Before
    public void before(){
        Grammar grammar = new Grammar("C:\\Users\\16112001\\IdeaProjects\\FLCD\\Lab5\\g1.txt");
        try {
            grammar.initGrammar();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        parser = new Parser(grammar);
    }

    @Test
    public void testFollow(){
        assertEquals(Set.of("epsilon",")"), parser.getFollowForNonterminals().get("S"));
        assertEquals(Set.of("epsilon",")"), parser.getFollowForNonterminals().get("A"));
        assertEquals(Set.of("+","epsilon",")"), parser.getFollowForNonterminals().get("B"));
        assertEquals(Set.of("+","epsilon",")"), parser.getFollowForNonterminals().get("C"));
        assertEquals(Set.of("*","+","epsilon",")"), parser.getFollowForNonterminals().get("D"));
    }

    @Test
    public void testFirst(){
        assertEquals(Set.of("(","a"), parser.getFirstForNonterminals().get("S"));
        assertEquals(Set.of("+","epsilon"), parser.getFirstForNonterminals().get("A"));
        assertEquals(Set.of("(","a"), parser.getFirstForNonterminals().get("B"));
        assertEquals(Set.of("*","epsilon"), parser.getFirstForNonterminals().get("C"));
        assertEquals(Set.of("(","a"), parser.getFirstForNonterminals().get("D"));
    }

}
