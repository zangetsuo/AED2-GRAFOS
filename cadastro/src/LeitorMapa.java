import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class LeitorMapa {
    public void carregarMapa(String caminhoArquivo, Grafo grafo) {
    HashMap<String,Vertice> verticesCriados = new HashMap<>();
    try {   

        // inicializa o leitor de arquivo para ler o CSV com os dados do mapa
    BufferedReader leitor = new BufferedReader(new FileReader(caminhoArquivo));
    String linha;
    while((linha=leitor.readLine()) != null) { // verificar se a linha que está sendo lida é nula
        String[] partes = linha.split(";"); // divide o csv em partes separando por ponto e virgula
   
        if(partes[0].equals("V")){ // verifica se é vertice ou aresta
            String bairro = partes[1];
            String rua = partes[2];
            int  numero = Integer.parseInt(partes[3]);

            Vertice v=new Vertice(bairro, rua, numero); // cria novo vertice com dados lidos
            grafo.adicionarVertice(v); // adiciona o vertice criado ao grafo
            String chave = bairro + rua + numero; // cria uma chave única para o vertice, concatenando bairro, rua e número
            verticesCriados.put(chave, v);  // inclui o verttice no hashmap associado sua chave (endereço) ao vertice
        }else if (partes[0].equals("A")){ 
            String chaveOrigem = partes[1] + partes[2] + partes[3]; // cria uma chave única para a origem, concatenando bairro, rua e número
            String chaveDestino = partes[4] + partes[5] + partes[6]; // cria uma chave única para o destino, concatenando bairro, rua e número
            int peso = Integer.parseInt(partes[7]); // converte o peso da aresta de String para int

            Vertice origem = verticesCriados.get(chaveOrigem); // busca o vertice de origem do hashmap
            Vertice destino = verticesCriados.get(chaveDestino); // busca o vertice destino do hashmap
            grafo.adicionarAresta(origem, destino, peso); // adiciona a aresta ao grafo, ligando origem e destino com o peso especificado
        }
            System.out.println(linha);
        }
        leitor.close();
    } catch (Exception e) { // como pode ocorrer uma exceção, a linguagem tende a "passar por cima" do erro e não sinalizar. ai que estra esta verificacação
        System.out.println("Erro ao ler o arquivo: " + e.getMessage());
    }
    }
}
