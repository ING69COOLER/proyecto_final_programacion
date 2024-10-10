package co.edu.uniquindio.poo.inicioSesion;
import java.io.IOException;

import co.edu.uniquindio.poo.App;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class InicioSesionController {
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
    private void switchToPrimary() throws IOException {
        App.setRoot("menu_principal");
    }
}