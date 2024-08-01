package MinimarketPro;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class login extends JFrame{
    private JButton loginButton;
    private JPanel panel1;
    private JTextField user;
    private JPasswordField pass;
    private JButton usuarioButton;

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
    public void iniciar(){
        setVisible(true);
        setSize(600,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
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
                principal vprinc = new principal();
                vprinc.iniciar();
                dispose();

        } else {
            JOptionPane.showMessageDialog(null,"Usuario o contraseña incorrectos.");
        }
        rs.close();
        pstmt.close();
        connection.close();
    }
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
            JOptionPane.showMessageDialog(null,"Bienvenid@ Usuario Cajero "+usering);
            ventana_cajero vtrans = new ventana_cajero();
            vtrans.iniciar();
            dispose();
        } else {
            JOptionPane.showMessageDialog(null,"Usuario o contraseña incorrectos.");
        }
        rs.close();
        pstmt.close();
        connection.close();
    }
    public Connection conexion() throws SQLException {
        String url="jdbc:mysql://uvbmbtmpi0evah2t:MYVCKxotJa0TSwg1SAT3@b4i0oz9mmhxht77tkqpd-mysql.services.clever-cloud.com:3306/b4i0oz9mmhxht77tkqpd";
        String user="uvbmbtmpi0evah2t";
        String password="MYVCKxotJa0TSwg1SAT3";
        return DriverManager.getConnection(url,user,password);
    }
}
