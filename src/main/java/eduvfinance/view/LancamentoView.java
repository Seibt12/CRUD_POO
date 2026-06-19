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

import eduvfinance.model.Categoria;
import eduvfinance.model.Lancamento;
import eduvfinance.model.Recorrencia;
import eduvfinance.model.TipoLancto;
import eduvfinance.repository.CategoriaRepository;
import eduvfinance.repository.LancamentoRepository;
import eduvfinance.util.Alertas;
import eduvfinance.util.ValidacaoException;
import eduvfinance.util.Validadores;

public class LancamentoView {

    private final LancamentoRepository repo = new LancamentoRepository();
    private final CategoriaRepository categoriaRepo = new CategoriaRepository();
    private final TableView<Lancamento> tabela = new TableView<>();
    private final ComboBox<TipoLancto> campoTipo = new ComboBox<>();
    private final TextField campoValor = new TextField();
    private final DatePicker campoData = new DatePicker();
    private final ComboBox<Recorrencia> campoRecorrencia = new ComboBox<>();
    private final ComboBox<Categoria> campoCategoria = new ComboBox<>();
    private Integer idEmEdicao = null;

    public Parent build() {
        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(15));
        campoValor.setPromptText("ex.: 1500,00");
        campoValor.setTextFormatter(new TextFormatter<>(c ->
            c.getControlNewText().matches("[0-9]*[,.]?[0-9]*") ? c : null));
        campoData.setConverter(Validadores.conversorData());
        campoTipo.getItems().addAll(TipoLancto.values());
        campoRecorrencia.getItems().addAll(Recorrencia.values());
        campoCategoria.getItems().addAll(categoriaRepo.listar());

        form.addRow(0, new Label("Tipo:"),        campoTipo);
        form.addRow(1, new Label("Valor:"),       campoValor);
        form.addRow(2, new Label("Data:"),        campoData);
        form.addRow(3, new Label("Recorrencia:"), campoRecorrencia);
        form.addRow(4, new Label("Categoria:"),   campoCategoria);

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
        tabela.getColumns().add(cId);
        tabela.getColumns().add(cTipo);
        tabela.getColumns().add(cValor);
        tabela.getColumns().add(cData);
        tabela.getColumns().add(cRec);
        tabela.getColumns().add(cCat);
        atualizarTabela();

        tabela.getSelectionModel().selectedItemProperty().addListener((o, a, sel) -> {
            if (sel != null) {
                idEmEdicao = sel.getId();
                campoTipo.setValue(sel.getTipo());
                campoValor.setText(String.valueOf(sel.getValor()));
                campoData.setValue(sel.getData());
                campoRecorrencia.setValue(sel.getRecorrencia());
                buscarPorId(campoCategoria.getItems(), sel.getIdCategoria()).ifPresent(campoCategoria::setValue);
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
            LocalDate data = campoData.getValue();
            if (data == null) throw new ValidacaoException("Data e obrigatoria.");
            if (campoRecorrencia.getValue() == null)
                throw new ValidacaoException("Selecione a recorrencia.");
            Recorrencia rec = campoRecorrencia.getValue();
            if (campoCategoria.getValue() == null) throw new ValidacaoException("Selecione a categoria.");
            int idCat = campoCategoria.getValue().getId();

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

    public void atualizar() {
        atualizarTabela();
        atualizarListas();
    }

    private void atualizarTabela() { tabela.getItems().setAll(repo.listar()); }

    private void atualizarListas() {
        Categoria categoriaSelecionada = campoCategoria.getValue();
        campoCategoria.getItems().setAll(categoriaRepo.listar());
        campoCategoria.setValue(categoriaSelecionada);
    }

    private void limparFormulario() {
        idEmEdicao = null;
        campoValor.clear(); campoData.setValue(null); campoCategoria.setValue(null);
        campoTipo.setValue(null); campoRecorrencia.setValue(null);
        tabela.getSelectionModel().clearSelection();
    }

    private <T extends eduvfinance.util.Identificavel> java.util.Optional<T> buscarPorId(
            java.util.List<T> itens, int id) {
        return itens.stream().filter(i -> i.getId() == id).findFirst();
    }
}
