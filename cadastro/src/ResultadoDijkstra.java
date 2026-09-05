import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ResultadoDijkstra {
    private Vertice origem;
    private HashMap<Vertice, Integer> distancias;      // só contém vértices alcançáveis
    private HashMap<Vertice, Vertice> predecessores;

    public ResultadoDijkstra(Vertice origem, HashMap<Vertice, Integer> distancias, HashMap<Vertice, Vertice> predecessores) {
        this.origem = origem;
        this.distancias = distancias;
        this.predecessores = predecessores;
    }

    public Vertice getOrigem() {
        return origem;
    }

    public boolean alcancavel(Vertice destino) {
        return distancias.containsKey(destino);
    }

    public int getTempoAte(Vertice destino) {
        return alcancavel(destino) ? distancias.get(destino) : -1;
    }

    public Set<Vertice> getAlcancaveis() {
        return distancias.keySet();
    }

    public Rota getRotaAte(Vertice destino) {
        // caso especial: origem == destino -> rota trivial, tempo 0
        if (destino.equals(origem)) {
            ArrayList<Vertice> caminho = new ArrayList<>();
            caminho.add(origem);
            return new Rota(origem, destino, true, 0, caminho);
        }

        if (!alcancavel(destino)) {
            return new Rota(origem, destino, false, -1, new ArrayList<>());
        }

        ArrayList<Vertice> caminho = new ArrayList<>();
        Vertice atual = destino;
        while (atual != null) {
            caminho.add(0, atual);
            if (atual.equals(origem)) break;
            atual = predecessores.get(atual);
        }

        return new Rota(origem, destino, true, distancias.get(destino), caminho);
    }
}