package jtdiff.antlr;

import jtdiff.antlr.generated.Java8Lexer;
import jtdiff.antlr.generated.Java8Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Before;
import org.junit.Test;

public class TestLISPSerializer {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void testSerialization() throws Exception {
    InputStream inputStream = getInputStream(
        "./src/test/java/jtdiff/antlr/testData/serializationInput");
    ANTLRInputStream input = new ANTLRInputStream(inputStream);
    Java8Lexer lexer = new Java8Lexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    Java8Parser parser = new Java8Parser(tokens);
    ParseTree tree = parser.compilationUnit();
    ParseTreeWalker walker = new ParseTreeWalker();

    TokenStream tokenStream = parser.getTokenStream();
    LISPSerializer lispSerializer = new LISPSerializer(tokenStream, parser);
    walker.walk(lispSerializer, tree);


    String expectedString = new String(Files.readAllBytes(
        Paths.get(
            "./src/test/java/jtdiff/antlr/testData/" +
            "expectedLISPSerializationOutput")));
    // System.out.println("Expected:\n" + expectedString);
    // System.out.println("Returned:\n" + lispSerializer.serialization());
    assertEquals(expectedString, lispSerializer.serialization());

    inputStream.close();
  }

  private InputStream getInputStream(String fileName) throws Exception {
    File file = new File(fileName);
    FileInputStream fis = null;
    return new FileInputStream(file);
  }
}