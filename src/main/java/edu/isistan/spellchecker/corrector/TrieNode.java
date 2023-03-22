package edu.isistan.spellchecker.corrector;

import static edu.isistan.spellchecker.corrector.TrieDictionary.ALPHABET_SIZE;

public class TrieNode {

    TrieNode[] children = new TrieNode[ALPHABET_SIZE]; //Hijos del nodo

    boolean isEndOfWord;  //Indica si es el final de la palabra

    TrieNode(){
        isEndOfWord = false;
        for (int i = 0; i < ALPHABET_SIZE; i++)
            children[i] = null;
    }

}

