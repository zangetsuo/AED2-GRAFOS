import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {

System.out.println("Diretório de trabalho atual: " + System.getProperty("user.dir"));        System.out.println("=== Teste manual do Grafo ===");

        Grafo grafo = new Grafo();

        Vertice va = new Vertice("heliopolis", "rua das flores", 20);
        Vertice vb = new Vertice("heliopolis", "rua das flores", 30);
        Vertice vc = new Vertice("heliopolis", "rua das flores", 60);

        grafo.adicionarVertice(va);
        grafo.adicionarVertice(vb);
        grafo.adicionarVertice(vc);
        grafo.adicionarAresta(va, vb, 5);

        ArrayList<Aresta> vizinhosDeVa = grafo.obterVizinhos(va);

        System.out.println("Quantidade de vizinhos de va: " + vizinhosDeVa.size());
        System.out.println("Peso da conexão va -> vb: " + vizinhosDeVa.get(0).getPeso());
        System.out.println("va existe no grafo? " + grafo.existeVertice(va));
        System.out.println("vc existe no grafo? " + grafo.existeVertice(vc));

        System.out.println("\n=== Teste de carregamento do mapa.csv ===");

        Grafo grafoDoMapa = new Grafo();
        LeitorMapa leitor = new LeitorMapa();
        leitor.carregarMapa("../mapa.csv", grafoDoMapa);

        Vertice enderecoTeste = new Vertice("heliopolis", "rua cleto campelo", 10);
        System.out.println("Endereco existe no grafo carregado do mapa? " + grafoDoMapa.existeVertice(enderecoTeste));

        ArrayList<Aresta> vizinhosDoEndereco = grafoDoMapa.obterVizinhos(enderecoTeste);
        System.out.println("Quantidade de vizinhos desse endereco: " + vizinhosDoEndereco.size());
        System.out.println("Diretório de trabalho atual: " + System.getProperty("user.dir"));
    }
}