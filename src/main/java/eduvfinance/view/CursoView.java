package eduvfinance.view;

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

import eduvfinance.model.Curso;
import eduvfinance.model.StatusCurso;
import eduvfinance.repository.CursoRepository;
import eduvfinance.util.Alertas;
import eduvfinance.util.ValidacaoException;
import eduvfinance.util.Validadores;

public class CursoView {

    private final CursoRepository repo = new CursoRepository();
    private final TableView<Curso> tabela = new TableView<>();
    private final TextField campoTitulo    = new TextField();
    private final TextField campoDescricao = new TextField();
    private final TextField campoPreco     = new TextField();
    private final ComboBox<StatusCurso> campoStatus = new ComboBox<>();
    private Integer idEmEdicao = null;

    public Parent build() {
        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(15));
        campoPreco.setPromptText("ex.: 199,90");
        campoStatus.getItems().addAll(StatusCurso.values());

        form.addRow(0, new Label("Titulo:"),    campoTitulo);
        form.addRow(1, new Label("Descricao:"), campoDescricao);
        form.addRow(2, new Label("Preco:"),     campoPreco);
        form.addRow(3, new Label("Status:"),    campoStatus);

        Button salvar  = new Button("Salvar");
        Button limpar  = new Button("Limpar");
        Button excluir = new Button("Excluir selecionado");
        form.add(new HBox(10, salvar, limpar, excluir), 1, 4);

        TableColumn<Curso, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Curso, String> cTitulo = new TableColumn<>("Titulo");
        cTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        TableColumn<Curso, String> cDesc = new TableColumn<>("Descricao");
        cDesc.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        TableColumn<Curso, Double> cPreco = new TableColumn<>("Preco");
        cPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        TableColumn<Curso, StatusCurso> cStatus = new TableColumn<>("Status");
        cStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tabela.getColumns().addAll(cId, cTitulo, cDesc, cPreco, cStatus);
        atualizarTabela();

        tabela.getSelectionModel().selectedItemProperty().addListener((o, a, sel) -> {
            if (sel != null) {
                idEmEdicao = sel.getId();
                campoTitulo.setText(sel.getTitulo());
                campoDescricao.setText(sel.getDescricao());
                campoPreco.setText(String.valueOf(sel.getPreco()));
                campoStatus.setValue(sel.getStatus());
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
            String titulo = Validadores.texto("Titulo", campoTitulo.getText());
            String desc   = Validadores.texto("Descricao", campoDescricao.getText());
            double preco  = Validadores.numeroPositivo("Preco", campoPreco.getText());
            if (campoStatus.getValue() == null)
                throw new ValidacaoException("Selecione o status do curso.");
            StatusCurso status = campoStatus.getValue();

            if (idEmEdicao == null) {
                repo.inserir(new Curso(titulo, desc, preco, status));
                Alertas.sucesso("Curso inserido.");
            } else {
                Curso c = new Curso(titulo, desc, preco, status);
                c.setId(idEmEdicao);
                repo.atualizar(c);
                Alertas.sucesso("Curso atualizado.");
            }
            limparFormulario();
            atualizarTabela();
        } catch (ValidacaoException ex) {
            Alertas.erro(ex.getMessage());
        }
    }

    private void excluir() {
        Curso sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) { Alertas.erro("Selecione um registro na tabela."); return; }
        repo.excluir(sel.getId());
        Alertas.sucesso("Curso excluido.");
        limparFormulario();
        atualizarTabela();
    }

    private void atualizarTabela() { tabela.getItems().setAll(repo.listar()); }

    private void limparFormulario() {
        idEmEdicao = null;
        campoTitulo.clear(); campoDescricao.clear(); campoPreco.clear();
        campoStatus.setValue(null);
        tabela.getSelectionModel().clearSelection();
    }
}
