# Testes

Bateria de testes do trabalho. Sem framework e sem biblioteca externa: cada suíte é uma classe
Java com `main`, igual ao resto do projeto.

Os testes **não alteram nada** em `cadastro/src/`. Eles só chamam os métodos públicos das classes
do trabalho, então o código da entrega é exatamente o que está sendo verificado.

Também não existe arquivo de dados aqui. Os testes usam o `cadastro/mapa.csv` do projeto e grafos
pequenos montados em memória dentro do próprio código.

## Como rodar

A partir da raiz do repositório:

```bash
javac -d cadastro/bin cadastro/src/*.java cadastro/testes/*.java
java -cp cadastro/bin RodarTestes
```

O `RodarTestes` executa as quatro suítes em sequência. Para rodar uma só, chame a classe direto
passando o caminho do mapa:

```bash
java -cp cadastro/bin TesteEscolha cadastro/mapa.csv
```

O `Estresse` não precisa de parâmetro nenhum.

## O que cada suíte cobre

`TesteCompleto` verifica `Vertice`, `Aresta` e `Grafo` isoladamente, e confere a integridade do
`mapa.csv`: se toda linha do arquivo virou vértice ou aresta no grafo com os dados certos, se não
há endereço declarado duas vezes e se todos os pesos são positivos, que é a pré-condição do
Dijkstra.

`Teste` cobre o contrato do Dijkstra: os casos de borda (origem igual ao destino, destino fora do
mapa, origem nula), um grafo montado à mão onde a aresta direta é pior que o caminho de dois
saltos, e a otimalidade do resultado sobre o mapa real.

`TesteEscolha` cobre a decisão de qual pizzaria atende: o `-1` sendo descartado antes da
comparação, o prazo nos limites exatos de 30 e 31 minutos, empate resolvido de forma estável, e
uma conferência dos 34 endereços do mapa contra o mínimo calculado à parte.

`Estresse` compara o Dijkstra com um Bellman-Ford de referência, escrito dentro do próprio
arquivo, em 3000 grafos dirigidos aleatórios. É a suíte com maior chance de pegar um erro de
algoritmo, porque o `mapa.csv` tem poucos caminhos alternativos e por isso não estressa a parte
de comparação do algoritmo.

## A marcação ATENCAO

Além de `OK` e `FALHOU`, a saída tem uma terceira marcação. `ATENCAO` é comportamento frágil já
conhecido do código, registrado de propósito para ficar documentado, sem contar como falha.

Por exemplo: `Vertice.hashCode()` estoura `NullPointerException` se o bairro for nulo, e
`Grafo.adicionarVertice` apaga as arestas de um vértice que já estava no grafo. Nenhum dos dois
acontece com o `mapa.csv` atual, então não são bugs ativos, mas são pontos onde o código não se
defende. Se o grupo decidir tratar algum deles, o teste correspondente passa a dar `OK` sozinho.

Resultado esperado hoje: 118 casos passando, nenhuma falha, 6 marcados como atenção, e nenhuma
divergência nos 3000 grafos aleatórios.
