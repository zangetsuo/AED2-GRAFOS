public class Aresta {
    private Vertice destino;
    private int peso;


public Aresta(Vertice destino, int peso){
        this.destino = destino;
        this.peso = peso;
}
public Vertice getDestino() {
        return destino;
    }

    public void setDestino(Vertice destino) {
        this.destino = destino;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
}
