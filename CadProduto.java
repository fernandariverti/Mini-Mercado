package superMarket;

public class CadProduto {
    ListaProdutos listaProd;

    public CadProduto() {
        listaProd = new ListaProdutos();
        listaProd.carregarDados();
    }

    public void menu() {
        int op;
        do {
            Screen.clear();
            System.out.println("CADASTRO DE PRODUTOS");
            System.out.println("====================");
            System.out.println("1) CADASTRAR PRODUTO");
            System.out.println("2) LISTAR PRODUTOS");
            System.out.println("3) BUSCAR PRODUTO");
            System.out.println("4) ALTERAR PRODUTO");
            System.out.println("5) REMOVER PRODUTO");
            System.out.println("0) VOLTAR");
            op = Entrada.inteiro("Opção: ");

            switch (op) {
                case 1:
                    listaProd.cadastrar();
                    break;
                case 2:
                    listar();
                    break;
                case 3:
                    listaProd.buscar();
                    break;
                case 4:
                    listaProd.alterar();
                    break;
                case 5:
                    listaProd.remover();
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

    public void listar() {
        Screen.titulo("LISTA DE PRODUTOS");
        listaProd.imprime();
    }
}
