package game.integracao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Scanner;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import game.Developer;
import game.challenges.BossMessage;
import game.challenges.GirlfriendChallenge;
import game.challenges.HRChecking;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DesafiosIT {

    private static final double D = 0.0001;

    @Test
    public void TI06_terminarRelacionamentoAtivaBoost4xNoProjeto() {
        Developer dev = new Developer("Ana");

        String entrada = "1\n" + "4\n"
                + Harness.repetir("1", 10)
                + "4\n";
        Harness.Saida s = Harness.jogar(dev, entrada, new Harness.ScriptedRandom(0, 1));

        assertTrue(s.out.contains("A sua namorada/o mandou uma mensagem"));
        assertTrue(s.out.contains("BOOST ATIVADO: XP x4.0 pelos proximos 5 projetos"));
        assertTrue("40 x 4 = 160", s.out.contains("Ganhou 160 XP."));

        assertEquals(205.0, dev.getXp(), D);
        assertEquals("Restam quatro projetos com boost", 4, dev.getBoostRemainingProjects());
    }

    @Test
    public void TI07_rhSenhaCorretaDepoisIncorreta() {
        Developer dev = new Developer("Ana");
        Scanner sc = Harness.teclado("1234\nerrada\n");
        HRChecking rh = new HRChecking();

        Harness.Saida s1 = Harness.capturar(() -> rh.execute(dev, sc));
        assertTrue(s1.out.contains("Rapido +25XP"));
        assertEquals(75.0, dev.getXp(), D);

        Harness.Saida s2 = Harness.capturar(() -> rh.execute(dev, sc));
        assertTrue(s2.out.contains("Lento -10XP"));
        assertEquals(65.0, dev.getXp(), D);
    }

    @Test
    public void TI08_chefeRespostaNaoNumerica() {
        Developer dev = new Developer("Ana");
        Scanner sc = Harness.teclado("abc\n");

        Harness.Saida s = Harness.capturar(() -> new BossMessage().execute(dev, sc));

        assertTrue(s.out.contains("Resposta inválida (não é um número)! O chefe ficou bravo. -30 XP"));
        assertEquals(20.0, dev.getXp(), D);
    }

    @Test
    public void TI09_namoradaSim_saldoDeveRefletirMais25() {
        Developer dev = new Developer("Ana");
        Harness.Saida s = Harness.capturar(() -> new GirlfriendChallenge().execute(dev, Harness.teclado("1\n")));

        assertTrue("Tela anuncia +25 XP", s.out.contains("Resposta: Sim (+25 XP)"));
        assertEquals("50 + 25 anunciado", 75.0, dev.getXp(), D);
    }

    @Test
    public void TI10_namoradaNao_saldoDeveRefletirMenos60() {
        Developer dev = new Developer("Ana");
        Harness.Saida s = Harness.capturar(() -> new GirlfriendChallenge().execute(dev, Harness.teclado("2\n")));

        assertTrue("Tela anuncia -60 XP", s.out.contains("(-60 XP)"));
        assertEquals("max(0, 50 - 60)", 0.0, dev.getXp(), D);
    }

    @Test
    public void TI11_namoradaAmanha_saldoDeveRefletirMenos30() {
        Developer dev = new Developer("Ana");
        Harness.Saida s = Harness.capturar(() -> new GirlfriendChallenge().execute(dev, Harness.teclado("3\n")));

        assertTrue("Tela anuncia -30 XP", s.out.contains("(-30 XP)"));
        assertEquals("50 - 30 anunciado", 20.0, dev.getXp(), D);
    }
}
