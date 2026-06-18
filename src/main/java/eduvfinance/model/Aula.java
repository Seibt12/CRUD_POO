package eduvfinance.model;

import eduvfinance.util.Identificavel;

public class Aula implements Identificavel {

    private int id;
    private String titulo;
    private String conteudo;
    private Nivel nivel;
    private int ordem;

    public Aula(String titulo, String conteudo, Nivel nivel, int ordem) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.nivel = nivel;
        this.ordem = ordem;
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public Nivel getNivel() { return nivel; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }

    public int getOrdem() { return ordem; }
    public void setOrdem(int ordem) { this.ordem = ordem; }

    @Override public String toString() { return ordem + " - " + titulo; }
}
