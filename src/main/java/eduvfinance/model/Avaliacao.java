package eduvfinance.model;

import java.time.LocalDate;

import eduvfinance.util.Identificavel;
import eduvfinance.util.Validadores;

public class Avaliacao implements Identificavel {

    private int id;
    private int nota;
    private String comentario;
    private LocalDate data;
    private int idCurso;

    public Avaliacao(int nota, String comentario, LocalDate data, int idCurso) {
        this.nota = nota;
        this.comentario = comentario;
        this.data = data;
        this.idCurso = idCurso;
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }

    public int getNota() { return nota; }
    public void setNota(int nota) { this.nota = nota; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getDataFormatada() { return Validadores.formatar(data); }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    @Override public String toString() { return "Nota " + nota + " - curso #" + idCurso; }
}
