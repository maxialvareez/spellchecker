package edu.isistan.spellchecker.corrector.impl;

import java.util.Set;
import java.util.TreeSet;

import edu.isistan.spellchecker.corrector.Corrector;
import edu.isistan.spellchecker.corrector.Dictionary;
import edu.isistan.spellchecker.tokenizer.TokenScanner;

/**
 * Este corrector sugiere correciones cuando dos letras adyacentes han sido cambiadas.
 * <p>
 * Un error com�n es cambiar las letras de orden, e.g.
 * "with" -> "wiht". Este corrector intenta dectectar palabras con exactamente un swap.
 * <p>
 * Por ejemplo, si la palabra mal escrita es "haet", se debe sugerir
 * tanto "heat" como "hate".
 * <p>
 * Solo cambio de letras contiguas se considera como swap.
 */
public class SwapCorrector extends Corrector {

	private Dictionary dict;

	/**
	 * Construcye el SwapCorrector usando un Dictionary.
	 *
	 * @param dict 
	 * @throws IllegalArgumentException si el diccionario provisto es null
	 */
	public SwapCorrector(Dictionary dict) {
		if(dict == null){
			throw new IllegalArgumentException();
		} else {
			this.dict = dict;
		}
	}

	/**
	 * 
	 * Este corrector sugiere correciones cuando dos letras adyacentes han sido cambiadas.
	 * <p>
	 * Un error com�n es cambiar las letras de orden, e.g.
	 * "with" -> "wiht". Este corrector intenta detectar palabras con exactamente un swap.
	 * <p>
	 * Por ejemplo, si la palabra mal escrita es "haet", se debe sugerir
	 * tanto "heat" como "hate".
	 * <p>
	 * Solo cambio de letras contiguas se considera como swap.
	 * <p>
	 * Ver superclase.
	 *
	 * @param wrong 
	 * @return retorna un conjunto (potencialmente vac�o) de sugerencias.
	 * @throws IllegalArgumentException si la entrada no es una palabra v�lida 
	 */
	public Set<String> getCorrections(String wrong) {

		TreeSet<String> correctionSet = new TreeSet<>();

		if (dict.isWord(wrong)) {
			return new TreeSet<>();
		}

		if (!TokenScanner.isWord(wrong)) {
			throw new IllegalArgumentException();
		}

		//Primer swap
		String primerSwap = wrong.substring(1, 2) + wrong.substring(0, 1) + wrong.substring(2, wrong.length());
		if(dict.isWord(primerSwap.trim().toLowerCase())) {
			correctionSet.add(primerSwap);
		}

		//Resto de swap -1
		for(int i =0; i<wrong.length()-2; i++) {
			String swapWord = wrong.substring(0, i) + wrong.substring(i+1, i+2) + wrong.substring(i, i+1) + wrong.substring(i+2, wrong.length());
			if(dict.isWord(swapWord.trim().toLowerCase())) {
				correctionSet.add(swapWord);
			}
		}

		//Ultimo swap
		String ultimoSwap = wrong.substring(0, wrong.length()-2) + wrong.substring(wrong.length()-1, wrong.length()) + wrong.substring(wrong.length()-2, wrong.length()-1);
		if(dict.isWord(ultimoSwap.trim().toLowerCase())) {
			correctionSet.add(ultimoSwap);
		}

		return matchCase(wrong, correctionSet);
	}
}
