# Testes de regressão com JUnit 5

Nesta pasta estão os testes automatizados que fizemos para comparar três versões do jogo DevJourney, utilizando Java e JUnit 5.

## O que é necessário

Para executar os testes, é preciso ter:

- Java 21 (JDK)
- Git
- Windows PowerShell
- JUnit Platform Console Standalone 1.12.2, disponível no [Maven Central](https://repo.maven.apache.org/maven2/org/junit/platform/junit-platform-console-standalone/1.12.2/junit-platform-console-standalone-1.12.2.jar)

O arquivo JAR do JUnit deve ser colocado na pasta `regressao-junit/libs/`.

Também é necessário ter três cópias do projeto, na mesma pasta onde está o `devjourney`:

| Pasta | Commit |
|---|---|
| `devjourney-atual` | `4bb1938` |
| `devjourney-antigo1` | `5ae8124` |
| `devjourney-antigo2` | `fd5c610` |

Cada cópia deve estar no commit indicado, sem alterações nos arquivos, para que os resultados possam ser reproduzidos.

## Como executar

Abra o terminal na pasta principal do projeto e execute:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\regressao-junit\executar-junit.ps1
```

O script executa os testes nas três versões e cria pastas `resultados-junit-*`, contendo os logs e relatórios XML.

Mesmo que algum teste falhe, o script continua executando as outras versões. No final, retorna código 1 caso alguma execução tenha falhas e 0 caso todas sejam bem-sucedidas. Problemas de preparação ou compilação interrompem a execução.

## Resultados

Os testes podem ser classificados como:

- **Aprovado:** o resultado foi o esperado.
- **Falha:** o resultado foi diferente do esperado.
- **Não aplicável:** a funcionalidade ainda não existia naquela versão.

Alguns testes usam valores esperados diferentes para cada versão, justamente para registrar como o jogo funcionava em cada uma delas. Por isso, um teste aprovado não significa necessariamente que o comportamento não mudou.

Os resultados da comparação estão em `evidencias/regressao/resumo-regressao.md`. Os logs e XMLs usados como evidência estão em `evidencias/regressao/publicaveis/`.

Os arquivos gerados durante a execução e o JAR do JUnit não são enviados para o GitHub.