import java.util.ArrayList;
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
    }

    public Vertice getOrigem() {
        return origem;
    }

    public boolean alcancavel(Vertice destino) {
        return distancias.containsKey(destino);
    }

    public int getTempoAte(Vertice destino) {
        if (!alcancavel(destino)) {
            return -1;
        }
        return distancias.get(destino);
    }

    public Set<Vertice> getAlcancaveis() {
        return distancias.keySet();
    }

    public Rota getRotaAte(Vertice destino) {
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
