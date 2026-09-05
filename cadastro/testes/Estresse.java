import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

// compara o Dijkstra do projeto com um Bellman-Ford de referencia
// escrito aqui dentro, em grafos dirigidos aleatorios com pesos positivos.
public class Estresse {

    // referencia: Bellman-Ford, lento porem simples e comprovadamente correto
    static HashMap<Vertice, Integer> bellmanFord(Grafo g, Vertice origem,
                                                 ArrayList<Vertice> vertices) {
        HashMap<Vertice, Integer> dist = new HashMap<Vertice, Integer>();
        dist.put(origem, 0);
        for (int passo = 0; passo < vertices.size(); passo++) {
            for (Vertice u : vertices) {
                if (!dist.containsKey(u)) continue;
                for (Aresta a : g.obterVizinhos(u)) {
                    int nova = dist.get(u) + a.getPeso();
                    Vertice w = a.getDestino();
                    if (!dist.containsKey(w) || nova < dist.get(w)) {
                        dist.put(w, nova);
                    }
                }
            }
        }
        return dist;
    }

    public static void main(String[] args) {
        int sementesTestadas = 0;
        int grafosComDivergencia = 0;
        long primeiraSementeRuim = -1;

        for (long semente = 1; semente <= 3000; semente++) {
            Random r = new Random(semente);
            int n = 6 + r.nextInt(9);          // 6 a 14 vertices
            Grafo g = new Grafo();
            ArrayList<Vertice> vs = new ArrayList<Vertice>();
            for (int i = 0; i < n; i++) {
                Vertice v = new Vertice("b", "rua " + i, 1);
                vs.add(v);
                g.adicionarVertice(v);
            }
            int arestas = n + r.nextInt(n * 2);
            for (int i = 0; i < arestas; i++) {
                int a = r.nextInt(n);
                int b = r.nextInt(n);
                if (a == b) continue;
                g.adicionarAresta(vs.get(a), vs.get(b), 1 + r.nextInt(20));
            }

            Vertice origem = vs.get(0);
            ResultadoDijkstra rd = Dijkstra.calcular(g, origem);
            HashMap<Vertice, Integer> ref = bellmanFord(g, origem, vs);

            boolean divergiu = false;
            for (Vertice v : vs) {
                int esperado = ref.containsKey(v) ? ref.get(v) : -1;
                int obtido = rd.getTempoAte(v);
                if (esperado != obtido) {
                    divergiu = true;
                    if (primeiraSementeRuim == -1) {
                        primeiraSementeRuim = semente;
                        System.out.println("DIVERGENCIA na semente " + semente
                                + " (" + n + " vertices, " + arestas + " arestas)");
                        System.out.println("  destino " + v
                                + ": Bellman-Ford diz " + esperado
                                + ", Dijkstra do projeto diz " + obtido);
                    }
                }
            }
            if (divergiu) grafosComDivergencia++;
            sementesTestadas++;
        }

        System.out.println("\ngrafos aleatorios testados: " + sementesTestadas);
        System.out.println("grafos com resultado divergente: " + grafosComDivergencia);
        if (grafosComDivergencia == 0) {
            System.out.println("RESULTADO: nenhuma divergencia encontrada");
        } else {
            System.out.println("RESULTADO: o Dijkstra do projeto devolve distancia errada em "
                    + grafosComDivergencia + " dos " + sementesTestadas + " grafos");
        }
    }
}
