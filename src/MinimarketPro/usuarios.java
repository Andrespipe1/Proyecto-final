package MinimarketPro;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class usuarios extends JFrame{
    private JButton buscarButton;
    private JPanel panel1;
    private JTextField userb;
    private JButton eliminarButton;
    private JButton actualizarDatosButton;
    private JButton crearUnNuevoUsuarioButton;
    private JButton regresarButton;
    private JTextField usuario;
    private JTextField password;

    public usuarios(){
        super("Administrar Usuarios");
        setContentPane(panel1);
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    buscarDatos();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                principal vprin = new principal();
                vprin.iniciar();
                dispose();
            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    eliminarDatos();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        actualizarDatosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    actualizarDatos();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        crearUnNuevoUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    crearDatos();
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

    public Connection conexion() throws SQLException {
        String url="jdbc:mysql://uvbmbtmpi0evah2t:MYVCKxotJa0TSwg1SAT3@b4i0oz9mmhxht77tkqpd-mysql.services.clever-cloud.com:3306/b4i0oz9mmhxht77tkqpd";
        String user="uvbmbtmpi0evah2t";
        String password="MYVCKxotJa0TSwg1SAT3";
        return DriverManager.getConnection(url,user,password);
    }

    public void buscarDatos() throws SQLException {
        int id_usuario = Integer.parseInt(userb.getText());
        Connection connection = conexion();
        String sql = "Select * from Cajeros where cajero_id=?;";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id_usuario);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            int id = rs.getInt("cajero_id");
            String nombre = rs.getString("usuario");
            String contra = rs.getString("contrasena");
            usuario.setText(nombre);
            password.setText(contra);
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró un registro con ese código");
        }
        rs.close();
        pstmt.close();
        connection.close();
    }
    public void eliminarDatos() throws SQLException {
        int id_cajero = Integer.parseInt(userb.getText());
        Connection connection = conexion();
        String sql = "DELETE FROM Cajeros WHERE cajero_id = ?;";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id_cajero);
        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Usuario eliminado exitosamente");
            usuario.setText("");
            password.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró un registro con ese número de historial clínico");
        }
        pstmt.close();
        connection.close();
    }
    public void actualizarDatos() throws SQLException {
        int id_cajero = Integer.parseInt(userb.getText());
        String nuevoUsuario = usuario.getText();
        String nuevaContrasena = password.getText();

        Connection connection = conexion();
        String sql = "UPDATE Cajeros SET usuario = ?, contrasena = ? WHERE cajero_id = ?;";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, nuevoUsuario);
        pstmt.setString(2, nuevaContrasena);
        pstmt.setInt(3, id_cajero);

        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Usuario actualizado exitosamente");
            usuario.setText("");
            password.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró un registro con ese número de historial clínico");
        }
        pstmt.close();
        connection.close();
    }
    public void crearDatos() throws SQLException {
        String nuevoUsuario = usuario.getText();
        String nuevaContrasena = password.getText();

        Connection connection = conexion();
        String sql = "INSERT INTO Cajeros (usuario, contrasena) VALUES (?, ?);";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, nuevoUsuario);
        pstmt.setString(2, nuevaContrasena);

        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Usuario creado exitosamente");
            usuario.setText("");
            password.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "Error al crear el usuario");
        }
        pstmt.close();
        connection.close();
    }

}

