package eduvfinance.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import javafx.util.StringConverter;

public final class Validadores {

    private Validadores() { }

    private static final DateTimeFormatter BR =
        DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public static String texto(String campo, String v) throws ValidacaoException {
        if (v == null || v.isBlank()) throw new ValidacaoException(campo + " e obrigatorio.");
        return v.trim();
    }

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

    public static LocalDate data(String campo, String v) throws ValidacaoException {
        try { return LocalDate.parse(v, BR); }
        catch (DateTimeParseException e) {
            throw new ValidacaoException(campo + " deve estar no formato DD/MM/AAAA.");
        }
    }

    public static String formatar(LocalDate d) { return d == null ? "" : d.format(BR); }

    public static StringConverter<LocalDate> conversorData() {
        return new StringConverter<LocalDate>() {
            @Override public String toString(LocalDate d) { return d == null ? "" : d.format(BR); }
            @Override public LocalDate fromString(String s) {
                if (s == null || s.isBlank()) return null;
                try { return LocalDate.parse(s, BR); } catch (Exception e) { return null; }
            }
        };
    }
}
