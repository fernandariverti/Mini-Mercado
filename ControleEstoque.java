package superMarket;

public class ControleEstoque {
    private static final int ESTOQUE_BAIXO = 5;

    private ListaProdutos produtos;

    public ControleEstoque() {
        produtos = new ListaProdutos();
        produtos.carregarDados();
    }

    public void menu() {
        int op;
        do {
            Screen.clear();
            System.out.println("CONTROLE DE ESTOQUE");
            System.out.println("===================");
            System.out.println("1) LISTAR ESTOQUE");
            System.out.println("2) DAR ENTRADA (REPOR)");
            System.out.println("3) AJUSTAR QUANTIDADE");
            System.out.println("4) ESTOQUE BAIXO (até " + ESTOQUE_BAIXO + " unidades)");
            System.out.println("0) VOLTAR");
            op = Entrada.inteiro("Opção: ");

            switch (op) {
                case 1:
                    listar();
                    break;
                case 2:
                    repor();
                    break;
                case 3:
                    ajustar();
                    break;
                case 4:
                    estoqueBaixo();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

            if (op != 0) {
                Screen.pressEnterToContinue();
            }
        } while (op != 0);
    }

    private void listar() {
        Screen.titulo("ESTOQUE ATUAL");
        produtos.imprime();
    }

    private void repor() {
        Screen.titulo("ENTRADA DE ESTOQUE");
        if (produtos.qtde == 0) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        produtos.imprime();
        System.out.println();

        Produto p = escolherProduto();
        if (p == null) {
            return;
        }

        int qtd = Entrada.inteiro("Quantidade a adicionar: ");
        if (qtd <= 0) {
            System.out.println("A quantidade deve ser maior que zero.");
            return;
        }

        int antes = p.getEstoque();
        p.setEstoque(antes + qtd);
        produtos.salvar();
        System.out.println();
        System.out.println("Estoque de " + p.getNome() + ": " + antes + " -> " + p.getEstoque());
    }

    private void ajustar() {
        Screen.titulo("AJUSTAR ESTOQUE");
        if (produtos.qtde == 0) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        produtos.imprime();
        System.out.println();

        Produto p = escolherProduto();
        if (p == null) {
            return;
        }

        int qtd = Entrada.inteiro("Nova quantidade em estoque: ");
        if (qtd < 0) {
            System.out.println("O estoque não pode ser negativo.");
            return;
        }

        int antes = p.getEstoque();
        p.setEstoque(qtd);
        produtos.salvar();
        System.out.println();
        System.out.println("Estoque de " + p.getNome() + ": " + antes + " -> " + qtd);
    }

    private void estoqueBaixo() {
        Screen.titulo("ESTOQUE BAIXO");
        boolean achou = false;
        for (int i = 0; i < produtos.qtde; i++) {
            if (produtos.lista[i].getEstoque() <= ESTOQUE_BAIXO) {
                produtos.lista[i].imprime();
                achou = true;
            }
        }
        if (!achou) {
            System.out.println("Nenhum produto com estoque baixo.");
        }
    }

    private Produto escolherProduto() {
        int codigo = Entrada.inteiro("Código do produto (0 cancela): ");
        if (codigo == 0) {
            return null;
        }
        Produto p = produtos.buscarCodigo(codigo);
        if (p == null) {
            System.out.println("Produto não encontrado.");
        }
        return p;
    }
}
