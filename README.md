# Dev Journey

Este projeto é um jogo de simulação de carreira em console (CLI) desenvolvido em Java, onde o jogador avança de Estagiário a CEO, gerenciando projetos e habilidades.

## Informações de Entrega

**Membros da Equipe:**
- Ian de Sousa Pereira, 00000850687

## Passos para Execução

1.  **Clone o Repositório:**
    ```bash
    git clone https://github.com/ianzdes/devjourney.git
    ```
2.  **Compile o Código:** (A partir do diretório raiz do projeto)
    ```bash
    # Compila todos os arquivos em seus respectivos pacotes
    javac game/*.java game/challenges/*.java game/projects/*.java game/service/*.java game/exceptions/*.java
    ```
3.  **Execute o Jogo:**
    ```bash
    java game.Main
    ```
    *O jogo iniciará solicitando o nome do Dev.*


## Funcionamento do Jogo

O jogo é controlado pelo menu principal e opera em um sistema de turnos.

### 1. Progressão de Carreira

| Cargo | Requisito (XP) | Observações |
| :--- | :--- | :--- |
| **Estagiário** | 0 | Início do jogo (50 XP inicial). |
| **Júnior** | 2000 | |
| **Pleno** | 4000 | |
| **Sênior** | 8000 | |
| **CEO** | 15000 | Meta Final. |

* **Promoção:** A opção **"Tentar Promoção"** verifica se o XP acumulado atinge a meta do próximo nível. Em caso de sucesso, o XP é **zerado**.

### 2. Sistema de Projetos por Turnos

O trabalho em projetos é incremental:

* **Progresso:** Cada turno de trabalho aumenta o progresso do projeto ativo em **10%** e concede **5 XP** de esforço.
* **Conclusão:** A recompensa total de XP é concedida somente ao atingir 100%.
* **Reposição:** Novos lotes de projetos são gerados automaticamente quando a lista disponível se esgota.

### 3. Mecânica de Alto Risco e Recompensa

O **`GirlfriendChallenge`** introduz uma mecânica de *boost* temporário:

| Escolha | Efeito Imediato | Recompensa de Longo Prazo |
| :--- | :--- | :--- |
| **4. Terminar** | Perda de **200 XP**. | Ativa **Boost de XP de 4.0x** (300% de aumento) para os **próximos 5 projetos**. |


---

# Testes e Qualidade de Software — DevJourney

Esta seção apresenta os procedimentos para executar os testes unitários, de integração e de regressão realizados pela equipe no projeto DevJourney.

Os testes foram desenvolvidos para avaliar o funcionamento do jogo, identificar possíveis defeitos e verificar diferenças entre versões do código.

## 1. Pré-requisitos

Para executar os testes, são necessários:

- Java JDK 21
- Git
- PowerShell
- JUnit 5 para os testes unitários e de regressão
- JUnit 4 para os testes de integração
- JaCoCo para a análise de cobertura

Confira a instalação do Java:

```powershell
java -version
javac -version
```

Os procedimentos utilizam compilação com `javac`, sem exigir Maven.

## 2. Testes unitários — JUnit 5

Os testes unitários verificam individualmente funcionalidades relacionadas a:

- Ganho e perda de XP.
- Progressão de carreira.
- Habilidades e bônus.
- Progresso e características dos projetos.
- Promoção entre cargos.

### Preparação

Na raiz do projeto que contém as pastas `test/` e `qa/lib/`, confirme que o arquivo `junit-platform-console-standalone-1.11.4.jar` está em `qa/lib/`.

### Execução

Execute os seguintes comandos no PowerShell:

```powershell
$jar = "qa\lib\junit-platform-console-standalone-1.11.4.jar"

New-Item -ItemType Directory -Force out | Out-Null

$fontes = @(Get-ChildItem -Path Career.java, Developer.java, Main.java, challenges, exceptions, projects, service, skills -Filter *.java -Recurse -File).FullName

$testes = @(Get-ChildItem test -Recurse -Filter "*.java").FullName

javac --release 21 -encoding UTF-8 -cp $jar -d out $fontes $testes

java -jar $jar execute --class-path out --scan-class-path --details tree
```

### Resultados registrados

| Resultado | Quantidade |
|---|---:|
| Testes executados | 36 |
| Aprovados | 34 |
| Reprovados | 2 |

As duas reprovações ocorreram no conjunto UT08, envolvendo valores de progresso fora do intervalo de 0% a 100%.

Esses resultados foram mantidos para documentar os comportamentos identificados.

## 3. Testes de integração — JUnit 4

Os testes de integração verificam a interação entre diferentes componentes do jogo.

Foram avaliados fluxos envolvendo:

- Promoção e progressão de carreira.
- Desenvolvimento e conclusão de projetos.
- Estudo e aplicação de bônus.
- Desafios e alterações de XP.
- Interface de interação com o jogador.

### Preparação

Execute os comandos na raiz do pacote de integração `devjourney-testes`, que contém as pastas:

```text
src/
  main/
    java/
  test/
    java/
lib/
```

As bibliotecas necessárias são:

- `junit-4.13.2.jar`
- `hamcrest-core-1.3.jar`

Ambas devem estar na pasta `lib/`.

### Execução

