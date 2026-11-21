# Executa os cenarios JUnit em tres commits, sem alterar os worktrees.
# Requer Java 21 e um JAR JUnit Platform Console Standalone 1.12.2 em libs/.
$ErrorActionPreference = 'Stop'
$base = $PSScriptRoot
$pastaProjetos = Split-Path -Parent (Split-Path -Parent $base)
$junit = Join-Path $base 'libs/junit-platform-console-standalone-1.12.2.jar'
if (-not (Test-Path $junit)) { throw "JUnit nao encontrado em $junit. Consulte README.md." }

$execucao = Join-Path $base ("resultados-junit-" + (Get-Date -Format 'yyyyMMdd-HHmmss') + '-' + [guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $execucao | Out-Null

$versoes = @(
    @{ Nome='atual'; Pasta='devjourney-atual'; SHA='4bb193868b2f45e59e67af1f65acc2a4b4e6f3aa'; Legado=$false; Adaptador='CurrentAdapter.java' },
    @{ Nome='antigo1'; Pasta='devjourney-antigo1'; SHA='5ae8124eff152532c5329150d84a6a55c1908e58'; Legado=$false; Adaptador='CurrentAdapter.java' },
    @{ Nome='antigo2'; Pasta='devjourney-antigo2'; SHA='fd5c610eb77994ab922163a036b704073927aa27'; Legado=$true; Adaptador='LegacyAdapter.java' }
)

$houveFalha = $false
foreach ($v in $versoes) {
    $origem = Join-Path $pastaProjetos $v.Pasta
    if (-not (Test-Path $origem)) { throw "Versao nao encontrada: $origem" }
    $head = (& git -C $origem rev-parse HEAD).Trim()
    if ($LASTEXITCODE -ne 0 -or $head -ne $v.SHA) { throw "SHA incorreto para $($v.Nome): $head" }
    $classes = Join-Path $execucao ($v.Nome + '-classes')
    $relatorio = Join-Path $execucao ($v.Nome + '-relatorio')
    New-Item -ItemType Directory -Path $classes | Out-Null
    New-Item -ItemType Directory -Path $relatorio | Out-Null

    $fontes = @(Get-ChildItem -Path $origem -Recurse -File -Filter '*.java' |
        Where-Object { $_.Name -ne 'Main.java' } |
        ForEach-Object { $_.FullName })
    $fontes += (Join-Path $base 'VersionAdapter.java')
    $fontes += (Join-Path $base $v.Adaptador)
    $fontes += (Join-Path $base 'RegressaoBasicaTest.java')

    & javac -encoding UTF-8 -cp $junit -d $classes $fontes
    if ($LASTEXITCODE -ne 0) { throw "Erro de compilacao: $($v.Nome)" }

    $propriedade = if ($v.Legado) { '-Dregressao.legacy=true' } else { '-Dregressao.legacy=false' }
    $log = Join-Path $execucao ($v.Nome + '.log')
    & java $propriedade -jar $junit execute --class-path $classes --select-class RegressaoBasicaTest --reports-dir $relatorio --details tree 2>&1 |
        Out-File -LiteralPath $log -Encoding utf8
    $codigo = $LASTEXITCODE
    if ($codigo -ne 0) { $houveFalha = $true }
    Write-Host "$($v.Nome): codigo de saida $codigo; log $log"
}
Write-Host "Relatorios JUnit em: $execucao"
if ($houveFalha) {
    Write-Host 'Uma ou mais versoes apresentaram falhas na execucao JUnit.'
    exit 1
}
exit 0
