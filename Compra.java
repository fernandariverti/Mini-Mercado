package superMarket;

public class Compra {
    private ListaProdutos produtos;
    private ListaClientes clientes;

    // Itens do carrinho (vetores paralelos: produto e quantidade)
    private Produto itens[];
    private int quantidades[];
    private int qtdeItens;

    public Compra() {
        produtos = new ListaProdutos();
        clientes = new ListaClientes();
    }

    public void realizar() {
        produtos.carregarDados();
        clientes.carregarDados();

        Screen.titulo("REALIZAR COMPRA");

        if (produtos.qtde == 0) {
            System.out.println("Não há produtos cadastrados.");
            return;
        }

        Cliente cliente = identificarCliente();

        itens = new Produto[produtos.qtde];
        quantidades = new int[produtos.qtde];
        qtdeItens = 0;

        montarCarrinho();

        if (qtdeItens == 0) {
            System.out.println();
            System.out.println("Compra cancelada: nenhum item foi adicionado.");
            return;
        }

        mostrarCupom(cliente);
        finalizar();
    }

    // Pergunta "Sou cadastrado?" e localiza o cliente pelo CPF
    private Cliente identificarCliente() {
        if (!Entrada.sim("Sou cadastrado?")) {
            return null;
        }

        while (true) {
            String cpf = Entrada.texto("CPF do cliente: ");
            Cliente c = clientes.buscarCPF(cpf);
            if (c != null) {
                System.out.println("Cliente: " + c.getNome());
                return c;
            }
            System.out.println("Cliente não encontrado.");
            if (!Entrada.sim("Deseja tentar novamente?")) {
                System.out.println("A compra seguirá sem cadastro.");
                return null;
            }
        }
    }

    private void montarCarrinho() {
        int codigo;
        do {
            System.out.println();
            produtos.imprime();
            System.out.println();
            codigo = Entrada.inteiro("Código do produto (0 para finalizar): ");
            if (codigo != 0) {
                adicionarItem(codigo);
            }
        } while (codigo != 0);
    }

    private void adicionarItem(int codigo) {
        Produto p = produtos.buscarCodigo(codigo);
        if (p == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        int qtd = Entrada.inteiro("Quantidade: ");
        if (qtd <= 0) {
            System.out.println("A quantidade deve ser maior que zero.");
            return;
        }

        int posicao = posicaoItem(p);
        int jaNoCarrinho = 0;
        if (posicao >= 0) {
            jaNoCarrinho = quantidades[posicao];
        }

        if (qtd + jaNoCarrinho > p.getEstoque()) {
            System.out.println("Estoque insuficiente! Disponível: " + (p.getEstoque() - jaNoCarrinho));
            return;
        }

        if (posicao >= 0) {
            quantidades[posicao] += qtd;
        } else {
            itens[qtdeItens] = p;
            quantidades[qtdeItens] = qtd;
            qtdeItens++;
        }
        System.out.println("Item adicionado.");
    }

    private int posicaoItem(Produto p) {
        for (int i = 0; i < qtdeItens; i++) {
            if (itens[i].getCodigo() == p.getCodigo()) {
                return i;
            }
        }
        return -1;
    }

    private void mostrarCupom(Cliente cliente) {
        double total = 0;

        System.out.println();
        System.out.println("============= CUPOM =============");
        if (cliente != null) {
            System.out.println("Cliente: " + cliente.getNome() + " (CPF " + cliente.getCpf() + ")");
        } else {
            System.out.println("Cliente: não cadastrado");
        }
        System.out.println("---------------------------------");

        for (int i = 0; i < qtdeItens; i++) {
            double subtotal = quantidades[i] * itens[i].getPreco();
            total += subtotal;
            System.out.printf("%s | qtd %d | unit. R$ %.2f | subtotal R$ %.2f%n",
                    itens[i].getNome(), quantidades[i], itens[i].getPreco(), subtotal);
        }

        System.out.println("---------------------------------");
        System.out.printf("TOTAL DA COMPRA: R$ %.2f%n", total);
        System.out.println();
    }

    private void finalizar() {
        if (!Entrada.sim("Confirmar compra?")) {
            System.out.println("Compra cancelada. O estoque não foi alterado.");
            return;
        }

        System.out.println();
        for (int i = 0; i < qtdeItens; i++) {
            int antes = itens[i].getEstoque();
            int depois = antes - quantidades[i];
            itens[i].setEstoque(depois);
            System.out.println("Estoque de " + itens[i].getNome() + ": " + antes + " -> " + depois);
        }
        produtos.salvar();
        System.out.println();
        System.out.println("Compra concluída com sucesso! Estoque atualizado.");
    }
}
