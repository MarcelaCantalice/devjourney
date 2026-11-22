import os, re, select, subprocess, sys, time

cp, destino = sys.argv[1], sys.argv[2]
proc = subprocess.Popen(["java", "-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8",
                         "-Dstderr.encoding=UTF-8", "-cp", cp, "game.Main"],
                        stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.STDOUT,
                        env={k: v for k, v in os.environ.items()
                             if k not in ("JAVA_TOOL_OPTIONS", "_JAVA_OPTIONS")})
log = []
t0 = time.time()

def ler(quieto=0.6, limite=8.0):
    buf, ultimo, ini = b"", time.time(), time.time()
    while time.time() - ini < limite:
        r, _, _ = select.select([proc.stdout], [], [], 0.05)
        if r:
            chunk = os.read(proc.stdout.fileno(), 65536)
            if not chunk:
                break
            buf += chunk
            ultimo = time.time()
        elif buf and time.time() - ultimo > quieto:
            break
        elif proc.poll() is not None and not r:
            break
    texto = buf.decode("utf-8", "replace")
    log.append(texto)
    return texto

def digitar(s, rotulo=None):
    if rotulo:
        log.append(f"\n[{time.time()-t0:6.1f}s] ===== {rotulo} =====\n")
    log.append(f"» {s}\n")
    proc.stdin.write((s + "\n").encode())
    proc.stdin.flush()
    return responder_desafios(ler())

def responder_desafios(texto):

    total = texto
    for _ in range(3):
        m = re.search(r"\*\* (\d+) \+ (\d+) \*\*", total.split("DESAFIO SURPRESA")[-1]) if "DESAFIO SURPRESA" in total else None
        if "DESAFIO SURPRESA" not in texto:
            break
        if "Digite sua senha" in texto:
            resp = "1234"
        elif m:
            resp = str(int(m.group(1)) + int(m.group(2)))
        elif "livre para sair" in texto:
            resp = "1"
        else:
            break
        log.append(f"» {resp}   (resposta ao desafio surpresa)\n")
        proc.stdin.write((resp + "\n").encode()); proc.stdin.flush()
        texto = ler()
        total += texto
    return total

ler()
digitar("2", "TM01 · Ajuda")
digitar("1", "TM01 · Iniciar jogo")
digitar("Joao", "TM01 · Nome do jogador")
digitar("3", "TM02 · Promover com 50 XP")
digitar("1", "TM03 · Trabalhar (abre projeto)")
digitar("1", "TM03 · Trabalhar (10%)")
for i in range(1, 5):
    digitar("sudo", f"TM04 · Bônus sudo {i}/4")
digitar("3", "TM04 · Promover")
digitar("abc", "TM06 · Entrada não numérica no menu")

for i in range(1, 81):
    saida = digitar("1", f"TM07 · Trabalhar até surgir o desafio da namorada (turno {i})")
    if "livre para sair" in saida:
        log.append("\n[TM07] Desafio da namorada apareceu neste turno.\n")
        break
digitar("4", "TM05 · Sair")
try:
    codigo = proc.wait(timeout=10)
except subprocess.TimeoutExpired:
    proc.kill(); codigo = "TIMEOUT"
log.append(f"\n[{time.time()-t0:6.1f}s] Processo encerrado. Código de saída: {codigo}\n")

with open(destino, "w", encoding="utf-8") as f:
    f.write("".join(log))
print("".join(log))
