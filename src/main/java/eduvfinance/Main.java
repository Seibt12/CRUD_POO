package eduvfinance;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

public class Main extends Application {

    private final AprendizView aprendizView = new AprendizView();
    private final MentorView mentorView = new MentorView();
    private final CursoView cursoView = new CursoView();
    private final AulaView aulaView = new AulaView();
    private final MatriculaView matriculaView = new MatriculaView();
    private final PagamentoView pagamentoView = new PagamentoView();
    private final LancamentoView lancamentoView = new LancamentoView();
    private final CategoriaView categoriaView = new CategoriaView();
    private final MetaFinanceiraView metaFinanceiraView = new MetaFinanceiraView();
    private final SimulacaoView simulacaoView = new SimulacaoView();
    private final AvaliacaoView avaliacaoView = new AvaliacaoView();
    private final AdministradorView administradorView = new AdministradorView();

    @Override
    public void start(Stage stage) {
        TabPane abas = new TabPane();
        abas.getTabs().addAll(
            aba("Aprendizes",  aprendizView.build()),
            aba("Mentores",    mentorView.build()),
            aba("Cursos",      cursoView.build()),
            aba("Aulas",       aulaView.build()),
            aba("Matriculas",  matriculaView.build()),
            aba("Pagamentos",  pagamentoView.build()),
            aba("Lancamentos", lancamentoView.build()),
            aba("Categorias",  categoriaView.build()),
            aba("Metas",       metaFinanceiraView.build()),
            aba("Simulacoes",  simulacaoView.build()),
            aba("Avaliacoes",  avaliacaoView.build()),
            aba("Admins",      administradorView.build())
        );

        Button atualizarTudo = new Button("Atualizar");
        atualizarTudo.setOnAction(e -> atualizarTudo());
        HBox barraSuperior = new HBox(atualizarTudo);
        barraSuperior.setPadding(new Insets(10));
        barraSuperior.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(barraSuperior, abas);
        stage.setScene(new Scene(root, 900, 600));
        stage.setTitle("EduvFinance Desktop - RA3");
        stage.show();
    }

    private void atualizarTudo() {
        aprendizView.atualizar();
        mentorView.atualizar();
        cursoView.atualizar();
        aulaView.atualizar();
        matriculaView.atualizar();
        pagamentoView.atualizar();
        lancamentoView.atualizar();
        categoriaView.atualizar();
        metaFinanceiraView.atualizar();
        simulacaoView.atualizar();
        avaliacaoView.atualizar();
        administradorView.atualizar();
    }

    private Tab aba(String titulo, Parent conteudo) {
        Tab t = new Tab(titulo, conteudo);
        t.setClosable(false);
        return t;
    }

    public static void main(String[] args) { launch(args); }
}
