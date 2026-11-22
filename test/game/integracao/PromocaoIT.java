package game.integracao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import game.Developer;
import game.service.Promotion.Level;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PromocaoIT {

    private static final double D = 0.0001;

    @Test
    public void TI01_promocaoRecusadaCom1999Xp() {
        Developer dev = new Developer("Ana");
        dev.gainXp(1949);

        Harness.Saida s = Harness.jogar(dev, "3\n4\n", new Harness.ScriptedRandom());

        assertEquals("Cargo deve permanecer Estagiario", Level.INTERN, dev.getPosition());
        assertEquals("Saldo deve ser preservado", 1999.0, dev.getXp(), D);
        assertTrue("Deve informar que falta 1 XP", s.err.contains("PROMOCAO FALHOU: Faltam 1.0 XP para Junior"));
    }

    @Test
    public void TI02_promocaoAceitaComExatos2000Xp() {
        Developer dev = new Developer("Ana");
        dev.gainXp(1950);

        Harness.Saida s = Harness.jogar(dev, "3\n4\n", new Harness.ScriptedRandom());

        assertEquals(Level.JUNIOR, dev.getPosition());
        assertEquals("XP zera após promoção", 0.0, dev.getXp(), D);
        assertTrue(s.out.contains("PROMOVIDO PARA Junior"));
        assertTrue("Painel após o turno exibe o novo cargo",
                s.out.contains("CARGO: Junior | XP: 0.0"));
    }

    @Test
    public void TI03_quatroBonusSudoHabilitamPromocao() {
        Developer dev = new Developer("Ana");

        Harness.Saida s = Harness.jogar(dev, Harness.repetir("sudo", 4) + "3\n4\n", new Harness.ScriptedRandom());

        int creditos = s.out.split("Bonus de 500 XP\\.", -1).length - 1;
        assertEquals("Quatro créditos de 500 XP", 4, creditos);
        assertTrue("Saldo antes da promoção deve ter chegado a 2050", s.out.contains("XP: 2050.0"));
        assertEquals(Level.JUNIOR, dev.getPosition());
        assertEquals(0.0, dev.getXp(), D);
    }

    @Test
    public void TI12_promocaoDeSeniorParaCeoEncerraOJogo() {
        Developer dev = new Developer("Ana");
        dev.setCurrentPosition(Level.SENIOR);
        dev.resetXp();
        dev.gainXp(15000);

        Harness.Saida s = Harness.jogar(dev, "3\n", new Harness.ScriptedRandom());

        assertEquals(Level.CEO, dev.getPosition());
        assertEquals(0.0, dev.getXp(), D);
        assertTrue(s.out.contains("PROMOVIDO PARA CEO"));
        assertTrue(s.out.contains("--- GAME OVER: VOCE E O CEO! ---"));
    }
}
