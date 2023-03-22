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

    //La entrada es vacia.
    @Test
    public void testEmpty() throws IOException {
        Reader in = new StringReader("");
        TokenScanner ts = new TokenScanner(in);

        assertFalse(TokenScanner.isWord(""));
        assertFalse(ts.hasNext());
    }

    //La entrada tiene una solo token palabra.
    @Test
    public void testUnaPalabra() throws IOException {
        Reader in = new StringReader("una");
        TokenScanner ts = new TokenScanner(in);

        assertTrue(ts.hasNext());
        assertEquals("una",ts.next());
    }

    //La entrada tiene un solo token no-palabra.
    @Test
    public void testUnaNoPalabra() throws IOException {
        Reader in = new StringReader("$%");
        TokenScanner ts = new TokenScanner(in);

        assertFalse(TokenScanner.isWord("$%"));
        assertTrue(ts.hasNext());
        assertEquals("$%",ts.next());
    }

    //La entrada tiene los dos tipos de tokens, y termina en un token palabra.
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

    //La entrada tiene los dos tipos de tokens, y termina en un token no palabra.
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

    //Chequear por una palabra que está en el diccionario.
    @Test
    public void testContainsWord() throws IOException {
        Dictionary d = Dictionary.make("smallDictionary.txt");
        assertTrue("'apple' -> should be true ('apple' in file)", d.isWord("apple"));
    }

    //Chequear por una palabra que NO está en el diccionario.
    @Test
    public void testNotContainsWord() throws IOException{
        Dictionary d = Dictionary.make("smallDictionary.txt");
        assertFalse("'pineapple' -> should be false", d.isWord("pineapple"));
    }

    //Preguntar por el número de palabras en el diccionario.
    @Test
    public void testDictSize() throws IOException {
        Dictionary d = Dictionary.make("smallDictionary.txt");
        assertEquals("32", 32, d.getNumWords());
    }

    //Verificar que el String vacio “” no sea una palabra.
    @Test
    public void testEmptyString() throws IOException{
        Dictionary d = Dictionary.make("smallDictionary.txt");
        assertFalse("'' -> should be false", d.isWord(""));
    }

    //Chequear que la misma palabra con distintas capitalizaciones esté en el diccionario
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

    //Probar un archivo con espacios extras en alrededor de las líneas o alrededor de las comas.
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

    //Pedir correcciones para una palabra sin correcciones.
    @Test
    public void testSinCorrecciones() throws IOException, FileCorrector.FormatException  {
        Corrector c = FileCorrector.make("smallMisspellings.txt");
        assertEquals("sinerror", makeSet(new String[]{}), c.getCorrections("sinerror"));
    }

    //Pedir correcciones para una palabra con múltiples correcciones.
    @Test
    public void testMultiplesCorrecciones() throws IOException, FileCorrector.FormatException  {
        Corrector c = FileCorrector.make("smallMisspellings.txt");
        assertEquals("TIGGER -> {Trigger,Tiger}", makeSet(new String[]{"Trigger","Tiger"}), c.getCorrections("TIGGER"));
    }

    ////Probar correcciones para palabras con distintas capitalizaciones.
    @Test
    public void testDistintasCapitalizacionesFileCorrector() throws IOException,FileCorrector.FormatException {
        Corrector c = FileCorrector.make("misspellings.txt");
        assertEquals("TjHe -> {the}", makeSet(new String[]{"The"}), c.getCorrections("TjHe"));

    }

                                        //-----------------SwapCorrector---------------

    //Proveer un diccionario null.
    @Test
    public void testDiccionarioNull() throws IOException {
        try {
            new SwapCorrector(null);
            fail("Fallo");
        } catch (IllegalArgumentException f) {
        }
    }

    //Pedir correcciones para una palabra que está en el diccionario
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

    //Pedir correcciones para una palabra con distintas capitalizaciones.
    @Test
    public void testDistintasCapitalizacionesSwapCorrector() throws IOException {
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