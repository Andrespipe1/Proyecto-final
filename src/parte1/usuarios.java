package parte1;

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
    private JLabel datos;
    private JButton regresarButton;

    public usuarios(){
        super("Administrar Usuarios");
        setContentPane(panel1);
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
        int id_usuario = Integer.parseInt(datos.getText());
        Connection connection = conexion();
        String sql = "Select * from PACIENTE where n_historial_clinico=?;";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id_usuario);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            String cedul = rs.getString("cedula");
            String histori = rs.getString("n_historial_clinico");
            String nombre = rs.getString("nombre");
            String apellido = rs.getString("apellido");
            String telefono = rs.getString("telefono");
            String edad = rs.getString("edad");
            String desc = rs.getString("descripcion_enfermedad");
            datos.setText("Cedula: "+cedul+" N° historial Clinico"+histori+"\n Nombre: "+nombre+" "+apellido+" Telf: "+telefono+" Edad: "+edad+" Enfermedad: "+desc);
            setSize(1000,500);
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró un registro con ese código");
        }
        rs.close();
        pstmt.close();
        connection.close();
    }
}

