package eduvfinance.repository;

import eduvfinance.model.Matricula;

public class MatriculaRepository extends RepositorioArquivo<Matricula> {
    public MatriculaRepository() { super("data/matriculas.dat"); }
}
