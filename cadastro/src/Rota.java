import java.util.ArrayList;

public class Rota {
    private Vertice origem;
    private Vertice destino;
    private boolean existe;
    private int tempoTotal;
    private ArrayList<Vertice> caminho;

    public Rota(
        Vertice origem,
        Vertice destino,
        boolean existe,
        int tempoTotal,
        ArrayList<Vertice> caminho
    ) {
        this.origem = origem;
        this.destino = destino;
        this.existe = existe;
        this.tempoTotal = tempoTotal;
        this.caminho = caminho;
    }

    public Vertice getOrigem() {
        return origem;
    }

    public Vertice getDestino() {
        return destino;
    }

    public boolean existe() {
        return existe;
    }

    public int getTempoTotal() {
        return tempoTotal;
    }

    public ArrayList<Vertice> getCaminho() {
        return caminho;
    }

    @Override
    public String toString() {
        if (!existe) {
            return "Sem rota entre "
                + origem.getBairro()
                + " e "
                + destino.getBairro();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(tempoTotal).append(" min: ");

        for (int i = 0; i < caminho.size(); i++) {
            Vertice v = caminho.get(i);

            sb.append(v.getRua())
              .append(", ")
              .append(v.getNumero())
              .append(" - ")
              .append(v.getBairro());

            if (i < caminho.size() - 1) {
                sb.append(" -> ");
            }
        }

        return sb.toString();
    }
}