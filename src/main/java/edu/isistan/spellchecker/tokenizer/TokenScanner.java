package edu.isistan.spellchecker.tokenizer;

import java.io.Reader;
import java.util.Iterator;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Dado un archivo provee un m�todo para recorrerlo.
 */
public class TokenScanner implements Iterator<String> {
    private Reader r;
    private int c = -1;

    /**
     * Crea un TokenScanner.
     * <p>
     * Como un iterador, el TokenScanner solo debe leer lo justo y
     * necesario para implementar los m�todos next() y hasNext().
     * No se debe leer toda la entrada de una.
     * <p>
     *
     * @param in fuente de entrada
     * @throws IOException              si hay alg�n error leyendo.
     * @throws IllegalArgumentException si el Reader provisto es null
     */
    public TokenScanner(java.io.Reader in) throws IOException {
        if (in != null) {
            this.r = in;
            this.c = r.read();
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Determina si un car�cer es una caracter v�lido para una palabra.
     * <p>
     * Un caracter v�lido es una letra (
     * Character.isLetter) o una apostrofe '\''.
     *
     * @param c
     * @return true si es un caracter
     */
    public static boolean isWordCharacter(int c) {
        return ((Character.isLetter(c)) || c == '\'');
    }


    /**
     * Determina si un string es una palabra v�lida.
     * Null no es una palabra v�lida.
     * Un string que todos sus caracteres son v�lidos es una
     * palabra. Por lo tanto, el string vac�o es una palabra v�lida.
     *
     * @param s
     * @return true si el string es una palabra.
     */
    public static boolean isWord(String s) {
        boolean palabraValida = true;
        if (s != null) {
            if (!s.isEmpty()) {
                for (int i = 0; i < s.length(); i++) {
                    if (!isWordCharacter(s.charAt(i))) {
                        palabraValida = false;
                    }
                }
                return palabraValida;
            }
        }
        return false;
    }

    /**
     * Determina si hay otro token en el reader.
     */
    public boolean hasNext() {
        return (c != -1);
    }

    /**
     * Retorna el siguiente token.
     *
     * @throws NoSuchElementException cuando se alcanz� el final de stream
     */
    public String next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        String answer = "";

        if (isWordCharacter(this.c) && hasNext()) {
            while ((isWordCharacter(this.c))) {
                answer = answer + (char) c;
                try {
                    c = r.read();
                } catch (IOException e) {
                    c = -1;
                }
            }
        } else {
            while (!isWordCharacter(c) && hasNext()) {
                answer = answer + (char) c;
                try {
                    c = r.read();
                } catch (IOException e) {
                    c = -1;
                }
            }
        }

        return answer;
    }

}
