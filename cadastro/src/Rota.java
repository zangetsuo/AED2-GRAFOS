import java.util.ArrayList;

public class Rota {
    private Vertice origem;
    private Vertice destino;
    private int tempoTotal;
    private ArrayList<Vertice> caminho;

    public Rota(Vertice origem, Vertice destino, int tempoTotal, ArrayList<Vertice> caminho) {
        this.origem = origem;
        this.destino = destino;
        this.tempoTotal = tempoTotal;
        this.caminho = caminho;
    }

    // usado quando não existe caminho entre os dois enderecos
    public static Rota inexistente(Vertice origem, Vertice destino) {
        return new Rota(origem, destino, -1, new ArrayList<Vertice>());
    }

    public Vertice getOrigem() {
        return origem;
    }

    public Vertice getDestino() {
        return destino;
    }

    public boolean existe() {
        return tempoTotal >= 0;
    }

    public int getTempoTotal() {
        return tempoTotal;
    }

    public ArrayList<Vertice> getCaminho() {
        return caminho;
    }

    @Override
    public String toString() {
        if (!existe()) {
            return "Sem rota de " + origem + " ate " + destino;
        }
        String texto = tempoTotal + " min: ";
        for (int i = 0; i < caminho.size(); i++) {
            if (i > 0) {
                texto = texto + " -> ";
            }
            texto = texto + caminho.get(i);
        }
        return texto;
    }
}
