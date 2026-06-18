package eduvfinance.repository;

import eduvfinance.model.Aula;

public class AulaRepository extends RepositorioArquivo<Aula> {
    public AulaRepository() { super("data/aulas.dat"); }
}
