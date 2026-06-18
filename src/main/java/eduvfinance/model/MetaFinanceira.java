package eduvfinance.model;

import java.time.LocalDate;

import eduvfinance.util.Identificavel;
import eduvfinance.util.Validadores;

public class MetaFinanceira implements Identificavel {

    private int id;
    private String nome;
    private double valorAlvo;
    private LocalDate dataAlvo;
    private int prioridade;

    public MetaFinanceira(String nome, double valorAlvo, LocalDate dataAlvo, int prioridade) {
        this.nome = nome;
        this.valorAlvo = valorAlvo;
        this.dataAlvo = dataAlvo;
        this.prioridade = prioridade;
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getValorAlvo() { return valorAlvo; }
    public void setValorAlvo(double valorAlvo) { this.valorAlvo = valorAlvo; }

    public LocalDate getDataAlvo() { return dataAlvo; }
    public void setDataAlvo(LocalDate dataAlvo) { this.dataAlvo = dataAlvo; }

    public String getDataAlvoFormatada() { return Validadores.formatar(dataAlvo); }

    public int getPrioridade() { return prioridade; }
    public void setPrioridade(int prioridade) { this.prioridade = prioridade; }

    @Override public String toString() {
        return nome + " (R$ " + String.format("%.2f", valorAlvo) + ")";
    }
}
