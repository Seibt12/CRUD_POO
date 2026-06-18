package eduvfinance.model;

import java.time.LocalDate;

import eduvfinance.util.Identificavel;
import eduvfinance.util.Validadores;

public class Administrador implements Identificavel {

    private int id;
    private String nome;
    private String email;
    private String senha;
    private LocalDate dataNascimento;

    public Administrador(String nome, String email, String senha, LocalDate dataNascimento) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
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

    @Override public String toString() { return nome + " (" + email + ")"; }
}
