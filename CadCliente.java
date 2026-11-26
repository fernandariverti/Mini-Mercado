package superMarket;

public class CadCliente {
    ListaClientes lista;

    public CadCliente() {
        lista = new ListaClientes();
        lista.carregarDados();
    }

    public void menu() {
        int op;
        do {
            Screen.clear();
            System.out.println("CADASTRO DE CLIENTES");
            System.out.println("====================");
            System.out.println("1) CADASTRAR CLIENTE");
            System.out.println("2) LISTAR CLIENTES");
            System.out.println("3) BUSCAR CLIENTE");
            System.out.println("4) ALTERAR CLIENTE");
            System.out.println("5) REMOVER CLIENTE");
            System.out.println("0) VOLTAR");
            op = Entrada.inteiro("Opção: ");

            switch (op) {
                case 1:
                    lista.cadastrar();
                    break;
                case 2:
                    listar();
                    break;
                case 3:
                    lista.buscar();
                    break;
                case 4:
                    lista.alterar();
                    break;
                case 5:
                    lista.remover();
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
        Screen.titulo("LISTA DE CLIENTES");
        lista.imprime();
    }
}
