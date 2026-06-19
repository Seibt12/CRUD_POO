package eduvfinance.view;

import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import eduvfinance.model.Avaliacao;
import eduvfinance.model.Curso;
import eduvfinance.repository.AvaliacaoRepository;
import eduvfinance.repository.CursoRepository;
import eduvfinance.util.Alertas;
import eduvfinance.util.ValidacaoException;
import eduvfinance.util.Validadores;

public class AvaliacaoView {

    private final AvaliacaoRepository repo = new AvaliacaoRepository();
    private final CursoRepository cursoRepo = new CursoRepository();
    private final TableView<Avaliacao> tabela = new TableView<>();
    private final TextField campoNota       = new TextField();
    private final TextField campoComentario = new TextField();
    private final DatePicker campoData      = new DatePicker();
    private final ComboBox<Curso> campoCurso = new ComboBox<>();
    private Integer idEmEdicao = null;

    public Parent build() {
        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(15));
        campoNota.setPromptText("0 a 5");
        campoNota.setTextFormatter(new TextFormatter<>(c ->
            c.getControlNewText().matches("[0-9]*") ? c : null));
        campoData.setConverter(Validadores.conversorData());
        campoCurso.getItems().addAll(cursoRepo.listar());

        form.addRow(0, new Label("Nota:"),       campoNota);
        form.addRow(1, new Label("Comentario:"), campoComentario);
        form.addRow(2, new Label("Data:"),       campoData);
        form.addRow(3, new Label("Curso:"),      campoCurso);

        Button salvar  = new Button("Salvar");
        Button limpar  = new Button("Limpar");
        Button excluir = new Button("Excluir selecionado");
        form.add(new HBox(10, salvar, limpar, excluir), 1, 4);

        TableColumn<Avaliacao, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Avaliacao, Integer> cNota = new TableColumn<>("Nota");
        cNota.setCellValueFactory(new PropertyValueFactory<>("nota"));
        TableColumn<Avaliacao, String> cCom = new TableColumn<>("Comentario");
        cCom.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        TableColumn<Avaliacao, String> cData = new TableColumn<>("Data");
        cData.setCellValueFactory(new PropertyValueFactory<>("dataFormatada"));
        TableColumn<Avaliacao, Integer> cCurso = new TableColumn<>("ID Curso");
        cCurso.setCellValueFactory(new PropertyValueFactory<>("idCurso"));
        tabela.getColumns().add(cId);
        tabela.getColumns().add(cNota);
        tabela.getColumns().add(cCom);
        tabela.getColumns().add(cData);
        tabela.getColumns().add(cCurso);
        atualizarTabela();

        tabela.getSelectionModel().selectedItemProperty().addListener((o, a, sel) -> {
            if (sel != null) {
                idEmEdicao = sel.getId();
                campoNota.setText(String.valueOf(sel.getNota()));
                campoComentario.setText(sel.getComentario());
                campoData.setValue(sel.getData());
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
            int nota = Validadores.inteiroPositivo("Nota", campoNota.getText());
            if (nota > 5) throw new ValidacaoException("Nota deve ser de 0 a 5.");
            String comentario = Validadores.texto("Comentario", campoComentario.getText());
            LocalDate data = campoData.getValue();
            if (data == null) throw new ValidacaoException("Data e obrigatoria.");
            if (campoCurso.getValue() == null) throw new ValidacaoException("Selecione o curso.");
            int idCurso = campoCurso.getValue().getId();

            if (idEmEdicao == null) {
                repo.inserir(new Avaliacao(nota, comentario, data, idCurso));
                Alertas.sucesso("Avaliacao inserida.");
            } else {
                Avaliacao av = new Avaliacao(nota, comentario, data, idCurso);
                av.setId(idEmEdicao);
                repo.atualizar(av);
                Alertas.sucesso("Avaliacao atualizada.");
            }
            limparFormulario();
            atualizarTabela();
        } catch (ValidacaoException ex) {
            Alertas.erro(ex.getMessage());
        }
    }

    private void excluir() {
        Avaliacao sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) { Alertas.erro("Selecione um registro na tabela."); return; }
        repo.excluir(sel.getId());
        Alertas.sucesso("Avaliacao excluida.");
        limparFormulario();
        atualizarTabela();
    }

    public void atualizar() {
        atualizarTabela();
        atualizarListas();
    }

    private void atualizarTabela() { tabela.getItems().setAll(repo.listar()); }

    private void atualizarListas() {
        Curso cursoSelecionado = campoCurso.getValue();
        campoCurso.getItems().setAll(cursoRepo.listar());
        campoCurso.setValue(cursoSelecionado);
    }

    private void limparFormulario() {
        idEmEdicao = null;
        campoNota.clear(); campoComentario.clear();
        campoData.setValue(null); campoCurso.setValue(null);
        tabela.getSelectionModel().clearSelection();
    }

    private <T extends eduvfinance.util.Identificavel> java.util.Optional<T> buscarPorId(
            java.util.List<T> itens, int id) {
        return itens.stream().filter(i -> i.getId() == id).findFirst();
    }
}
