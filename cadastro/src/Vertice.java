public class Vertice {
    private String bairro; // ao colocar o "private" o a variavel bairro so pode ser acessado dentro da classe vertice, se tentar acessar no app. java vai dar erro
    private String rua;
    private int numero;

    public Vertice(String bairro, String rua, int numero) { // constreutor da classe vertice "assemelhasse com struct em c"
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
    }

    public String getBairro() {
        if(bairro == null){
            return "Bairro não informado";
        } else {
            return bairro;
        }
    } // devolve o atributo bairro, o valor que esta armazenado na variavel bairro

    public void setBairro(String bairro) {
        this.bairro = bairro; // recebe o valor e guarda no atributo bairro
    }

    public String getRua() {
        if( rua == null){
            return "Rua não informada";
        } else {
            return rua;
        }
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public int getNumero() {
        if( numero == 0){
            return 0;
        } else {
            return numero;
        }
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
    @Override // susbstituimos uma finção nativa do java por essa aqui para verificar se dois vertices são iguais
    public boolean equals(Object outroObjeto) {
    if (this == outroObjeto) {
        return true;
    }
    if (!(outroObjeto instanceof Vertice)) {
        return false;
    }
    Vertice outroVertice = (Vertice) outroObjeto; 
    return this.numero == outroVertice.numero && this.bairro.equals(outroVertice.bairro) && this.rua.equals(outroVertice.rua);
}
@Override
public int hashCode() {
    return bairro.hashCode() + rua.hashCode() + numero;
}
@Override
public String toString() {
    return rua + ", " + numero + " - " + bairro;
}
}
