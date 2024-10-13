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
        try {
            String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
            Connection con = DriverManager.getConnection(url);

            // Obtener el nombre y costo del evento
            String queryEvento = "SELECT Nombre, Costo, porcentaje_extra FROM Evento WHERE Id = ?";
            PreparedStatement psEvento = con.prepareStatement(queryEvento);
            psEvento.setInt(1, idEvento);
            ResultSet rsEvento = psEvento.executeQuery();
            if (rsEvento.next()) {
                lblNombreEvento.setText(rsEvento.getString("Nombre"));
                costoRegular = rsEvento.getDouble("Costo");
                porcentajeExtra = rsEvento.getDouble("porcentaje_extra");
                lblCostoEvento.setText("Costo: $" + costoRegular);
            }

            // Cargar las sillas regulares
            cargarSillasDisponibles("SELECT id, nombre FROM sillas_regular", gridSillas, "sillas_regular");

            // Cargar las sillas VIP
            cargarSillasDisponibles("SELECT id, nombre FROM sillas_vip", gridSillasVip, "sillas_vip");


            con.close();
        } catch (Exception e) {
            System.out.println("Error al cargar los datos del evento: " + e);
        }
    }

private void cargarSillasDisponibles(String query, GridPane grid, String tipoSillaQuery) {
    try {
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
        Connection con = DriverManager.getConnection(url);

        // Consultar las sillas ocupadas para el evento actual y el tipo de silla específico (regular o VIP)
        String querySillasOcupadas = "SELECT id_silla FROM persona WHERE id_evento = ? AND tipo_silla = ?";
        PreparedStatement psOcupadas = con.prepareStatement(querySillasOcupadas);
        psOcupadas.setInt(1, idEvento);  // Usamos el id del evento actual
        psOcupadas.setString(2, tipoSillaQuery);  // Filtramos por el tipo de silla ("sillas_regular" o "sillas_vip")
        ResultSet rsOcupadas = psOcupadas.executeQuery();

        // Almacenar las sillas ocupadas en un Set (no se requieren pares clave-valor)
        Set<Integer> sillasOcupadas = new HashSet<>();
        while (rsOcupadas.next()) {
            int idSilla = rsOcupadas.getInt("id_silla");
            sillasOcupadas.add(idSilla);
            // Mostrar datos para depuración
            System.out.println("Silla ocupada: ID=" + idSilla);
        }

        // Ejecutar la consulta para obtener todas las sillas (regulares o VIP)
        PreparedStatement psSillas = con.prepareStatement(query);
        ResultSet rsSillas = psSillas.executeQuery();
        int row = 0, col = 0;

        while (rsSillas.next()) {
            int idSilla = rsSillas.getInt("id");  // Obtener el ID real de la silla
            String nombreSilla = rsSillas.getString("nombre");
            RadioButton silla = new RadioButton(nombreSilla);
            silla.setToggleGroup(toggleGroupSillas);  // Asignar el RadioButton al mismo grupo

            // Verificar si la silla está ocupada
            boolean esOcupada = sillasOcupadas.contains(idSilla);

            // Mostrar datos de depuración sobre la silla
            System.out.println("Verificando silla: ID=" + idSilla + ", Nombre=" + nombreSilla + ", Ocupada=" + esOcupada);

            if (esOcupada) {
                silla.setStyle("-fx-text-fill: red;");  // Cambiar el color del texto a rojo si la silla está ocupada
                silla.setDisable(true);  // Opcional: Deshabilitar la silla si está ocupada
            }

            silla.setOnAction(e -> calcularTotalPagar());
            grid.add(silla, col++, row);

            if (col == 5) {  // Cambia de fila después de 5 columnas
                col = 0;
                row++;
            }
        }

        con.close();
    } catch (Exception e) {
        System.out.println("Error al cargar las sillas: " + e);
    }
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
            // Verificar si el id_persona ya está asignado a este evento
            String queryCheckIdEvento = "SELECT COUNT(*) FROM persona WHERE id_persona = ? AND id_evento = ?";
            try (PreparedStatement psCheckIdEvento = con.prepareStatement(queryCheckIdEvento)) {
                psCheckIdEvento.setInt(1, Integer.parseInt(txtIdPersona.getText()));
                psCheckIdEvento.setInt(2, idEvento); // Usar el id del evento actual
                try (ResultSet rsCheckIdEvento = psCheckIdEvento.executeQuery()) {
                    rsCheckIdEvento.next();
                    int countIdEvento = rsCheckIdEvento.getInt(1);
    
                    if (countIdEvento > 0) {
                        System.out.println("Error: El id_persona ya está asignado a este evento. No se puede duplicar.");
                        return; // Salir del método si el id_persona ya existe para el evento actual
                    }
                }
            }


            // Verificar si la combinación de evento, silla y tipo ya está asignada
            String queryCheck = "SELECT COUNT(*) FROM persona WHERE id_evento = ? AND id_silla = ? AND tipo_silla = ?";
            int idSilla = 0;
            String tipoSilla = "";

            // Obtener la silla seleccionada
            RadioButton sillaSeleccionada = (RadioButton) toggleGroupSillas.getSelectedToggle();
            if (sillaSeleccionada != null) {
                // Determinar si la silla seleccionada es de tipo regular o VIP
                if (gridSillas.getChildren().contains(sillaSeleccionada)) {
                    idSilla = gridSillas.getChildren().indexOf(sillaSeleccionada) + 1; // Asumiendo que el ID de la silla regular comienza en 1
                    tipoSilla = "sillas_regular";
                } else if (gridSillasVip.getChildren().contains(sillaSeleccionada)) {
                    idSilla = gridSillasVip.getChildren().indexOf(sillaSeleccionada) + 1; // Asumiendo que el ID de la silla VIP comienza en 1
                    tipoSilla = "sillas_vip";
                }
            }

            if (idSilla == 0 || tipoSilla.isEmpty()) {
                System.out.println("Error: No se ha seleccionado ninguna silla.");
                return;
            }

            try (PreparedStatement psCheck = con.prepareStatement(queryCheck)) {
                psCheck.setInt(1, idEvento);
                psCheck.setInt(2, idSilla);
                psCheck.setString(3, tipoSilla);
                try (ResultSet rsCheck = psCheck.executeQuery()) {
                    rsCheck.next();
                    int count = rsCheck.getInt(1);

                    if (count > 0) {
                        System.out.println("Error: La combinación de evento, silla y tipo ya está asignada.");
                        return; // Salir del método si la combinación ya está asignada
                    }
                }
            }

            // Insertar los datos del cliente
            String queryInsert = "INSERT INTO persona (id_evento, id_silla, tipo_silla, nombre_persona, id_persona, total_pagar) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psInsert = con.prepareStatement(queryInsert)) {
                psInsert.setInt(1, idEvento);
                psInsert.setInt(2, idSilla);
                psInsert.setString(3, tipoSilla);
                psInsert.setString(4, txtNombrePersona.getText());
                psInsert.setInt(5, Integer.parseInt(txtIdPersona.getText())); // Asumiendo que este valor es único
                psInsert.setDouble(6, Double.parseDouble(lblTotalPagar.getText().replace("Total a Pagar: $", "")));

                psInsert.executeUpdate();
                System.out.println("Cliente guardado exitosamente.");

                Stage stage = (Stage) txtNombrePersona.getScene().getWindow(); // Obtener el Stage desde cualquier nodo
                stage.close(); // Cerrar la ventana actual
            }
        } catch (Exception e) {
            System.out.println("Error al guardar el cliente: " + e.getMessage());
            e.printStackTrace(); // Para obtener más detalles en la consola
        }
    }

}
