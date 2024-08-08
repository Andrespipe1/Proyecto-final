package MinimarketPro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBd {
    /**
     * Establece una conexión con la base de datos utilizando JDBC.
     *
     * @return La conexión a la base de datos.
     * @throws SQLException Si ocurre un error al establecer la conexión.
     */
    public static Connection obtenerConexion() throws SQLException {
        String url = "jdbc:mysql://uvbmbtmpi0evah2t:MYVCKxotJa0TSwg1SAT3@b4i0oz9mmhxht77tkqpd-mysql.services.clever-cloud.com:3306/b4i0oz9mmhxht77tkqpd";
        String user = "uvbmbtmpi0evah2t";
        String password = "MYVCKxotJa0TSwg1SAT3";
        return DriverManager.getConnection(url, user, password);
    }
}