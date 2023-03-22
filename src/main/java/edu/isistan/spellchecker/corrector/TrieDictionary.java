package edu.isistan.spellchecker.corrector;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import edu.isistan.spellchecker.tokenizer.TokenScanner;

/**
 * El diccionario maneja todas las palabras conocidas.
 * El diccionario es case insensitive
 *
 * Una palabra "válida" es una secuencia de letras (determinado por Character.isLetter)
 * o apostrofes.
 */
public class TrieDictionary {

    static final int ALPHABET_SIZE = 27; //Se suma 1 valor para el apóstrofe a las 26

    private TrieNode dictionary; //Nodo raiz

    /**
     * Construye un diccionario usando un TokenScanner
     * <p>
     * Una palabra válida es una secuencia de letras (ver Character.isLetter) o apostrofes.
     * Toda palabra no válida se debe ignorar
     *
     * <p>
     *
     * @param ts
     * @throws IOException Error leyendo el archivo
     * @throws IllegalArgumentException el TokenScanner es null
     */
    public TrieDictionary(TokenScanner ts) throws IOException {
        this. dictionary = new TrieNode();
        if(ts == null){
            throw new IllegalArgumentException();
        }
        while (ts.hasNext()){
            String nextString = ts.next();
            if(TokenScanner.isWord(nextString)){
                this.insert(nextString.toLowerCase());
            }
        }
    }


    /**
     * Construye un diccionario usando un archivo.
     *
     *
     * @param filename
     * @throws FileNotFoundException si el archivo no existe
     * @throws IOException Error leyendo el archivo
     */
        public static TrieDictionary make(String filename) throws IOException {
        Reader r = new FileReader(filename);
        TrieDictionary d = new TrieDictionary(new TokenScanner(r));
        r.close();
        return d;
    }

    /**
     * Retorna el número de palabras correctas en el diccionario.
     * Recuerde que como es case insensitive si Dogs y doGs están en el
     * diccionario, cuentan como una sola palabra.
     *
     * @return número de palabras únicas
     */
//    public int getNumWords() {
//        int numPalabras = dictionary.size();
//        return numPalabras;
//    }

    /**
     * Testea si una palabra es parte del diccionario. Si la palabra no está en
     * el diccionario debe retornar false. null debe retornar falso.
     * Si en el diccionario está la palabra Dog y se pregunta por la palabra dog
     * debe retornar true, ya que es case insensitive.
     *
     *Llamar a este método no debe reabrir el archivo de palabras.
     *
     * @param //word verifica si la palabra está en el diccionario.
     * Asuma que todos los espacios en blanco antes y despues de la palabra fueron removidos.
     * @return si la palabra está en el diccionario.
     */
    public boolean isWord(String word) {
        if(word != null){
            return this.search(word.toLowerCase());
        }
        return false;
    }


   //Métodos de búsqueda e inserción en el Trie

    public void insert(String key)
    {
        int level;
        int length = key.length();
        int index;

        TrieNode current = this.dictionary;

        for (level = 0; level < length; level++)
        {
            if (key.charAt(level) == '\'')
                index = 26;
            else
                index = key.charAt(level) - 'a';  //Se le resta el valor en ASCII de la letra a para que se sitúe entre 0 y 25 (las letras en el array)

            if (current.children[index] == null)
                current.children[index] = new TrieNode();

            current = current.children[index];
        }

        // mark last node as leaf
        current.isEndOfWord = true;
    }

    public boolean search(String key)
    {
        int level;
        int length = key.length();
        int index;
        TrieNode current = this.dictionary;

        for (level = 0; level < length; level++)
        {
            if (key.charAt(level) == '\'')
                index = 26;
            else
                index = key.charAt(level) - 'a';  //Se le resta el valor en ASCII de la letra a para que se sitúe entre 0 y 25 (las letras en el array)

            if (current.children[index] == null)
                return false;

            current = current.children[index];
        }

        return (current.isEndOfWord);
    }

}