package co.edu.uniquindio.poo.registro;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import co.edu.uniquindio.poo.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;



public class RegistroController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_registrar;

    @FXML
    private Button btn_regresar;

    @FXML
    private TextField txt_clave_empresarial;

    @FXML
    private TextField txt_contraseña_1;

    @FXML
    private TextField txt_contraseña_2;

    @FXML
    private TextField txt_ususario;

     @FXML
    private void registar() throws IOException {
        App.setRoot("inicio_Sesion");
    }

    @FXML
    void regresar( ) throws IOException {
        App.setRoot("inicio_Sesion");
    }

    @FXML
    void initialize() {
        assert btn_registrar != null : "fx:id=\"btn_registrar\" was not injected: check your FXML file 'registro.fxml'.";
        assert btn_regresar != null : "fx:id=\"btn_regresar\" was not injected: check your FXML file 'registro.fxml'.";
        assert txt_clave_empresarial != null : "fx:id=\"txt_clave_empresarial\" was not injected: check your FXML file 'registro.fxml'.";
        assert txt_contraseña_1 != null : "fx:id=\"txt_contraseña_1\" was not injected: check your FXML file 'registro.fxml'.";
        assert txt_contraseña_2 != null : "fx:id=\"txt_contraseña_2\" was not injected: check your FXML file 'registro.fxml'.";
        assert txt_ususario != null : "fx:id=\"txt_ususario\" was not injected: check your FXML file 'registro.fxml'.";

    }

}
