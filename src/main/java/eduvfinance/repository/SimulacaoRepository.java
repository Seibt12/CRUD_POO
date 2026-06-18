package eduvfinance.repository;

import eduvfinance.model.Simulacao;

public class SimulacaoRepository extends RepositorioArquivo<Simulacao> {
    public SimulacaoRepository() { super("data/simulacoes.dat"); }
}
