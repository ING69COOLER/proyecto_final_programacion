package co.edu.uniquindio.poo.dataBase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DBUtils {

    public static void agregarEvento(String nombre, int costo, String tipo, double porcentajeExtra) {
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
        String query = "INSERT INTO Evento (Nombre, Costo, Tipo, porcentaje_extra) VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(query)) {
            
            // Asignar los valores a los parámetros de la consulta
            pstmt.setString(1, nombre);
            pstmt.setInt(2, costo);
            pstmt.setString(3, tipo);
            pstmt.setDouble(4, porcentajeExtra);

            // Ejecutar la consulta
            pstmt.executeUpdate();

            System.out.println("Evento agregado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al agregar evento: " + e);
        }
    }

    public static void agregarUsuarios(String user, String password) {
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
        String query = "INSERT INTO Usuarios (user, password) VALUES (?, ?)";
    
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(query)) {
            
            // Asignar los valores a los parámetros de la consulta
            pstmt.setString(1, user);
            pstmt.setString(2, password);
    
            // Ejecutar la consulta
            pstmt.executeUpdate();
    
            System.out.println("Usuario agregado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al agregar usuario: " + e);
        }
    }
 

    public static void agregarPersona(int idEvento, int idSilla, String tipoSilla, String nombrePersona, int idPersona, int totalPagar) {
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
        String query = "INSERT INTO persona (id_evento, id_silla, tipo_silla, nombre_persona, id_persona, total_pagar) VALUES (?, ?, ?, ?, ?, ?)";
    
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement pstmt = con.prepareStatement(query)) {
            
            // Asignar los valores a los parámetros de la consulta
            pstmt.setInt(1, idEvento);
            pstmt.setInt(2, idSilla);
            pstmt.setString(3, tipoSilla);
            pstmt.setString(4, nombrePersona);
            pstmt.setInt(5, idPersona);
            pstmt.setInt(6, totalPagar);
    
            // Ejecutar la consulta
            pstmt.executeUpdate();
    
            System.out.println("Persona agregada exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al agregar persona: " + e);
        }
    }
    
    
    
}
