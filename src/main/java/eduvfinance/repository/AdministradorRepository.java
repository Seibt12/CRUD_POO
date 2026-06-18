package eduvfinance.repository;

import eduvfinance.model.Administrador;

public class AdministradorRepository extends RepositorioArquivo<Administrador> {
    public AdministradorRepository() { super("data/administradores.dat"); }
}
