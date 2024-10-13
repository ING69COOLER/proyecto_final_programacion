module co.edu.uniquindio.poo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;

    // Abre los paquetes necesarios para JavaFX
    opens co.edu.uniquindio.poo to javafx.fxml;
    opens co.edu.uniquindio.poo.inicioSesion to javafx.fxml;
    opens co.edu.uniquindio.poo.menuPrincipal to javafx.fxml;
    opens co.edu.uniquindio.poo.registro to javafx.fxml;
    opens co.edu.uniquindio.poo.crear_evento to javafx.fxml;
    opens co.edu.uniquindio.poo.editar_Evento to javafx.fxml;

    // Exporta los paquetes para su uso fuera del módulo
    exports co.edu.uniquindio.poo;
}
