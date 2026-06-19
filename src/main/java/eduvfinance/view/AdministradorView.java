package eduvfinance.view;

import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import eduvfinance.model.Administrador;
import eduvfinance.repository.AdministradorRepository;
import eduvfinance.util.Alertas;
import eduvfinance.util.ValidacaoException;
import eduvfinance.util.Validadores;

public class AdministradorView {

    private final AdministradorRepository repo = new AdministradorRepository();
    private final TableView<Administrador> tabela = new TableView<>();
    private final TextField campoNome  = new TextField();
    private final TextField campoEmail = new TextField();
    private final TextField campoSenha = new TextField();
    private final DatePicker campoData = new DatePicker();
    private Integer idEmEdicao = null;

    public Parent build() {
        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(15));
        campoData.setConverter(Validadores.conversorData());

        form.addRow(0, new Label("Nome:"),  campoNome);
        form.addRow(1, new Label("E-mail:"), campoEmail);
        form.addRow(2, new Label("Senha:"),  campoSenha);
        form.addRow(3, new Label("Nascimento:"), campoData);

        Button salvar  = new Button("Salvar");
        Button limpar  = new Button("Limpar");
        Button excluir = new Button("Excluir selecionado");
        form.add(new HBox(10, salvar, limpar, excluir), 1, 4);

        TableColumn<Administrador, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Administrador, String> cNome = new TableColumn<>("Nome");
        cNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Administrador, String> cEmail = new TableColumn<>("E-mail");
        cEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<Administrador, String> cData = new TableColumn<>("Nascimento");
        cData.setCellValueFactory(new PropertyValueFactory<>("dataNascimentoFormatada"));
        tabela.getColumns().add(cId);
        tabela.getColumns().add(cNome);
        tabela.getColumns().add(cEmail);
        tabela.getColumns().add(cData);
        atualizarTabela();

        tabela.getSelectionModel().selectedItemProperty().addListener((o, a, sel) -> {
            if (sel != null) {
                idEmEdicao = sel.getId();
                campoNome.setText(sel.getNome());
                campoEmail.setText(sel.getEmail());
                campoSenha.setText(sel.getSenha());
                campoData.setValue(sel.getDataNascimento());
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
            String email = Validadores.texto("E-mail", campoEmail.getText());
            String senha = Validadores.texto("Senha", campoSenha.getText());
            LocalDate nasc = campoData.getValue();
            if (nasc == null) throw new ValidacaoException("Nascimento e obrigatorio.");

            if (idEmEdicao == null) {
                repo.inserir(new Administrador(nome, email, senha, nasc));
                Alertas.sucesso("Administrador inserido.");
            } else {
                Administrador ad = new Administrador(nome, email, senha, nasc);
                ad.setId(idEmEdicao);
                repo.atualizar(ad);
                Alertas.sucesso("Administrador atualizado.");
            }
            limparFormulario();
            atualizarTabela();
        } catch (ValidacaoException ex) {
            Alertas.erro(ex.getMessage());
        }
    }

    private void excluir() {
        Administrador sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) { Alertas.erro("Selecione um registro na tabela."); return; }
        repo.excluir(sel.getId());
        Alertas.sucesso("Administrador excluido.");
        limparFormulario();
        atualizarTabela();
    }

    public void atualizar() { atualizarTabela(); }

    private void atualizarTabela() { tabela.getItems().setAll(repo.listar()); }

    private void limparFormulario() {
        idEmEdicao = null;
        campoNome.clear(); campoEmail.clear(); campoSenha.clear(); campoData.setValue(null);
        tabela.getSelectionModel().clearSelection();
    }
}
