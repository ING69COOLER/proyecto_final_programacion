package co.edu.uniquindio.poo.menuPrincipal;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import co.edu.uniquindio.poo.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuPrincipalController {

   

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_agregar_avento;

    @FXML
    private Button btn_balance;

    @FXML
    private Button primaryButton;

    @FXML
    void abrir_ventana_balance(ActionEvent event) {

    }
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("inicio_Sesion");
    }

    @FXML
   private void ventana_agregar_evento( ) throws IOException {
        App.setRoot("crear_evento");
    }
    
}