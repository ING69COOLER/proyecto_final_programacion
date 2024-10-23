package co.edu.uniquindio.poo.menuPrincipal;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import co.edu.uniquindio.poo.App;
import co.edu.uniquindio.poo.Utils;
import co.edu.uniquindio.poo.editar_Evento.EditarEventoController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

public class MenuPrincipalController implements Utils, IMenuPrincipal{

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

    @FXML
    private TextArea labelSillasLibres;

    @FXML
    private Button btn_eliminar_evento;

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
                eventoBtn.setStyle(
                        "-fx-background-color: linear-gradient(yellow, #FF8C00);" + // Degradado de blanco a naranja oscuro
                        "-fx-text-fill: black;" +    // Color de texto blanco
                        "-fx-font-family: 'Forte';" + // Fuente "Forte"
                        "-fx-background-radius: 20;" + // Bordes redondeados
                        "-fx-padding: 10px 20px;" +  // Espaciado interno (alto y ancho)
                        "-fx-font-size: 14px;" +     // Tamaño de fuente
                        "-fx-border-radius: 20;"     // Bordes redondeados en el borde exterior
                    );

                
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

    
    @FXML
    void eliminar_evento(ActionEvent event) {
        Optional<Pair<String, String>> result = mostrarDialogoEliminarEvento();
        result.ifPresent(datos -> {
            String nombreEvento = datos.getKey();
            String claveEmpresarial = datos.getValue();

            // Validar clave empresarial
            if (!validarClaveEmpresarial(claveEmpresarial)) {
                System.out.println("Clave empresarial incorrecta.");
                return;
            }

            // Eliminar el evento y personas relacionadas
            if (eliminarPersonasYEvento(nombreEvento)) {
                limpiarVistaEventos();
                cargarEventos();
            }
        });
    }
    // Este método se llama al inicializar el controlador
    @FXML
    void initialize() {
    
        cargarEventos(); // Cargar los eventos al abrir la ventana
        iniciarActualizacionAutomatica();
    }


