package eduvfinance.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import eduvfinance.model.Categoria;
import eduvfinance.repository.CategoriaRepository;
import eduvfinance.util.Alertas;
import eduvfinance.util.ValidacaoException;
import eduvfinance.util.Validadores;

public class CategoriaView {

    private final CategoriaRepository repo = new CategoriaRepository();
    private final TableView<Categoria> tabela = new TableView<>();
    private final TextField campoNome      = new TextField();
    private final TextField campoDescricao = new TextField();
    private final TextField campoIcone     = new TextField();
    private Integer idEmEdicao = null;

    public Parent build() {
        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(15));
        campoIcone.setPromptText("ex.: carteira, casa, carro");
        form.addRow(0, new Label("Nome:"),      campoNome);
        form.addRow(1, new Label("Descricao:"), campoDescricao);
        form.addRow(2, new Label("Icone:"),     campoIcone);

        Button salvar  = new Button("Salvar");
        Button limpar  = new Button("Limpar");
        Button excluir = new Button("Excluir selecionado");
        form.add(new HBox(10, salvar, limpar, excluir), 1, 3);

        TableColumn<Categoria, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Categoria, String> cNome = new TableColumn<>("Nome");
        cNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Categoria, String> cDesc = new TableColumn<>("Descricao");
        cDesc.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        TableColumn<Categoria, String> cIcone = new TableColumn<>("Icone");
        cIcone.setCellValueFactory(new PropertyValueFactory<>("icone"));
        tabela.getColumns().add(cId);
        tabela.getColumns().add(cNome);
        tabela.getColumns().add(cDesc);
        tabela.getColumns().add(cIcone);
        atualizarTabela();

        tabela.getSelectionModel().selectedItemProperty().addListener((o, a, sel) -> {
            if (sel != null) {
                idEmEdicao = sel.getId();
                campoNome.setText(sel.getNome());
                campoDescricao.setText(sel.getDescricao());
                campoIcone.setText(sel.getIcone());
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
            String nome  = Validadores.texto("Nome", campoNome.getText());
            String desc  = Validadores.texto("Descricao", campoDescricao.getText());
            String icone = Validadores.texto("Icone", campoIcone.getText());
            if (idEmEdicao == null) {
                repo.inserir(new Categoria(nome, desc, icone));
                Alertas.sucesso("Categoria inserida.");
            } else {
                Categoria c = new Categoria(nome, desc, icone);
                c.setId(idEmEdicao);
                repo.atualizar(c);
                Alertas.sucesso("Categoria atualizada.");
            }
            limparFormulario();
            atualizarTabela();
        } catch (ValidacaoException ex) {
            Alertas.erro(ex.getMessage());
        }
    }

    private void excluir() {
        Categoria sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) { Alertas.erro("Selecione um registro na tabela."); return; }
        repo.excluir(sel.getId());
        Alertas.sucesso("Categoria excluida.");
        limparFormulario();
        atualizarTabela();
    }

    private void atualizarTabela() { tabela.getItems().setAll(repo.listar()); }

    private void limparFormulario() {
        idEmEdicao = null;
        campoNome.clear(); campoDescricao.clear(); campoIcone.clear();
        tabela.getSelectionModel().clearSelection();
    }
}
