package co.edu.uniquindio.poo.editar_Evento;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class EditarEventoController {

    @FXML
    private Label lblNombreEvento, lblCostoEvento, lblTotalPagar;

    @FXML
    private GridPane gridSillas, gridSillasVip;

    @FXML
    private TextField txtNombrePersona, txtIdPersona;

    private int idEvento;
    private double costoRegular;
    private double porcentajeExtra;

    // Un único ToggleGroup para manejar la selección de sillas
    private ToggleGroup toggleGroupSillas = new ToggleGroup();

    @FXML
    public void initialize() {
        // Este método se llama automáticamente al cargar la ventana
    }

    public void cargarDatosEvento(int idEvento) {
        this.idEvento = idEvento;
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
        
        try (Connection con = DriverManager.getConnection(url)) {
            cargarDetallesEvento(con);
            cargarSillasDisponibles("SELECT id, nombre FROM sillas_regular", gridSillas, "sillas_regular");
            cargarSillasDisponibles("SELECT id, nombre FROM sillas_vip", gridSillasVip, "sillas_vip");
        } catch (Exception e) {
            System.out.println("Error al cargar los datos del evento: " + e);
        }
    }
    
    private void cargarDetallesEvento(Connection con) throws SQLException {
        String queryEvento = "SELECT Nombre, Costo, porcentaje_extra FROM Evento WHERE Id = ?";
        try (PreparedStatement psEvento = con.prepareStatement(queryEvento)) {
            psEvento.setInt(1, idEvento);
            try (ResultSet rsEvento = psEvento.executeQuery()) {
                if (rsEvento.next()) {
                    lblNombreEvento.setText(rsEvento.getString("Nombre"));
                    costoRegular = rsEvento.getDouble("Costo");
                    porcentajeExtra = rsEvento.getDouble("porcentaje_extra");
                    lblCostoEvento.setText("Costo: $" + costoRegular);
                }
            }
        }
    }
    



    private void cargarSillasDisponibles(String query, GridPane grid, String tipoSillaQuery) {
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        try (Connection con = DriverManager.getConnection(url)) {
            Set<Integer> sillasOcupadas = obtenerSillasOcupadas(con, tipoSillaQuery);
            cargarSillasEnGrid(con, query, grid, sillasOcupadas);
        } catch (Exception e) {
            System.out.println("Error al cargar las sillas: " + e);
        }
    }

    private Set<Integer> obtenerSillasOcupadas(Connection con, String tipoSillaQuery) throws SQLException {
        String querySillasOcupadas = "SELECT id_silla FROM persona WHERE id_evento = ? AND tipo_silla = ?";
        try (PreparedStatement psOcupadas = con.prepareStatement(querySillasOcupadas)) {
            psOcupadas.setInt(1, idEvento); // Usar el id del evento actual
            psOcupadas.setString(2, tipoSillaQuery); // Filtrar por el tipo de silla (regular o VIP)
            ResultSet rsOcupadas = psOcupadas.executeQuery();

            Set<Integer> sillasOcupadas = new HashSet<>();
            while (rsOcupadas.next()) {
                int idSilla = rsOcupadas.getInt("id_silla");
                sillasOcupadas.add(idSilla);
                System.out.println("Silla ocupada: ID=" + idSilla); // Depuración
            }
            return sillasOcupadas;
        }
    }

    private void cargarSillasEnGrid(Connection con, String query, GridPane grid, Set<Integer> sillasOcupadas)
            throws SQLException {
        try (PreparedStatement psSillas = con.prepareStatement(query);
                ResultSet rsSillas = psSillas.executeQuery()) {

            int row = 0, col = 0;

            while (rsSillas.next()) {
                int idSilla = rsSillas.getInt("id");
                String nombreSilla = rsSillas.getString("nombre");
                RadioButton silla = crearBotonSilla(nombreSilla, idSilla, sillasOcupadas);

                grid.add(silla, col++, row);

                if (col == 5) { // Cambiar de fila después de 5 columnas
                    col = 0;
                    row++;
                }
            }
        }
    }

    private RadioButton crearBotonSilla(String nombreSilla, int idSilla, Set<Integer> sillasOcupadas) {
        RadioButton silla = new RadioButton(nombreSilla);
        silla.setToggleGroup(toggleGroupSillas); // Asignar el RadioButton al grupo

        boolean esOcupada = sillasOcupadas.contains(idSilla);
        System.out.println("Verificando silla: ID=" + idSilla + ", Nombre=" + nombreSilla + ", Ocupada=" + esOcupada); // Depuración

        if (esOcupada) {
            silla.setStyle("-fx-text-fill: red;"); // Cambiar el color del texto a rojo si está ocupada
            silla.setDisable(true); // Deshabilitar si está ocupada
        }

        silla.setOnAction(e -> calcularTotalPagar());
        return silla;
    }

    @FXML
    private void calcularTotalPagar() {
        double totalPagar = costoRegular; // Iniciar con el costo regular del evento

        // Verificar si se seleccionó una silla
        if (toggleGroupSillas.getSelectedToggle() != null) {
            RadioButton sillaSeleccionada = (RadioButton) toggleGroupSillas.getSelectedToggle();
            // Verificar si es una silla VIP
            boolean esVip = gridSillasVip.getChildren().stream()
                    .anyMatch(node -> node instanceof RadioButton && node.equals(sillaSeleccionada));

            if (esVip) {
                totalPagar += (costoRegular * porcentajeExtra / 100); // Sumar el porcentaje adicional
            }
        }

        lblTotalPagar.setText("Total a Pagar: $" + totalPagar); // Actualizar el total en la etiqueta
    }

    @FXML
    private void guardarCliente() {
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        try (Connection con = DriverManager.getConnection(url)) {
            if (idPersonaAsignadaEvento(con)) {
                System.out.println("Error: El id_persona ya está asignado a este evento. No se puede duplicar.");
                return;
            }

            int idSilla;
            String tipoSilla;
            try {
                idSilla = obtenerIdSillaSeleccionada();
                tipoSilla = obtenerTipoSillaSeleccionada();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }

            if (combinacionSillaYaAsignada(con, idSilla, tipoSilla)) {
                System.out.println("Error: La combinación de evento, silla y tipo ya está asignada.");
                return;
            }

            insertarCliente(con, idSilla, tipoSilla);
            cerrarVentana();
        } catch (Exception e) {
            System.out.println("Error al guardar el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean idPersonaAsignadaEvento(Connection con) throws SQLException {
        String queryCheckIdEvento = "SELECT COUNT(*) FROM persona WHERE id_persona = ? AND id_evento = ?";
        try (PreparedStatement psCheckIdEvento = con.prepareStatement(queryCheckIdEvento)) {
            psCheckIdEvento.setInt(1, Integer.parseInt(txtIdPersona.getText()));
            psCheckIdEvento.setInt(2, idEvento); // Usar el id del evento actual
            try (ResultSet rsCheckIdEvento = psCheckIdEvento.executeQuery()) {
                rsCheckIdEvento.next();
                return rsCheckIdEvento.getInt(1) > 0;
            }
        }
    }

    private int obtenerIdSillaSeleccionada() {
        RadioButton sillaSeleccionada = (RadioButton) toggleGroupSillas.getSelectedToggle();
        if (sillaSeleccionada != null) {
            if (gridSillas.getChildren().contains(sillaSeleccionada)) {
                return gridSillas.getChildren().indexOf(sillaSeleccionada) + 1; // Sillas regulares
            } else if (gridSillasVip.getChildren().contains(sillaSeleccionada)) {
                return gridSillasVip.getChildren().indexOf(sillaSeleccionada) + 1; // Sillas VIP
            }
        }
        throw new IllegalStateException("Error: No se ha seleccionado ninguna silla.");
    }

    private String obtenerTipoSillaSeleccionada() {
        RadioButton sillaSeleccionada = (RadioButton) toggleGroupSillas.getSelectedToggle();
        if (sillaSeleccionada != null) {
            if (gridSillas.getChildren().contains(sillaSeleccionada)) {
                return "sillas_regular";
            } else if (gridSillasVip.getChildren().contains(sillaSeleccionada)) {
                return "sillas_vip";
            }
        }
        throw new IllegalStateException("Error: No se ha seleccionado ninguna silla.");
    }

    private boolean combinacionSillaYaAsignada(Connection con, int idSilla, String tipoSilla) throws SQLException {
        String queryCheck = "SELECT COUNT(*) FROM persona WHERE id_evento = ? AND id_silla = ? AND tipo_silla = ?";
        try (PreparedStatement psCheck = con.prepareStatement(queryCheck)) {
            psCheck.setInt(1, idEvento);
            psCheck.setInt(2, idSilla);
            psCheck.setString(3, tipoSilla);
            try (ResultSet rsCheck = psCheck.executeQuery()) {
                rsCheck.next();
                return rsCheck.getInt(1) > 0;
            }
        }
    }

    private void insertarCliente(Connection con, int idSilla, String tipoSilla) throws SQLException {
        String queryInsert = "INSERT INTO persona (id_evento, id_silla, tipo_silla, nombre_persona, id_persona, total_pagar) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement psInsert = con.prepareStatement(queryInsert)) {
            psInsert.setInt(1, idEvento);
            psInsert.setInt(2, idSilla);
            psInsert.setString(3, tipoSilla);
            psInsert.setString(4, txtNombrePersona.getText());
            psInsert.setInt(5, Integer.parseInt(txtIdPersona.getText()));
            psInsert.setDouble(6, Double.parseDouble(lblTotalPagar.getText().replace("Total a Pagar: $", "")));

            psInsert.executeUpdate();
            System.out.println("Cliente guardado exitosamente.");
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombrePersona.getScene().getWindow();
        stage.close();
    }

}
