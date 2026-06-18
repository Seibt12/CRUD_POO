package eduvfinance.util;

/**
 * Excecao verificada lancada quando a entrada do usuario nao passa nas
 * validacoes (texto vazio, numero invalido, data fora do formato, etc.).
 */
public class ValidacaoException extends Exception {
    public ValidacaoException(String msg) { super(msg); }
}
