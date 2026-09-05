
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Grafo {
    private HashMap<Vertice, ArrayList<Aresta>> adjacencias;

    public Grafo(){
    this.adjacencias = new HashMap<Vertice, ArrayList<Aresta>>(); // cria o hasmap e junto com ele a lista de array para armazenar as arestas que
    }                                                             // as arestas que saem do vertice, então todos vertices teram um arraylist
    public void adicionarVertice(Vertice v){
      adjacencias.put(v, new ArrayList<Aresta>()); // cria-se o endereço V no mapa de hashs "hashmap" 
    }                                              // e gera um novo array dentro desse endereço e nesse array armazena a lista de arestas que saem desse vértice
    public ArrayList<Aresta> obterVizinhos(Vertice v) {
        ArrayList<Aresta> vizinhos = adjacencias.get(v);
        if (vizinhos == null) { // vertice fora do grafo devolve lista vazia em vez de null
            return new ArrayList<Aresta>();
        }
        return vizinhos;
    }
    public void adicionarAresta(Vertice origem, Vertice destino, int peso){
      Aresta novaAresta = new Aresta(destino, peso); // cria uma nova aresta que leva ao destino e tem o peso (tempo) informado
      adjacencias.get(origem).add(novaAresta); // pega o endereço do vertice origem e adiciona a aresta que leva ao destino
    }
    public boolean existeVertice(Vertice v){
      return adjacencias.containsKey(v);
    }

    public Set<Vertice> obterVertices(){
      return adjacencias.keySet();
    }

  }
