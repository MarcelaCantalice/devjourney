import game.Developer;
import game.exceptions.InsufficientXPException;
import game.projects.ProjectDataScience;
import game.projects.ProjectSmartContract;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegressaoBasicaTest {
    private final VersionAdapter adapter;
    private final double limiteJunior;

    RegressaoBasicaTest() {
        boolean versaoAntiga = Boolean.getBoolean("regressao.legacy");
        adapter = criarAdaptador();
        limiteJunior = versaoAntiga ? 200.0 : 2000.0;
    }

    private static VersionAdapter criarAdaptador() {
        try {
            String nome = Boolean.getBoolean("regressao.legacy") ? "LegacyAdapter" : "CurrentAdapter";
            return (VersionAdapter) Class.forName(nome).getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException erro) {
            throw new IllegalStateException("Nao foi possivel carregar o adaptador da versao", erro);
        }
    }

    @Test
    void reg01_xpNaoPodeSerNegativo() throws Exception {
        // Arrange
        Developer desenvolvedor = new Developer("Teste");
        // Act
        desenvolvedor.gainXp(-1000);
        // Assert
        assertEquals(0.0, adapter.xp(desenvolvedor), 0.00001);
    }

    @Test
    void reg02_promocaoSemXpSuficienteDeveSerRecusada() {
        // Arrange
        Developer desenvolvedor = new Developer("Teste");
        // Act + Assert
        assertThrows(InsufficientXPException.class, () -> adapter.promote(desenvolvedor));
        assertEquals("INTERN", desenvolvedor.getPosition().name());
    }

    @Test
    void reg03_promocaoNoLimiteDeveFuncionar() throws Exception {
        // Arrange
        Developer desenvolvedor = new Developer("Teste");
        desenvolvedor.gainXp(limiteJunior - adapter.xp(desenvolvedor));
        // Act
        adapter.promote(desenvolvedor);
        // Assert
        assertEquals("JUNIOR", desenvolvedor.getPosition().name());
        assertEquals(0.0, adapter.xp(desenvolvedor), 0.00001);
    }

    @Test
    void reg04_recompensaDataScienceDificuldadeDois() {
        // Arrange
        ProjectDataScience projeto = new ProjectDataScience("Teste", 2);
        int xpEsperado = Boolean.getBoolean("regressao.legacy") ? 94 : 35;
        // Act
        int xpObtido = projeto.finishProject();
        // Assert
        assertEquals(xpEsperado, xpObtido);
    }

    @Test
    void reg05_recompensaSmartContractDificuldadeDois() {
        // Arrange
        ProjectSmartContract projeto = new ProjectSmartContract("Teste", 2);
        int xpEsperado = Boolean.getBoolean("regressao.legacy") ? 120 : 30;
        // Act
        int xpObtido = projeto.finishProject();
        // Assert
        assertEquals(xpEsperado, xpObtido);
    }

    @Test
    @DisabledIfSystemProperty(named = "regressao.legacy", matches = "true", disabledReason = "NAO APLICAVEL: boost temporario nao existe neste commit")
    void reg07_boostExpiraAposDoisUsos() {
        // Arrange
        Developer desenvolvedor = new Developer("Teste");
        adapter.activateXpBoost(desenvolvedor, 2.0, 2);

        // Act
        double multiplicadorInicial = adapter.xpMultiplier(desenvolvedor);
        int usosIniciais = adapter.boostRemainingProjects(desenvolvedor);
        adapter.useBoost(desenvolvedor);
        double multiplicadorAposUmUso = adapter.xpMultiplier(desenvolvedor);
        int usosAposUmUso = adapter.boostRemainingProjects(desenvolvedor);
        adapter.useBoost(desenvolvedor);
        double multiplicadorFinal = adapter.xpMultiplier(desenvolvedor);
        int usosFinais = adapter.boostRemainingProjects(desenvolvedor);

        // Assert
        assertEquals(2.0, multiplicadorInicial, 0.00001);
        assertEquals(2, usosIniciais);
        assertEquals(2.0, multiplicadorAposUmUso, 0.00001);
        assertEquals(1, usosAposUmUso);
        assertEquals(1.0, multiplicadorFinal, 0.00001);
        assertEquals(0, usosFinais);
    }

    @Test
    @DisabledIfSystemProperty(named = "regressao.legacy", matches = "true", disabledReason = "NAO APLICAVEL: GirlfriendChallenge nao existe neste commit")
    void reg09_1_opcaoSim() throws Exception {
        verificarRecompensaDoDesafio(1, 25);
    }

    @Test
    @DisabledIfSystemProperty(named = "regressao.legacy", matches = "true", disabledReason = "NAO APLICAVEL: GirlfriendChallenge nao existe neste commit")
    void reg09_2_opcaoNao() throws Exception {
        verificarRecompensaDoDesafio(2, -60);
    }

    @Test
    @DisabledIfSystemProperty(named = "regressao.legacy", matches = "true", disabledReason = "NAO APLICAVEL: GirlfriendChallenge nao existe neste commit")
    void reg09_3_opcaoAmanha() throws Exception {
        verificarRecompensaDoDesafio(3, -30);
    }

    private void verificarRecompensaDoDesafio(int opcao, int xpEsperado) throws Exception {
        // Arrange
        Developer desenvolvedor = new Developer("Teste");
        desenvolvedor.gainXp(200.0 - adapter.xp(desenvolvedor));
        double xpInicial = adapter.xp(desenvolvedor);
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        PrintStream original = System.out;

        // Act
        try (Scanner entrada = new Scanner(opcao + "\n");
             PrintStream captura = new PrintStream(saida, true, StandardCharsets.UTF_8)) {
            System.setOut(captura);
            adapter.girlfriendChallenge(desenvolvedor, entrada);
        } finally {
            System.setOut(original);
        }

        // Assert
        Matcher mensagem = Pattern.compile("\\(([+-]\\d+)\\s*XP\\)").matcher(saida.toString(StandardCharsets.UTF_8));
        assertEquals(true, mensagem.find(), "A mensagem deve anunciar a variacao de XP");
        int xpAnunciado = Integer.parseInt(mensagem.group(1));
        assertEquals(xpEsperado, xpAnunciado, "XP anunciado");
        assertEquals((double) xpAnunciado, adapter.xp(desenvolvedor) - xpInicial, 0.00001, "XP aplicado deve corresponder ao anunciado");
    }

    @Test
    void reg10_promocaoUmPontoAbaixoDeveSerRecusada() throws Exception {
        // Arrange
        Developer desenvolvedor = new Developer("Teste");
        double xpDesejado = limiteJunior - 1;
        desenvolvedor.gainXp(xpDesejado - adapter.xp(desenvolvedor));
        // Act + Assert
        assertThrows(InsufficientXPException.class, () -> adapter.promote(desenvolvedor));
        assertEquals("INTERN", desenvolvedor.getPosition().name());
        assertEquals(xpDesejado, adapter.xp(desenvolvedor), 0.00001);
    }
}
