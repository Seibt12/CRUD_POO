package eduvfinance.repository;

import eduvfinance.model.Categoria;

public class CategoriaRepository extends RepositorioArquivo<Categoria> {
    public CategoriaRepository() { super("data/categorias.dat"); }
}
