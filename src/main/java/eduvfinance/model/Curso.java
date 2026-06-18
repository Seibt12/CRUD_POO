package eduvfinance.model;

import eduvfinance.util.Identificavel;

/**
 * Curso/trilha publicada na plataforma, com preco e status de aprovacao.
 */
public class Curso implements Identificavel {

    private int id;
    private String titulo;
    private String descricao;
    private double preco;
    private StatusCurso status;

    public Curso(String titulo, String descricao, double preco, StatusCurso status) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.preco = preco;
        this.status = status;
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public StatusCurso getStatus() { return status; }
    public void setStatus(StatusCurso status) { this.status = status; }

    @Override public String toString() { return titulo + " (" + status + ")"; }
}
