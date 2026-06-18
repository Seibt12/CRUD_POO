package eduvfinance.repository;

import eduvfinance.model.Mentor;

public class MentorRepository extends RepositorioArquivo<Mentor> {
    public MentorRepository() { super("data/mentores.dat"); }
}
