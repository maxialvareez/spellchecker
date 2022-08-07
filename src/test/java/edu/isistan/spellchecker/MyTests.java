package edu.isistan.spellchecker;
import edu.isistan.spellchecker.corrector.Dictionary;
import edu.isistan.spellchecker.tokenizer.TokenScanner;
import org.junit.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

/** Cree sus propios tests. */
public class MyTests {

    @Test
    public void testEmpty() throws IOException {
        Reader in = new StringReader("");
        TokenScanner ts = new TokenScanner(in);

        assertTrue(TokenScanner.isWord(""));
        assertFalse(ts.hasNext());
    }

    @Test
    public void testUnaPalabra() throws IOException {
        Reader in = new StringReader("una");
        TokenScanner ts = new TokenScanner(in);

        assertTrue(ts.hasNext());
        assertEquals("una",ts.next());
    }

    @Test
    public void testUnaNoPalabra() throws IOException {
        Reader in = new StringReader("$%");
        TokenScanner ts = new TokenScanner(in);

        assertFalse(TokenScanner.isWord("$%"));
        assertTrue(ts.hasNext());
        assertEquals("$%",ts.next());
    }

    @Test
    public void testDosPalabrasValido() throws IOException {
        Reader in = new StringReader("$% hola");
        TokenScanner ts = new TokenScanner(in);

        assertFalse(TokenScanner.isWord("$%"));
        assertTrue(TokenScanner.isWord("hola"));

        assertTrue(ts.hasNext());
        assertEquals("$% ",ts.next());

        assertTrue(ts.hasNext());
        assertEquals("hola",ts.next());

    }

    @Test
    public void testDosPalabrasNoValido() throws IOException {
        Reader in = new StringReader("hola $%");
        TokenScanner ts = new TokenScanner(in);

        assertTrue(TokenScanner.isWord("hola"));
        assertFalse(TokenScanner.isWord(" $%"));

        assertTrue(ts.hasNext());
        assertEquals("hola",ts.next());

        assertTrue(ts.hasNext());
        assertEquals(" $%",ts.next());

    }

    @Test
    public void testContainsWord() throws IOException {
        Dictionary d = Dictionary.make("smallDictionary.txt");
        assertTrue("'apple' -> should be true ('apple' in file)", d.isWord("apple"));
    }

    @Test
    public void testNotContainsWord() throws IOException{
        Dictionary d = Dictionary.make("smallDictionary.txt");
        assertFalse("'pineapple' -> should be false", d.isWord("pineapple"));
    }

    @Test
    public void testDictSize() throws IOException {
        Dictionary d = Dictionary.make("smallDictionary.txt");
        assertEquals("32", 32, d.getNumWords());
    }

    @Test
    public void testEmptyString() throws IOException{
        Dictionary d = Dictionary.make("smallDictionary.txt");
        assertFalse("'' -> should be false", d.isWord(""));
    }

    @Test
    public void testDistintasCaps() throws IOException{
        Dictionary d = Dictionary.make("smallDictionary.txt");
        assertTrue("'ApPle' -> should be true ('apple' in file)", d.isWord("apple"));
        assertTrue("'banANA' -> should be true ('banana' in file)", d.isWord("Banana"));
        assertTrue("'aPPle' -> should be true ('apple' in file)", d.isWord("apple"));
        assertTrue("'BaNaNa' -> should be true ('banana' in file)", d.isWord("Banana"));
    }



}