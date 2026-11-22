# Interface e desafios — TM01, TM06 e TM07

| ID | Ação | Critério |
|---|---|---|
| TM01 | Abra a ajuda (2), retorne ao menu, inicie (1) e informe Joao. | Meta de 2.000 XP, chance escrita como 30%, Estagiário e 50 XP. O relatório registra 30%%. |
| TM06 | No menu da carreira, anote cargo/XP e digite abc. | Aviso de entrada inválida, mesmo cargo e saldo. |
| TM07 | Anote o saldo antes de um turno de trabalho que acione o desafio de relacionamento e escolha Sim. | Crédito anunciado de 25 XP além do esforço. Na sessão registrada: 115 + 5 + 25 = 145 esperados; painel de 160 XP. |

O script sessao_manual.py reproduz uma partida completa em ambiente POSIX com Python 3 e java no PATH. A partir da raiz, com as classes compiladas, execute: `python3 integracao-junit/sessao_manual.py out transcricao.txt`. A aleatoriedade altera a sequência; se o desafio não surgir, registre TM07 como não observado nessa partida.
