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

import eduvfinance.model.MetodoPagto;
import eduvfinance.model.Pagamento;
import eduvfinance.repository.PagamentoRepository;
import eduvfinance.util.Alertas;
import eduvfinance.util.ValidacaoException;
import eduvfinance.util.Validadores;

public class PagamentoView {

    private final PagamentoRepository repo = new PagamentoRepository();
    private final TableView<Pagamento> tabela = new TableView<>();
    private final TextField campoValor = new TextField();
    private final TextField campoData  = new TextField();
    private final ComboBox<MetodoPagto> campoMetodo = new ComboBox<>();
    private final TextField campoIdMatricula = new TextField();
    private Integer idEmEdicao = null;

    public Parent build() {
        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(15));
        campoValor.setPromptText("ex.: 199,90");
        campoData.setPromptText("DD/MM/AAAA");
        campoMetodo.getItems().addAll(MetodoPagto.values());

        form.addRow(0, new Label("Valor:"),        campoValor);
        form.addRow(1, new Label("Data pagto:"),   campoData);
        form.addRow(2, new Label("Metodo:"),       campoMetodo);
        form.addRow(3, new Label("ID Matricula:"), campoIdMatricula);

        Button salvar  = new Button("Salvar");
        Button limpar  = new Button("Limpar");
        Button excluir = new Button("Excluir selecionado");
        form.add(new HBox(10, salvar, limpar, excluir), 1, 4);

        TableColumn<Pagamento, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Pagamento, Double> cValor = new TableColumn<>("Valor");
        cValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        TableColumn<Pagamento, String> cData = new TableColumn<>("Data");
        cData.setCellValueFactory(new PropertyValueFactory<>("dataPagamentoFormatada"));
        TableColumn<Pagamento, MetodoPagto> cMetodo = new TableColumn<>("Metodo");
        cMetodo.setCellValueFactory(new PropertyValueFactory<>("metodo"));
        TableColumn<Pagamento, Integer> cMat = new TableColumn<>("ID Matricula");
        cMat.setCellValueFactory(new PropertyValueFactory<>("idMatricula"));
        tabela.getColumns().addAll(cId, cValor, cData, cMetodo, cMat);
        atualizarTabela();

        tabela.getSelectionModel().selectedItemProperty().addListener((o, a, sel) -> {
            if (sel != null) {
                idEmEdicao = sel.getId();
                campoValor.setText(String.valueOf(sel.getValor()));
                campoData.setText(sel.getDataPagamentoFormatada());
                campoMetodo.setValue(sel.getMetodo());
                campoIdMatricula.setText(String.valueOf(sel.getIdMatricula()));
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
            double valor = Validadores.numeroPositivo("Valor", campoValor.getText());
            LocalDate data = Validadores.data("Data pagto", campoData.getText());
            if (campoMetodo.getValue() == null)
                throw new ValidacaoException("Selecione o metodo de pagamento.");
            MetodoPagto metodo = campoMetodo.getValue();
            int idMat = Validadores.inteiroPositivo("ID Matricula", campoIdMatricula.getText());

            if (idEmEdicao == null) {
                repo.inserir(new Pagamento(valor, data, metodo, idMat));
                Alertas.sucesso("Pagamento inserido.");
            } else {
                Pagamento p = new Pagamento(valor, data, metodo, idMat);
                p.setId(idEmEdicao);
                repo.atualizar(p);
                Alertas.sucesso("Pagamento atualizado.");
            }
            limparFormulario();
            atualizarTabela();
        } catch (ValidacaoException ex) {
            Alertas.erro(ex.getMessage());
        }
    }

    private void excluir() {
        Pagamento sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) { Alertas.erro("Selecione um registro na tabela."); return; }
        repo.excluir(sel.getId());
        Alertas.sucesso("Pagamento excluido.");
        limparFormulario();
        atualizarTabela();
    }

    private void atualizarTabela() { tabela.getItems().setAll(repo.listar()); }

    private void limparFormulario() {
        idEmEdicao = null;
        campoValor.clear(); campoData.clear(); campoIdMatricula.clear();
        campoMetodo.setValue(null);
        tabela.getSelectionModel().clearSelection();
    }
}
