package eduvfinance.view;

import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import eduvfinance.model.Aprendiz;
import eduvfinance.model.Curso;
import eduvfinance.model.Matricula;
import eduvfinance.model.StatusMatricula;
import eduvfinance.repository.AprendizRepository;
import eduvfinance.repository.CursoRepository;
import eduvfinance.repository.MatriculaRepository;
import eduvfinance.util.Alertas;
import eduvfinance.util.ValidacaoException;
import eduvfinance.util.Validadores;

public class MatriculaView {

    private final MatriculaRepository repo = new MatriculaRepository();
    private final AprendizRepository aprendizRepo = new AprendizRepository();
    private final CursoRepository cursoRepo = new CursoRepository();
    private final TableView<Matricula> tabela = new TableView<>();
    private final DatePicker campoData     = new DatePicker();
    private final ComboBox<StatusMatricula> campoStatus = new ComboBox<>();
    private final TextField campoProgresso = new TextField();
    private final ComboBox<Aprendiz> campoAprendiz = new ComboBox<>();
    private final ComboBox<Curso> campoCurso = new ComboBox<>();
    private Integer idEmEdicao = null;

    public Parent build() {
        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(15));
        campoData.setConverter(Validadores.conversorData());
        campoProgresso.setPromptText("0 a 100");
        campoProgresso.setTextFormatter(new TextFormatter<>(c ->
            c.getControlNewText().matches("[0-9]*") ? c : null));
        campoStatus.getItems().addAll(StatusMatricula.values());
        campoAprendiz.getItems().addAll(aprendizRepo.listar());
        campoCurso.getItems().addAll(cursoRepo.listar());

        form.addRow(0, new Label("Data matricula:"), campoData);
        form.addRow(1, new Label("Status:"),         campoStatus);
        form.addRow(2, new Label("Progresso (%):"),  campoProgresso);
        form.addRow(3, new Label("Aprendiz:"),       campoAprendiz);
        form.addRow(4, new Label("Curso:"),          campoCurso);

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
        tabela.getColumns().add(cId);
        tabela.getColumns().add(cData);
        tabela.getColumns().add(cStatus);
        tabela.getColumns().add(cProg);
        tabela.getColumns().add(cAp);
        tabela.getColumns().add(cCur);
        atualizarTabela();

        tabela.getSelectionModel().selectedItemProperty().addListener((o, a, sel) -> {
            if (sel != null) {
                idEmEdicao = sel.getId();
                campoData.setValue(sel.getDataMatricula());
                campoStatus.setValue(sel.getStatus());
                campoProgresso.setText(String.valueOf(sel.getProgresso()));
                buscarPorId(campoAprendiz.getItems(), sel.getIdAprendiz()).ifPresent(campoAprendiz::setValue);
                buscarPorId(campoCurso.getItems(), sel.getIdCurso()).ifPresent(campoCurso::setValue);
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
            LocalDate data = campoData.getValue();
            if (data == null) throw new ValidacaoException("Data matricula e obrigatoria.");
            if (campoStatus.getValue() == null)
                throw new ValidacaoException("Selecione o status da matricula.");
            StatusMatricula status = campoStatus.getValue();
            int progresso  = Validadores.inteiroPositivo("Progresso", campoProgresso.getText());
            if (progresso > 100) throw new ValidacaoException("Progresso deve ser de 0 a 100.");
            if (campoAprendiz.getValue() == null) throw new ValidacaoException("Selecione o aprendiz.");
            if (campoCurso.getValue() == null) throw new ValidacaoException("Selecione o curso.");
            int idAprendiz = campoAprendiz.getValue().getId();
            int idCurso    = campoCurso.getValue().getId();

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

    public void atualizar() {
        atualizarTabela();
        atualizarListas();
    }

    private void atualizarTabela() { tabela.getItems().setAll(repo.listar()); }

    private void atualizarListas() {
        Aprendiz aprendizSelecionado = campoAprendiz.getValue();
        Curso cursoSelecionado = campoCurso.getValue();
        campoAprendiz.getItems().setAll(aprendizRepo.listar());
        campoCurso.getItems().setAll(cursoRepo.listar());
        campoAprendiz.setValue(aprendizSelecionado);
        campoCurso.setValue(cursoSelecionado);
    }

    private void limparFormulario() {
        idEmEdicao = null;
        campoData.setValue(null); campoProgresso.clear();
        campoAprendiz.setValue(null); campoCurso.setValue(null);
        campoStatus.setValue(null);
        tabela.getSelectionModel().clearSelection();
    }

    private <T extends eduvfinance.util.Identificavel> java.util.Optional<T> buscarPorId(
            java.util.List<T> itens, int id) {
        return itens.stream().filter(i -> i.getId() == id).findFirst();
    }
}
