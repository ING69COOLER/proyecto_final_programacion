package co.edu.uniquindio.poo.menuPrincipal;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import co.edu.uniquindio.poo.App;
import co.edu.uniquindio.poo.editar_Evento.EditarEventoController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    private VBox vboxEventos;

    // Método para abrir la ventana de balance
    @FXML
    void abrir_ventana_balance(ActionEvent event) {
    }

    // Método para cambiar a la ventana de inicio de sesión
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("inicio_Sesion");
    }

    // Método para cambiar a la ventana de crear evento
    @FXML
    private void ventana_agregar_evento() throws IOException {
        App.setRoot("crear_evento");
    }

    // Método para cargar los eventos desde la base de datos
    @FXML
    private void cargarEventos() {
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        try {
            Connection con = DriverManager.getConnection(url);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Evento");

            while (rs.next()) {
                String nombreEvento = rs.getString("Nombre");
                int idEvento = rs.getInt("Id");

                // Crear un botón para cada evento
                Button eventoBtn = new Button(nombreEvento);
                eventoBtn.setStyle("-fx-background-color: #FF8C00; -fx-text-fill: white;");
                eventoBtn.setPrefWidth(200);
                eventoBtn.setOnAction(e -> editarEvento(idEvento));

                // Agregar el botón al VBox
                vboxEventos.getChildren().add(eventoBtn);
            }

            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Error al cargar los eventos: " + e);
        }
    }

    private void editarEvento(int idEvento) {
        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/editar_evento.fxml"));
            Parent root = loader.load();
    
            // Obtener el controlador y pasarle el ID del evento
            EditarEventoController controller = loader.getController();
            controller.cargarDatosEvento(idEvento);  // Pasar el ID del evento
    
            // Configurar y mostrar la ventana de edición
            Stage stage = new Stage();
            stage.setTitle("Editar Evento");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    // Este método se llama al inicializar el controlador
    @FXML
    void initialize() {
        cargarEventos(); // Cargar los eventos al abrir la ventana
    }
}
