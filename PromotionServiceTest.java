package game.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import game.exceptions.InsufficientXPException;
import game.service.Promotion.Level;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("PromotionService (UT10 a UT12)")
class PromotionServiceTest {

    @Test
    @DisplayName("UT10 estagiario com 50 XP nao e promovido e recebe InsufficientXPException")
    void ut10_promoverSemXpSuficiente_lancaExcecao() {

        DeveloperFake dev = new DeveloperFake(Level.INTERN, 50);

        InsufficientXPException excecao = assertThrows(InsufficientXPException.class,
            () -> PromotionService.attemptPromotion(dev));

        assertAll(
            () -> assertTrue(excecao.getMessage().contains("1950"), excecao.getMessage()),
            () -> assertTrue(excecao.getMessage().contains("Junior"), excecao.getMessage()),
            () -> assertEquals(Level.INTERN, dev.getPosition()),
            () -> assertEquals(50, dev.getXp()),
            () -> assertEquals(0, dev.chamadasResetXp)
        );
    }

    @Test
    @DisplayName("UT10 (borda) 1 XP abaixo do limite (1999) ainda lanca a excecao")
    void ut10_umXpAbaixoDoLimite_lancaExcecao() {

        DeveloperFake dev = new DeveloperFake(Level.INTERN, 1999);

        InsufficientXPException excecao = assertThrows(InsufficientXPException.class,
            () -> PromotionService.attemptPromotion(dev));
        assertAll(
            () -> assertTrue(excecao.getMessage().contains("Faltam 1"), excecao.getMessage()),
            () -> assertEquals(Level.INTERN, dev.getPosition()),
            () -> assertEquals(1999, dev.getXp())
        );
    }

    @ParameterizedTest(name = "UT11 {0} com exatamente {1} XP e promovido a {2}")
    @CsvSource({
        "INTERN, 2000, JUNIOR",
        "JUNIOR, 4000, PLENO",
        "PLENO,  8000, SENIOR",
        "SENIOR, 15000, CEO"
    })
    void ut11_promoverComXpSuficiente_atualizaCargo(Level atual, double xpNoLimite, Level esperado) {

        DeveloperFake dev = new DeveloperFake(atual, xpNoLimite);

        assertDoesNotThrow(() -> PromotionService.attemptPromotion(dev));

        assertEquals(esperado, dev.getPosition());
    }

    @Test
    @DisplayName("UT11 (borda) CEO nao tem proximo cargo: nada muda e nenhuma excecao e lancada")
    void ut11_ceoTentaPromocao_permaneceCeo() {

        DeveloperFake dev = new DeveloperFake(Level.CEO, 99999);

        assertDoesNotThrow(() -> PromotionService.attemptPromotion(dev));

        assertAll(
            () -> assertEquals(Level.CEO, dev.getPosition()),
            () -> assertEquals(99999, dev.getXp()),
            () -> assertEquals(0, dev.chamadasSetCurrentPosition),
            () -> assertEquals(0, dev.chamadasResetXp)
        );
    }

    @Test
    @DisplayName("UT12 apos a promocao o XP e zerado")
    void ut12_aposPromocao_xpZerado() throws InsufficientXPException {

        DeveloperFake dev = new DeveloperFake(Level.INTERN, 2000);

        PromotionService.attemptPromotion(dev);

        assertAll(
            () -> assertEquals(0, dev.getXp()),
            () -> assertEquals(1, dev.chamadasResetXp)
        );
    }

    @Test
    @DisplayName("UT12 (borda) XP excedente tambem e descartado: 2500 XP viram 0, nao 500")
    void ut12_xpExcedente_naoEhCarregadoParaOProximoCargo() throws InsufficientXPException {

        DeveloperFake dev = new DeveloperFake(Level.INTERN, 2500);

        PromotionService.attemptPromotion(dev);

        assertAll(
            () -> assertEquals(Level.JUNIOR, dev.getPosition()),
            () -> assertEquals(0, dev.getXp())
        );
    }
}
