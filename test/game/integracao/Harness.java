package game.integracao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.Scanner;

import game.Career;
import game.Developer;

final class Harness {

    private Harness() { }

    static final class ScriptedRandom extends Random {
        private final Deque<Integer> valores = new ArrayDeque<>();

        ScriptedRandom(int... seq) {
            for (int v : seq) valores.add(v);
        }

        @Override
        public int nextInt(int bound) {
            Integer v = valores.poll();
            return v == null ? bound - 1 : v;
        }
    }

    static final class Saida {
        final String out;
        final String err;
        Saida(String out, String err) { this.out = out; this.err = err; }
        String tudo() { return out + "\n" + err; }
    }

    static Scanner teclado(String linhas) {
        return new Scanner(new ByteArrayInputStream(linhas.getBytes(StandardCharsets.UTF_8)), "UTF-8");
    }

    static void injetarRandom(Career career, Random r) {
        try {
            Field f = Career.class.getDeclaredField("randomGenerator");
            f.setAccessible(true);
            f.set(career, r);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Não foi possível injetar o Random em Career", e);
        }
    }

    static Saida jogar(Developer dev, String entrada, Random sorteio) {
        Scanner sc = teclado(entrada);
        Career career = new Career(dev, sc);
        injetarRandom(career, sorteio);
        return capturar(career::startJourney);
    }

    static Saida capturar(Runnable acao) {
        PrintStream outOriginal = System.out;
        PrintStream errOriginal = System.err;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
            System.setErr(new PrintStream(err, true, StandardCharsets.UTF_8));
            acao.run();
        } finally {
            System.setOut(outOriginal);
            System.setErr(errOriginal);
        }
        Saida s = new Saida(out.toString(StandardCharsets.UTF_8), err.toString(StandardCharsets.UTF_8));

        outOriginal.println(s.out);
        if (!s.err.isEmpty()) outOriginal.println("[stderr] " + s.err);
        return s;
    }

    static Saida executarMain(String entrada) {
        InputStream inOriginal = System.in;
        try {
            System.setIn(new ByteArrayInputStream(entrada.getBytes(StandardCharsets.UTF_8)));
            return capturar(() -> game.Main.main(new String[0]));
        } finally {
            System.setIn(inOriginal);
        }
    }

    static String repetir(String linha, int vezes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vezes; i++) sb.append(linha).append('\n');
        return sb.toString();
    }
}
