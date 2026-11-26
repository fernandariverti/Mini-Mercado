package superMarket;

public class Cliente {
    // A ordem dos atributos define a ordem das colunas no CSV
    private String nome = "";
    private String cpf = "";
    private String email = "";
    private String telefone = "";

    public Cliente() {

    }

    public Cliente(String cpf, String nome, String email, String telefone) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void imprime() {
        System.out.println("CPF: " + cpf + " | Nome: " + nome
                + " | Telefone: " + telefone + " | Email: " + email);
    }

    // Lê nome, telefone e email (o CPF é informado antes, pela ListaClientes)
    public void lerDados() {
        String n = Entrada.texto("Digite o Nome: ");
        while (n.isEmpty()) {
            System.out.println("O nome não pode ser vazio.");
            n = Entrada.texto("Digite o Nome: ");
        }
        this.nome = n;
        this.telefone = Entrada.texto("Digite o Telefone: ");
        this.email = lerEmail("Digite o Email: ");
    }

    // Altera os dados; ENTER mantém o valor atual
    public void alterarDados() {
        String n = Entrada.texto("Nome (ENTER mantém \"" + nome + "\"): ");
        if (!n.isEmpty()) {
            this.nome = n;
        }

        String t = Entrada.texto("Telefone (ENTER mantém \"" + telefone + "\"): ");
        if (!t.isEmpty()) {
            this.telefone = t;
        }

        String e = lerEmail("Email (ENTER mantém \"" + email + "\"): ");
        if (!e.isEmpty()) {
            this.email = e;
        }
    }

    private String lerEmail(String pergunta) {
        String e = Entrada.texto(pergunta);
        while (!e.isEmpty() && !e.contains("@")) {
            System.out.println("Email inválido (deve conter @).");
            e = Entrada.texto(pergunta);
        }
        return e;
    }
}
