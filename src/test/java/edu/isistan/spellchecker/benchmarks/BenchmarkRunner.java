package edu.isistan.spellchecker.benchmarks;

public class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
        // Configurar opciones de JMH
        org.openjdk.jmh.Main.main(new String[]{
                "edu.isistan.spellchecker.benchmarks.DiccionarioBenchmark",

        });
    }
}
