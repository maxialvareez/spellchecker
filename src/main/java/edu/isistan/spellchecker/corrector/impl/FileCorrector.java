package edu.isistan.spellchecker.corrector.impl;

import java.util.*;

import edu.isistan.spellchecker.corrector.Corrector;
import edu.isistan.spellchecker.tokenizer.TokenScanner;

import java.io.*;

/**
 * Corrector basado en un archivo.
 * 
 */
public class FileCorrector extends Corrector {

	private TreeMap<String, TreeSet<String>> correcciones = new TreeMap<>();

	/** Clase especial que se utiliza al tener 
	 * algún error de formato en el archivo de entrada.
	 */
	public static class FormatException extends Exception {
		public FormatException(String msg) {
			super(msg);
		}
	}


	/**
	 * Constructor del FileReader
	 *
	 * Utilice un BufferedReader para leer el archivo de definición
	 *
	 * <p> 
	 * Cada línea del archivo del diccionario tiene el siguiente formato: 
	 * misspelled_word,corrected_version
	 *
	 * <p>
	 *Ejemplo:<br>
	 * <pre>
	 * aligatur,alligator<br>
	 * baloon,balloon<br>
	 * inspite,in spite<br>
	 * who'ev,who've<br>
	 * ther,their<br>
	 * ther,there<br>
	 * </pre>
	 * <p>
	 * Estas líneas no son case-insensitive, por lo que todas deberían generar el mismo efecto:<br>
	 * <pre>
	 * baloon,balloon<br>
	 * Baloon,balloon<br>
	 * Baloon,Balloon<br>
	 * BALOON,balloon<br>
	 * bAlOon,BALLOON<br>
	 * </pre>
	 * <p>
	 * Debe ignorar todos los espacios vacios alrededor de las palabras, por lo
	 * que estas entradas son todas equivalentes:<br>
	 * <pre>
	 * inspite,in spite<br>
	 *    inspite,in spite<br>
	 * inspite   ,in spite<br>
	 *  inspite ,   in spite  <br>
	 * </pre>
	 * Los espacios son permitidos dentro de las sugerencias. 
	 *
	 * <p>
	 * Debería arrojar <code>FileCorrector.FormatException</code> si se encuentra algún
	 * error de formato:<br>
	 * <pre>
	 * ,correct<br>
	 * wrong,<br>
	 * wrong correct<br>
	 * wrong,correct,<br>
	 * </pre>
	 * <p>
	 *
	 * @param r Secuencia de caracteres 
	 * @throws IOException error leyendo el archivo
	 * @throws FileCorrector.FormatException error de formato
	 * @throws IllegalArgumentException reader es null
	 */
	public FileCorrector(Reader r) throws IOException, FormatException {
		if(r == null){  //Si es null, excepción.
			throw new IllegalArgumentException();
		}
		BufferedReader read = new BufferedReader(r);
		String line;
		while((line = read.readLine()) != null){ // Si cada linea que se lee es distinta de null.
			String nuevaLinea = line.trim().toLowerCase();
			String[] palabras = nuevaLinea.split(","); // Se separan en la coma.
			if(palabras.length != 2){ // Si no hay dos palabras en el arreglo, es un error de formato.
				throw new FileCorrector.FormatException("Error de formato");
			}
			String error = palabras[0].trim(); // La primera parte es el error.
			String palabraCorrecta = palabras[1].trim(); // La segunda parte es la parte correcta.

			TreeSet<String> correccionesArchivo = correcciones.get(error);
			if(correccionesArchivo == null){ // Si no había correciones en el archivo, de esa palabra.
				TreeSet<String> nuevaCorreccion = new TreeSet<>();
				nuevaCorreccion.add(palabraCorrecta);
				correcciones.put(error, nuevaCorreccion); // Se agrega el error y un set con la palabra correcta.
			} else {
				correccionesArchivo.add(palabraCorrecta);
				correcciones.put(error, correccionesArchivo); // Se agrega la corrección al set, total si está repetida no se agrega.
			}
		}
	}

	/** Construye el Filereader.
	 *
	 * @param filename 
	 * @throws IOException 
	 * @throws FileCorrector.FormatException 
	 * @throws FileNotFoundException 
	 */
	public static FileCorrector make(String filename) throws IOException, FormatException {
		Reader r = new FileReader(filename);
		FileCorrector fc;
		try {
			fc = new FileCorrector(r);
		} finally {
			if (r != null) { r.close(); }
		}
		return fc;
	}

	/**
	 * Retorna una lista de correcciones para una palabra dada.
	 * Si la palabra mal escrita no está en el diccionario el set es vacio.
	 * <p>
	 * Ver superclase.
	 *
	 * @param wrong 
	 * @return retorna un conjunto (potencialmente vacío) de sugerencias.
	 * @throws IllegalArgumentException si la entrada no es una palabra válida 
	 */
	public Set<String> getCorrections(String wrong) {
		if(!TokenScanner.isWord(wrong)){
			throw new IllegalArgumentException();
		}
		String aux = wrong.trim().toLowerCase();
		TreeSet<String> correccionesArchivo = correcciones.get(aux);
		if(correccionesArchivo != null){
			Set<String> correcta = matchCase(wrong, correccionesArchivo);
			return correcta;
		}
		return new TreeSet<>();
	}
}
