// resultado da decisao de qual unidade atende um cliente.
// quando nenhuma unidade alcanca o cliente, foiAtendido() e false,
// getPizzaria() e null e getTempo() devolve -1, seguindo a mesma
// convencao do -1 usada pelo Dijkstra.
public class Escolha {
    private Vertice cliente;
    private Vertice pizzaria;
    private Rota rota;
    private boolean atendido;
    private boolean dentroDoPrazo;

    public Escolha(Vertice cliente, Vertice pizzaria, Rota rota, boolean dentroDoPrazo) {
        this.cliente = cliente;
        this.pizzaria = pizzaria;
        this.rota = rota;
        this.atendido = true;
        this.dentroDoPrazo = dentroDoPrazo;
    }

    // usado quando nenhuma unidade tem caminho ate o cliente
    public static Escolha semAtendimento(Vertice cliente) {
        Escolha e = new Escolha(cliente, null, Rota.inexistente(null, cliente), false);
        e.atendido = false;
        return e;
    }

    public Vertice getCliente() {
        return cliente;
    }

    // a unidade escolhida, ou null quando nenhuma alcanca o cliente
    public Vertice getPizzaria() {
        return pizzaria;
    }

    public Rota getRota() {
        return rota;
    }

    public boolean foiAtendido() {
        return atendido;
    }

    // true quando o tempo cabe no prazo de EscolhaPizzaria.PRAZO_MINUTOS
    public boolean dentroDoPrazo() {
        return dentroDoPrazo;
    }

    public int getTempo() {
        if (!atendido) {
            return -1;
        }
        return rota.getTempoTotal();
    }

    @Override
    public String toString() {
        if (!atendido) {
            return "Nenhuma unidade alcanca " + cliente;
        }
        String prazo;
        if (dentroDoPrazo) {
            prazo = "dentro do prazo de " + EscolhaPizzaria.PRAZO_MINUTOS + " min";
        } else {
            prazo = "ACIMA do prazo de " + EscolhaPizzaria.PRAZO_MINUTOS + " min";
        }
        return cliente + ": sai de " + pizzaria + " em " + getTempo() + " min (" + prazo + ")";
    }
}
