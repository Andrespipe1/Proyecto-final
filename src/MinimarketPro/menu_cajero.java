package MinimarketPro;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Clase que representa la ventana principal para el cajero en el sistema de minimarket.
 * Permite realizar operaciones de búsqueda de productos, gestión del carrito de compras,
 * y realización de compras con generación de notas de venta en PDF.
 *
 * @author Andres Tufiño
 * @version 1.0
 */
public class menu_cajero extends JFrame{
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JTextField idBuscar;
    private JButton buscarButton;
    private JTable tabla;
    private JButton agregarAlCarritoButton;
    private JButton comprar;
    private JTable table1;
    private JButton cargarCarritoButton;
    private JButton logOutButton;
    private JButton cancelarCompraButton;
    private final int cajeroId;

    /**
     * Constructor de la clase. Inicializa la interfaz gráfica y configura los eventos.
     *
     * @param cajeroId El ID del cajero que está utilizando esta ventana para guardar que cajero a iniciado sesion.
     */
    public menu_cajero(int cajeroId) {
        this.cajeroId = cajeroId;

        setContentPane(panel1);
        // para la tabla principal
        String[] columnNames = {"ID", "Nombre Producto", "Descripción", "Precio", "Stock", "Imagen"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        tabla.setModel(model);

        //para la tabla carrito de compras
        String[] carritoColumnNames = {"ID Producto", "Nombre Producto", "Cantidad", "Precio Unitario"};
        DefaultTableModel carritoModel = new DefaultTableModel(carritoColumnNames, 0);
        table1.setModel(carritoModel);
        // Cambiar color de los encabezados de la tabla
        JTableHeader header = table1.getTableHeader();
        JTableHeader header2 = tabla.getTableHeader();
        header.setBackground(Color.BLUE); // Color de fondo
        header.setForeground(Color.WHITE); // Color del texto
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header2.setBackground(Color.BLUE); // Color de fondo
        header2.setForeground(Color.WHITE); // Color del texto
        header2.setFont(new Font("Arial", Font.BOLD, 14));
        try {
            cargarTodosLosProductos();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Configurar el renderizador de celdas para la columna de imagen
        tabla.getColumnModel().getColumn(5).setCellRenderer(new ImageRender());
        try {
            cargarTodosLosProductos();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Si el usuario hace clic en "Sí", cerrar sesión
                    login vprin = new login();
                    vprin.iniciar();
                    dispose();
                }
            }
        });
        agregarAlCarritoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    agregarAlCarrito();
                    cargarTodosLosProductos();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        comprar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    realizarCompra();
                } catch (SQLException | DocumentException | IOException ex) {
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
        cargarCarritoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    cargarProductosCarrito();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        cancelarCompraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    cancelarCompra();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * Carga todos los productos disponibles en la base de datos y los muestra en la tabla principal.
     *
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public void cargarTodosLosProductos() throws SQLException {
        Connection connection = ConexionBd.obtenerConexion();
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

    /**
     * Carga los productos actualmente en el carrito del cajero y los muestra en la tabla del carrito.
     *
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public void cargarProductosCarrito() throws SQLException {
        Connection connection = ConexionBd.obtenerConexion();
        String sql = "SELECT c.producto_id, p.nombre_producto, c.cantidad, p.precio " +
                "FROM Carrito c " +
                "JOIN Productos p ON c.producto_id = p.producto_id " +
                "WHERE c.cajero_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, cajeroId);  // Asume que cajeroId es el ID del cajero actual
        ResultSet rs = pstmt.executeQuery();

        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);

        while (rs.next()) {
            int id = rs.getInt("producto_id");
            String nombre = rs.getString("nombre_producto");
            int cantidad = rs.getInt("cantidad");
            double precio = rs.getDouble("precio");

            model.addRow(new Object[]{id, nombre, cantidad, precio});
        }

        rs.close();
        pstmt.close();
        connection.close();
    }

    /**
     * Busca un producto por su ID y lo muestra en la tabla principal.
     *
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public void buscarProducto() throws SQLException {
        int id_prod=Integer.parseInt(idBuscar.getText());
        Connection connection = ConexionBd.obtenerConexion();
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

    /**
     * Agrega un producto al carrito basado en el ID proporcionado en el campo de búsqueda.
     *
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public void agregarAlCarrito() throws SQLException {
        int productId = Integer.parseInt(idBuscar.getText());
        Connection connection = ConexionBd.obtenerConexion();

        // Verificar stock y agregar al carrito
        String sql = "SELECT stock FROM Productos WHERE producto_id=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, productId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            int stock = rs.getInt("stock");

            if (stock > 0) {
                String insertCarritoSql = "INSERT INTO Carrito (producto_id, cantidad, cajero_id) VALUES (?, ?, ?)";
                pstmt = connection.prepareStatement(insertCarritoSql);
                pstmt.setInt(1, productId);
                pstmt.setInt(2, 1);  // Cantidad agregada
                pstmt.setInt(3, cajeroId);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Producto agregado al carrito.");
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

    /**
     * Realiza la compra de los productos en el carrito, actualiza el stock y genera una nota de venta en PDF.
     *
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     * @throws DocumentException Si ocurre un error al crear el documento PDF.
     * @throws IOException Si ocurre un error al guardar el archivo PDF.
     */
    public void realizarCompra() throws SQLException, DocumentException, IOException {
        Connection connection = ConexionBd.obtenerConexion();
        // Formateador para los precios
        DecimalFormat df = new DecimalFormat("#.00");
        // Obtener productos del carrito
        String sql = "SELECT c.producto_id, p.nombre_producto, c.cantidad, p.precio FROM Carrito c JOIN Productos p ON c.producto_id = p.producto_id WHERE c.cajero_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, cajeroId);
        ResultSet rs = pstmt.executeQuery();

        double totalPrice = 0;
        PdfPTable table = new PdfPTable(4);
        table.addCell("Producto ID");
        table.addCell("Nombre Producto");
        table.addCell("Cantidad");
        table.addCell("Precio Total");

        while (rs.next()) {
            int productId = rs.getInt("producto_id");
            String productName = rs.getString("nombre_producto");
            int quantity = rs.getInt("cantidad");
            double price = rs.getDouble("precio");

            double priceTotal = price * quantity;
            totalPrice += priceTotal;

            table.addCell(String.valueOf(productId));
            table.addCell(productName);
            table.addCell(String.valueOf(quantity));
            table.addCell(String.valueOf(priceTotal));

            // Actualizar stock
            String updateStockSql = "UPDATE Productos SET stock = stock - ? WHERE producto_id = ?";
            pstmt = connection.prepareStatement(updateStockSql);
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();

            // Guardar transacción
            String insertTransactionSql = "INSERT INTO Transacciones (producto_id, cantidad, precio_total, fecha, cajero_id) VALUES (?, ?, ?, ?, ?)";
            pstmt = connection.prepareStatement(insertTransactionSql);
            pstmt.setInt(1, productId);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, priceTotal);
            pstmt.setDate(4, new java.sql.Date(new Date().getTime()));
            pstmt.setInt(5, cajeroId);
            pstmt.executeUpdate();
        }

        // Generar nota de venta en PDF
        generarNotaDeVenta(table, totalPrice);

        // Limpiar carrito
        String deleteCarritoSql = "DELETE FROM Carrito WHERE cajero_id = ?";
        pstmt = connection.prepareStatement(deleteCarritoSql);
        pstmt.setInt(1, cajeroId);
        pstmt.executeUpdate();

        // Recargar productos
        cargarTodosLosProductos();

        JOptionPane.showMessageDialog(null, "Compra realizada exitosamente.");

        rs.close();
        pstmt.close();
        connection.close();
    }

    /**
     * Genera una nota de venta en formato PDF con los detalles de la compra.
     *
     * @param table La tabla con los detalles de los productos comprados.
     * @param totalPrice El precio total de la compra.
     * @throws DocumentException Si ocurre un error al crear el documento PDF.
     * @throws IOException Si ocurre un error al guardar el archivo PDF.
     */
    public void generarNotaDeVenta(PdfPTable table, double totalPrice) throws DocumentException, IOException {
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

            Paragraph fecha = new Paragraph("Fecha: "+new java.sql.Date(new Date().getTime()));
            Paragraph cajeroInfo = new Paragraph("Cajero ID: " + cajeroId + "\n\n");
            fecha.setAlignment(Element.ALIGN_CENTER);
            document.add(fecha);
            cajeroInfo.setAlignment(Element.ALIGN_LEFT);
            document.add(cajeroInfo);

            document.add(table);

            Paragraph total = new Paragraph("Precio Total: " + totalPrice);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();

            JOptionPane.showMessageDialog(null, "Nota de venta generada exitosamente en: " + rutaGuardado);
        } else {
            JOptionPane.showMessageDialog(null, "Guardado de nota de venta cancelado.");
        }
    }

    /**
     * Cancela la compra actual eliminando todos los productos del carrito del cajero.
     *
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public void cancelarCompra() throws SQLException {
        Connection connection = ConexionBd.obtenerConexion();
        // Eliminar todos los productos del carrito para el cajero actual
        String deleteCarritoSql = "DELETE FROM Carrito WHERE cajero_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(deleteCarritoSql);
        pstmt.setInt(1, cajeroId);
        pstmt.executeUpdate();

        // Recargar los productos en la tabla
        cargarTodosLosProductos();

        // Vaciar la tabla del carrito
        DefaultTableModel carritoModel = (DefaultTableModel) table1.getModel();
        carritoModel.setRowCount(0);

        JOptionPane.showMessageDialog(null, "Compra cancelada y carrito vaciado.");

        pstmt.close();
        connection.close();
    }
    /**
     * Muestra la ventana del cajero con la configuración inicial.
     */
    public void iniciar() {
        setVisible(true);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
