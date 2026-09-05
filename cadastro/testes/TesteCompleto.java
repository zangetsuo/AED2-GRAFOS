import java.io.BufferedReader;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

// cobre o que as outras baterias nao alcancavam: Vertice, Aresta, Grafo,
// LeitorMapa com arquivos quebrados e a integridade do mapa.csv.
public class TesteCompleto {
    static int passou = 0, falhou = 0, atencao = 0;

    static void checa(String nome, boolean cond) {
        if (cond) { passou++; System.out.println("  OK        " + nome); }
        else { falhou++; System.out.println("  FALHOU    " + nome); }
    }
    // comportamento atual que funciona, mas e fragil: registrado, nao contado como falha
    static void defeito(String nome, boolean confirmado) {
        if (confirmado) { atencao++; System.out.println("  ATENCAO   " + nome); }
        else { passou++; System.out.println("  OK        " + nome + " (nao se confirma mais)"); }
    }
    static void bloco(String t) { System.out.println("\n[" + t + "]"); }

    static PrintStream saida;
    static void calar() { saida = System.out; System.setOut(new PrintStream(new OutputStream() { public void write(int b) { } })); }
    static void falar() { System.setOut(saida); }

    public static void main(String[] args) throws Exception {
        String mapaReal = args[0];

        // ---------------------------------------------------------------- Vertice
        bloco("Vertice: getters, setters e representacao");
        Vertice v = new Vertice("heliopolis", "rua cleto campelo", 10);
        checa("getBairro", v.getBairro().equals("heliopolis"));
        checa("getRua", v.getRua().equals("rua cleto campelo"));
        checa("getNumero", v.getNumero() == 10);
        checa("toString", v.toString().equals("rua cleto campelo, 10 - heliopolis"));
        Vertice mut = new Vertice("a", "b", 1);
        mut.setBairro("c"); mut.setRua("d"); mut.setNumero(2);
        checa("setters alteram os tres campos",
              mut.getBairro().equals("c") && mut.getRua().equals("d") && mut.getNumero() == 2);
        checa("getBairro avisa quando o campo e nulo",
              new Vertice(null, "r", 1).getBairro().equals("Bairro não informado"));
        checa("getRua avisa quando o campo e nulo",
              new Vertice("b", null, 1).getRua().equals("Rua não informada"));
        checa("numero zero e devolvido como zero", new Vertice("b", "r", 0).getNumero() == 0);

        bloco("Vertice: igualdade e hash");
        checa("equals com null devolve false", !v.equals(null));
        checa("equals ignora instancia", v.equals(new Vertice("heliopolis", "rua cleto campelo", 10)));
        checa("bairro diferente nao e igual", !v.equals(new Vertice("centro", "rua cleto campelo", 10)));
        checa("rua diferente nao e igual", !v.equals(new Vertice("heliopolis", "outra rua", 10)));
        checa("hashCode estavel entre chamadas", v.hashCode() == v.hashCode());
        boolean npeHash = false;
        try { new Vertice(null, "r", 1).hashCode(); } catch (NullPointerException e) { npeHash = true; }
        defeito("hashCode() estoura NullPointerException com bairro nulo", npeHash);
        boolean npeEquals = false;
        try { new Vertice(null, "r", 1).equals(new Vertice("b", "r", 1)); }
        catch (NullPointerException e) { npeEquals = true; }
        defeito("equals() estoura NullPointerException com bairro nulo", npeEquals);
        // vertice usado como chave e depois alterado
        Grafo gm = new Grafo();
        Vertice chave = new Vertice("x", "y", 1);
        gm.adicionarVertice(chave);
        chave.setNumero(99);
        defeito("alterar um vertice ja usado como chave o some do grafo", !gm.existeVertice(chave));

        // ---------------------------------------------------------------- Aresta
        bloco("Aresta");
        Vertice destino = new Vertice("b", "r", 1);
        Aresta a = new Aresta(destino, 15);
        checa("getDestino", a.getDestino().equals(destino));
        checa("getPeso", a.getPeso() == 15);
        Vertice outro = new Vertice("b", "r", 2);
        a.setDestino(outro); a.setPeso(3);
        checa("setDestino", a.getDestino().equals(outro));
        checa("setPeso", a.getPeso() == 3);
        checa("aceita peso zero", new Aresta(destino, 0).getPeso() == 0);

        // ---------------------------------------------------------------- Grafo
        bloco("Grafo");
        Grafo g = new Grafo();
        Vertice p = new Vertice("b", "p", 1), q = new Vertice("b", "q", 1), r = new Vertice("b", "r", 1);
        checa("grafo novo esta vazio", g.obterVertices().size() == 0);
        g.adicionarVertice(p); g.adicionarVertice(q); g.adicionarVertice(r);
        checa("obterVertices conta os tres", g.obterVertices().size() == 3);
        g.adicionarAresta(p, q, 4);
        g.adicionarAresta(p, r, 9);
        checa("duas arestas saindo do mesmo vertice", g.obterVizinhos(p).size() == 2);
        checa("vertice sem saida tem lista vazia", g.obterVizinhos(q).size() == 0);
        checa("existeVertice(null) devolve false sem estourar", !g.existeVertice(null));
        checa("obterVizinhos(null) devolve lista vazia", g.obterVizinhos(null).size() == 0);
        g.adicionarAresta(p, q, 1); // aresta paralela
        checa("aceita aresta paralela (duas para o mesmo destino)", g.obterVizinhos(p).size() == 3);
        g.adicionarAresta(p, p, 2); // laco
        checa("aceita laco (vertice para ele mesmo)", g.obterVizinhos(p).size() == 4);
        boolean npeAresta = false;
        try { g.adicionarAresta(new Vertice("fora", "do grafo", 1), q, 5); }
        catch (NullPointerException e) { npeAresta = true; }
        defeito("adicionarAresta com origem fora do grafo estoura NullPointerException", npeAresta);
        Grafo g2 = new Grafo();
        g2.adicionarVertice(p);
        g2.adicionarVertice(q);
        g2.adicionarAresta(p, q, 5);
        g2.adicionarVertice(p); // readicionar o mesmo vertice
        defeito("readicionar um vertice apaga as arestas que saiam dele", g2.obterVizinhos(p).size() == 0);

        // ---------------------------------------------------------------- LeitorMapa
        bloco("LeitorMapa: arquivo ausente");
        Grafo gi = new Grafo();
        calar(); new LeitorMapa().carregarMapa("arquivo_que_nao_existe.csv", gi); falar();
        checa("arquivo inexistente nao lanca excecao para quem chama", true);
        defeito("mas devolve um grafo vazio sem avisar que a leitura falhou",
                gi.obterVertices().size() == 0);

        // ---------------------------------------------------------------- mapa.csv
        bloco("mapa.csv: integridade dos dados");
        Grafo mapa = new Grafo();
        calar(); new LeitorMapa().carregarMapa(mapaReal, mapa); falar();

        ArrayList<String[]> linhasV = new ArrayList<String[]>();
        ArrayList<String[]> linhasA = new ArrayList<String[]>();
        BufferedReader br = new BufferedReader(new FileReader(mapaReal));
        String linha;
        int numeroLinha = 0, linhasVazias = 0, linhasDesconhecidas = 0;
        while ((linha = br.readLine()) != null) {
            numeroLinha++;
            if (linha.trim().isEmpty()) { linhasVazias++; continue; }
            String[] partes = linha.split(";");
            if (partes[0].equals("V")) linhasV.add(partes);
            else if (partes[0].equals("A")) linhasA.add(partes);
            else linhasDesconhecidas++;
        }
        br.close();
        checa("o arquivo nao tem linhas vazias nem tipos desconhecidos",
              linhasVazias == 0 && linhasDesconhecidas == 0);
        checa("34 linhas V e 39 linhas A", linhasV.size() == 34 && linhasA.size() == 39);
        checa("o grafo tem um vertice para cada linha V", mapa.obterVertices().size() == linhasV.size());

        boolean camposV = true, semDuplicata = true;
        HashSet<String> vistos = new HashSet<String>();
        for (String[] partes : linhasV) {
            if (partes.length != 4) camposV = false;
            if (!vistos.add(partes[1] + "|" + partes[2] + "|" + partes[3])) semDuplicata = false;
            if (!mapa.existeVertice(new Vertice(partes[1], partes[2], Integer.parseInt(partes[3]))))
                camposV = false;
        }
        checa("toda linha V tem 4 campos e virou vertice no grafo", camposV);
        checa("nao ha endereco declarado duas vezes", semDuplicata);

        int totalArestas = 0;
        for (Vertice x : mapa.obterVertices()) totalArestas += mapa.obterVizinhos(x).size();
        checa("o grafo tem uma aresta para cada linha A", totalArestas == linhasA.size());

        boolean camposA = true, pesosPositivos = true, arestasConferem = true;
        HashMap<String, Integer> esperadas = new HashMap<String, Integer>();
        for (String[] partes : linhasA) {
            if (partes.length != 8) { camposA = false; continue; }
            int peso = Integer.parseInt(partes[7]);
            if (peso <= 0) pesosPositivos = false;
            Vertice origem = new Vertice(partes[1], partes[2], Integer.parseInt(partes[3]));
            Vertice dest = new Vertice(partes[4], partes[5], Integer.parseInt(partes[6]));
            if (!mapa.existeVertice(origem) || !mapa.existeVertice(dest)) arestasConferem = false;
            esperadas.put(origem + " => " + dest, peso);
            boolean achou = false;
            for (Aresta ar : mapa.obterVizinhos(origem))
                if (ar.getDestino().equals(dest) && ar.getPeso() == peso) achou = true;
            if (!achou) arestasConferem = false;
        }
        checa("toda linha A tem 8 campos", camposA);
        checa("todos os pesos sao positivos (exigencia do Dijkstra)", pesosPositivos);
        checa("toda aresta do arquivo existe no grafo com o peso certo", arestasConferem);
        checa("nao ha aresta duplicada", esperadas.size() == linhasA.size());

        int mutuas = 0;
        for (String[] partes : linhasA) {
            Vertice origem = new Vertice(partes[1], partes[2], Integer.parseInt(partes[3]));
            Vertice dest = new Vertice(partes[4], partes[5], Integer.parseInt(partes[6]));
            if (esperadas.containsKey(dest + " => " + origem)) mutuas++;
        }
        System.out.println("           (arestas com volta: " + mutuas
                + " | so de ida: " + (linhasA.size() - mutuas) + ")");

        bloco("mapa.csv: as duas pizzarias");
        Vertice ph = new Vertice("heliopolis", "praca da pizzaria heliopolis", 1);
        Vertice pc = new Vertice("centro", "praca da pizzaria centro", 1);
        checa("pizzaria de Heliopolis esta no mapa", mapa.existeVertice(ph));
        checa("pizzaria do Centro esta no mapa", mapa.existeVertice(pc));
        checa("cada pizzaria tem saida",
              mapa.obterVizinhos(ph).size() > 0 && mapa.obterVizinhos(pc).size() > 0);
        ResultadoDijkstra rh = Dijkstra.calcular(mapa, ph);
        ResultadoDijkstra rc = Dijkstra.calcular(mapa, pc);
        checa("25 alcancaveis de Heliopolis", rh.getAlcancaveis().size() == 25);
        checa("16 alcancaveis do Centro", rc.getAlcancaveis().size() == 16);
        int nenhuma = 0;
        for (Vertice x : mapa.obterVertices())
            if (!rh.alcancavel(x) && !rc.alcancavel(x)) nenhuma++;
        checa("9 enderecos nao sao alcancaveis por nenhuma unidade", nenhuma == 9);

        System.out.println("\n=====================================");
        System.out.println("  passou: " + passou + "   falhou: " + falhou
                + "   atencao: " + atencao);
        System.out.println("=====================================");
    }
}
