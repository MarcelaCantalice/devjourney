# Resultados dos testes de regressão — DevJourney

## Resumo

Criamos uma suíte com dez testes automatizados em JUnit 5 para comparar três versões do jogo DevJourney.

| Versão | Commit | Aprovados | Falhas | Não aplicáveis |
|---|---|---:|---:|---:|
| 28/11/2025 | `4bb1938` | 7 | 3 | 0 |
| 25/11/2025 | `5ae8124` | 7 | 3 | 0 |
| 20/11/2025 | `fd5c610` | 6 | 0 | 4 |

Os testes foram executados em 21/11/2026, utilizando Java 21, JUnit 5 e PowerShell. Os logs e relatórios XML estão na pasta `publicaveis/`.

## Diferenças entre as versões

Alguns testes utilizam valores esperados diferentes para cada versão, para registrar como o jogo funcionava em cada uma delas.

| Comportamento | Versão antiga | Versões recentes |
|---|---:|---:|
| XP para promoção a Júnior | 200 | 2000 |
| Data Science (dificuldade 2) | 94 XP | 35 XP |
| Smart Contract (dificuldade 2) | 120 XP | 30 XP |

Por isso, um teste aprovado não significa necessariamente que não houve mudanças entre as versões.

## Problemas encontrados

Nas duas versões mais recentes, os três testes de REG-09 falharam porque o XP anunciado no desafio era diferente do XP realmente aplicado:

| Opção | XP anunciado | XP aplicado |
|---|---:|---:|
| Sim | +25 | +40 |
| Não | -60 | -20 |
| Amanhã | -30 | 0 |

Na versão antiga, quatro testes foram considerados não aplicáveis porque o boost temporário e o desafio ainda não existiam.

## Limitações

REG-06 e REG-08 ficaram fora do escopo automatizado. Os testes cobrem apenas alguns comportamentos do jogo, e os resultados não permitem determinar se o problema de REG-09 está nas mensagens ou nas regras de XP.