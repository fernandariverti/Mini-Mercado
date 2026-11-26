package superMarket;

import java.util.Arrays;

public class ListaClientes {
    private static final String ARQUIVO = "./superMarket/clientes.csv";

    public Cliente lista[];
    public int qtde;

    public ListaClientes() {
        lista = new Cliente[100];
        qtde = 0;
    }

    // ---------- CREATE ----------
    public void cadastrar() {
        Screen.titulo("CADASTRAR CLIENTE");

        String cpf = Entrada.texto("Digite o CPF: ");
        if (!cpfValido(cpf)) {
            System.out.println("CPF inválido. Use apenas números, pontos e traço.");
            return;
        }
        if (posicaoCPF(cpf) >= 0) {
            System.out.println("Já existe um cliente cadastrado com esse CPF.");
            return;
        }

        Cliente c = new Cliente();
        c.setCpf(cpf);
        c.lerDados();
        adiciona(c);
        salvar();
        System.out.println();
        System.out.println("Cliente cadastrado com sucesso!");
    }

    public void adiciona(Cliente c) {
        if (qtde == lista.length) {
            lista = Arrays.copyOf(lista, lista.length * 2);
        }
        lista[qtde] = c;
        qtde++;
    }

    // ---------- READ ----------
    public void imprime() {
        if (qtde == 0) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        for (int i = 0; i < qtde; i++) {
            lista[i].imprime();
        }
    }

    public Cliente buscarCPF(String cpf) {
        int posicao = posicaoCPF(cpf);
        if (posicao < 0) {
            return null;
        }
        return lista[posicao];
    }

    public void buscar() {
        Screen.titulo("BUSCAR CLIENTE");

        String cpf = Entrada.texto("Digite o CPF: ");
        Cliente c = buscarCPF(cpf);
        System.out.println();
        if (c == null) {
            System.out.println("Cliente não encontrado.");
        } else {
            c.imprime();
        }
    }

    // ---------- UPDATE ----------
    public void alterar() {
        Screen.titulo("ALTERAR CLIENTE");

        if (qtde == 0) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        imprime();
        System.out.println();

        String cpf = Entrada.texto("CPF do cliente a alterar: ");
        Cliente c = buscarCPF(cpf);
        if (c == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        System.out.println("(ENTER mantém o valor atual; o CPF não pode ser alterado)");
        c.alterarDados();
        salvar();
        System.out.println();
        System.out.println("Cliente alterado com sucesso!");
    }

    // ---------- DELETE ----------
    public void remover() {
        Screen.titulo("REMOVER CLIENTE");

        if (qtde == 0) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        imprime();
        System.out.println();

        String cpf = Entrada.texto("CPF do cliente a remover: ");
        int posicao = posicaoCPF(cpf);
        if (posicao < 0) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        if (Entrada.sim("Remover \"" + lista[posicao].getNome() + "\"?")) {
            removerItem(posicao);
            salvar();
            System.out.println();
            System.out.println("Cliente removido com sucesso!");
        } else {
            System.out.println("Remoção cancelada.");
        }
    }

    public void removerItem(int posicao) {
        for (int i = posicao; i < qtde - 1; i++) {
            lista[i] = lista[i + 1];
        }
        lista[qtde - 1] = null;
        qtde--;
    }

    // ---------- AUXILIARES ----------
    private int posicaoCPF(String cpf) {
        String digitos = soDigitos(cpf);
        if (digitos.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < qtde; i++) {
            if (soDigitos(lista[i].getCpf()).equals(digitos)) {
                return i;
            }
        }
        return -1;
    }

    private String soDigitos(String texto) {
        return texto.replaceAll("[^0-9]", "");
    }

    private boolean cpfValido(String cpf) {
        return cpf.matches("[0-9.\\-]+") && !soDigitos(cpf).isEmpty();
    }

    // ---------- ARQUIVO CSV ----------
    public void salvar() {
        Cliente clientes[] = new Cliente[qtde];
        for (int i = 0; i < qtde; i++) {
            clientes[i] = lista[i];
        }
        CsvUtil.salvar(clientes, ARQUIVO);
    }

    public void carregarDados() {
        Cliente[] clientes = CsvUtil.ler(ARQUIVO, Cliente.class);
        lista = new Cliente[Math.max(100, clientes.length)];
        for (int i = 0; i < clientes.length; i++) {
            lista[i] = clientes[i];
        }
        qtde = clientes.length;
    }
}
