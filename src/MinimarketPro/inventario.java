package MinimarketPro;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class inventario extends JFrame{
    private JButton eliminarProductoButton;
    private JPanel panel1;
    private JButton regresarButton;
    private JTextField id_buscar;
    private JButton buscarButton;
    private JTextField nom;
    private JTextField desc;
    private JTextField precio;
    private JTextField stock;
    private JButton actualizarProductoButton;
    private JButton agregarProductoButton;
    private JButton limpiarButton;

    public inventario() {
        super("Ventana Inventarios");
        setContentPane(panel1);
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                principal vprin = new principal();
                vprin.iniciar();
                dispose();
            }
        });
        actualizarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        eliminarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        agregarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    agregarProductos();
                } catch (SQLException ex) {

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nom.setText("");
                desc.setText("");
                precio.setText("");
                stock.setText("");
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
    public void agregarProductos() throws SQLException, IOException {
        String nombre = nom.getText();
        String descripcion = desc.getText();
        double precios = Double.parseDouble(precio.getText());
        int cant = Integer.parseInt(stock.getText());

        Connection connection = conexion();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String rutaImagen = selectedFile.getAbsolutePath();
            String nombreImagen = selectedFile.getName();
            FileInputStream fis = new FileInputStream(new File(rutaImagen));

            String sql = "INSERT INTO Productos (nombre_producto, descripcion, precio, stock, nombreimagen, imagen) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, descripcion);
            pstmt.setDouble(3, precios);
            pstmt.setInt(4, cant);
            pstmt.setString(5, nombreImagen);
            pstmt.setBinaryStream(6, fis, (int) new File(rutaImagen).length());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Producto agregado exitosamente");

            } else {
                JOptionPane.showMessageDialog(null, "Error al agregar el producto");
            }

            fis.close();
            pstmt.close();
            connection.close();
        }
    }

}
