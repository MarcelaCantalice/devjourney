package game.integracao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import game.Developer;
import static org.junit.Assert.assertEquals;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InterfaceIT {

    @Test
    public void TI14_ajudaInicioDeCarreiraESaida() {
        Harness.Saida s = Harness.executarMain("2\n1\nJoao\n4\n");

        assertTrue("Ajuda lê a meta de Júnior de Level", s.out.contains("Para ser promovido a Junior, voce precisa de 2000 XP."));
        assertTrue("Nome digitado chega ao painel de Career",
                s.out.contains("DEV: Joao | CARGO: Estagiario | XP: 50.0"));
        assertTrue(s.out.contains("Saindo do jogo."));
    }

    @Test
    public void TI15_ajudaExibeChanceDeDesafioCorretamente() {
        Harness.Saida s = Harness.executarMain("2\n3\n");

        Developer comDesafio = new Developer("Ana");
        Harness.Saida primeiro = Harness.jogar(comDesafio, "1\n1234\n4\n", new Harness.ScriptedRandom(29, 0));
        assertTrue(primeiro.out.contains("DESAFIO SURPRESA"));
        assertEquals(75.0, comDesafio.getXp(), 0.001);

        Developer semDesafio = new Developer("Ana");
        Harness.Saida segundo = Harness.jogar(semDesafio, "1\n4\n", new Harness.ScriptedRandom(30));
        assertFalse(segundo.out.contains("DESAFIO SURPRESA"));
        assertEquals(50.0, semDesafio.getXp(), 0.001);

        assertTrue("Ajuda deve informar 30% de chance", s.out.contains("Ha 30% de chance"));
        assertFalse("Não deve exibir o escape literal %%", s.out.contains("30%%"));
    }
}
