package eduvfinance.model;

import java.time.LocalDate;

import eduvfinance.util.Identificavel;
import eduvfinance.util.Validadores;

public class Aprendiz implements Identificavel {

    private int id;
    private String nome;
    private String email;
    private String senha;
    private LocalDate dataNascimento;
    private String perfilInvestidor;

    public Aprendiz(String nome, String email, String senha,
                    LocalDate dataNascimento, String perfilInvestidor) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.perfilInvestidor = perfilInvestidor;
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getDataNascimentoFormatada() { return Validadores.formatar(dataNascimento); }

    public String getPerfilInvestidor() { return perfilInvestidor; }
    public void setPerfilInvestidor(String perfilInvestidor) { this.perfilInvestidor = perfilInvestidor; }

    @Override public String toString() { return nome + " (" + email + ")"; }
}
