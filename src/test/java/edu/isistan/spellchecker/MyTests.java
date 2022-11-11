package edu.isistan.spellchecker;
import edu.isistan.spellchecker.corrector.Corrector;
import edu.isistan.spellchecker.corrector.Dictionary;
import edu.isistan.spellchecker.corrector.impl.FileCorrector;
import edu.isistan.spellchecker.corrector.impl.SwapCorrector;
import edu.isistan.spellchecker.tokenizer.TokenScanner;
import org.junit.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

/** Cree sus propios tests. */
public class MyTests {

    @Test
    public void testEmpty() throws IOException {
        Reader in = new StringReader("");
        TokenScanner ts = new TokenScanner(in);

        assertFalse(TokenScanner.isWord(""));
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




    private Set<String> makeSet(String[] strings) {
        Set<String> mySet = new TreeSet<String>();
        for (String s : strings) {
            mySet.add(s);
        }
        return mySet;
    }

    /* Falta un test de FileCorrector */

    @Test
    public void testEspaciosComas() throws IOException{
        Reader in = new StringReader("Aren't you \ntired");
        TokenScanner d = new TokenScanner(in);
        try {
            assertTrue("has next", d.hasNext());
            assertEquals("Aren't", d.next());

            assertTrue("has next", d.hasNext());
            assertEquals(" ", d.next());

            assertTrue("has next", d.hasNext());
            assertEquals("you", d.next());

            assertTrue("has next", d.hasNext());
            assertEquals(" \n", d.next());

            assertTrue("has next", d.hasNext());
            assertEquals("tired", d.next());

            assertFalse("reached end of stream", d.hasNext());
        } finally {
            in.close();
        }
    }


    @Test
    public void testSinCorrecciones() throws IOException, FileCorrector.FormatException  {
        Corrector c = FileCorrector.make("smallMisspellings.txt");
        assertEquals("sinerror", makeSet(new String[]{}), c.getCorrections("sinerror"));
    }

    @Test
    public void testMultiplesCorrecciones() throws IOException, FileCorrector.FormatException  {
        Corrector c = FileCorrector.make("smallMisspellings.txt");
        assertEquals("TIGGER -> {Trigger,Tiger}", makeSet(new String[]{"Trigger","Tiger"}), c.getCorrections("TIGGER"));
    }

    @Test
    public void testMultipleCorrectionesMinMay() throws IOException, FileCorrector.FormatException  {
        Corrector c = FileCorrector.make("smallMisspellings.txt");
        assertEquals("Tigger -> {Trigger,Tiger}", makeSet(new String[]{"Trigger","Tiger"}), c.getCorrections("Tigger"));
    }

    @Test
    public void testDiccionarioNull() throws IOException {
        try {
            new SwapCorrector(null);
            fail("Fallo");
        } catch (IllegalArgumentException f) {
        }
    }

    @Test
    public void testPalabraDiccionario() throws IOException {
        Reader reader = new FileReader("smallDictionary.txt");

        try {
            Dictionary d = new Dictionary(new TokenScanner(reader));
            SwapCorrector swap = new SwapCorrector(d);
            assertEquals("hers -> {}", makeSet(new String[]{}), swap.getCorrections("hers"));
        } finally {
            reader.close();
        }
    }

    @Test
    public void testDistintasCapitalizaciones() throws IOException {
        Reader reader = new FileReader("smallDictionary.txt");

        try {
            Dictionary d = new Dictionary(new TokenScanner(reader));
            SwapCorrector swap = new SwapCorrector(d);
            assertEquals("eT -> {Te}", makeSet(new String[]{"Te"}), swap.getCorrections("Et"));
        } finally {
            reader.close();
        }
    }


}