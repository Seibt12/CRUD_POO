package eduvfinance.repository;

import eduvfinance.model.MetaFinanceira;

public class MetaFinanceiraRepository extends RepositorioArquivo<MetaFinanceira> {
    public MetaFinanceiraRepository() { super("data/metas.dat"); }
}
