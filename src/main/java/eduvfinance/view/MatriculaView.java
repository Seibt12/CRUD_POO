package eduvfinance.view;

import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import eduvfinance.model.Matricula;
import eduvfinance.model.StatusMatricula;
import eduvfinance.repository.MatriculaRepository;
import eduvfinance.util.Alertas;
import eduvfinance.util.ValidacaoException;
import eduvfinance.util.Validadores;

/**
 * Tela CRUD de Matricula (inscricao de um aprendiz em um curso).
 */
public class MatriculaView {

    private final MatriculaRepository repo = new MatriculaRepository();
    private final TableView<Matricula> tabela = new TableView<>();
    private final TextField campoData      = new TextField();
    private final ComboBox<StatusMatricula> campoStatus = new ComboBox<>();
    private final TextField campoProgresso = new TextField();
    private final TextField campoIdAprendiz = new TextField();
    private final TextField campoIdCurso   = new TextField();
    private Integer idEmEdicao = null;

    public Parent build() {
        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(15));
        campoData.setPromptText("DD/MM/AAAA");
        campoProgresso.setPromptText("0 a 100");
        campoStatus.getItems().addAll(StatusMatricula.values());

        form.addRow(0, new Label("Data matricula:"), campoData);
        form.addRow(1, new Label("Status:"),         campoStatus);
        form.addRow(2, new Label("Progresso (%):"),  campoProgresso);
        form.addRow(3, new Label("ID Aprendiz:"),    campoIdAprendiz);
        form.addRow(4, new Label("ID Curso:"),       campoIdCurso);

        Button salvar  = new Button("Salvar");
        Button limpar  = new Button("Limpar");
        Button excluir = new Button("Excluir selecionado");
        form.add(new HBox(10, salvar, limpar, excluir), 1, 5);

        TableColumn<Matricula, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Matricula, String> cData = new TableColumn<>("Data");
        cData.setCellValueFactory(new PropertyValueFactory<>("dataMatriculaFormatada"));
        TableColumn<Matricula, StatusMatricula> cStatus = new TableColumn<>("Status");
        cStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<Matricula, Integer> cProg = new TableColumn<>("Progresso");
        cProg.setCellValueFactory(new PropertyValueFactory<>("progresso"));
        TableColumn<Matricula, Integer> cAp = new TableColumn<>("ID Aprendiz");
        cAp.setCellValueFactory(new PropertyValueFactory<>("idAprendiz"));
        TableColumn<Matricula, Integer> cCur = new TableColumn<>("ID Curso");
        cCur.setCellValueFactory(new PropertyValueFactory<>("idCurso"));
        tabela.getColumns().addAll(cId, cData, cStatus, cProg, cAp, cCur);
        atualizarTabela();

        tabela.getSelectionModel().selectedItemProperty().addListener((o, a, sel) -> {
            if (sel != null) {
                idEmEdicao = sel.getId();
                campoData.setText(sel.getDataMatriculaFormatada());
                campoStatus.setValue(sel.getStatus());
                campoProgresso.setText(String.valueOf(sel.getProgresso()));
                campoIdAprendiz.setText(String.valueOf(sel.getIdAprendiz()));
                campoIdCurso.setText(String.valueOf(sel.getIdCurso()));
            }
        });

        salvar.setOnAction(e -> salvar());
        limpar.setOnAction(e -> limparFormulario());
        excluir.setOnAction(e -> excluir());

        VBox root = new VBox(10, form, tabela);
        root.setPadding(new Insets(15));
        return root;
    }

    private void salvar() {
        try {
            LocalDate data = Validadores.data("Data matricula", campoData.getText());
            if (campoStatus.getValue() == null)
                throw new ValidacaoException("Selecione o status da matricula.");
            StatusMatricula status = campoStatus.getValue();
            int progresso  = Validadores.inteiroPositivo("Progresso", campoProgresso.getText());
            if (progresso > 100) throw new ValidacaoException("Progresso deve ser de 0 a 100.");
            int idAprendiz = Validadores.inteiroPositivo("ID Aprendiz", campoIdAprendiz.getText());
            int idCurso    = Validadores.inteiroPositivo("ID Curso", campoIdCurso.getText());

            if (idEmEdicao == null) {
                repo.inserir(new Matricula(data, status, progresso, idAprendiz, idCurso));
                Alertas.sucesso("Matricula inserida.");
            } else {
                Matricula m = new Matricula(data, status, progresso, idAprendiz, idCurso);
                m.setId(idEmEdicao);
                repo.atualizar(m);
                Alertas.sucesso("Matricula atualizada.");
            }
            limparFormulario();
            atualizarTabela();
        } catch (ValidacaoException ex) {
            Alertas.erro(ex.getMessage());
        }
    }

    private void excluir() {
        Matricula sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) { Alertas.erro("Selecione um registro na tabela."); return; }
        repo.excluir(sel.getId());
        Alertas.sucesso("Matricula excluida.");
        limparFormulario();
        atualizarTabela();
    }

    private void atualizarTabela() { tabela.getItems().setAll(repo.listar()); }

    private void limparFormulario() {
        idEmEdicao = null;
        campoData.clear(); campoProgresso.clear();
        campoIdAprendiz.clear(); campoIdCurso.clear();
        campoStatus.setValue(null);
        tabela.getSelectionModel().clearSelection();
    }
}
