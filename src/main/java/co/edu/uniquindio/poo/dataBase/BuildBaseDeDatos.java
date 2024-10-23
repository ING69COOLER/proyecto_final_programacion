package co.edu.uniquindio.poo.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class BuildBaseDeDatos implements IBuildBaseDeDatos {

    private static BuildBaseDeDatos instancia;

    // Método para crear la tabla "persona" si no existe
    public  void crearTablasUsuarios() {
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";
        try {
            // Conectar a la base de datos
            Connection con = DriverManager.getConnection(url);
            Statement smt = con.createStatement();

            // Crear la tabla solo si no existe, sin insertar datos
            smt.executeUpdate("CREATE TABLE IF NOT EXISTS Usuarios (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    "user TEXT NOT NULL, " +
                    "password TEXT NOT NULL);");

            // Cerrar la conexión
            smt.close();
            con.close();

            System.out.println("Tabla creada exitosamente (si no existía).");

        } catch (Exception e) {
            System.out.println("Error al crear la tabla: " + e);
        }
    }

    public void crearTablaEvento() {
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        try {
            Connection con = DriverManager.getConnection(url);
            Statement smt = con.createStatement();

            smt.executeUpdate("CREATE TABLE \"Evento\" (\r\n" + //
                    "\t\"Id\"\tINTEGER NOT NULL UNIQUE,\r\n" + //
                    "\t\"Nombre\"\tTEXT NOT NULL,\r\n" + //
                    "\t\"Costo\"\tINTEGER NOT NULL,\r\n" + //
                    "\t\"Tipo\"\tTEXT NOT NULL,\r\n" + //
                    "\t\"porcentaje_extra\"\tREAL NOT NULL,\r\n" + //
                    "\tPRIMARY KEY(\"Id\" AUTOINCREMENT)\r\n" + //
                    ");");
            smt.close();
            con.close();

            System.out.println("Tabla creada exitosamente (si no existía).");
        } catch (Exception e) {
            System.out.println("Error al crear la tabla: " + e);
        }
    }

    public  void crearTablaPersonas() {
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        try {
            Connection con = DriverManager.getConnection(url);
            Statement smt = con.createStatement();

            smt.executeUpdate("CREATE TABLE \"persona\" (\r\n" + //
                    "\t\"id\"\tINTEGER NOT NULL UNIQUE,\r\n" + //
                    "\t\"id_evento\"\tINTEGER NOT NULL ,\r\n" + //
                    "\t\"id_silla\"\tINTEGER NOT NULL ,\r\n" + //
                    "\t\"tipo_silla\"\tTEXT NOT NULL ,\r\n" + //
                    "\t\"nombre_persona\"\tTEXT NOT NULL,\r\n" + //
                    "\t\"id_persona\"\tINTEGER NOT NULL ,\r\n" + //
                    "\t\"total_pagar\"\tINTEGER NOT NULL,\r\n" + //
                    "\tPRIMARY KEY(\"id\")\r\n" + //
                    ");");
            smt.close();
            con.close();

            System.out.println("Tabla creada exitosamente (si no existía).");
        } catch (Exception e) {
            System.out.println("Error al crear la tabla: " + e);
        }
    }

    public  void crearSillasVip() {
    String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

    try (Connection con = DriverManager.getConnection(url);
         Statement smt = con.createStatement()) {

        // Crear la tabla sillas_vip si no existe
        smt.executeUpdate("CREATE TABLE IF NOT EXISTS \"sillas_vip\" (\r\n" +
                "\t\"id\"\tINTEGER NOT NULL UNIQUE,\r\n" +
                "\t\"nombre\"\tTEXT NOT NULL,\r\n" + // Cambiado a TEXT para las letras
                "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\r\n" +
                ");");

        // Verificar si ya existen registros en la tabla
        String countQuery = "SELECT COUNT(*) FROM sillas_vip";
        try (Statement stmtCount = con.createStatement();
             ResultSet rsCount = stmtCount.executeQuery(countQuery)) {

            rsCount.next();
            int count = rsCount.getInt(1);

            if (count > 0) {
                System.out.println("Las sillas VIP ya están creadas. No se insertarán nuevas.");
                return; // Salir si ya hay registros
            }
        }

        // Insertar sillas VIP si no hay registros
        String insertQuery = "INSERT INTO sillas_vip (nombre) VALUES (?)";
        try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            for (int i = 0; i < 10; i++) {
                char letra = (char) ('A' + (i % 26)); // Obtener una letra del alfabeto (se repiten después de 'Z')
                pstmt.setString(1, String.valueOf(letra)); // Asignar la letra de silla
                pstmt.executeUpdate();
            }
        }

        System.out.println("Sillas VIP insertadas exitosamente.");
    } catch (Exception e) {
        System.out.println("Error al crear o insertar sillas VIP: " + e);
    }
}

    public  void crearSillas() {
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        try (Connection con = DriverManager.getConnection(url);
            Statement smt = con.createStatement()) {

            // Crear la tabla sillas si no existe
            smt.executeUpdate("CREATE TABLE IF NOT EXISTS \"sillas_regular\" (\r\n" +
                    "\t\"id\"\tINTEGER NOT NULL UNIQUE,\r\n" +
                    "\t\"nombre\"\tTEXT NOT NULL,\r\n" + // Cambiado a TEXT para las letras
                    "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\r\n" +
                    ");");

            // Verificar si ya existen registros en la tabla
            String countQuery = "SELECT COUNT(*) FROM sillas_regular";
            try (Statement stmtCount = con.createStatement();
                ResultSet rsCount = stmtCount.executeQuery(countQuery)) {

                rsCount.next();
                int count = rsCount.getInt(1);

                if (count > 0) {
                    System.out.println("Las sillas regulares ya están creadas. No se insertarán nuevas.");
                    return; // Salir si ya hay registros
                }
            }

            // Insertar sillas regulares si no hay registros
            String insertQuery = "INSERT INTO sillas_regular (nombre) VALUES (?)";
            try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
                for (int i = 0; i < 10; i++) {
                    char letra = (char) ('A' + (i % 26)); // Obtener una letra del alfabeto (se repiten después de 'Z')
                    pstmt.setString(1, String.valueOf(letra)); // Asignar la letra de silla
                    pstmt.executeUpdate();
                }
            }

            System.out.println("Sillas regulares insertadas exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al crear o insertar sillas regulares: " + e);
        }
    }


    public void crearTablas(){
        crearSillas();
        crearSillasVip();
        crearTablaEvento();
        crearTablaPersonas();
        crearTablasUsuarios();
    }

    public static BuildBaseDeDatos getInstancia() {
        if (instancia == null) {
            instancia = new BuildBaseDeDatos();
        }
        return instancia;  
    }



}