package eduvfinance;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import eduvfinance.view.AdministradorView;
import eduvfinance.view.AprendizView;
import eduvfinance.view.AulaView;
import eduvfinance.view.AvaliacaoView;
import eduvfinance.view.CategoriaView;
import eduvfinance.view.CursoView;
import eduvfinance.view.LancamentoView;
import eduvfinance.view.MatriculaView;
import eduvfinance.view.MentorView;
import eduvfinance.view.MetaFinanceiraView;
import eduvfinance.view.PagamentoView;
import eduvfinance.view.SimulacaoView;

/**
 * Classe principal do EduvFinance Desktop.
 * Monta um TabPane com uma aba para cada entidade de dominio (CRUD).
 * Toda a interface e criada por codigo (sem FXML, sem SceneBuilder).
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        TabPane abas = new TabPane();
        abas.getTabs().addAll(
            aba("Aprendizes",  new AprendizView().build()),
            aba("Mentores",    new MentorView().build()),
            aba("Cursos",      new CursoView().build()),
            aba("Aulas",       new AulaView().build()),
            aba("Matriculas",  new MatriculaView().build()),
            aba("Pagamentos",  new PagamentoView().build()),
            aba("Lancamentos", new LancamentoView().build()),
            aba("Categorias",  new CategoriaView().build()),
            aba("Metas",       new MetaFinanceiraView().build()),
            aba("Simulacoes",  new SimulacaoView().build()),
            aba("Avaliacoes",  new AvaliacaoView().build()),
            aba("Admins",      new AdministradorView().build())
        );
        stage.setScene(new Scene(abas, 900, 600));
        stage.setTitle("EduvFinance Desktop - RA3");
        stage.show();
    }

    private Tab aba(String titulo, Parent conteudo) {
        Tab t = new Tab(titulo, conteudo);
        t.setClosable(false);
        return t;
    }

    public static void main(String[] args) { launch(args); }
}
