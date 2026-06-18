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

import eduvfinance.model.Lancamento;
import eduvfinance.model.Recorrencia;
import eduvfinance.model.TipoLancto;
import eduvfinance.repository.LancamentoRepository;
import eduvfinance.util.Alertas;
import eduvfinance.util.ValidacaoException;
import eduvfinance.util.Validadores;

public class LancamentoView {

    private final LancamentoRepository repo = new LancamentoRepository();
    private final TableView<Lancamento> tabela = new TableView<>();
    private final ComboBox<TipoLancto> campoTipo = new ComboBox<>();
    private final TextField campoValor = new TextField();
    private final TextField campoData  = new TextField();
    private final ComboBox<Recorrencia> campoRecorrencia = new ComboBox<>();
    private final TextField campoIdCategoria = new TextField();
    private Integer idEmEdicao = null;

    public Parent build() {
        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(15));
        campoValor.setPromptText("ex.: 1500,00");
        campoData.setPromptText("DD/MM/AAAA");
        campoTipo.getItems().addAll(TipoLancto.values());
        campoRecorrencia.getItems().addAll(Recorrencia.values());

        form.addRow(0, new Label("Tipo:"),        campoTipo);
        form.addRow(1, new Label("Valor:"),       campoValor);
        form.addRow(2, new Label("Data:"),        campoData);
        form.addRow(3, new Label("Recorrencia:"), campoRecorrencia);
        form.addRow(4, new Label("ID Categoria:"), campoIdCategoria);

        Button salvar  = new Button("Salvar");
        Button limpar  = new Button("Limpar");
        Button excluir = new Button("Excluir selecionado");
        form.add(new HBox(10, salvar, limpar, excluir), 1, 5);

        TableColumn<Lancamento, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Lancamento, TipoLancto> cTipo = new TableColumn<>("Tipo");
        cTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        TableColumn<Lancamento, Double> cValor = new TableColumn<>("Valor");
        cValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        TableColumn<Lancamento, String> cData = new TableColumn<>("Data");
        cData.setCellValueFactory(new PropertyValueFactory<>("dataFormatada"));
        TableColumn<Lancamento, Recorrencia> cRec = new TableColumn<>("Recorrencia");
        cRec.setCellValueFactory(new PropertyValueFactory<>("recorrencia"));
        TableColumn<Lancamento, Integer> cCat = new TableColumn<>("ID Categoria");
        cCat.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        tabela.getColumns().addAll(cId, cTipo, cValor, cData, cRec, cCat);
        atualizarTabela();

        tabela.getSelectionModel().selectedItemProperty().addListener((o, a, sel) -> {
            if (sel != null) {
                idEmEdicao = sel.getId();
                campoTipo.setValue(sel.getTipo());
                campoValor.setText(String.valueOf(sel.getValor()));
                campoData.setText(sel.getDataFormatada());
                campoRecorrencia.setValue(sel.getRecorrencia());
                campoIdCategoria.setText(String.valueOf(sel.getIdCategoria()));
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
            if (campoTipo.getValue() == null)
                throw new ValidacaoException("Selecione o tipo do lancamento.");
            TipoLancto tipo = campoTipo.getValue();
            double valor = Validadores.numeroPositivo("Valor", campoValor.getText());
            LocalDate data = Validadores.data("Data", campoData.getText());
            if (campoRecorrencia.getValue() == null)
                throw new ValidacaoException("Selecione a recorrencia.");
            Recorrencia rec = campoRecorrencia.getValue();
            int idCat = Validadores.inteiroPositivo("ID Categoria", campoIdCategoria.getText());

            if (idEmEdicao == null) {
                repo.inserir(new Lancamento(tipo, valor, data, rec, idCat));
                Alertas.sucesso("Lancamento inserido.");
            } else {
                Lancamento l = new Lancamento(tipo, valor, data, rec, idCat);
                l.setId(idEmEdicao);
                repo.atualizar(l);
                Alertas.sucesso("Lancamento atualizado.");
            }
            limparFormulario();
            atualizarTabela();
        } catch (ValidacaoException ex) {
            Alertas.erro(ex.getMessage());
        }
    }

    private void excluir() {
        Lancamento sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) { Alertas.erro("Selecione um registro na tabela."); return; }
        repo.excluir(sel.getId());
        Alertas.sucesso("Lancamento excluido.");
        limparFormulario();
        atualizarTabela();
    }

    private void atualizarTabela() { tabela.getItems().setAll(repo.listar()); }

    private void limparFormulario() {
        idEmEdicao = null;
        campoValor.clear(); campoData.clear(); campoIdCategoria.clear();
        campoTipo.setValue(null); campoRecorrencia.setValue(null);
        tabela.getSelectionModel().clearSelection();
    }
}
