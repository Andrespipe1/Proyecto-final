package MinimarketPro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class transaccion extends JFrame{
    private JButton comprar;
    private JPanel panel1;
    private JTextField idBuscar;
    private JButton buscarButton;
    private JTable tabla;

    public transaccion() {
        super("Transacciones");
        setContentPane(panel1);
        String[] columnNames = {"ID", "Nombre Producto", "Descripción", "Precio", "Stock", "Imagen"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        tabla.setModel(model);

        // Configurar el renderizador de celdas para la columna de imagen
        tabla.getColumnModel().getColumn(5).setCellRenderer(new ImageRender());
        try {
            cargarTodosLosProductos();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        comprar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    buscarProducto();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    public void cargarTodosLosProductos() throws SQLException {
        Connection connection = conexion();
        String sql = "SELECT producto_id, nombre_producto, descripcion, precio, stock, imagen FROM Productos";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);

        while (rs.next()) {
            int id = rs.getInt("producto_id");
            String nombre = rs.getString("nombre_producto");
            String descripcion = rs.getString("descripcion");
            double precio = rs.getDouble("precio");
            int stock = rs.getInt("stock");
            byte[] imgBytes = rs.getBytes("imagen");

            ImageIcon icon = null;
            if (imgBytes != null) {
                Image img = Toolkit.getDefaultToolkit().createImage(imgBytes);
                icon = new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            }

            model.addRow(new Object[]{id, nombre, descripcion, precio, stock, icon});
        }

        rs.close();
        pstmt.close();
        connection.close();
    }
    public void buscarProducto() throws SQLException {
        int id_prod=Integer.parseInt(idBuscar.getText());
        Connection connection = conexion();
        String sql = "SELECT producto_id, nombre_producto, descripcion, precio, stock, imagen FROM Productos WHERE producto_id=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id_prod);
        ResultSet rs = pstmt.executeQuery();

        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);

        while (rs.next()) {
            int id = rs.getInt("producto_id");
            String nombre = rs.getString("nombre_producto");
            String descripcion = rs.getString("descripcion");
            double precio = rs.getDouble("precio");
            int stock = rs.getInt("stock");
            byte[] imgBytes = rs.getBytes("imagen");

            ImageIcon icon = null;
            if (imgBytes != null) {
                Image img = Toolkit.getDefaultToolkit().createImage(imgBytes);
                icon = new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(null,"Producto Encontrado!");
            }

            model.addRow(new Object[]{id, nombre, descripcion, precio, stock, icon});
        }

        rs.close();
        pstmt.close();
        connection.close();
    }

    public Connection conexion() throws SQLException {
        String url = "jdbc:mysql://uvbmbtmpi0evah2t:MYVCKxotJa0TSwg1SAT3@b4i0oz9mmhxht77tkqpd-mysql.services.clever-cloud.com:3306/b4i0oz9mmhxht77tkqpd";
        String user = "uvbmbtmpi0evah2t";
        String password = "MYVCKxotJa0TSwg1SAT3";
        return DriverManager.getConnection(url, user, password);
    }

    public void iniciar(){
        setVisible(true);
        setSize(600,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
}
