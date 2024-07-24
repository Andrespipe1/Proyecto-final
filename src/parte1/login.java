package parte1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class login extends JFrame{
    private JButton loginButton;
    private JPanel panel1;
    private JTextField user;
    private JTextField pass;

    public login() {
        super("Ventana de Login");
        setContentPane(panel1);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    verificarDatos();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    public void iniciar(){
        setVisible(true);
        setSize(600,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
    public void verificarDatos() throws SQLException {
        String usering= user.getText();
        String passing= pass.getText();
        Connection connection= conexion();
        String sql = "SELECT * FROM login WHERE username = ? AND password = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, usering);
        pstmt.setString(2, passing);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            JOptionPane.showMessageDialog(null,"Bienvenid@ "+usering);


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
