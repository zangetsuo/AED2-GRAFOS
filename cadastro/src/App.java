import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {

        System.out.println("projeto da pizzaria iniciado");

        Vertice v1 = new Vertice("heliopolis", "rua das flores", 10);
        Aresta a1 = new Aresta(v1, 5);
        Grafo grafo = new Grafo();

        Vertice va= new Vertice("heliopolis", "rua das flores", 20);
        Vertice vb= new Vertice("heliopolis", "rua das flores", 30);

        grafo.adicionarVertice(va);
        grafo.adicionarVertice(vb);
        grafo.adicionarAresta(va, vb,5);

        ArrayList<Aresta> vizinhosDeVa = grafo.obterVizinhos(va);

        System.out.println("Vértices adicionados!");
        System.out.println("Bairro: " + v1.getBairro());
        System.out.println("Rua: " + v1.getRua());
        System.out.println("Número: " + v1.getNumero());
        System.out.println("Peso: " + a1.getPeso());
        System.out.println("Destino: " + a1.getDestino().getRua());
        System.out.println("Grafo criado com sucesso");
        System.out.println("Quantidade de vizinhos de va: " + vizinhosDeVa.size());        
        System.out.println("peso da conexão :" + vizinhosDeVa.get(0).getPeso());
    }
}
