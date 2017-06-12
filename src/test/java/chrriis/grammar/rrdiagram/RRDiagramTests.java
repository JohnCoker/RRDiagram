package chrriis.grammar.rrdiagram;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import chrriis.grammar.model.BNFToGrammar;
import chrriis.grammar.model.Choice;
import chrriis.grammar.model.Grammar;
import chrriis.grammar.model.GrammarToRRDiagram;
import chrriis.grammar.model.Literal;
import chrriis.grammar.model.Repetition;
import chrriis.grammar.model.Rule;
import chrriis.grammar.model.RuleReference;
import chrriis.grammar.model.Sequence;
import chrriis.grammar.model.SpecialSequence;

/**
 * @author Lukas Eder
 */
public class RRDiagramTests {

  @Test
  public void testConsecutiveRuleReferencesSeparatedByNewline() {
    assertEquals(5, countElements("rect", svg("rule = a b\nc d\n\te;")));
    assertEquals(5, countElements("rect", svg("rule = a b\r\nc d\ne;")));
  }

  @Test
  public void testGrammarToString() {
    assertEquals("r = a b;", grammar("r = a b;").toString());
    assertEquals("r1 = a b;\nr2 = c d;", grammar("r1 = a b;\nr2 = c d;").toString());
  }

  @Test
  public void testRuleToString() {
    assertEquals("r = a b;", rule("r = a b;").toString());
  }

  @Test
  public void testChoiceToString() {
    assertEquals("a | b", new Choice(new RuleReference("a"), new RuleReference("b")).toString());
  }

  @Test
  public void testLiteralToString() {
    assertEquals("'a'", new Literal("a").toString());
  }

  @Test
  public void testRuleReferenceToString() {
    assertEquals("a", new RuleReference("a").toString());
  }

  @Test
  public void testSequenceToString() {
    assertEquals("a b", new Sequence(new RuleReference("a"), new RuleReference("b")).toString());
  }

  @Test
  public void testSpecialSequenceToString() {
    assertEquals("(? abc ?)", new SpecialSequence("abc").toString());
  }

  @Test
  public void testRepetitionToString() {
    assertEquals("{ a }", new Repetition(new RuleReference("a"), 0, null).toString());
    assertEquals("[ a ]", new Repetition(new RuleReference("a"), 0, 1).toString());
    assertEquals("2 * [ a ]", new Repetition(new RuleReference("a"), 0, 2).toString());
  }

  // Test utilities

  private String svg(String string) {
    Grammar grammar = grammar(string);
    Rule[] rules = grammar.getRules();
    GrammarToRRDiagram grammarToRRDiagram = new GrammarToRRDiagram();
    RRDiagram diagram = grammarToRRDiagram.convert(rules[0]);
    RRDiagramToSVG rrDiagramToSVG = new RRDiagramToSVG();
    String svg = rrDiagramToSVG.convert(diagram);
    return svg;
  }

  private Grammar grammar(String string) {
    BNFToGrammar bnfToGrammar = new BNFToGrammar();
    Grammar grammar = bnfToGrammar.convert(string);
    return grammar;
  }

  private Rule rule(String string) {
    return grammar(string).getRules()[0];
  }

  private int countElements(String tagName, String svg) {
    try {
      Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(svg)));
      return document.getElementsByTagName(tagName).getLength();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
