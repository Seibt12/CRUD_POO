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

import eduvfinance.model.Matricula;
import eduvfinance.model.MetodoPagto;
import eduvfinance.model.Pagamento;
import eduvfinance.repository.MatriculaRepository;
import eduvfinance.repository.PagamentoRepository;
import eduvfinance.util.Alertas;
import eduvfinance.util.ValidacaoException;
import eduvfinance.util.Validadores;

public class PagamentoView {

    private final PagamentoRepository repo = new PagamentoRepository();
    private final MatriculaRepository matriculaRepo = new MatriculaRepository();
    private final TableView<Pagamento> tabela = new TableView<>();
    private final TextField campoValor = new TextField();
    private final DatePicker campoData = new DatePicker();
    private final ComboBox<MetodoPagto> campoMetodo = new ComboBox<>();
    private final ComboBox<Matricula> campoMatricula = new ComboBox<>();
    private Integer idEmEdicao = null;

    public Parent build() {
        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(15));
        campoValor.setPromptText("ex.: 199,90");
        campoValor.setTextFormatter(new TextFormatter<>(c ->
            c.getControlNewText().matches("[0-9]*[,.]?[0-9]*") ? c : null));
        campoData.setConverter(Validadores.conversorData());
        campoMetodo.getItems().addAll(MetodoPagto.values());
        campoMatricula.getItems().addAll(matriculaRepo.listar());

        form.addRow(0, new Label("Valor:"),        campoValor);
        form.addRow(1, new Label("Data pagto:"),   campoData);
        form.addRow(2, new Label("Metodo:"),       campoMetodo);
        form.addRow(3, new Label("Matricula:"),    campoMatricula);

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
        tabela.getColumns().add(cId);
        tabela.getColumns().add(cValor);
        tabela.getColumns().add(cData);
        tabela.getColumns().add(cMetodo);
        tabela.getColumns().add(cMat);
        atualizarTabela();

        tabela.getSelectionModel().selectedItemProperty().addListener((o, a, sel) -> {
            if (sel != null) {
                idEmEdicao = sel.getId();
                campoValor.setText(String.valueOf(sel.getValor()));
                campoData.setValue(sel.getDataPagamento());
                campoMetodo.setValue(sel.getMetodo());
                buscarPorId(campoMatricula.getItems(), sel.getIdMatricula()).ifPresent(campoMatricula::setValue);
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
            LocalDate data = campoData.getValue();
            if (data == null) throw new ValidacaoException("Data pagto e obrigatoria.");
            if (campoMetodo.getValue() == null)
                throw new ValidacaoException("Selecione o metodo de pagamento.");
            MetodoPagto metodo = campoMetodo.getValue();
            if (campoMatricula.getValue() == null) throw new ValidacaoException("Selecione a matricula.");
            int idMat = campoMatricula.getValue().getId();

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

    public void atualizar() {
        atualizarTabela();
        atualizarListas();
    }

    private void atualizarTabela() { tabela.getItems().setAll(repo.listar()); }

    private void atualizarListas() {
        Matricula matriculaSelecionada = campoMatricula.getValue();
        campoMatricula.getItems().setAll(matriculaRepo.listar());
        campoMatricula.setValue(matriculaSelecionada);
    }

    private void limparFormulario() {
        idEmEdicao = null;
        campoValor.clear(); campoData.setValue(null); campoMatricula.setValue(null);
        campoMetodo.setValue(null);
        tabela.getSelectionModel().clearSelection();
    }

    private <T extends eduvfinance.util.Identificavel> java.util.Optional<T> buscarPorId(
            java.util.List<T> itens, int id) {
        return itens.stream().filter(i -> i.getId() == id).findFirst();
    }
}
