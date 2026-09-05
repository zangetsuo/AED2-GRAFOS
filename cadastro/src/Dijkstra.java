import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Dijkstra {

    public static ResultadoDijkstra calcular(Grafo grafo, Vertice origem) {
        if (origem == null || !grafo.existeVertice(origem)) {
            throw new IllegalArgumentException(
                "Vértice de origem inválido ou fora do grafo"
            );
        }

        HashMap<Vertice, Integer> distancias = new HashMap<>();
        HashMap<Vertice, Vertice> predecessores = new HashMap<>();
        HashSet<Vertice> visitados = new HashSet<>();

        PriorityQueue<Vertice> fila = new PriorityQueue<>(
            Comparator.comparingInt(
                v -> distancias.getOrDefault(v, Integer.MAX_VALUE)
            )
        );

        distancias.put(origem, 0);
        fila.add(origem);

        while (!fila.isEmpty()) {
            Vertice atual = fila.poll();

            if (visitados.contains(atual)) {
                continue;
            }

            visitados.add(atual);

            ArrayList<Aresta> vizinhos = grafo.obterVizinhos(atual);

            if (vizinhos == null) {
                continue;
            }

            for (Aresta aresta : vizinhos) {
                Vertice vizinho = aresta.getDestino();

                int novaDistancia =
                    distancias.get(atual) + aresta.getPeso();

                if (novaDistancia <
                    distancias.getOrDefault(vizinho, Integer.MAX_VALUE)) {

                    distancias.put(vizinho, novaDistancia);
                    predecessores.put(vizinho, atual);
                    fila.add(vizinho);
                }
            }
        }

        return new ResultadoDijkstra(
            origem,
            distancias,
            predecessores
        );
    }

    public static Rota menorCaminho(
        Grafo grafo,
        Vertice origem,
        Vertice destino
    ) {
        return calcular(grafo, origem).getRotaAte(destino);
    }
}