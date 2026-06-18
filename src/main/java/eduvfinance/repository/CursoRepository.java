package eduvfinance.repository;

import eduvfinance.model.Curso;

public class CursoRepository extends RepositorioArquivo<Curso> {
    public CursoRepository() { super("data/cursos.dat"); }
}
