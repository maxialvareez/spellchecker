package edu.isistan.spellchecker.corrector;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import edu.isistan.spellchecker.tokenizer.TokenScanner;

/**
 * El diccionario maneja todas las palabras conocidas.
 * El diccionario es case insensitive
 *
 * Una palabra "válida" es una secuencia de letras (determinado por Character.isLetter)
 * o apostrofes.
 */
public class TrieDictionary {

    private TrieDictionary dictionary; // No agrega repetidos el TreeSet, es una buena opción.
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
        Tree dictionary = new Tree();
        if(ts == null){
            throw new IllegalArgumentException();
        }
        while (ts.hasNext()){
            String nextString = ts.next();
            if(TokenScanner.isWord(nextString)){
                dictionary.add(nextString.toLowerCase());
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
    public static Dictionary make(String filename) throws IOException {
        Reader r = new FileReader(filename);
        Dictionary d = new Dictionary(new TokenScanner(r));
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
    public int getNumWords() {
        int numPalabras = dictionary.size();
        return numPalabras;
    }

    /**
     * Testea si una palabra es parte del diccionario. Si la palabra no está en
     * el diccionario debe retornar false. null debe retornar falso.
     * Si en el diccionario está la palabra Dog y se pregunta por la palabra dog
     * debe retornar true, ya que es case insensitive.
     *
     *Llamar a este método no debe reabrir el archivo de palabras.
     *
     * @param word verifica si la palabra está en el diccionario.
     * Asuma que todos los espacios en blanco antes y despues de la palabra fueron removidos.
     * @return si la palabra está en el diccionario.
     */
    public boolean isWord(String word) {
        if(word != null){
            return dictionary.contains(word.toLowerCase());
        }
        return false;
    }

    public class Tree {
        private Node root;

        public Tree() {
            root = new Node();
            root.children = new ArrayList<Node>();
        }

        public void agregar(String palabra) {


            char[] letras = palabra.toCharArray();
            if (this.root.parent != null && this.root.children == null){ //si no esta vacio el arbol
                addWord(this.root,letras,0 );
            }


        }
        private void addWord(Node node, char[] letters, int pos) {
            if (pos <= letters.length) {
                    int i = 0;
                    if(node.hasChildren()) {
                        while (i < node.getChildren().size()) {
                            if (node.getChildren().get(i).getData() == letters[pos]) {
                                addWord(node.getChildren().get(i), letters, pos + 1);
                                break;
                            }
                            i++;
                        }
                        if (i == node.getChildren().size()) {
                            node.addChildren(letters[pos]);
                        }
                    }
                    else{
                        node.addChildren(letters[pos]);
                        addWord(node.getChildren().get(0), letters,pos+1);
                    }
            }
        }

        public class Node {
            private char data;
            private Node parent;
            private List<Node> children;

            public Node(){}
            public Node(char data, Node parent){
                this.data = data;
                this.parent = parent;
            }
            public List<Node> getChildren(){
                return this.children;
        }

            public Node getParent() {
                return parent;
            }

            public Node setParent(Node parent) {
                this.parent = parent;
                return this;
            }
            public void addChildren(char c){
                if(this.children != null) {
                    this.children.add(new Node(c, this));
                }
                else{
                    this.children = new ArrayList<>();
                    this.children.add(new Node(c, this));
                }
            }

            public char getData() {
                return data;
            }

            public Node setData(char data) {
                this.data = data;
                return this;
            }

            public boolean hasChildren(){
                if (this.children == null){
                    return false;
                }
                return true;
            }
        }
    }

}