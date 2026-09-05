import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {

        System.out.println("Diretório de trabalho atual: " + System.getProperty("user.dir"));
        System.out.println("=== Teste manual do Grafo ===");

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
        System.out.println("Endereco existe no grafo carregado do mapa? "
                + grafoDoMapa.existeVertice(enderecoTeste));

        ArrayList<Aresta> vizinhosDoEndereco = grafoDoMapa.obterVizinhos(enderecoTeste);
        System.out.println("Quantidade de vizinhos desse endereco: "
                + vizinhosDoEndereco.size());

        System.out.println("\n=== Teste do Dijkstra ===");

        Vertice pizzariaHeliopolis = new Vertice(
                "heliopolis",
                "praca da pizzaria heliopolis",
                1
        );

        Vertice pizzariaCentro = new Vertice(
                "centro",
                "praca da pizzaria centro",
                1
        );

        // Cliente usado no teste da sua versão
        Vertice cliente = enderecoTeste;

        System.out.println("Pizzaria Heliópolis existe no grafo? "
                + grafoDoMapa.existeVertice(pizzariaHeliopolis));

        System.out.println("Pizzaria Centro existe no grafo? "
                + grafoDoMapa.existeVertice(pizzariaCentro));

        // Calcula Dijkstra uma vez para cada pizzaria
        ResultadoDijkstra deHeliopolis =
                Dijkstra.calcular(grafoDoMapa, pizzariaHeliopolis);

        ResultadoDijkstra deCentro =
                Dijkstra.calcular(grafoDoMapa, pizzariaCentro);

        int tempoHeliopolis = deHeliopolis.getTempoAte(cliente);
        int tempoCentro = deCentro.getTempoAte(cliente);

        System.out.println("Tempo de Heliópolis até o cliente: "
                + tempoHeliopolis + " min");

        System.out.println("Tempo do Centro até o cliente: "
                + tempoCentro + " min");

        System.out.println("\n=== Escolha da pizzaria ===");

        // registra as duas unidades: o dijkstra roda uma vez para cada uma
        // e serve para todos os clientes consultados depois
        EscolhaPizzaria escolha = new EscolhaPizzaria(grafoDoMapa);
        escolha.adicionarUnidade(pizzariaHeliopolis);
        escolha.adicionarUnidade(pizzariaCentro);

        Vertice[] clientes = {
                cliente,
                new Vertice("centro", "rua do comercio", 30),
                new Vertice("magano", "rua conde da boa vista", 30),
                new Vertice("domheldercamara", "rua abdenago revoredo", 10),
                new Vertice("heliopolis", "rua maria candido da silva", 40)
        };

        for (int i = 0; i < clientes.length; i++) {
            Escolha decisao = escolha.escolher(clientes[i]);
            System.out.println(decisao);
            if (decisao.foiAtendido()) {
                System.out.println("    " + decisao.getRota());
            }
        }

        System.out.println("\n=== Teste com endereços mais distantes ===");

        Vertice clienteFranciscoFigueira = new Vertice(
                "franciscofigueira",
                "rua conceicao cardoso",
                10
        );

        Vertice clienteDomHelderCamara = new Vertice(
                "domheldercamara",
                "rua abdenago revoredo",
                10
        );

        System.out.println("Tempo Heliópolis -> Francisco Figueira: "
                + deHeliopolis.getTempoAte(clienteFranciscoFigueira));

        System.out.println("Tempo Centro -> Francisco Figueira: "
                + deCentro.getTempoAte(clienteFranciscoFigueira));

        System.out.println("Tempo Heliópolis -> Dom Helder Câmara: "
                + deHeliopolis.getTempoAte(clienteDomHelderCamara));

        System.out.println("Tempo Centro -> Dom Helder Câmara: "
                + deCentro.getTempoAte(clienteDomHelderCamara));

        System.out.println("\n=== Testes adicionais ===");

        Vertice clienteMagano = new Vertice(
                "magano",
                "rua conde da boa vista",
                30
        );

        Rota rotaMagano = Dijkstra.menorCaminho(
                grafoDoMapa,
                pizzariaHeliopolis,
                clienteMagano
        );

        System.out.println("Heliopolis -> " + clienteMagano);
        System.out.println("  " + rotaMagano);

        Vertice clienteSemRota = new Vertice(
                "heliopolis",
                "rua maria candido da silva",
                40
        );

        Rota semRota = Dijkstra.menorCaminho(
                grafoDoMapa,
                pizzariaCentro,
                clienteSemRota
        );

        System.out.println("Centro -> " + clienteSemRota);
        System.out.println("  existe? " + semRota.existe()
                + " | tempo: " + semRota.getTempoTotal()
                + " | caminho: " + semRota.getCaminho().size());

        Vertice clienteComparado = new Vertice(
                "centro",
                "rua do comercio",
                30
        );

        System.out.println("Comparacao para " + clienteComparado);
        System.out.println("  heliopolis: "
                + deHeliopolis.getTempoAte(clienteComparado) + " min");

        System.out.println("  centro: "
                + deCentro.getTempoAte(clienteComparado) + " min");

        System.out.println("Enderecos alcancaveis a partir de heliopolis: "
                + deHeliopolis.getAlcancaveis().size());

        System.out.println("Enderecos alcancaveis a partir do centro: "
                + deCentro.getAlcancaveis().size());
    }
}