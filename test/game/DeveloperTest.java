
package game;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import game.service.Promotion.Level;

class DeveloperTest {

    @Test
    @DisplayName("UT01 - Estado inicial do desenvolvedor")
    void ut01_estadoInicial() {
        Developer dev = new Developer("TesteQA");

        assertAll(
            () -> assertEquals(50.0, dev.getXp()),
            () -> assertEquals(Level.INTERN, dev.getPosition()),
            () -> assertTrue(dev.getSkills().isEmpty())
        );
    }

    @Test
    @DisplayName("UT02 - Ganho de XP")
    void ut02_ganhoDeXp() {
        Developer dev = new Developer("TesteQA");

        dev.gainXp(100);

        assertEquals(150.0, dev.getXp());
    }

    @Test
    @DisplayName("UT03 - XP não pode ficar negativo")
    void ut03_xpNaoNegativo() {
        Developer dev = new Developer("TesteQA");

        dev.gainXp(-100);

        assertEquals(0.0, dev.getXp());
    }

    @Test
    @DisplayName("UT04 - Adição de habilidades")
    void ut04_adicionarHabilidade() {
        Developer dev = new Developer("TesteQA");

        boolean adicionada = dev.addSkill("Java");

        assertAll(
            () -> assertTrue(adicionada),
            () -> assertTrue(dev.getSkills().contains("Java")),
            () -> assertEquals(1, dev.getSkills().size())
        );
    }

    @Test
    @DisplayName("UT05 - Impedir habilidades duplicadas")
    void ut05_habilidadeDuplicada() {
        Developer dev = new Developer("TesteQA");

        boolean primeiraAdicao = dev.addSkill("Java");
        boolean segundaAdicao = dev.addSkill("Java");

        assertAll(
            () -> assertTrue(primeiraAdicao),
            () -> assertFalse(segundaAdicao),
            () -> assertEquals(1, dev.getSkills().size())
        );
    }

    @Test
    @DisplayName("UT06 - Expiração do boost de XP")
    void ut06_expiracaoBoost() {
        Developer dev = new Developer("TesteQA");

        dev.activateXpBoost(4.0, 2);

        assertAll(
            () -> assertEquals(4.0, dev.getXpMultiplier()),
            () -> assertEquals(2, dev.getBoostRemainingProjects())
        );

        dev.useBoost();

        assertEquals(1, dev.getBoostRemainingProjects());
        assertEquals(4.0, dev.getXpMultiplier());

        dev.useBoost();

        assertAll(
            () -> assertEquals(0, dev.getBoostRemainingProjects()),
            () -> assertEquals(1.0, dev.getXpMultiplier())
        );
    }

    @Test
    @DisplayName("UT02 - Ganho de zero XP")
    void ut02_ganhoZeroXp() {
        Developer dev = new Developer("TesteQA");

        dev.gainXp(0);

        assertEquals(50.0, dev.getXp());
    }

    @Test
    @DisplayName("UT03 - Perda parcial de XP")
    void ut03_perdaParcialXp() {
        Developer dev = new Developer("TesteQA");

        dev.gainXp(-20);

        assertEquals(30.0, dev.getXp());
    }

    @Test
    @DisplayName("UT06 - Boost permanece expirado")
    void ut06_boostPermaneceExpirado() {
        Developer dev = new Developer("TesteQA");

        dev.activateXpBoost(4.0, 1);

        dev.useBoost();
        dev.useBoost();

        assertAll(
            () -> assertEquals(0, dev.getBoostRemainingProjects()),
            () -> assertEquals(1.0, dev.getXpMultiplier())
        );
    }

    @Test
    @DisplayName("UT06 - Aumento permanente de XP")
    void ut06_boostPermanente() {
        Developer dev = new Developer("TesteQA");

        dev.addPermanentXpBoost(20);

        assertEquals(1.2, dev.getPermanentXpBoost(), 0.0001);
    }

    @Test
    @DisplayName("UT04 - Iniciar estudo de habilidade")
    void ut04_iniciarEstudoHabilidade() {
        Developer dev = new Developer("TesteQA");

        dev.setActiveSkillStudy("Java");

        assertAll(
            () -> assertEquals("Java", dev.getActiveSkillStudy()),
            () -> assertEquals(0, dev.getSkillProgress())
        );
    }

    @Test
    @DisplayName("UT04 - Atualizar progresso de estudo")
    void ut04_atualizarProgressoEstudo() {
        Developer dev = new Developer("TesteQA");

        dev.setActiveSkillStudy("Java");
        dev.setSkillProgress(50);

        assertAll(
            () -> assertEquals("Java", dev.getActiveSkillStudy()),
            () -> assertEquals(50, dev.getSkillProgress())
        );
    }

    @Test
    @DisplayName("UT04 - Finalizar estudo de habilidade")
    void ut04_finalizarEstudoHabilidade() {
        Developer dev = new Developer("TesteQA");

        dev.setActiveSkillStudy("Java");
        dev.setSkillProgress(80);

        dev.finishSkillStudy();

        assertAll(
            () -> assertNull(dev.getActiveSkillStudy()),
            () -> assertEquals(0, dev.getSkillProgress())
        );
    }

    @Test
    @DisplayName("UT03 - Redefinição de XP")
    void ut03_redefinirXp() {
        Developer dev = new Developer("TesteQA");

        dev.gainXp(100);
        dev.resetXp();

        assertEquals(0.0, dev.getXp());
    }

    @Test
    @DisplayName("UT01 - Alteração de cargo")
    void ut01_alterarCargo() {
        Developer dev = new Developer("TesteQA");

        dev.setCurrentPosition(Level.JUNIOR);

        assertEquals(Level.JUNIOR, dev.getPosition());
    }

    @Test
    @DisplayName("UT01 - Consulta do nome do desenvolvedor")
    void ut01_consultarNome() {
        Developer dev = new Developer("TesteQA");

        assertEquals("TesteQA", dev.getName());
    }





}