    public void editarEvento(int idEvento) {
        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/editar_evento.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle el ID del evento
            EditarEventoController controller = loader.getController();
            controller.cargarDatosEvento(idEvento); // Pasar el ID del evento

            // Configurar y mostrar la ventana de edición
            Stage stage = new Stage();
            stage.setTitle("Editar Evento");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actualizarResumenSillas() {
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        try (Connection con = DriverManager.getConnection(url);
                Statement stmt = con.createStatement();
                ResultSet eventos = stmt.executeQuery("SELECT * FROM Evento")) {

            StringBuilder resumen = new StringBuilder();

            while (eventos.next()) {
                String nombreEvento = eventos.getString("Nombre");
                int idEvento = eventos.getInt("Id");

                // Obtener el resumen para el evento actual
                String eventoResumen = obtenerResumenEvento(con, idEvento, nombreEvento);
                resumen.append(eventoResumen);
            }

            // Actualiza el label con la información
            labelSillasLibres.setText(resumen.toString());

        } catch (Exception e) {
            System.out.println("Error al contar las sillas: " + e);
        }
    }

    private String obtenerResumenEvento(Connection con, int idEvento, String nombreEvento) throws SQLException {
        int sillasVip = contarSillasVip(con, idEvento);
        int sillasRegulares = contarSillasRegulares(con, idEvento);

        return String.format("Evento: %s\n  Sillas Regulares: %d\n  Sillas VIP: %d\n\n",
                nombreEvento, sillasRegulares, sillasVip);
    }

    private int contarSillasVip(Connection con, int idEvento) throws SQLException {
        String vipQuery = "SELECT COUNT(*) FROM sillas_vip WHERE id NOT IN " +
                "(SELECT id_silla FROM persona WHERE id_evento = ? AND tipo_silla = 'sillas_vip')";
        try (PreparedStatement pstmtVip = con.prepareStatement(vipQuery)) {
            pstmtVip.setInt(1, idEvento);
            ResultSet rsVip = pstmtVip.executeQuery();
            rsVip.next();
            return rsVip.getInt(1);
        }
    }

    private int contarSillasRegulares(Connection con, int idEvento) throws SQLException {
        String regularQuery = "SELECT COUNT(*) FROM sillas_regular WHERE id NOT IN " +
                "(SELECT id_silla FROM persona WHERE id_evento = ? AND tipo_silla = 'sillas_regular')";
        try (PreparedStatement pstmtRegular = con.prepareStatement(regularQuery)) {
            pstmtRegular.setInt(1, idEvento);
            ResultSet rsRegular = pstmtRegular.executeQuery();
            rsRegular.next();
            return rsRegular.getInt(1);
        }
    }

    

    public void iniciarActualizacionAutomatica() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> actualizarResumenSillas()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // Mostrar diálogo para ingresar nombre del evento y clave empresarial
    private Optional<Pair<String, String>> mostrarDialogoEliminarEvento() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Eliminar Evento");
        dialog.setHeaderText("Ingrese el nombre del evento y la clave empresarial para eliminarlo");

        ButtonType eliminarButtonType = new ButtonType("Eliminar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(eliminarButtonType, ButtonType.CANCEL);

        TextField txtNombreEvento = new TextField();
        txtNombreEvento.setPromptText("Nombre del evento");
        PasswordField txtClaveEmpresarial = new PasswordField();
        txtClaveEmpresarial.setPromptText("Clave empresarial");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Nombre del evento:"), 0, 0);
        grid.add(txtNombreEvento, 1, 0);
        grid.add(new Label("Clave empresarial:"), 0, 1);
        grid.add(txtClaveEmpresarial, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Node eliminarButton = dialog.getDialogPane().lookupButton(eliminarButtonType);
        eliminarButton.setDisable(true);

        txtNombreEvento.textProperty().addListener((observable, oldValue, newValue) -> {
            eliminarButton.setDisable(newValue.trim().isEmpty() || txtClaveEmpresarial.getText().trim().isEmpty());
        });
        txtClaveEmpresarial.textProperty().addListener((observable, oldValue, newValue) -> {
            eliminarButton.setDisable(newValue.trim().isEmpty() || txtNombreEvento.getText().trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == eliminarButtonType) {
                return new Pair<>(txtNombreEvento.getText(), txtClaveEmpresarial.getText());
            }
            return null;
        });

        return dialog.showAndWait();
    }

    // Validar la clave empresarial ingresada
    private boolean validarClaveEmpresarial(String claveEmpresarial) {
        return claveEmpresarial.equals(getClave_empresarial());
    }

    // Eliminar las personas asociadas y el evento de la base de datos
    private boolean eliminarPersonasYEvento(String nombreEvento) {
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        try (Connection con = DriverManager.getConnection(url)) {
            // Primero eliminar las personas relacionadas con el evento
            String queryEliminarPersonas = "DELETE FROM persona WHERE id_evento = (SELECT Id FROM Evento WHERE Nombre = ?)";
            try (PreparedStatement psEliminarPersonas = con.prepareStatement(queryEliminarPersonas)) {
                psEliminarPersonas.setString(1, nombreEvento);
                int personasEliminadas = psEliminarPersonas.executeUpdate();
                System.out.println("Se eliminaron " + personasEliminadas + " personas relacionadas con el evento.");
            }

            // Luego eliminar el evento
            String queryEliminarEvento = "DELETE FROM Evento WHERE Nombre = ?";
            try (PreparedStatement psEliminarEvento = con.prepareStatement(queryEliminarEvento)) {
                psEliminarEvento.setString(1, nombreEvento);
                int eventosEliminados = psEliminarEvento.executeUpdate();
                if (eventosEliminados > 0) {
                    System.out.println("El evento '" + nombreEvento + "' fue eliminado correctamente.");
                    return true;
                } else {
                    System.out.println("No se encontró el evento con el nombre: " + nombreEvento);
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar el evento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public void limpiarVistaEventos() {
        // Elimina todos los nodos hijos del VBox que contiene los botones de eventos
        vboxEventos.getChildren().clear();
    }
}
