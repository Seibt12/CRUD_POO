package eduvfinance.view;

import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import eduvfinance.model.Mentor;
import eduvfinance.repository.MentorRepository;
import eduvfinance.util.Alertas;
import eduvfinance.util.ValidacaoException;
import eduvfinance.util.Validadores;

public class MentorView {

    private final MentorRepository repo = new MentorRepository();
    private final TableView<Mentor> tabela = new TableView<>();
    private final TextField campoNome  = new TextField();
    private final TextField campoEmail = new TextField();
    private final TextField campoSenha = new TextField();
    private final DatePicker campoData = new DatePicker();
    private final TextArea  campoBio   = new TextArea();
    private Integer idEmEdicao = null;

    public Parent build() {
        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(15));
        campoData.setConverter(Validadores.conversorData());
        campoBio.setPrefRowCount(3);

        form.addRow(0, new Label("Nome:"),  campoNome);
        form.addRow(1, new Label("E-mail:"), campoEmail);
        form.addRow(2, new Label("Senha:"),  campoSenha);
        form.addRow(3, new Label("Nascimento:"), campoData);
        form.addRow(4, new Label("Biografia:"), campoBio);

        Button salvar  = new Button("Salvar");
        Button limpar  = new Button("Limpar");
        Button excluir = new Button("Excluir selecionado");
        form.add(new HBox(10, salvar, limpar, excluir), 1, 5);

        TableColumn<Mentor, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Mentor, String> cNome = new TableColumn<>("Nome");
        cNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Mentor, String> cEmail = new TableColumn<>("E-mail");
        cEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<Mentor, String> cData = new TableColumn<>("Nascimento");
        cData.setCellValueFactory(new PropertyValueFactory<>("dataNascimentoFormatada"));
        TableColumn<Mentor, String> cBio = new TableColumn<>("Biografia");
        cBio.setCellValueFactory(new PropertyValueFactory<>("biografia"));
        tabela.getColumns().add(cId);
        tabela.getColumns().add(cNome);
        tabela.getColumns().add(cEmail);
        tabela.getColumns().add(cData);
        tabela.getColumns().add(cBio);
        atualizarTabela();

        tabela.getSelectionModel().selectedItemProperty().addListener((o, a, sel) -> {
            if (sel != null) {
                idEmEdicao = sel.getId();
                campoNome.setText(sel.getNome());
                campoEmail.setText(sel.getEmail());
                campoSenha.setText(sel.getSenha());
                campoData.setValue(sel.getDataNascimento());
                campoBio.setText(sel.getBiografia());
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
            String bio   = Validadores.texto("Biografia", campoBio.getText());

            if (idEmEdicao == null) {
                repo.inserir(new Mentor(nome, email, senha, nasc, bio));
                Alertas.sucesso("Mentor inserido.");
            } else {
                Mentor m = new Mentor(nome, email, senha, nasc, bio);
                m.setId(idEmEdicao);
                repo.atualizar(m);
                Alertas.sucesso("Mentor atualizado.");
            }
            limparFormulario();
            atualizarTabela();
        } catch (ValidacaoException ex) {
            Alertas.erro(ex.getMessage());
        }
    }

    private void excluir() {
        Mentor sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) { Alertas.erro("Selecione um registro na tabela."); return; }
        repo.excluir(sel.getId());
        Alertas.sucesso("Mentor excluido.");
        limparFormulario();
        atualizarTabela();
    }

    private void atualizarTabela() { tabela.getItems().setAll(repo.listar()); }

    private void limparFormulario() {
        idEmEdicao = null;
        campoNome.clear(); campoEmail.clear(); campoSenha.clear();
        campoData.setValue(null); campoBio.clear();
        tabela.getSelectionModel().clearSelection();
    }
}
