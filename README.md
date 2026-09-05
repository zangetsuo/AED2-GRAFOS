# AED2-GRAFOS

Trabalho de Algoritmos e Estruturas de Dados 2.

Uma pizzaria tem duas unidades e precisa decidir de qual delas sai cada entrega. O programa
representa a cidade como um grafo, calcula o menor tempo de cada unidade até o endereço do
cliente e escolhe a mais rápida entre as que conseguem chegar lá.

## O problema

Cada endereço da cidade é um vértice, cada ligação entre dois endereços é uma aresta, e o peso
da aresta é o tempo em minutos daquele trecho. O grafo é dirigido, então nem todo caminho tem
volta.

Dado um cliente, o programa responde três coisas: de qual unidade sai a entrega, qual o trajeto,
e se o tempo cabe no prazo de 30 minutos. Quando nenhuma unidade tem caminho até o cliente, a
resposta é que o endereço não é atendido.

O prazo não faz parte do cálculo de menor caminho. O algoritmo devolve minutos e trajeto, e a
regra dos 30 minutos é aplicada depois, na hora de decidir.

## Como rodar

A partir da raiz do repositório:

```bash
javac -d cadastro/bin cadastro/src/*.java
cd cadastro/src && java -cp ../bin App
```

O `cd` é necessário: o `App` abre o mapa pelo caminho relativo `../mapa.csv`, então precisa ser
executado de dentro de `cadastro/src`.

## Testes

```bash
javac -d cadastro/bin cadastro/src/*.java cadastro/testes/*.java
java -cp cadastro/bin RodarTestes
```

São 118 casos, sem framework e sem biblioteca externa. Detalhes do que cada suíte cobre estão em
`cadastro/testes/README.md`.

## Estrutura

O código está em `cadastro/src`, dividido em três camadas independentes. O leitor não sabe o que
é pizzaria, o algoritmo de menor caminho não sabe o que é prazo, e a decisão não sabe como o
caminho foi calculado.

| camada | classes | responsabilidade |
| --- | --- | --- |
| dados da cidade | `Vertice`, `Aresta`, `Grafo`, `LeitorMapa` | representar o mapa e carregá-lo do arquivo |
| menor caminho | `Dijkstra`, `ResultadoDijkstra`, `Rota` | calcular tempo e trajeto entre dois endereços |
| decisão | `EscolhaPizzaria`, `Escolha` | comparar as unidades e aplicar o prazo |
| demonstração | `App` | exemplo de uso das três camadas |

O grafo usa lista de adjacência, um `HashMap` que leva de cada vértice para a lista de arestas
que saem dele. O `Vertice` sobrescreve `equals` e `hashCode` para que dois objetos com o mesmo
bairro, rua e número contem como o mesmo endereço, o que é o que permite usá-lo como chave.

Quando não existe caminho entre dois endereços, o tempo devolvido é `-1`. Esse valor precisa ser
descartado antes de qualquer comparação: solto numa comparação direta, `-1` passa como o menor
tempo e a unidade que não consegue entregar ganharia a escolha.

## O mapa

Os dados ficam em `cadastro/mapa.csv`, com endereços reais de Garanhuns nos bairros Heliópolis,
Magano, Centro, Francisco Figueira e Dom Helder Câmara. São 34 endereços e 39 ligações.

Cada linha começa com `V` de vértice ou `A` de aresta:

```
V;bairro;rua;numero
A;bairro origem;rua origem;numero origem;bairro destino;rua destino;numero destino;peso
```

Dentro de uma mesma rua as ligações existem só no sentido crescente da numeração. Por causa
disso 9 dos 34 endereços não são alcançáveis por nenhuma das duas unidades.

Trocar a cidade é trocar esse arquivo. O código continua igual.

## Integrantes

Arthur Lima da Rocha

Diego Rodrigues Figueiredo

Emanuel de Lima Paz
