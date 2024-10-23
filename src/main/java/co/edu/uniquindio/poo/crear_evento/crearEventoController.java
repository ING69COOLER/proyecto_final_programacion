package co.edu.uniquindio.poo.crear_evento;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import co.edu.uniquindio.poo.App;
import co.edu.uniquindio.poo.dataBase.BuildBaseDeDatos;
import co.edu.uniquindio.poo.dataBase.DBUtils;
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
    private void crear_evento() throws IOException {
        try {
            // Obtener valores de los campos de texto
            String nombre = txt_nombre_evento.getText();
            int costo = Integer.parseInt(txt_costo_evento.getText());
            double porcentajeExtra = Double.parseDouble(txt_porcentaje_extra_evento_vip.getText());

            // Determinar el tipo de evento basado en las casillas de verificación
            String tipoEvento = "";
            if (chc_concierto.isSelected()) {
                tipoEvento = "Concierto";
            } else if (chc_partido.isSelected()) {
                tipoEvento = "Partido";
            }

            // Validar que se haya seleccionado un tipo de evento
            if (tipoEvento.isEmpty()) {
                System.out.println("Debes seleccionar el tipo de evento.");
                return;
            }

            // Agregar el evento a la base de datos
            DBUtils.getInstancia().agregarEvento(nombre, costo, tipoEvento, porcentajeExtra);

            // Redirigir al menú principal
            App.setRoot("menu_principal");

        } catch (NumberFormatException e) {
            System.out.println("Error: Asegúrate de ingresar números válidos para el costo y el porcentaje.");
        } catch (Exception e) {
            System.out.println("Error al crear el evento: " + e.getMessage());
        }
    }

    @FXML
    void regresar() throws IOException {
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

        // Lógica para deseleccionar una checkbox si la otra es seleccionada
        chc_concierto.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                chc_partido.setSelected(false);
            }
        });

        chc_partido.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                chc_concierto.setSelected(false);
            }
        });
    }
}