```powershell
New-Item -ItemType Directory -Path "target\classes", "target\test-classes" -Force | Out-Null

$fonte = @(Get-ChildItem "src\main\java" -Recurse -Filter "*.java" -File).FullName

javac -encoding UTF-8 -d "target\classes" $fonte

$cp = "target\classes;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar"

$testes = @(Get-ChildItem "src\test\java" -Recurse -Filter "*.java" -File).FullName

javac -encoding UTF-8 -cp $cp -d "target\test-classes" $testes

$cp = "target\classes;target\test-classes;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar"

java "-Duser.language=en" "-Duser.country=US" -cp $cp org.junit.runner.JUnitCore `
  game.integracao.PromocaoIT `
  game.integracao.ProjetoEstudoIT `
  game.integracao.DesafiosIT `
  game.integracao.InterfaceIT
```

A configuração regional `en-US` evita diferenças na representação de números decimais durante as comparações textuais realizadas pelos testes.

### Resultados registrados

| Resultado | Quantidade |
|---|---:|
| Testes executados | 15 |
| Aprovados | 11 |
| Reprovados | 4 |

As falhas identificadas foram:

- **TI09:** diferença entre o XP anunciado e o aplicado na opção "Sim".
- **TI10:** diferença entre o XP anunciado e o aplicado na opção "Não".
- **TI11:** diferença entre o XP anunciado e o aplicado na opção "Amanhã".
- **TI15:** apresentação incorreta de `30%%` na mensagem de ajuda.

As falhas foram registradas sem modificar o código de produção para forçar aprovações.

## 4. Testes de regressão — JUnit 5

Os testes de regressão comparam o comportamento do DevJourney em três commits históricos do repositório original.

### Versões analisadas

| Versão | Commit | Data |
|---|---|---|
| Referência | `4bb1938` | 28/11/2025 |
| Intermediária | `5ae8124` | 25/11/2025 |
| Antiga | `fd5c610` | 20/11/2025 |

### Preparação dos commits

Execute:

```powershell
git clone https://github.com/ianzdes/devjourney.git devjourney

cd devjourney

git worktree add --detach ../devjourney-atual 4bb1938

git worktree add --detach ../devjourney-antigo1 5ae8124

git worktree add --detach ../devjourney-antigo2 fd5c610
```

Disponibilize a pasta `regressao-junit/` no clone principal, contendo o script `executar-junit.ps1`, os testes e os adaptadores de versão.

Coloque também o arquivo `junit-platform-console-standalone-1.12.2.jar` em `regressao-junit/libs/`.

Download da biblioteca:

https://repo.maven.apache.org/maven2/org/junit/platform/junit-platform-console-standalone/1.12.2/junit-platform-console-standalone-1.12.2.jar

### Execução

Na raiz de `devjourney/`, execute:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\regressao-junit\executar-junit.ps1
```

O script executa os casos automatizados em cada versão e gera registros de execução em arquivos de log e relatórios XML.

Consulte também o README específico da pasta `regressao-junit/` para conferir sua organização de diretórios.

### Resultados registrados

| Versão | Executados | Aprovados | Reprovados | Não aplicáveis |
|---|---:|---:|---:|---:|
| Referência | 10 | 7 | 3 | 0 |
| Intermediária | 10 | 7 | 3 | 0 |
| Antiga | 10 | 6 | 0 | 4 |

As três falhas presentes nas versões de referência e intermediária estão relacionadas às diferenças entre o XP anunciado e o aplicado no desafio de relacionamento.

Na versão antiga, quatro testes foram classificados como não aplicáveis porque as funcionalidades correspondentes ainda não existiam.

## 5. Cobertura de código — JaCoCo

A equipe utilizou o JaCoCo para medir a cobertura obtida pelas diferentes suítes de testes.

| Modalidade | Cobertura de linhas | Cobertura de ramificações |
|---|---:|---:|
| Testes unitários | 23,37% | 13,64% |
| Testes de integração | 90,12% | 76,14% |
| Testes de regressão | 27,25% | 13,41% |

**Observação:** a cobertura da regressão foi calculada sobre um conjunto de 16 classes, sem `Main.java`. Por esse motivo, seus percentuais não devem ser comparados diretamente aos resultados das outras modalidades, que utilizaram 17 classes.

## 6. Evidências e documentação

O relatório final apresenta:

- Casos de teste e resultados esperados e obtidos.
- Resultados de execução do JUnit.
- Evidências dos testes manuais.
- Capturas de tela.
- Relatórios de cobertura JaCoCo.
- Registro dos defeitos e incidentes.
- Comparação de regressão entre os três commits.

Os resultados apresentados correspondem às execuções documentadas pela equipe.

Alguns testes possuem falhas conhecidas e documentadas. Portanto, a ocorrência dessas reprovações não significa necessariamente que houve um problema de instalação ou execução dos testes.

## 7. Observações importantes

- Execute cada suíte na pasta correspondente.
- Confira se as bibliotecas necessárias estão disponíveis.
- Utilize as versões de código indicadas para reproduzir os resultados históricos.
- Mantenha as falhas documentadas.
- Consulte o relatório final para verificar os procedimentos, resultados e evidências detalhadas.

**Projeto original:** https://github.com/ianzdes/devjourney

**Fork utilizado para os testes:** https://github.com/MarcelaCantalice/devjourney/tree/testes-qualidade-software


### 4. Desafios e Comandos

* **Desafios (`Challenges`):** Há **30% de chance** de um desafio surpresa (`HRChecking` ou `GirlfriendChallenge`) ser acionado após cada turno do menu, resultando em ganhos ou perdas de XP.
* **Easter Egg:** Digitar **`sudo`** no menu de opções concede um grande bônus imediato de XP.

---
