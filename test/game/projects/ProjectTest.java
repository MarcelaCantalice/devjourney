package game.projects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Project (UT07 a UT09)")
class ProjectTest {

    static Stream<Arguments> tiposDeProjeto() {
        return Stream.of(
            Arguments.of("ProjectAI", (BiFunction<String, Integer, Project>) ProjectAI::new),
            Arguments.of("ProjectDataScience", (BiFunction<String, Integer, Project>) ProjectDataScience::new),
            Arguments.of("ProjectSmartContract", (BiFunction<String, Integer, Project>) ProjectSmartContract::new)
        );
    }

    @ParameterizedTest(name = "UT07 [{0}] projeto recem-criado comeca com progresso 0%")
    @MethodSource("tiposDeProjeto")
    void ut07_projetoRecemCriado_comecaComProgressoZero(String tipo,
            BiFunction<String, Integer, Project> fabrica) {

        Project projeto = fabrica.apply("Relatorio Mensal", 1);

        assertEquals(0, projeto.getProgress(), tipo + " deveria iniciar com 0% de progresso");
    }

    @Test
    @DisplayName("UT08 atualizar o progresso altera o valor para o informado")
    void ut08_setProgress_atualizaProgresso() {

        Project projeto = new ProjectDataScience("Pipeline de Dados", 5);

        projeto.setProgress(30);

        assertEquals(30, projeto.getProgress());
    }

    @Test
    @DisplayName("UT08 (borda) progresso acumula em passos de 10% ate 100%, como no Career")
    void ut08_progressoIncremental_chegaA100() {

        Project projeto = new ProjectAI("Feature de Recomendacao", 6);

        for (int turno = 1; turno <= 10; turno++) {
            projeto.setProgress(projeto.getProgress() + 10);
        }

        assertEquals(100, projeto.getProgress());
    }

    @ParameterizedTest(name = "UT08 (valor invalido) setProgress({0}) deve manter o progresso entre 0 e 100")
    @ValueSource(ints = {-10, 150})
    void ut08_progressoForaDoIntervalo_naoDeveSerAceito(int valorInvalido) {

        Project projeto = new ProjectDataScience("Relatorio Mensal", 1);

        projeto.setProgress(valorInvalido);

        int progresso = projeto.getProgress();
        assertTrue(progresso >= 0 && progresso <= 100,
            "progresso deveria ficar entre 0 e 100, mas ficou " + progresso + "%");
    }

    @ParameterizedTest(name = "UT09 [{0}] guarda nome e dificuldade informados no construtor")
    @MethodSource("tiposDeProjeto")
    void ut09_dadosDoProjeto_nomeEDificuldadeCorretos(String tipo,
            BiFunction<String, Integer, Project> fabrica) {

        Project projeto = fabrica.apply("Refatoracao Legacy", 7);

        assertAll(
            () -> assertEquals("Refatoracao Legacy", projeto.getName()),
            () -> assertEquals(7, projeto.getDifficulty())
        );
    }

    @Test
    @DisplayName("UT09 atualizar o progresso nao altera nome nem dificuldade")
    void ut09_setProgress_naoAlteraNomeNemDificuldade() {

        Project projeto = new ProjectSmartContract("Site Institucional Basico", 3);

        projeto.setProgress(50);

        assertAll(
            () -> assertEquals("Site Institucional Basico", projeto.getName()),
            () -> assertEquals(3, projeto.getDifficulty())
        );
    }
}
