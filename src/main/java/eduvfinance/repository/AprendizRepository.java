package eduvfinance.repository;

import eduvfinance.model.Aprendiz;

public class AprendizRepository extends RepositorioArquivo<Aprendiz> {
    public AprendizRepository() { super("data/aprendizes.dat"); }
}
