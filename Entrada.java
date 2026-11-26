package superMarket;

import java.util.Scanner;

/**
 * Leitura de dados do teclado. Existe um único Scanner em todo o programa,
 * evitando problemas de buffer ao misturar nextInt() e nextLine().
 */
public class Entrada {

    public static final Scanner SC = new Scanner(System.in);

    public static String texto(String pergunta) {
        System.out.print(pergunta);
        if (!SC.hasNextLine()) {
            System.out.println();
            System.out.println("Entrada encerrada. Até logo!");
            System.exit(0);
        }
        return SC.nextLine().trim();
    }

    public static int inteiro(String pergunta) {
        while (true) {
            try {
                return Integer.parseInt(texto(pergunta));
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    public static double decimal(String pergunta) {
        while (true) {
            try {
                return Double.parseDouble(texto(pergunta).replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Digite um valor numérico válido.");
            }
        }
    }

    public static boolean sim(String pergunta) {
        String resposta = texto(pergunta + " (S/N): ");
        return resposta.equalsIgnoreCase("S") || resposta.equalsIgnoreCase("SIM");
    }
}
