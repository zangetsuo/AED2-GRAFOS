import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.ArrayList;

// menor caminho em tempo (minutos). o grafo é dirigido e os pesos precisam ser positivos.
public class Dijkstra {

    public static ResultadoDijkstra calcular(Grafo grafo, Vertice origem) {
        if (origem == null || !grafo.existeVertice(origem)) {
            throw new IllegalArgumentException("Origem nao existe no grafo: " + origem);
        }

        HashMap<Vertice, Integer> distancias = new HashMap<Vertice, Integer>();
        HashMap<Vertice, Vertice> anteriores = new HashMap<Vertice, Vertice>();

        // a fila reaproveita a Aresta como par (vertice, valor), mas aqui o peso
        // é a distancia acumulada desde a origem, não o tempo de uma rua
        PriorityQueue<Aresta> fila = new PriorityQueue<Aresta>((a, b) -> a.getPeso() - b.getPeso());

        distancias.put(origem, 0);
        fila.add(new Aresta(origem, 0));

        while (!fila.isEmpty()) {
            Aresta atual = fila.poll();
            Vertice v = atual.getDestino();

            if (atual.getPeso() > distancias.get(v)) {
                continue; // entrada velha da fila, já achamos caminho melhor para v
            }

            ArrayList<Aresta> vizinhos = grafo.obterVizinhos(v);
            for (int i = 0; i < vizinhos.size(); i++) {
                Aresta rua = vizinhos.get(i);
                Vertice vizinho = rua.getDestino();
                int novaDistancia = distancias.get(v) + rua.getPeso();

                if (!distancias.containsKey(vizinho) || novaDistancia < distancias.get(vizinho)) {
                    distancias.put(vizinho, novaDistancia);
                    anteriores.put(vizinho, v);
                    fila.add(new Aresta(vizinho, novaDistancia));
                }
            }
        }

        return new ResultadoDijkstra(origem, distancias, anteriores);
    }

    public static Rota menorCaminho(Grafo grafo, Vertice origem, Vertice destino) {
        return calcular(grafo, origem).getRotaAte(destino);
    }
}
