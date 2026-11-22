package game.integracao;

import java.io.PrintStream;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class ExecutorLocal {

    public static void main(String[] args) {
        final PrintStream console = System.out;
        JUnitCore core = new JUnitCore();
        final StringBuilder resumo = new StringBuilder();
        core.addListener(new RunListener() {
            private boolean falhou;
            @Override public void testStarted(Description d) {
                falhou = false;
                console.println("\n>>> INICIO " + d.getClassName() + "." + d.getMethodName());
            }
            @Override public void testFailure(Failure f) {
                falhou = true;
                resumo.append(String.format("FALHOU  %-60s %s%n", f.getDescription().getMethodName(), f.getMessage()));
            }
            @Override public void testFinished(Description d) {
                if (!falhou) resumo.append(String.format("PASSOU  %s%n", d.getMethodName()));
            }
        });
        Result r = core.run(PromocaoIT.class, ProjetoEstudoIT.class, DesafiosIT.class, InterfaceIT.class);

        console.println("\n================ RESUMO POR CASO ================");
        console.print(resumo);
        console.printf("Executados: %d | Aprovados: %d | Falhos: %d | Ignorados: %d | Tempo: %d ms%n",
                r.getRunCount(), r.getRunCount() - r.getFailureCount(), r.getFailureCount(),
                r.getIgnoreCount(), r.getRunTime());
        System.exit(r.wasSuccessful() ? 0 : 1);
    }
}
