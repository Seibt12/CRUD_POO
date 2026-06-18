package eduvfinance.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import eduvfinance.util.Identificavel;

public abstract class RepositorioArquivo<T extends Identificavel> {

    private final String arquivo;
    protected List<T> registros = new ArrayList<>();
    private int proximoId = 1;

    protected RepositorioArquivo(String arquivo) {
        this.arquivo = arquivo;
        carregar();
    }

    @SuppressWarnings("unchecked")
    private void carregar() {
        File f = new File(arquivo);
        if (!f.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            registros = (List<T>) in.readObject();
            proximoId = registros.stream().mapToInt(Identificavel::getId).max().orElse(0) + 1;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar " + arquivo + ": " + e.getMessage(), e);
        }
    }

    private void salvar() {
        File pai = new File(arquivo).getParentFile();
        if (pai != null) pai.mkdirs();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            out.writeObject(registros);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar " + arquivo + ": " + e.getMessage(), e);
        }
    }

    public T inserir(T obj) { obj.setId(proximoId++); registros.add(obj); salvar(); return obj; }

    public List<T> listar() { return new ArrayList<>(registros); }

    public void atualizar(T obj) {
        for (int i = 0; i < registros.size(); i++) {
            if (registros.get(i).getId() == obj.getId()) { registros.set(i, obj); salvar(); return; }
        }
        throw new NoSuchElementException("Registro id=" + obj.getId() + " nao encontrado.");
    }

    public void excluir(int id) {
        if (!registros.removeIf(r -> r.getId() == id))
            throw new NoSuchElementException("Registro id=" + id + " nao encontrado.");
        salvar();
    }
}
