package eduvfinance.model;

import java.time.LocalDate;

import eduvfinance.util.Identificavel;
import eduvfinance.util.Validadores;

/**
 * Pagamento associado a uma matricula (idMatricula como chave simples).
 */
public class Pagamento implements Identificavel {

    private int id;
    private double valor;
    private LocalDate dataPagamento;
    private MetodoPagto metodo;
    private int idMatricula;

    public Pagamento(double valor, LocalDate dataPagamento, MetodoPagto metodo, int idMatricula) {
        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.metodo = metodo;
        this.idMatricula = idMatricula;
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }

    public String getDataPagamentoFormatada() { return Validadores.formatar(dataPagamento); }

    public MetodoPagto getMetodo() { return metodo; }
    public void setMetodo(MetodoPagto metodo) { this.metodo = metodo; }

    public int getIdMatricula() { return idMatricula; }
    public void setIdMatricula(int idMatricula) { this.idMatricula = idMatricula; }

    @Override public String toString() {
        return "Pagamento #" + id + " R$ " + String.format("%.2f", valor) + " (" + metodo + ")";
    }
}
