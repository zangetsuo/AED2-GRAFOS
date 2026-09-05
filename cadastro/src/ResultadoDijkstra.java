import java.util.ArrayList;
<<<<<<< HEAD
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
=======
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

// guarda o resultado de uma execucao do Dijkstra a partir de uma origem
public class ResultadoDijkstra {
    private Vertice origem;
    private HashMap<Vertice, Integer> distancias;
    private HashMap<Vertice, Vertice> anteriores;

    public ResultadoDijkstra(Vertice origem, HashMap<Vertice, Integer> distancias, HashMap<Vertice, Vertice> anteriores) {
        this.origem = origem;
        this.distancias = distancias;
        this.anteriores = anteriores;
>>>>>>> e35181034a5df6287485b2b6d4a8315642dc850a
    }

    public Vertice getOrigem() {
        return origem;
    }

    public boolean alcancavel(Vertice destino) {
        return distancias.containsKey(destino);
    }

    public int getTempoAte(Vertice destino) {
<<<<<<< HEAD
        return alcancavel(destino) ? distancias.get(destino) : -1;
=======
        if (!alcancavel(destino)) {
            return -1;
        }
        return distancias.get(destino);
>>>>>>> e35181034a5df6287485b2b6d4a8315642dc850a
    }

    public Set<Vertice> getAlcancaveis() {
        return distancias.keySet();
    }

    public Rota getRotaAte(Vertice destino) {
<<<<<<< HEAD
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
=======
        if (!alcancavel(destino)) {
            return Rota.inexistente(origem, destino);
        }
        // percorre os anteriores do destino ate a origem e inverte no final
        ArrayList<Vertice> caminho = new ArrayList<Vertice>();
        Vertice atual = destino;
        while (atual != null) {
            caminho.add(atual);
            atual = anteriores.get(atual);
        }
        Collections.reverse(caminho);
        return new Rota(origem, destino, distancias.get(destino), caminho);
    }
}
>>>>>>> e35181034a5df6287485b2b6d4a8315642dc850a
