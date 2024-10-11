package co.edu.uniquindio.poo.dataBase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class BuildBaseDeDatos {

    // Método para crear la tabla "persona" si no existe
    public static void crearTablasUsuarios( ) {
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

    public static void crearTablaEvento(){
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        try{
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
        }catch (Exception e) {
            System.out.println("Error al crear la tabla: " + e);
        }
    }
    public static void crearTablaPersonas(){
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\DB.db";

        try{
            Connection con = DriverManager.getConnection(url);
            Statement smt = con.createStatement();

            smt.executeUpdate("CREATE TABLE \"persona\" (\r\n" + //
                                "\t\"id\"\tINTEGER NOT NULL UNIQUE,\r\n" + //
                                "\t\"id_evento\"\tINTEGER NOT NULL UNIQUE,\r\n" + //
                                "\t\"id_silla\"\tINTEGER NOT NULL UNIQUE,\r\n" + //
                                "\t\"tipo_silla\"\tINTEGER NOT NULL,\r\n" + //
                                "\t\"nombre_persona\"\tTEXT NOT NULL,\r\n" + //
                                "\t\"id_persona\"\tINTEGER NOT NULL UNIQUE,\r\n" + //
                                "\t\"total_pagar\"\tINTEGER NOT NULL,\r\n" + //
                                "\tPRIMARY KEY(\"id\")\r\n" + //
                                ");");
            smt.close();
            con.close();

            System.out.println("Tabla creada exitosamente (si no existía).");
        }catch (Exception e) {
            System.out.println("Error al crear la tabla: " + e);
        }
    }
    public static void crearSillasVip( ){
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\"+"DB.db";

        try{
            Connection con = DriverManager.getConnection(url);
            Statement smt = con.createStatement();

            smt.executeUpdate("CREATE TABLE \"sillas_vip\" (\r\n" + //
                                "\t\"id\"\tINTEGER NOT NULL UNIQUE,\r\n" + //
                                "\t\"nombre\"\tINTEGER NOT NULL,\r\n" + //
                                "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\r\n" + //
                                ");");
            smt.close();
            con.close();

            System.out.println("Tabla creada exitosamente (si no existía).");
        }catch (Exception e) {
            System.out.println("Error al crear la tabla: " + e);
        }
    }

    public static void crearSillas(){
        String url = "jdbc:sqlite:src\\main\\java\\co\\edu\\uniquindio\\poo\\dataBase\\DB\\"+"DB.db";

        try{
            Connection con = DriverManager.getConnection(url);
            Statement smt = con.createStatement();

            smt.executeUpdate("CREATE TABLE \"sillas\" (\r\n" + //
                                "\t\"id\"\tINTEGER NOT NULL UNIQUE,\r\n" + //
                                "\t\"nombre\"\tINTEGER NOT NULL,\r\n" + //
                                "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\r\n" + //
                                ");");
            smt.close();
            con.close();

            System.out.println("Tabla creada exitosamente (si no existía).");
        }catch (Exception e) {
            System.out.println("Error al crear la tabla: " + e);
        }
    }
}