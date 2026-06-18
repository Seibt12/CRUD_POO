package eduvfinance.model;

import eduvfinance.util.Identificavel;

public class Categoria implements Identificavel {

    private int id;
    private String nome;
    private String descricao;
    private String icone;

    public Categoria(String nome, String descricao, String icone) {
        this.nome = nome;
        this.descricao = descricao;
        this.icone = icone;
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }

    @Override public String toString() { return nome; }
}
