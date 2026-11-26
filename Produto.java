package superMarket;

public class Produto {
    // A ordem dos atributos define a ordem das colunas no CSV
    private int codigo;
    private String nome = "";
    private double preco;
    private int estoque;

    public Produto() {

    }

    public Produto(int codigo, String nome, double preco, int estoque) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public void imprime() {
        System.out.printf("Código: %d | Nome: %s | Preço: R$ %.2f | Estoque: %d%n",
                codigo, nome, preco, estoque);
    }
}
