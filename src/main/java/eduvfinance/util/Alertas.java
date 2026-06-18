package eduvfinance.util;

import javafx.scene.control.Alert;

/**
 * Atalhos para exibir caixas de dialogo de erro e sucesso de forma padronizada.
 */
public final class Alertas {

    private Alertas() { } // classe utilitaria: nao instanciar

    public static void erro(String msg)    { mostrar(Alert.AlertType.ERROR, "Erro", msg); }
    public static void sucesso(String msg)  { mostrar(Alert.AlertType.INFORMATION, "Sucesso", msg); }

    private static void mostrar(Alert.AlertType t, String titulo, String msg) {
        Alert a = new Alert(t);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
