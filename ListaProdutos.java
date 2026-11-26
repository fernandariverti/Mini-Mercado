package superMarket;

import java.util.Arrays;

public class ListaProdutos {
    private static final String ARQUIVO = "./superMarket/produtos.csv";

    public Produto lista[];
    public int qtde;

    public ListaProdutos() {
        lista = new Produto[100];
        qtde = 0;
    }

    // ---------- CREATE ----------
    public void cadastrar() {
        Screen.titulo("CADASTRAR PRODUTO");

        String nome = Entrada.texto("Digite o Nome: ");
        if (nome.isEmpty()) {
            System.out.println("O nome não pode ser vazio.");
            return;
        }

        double preco = Entrada.decimal("Digite o Preço: R$ ");
        if (preco <= 0) {
            System.out.println("O preço deve ser maior que zero.");
            return;
        }

        int estoque = Entrada.inteiro("Digite a Quantidade em estoque: ");
        if (estoque < 0) {
            System.out.println("O estoque não pode ser negativo.");
            return;
        }

        Produto p = new Produto(proximoCodigo(), nome, preco, estoque);
        adiciona(p);
        salvar();
        System.out.println();
        System.out.println("Produto cadastrado com sucesso! Código: " + p.getCodigo());
    }

    public void adiciona(Produto p) {
        if (qtde == lista.length) {
            lista = Arrays.copyOf(lista, lista.length * 2);
        }
        lista[qtde] = p;
        qtde++;
    }

    public int proximoCodigo() {
        int maior = 0;
        for (int i = 0; i < qtde; i++) {
            if (lista[i].getCodigo() > maior) {
                maior = lista[i].getCodigo();
            }
        }
        return maior + 1;
    }

    // ---------- READ ----------
    public void imprime() {
        if (qtde == 0) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        for (int i = 0; i < qtde; i++) {
            lista[i].imprime();
        }
    }

    public Produto buscarCodigo(int codigo) {
        for (int i = 0; i < qtde; i++) {
            if (lista[i].getCodigo() == codigo) {
                return lista[i];
            }
        }
        return null;
    }

    public void buscar() {
        Screen.titulo("BUSCAR PRODUTO");

        String termo = Entrada.texto("Digite o código ou parte do nome: ");
        if (termo.isEmpty()) {
            System.out.println("Nenhum termo informado.");
            return;
        }

        System.out.println();
        boolean achou = false;
        for (int i = 0; i < qtde; i++) {
            Produto p = lista[i];
            boolean mesmoCodigo = String.valueOf(p.getCodigo()).equals(termo);
            boolean mesmoNome = p.getNome().toLowerCase().contains(termo.toLowerCase());
            if (mesmoCodigo || mesmoNome) {
                p.imprime();
                achou = true;
            }
        }
        if (!achou) {
            System.out.println("Produto não encontrado.");
        }
    }

    // ---------- UPDATE ----------
    public void alterar() {
        Screen.titulo("ALTERAR PRODUTO");

        if (qtde == 0) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        imprime();
        System.out.println();

        int codigo = Entrada.inteiro("Código do produto: ");
        Produto p = buscarCodigo(codigo);
        if (p == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.println("(ENTER mantém o valor atual)");

        String nome = Entrada.texto("Nome (" + p.getNome() + "): ");
        if (!nome.isEmpty()) {
            p.setNome(nome);
        }

        String valorPreco = Entrada.texto("Preço (R$ " + String.format("%.2f", p.getPreco()) + "): ");
        if (!valorPreco.isEmpty()) {
            try {
                double preco = Double.parseDouble(valorPreco.replace(',', '.'));
                if (preco <= 0) {
                    System.out.println("O preço deve ser maior que zero. Valor anterior mantido.");
                } else {
                    p.setPreco(preco);
                }
            } catch (NumberFormatException e) {
                System.out.println("Preço inválido. Valor anterior mantido.");
            }
        }

        String valorEstoque = Entrada.texto("Estoque (" + p.getEstoque() + "): ");
        if (!valorEstoque.isEmpty()) {
            try {
                int estoque = Integer.parseInt(valorEstoque);
                if (estoque < 0) {
                    System.out.println("O estoque não pode ser negativo. Valor anterior mantido.");
                } else {
                    p.setEstoque(estoque);
                }
            } catch (NumberFormatException e) {
                System.out.println("Estoque inválido. Valor anterior mantido.");
            }
        }

        salvar();
        System.out.println();
        System.out.println("Produto alterado com sucesso!");
    }

    // ---------- DELETE ----------
    public void remover() {
        Screen.titulo("REMOVER PRODUTO");

        if (qtde == 0) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        imprime();
        System.out.println();

        int codigo = Entrada.inteiro("Código do produto: ");
        int posicao = posicaoCodigo(codigo);
        if (posicao < 0) {
            System.out.println("Produto não encontrado.");
            return;
        }

        if (Entrada.sim("Remover \"" + lista[posicao].getNome() + "\"?")) {
            removerItem(posicao);
            salvar();
            System.out.println();
            System.out.println("Produto removido com sucesso!");
        } else {
            System.out.println("Remoção cancelada.");
        }
    }

    private int posicaoCodigo(int codigo) {
        for (int i = 0; i < qtde; i++) {
            if (lista[i].getCodigo() == codigo) {
                return i;
            }
        }
        return -1;
    }

    public void removerItem(int posicao) {
        for (int i = posicao; i < qtde - 1; i++) {
            lista[i] = lista[i + 1];
        }
        lista[qtde - 1] = null;
        qtde--;
    }

    // ---------- ARQUIVO CSV ----------
    public void salvar() {
        Produto prods[] = new Produto[qtde];
        for (int i = 0; i < qtde; i++) {
            prods[i] = lista[i];
        }
        CsvUtil.salvar(prods, ARQUIVO);
    }

    public void carregarDados() {
        Produto[] prods = CsvUtil.ler(ARQUIVO, Produto.class);
        lista = new Produto[Math.max(100, prods.length)];
        for (int i = 0; i < prods.length; i++) {
            lista[i] = prods[i];
        }
        qtde = prods.length;
        corrigirCodigos();
    }

    // Garante que todo produto tenha um código único (ex.: CSV antigo só com nome;preco)
    private void corrigirCodigos() {
        boolean alterou = false;
        for (int i = 0; i < qtde; i++) {
            boolean repetido = false;
            for (int j = 0; j < i; j++) {
                if (lista[j].getCodigo() == lista[i].getCodigo()) {
                    repetido = true;
                }
            }
            if (lista[i].getCodigo() <= 0 || repetido) {
                lista[i].setCodigo(proximoCodigo());
                alterou = true;
            }
        }
        if (alterou) {
            salvar();
        }
    }
}
