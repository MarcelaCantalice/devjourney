package game.integracao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import game.Developer;
import game.skills.SkillCatalog;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProjetoEstudoIT {

    private static final double D = 0.0001;

    @Test
    public void TI04_primeiroProjetoConcluidoEProximoAberto() {
        Developer dev = new Developer("Ana");

        Harness.Saida s = Harness.jogar(dev, Harness.repetir("1", 12) + "4\n", new Harness.ScriptedRandom());

        assertTrue(s.out.contains("Iniciando projeto: Site Institucional Basico"));
        assertTrue(s.out.contains("Progresso: 90%"));
        assertTrue("Recompensa do SmartContract dificuldade 3 = 10 + 3*10", s.out.contains("Ganhou 40 XP."));

        assertEquals(135.0, dev.getXp(), D);
        assertTrue("Próximo projeto deve ser aberto", s.out.contains("Iniciando projeto: Bot de Respostas Automaticas"));
    }

    @Test
    public void TI05_skillJavaAvancadoAumentaRecompensaDoProjeto() {
        Developer dev = new Developer("Ana");
        int indice = indiceNoMenuDeEstudo(dev, "Java Avancado");

        String entrada = "2\n" + indice + "\n"
                + Harness.repetir("2", 4)
                + Harness.repetir("1", 11)
                + "4\n";
        Harness.Saida s = Harness.jogar(dev, entrada, new Harness.ScriptedRandom());

        assertTrue(dev.getSkills().contains("Java Avancado"));
        assertEquals("Bônus permanente 1,05x", 1.05, dev.getPermanentXpBoost(), D);
        assertTrue("40 x 1,05 = 42", s.out.contains("Ganhou 42 XP."));

        assertEquals(222.0, dev.getXp(), D);
    }

    @Test
    public void TI13_listaDeProjetosERepostaAposOsTresIniciais() {
        Developer dev = new Developer("Ana");

        Harness.Saida s = Harness.jogar(dev, Harness.repetir("1", 34) + "4\n", new Harness.ScriptedRandom());

        int iSite = s.out.indexOf("Iniciando projeto: Site Institucional Basico");
        int iBot = s.out.indexOf("Iniciando projeto: Bot de Respostas Automaticas");
        int iRel = s.out.indexOf("Iniciando projeto: Relatorio Mensal");
        int iNovos = s.out.indexOf("Novos projetos chegaram");
        int iLegacy = s.out.indexOf("Iniciando projeto: Refatoracao Legacy");

        assertTrue("Os três projetos iniciais devem abrir em ordem", iSite >= 0 && iSite < iBot && iBot < iRel);
        assertEquals("Três projetos concluídos", 3, s.out.split("PROJETO CONCLUIDO", -1).length - 1);
        assertTrue("Novo lote gerado só depois do 3º", iNovos > iRel);
        assertTrue("Primeiro projeto do novo lote aberto", iLegacy > iNovos);
    }

    private static int indiceNoMenuDeEstudo(Developer dev, String skill) {
        List<String> disponiveis = new ArrayList<>();
        for (String sk : SkillCatalog.getAllSkills()) {
            if (!dev.getSkills().contains(sk)) disponiveis.add(sk);
        }
        return disponiveis.indexOf(skill) + 1;
    }
}
