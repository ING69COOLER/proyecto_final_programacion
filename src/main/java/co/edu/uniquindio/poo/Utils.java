package co.edu.uniquindio.poo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class Utils {
    static String clave_empresarial = "1234";

    public static String getClave_empresarial() {
        return clave_empresarial;

        
    }

    public  boolean usuarioExiste(String usuario) {
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
        String query = "SELECT * FROM Usuarios WHERE user = ?";

        try (Connection con = DriverManager.getConnection(url);
                PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, usuario); // Asignar el nombre de usuario al parámetro

            // Ejecutar la consulta
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true; // Si hay un resultado, el usuario ya existe
            }
        } catch (Exception e) {
            System.out.println("Error al verificar si el usuario existe: " + e);
        }

        return false; // Si no hay resultados, el usuario no existe
    }

    public  boolean verificarClave(String usuario, String clave) {
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
        boolean coincide = false; // Variable para almacenar el resultado

        try {
            // Conectar a la base de datos
            Connection con = DriverManager.getConnection(url);
            // Usar PreparedStatement para evitar inyecciones SQL
            String sql = "SELECT * FROM Usuarios WHERE user = ? AND password = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, usuario); // Establecer el usuario
            pstmt.setString(2, clave);    // Establecer la clave

            // Ejecutar la consulta
            ResultSet rs = pstmt.executeQuery();

            // Verificar si hay algún resultado
            if (rs.next()) {
                coincide = true; // Hay coincidencia
            }

            // Cerrar los recursos
            rs.close();
            pstmt.close();
            con.close();

        } catch (Exception e) {
            System.out.println("Error al verificar la clave: " + e);
        }

        return coincide; // Retornar el resultado
    }

    
}
