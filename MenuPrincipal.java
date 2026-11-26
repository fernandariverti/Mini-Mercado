package superMarket;

public class MenuPrincipal {

    public MenuPrincipal() {

    }

    public void mostrar() {
        int op;
        do {
            Screen.clear();
            System.out.println("***************************");
            System.out.println("* SUPER MARKET VERSÃO 1.0 *");
            System.out.println("***************************");
            System.out.println("1) PRODUTOS");
            System.out.println("2) CLIENTES");
            System.out.println("3) REALIZAR COMPRA");
            System.out.println("4) CONTROLE DE ESTOQUE");
            System.out.println("0) SAIR");
            op = Entrada.inteiro("Opção: ");

            switch (op) {
                case 1:
                    CadProduto cadProduto = new CadProduto();
                    cadProduto.menu();
                    break;
                case 2:
                    CadCliente cadCliente = new CadCliente();
                    cadCliente.menu();
                    break;
                case 3:
                    Compra compra = new Compra();
                    compra.realizar();
                    Screen.pressEnterToContinue();
                    break;
                case 4:
                    ControleEstoque controle = new ControleEstoque();
                    controle.menu();
                    break;
                case 0:
                    System.out.println("Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida.");
                    Screen.pressEnterToContinue();
            }
        } while (op != 0);
    }
}
