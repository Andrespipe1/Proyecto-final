package MinimarketPro;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Clase que representa la ventana de login para el sistema del minimarket.
 * Permite a los usuarios administradores y cajeros iniciar sesión en el sistema.
 *
 * @author Andres Tufiño
 * @version 1.0.01
 */
public class login extends JFrame{
    private JButton loginButton;
    private JPanel panel1;
    private JTextField user;
    private JPasswordField pass;
    private JButton usuarioButton;

    /**
     * Constructor de la clase login.
     * Configura la ventana de login y define las acciones de los botones.
     */

    public login() {
        super("Ventana de Login");
        setContentPane(panel1);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    verificarDatosAdmin();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        usuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    verificarDatosCajero();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    /**
     * Inicia la ventana de login.
     * Establece la visibilidad, tamaño y comportamiento de cierre de la ventana.
     */
    public void iniciar(){
        setVisible(true);
        setSize(600,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    /**
     * Verifica los datos ingresados por el administrador en la base de datos.
     * Si los datos son correctos, inicia el menú principal del administrador.
     *
     * @throws SQLException Si ocurre un error al conectar con la base de datos.
     */
    public void verificarDatosAdmin() throws SQLException {
        String usering= user.getText();
        String passing = new String(pass.getPassword());
        Connection connection= conexion();
        String sql = "SELECT * FROM login WHERE username = ? AND password = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, usering);
        pstmt.setString(2, passing);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            JOptionPane.showMessageDialog(null,"Bienvenid@ Administrador "+usering);
                menu vprinc = new menu();
                vprinc.iniciar();
                dispose();

        } else {
            JOptionPane.showMessageDialog(null,"Usuario o contraseña incorrectos.");
        }
        rs.close();
        pstmt.close();
        connection.close();
    }
    /**
     * Verifica los datos ingresados por el cajero en la base de datos.
     * Si los datos son correctos, inicia el menú del cajero.
     *
     * @throws SQLException Si ocurre un error al conectar con la base de datos.
     */
    public void verificarDatosCajero() throws SQLException {
        String usering= user.getText();
        String passing = new String(pass.getPassword());
        Connection connection= conexion();
        String sql = "SELECT * FROM Cajeros WHERE usuario = ? AND contrasena = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, usering);
        pstmt.setString(2, passing);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            int cajeroId = rs.getInt("cajero_id");
            JOptionPane.showMessageDialog(null,"Bienvenid@ Usuario Cajero "+usering);
            menu_cajero vtrans = new menu_cajero(cajeroId);
            vtrans.iniciar();
            dispose();
        } else {
            JOptionPane.showMessageDialog(null,"Usuario o contraseña incorrectos.");
        }
        rs.close();
        pstmt.close();
        connection.close();
    }
    /**
     * Establece la conexión con la base de datos.
     *
     * @return La conexión establecida con la base de datos.
     * @throws SQLException Si ocurre un error al conectar con la base de datos.
     */
    public Connection conexion() throws SQLException {
        String url="jdbc:mysql://uvbmbtmpi0evah2t:MYVCKxotJa0TSwg1SAT3@b4i0oz9mmhxht77tkqpd-mysql.services.clever-cloud.com:3306/b4i0oz9mmhxht77tkqpd";
        String user="uvbmbtmpi0evah2t";
        String password="MYVCKxotJa0TSwg1SAT3";
        return DriverManager.getConnection(url,user,password);
    }
}
