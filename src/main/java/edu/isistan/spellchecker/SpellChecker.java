package edu.isistan.spellchecker;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.*;

import edu.isistan.spellchecker.corrector.Corrector;
import edu.isistan.spellchecker.corrector.Dictionary;
import edu.isistan.spellchecker.tokenizer.TokenScanner;

/**
 * El SpellChecker usa un Dictionary, un Corrector, and I/O para chequear
 * de forma interactiva un stream de texto. Despues escribe la salida corregida
 * en un stream de salida. Los streams pueden ser archivos, sockets, o cualquier
 * otro stream de Java.
 * <p>
 * Nota:
 * <ul>
 * <li> La implementación provista provee métodos utiles para implementar el SpellChecker.
 * <li> Toda la salida al usuario deben enviarse a System.out (salida estandar)
 * </ul>
 * <p>
 * El SpellChecker es usado por el SpellCkecherRunner. Ver:
 *
 * @see SpellCheckerRunner
 */
public class SpellChecker {
    private Corrector corr;
    private Dictionary dict;

    /**
     * Constructor del SpellChecker
     *
     * @param c un Corrector
     * @param d un Dictionary
     */
    public SpellChecker(Corrector c, Dictionary d) {
        corr = c;
        dict = d;
    }

    /**
     * Returna un entero desde el Scanner provisto. El entero estará en el rango [min, max].
     * Si no se ingresa un entero o este está fuera de rango, repreguntará.
     *
     * @param min
     * @param max
     * @param sc
     */
    private int getNextInt(int min, int max, Scanner sc) {
        while (true) {
            try {
                int choice = Integer.parseInt(sc.next());
                if (choice >= min && choice <= max) {
                    return choice;
                }
            } catch (NumberFormatException ex) {
                // Was not a number. Ignore and prompt again.
            }
            System.out.println("Entrada invalida. Pruebe de nuevo!");
        }
    }

    /**
     * Retorna el siguiente String del Scanner.
     *
     * @param sc
     */
    private String getNextString(Scanner sc) {
        return sc.next();
    }


    /**
     * checkDocument interactivamente chequea un archivo de texto..
     * Internamente, debe usar un TokenScanner para parsear el documento.
     * Tokens de tipo palabra que no se encuentran en el diccionario deben ser corregidos
     * ; otros tokens deben insertarse tal cual en en documento de salida.
     * <p>
     *
     * @param in    stream donde se encuentra el documento de entrada.
     * @param input entrada interactiva del usuario. Por ejemplo, entrada estandar System.in
     * @param out   stream donde se escribe el documento de salida.
     * @throws IOException si se produce algún error leyendo el documento.
     */
    public void checkDocument(Reader in, InputStream input, Writer out) throws IOException {
        Scanner sc = new Scanner(input);
        TokenScanner doc = new TokenScanner(in);
        while (doc.hasNext()) {
            String wordToBeRead = doc.next();
            //non-word tokens ouputted verbatim
            if (!TokenScanner.isWord(wordToBeRead)) {
                out.write(wordToBeRead);
                //words in the dictionary ouputted verbatim
            } else if (dict.isWord(wordToBeRead)) {
                out.write(wordToBeRead);
            } else {
                Set<String> correct = corr.getCorrections(wordToBeRead);
                String[] correctionsArray = correct.toArray(new String[correct.size()]);
                System.out.println("The word: " + wordToBeRead + " is not in the dictionary. Please enter the number "
                        + "corresponding with the appropriate action: ");
                System.out.println("0: Ignore and continue");
                System.out.println("1: Replace with another word");
                for (int i = 0; i < correctionsArray.length; i++) {
                    System.out.println("Replace with " + correctionsArray[i]);
                }
                int userSelection = getNextInt(0, correctionsArray.length + 1, sc);
                //Output depending on user selection
                if (userSelection == 0) {
                    out.write(wordToBeRead);
                } else if (userSelection == 1) {
                    out.write(getNextString(sc));
                    ;
                } else {
                    out.write(correctionsArray[userSelection - 2]);
                }
            }
        }
    }
}
