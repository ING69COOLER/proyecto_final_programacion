package co.edu.uniquindio.poo.inicioSesion;
import java.io.IOException;

import co.edu.uniquindio.poo.App;
import co.edu.uniquindio.poo.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class InicioSesionController extends Utils {
    @FXML
    private Button btn_Inicio_Sesion;

    @FXML
    private Button btn_registrar_usuario;

    @FXML
    private TextField txt_Password;

    @FXML
    private TextField txt_User;

    @FXML
    private void registrar_Usuario() throws IOException {
        App.setRoot("registro");
    }

    @FXML
    private void iniciar_sesion() throws IOException {
        String user = txt_User.getText();
        String password = txt_Password.getText();

        if (usuarioExiste(user) && verificarClave(user, password)) {
            App.setRoot("menu_principal");
            
        } else {
            mostrarAlerta("Error al ingresar", "La el usuario o la contraseña son invalidos");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}