package eduvfinance.repository;

import eduvfinance.model.Pagamento;

public class PagamentoRepository extends RepositorioArquivo<Pagamento> {
    public PagamentoRepository() { super("data/pagamentos.dat"); }
}
