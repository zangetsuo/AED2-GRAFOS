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
        System.out.println("\n=== Teste do Dijkstra ===");

        Vertice pizzariaHeliopolis = new Vertice("heliopolis", "praca da pizzaria heliopolis", 1);
        Vertice pizzariaCentro = new Vertice("centro", "praca da pizzaria centro", 1);

        Vertice cliente = new Vertice("magano", "rua conde da boa vista", 30);
        Rota rota = Dijkstra.menorCaminho(grafoDoMapa, pizzariaHeliopolis, cliente);
        System.out.println("Heliopolis -> " + cliente);
        System.out.println("  " + rota);

        Vertice clienteSemRota = new Vertice("heliopolis", "rua maria candido da silva", 40);
        Rota semRota = Dijkstra.menorCaminho(grafoDoMapa, pizzariaCentro, clienteSemRota);
        System.out.println("Centro -> " + clienteSemRota);
        System.out.println("  existe? " + semRota.existe() + " | tempo: " + semRota.getTempoTotal() + " | caminho: " + semRota.getCaminho().size());

        // uma execucao por pizzaria serve para todos os clientes
        ResultadoDijkstra deHeliopolis = Dijkstra.calcular(grafoDoMapa, pizzariaHeliopolis);
        ResultadoDijkstra deCentro = Dijkstra.calcular(grafoDoMapa, pizzariaCentro);

        Vertice clienteComparado = new Vertice("centro", "rua do comercio", 30);
        System.out.println("Comparacao para " + clienteComparado);
        System.out.println("  heliopolis: " + deHeliopolis.getTempoAte(clienteComparado) + " min");
        System.out.println("  centro: " + deCentro.getTempoAte(clienteComparado) + " min");

        System.out.println("Enderecos alcancaveis a partir de heliopolis: " + deHeliopolis.getAlcancaveis().size());
        System.out.println("Enderecos alcancaveis a partir do centro: " + deCentro.getAlcancaveis().size());
    }
}