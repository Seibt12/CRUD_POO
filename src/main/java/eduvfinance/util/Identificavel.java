package eduvfinance.util;

/**
 * Contrato de identidade. Toda entidade persistivel tem um id gerenciado
 * pelo repositorio. Estende Serializable para permitir gravacao em arquivo.
 */
public interface Identificavel extends java.io.Serializable {
    int getId();
    void setId(int id);
}
