package eduvfinance.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Conjunto de validadores reutilizados por todas as telas.
 * Centraliza o tratamento de texto, numeros e datas no formato pt-BR.
 */
public final class Validadores {

    private Validadores() { } // classe utilitaria: nao instanciar

    private static final DateTimeFormatter BR =
        DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    /** Garante que o campo de texto nao esta vazio. */
    public static String texto(String campo, String v) throws ValidacaoException {
        if (v == null || v.isBlank()) throw new ValidacaoException(campo + " e obrigatorio.");
        return v.trim();
    }

    /** Converte texto em numero >= 0, aceitando virgula como separador decimal. */
    public static double numeroPositivo(String campo, String v) throws ValidacaoException {
        try {
            if (v == null || v.isBlank()) throw new ValidacaoException(campo + " e obrigatorio.");
            double n = Double.parseDouble(v.replace(",", "."));
            if (n < 0) throw new ValidacaoException(campo + " nao pode ser negativo.");
            return n;
        } catch (NumberFormatException e) {
            throw new ValidacaoException(campo + " deve ser um numero valido.");
        }
    }

    /** Converte texto em inteiro >= 0. */
    public static int inteiroPositivo(String campo, String v) throws ValidacaoException {
        try {
            if (v == null || v.isBlank()) throw new ValidacaoException(campo + " e obrigatorio.");
            int n = Integer.parseInt(v.trim());
            if (n < 0) throw new ValidacaoException(campo + " nao pode ser negativo.");
            return n;
        } catch (NumberFormatException e) {
            throw new ValidacaoException(campo + " deve ser um numero inteiro valido.");
        }
    }

    /** Faz o parse de uma data no formato DD/MM/AAAA com validacao estrita. */
    public static LocalDate data(String campo, String v) throws ValidacaoException {
        try { return LocalDate.parse(v, BR); }
        catch (DateTimeParseException e) {
            throw new ValidacaoException(campo + " deve estar no formato DD/MM/AAAA.");
        }
    }

    /** Formata uma data para exibicao na tabela (DD/MM/AAAA). */
    public static String formatar(LocalDate d) { return d == null ? "" : d.format(BR); }
}
