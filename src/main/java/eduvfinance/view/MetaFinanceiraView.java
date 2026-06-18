package eduvfinance.view;

import java.time.LocalDate;

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

import eduvfinance.model.MetaFinanceira;
import eduvfinance.repository.MetaFinanceiraRepository;
import eduvfinance.util.Alertas;
import eduvfinance.util.ValidacaoException;
import eduvfinance.util.Validadores;

/**
 * Tela CRUD de MetaFinanceira (objetivo financeiro pessoal do aprendiz).
 */
public class MetaFinanceiraView {

    private final MetaFinanceiraRepository repo = new MetaFinanceiraRepository();
    private final TableView<MetaFinanceira> tabela = new TableView<>();
    private final TextField campoNome       = new TextField();
    private final TextField campoValorAlvo  = new TextField();
    private final TextField campoDataAlvo   = new TextField();
    private final TextField campoPrioridade = new TextField();
    private Integer idEmEdicao = null;

    public Parent build() {
        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(15));
        campoValorAlvo.setPromptText("ex.: 10000,00");
        campoDataAlvo.setPromptText("DD/MM/AAAA");
        campoPrioridade.setPromptText("1 a 5");

        form.addRow(0, new Label("Nome:"),       campoNome);
        form.addRow(1, new Label("Valor alvo:"), campoValorAlvo);
        form.addRow(2, new Label("Data alvo:"),  campoDataAlvo);
        form.addRow(3, new Label("Prioridade:"), campoPrioridade);

        Button salvar  = new Button("Salvar");
        Button limpar  = new Button("Limpar");
        Button excluir = new Button("Excluir selecionado");
        form.add(new HBox(10, salvar, limpar, excluir), 1, 4);

        TableColumn<MetaFinanceira, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<MetaFinanceira, String> cNome = new TableColumn<>("Nome");
        cNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<MetaFinanceira, Double> cValor = new TableColumn<>("Valor alvo");
        cValor.setCellValueFactory(new PropertyValueFactory<>("valorAlvo"));
        TableColumn<MetaFinanceira, String> cData = new TableColumn<>("Data alvo");
        cData.setCellValueFactory(new PropertyValueFactory<>("dataAlvoFormatada"));
        TableColumn<MetaFinanceira, Integer> cPrio = new TableColumn<>("Prioridade");
        cPrio.setCellValueFactory(new PropertyValueFactory<>("prioridade"));
        tabela.getColumns().addAll(cId, cNome, cValor, cData, cPrio);
        atualizarTabela();

        tabela.getSelectionModel().selectedItemProperty().addListener((o, a, sel) -> {
            if (sel != null) {
                idEmEdicao = sel.getId();
                campoNome.setText(sel.getNome());
                campoValorAlvo.setText(String.valueOf(sel.getValorAlvo()));
                campoDataAlvo.setText(sel.getDataAlvoFormatada());
                campoPrioridade.setText(String.valueOf(sel.getPrioridade()));
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
            String nome = Validadores.texto("Nome", campoNome.getText());
            double valor = Validadores.numeroPositivo("Valor alvo", campoValorAlvo.getText());
            LocalDate data = Validadores.data("Data alvo", campoDataAlvo.getText());
            int prio = Validadores.inteiroPositivo("Prioridade", campoPrioridade.getText());
            if (prio < 1 || prio > 5) throw new ValidacaoException("Prioridade deve ser de 1 a 5.");

            if (idEmEdicao == null) {
                repo.inserir(new MetaFinanceira(nome, valor, data, prio));
                Alertas.sucesso("Meta inserida.");
            } else {
                MetaFinanceira m = new MetaFinanceira(nome, valor, data, prio);
                m.setId(idEmEdicao);
                repo.atualizar(m);
                Alertas.sucesso("Meta atualizada.");
            }
            limparFormulario();
            atualizarTabela();
        } catch (ValidacaoException ex) {
            Alertas.erro(ex.getMessage());
        }
    }

    private void excluir() {
        MetaFinanceira sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) { Alertas.erro("Selecione um registro na tabela."); return; }
        repo.excluir(sel.getId());
        Alertas.sucesso("Meta excluida.");
        limparFormulario();
        atualizarTabela();
    }

    private void atualizarTabela() { tabela.getItems().setAll(repo.listar()); }

    private void limparFormulario() {
        idEmEdicao = null;
        campoNome.clear(); campoValorAlvo.clear();
        campoDataAlvo.clear(); campoPrioridade.clear();
        tabela.getSelectionModel().clearSelection();
    }
}
