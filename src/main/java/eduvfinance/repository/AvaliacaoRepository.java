package eduvfinance.repository;

import eduvfinance.model.Avaliacao;

public class AvaliacaoRepository extends RepositorioArquivo<Avaliacao> {
    public AvaliacaoRepository() { super("data/avaliacoes.dat"); }
}
