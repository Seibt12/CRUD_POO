# EduvFinance Desktop — RA3 (POO em Java + JavaFX)

Versão desktop da plataforma de educação financeira **EduvFinance**, desenvolvida
para a avaliação RA3 de Programação Orientada a Objetos. A aplicação faz **CRUD**
(Inserção, Consulta, Atualização e Exclusão) de 12 entidades de domínio, com
persistência em arquivo e interface JavaFX construída **inteiramente por código**
(sem FXML e sem SceneBuilder).

## Requisitos

- **JDK 21** instalado.
- Maven (baixa o JavaFX automaticamente — não é preciso instalar o SDK à mão).

## Como rodar

No terminal, na raiz do projeto (onde está o `pom.xml`):

```bash
mvn clean javafx:run
```

No **VSCode**: instale o *Extension Pack for Java* e rode pelo terminal com `mvn javafx:run`.
No **IntelliJ**: `File → Open` apontando para a pasta do projeto; aba *Maven → Plugins → javafx → javafx:run`.

## Arquitetura

Camadas: `Main` → `view` → `repository` → `model`. A View nunca lê arquivo direto;
sempre passa pelo repositório. Os arquivos `.dat` são gerados em runtime na pasta
`data/` na raiz do projeto.

```
src/main/java/eduvfinance/
├── Main.java                  → janela principal (uma aba por entidade)
├── model/                     → 12 entidades + 7 enums (Serializable)
├── repository/                → persistência em arquivo (RepositorioArquivo genérico)
├── view/                      → telas JavaFX (uma por entidade)
└── util/                      → Identificavel, ValidacaoException, Validadores, Alertas
```

## Entidades (12)

Aprendiz · Mentor · Administrador · Curso · Aula · Matricula · Pagamento ·
Lancamento · Categoria · MetaFinanceira · Simulacao · Avaliacao.

Cada entidade tem Model (`implements Identificavel`, ≥3 atributos), um repositório
próprio e uma tela CRUD com formulário, `TableView`, edição e exclusão. Datas no
formato pt-BR `DD/MM/AAAA`, campos numéricos e enums validados com tratamento de
exceções.

## Divisão por aluno

| Aluno   | Classe 1        | Classe 2        | Frente do sistema          |
|---------|-----------------|-----------------|----------------------------|
| Aluno 1 | Aprendiz        | Mentor          | Usuários da plataforma     |
| Aluno 2 | Curso           | Aula            | Catálogo / trilhas         |
| Aluno 3 | Matricula       | Pagamento       | Inscrição em cursos        |
| Aluno 4 | Lancamento      | Categoria       | Gestão financeira pessoal  |
| Aluno 5 | MetaFinanceira  | Simulacao       | Metas e investimentos      |
| Aluno 6 | Avaliacao       | Administrador   | Interação e administração  |
