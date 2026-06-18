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

import eduvfinance.model.Simulacao;
import eduvfinance.model.TipoInvestimento;
import eduvfinance.repository.SimulacaoRepository;
import eduvfinance.util.Alertas;
import eduvfinance.util.ValidacaoException;
import eduvfinance.util.Validadores;

public class SimulacaoView {

    private final SimulacaoRepository repo = new SimulacaoRepository();
    private final TableView<Simulacao> tabela = new TableView<>();
    private final TextField campoAporteInicial = new TextField();
    private final TextField campoAporteMensal  = new TextField();
    private final TextField campoTaxa    = new TextField();
    private final TextField campoPeriodo = new TextField();
    private final ComboBox<TipoInvestimento> campoTipo = new ComboBox<>();
    private Integer idEmEdicao = null;

    public Parent build() {
        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(15));
        campoAporteInicial.setPromptText("ex.: 1000,00");
        campoAporteMensal.setPromptText("ex.: 200,00");
        campoTaxa.setPromptText("% ao mes, ex.: 1,0");
        campoPeriodo.setPromptText("meses, ex.: 24");
        campoTipo.getItems().addAll(TipoInvestimento.values());

        form.addRow(0, new Label("Aporte inicial:"), campoAporteInicial);
        form.addRow(1, new Label("Aporte mensal:"),  campoAporteMensal);
        form.addRow(2, new Label("Taxa (% mes):"),   campoTaxa);
        form.addRow(3, new Label("Periodo (meses):"), campoPeriodo);
        form.addRow(4, new Label("Tipo:"),           campoTipo);

        Button salvar  = new Button("Salvar");
        Button limpar  = new Button("Limpar");
        Button excluir = new Button("Excluir selecionado");
        form.add(new HBox(10, salvar, limpar, excluir), 1, 5);

        TableColumn<Simulacao, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Simulacao, Double> cInicial = new TableColumn<>("Aporte ini.");
        cInicial.setCellValueFactory(new PropertyValueFactory<>("aporteInicial"));
        TableColumn<Simulacao, Double> cMensal = new TableColumn<>("Aporte mes");
        cMensal.setCellValueFactory(new PropertyValueFactory<>("aporteMensal"));
        TableColumn<Simulacao, Double> cTaxa = new TableColumn<>("Taxa %");
        cTaxa.setCellValueFactory(new PropertyValueFactory<>("taxa"));
        TableColumn<Simulacao, Integer> cPeriodo = new TableColumn<>("Periodo");
        cPeriodo.setCellValueFactory(new PropertyValueFactory<>("periodo"));
        TableColumn<Simulacao, TipoInvestimento> cTipo = new TableColumn<>("Tipo");
        cTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        TableColumn<Simulacao, String> cMontante = new TableColumn<>("Montante (R$)");
        cMontante.setCellValueFactory(new PropertyValueFactory<>("montanteFormatado"));
        tabela.getColumns().addAll(cId, cInicial, cMensal, cTaxa, cPeriodo, cTipo, cMontante);
        atualizarTabela();

        tabela.getSelectionModel().selectedItemProperty().addListener((o, a, sel) -> {
            if (sel != null) {
                idEmEdicao = sel.getId();
                campoAporteInicial.setText(String.valueOf(sel.getAporteInicial()));
                campoAporteMensal.setText(String.valueOf(sel.getAporteMensal()));
                campoTaxa.setText(String.valueOf(sel.getTaxa()));
                campoPeriodo.setText(String.valueOf(sel.getPeriodo()));
                campoTipo.setValue(sel.getTipo());
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
            double inicial = Validadores.numeroPositivo("Aporte inicial", campoAporteInicial.getText());
            double mensal  = Validadores.numeroPositivo("Aporte mensal", campoAporteMensal.getText());
            double taxa    = Validadores.numeroPositivo("Taxa", campoTaxa.getText());
            int periodo    = Validadores.inteiroPositivo("Periodo", campoPeriodo.getText());
            if (campoTipo.getValue() == null)
                throw new ValidacaoException("Selecione o tipo de investimento.");
            TipoInvestimento tipo = campoTipo.getValue();

            if (idEmEdicao == null) {
                repo.inserir(new Simulacao(inicial, mensal, taxa, periodo, tipo));
                Alertas.sucesso("Simulacao inserida.");
            } else {
                Simulacao s = new Simulacao(inicial, mensal, taxa, periodo, tipo);
                s.setId(idEmEdicao);
                repo.atualizar(s);
                Alertas.sucesso("Simulacao atualizada.");
            }
            limparFormulario();
            atualizarTabela();
        } catch (ValidacaoException ex) {
            Alertas.erro(ex.getMessage());
        }
    }

    private void excluir() {
        Simulacao sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) { Alertas.erro("Selecione um registro na tabela."); return; }
        repo.excluir(sel.getId());
        Alertas.sucesso("Simulacao excluida.");
        limparFormulario();
        atualizarTabela();
    }

    private void atualizarTabela() { tabela.getItems().setAll(repo.listar()); }

    private void limparFormulario() {
        idEmEdicao = null;
        campoAporteInicial.clear(); campoAporteMensal.clear();
        campoTaxa.clear(); campoPeriodo.clear();
        campoTipo.setValue(null);
        tabela.getSelectionModel().clearSelection();
    }
}
