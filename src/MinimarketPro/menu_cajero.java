package MinimarketPro;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Date;


public class menu_cajero extends JFrame{
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JTextField idBuscar;
    private JButton buscarButton;
    private JTable tabla;
    private JButton button1;
    private JButton comprar;
    private final int cajeroId;

    public menu_cajero(int cajeroId) {
        this.cajeroId = cajeroId;

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
                try {
                    realizarCompra();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (DocumentException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
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
    public void realizarCompra() throws SQLException, DocumentException, IOException {
        int productId = Integer.parseInt(idBuscar.getText());
        Connection connection = conexion();

        // Obtener detalles del producto
        String sql = "SELECT nombre_producto, precio, stock FROM Productos WHERE producto_id=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, productId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String productName = rs.getString("nombre_producto");
            double price = rs.getDouble("precio");
            int stock = rs.getInt("stock");

            if (stock > 0) {
                // Actualizar stock
                String updateStockSql = "UPDATE Productos SET stock = stock - 1 WHERE producto_id = ?";
                pstmt = connection.prepareStatement(updateStockSql);
                pstmt.setInt(1, productId);
                pstmt.executeUpdate();

                // Guardar transacción
                String insertTransactionSql = "INSERT INTO Transacciones (producto_id, cantidad, precio_total, fecha, cajero_id) VALUES (?, ?, ?, ?, ?)";
                pstmt = connection.prepareStatement(insertTransactionSql);
                pstmt.setInt(1, productId);
                pstmt.setInt(2, 1);  // Cantidad comprada
                pstmt.setDouble(3, price);
                pstmt.setDate(4, new java.sql.Date(new Date().getTime()));
                pstmt.setInt(5, cajeroId);  // Agregar ID del cajero
                pstmt.executeUpdate();

                // Generar nota de venta en PDF
                generarNotaDeVenta(productId, productName, price);

                // Recargar productos
                cargarTodosLosProductos();

                JOptionPane.showMessageDialog(null, "Compra realizada exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "Stock insuficiente.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Producto no encontrado.");
        }

        rs.close();
        pstmt.close();
        connection.close();
    }

    public void generarNotaDeVenta(int productId, String productName, double price) throws DocumentException, IOException {
        // Usar JFileChooser para permitir al usuario seleccionar la ubicación de guardado
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Nota de Venta");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String rutaGuardado = fileToSave.getAbsolutePath();

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(rutaGuardado));
            document.open();

            Paragraph title = new Paragraph("Nota de Venta");
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph cajeroInfo = new Paragraph("Cajero ID: " + cajeroId+"\n");
            cajeroInfo.setAlignment(Element.ALIGN_LEFT);
            document.add(cajeroInfo);

            PdfPTable table = new PdfPTable(3);
            PdfPCell cell1 = new PdfPCell(new Paragraph("Producto ID"));
            PdfPCell cell2 = new PdfPCell(new Paragraph("Nombre Producto"));
            PdfPCell cell3 = new PdfPCell(new Paragraph("Precio"));
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);

            table.addCell(String.valueOf(productId));
            table.addCell(productName);
            table.addCell(String.valueOf(price));

            document.add(table);
            document.close();

            JOptionPane.showMessageDialog(null, "Nota de venta generada exitosamente en: " + rutaGuardado);
        } else {
            JOptionPane.showMessageDialog(null, "Guardado de nota de venta cancelado.");
        }
    }
}
