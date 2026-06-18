package eduvfinance.model;

import java.time.LocalDate;

import eduvfinance.util.Identificavel;
import eduvfinance.util.Validadores;

/**
 * Lancamento financeiro pessoal (receita, despesa ou transferencia),
 * associado a uma categoria (idCategoria como chave simples).
 */
public class Lancamento implements Identificavel {

    private int id;
    private TipoLancto tipo;
    private double valor;
    private LocalDate data;
    private Recorrencia recorrencia;
    private int idCategoria;

    public Lancamento(TipoLancto tipo, double valor, LocalDate data,
                      Recorrencia recorrencia, int idCategoria) {
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
        this.recorrencia = recorrencia;
        this.idCategoria = idCategoria;
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }

    public TipoLancto getTipo() { return tipo; }
    public void setTipo(TipoLancto tipo) { this.tipo = tipo; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getDataFormatada() { return Validadores.formatar(data); }

    public Recorrencia getRecorrencia() { return recorrencia; }
    public void setRecorrencia(Recorrencia recorrencia) { this.recorrencia = recorrencia; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    @Override public String toString() {
        return tipo + " R$ " + String.format("%.2f", valor);
    }
}
