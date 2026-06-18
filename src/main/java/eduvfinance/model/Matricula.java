package eduvfinance.model;

import java.time.LocalDate;

import eduvfinance.util.Identificavel;
import eduvfinance.util.Validadores;

/**
 * Inscricao de um aprendiz em um curso. Guarda os ids relacionados
 * (idAprendiz e idCurso) como chaves simples, sem banco de dados.
 */
public class Matricula implements Identificavel {

    private int id;
    private LocalDate dataMatricula;
    private StatusMatricula status;
    private int progresso; // percentual concluido (0 a 100)
    private int idAprendiz;
    private int idCurso;

    public Matricula(LocalDate dataMatricula, StatusMatricula status,
                     int progresso, int idAprendiz, int idCurso) {
        this.dataMatricula = dataMatricula;
        this.status = status;
        this.progresso = progresso;
        this.idAprendiz = idAprendiz;
        this.idCurso = idCurso;
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }

    public LocalDate getDataMatricula() { return dataMatricula; }
    public void setDataMatricula(LocalDate dataMatricula) { this.dataMatricula = dataMatricula; }

    public String getDataMatriculaFormatada() { return Validadores.formatar(dataMatricula); }

    public StatusMatricula getStatus() { return status; }
    public void setStatus(StatusMatricula status) { this.status = status; }

    public int getProgresso() { return progresso; }
    public void setProgresso(int progresso) { this.progresso = progresso; }

    public int getIdAprendiz() { return idAprendiz; }
    public void setIdAprendiz(int idAprendiz) { this.idAprendiz = idAprendiz; }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    @Override public String toString() {
        return "Matricula #" + id + " (" + status + ", " + progresso + "%)";
    }
}
