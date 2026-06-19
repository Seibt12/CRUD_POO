package eduvfinance.view;

import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import eduvfinance.model.Aprendiz;
import eduvfinance.repository.AprendizRepository;
import eduvfinance.util.Alertas;
import eduvfinance.util.ValidacaoException;
import eduvfinance.util.Validadores;

public class AprendizView {

    private final AprendizRepository repo = new AprendizRepository();
    private final TableView<Aprendiz> tabela = new TableView<>();
    private final TextField campoNome   = new TextField();
    private final TextField campoEmail  = new TextField();
    private final TextField campoSenha   = new TextField();
    private final DatePicker campoData   = new DatePicker();
    private final ComboBox<String> campoPerfil = new ComboBox<>();
    private Integer idEmEdicao = null;

    public Parent build() {
        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(15));
        campoData.setConverter(Validadores.conversorData());
        campoPerfil.getItems().addAll("Conservador", "Moderado", "Arrojado");

        form.addRow(0, new Label("Nome:"),  campoNome);
        form.addRow(1, new Label("E-mail:"), campoEmail);
        form.addRow(2, new Label("Senha:"),  campoSenha);
        form.addRow(3, new Label("Nascimento:"), campoData);
        form.addRow(4, new Label("Perfil investidor:"), campoPerfil);

        Button salvar  = new Button("Salvar");
        Button limpar  = new Button("Limpar");
        Button excluir = new Button("Excluir selecionado");
        form.add(new HBox(10, salvar, limpar, excluir), 1, 5);

        TableColumn<Aprendiz, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Aprendiz, String> cNome = new TableColumn<>("Nome");
        cNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Aprendiz, String> cEmail = new TableColumn<>("E-mail");
        cEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<Aprendiz, String> cData = new TableColumn<>("Nascimento");
        cData.setCellValueFactory(new PropertyValueFactory<>("dataNascimentoFormatada"));
        TableColumn<Aprendiz, String> cPerfil = new TableColumn<>("Perfil");
        cPerfil.setCellValueFactory(new PropertyValueFactory<>("perfilInvestidor"));
        tabela.getColumns().add(cId);
        tabela.getColumns().add(cNome);
        tabela.getColumns().add(cEmail);
        tabela.getColumns().add(cData);
        tabela.getColumns().add(cPerfil);
        atualizarTabela();

        tabela.getSelectionModel().selectedItemProperty().addListener((o, a, sel) -> {
            if (sel != null) {
                idEmEdicao = sel.getId();
                campoNome.setText(sel.getNome());
                campoEmail.setText(sel.getEmail());
                campoSenha.setText(sel.getSenha());
                campoData.setValue(sel.getDataNascimento());
                campoPerfil.setValue(sel.getPerfilInvestidor());
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
            if (campoPerfil.getValue() == null)
                throw new ValidacaoException("Selecione o perfil investidor.");
            String perfil = campoPerfil.getValue();

            if (idEmEdicao == null) {
                repo.inserir(new Aprendiz(nome, email, senha, nasc, perfil));
                Alertas.sucesso("Aprendiz inserido.");
            } else {
                Aprendiz ap = new Aprendiz(nome, email, senha, nasc, perfil);
                ap.setId(idEmEdicao);
                repo.atualizar(ap);
                Alertas.sucesso("Aprendiz atualizado.");
            }
            limparFormulario();
            atualizarTabela();
        } catch (ValidacaoException ex) {
            Alertas.erro(ex.getMessage());
        }
    }

    private void excluir() {
        Aprendiz sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) { Alertas.erro("Selecione um registro na tabela."); return; }
        repo.excluir(sel.getId());
        Alertas.sucesso("Aprendiz excluido.");
        limparFormulario();
        atualizarTabela();
    }

    public void atualizar() { atualizarTabela(); }

    private void atualizarTabela() { tabela.getItems().setAll(repo.listar()); }

    private void limparFormulario() {
        idEmEdicao = null;
        campoNome.clear(); campoEmail.clear(); campoSenha.clear(); campoData.setValue(null);
        campoPerfil.setValue(null);
        tabela.getSelectionModel().clearSelection();
    }
}
