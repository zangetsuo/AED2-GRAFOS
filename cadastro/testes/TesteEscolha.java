import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class TesteEscolha {
    static int passou = 0, falhou = 0;

    static void checa(String nome, boolean cond) {
        if (cond) { passou++; System.out.println("  OK      " + nome); }
        else { falhou++; System.out.println("  FALHOU  " + nome); }
    }
    static void bloco(String t) { System.out.println("\n[" + t + "]"); }

    static Vertice v(String rua) { return new Vertice("b", rua, 1); }

    public static void main(String[] args) {
        String caminhoMapa = args[0];

        bloco("Escolhe a unidade mais rapida");
        Grafo g = new Grafo();
        Vertice uA = v("unidade A"), uB = v("unidade B"), cli = v("cliente");
        g.adicionarVertice(uA); g.adicionarVertice(uB); g.adicionarVertice(cli);
        g.adicionarAresta(uA, cli, 20);
        g.adicionarAresta(uB, cli, 5);
        EscolhaPizzaria e1 = new EscolhaPizzaria(g);
        e1.adicionarUnidade(uA); e1.adicionarUnidade(uB);
        Escolha d1 = e1.escolher(cli);
        checa("escolhe a unidade B (5 min) e nao a A (20 min)", d1.getPizzaria().equals(uB));
        checa("tempo devolvido e 5", d1.getTempo() == 5);
        checa("foiAtendido", d1.foiAtendido());
        checa("a rota sai da unidade escolhida", d1.getRota().getOrigem().equals(uB));
        checa("a rota chega no cliente", d1.getRota().getDestino().equals(cli));
        checa("getTempo concorda com a rota", d1.getTempo() == d1.getRota().getTempoTotal());

        bloco("O -1 nao pode vencer a comparacao");
        // unidade B nao tem caminho nenhum ate o cliente: getTempoAte devolve -1.
        // comparando direto, -1 < 50 e a unidade que nao entrega venceria.
        Grafo g2 = new Grafo();
        Vertice uC = v("unidade C"), uD = v("unidade D"), cli2 = v("cliente 2");
        g2.adicionarVertice(uC); g2.adicionarVertice(uD); g2.adicionarVertice(cli2);
        g2.adicionarAresta(uC, cli2, 50);
        EscolhaPizzaria e2 = new EscolhaPizzaria(g2);
        e2.adicionarUnidade(uD); // a que nao alcanca entra primeiro de proposito
        e2.adicionarUnidade(uC);
        Escolha d2 = e2.escolher(cli2);
        checa("escolhe a unidade que alcanca, mesmo sendo lenta", d2.getPizzaria().equals(uC));
        checa("tempo 50 e nao -1", d2.getTempo() == 50);

        bloco("Nenhuma unidade alcanca o cliente");
        Escolha d3 = e2.escolher(v("ilha"));
        checa("cliente fora do grafo: foiAtendido false", !d3.foiAtendido());
        checa("getPizzaria null", d3.getPizzaria() == null);
        checa("getTempo -1", d3.getTempo() == -1);
        checa("dentroDoPrazo false", !d3.dentroDoPrazo());
        checa("a rota nao existe", !d3.getRota().existe());
        checa("toString avisa que ninguem alcanca", d3.toString().startsWith("Nenhuma unidade alcanca"));

        bloco("Prazo de 30 minutos");
        Grafo g3 = new Grafo();
        Vertice uE = v("unidade E"), c30 = v("cliente 30"), c31 = v("cliente 31");
        g3.adicionarVertice(uE); g3.adicionarVertice(c30); g3.adicionarVertice(c31);
        g3.adicionarAresta(uE, c30, 30);
        g3.adicionarAresta(uE, c31, 31);
        EscolhaPizzaria e3 = new EscolhaPizzaria(g3);
        e3.adicionarUnidade(uE);
        checa("PRAZO_MINUTOS e 30", EscolhaPizzaria.PRAZO_MINUTOS == 30);
        Escolha exato = e3.escolher(c30);
        checa("exatamente 30 min esta dentro do prazo", exato.dentroDoPrazo());
        checa("30 min: atendido", exato.foiAtendido());
        Escolha estourou = e3.escolher(c31);
        checa("31 min esta fora do prazo", !estourou.dentroDoPrazo());
        checa("31 min: ainda e atendido e devolve a rota", estourou.foiAtendido()
              && estourou.getRota().existe() && estourou.getTempo() == 31);
        checa("toString avisa que estourou", estourou.toString().contains("ACIMA do prazo"));

        bloco("Empate e determinismo");
        Grafo g4 = new Grafo();
        Vertice uF = v("unidade F"), uG = v("unidade G"), cli4 = v("cliente 4");
        g4.adicionarVertice(uF); g4.adicionarVertice(uG); g4.adicionarVertice(cli4);
        g4.adicionarAresta(uF, cli4, 10);
        g4.adicionarAresta(uG, cli4, 10);
        EscolhaPizzaria e4 = new EscolhaPizzaria(g4);
        e4.adicionarUnidade(uF); e4.adicionarUnidade(uG);
        checa("empate fica com a registrada primeiro", e4.escolher(cli4).getPizzaria().equals(uF));
        boolean estavel = true;
        for (int i = 0; i < 50; i++) if (!e4.escolher(cli4).getPizzaria().equals(uF)) estavel = false;
        checa("a resposta e a mesma em 50 chamadas", estavel);

        bloco("Casos de borda");
        checa("cliente == unidade: 0 min", e1.escolher(uA).getTempo() == 0);
        checa("cliente == unidade: dentro do prazo", e1.escolher(uA).dentroDoPrazo());
        boolean semUnidades = !new EscolhaPizzaria(g).escolher(cli).foiAtendido();
        checa("sem unidades registradas: nao atendido", semUnidades);
        boolean lancouCliente = false;
        try { e1.escolher(null); } catch (IllegalArgumentException ex) { lancouCliente = true; }
        checa("cliente null lanca IllegalArgumentException", lancouCliente);
        boolean lancouGrafo = false;
        try { new EscolhaPizzaria(null); } catch (IllegalArgumentException ex) { lancouGrafo = true; }
        checa("grafo null lanca IllegalArgumentException", lancouGrafo);
        boolean lancouUnidade = false;
        try { e1.adicionarUnidade(v("nao existe no grafo")); }
        catch (IllegalArgumentException ex) { lancouUnidade = true; }
        checa("unidade fora do grafo lanca IllegalArgumentException", lancouUnidade);
        ArrayList<Vertice> copia = e1.getUnidades();
        copia.clear();
        checa("getUnidades devolve copia, nao a lista interna", e1.getUnidades().size() == 2);

        bloco("Escolha sobre o mapa.csv real");
        Grafo mapa = new Grafo();
        PrintStream orig = System.out;
        System.setOut(new PrintStream(new OutputStream() { public void write(int b) { } }));
        new LeitorMapa().carregarMapa(caminhoMapa, mapa);
        System.setOut(orig);

        Vertice pHeliopolis = new Vertice("heliopolis", "praca da pizzaria heliopolis", 1);
        Vertice pCentro = new Vertice("centro", "praca da pizzaria centro", 1);
        EscolhaPizzaria real = new EscolhaPizzaria(mapa);
        real.adicionarUnidade(pHeliopolis);
        real.adicionarUnidade(pCentro);

        Vertice clienteMagano = new Vertice("magano", "rua conde da boa vista", 30);
        Escolha dMagano = real.escolher(clienteMagano);
        checa("cliente do Magano sai do Centro (17) e nao de Heliopolis (29)",
              dMagano.getPizzaria().equals(pCentro) && dMagano.getTempo() == 17);
        checa("cliente do Magano cabe no prazo", dMagano.dentroDoPrazo());
        Escolha dSemRota = real.escolher(new Vertice("heliopolis", "rua maria candido da silva", 40));
        checa("endereco que ninguem alcanca: nao atendido", !dSemRota.foiAtendido());

        // conferencia exaustiva: para todo endereco do mapa, a escolha tem que
        // bater com o minimo calculado a parte, direto dos dois Dijkstras
        ResultadoDijkstra rH = Dijkstra.calcular(mapa, pHeliopolis);
        ResultadoDijkstra rC = Dijkstra.calcular(mapa, pCentro);
        boolean tudoBate = true;
        int atendidos = 0, foraDoPrazo = 0, semAtendimento = 0;
        for (Vertice destino : mapa.obterVertices()) {
            int tH = rH.getTempoAte(destino);
            int tC = rC.getTempoAte(destino);
            Escolha d = real.escolher(destino);

            if (tH == -1 && tC == -1) {
                semAtendimento++;
                if (d.foiAtendido()) tudoBate = false;
                continue;
            }
            int esperado;
            if (tH == -1) esperado = tC;
            else if (tC == -1) esperado = tH;
            else esperado = Math.min(tH, tC);

            atendidos++;
            if (!d.foiAtendido() || d.getTempo() != esperado) tudoBate = false;
            if (d.dentroDoPrazo() != (esperado <= 30)) tudoBate = false;
            if (!d.getRota().getOrigem().equals(d.getPizzaria())) tudoBate = false;
            if (!d.dentroDoPrazo()) foraDoPrazo++;
        }
        checa("os 34 enderecos do mapa batem com o minimo calculado a parte", tudoBate);
        System.out.println("           (atendidos: " + atendidos
                + " | destes fora do prazo: " + foraDoPrazo
                + " | sem atendimento: " + semAtendimento + ")");

        System.out.println("\n=====================================");
        System.out.println("  passou: " + passou + "   falhou: " + falhou);
        System.out.println("=====================================");
    }
}
