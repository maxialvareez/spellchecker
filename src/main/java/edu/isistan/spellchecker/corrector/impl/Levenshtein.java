package edu.isistan.spellchecker.corrector.impl;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import edu.isistan.spellchecker.corrector.Corrector;
import edu.isistan.spellchecker.corrector.Dictionary;
import edu.isistan.spellchecker.tokenizer.TokenScanner;

/**
 *
 * Un corrector inteligente que utiliza "edit distance" para generar correcciones.
 * 
 * La distancia de Levenshtein es el n�mero minimo de ediciones que se deber
 * realizar a un string para igualarlo a otro. Por edici�n se entiende:
 * <ul>
 * <li> insertar una letra
 * <li> borrar una letra
 * <li> cambiar una letra
 * </ul>
 *
 * Una "letra" es un caracter a-z (no contar los apostrofes).
 * Intercambiar letras (thsi -> this) <it>no</it> cuenta como una edici�n.
 * <p>
 * Este corrector sugiere palabras que esten a edit distance uno.
 */

public class Levenshtein extends Corrector {

	private Dictionary dict;

	/**
	 * Construye un Levenshtein Corrector usando un Dictionary.
	 * Debe arrojar <code>IllegalArgumentException</code> si el diccionario es null.
	 *
	 * @param dict
	 */
	public Levenshtein(Dictionary dict) {
		//throw new UnsupportedOperationException(); // STUB
		if(dict == null){
			throw new IllegalArgumentException();
		} else {
			this.dict = dict;
		}
	}

	/**
	 * @param s palabra
	 * @return todas las palabras a erase distance uno
	 */
	Set<String> getDeletions(String s) {
		TreeSet<String> correctionSet = new TreeSet<>();
		String resultadoBorrado;
		for(int i =0; i<s.length(); i++) {
			resultadoBorrado = this.deleteChar(s,i);

			if(dict.isWord(resultadoBorrado.trim().toLowerCase())) {
				correctionSet.add(resultadoBorrado);
			}
		}
		return correctionSet;
	}

	/**
	 * @param s palabra
	 * @return todas las palabras a substitution distance uno
	 */
	public Set<String> getSubstitutions(String s) {
		TreeSet<String> correctionSet = new TreeSet<>();
		String resultadoSustituido;
		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

		for(int i =0; i<s.length(); i++) {
			for(int c =0; c<alphabet.length-1; c++) {
				resultadoSustituido = this.changeChar(s,alphabet[c], i);
				if (dict.isWord(resultadoSustituido.trim().toLowerCase())) {
					if(!resultadoSustituido.trim().toLowerCase().equals(s)){
						correctionSet.add(resultadoSustituido);
					}
				}
			}
		}
		return correctionSet;
	}


	/**
	 * @param s palabra
	 * @return todas las palabras a insert distance uno
	 */
	public Set<String> getInsertions(String s) {
		TreeSet<String> correctionSet = new TreeSet<>();
		String resultadoBorrado;
		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

		for(int i =0; i<=s.length(); i++) {
			for(int c =0; c<alphabet.length-1; c++) {
				resultadoBorrado = this.addChar(s,alphabet[c], i);
				if (dict.isWord(resultadoBorrado.trim().toLowerCase())) {
					correctionSet.add(resultadoBorrado);
				}
			}
		}
		return correctionSet;
	}


	public Set<String> getCorrections(String wrong) {
		TreeSet<String> correctionSet = new TreeSet<>();

		if (dict.isWord(wrong)) {
			return new TreeSet<>();
		}

		if (!TokenScanner.isWord(wrong)) {
			throw new IllegalArgumentException();
		}
		correctionSet.addAll(this.getDeletions(wrong));
		correctionSet.addAll(this.getInsertions(wrong));
		correctionSet.addAll(this.getSubstitutions(wrong));

		if (Character.isUpperCase(wrong.toCharArray()[0])){
			correctionSet = correctionSet.stream().map(correction -> correction.substring(0,1).toUpperCase() + correction.substring(1).toLowerCase()).collect(Collectors.toCollection(TreeSet::new));
		}
		return correctionSet;
	}



	public String addChar(String str, char ch, int position) {
		StringBuilder sb = new StringBuilder(str);
		sb.insert(position, ch);
		return sb.toString();
	}

	public String deleteChar(String str, int position) {
		StringBuilder sb = new StringBuilder(str);
		sb.deleteCharAt(position);
		return sb.toString();
	}

	public String changeChar(String str,char c,  int position){
		StringBuilder sb = new StringBuilder(str);
		sb.setCharAt(position, c);
		return sb.toString();
	}
}

