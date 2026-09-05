import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

// leva de testes rapida sobre o codigo atual. nao altera nada do projeto.
public class Teste {
    static int passou = 0;
    static int falhou = 0;

    static void checa(String nome, boolean condicao) {
        if (condicao) {
            passou++;
            System.out.println("  OK      " + nome);
        } else {
            falhou++;
            System.out.println("  FALHOU  " + nome);
        }
    }

    static void bloco(String titulo) {
        System.out.println("\n[" + titulo + "]");
    }

    public static void main(String[] args) {
        String caminhoMapa = args[0];

        bloco("Vertice");
        Vertice v1 = new Vertice("heliopolis", "rua cleto campelo", 10);
        Vertice v2 = new Vertice("heliopolis", "rua cleto campelo", 10);
        Vertice v3 = new Vertice("heliopolis", "rua cleto campelo", 20);
        checa("dois vertices com os mesmos dados sao equals", v1.equals(v2));
        checa("equals reflexivo", v1.equals(v1));
        checa("numero diferente nao e equals", !v1.equals(v3));
        checa("equals com outro tipo devolve false", !v1.equals("texto"));
        checa("hashCode igual para vertices iguais", v1.hashCode() == v2.hashCode());
        java.util.HashMap<Vertice, String> mapa = new java.util.HashMap<Vertice, String>();
        mapa.put(v1, "achou");
        checa("instancia diferente acha a mesma chave no HashMap", "achou".equals(mapa.get(v2)));
        checa("toString no formato esperado",
              "rua cleto campelo, 10 - heliopolis".equals(v1.toString()));

        bloco("Grafo");
        Grafo g = new Grafo();
        g.adicionarVertice(v1);
        g.adicionarVertice(v3);
        g.adicionarAresta(v1, v3, 5);
        checa("existeVertice para vertice adicionado", g.existeVertice(v1));
        checa("existeVertice para vertice de fora", !g.existeVertice(new Vertice("x", "y", 1)));
        checa("obterVizinhos devolve a aresta criada", g.obterVizinhos(v1).size() == 1);
        checa("peso da aresta preservado", g.obterVizinhos(v1).get(0).getPeso() == 5);
        checa("destino da aresta preservado", g.obterVizinhos(v1).get(0).getDestino().equals(v3));
        checa("grafo dirigido: aresta nao aparece na volta", g.obterVizinhos(v3).size() == 0);
        ArrayList<Aresta> vizinhosDeFora = g.obterVizinhos(new Vertice("x", "y", 1));
        checa("obterVizinhos de vertice fora do grafo devolve lista vazia, nao null",
              vizinhosDeFora != null && vizinhosDeFora.size() == 0);
        checa("obterVertices devolve os dois vertices", g.obterVertices().size() == 2);

        bloco("Dijkstra em grafo pequeno construido na mao");
        // a->b=1, b->c=2, a->c=10 : o menor caminho ate c e a->b->c com 3, nao a aresta direta
        Grafo p = new Grafo();
        Vertice a = new Vertice("t", "rua a", 1);
        Vertice b = new Vertice("t", "rua b", 1);
        Vertice c = new Vertice("t", "rua c", 1);
        Vertice ilha = new Vertice("t", "rua ilha", 1);
        p.adicionarVertice(a);
        p.adicionarVertice(b);
        p.adicionarVertice(c);
        p.adicionarVertice(ilha);
        p.adicionarAresta(a, b, 1);
        p.adicionarAresta(b, c, 2);
        p.adicionarAresta(a, c, 10);
        ResultadoDijkstra rp = Dijkstra.calcular(p, a);
        checa("prefere o caminho de 2 saltos (3) em vez da aresta direta (10)",
              rp.getTempoAte(c) == 3);
        checa("caminho reconstruido passa por b", rp.getRotaAte(c).getCaminho().size() == 3
              && rp.getRotaAte(c).getCaminho().get(1).equals(b));
        checa("vertice sem aresta de entrada e inalcancavel", !rp.alcancavel(ilha));
        checa("alcancaveis conta so os 3 conectados", rp.getAlcancaveis().size() == 3);

        bloco("Dijkstra: casos de borda do DIJKSTRA.md");
        Rota mesma = Dijkstra.menorCaminho(p, a, a);
        checa("origem == destino: existe()", mesma.existe());
        checa("origem == destino: tempo 0", mesma.getTempoTotal() == 0);
        checa("origem == destino: caminho com 1 elemento", mesma.getCaminho().size() == 1);
        Rota semRota = Dijkstra.menorCaminho(p, a, new Vertice("fora", "do mapa", 99));
        checa("destino fora do mapa: existe() == false", !semRota.existe());
        checa("destino fora do mapa: tempo -1", semRota.getTempoTotal() == -1);
        checa("destino fora do mapa: caminho vazio", semRota.getCaminho().size() == 0);
        checa("destino fora do mapa: getTempoAte tambem devolve -1",
              rp.getTempoAte(new Vertice("fora", "do mapa", 99)) == -1);
        boolean lancouNull = false;
        try { Dijkstra.calcular(p, null); } catch (IllegalArgumentException e) { lancouNull = true; }
        checa("origem null lanca IllegalArgumentException", lancouNull);
        boolean lancouFora = false;
        try { Dijkstra.calcular(p, new Vertice("fora", "do mapa", 99)); }
        catch (IllegalArgumentException e) { lancouFora = true; }
        checa("origem fora do grafo lanca IllegalArgumentException", lancouFora);

        bloco("LeitorMapa + mapa.csv");
        Grafo mapaReal = new Grafo();
        PrintStream saidaOriginal = System.out;
        System.setOut(new PrintStream(new OutputStream() { public void write(int b) { } }));
        new LeitorMapa().carregarMapa(caminhoMapa, mapaReal);
        System.setOut(saidaOriginal);
        int totalArestas = 0;
        for (Vertice v : mapaReal.obterVertices()) {
            totalArestas += mapaReal.obterVizinhos(v).size();
        }
        checa("carregou 34 vertices", mapaReal.obterVertices().size() == 34);
        checa("carregou 39 arestas", totalArestas == 39);
        checa("endereco conhecido existe no grafo",
              mapaReal.existeVertice(new Vertice("heliopolis", "rua cleto campelo", 10)));

        bloco("Dijkstra sobre o mapa real");
        Vertice pizzariaHeliopolis = new Vertice("heliopolis", "praca da pizzaria heliopolis", 1);
        Vertice pizzariaCentro = new Vertice("centro", "praca da pizzaria centro", 1);
        Vertice cliente = new Vertice("magano", "rua conde da boa vista", 30);
        Vertice inalcancavelDoCentro = new Vertice("heliopolis", "rua maria candido da silva", 40);

        ResultadoDijkstra deHeliopolis = Dijkstra.calcular(mapaReal, pizzariaHeliopolis);
        ResultadoDijkstra deCentro = Dijkstra.calcular(mapaReal, pizzariaCentro);

        checa("Heliopolis -> cliente do Magano em 29 min", deHeliopolis.getTempoAte(cliente) == 29);
        Rota rotaCliente = deHeliopolis.getRotaAte(cliente);
        checa("rota comeca na pizzaria de Heliopolis",
              rotaCliente.getCaminho().get(0).equals(pizzariaHeliopolis));
        checa("rota termina no cliente",
              rotaCliente.getCaminho().get(rotaCliente.getCaminho().size() - 1).equals(cliente));
        checa("getTempoAte e getTempoTotal concordam",
              rotaCliente.getTempoTotal() == deHeliopolis.getTempoAte(cliente));
        int somaDosPesos = 0;
        ArrayList<Vertice> caminho = rotaCliente.getCaminho();
        for (int i = 0; i < caminho.size() - 1; i++) {
            for (Aresta ar : mapaReal.obterVizinhos(caminho.get(i))) {
                if (ar.getDestino().equals(caminho.get(i + 1))) { somaDosPesos += ar.getPeso(); break; }
            }
        }
        checa("soma dos pesos do caminho bate com o tempo total",
              somaDosPesos == rotaCliente.getTempoTotal());
        checa("Centro nao alcanca rua maria candido da silva, 40",
              !deCentro.alcancavel(inalcancavelDoCentro));
        checa("e devolve -1 nesse caso", deCentro.getTempoAte(inalcancavelDoCentro) == -1);
        checa("25 enderecos alcancaveis a partir de Heliopolis",
              deHeliopolis.getAlcancaveis().size() == 25);
        checa("16 enderecos alcancaveis a partir do Centro",
              deCentro.getAlcancaveis().size() == 16);

        bloco("Consistencia geral do Dijkstra no mapa real");
        boolean todasRotasCoerentes = true;
        for (Vertice destino : deHeliopolis.getAlcancaveis()) {
            Rota r = deHeliopolis.getRotaAte(destino);
            if (!r.existe() || r.getTempoTotal() != deHeliopolis.getTempoAte(destino)) {
                todasRotasCoerentes = false;
            }
            if (!r.getCaminho().get(0).equals(pizzariaHeliopolis)) todasRotasCoerentes = false;
            if (!r.getCaminho().get(r.getCaminho().size() - 1).equals(destino)) todasRotasCoerentes = false;
        }
        checa("as 25 rotas de Heliopolis sao coerentes (tempo, inicio e fim)", todasRotasCoerentes);
        boolean desigualdadeTriangularOk = true;
        for (Vertice u : deHeliopolis.getAlcancaveis()) {
            for (Aresta ar : mapaReal.obterVizinhos(u)) {
                Vertice w = ar.getDestino();
                if (deHeliopolis.getTempoAte(u) + ar.getPeso() < deHeliopolis.getTempoAte(w)) {
                    desigualdadeTriangularOk = false;
                }
            }
        }
        checa("nenhuma aresta permite melhorar uma distancia (otimalidade)", desigualdadeTriangularOk);

        System.out.println("\n=====================================");
        System.out.println("  passou: " + passou + "   falhou: " + falhou);
        System.out.println("=====================================");
    }
}
