// roda as quatro suites de uma vez.
//
// nenhuma suite altera as classes do trabalho: todas usam apenas os metodos
// publicos de Grafo, Vertice, Aresta, LeitorMapa, Dijkstra e EscolhaPizzaria.
//
// uso, a partir da raiz do repositorio:
//   javac -d cadastro/bin cadastro/src/*.java cadastro/testes/*.java
//   java -cp cadastro/bin RodarTestes
public class RodarTestes {

    public static void main(String[] args) throws Exception {
        String mapa = "cadastro/mapa.csv";
        if (args.length > 0) {
            mapa = args[0];
        }

        String[] caminho = { mapa };

        System.out.println("###### Vertice, Aresta, Grafo e integridade do mapa.csv ######");
        TesteCompleto.main(caminho);

        System.out.println("\n\n###### Contrato do Dijkstra ######");
        Teste.main(caminho);

        System.out.println("\n\n###### Escolha da pizzaria ######");
        TesteEscolha.main(caminho);

        System.out.println("\n\n###### Dijkstra contra Bellman-Ford em grafos aleatorios ######");
        Estresse.main(new String[0]);
    }
}
