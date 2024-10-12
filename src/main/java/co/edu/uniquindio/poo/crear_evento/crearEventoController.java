package co.edu.uniquindio.poo.crear_evento;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import co.edu.uniquindio.poo.App;
import co.edu.uniquindio.poo.dataBase.BuildBaseDeDatos;
import co.edu.uniquindio.poo.dataBase.DBUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class crearEventoController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_crear;

    @FXML
    private Button btn_regresar;

    @FXML
    private CheckBox chc_concierto;

    @FXML
    private CheckBox chc_partido;

    @FXML
    private TextField txt_costo_evento;

    @FXML
    private TextField txt_nombre_evento;

    @FXML
    private TextField txt_porcentaje_extra_evento_vip;

    @FXML
    private void crear_evento( ) throws IOException {
        String nombre = "concietito";
        BuildBaseDeDatos.crearTablaEvento();
        DBUtils.agregarEvento(nombre, 50000, "Concieto", 0.05);
        

        App.setRoot("menu_principal");
    }

    @FXML
    void regresar( ) throws IOException {
        App.setRoot("menu_principal");
    }

    @FXML
    void initialize() {
        assert btn_crear != null : "fx:id=\"btn_crear\" was not injected: check your FXML file 'crear_evento.fxml'.";
        assert btn_regresar != null : "fx:id=\"btn_regresar\" was not injected: check your FXML file 'crear_evento.fxml'.";
        assert chc_concierto != null : "fx:id=\"chc_concierto\" was not injected: check your FXML file 'crear_evento.fxml'.";
        assert chc_partido != null : "fx:id=\"chc_partido\" was not injected: check your FXML file 'crear_evento.fxml'.";
        assert txt_costo_evento != null : "fx:id=\"txt_costo_evento\" was not injected: check your FXML file 'crear_evento.fxml'.";
        assert txt_nombre_evento != null : "fx:id=\"txt_nombre_evento\" was not injected: check your FXML file 'crear_evento.fxml'.";
        assert txt_porcentaje_extra_evento_vip != null : "fx:id=\"txt_porcentaje_extra_evento_vip\" was not injected: check your FXML file 'crear_evento.fxml'.";

    }

}
