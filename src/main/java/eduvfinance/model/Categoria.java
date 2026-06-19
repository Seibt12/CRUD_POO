package eduvfinance.model;

import eduvfinance.util.Identificavel;

public class Categoria implements Identificavel {

    private int id;
    private String nome;
    private TipoLancto tipoPadrao;
    private String icone;

    public Categoria(String nome, TipoLancto tipoPadrao, String icone) {
        this.nome = nome;
        this.tipoPadrao = tipoPadrao;
        this.icone = icone;
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public TipoLancto getTipoPadrao() { return tipoPadrao; }
    public void setTipoPadrao(TipoLancto tipoPadrao) { this.tipoPadrao = tipoPadrao; }

    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }

    @Override public String toString() { return nome; }
}
