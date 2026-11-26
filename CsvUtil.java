package superMarket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CsvUtil {

    // =========================
    // SALVAR ARRAY DE OBJETOS
    // =========================
    public static void salvar(Object[] objetos, String nomeArquivo) {
        if (objetos == null) {
            return;
        }

        Class<?> clazz = objetos.getClass().getComponentType();
        Field[] fields = clazz.getDeclaredFields();

        try {
            Path caminho = Paths.get(nomeArquivo);
            if (caminho.getParent() != null) {
                Files.createDirectories(caminho.getParent());
            }

            try (PrintWriter writer = new PrintWriter(
                    Files.newBufferedWriter(caminho, StandardCharsets.UTF_8))) {

                // Cabeçalho (sempre gravado, mesmo com a lista vazia)
                for (int i = 0; i < fields.length; i++) {
                    writer.print(fields[i].getName());
                    if (i < fields.length - 1) {
                        writer.print(";");
                    }
                }
                writer.println();

                // Dados
                for (Object obj : objetos) {
                    if (obj == null) {
                        continue;
                    }
                    for (int i = 0; i < fields.length; i++) {
                        fields[i].setAccessible(true);
                        writer.print(formatar(fields[i].get(obj)));
                        if (i < fields.length - 1) {
                            writer.print(";");
                        }
                    }
                    writer.println();
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao salvar " + nomeArquivo + ": " + e.getMessage());
        }
    }

    private static String formatar(Object valor) {
        if (valor == null) {
            return "";
        }
        if (valor instanceof Double) {
            // Sempre com ponto e duas casas (ex.: 5.50)
            return String.format(Locale.US, "%.2f", (Double) valor);
        }
        // Evita que ';' ou quebra de linha no texto quebrem o CSV
        return String.valueOf(valor).replace(';', ',').replace('\n', ' ').replace('\r', ' ');
    }

    // =========================
    // LER CSV E RETORNAR ARRAY
    // =========================
    public static <T> T[] ler(String nomeArquivo, Class<T> entity) {
        List<T> lista = new ArrayList<>();
        Path caminho = Paths.get(nomeArquivo);

        if (Files.exists(caminho)) {
            try (BufferedReader reader = Files.newBufferedReader(caminho, StandardCharsets.UTF_8)) {

                String header = reader.readLine();
                if (header != null) {
                    // Remove BOM (Excel/Bloco de Notas) caso exista
                    header = header.replace("\uFEFF", "").trim();
                    String[] nomesCampos = header.split(";", -1);

                    String linha;
                    int numeroLinha = 1;
                    while ((linha = reader.readLine()) != null) {
                        numeroLinha++;
                        if (linha.trim().isEmpty()) {
                            continue;
                        }
                        try {
                            lista.add(lerLinha(linha, nomesCampos, entity));
                        } catch (Exception e) {
                            System.out.println("Linha " + numeroLinha + " de " + nomeArquivo
                                    + " ignorada (dados inválidos).");
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Não foi possível ler " + nomeArquivo + ": " + e.getMessage());
            }
        }

        // Converter List -> Array (vazio se não houver dados)
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(entity, lista.size());
        return lista.toArray(array);
    }

    private static <T> T lerLinha(String linha, String[] nomesCampos, Class<T> entity)
            throws Exception {
        String[] valores = linha.split(";", -1);
        T obj = entity.getDeclaredConstructor().newInstance();

        for (int i = 0; i < nomesCampos.length && i < valores.length; i++) {
            Field field;
            try {
                field = entity.getDeclaredField(nomesCampos[i].trim());
            } catch (NoSuchFieldException e) {
                continue; // coluna desconhecida: ignora
            }
            field.setAccessible(true);

            Class<?> tipo = field.getType();
            String valor = valores[i].trim();

            // Conversões básicas
            if (tipo == int.class || tipo == Integer.class) {
                field.set(obj, valor.isEmpty() ? 0 : Integer.parseInt(valor));
            } else if (tipo == double.class || tipo == Double.class) {
                field.set(obj, valor.isEmpty() ? 0.0 : Double.parseDouble(valor.replace(',', '.')));
            } else if (tipo == boolean.class || tipo == Boolean.class) {
                field.set(obj, Boolean.parseBoolean(valor));
            } else {
                field.set(obj, valor);
            }
        }
        return obj;
    }
}
