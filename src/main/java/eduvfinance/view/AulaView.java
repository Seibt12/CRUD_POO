package eduvfinance.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import eduvfinance.model.Aula;
import eduvfinance.model.Nivel;
import eduvfinance.repository.AulaRepository;
import eduvfinance.util.Alertas;
import eduvfinance.util.ValidacaoException;
import eduvfinance.util.Validadores;

public class AulaView {

    private final AulaRepository repo = new AulaRepository();
    private final TableView<Aula> tabela = new TableView<>();
    private final TextField campoTitulo   = new TextField();
    private final TextField campoConteudo = new TextField();
    private final ComboBox<Nivel> campoNivel = new ComboBox<>();
    private final TextField campoOrdem    = new TextField();
    private Integer idEmEdicao = null;

    public Parent build() {
        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(15));
        campoOrdem.setPromptText("ex.: 1");
        campoOrdem.setTextFormatter(new TextFormatter<>(c ->
            c.getControlNewText().matches("[0-9]*") ? c : null));
        campoNivel.getItems().addAll(Nivel.values());

        form.addRow(0, new Label("Titulo:"),   campoTitulo);
        form.addRow(1, new Label("Conteudo:"), campoConteudo);
        form.addRow(2, new Label("Nivel:"),    campoNivel);
        form.addRow(3, new Label("Ordem:"),    campoOrdem);

        Button salvar  = new Button("Salvar");
        Button limpar  = new Button("Limpar");
        Button excluir = new Button("Excluir selecionado");
        form.add(new HBox(10, salvar, limpar, excluir), 1, 4);

        TableColumn<Aula, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Aula, String> cTitulo = new TableColumn<>("Titulo");
        cTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        TableColumn<Aula, String> cConteudo = new TableColumn<>("Conteudo");
        cConteudo.setCellValueFactory(new PropertyValueFactory<>("conteudo"));
        TableColumn<Aula, Nivel> cNivel = new TableColumn<>("Nivel");
        cNivel.setCellValueFactory(new PropertyValueFactory<>("nivel"));
        TableColumn<Aula, Integer> cOrdem = new TableColumn<>("Ordem");
        cOrdem.setCellValueFactory(new PropertyValueFactory<>("ordem"));
        tabela.getColumns().add(cId);
        tabela.getColumns().add(cTitulo);
        tabela.getColumns().add(cConteudo);
        tabela.getColumns().add(cNivel);
        tabela.getColumns().add(cOrdem);
        atualizarTabela();

        tabela.getSelectionModel().selectedItemProperty().addListener((o, a, sel) -> {
            if (sel != null) {
                idEmEdicao = sel.getId();
                campoTitulo.setText(sel.getTitulo());
                campoConteudo.setText(sel.getConteudo());
                campoNivel.setValue(sel.getNivel());
                campoOrdem.setText(String.valueOf(sel.getOrdem()));
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
            String titulo   = Validadores.texto("Titulo", campoTitulo.getText());
            String conteudo = Validadores.texto("Conteudo", campoConteudo.getText());
            if (campoNivel.getValue() == null)
                throw new ValidacaoException("Selecione o nivel da aula.");
            Nivel nivel = campoNivel.getValue();
            int ordem = Validadores.inteiroPositivo("Ordem", campoOrdem.getText());

            if (idEmEdicao == null) {
                repo.inserir(new Aula(titulo, conteudo, nivel, ordem));
                Alertas.sucesso("Aula inserida.");
            } else {
                Aula au = new Aula(titulo, conteudo, nivel, ordem);
                au.setId(idEmEdicao);
                repo.atualizar(au);
                Alertas.sucesso("Aula atualizada.");
            }
            limparFormulario();
            atualizarTabela();
        } catch (ValidacaoException ex) {
            Alertas.erro(ex.getMessage());
        }
    }

    private void excluir() {
        Aula sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) { Alertas.erro("Selecione um registro na tabela."); return; }
        repo.excluir(sel.getId());
        Alertas.sucesso("Aula excluida.");
        limparFormulario();
        atualizarTabela();
    }

    public void atualizar() { atualizarTabela(); }

    private void atualizarTabela() { tabela.getItems().setAll(repo.listar()); }

    private void limparFormulario() {
        idEmEdicao = null;
        campoTitulo.clear(); campoConteudo.clear(); campoOrdem.clear();
        campoNivel.setValue(null);
        tabela.getSelectionModel().clearSelection();
    }
}
