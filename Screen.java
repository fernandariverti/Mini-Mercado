package superMarket;

import java.io.IOException;

public class Screen {

    public static void clear() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                // Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Linux / Unix / macOS
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            // Se não for possível limpar a tela, apenas pula linhas
            for (int i = 0; i < 30; i++) {
                System.out.println();
            }
        }
    }

    public static void titulo(String texto) {
        String linha = "====================================";
        int espacos = (linha.length() - texto.length()) / 2;
        if (espacos < 0) {
            espacos = 0;
        }
        clear();
        System.out.println(linha);
        for (int i = 0; i < espacos; i++) {
            System.out.print(" ");
        }
        System.out.println(texto);
        System.out.println(linha);
        System.out.println();
    }

    public static void pressEnterToContinue() {
        System.out.println();
        System.out.print("Pressione ENTER para continuar...");
        Entrada.SC.nextLine();
    }
}
