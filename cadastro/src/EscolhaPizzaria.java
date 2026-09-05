import java.util.ArrayList;

// decide de qual unidade sai a entrega para um cliente.
//
// o dijkstra roda uma vez por unidade, no momento em que ela e registrada,
// e o resultado e reaproveitado para todos os clientes: nao adianta rodar
// de novo a cada endereco porque a origem nao muda.
public class EscolhaPizzaria {

    // prazo de entrega em minutos. o dijkstra nao sabe nada sobre isso,
    // a regra e aplicada aqui.
    public static final int PRAZO_MINUTOS = 30;

    private Grafo grafo;
    private ArrayList<Vertice> unidades;
    private ArrayList<ResultadoDijkstra> resultados;

    public EscolhaPizzaria(Grafo grafo) {
        if (grafo == null) {
            throw new IllegalArgumentException("Grafo nao pode ser nulo");
        }
        this.grafo = grafo;
        this.unidades = new ArrayList<Vertice>();
        this.resultados = new ArrayList<ResultadoDijkstra>();
    }

    // registra uma unidade e ja calcula o dijkstra a partir dela.
    // lanca IllegalArgumentException se o endereco da unidade nao esta no mapa.
    public void adicionarUnidade(Vertice pizzaria) {
        ResultadoDijkstra resultado = Dijkstra.calcular(grafo, pizzaria);
        unidades.add(pizzaria);
        resultados.add(resultado);
    }

    public ArrayList<Vertice> getUnidades() {
        return new ArrayList<Vertice>(unidades);
    }

    // escolhe a unidade que chega mais rapido no cliente.
    //
    // o -1 e tratado antes de qualquer comparacao: unidade que nao alcanca o
    // cliente e descartada, nunca comparada. se comparasse direto, o -1 passaria
    // como o menor valor e justamente a unidade que nao entrega venceria.
    //
    // empate fica com a unidade registrada primeiro, para a resposta ser sempre
    // a mesma para a mesma entrada.
    public Escolha escolher(Vertice cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente nao pode ser nulo");
        }

        Vertice melhorUnidade = null;
        ResultadoDijkstra melhorResultado = null;
        int melhorTempo = -1;

        for (int i = 0; i < unidades.size(); i++) {
            ResultadoDijkstra resultado = resultados.get(i);

            if (!resultado.alcancavel(cliente)) {
                continue; // nao ha caminho ate o cliente, esta unidade esta fora
            }

            int tempo = resultado.getTempoAte(cliente);

            if (melhorUnidade == null || tempo < melhorTempo) {
                melhorUnidade = unidades.get(i);
                melhorResultado = resultado;
                melhorTempo = tempo;
            }
        }

        if (melhorUnidade == null) {
            return Escolha.semAtendimento(cliente); // nenhuma unidade alcanca
        }

        Rota rota = melhorResultado.getRotaAte(cliente);
        boolean dentroDoPrazo = melhorTempo <= PRAZO_MINUTOS;
        return new Escolha(cliente, melhorUnidade, rota, dentroDoPrazo);
    }
}
