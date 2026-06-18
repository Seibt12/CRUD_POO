package eduvfinance.repository;

import eduvfinance.model.Lancamento;

public class LancamentoRepository extends RepositorioArquivo<Lancamento> {
    public LancamentoRepository() { super("data/lancamentos.dat"); }
}
