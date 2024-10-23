package co.edu.uniquindio.poo.registro;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import co.edu.uniquindio.poo.App;
import co.edu.uniquindio.poo.Utils;
import co.edu.uniquindio.poo.dataBase.DBUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class RegistroController implements Utils {

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
    private TextField txt_usuario;

    @FXML
    private void registar() throws IOException {
        String usuario = txt_usuario.getText();
        String clave = txt_clave_empresarial.getText();
        String contraseña1 = txt_contraseña_1.getText();
        String contraseña2 = txt_contraseña_2.getText();

        // Verificar si las contraseñas coinciden y si la clave empresarial es correcta
        if (contraseña1.equals(contraseña2) && clave.equals(getClave_empresarial())) {
            // Verificar si el usuario ya existe en la base de datos
            if (usuarioExiste(usuario)) {
                System.out.println("El usuario ya existe. Intenta con otro nombre de usuario.");
            } else {
                // Agregar el usuario a la base de datos
                DBUtils.getInstancia().agregarUsuarios(usuario, contraseña1);

                System.out.println("Usuario registrado exitosamente.");
                
                // Redirigir a la ventana de inicio de sesión
                App.setRoot("inicio_Sesion");
            }
        } else {
            System.out.println("Las contraseñas no coinciden o la clave empresarial es incorrecta.");
        }
    }

    @FXML
    void regresar() throws IOException {
        App.setRoot("inicio_Sesion");
    }

}
