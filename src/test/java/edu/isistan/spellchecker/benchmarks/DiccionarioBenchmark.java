package edu.isistan.spellchecker.benchmarks;

import edu.isistan.spellchecker.corrector.Dictionary;
import edu.isistan.spellchecker.tokenizer.TokenScanner;
import org.openjdk.jmh.annotations.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class DiccionarioBenchmark {


    private Dictionary dictionary;

    private String wordToSearch = "kcgbaktdkyhaxnopkldj";

    @Setup(Level.Iteration)
    public void setUp() throws IOException {
        loadDictionary("palabras.txt");
        System.out.println("Tamaño del diccionario: " + dictionary.getNumWords());
    }

    private void loadDictionary(String filePath) throws IOException {
        TokenScanner tokenScanner = new TokenScanner(new FileReader(filePath));
        dictionary = new Dictionary(tokenScanner);
    }

    @Benchmark
    public void searchWord() {
        dictionary.isWord(this.wordToSearch);
    }

}
