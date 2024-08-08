package MinimarketPro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

/**
 * Clase que representa la ventana principal de administración del minimarket.
 * Permite gestionar usuarios, productos e inventarios.
 *
 * @author Andrés Tufiño
 * @version 1.0.1
 */
public class menu extends JFrame{
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JButton buscarButton;
    private JTextField userb;
    private JButton eliminarButton;
    private JButton actualizarDatosButton;
    private JButton crearUnNuevoUsuarioButton;
    private JButton regresarButton;
    private JTextField usuario;
    private JTextField password;
    private JTextField id_buscar;
    private JTextField nom;
    private JTextField desc;
    private JTextField stock;
    private JTextField precio;
    private JButton actualizarProductoButton;
    private JButton eliminarProductoButton;
    private JButton agregarProductoButton;
    private JButton limpiarButton;
    private JButton buscarInv;
    private JTable table1;
    private JButton buscarVentas;
    private JTextField idCajerov;

    /**
     * Constructor de la clase `menu`.
     * Configura el JFrame con el contenido del panel y establece los modelos de tabla para las ventas.
     * Configura los oyentes de eventos para los botones de la interfaz.
     */
    public menu() {
        super("Ventana Admin");
        setContentPane(panel1);
        /*Configuracionde la tabla de ventas*/
        String[] columnNames = {"ID Venta", "ID Cajero","Nombre Cajero", "Fecha"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table1.setModel(model);
        // Cambiar color de los encabezados de la tabla
        JTableHeader header = table1.getTableHeader();
        header.setBackground(Color.BLUE); // Color de fondo
        header.setForeground(Color.WHITE); // Color del texto
        header.setFont(new Font("Arial", Font.BOLD, 14));

        /*Este apartado tiene la pestaña de usuarios*/

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
                int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Si el usuario hace clic en "Sí", cerrar sesión
                    login vprin = new login();
                    vprin.iniciar();
                    dispose();
                }
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
        /*Aqui va lo de la gestion de invetarios*/
        actualizarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    actualizarDatosInv();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        eliminarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    eliminarDatosInv();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
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
        buscarInv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    buscarDatosInv();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buscarVentas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    cargarVentasCajero();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    /**
     * Inicializa y muestra la ventana.
     * Configura el tamaño, la ubicación y el comportamiento de cierre de la ventana.
     */
    public void iniciar(){
        setVisible(true);
        setSize(600,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    /**
     * Establece una conexión con la base de datos utilizando JDBC.
     *
     * @return La conexión a la base de datos.
     * @throws SQLException Si ocurre un error al establecer la conexión.
     */
    public Connection conexion() throws SQLException {
        String url="jdbc:mysql://uvbmbtmpi0evah2t:MYVCKxotJa0TSwg1SAT3@b4i0oz9mmhxht77tkqpd-mysql.services.clever-cloud.com:3306/b4i0oz9mmhxht77tkqpd";
        String user="uvbmbtmpi0evah2t";
        String password="MYVCKxotJa0TSwg1SAT3";
        return DriverManager.getConnection(url,user,password);
    }
    /**
     * Busca los datos de un usuario en la base de datos.
     *
     * @throws SQLException Si ocurre un error en la consulta SQL.
     */
    /*Funciones para la administracion de usuarios*/
    public void buscarDatos() throws SQLException {
        int id_usuario = Integer.parseInt(userb.getText());
        Connection connection = conexion();
        String sql = "Select * from Cajeros where cajero_id=?;";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id_usuario);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
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
    /**
     * Elimina un usuario de la base de datos.
     *
     * @throws SQLException Si ocurre un error en la consulta SQL.
     */
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
    /**
     * Actualiza los datos de un usuario en la base de datos.
     *
     * @throws SQLException Si ocurre un error en la consulta SQL.
     */
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
    /**
     * Crea un nuevo usuario en la base de datos.
     *
     * @throws SQLException Si ocurre un error en la consulta SQL.
     */
    public void crearDatos() throws SQLException {
        String nuevoUsuario = usuario.getText();
        String nuevaContrasena = password.getText();

        Connection connection = conexion();
        String sql = "INSERT INTO Cajeros (usuario, contrasena) VALUES (?, ?);";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, nuevoUsuario);
        pstmt.setString(2, nuevaContrasena);
        if (nuevoUsuario.isEmpty() || nuevaContrasena.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error llene los campos");
        }
        else{
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Usuario creado exitosamente");
                usuario.setText("");
                password.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Error al crear el usuario");
            }
            pstmt.close();
            connection.close();}

    }
    /**
     * Actualiza los datos de un producto en la base de datos, incluyendo la imagen del producto.
     *
     * @throws SQLException Si ocurre un error en la consulta SQL.
     * @throws IOException Si ocurre un error al leer el archivo de imagen.
     */
    /*Funciones para inventarios*/
    public void actualizarDatosInv() throws SQLException, IOException {
        int id_prod = Integer.parseInt(id_buscar.getText());
        String nuevoNom = nom.getText();
        String nuevaDesc = desc.getText();
        double nuevoPrecio = Double.parseDouble(precio.getText());
        int nuevoStock = Integer.parseInt(stock.getText());

        Connection connection = conexion();

        // Abre un diálogo para seleccionar una nueva imagen
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileInputStream fis = null;
        String nombreImagen = null;

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            nombreImagen = selectedFile.getName();
            fis = new FileInputStream(selectedFile);
        }

        // Consulta SQL para actualizar los datos del producto
        String sql = "UPDATE Productos SET nombre_producto=?, descripcion=?, precio=?, stock=?, nombreimagen=?, imagen=? WHERE producto_id=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, nuevoNom);
        pstmt.setString(2, nuevaDesc);
        pstmt.setDouble(3, nuevoPrecio);
        pstmt.setInt(4, nuevoStock);

        if (fis != null) {
            pstmt.setString(5, nombreImagen);
            pstmt.setBinaryStream(6, fis, (int) new File(fileChooser.getSelectedFile().getAbsolutePath()).length());
        } else {
            pstmt.setNull(5, java.sql.Types.VARCHAR);
            pstmt.setNull(6, java.sql.Types.BLOB);
        }

        pstmt.setInt(7, id_prod);

        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Producto actualizado exitosamente");
        } else {
            JOptionPane.showMessageDialog(null, "Error al actualizar el producto");
        }

