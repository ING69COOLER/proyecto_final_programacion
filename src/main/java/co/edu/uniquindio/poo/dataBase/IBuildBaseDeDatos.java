package co.edu.uniquindio.poo.dataBase;

// posibles soluciones
//1- crear crud de cada tabla
//2- usar composite para manejarlos todos
public interface IBuildBaseDeDatos {
    void crearTablaEvento();
    void crearTablaPersonas();
    void crearSillas();
    void crearSillasVip();
    void crearTablasUsuarios();
    
}