        if (fis != null) {
            fis.close();
        }
        pstmt.close();
        connection.close();
    }

    /**
     * Agrega un nuevo producto a la base de datos, incluyendo la imagen del producto.
     *
     * @throws SQLException Si ocurre un error en la consulta SQL.
     * @throws IOException Si ocurre un error al leer el archivo de imagen.
     */
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

    /**
     * Busca los datos de un producto en la base de datos.
     *
     * @throws SQLException Si ocurre un error en la consulta SQL.
     */
    public void buscarDatosInv() throws SQLException {
        int id_prod = Integer.parseInt(id_buscar.getText());
        Connection connection = conexion();
        String sql = "Select * from Productos where producto_id=?;";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id_prod);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {

            String nombre = rs.getString("nombre_producto");
            String descrip = rs.getString("descripcion");
            String precios = rs.getString("precio");
            String cantidad = rs.getString("stock");
            nom.setText(nombre);
            desc.setText(descrip);
            precio.setText(precios);
            stock.setText(cantidad);

        } else {
            JOptionPane.showMessageDialog(null, "No se encontró un registro con ese código");
        }
        rs.close();
        pstmt.close();
        connection.close();
    }

    /**
     * Elimina un producto de la base de datos.
     *
     * @throws SQLException Si ocurre un error en la consulta SQL.
     */
    public void eliminarDatosInv() throws SQLException {
        int id_prod = Integer.parseInt(id_buscar.getText());

        Connection connection = conexion();
        String sql = "DELETE FROM Productos WHERE producto_id = ?;";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id_prod);

        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Producto eliminado exitosamente");
            id_buscar.setText("");
            nom.setText("");
            desc.setText("");
            precio.setText("");
            stock.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "Error al eliminar el registro");
        }
        pstmt.close();
        connection.close();
    }

    /**
     * Carga las ventas realizadas por un cajero específico y las muestra en la tabla.
     *
     * @throws SQLException Si ocurre un error en la consulta SQL.
     */
    public void cargarVentasCajero() throws SQLException {
        int cajeroId = Integer.parseInt(idCajerov.getText()); // ID del cajero a buscar
        Connection connection = conexion();
        String sql = "SELECT t.transaccion_id, t.cajero_id, c.usuario, t.fecha " +
                "FROM Transacciones t " +
                "JOIN Cajeros c ON t.cajero_id = c.cajero_id " +
                "WHERE t.cajero_id = ?";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, cajeroId);
        ResultSet rs = pstmt.executeQuery();

        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0); // Limpiar la tabla antes de cargar los datos

        while (rs.next()) {
            int idVenta = rs.getInt("transaccion_id");
            int idCajero = rs.getInt("cajero_id");
            String nombreCajero = rs.getString("usuario");
            Date fecha = rs.getDate("fecha");

            model.addRow(new Object[]{idVenta,idCajero, nombreCajero, fecha});
        }

        rs.close();
        pstmt.close();
        connection.close();
    }

}
