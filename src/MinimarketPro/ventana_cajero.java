package MinimarketPro;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ventana_cajero extends JFrame{
    private JButton salirButton;
    private JPanel panel1;
    private JButton transaccionesCompraButton;

    public ventana_cajero() {
        super("Ventana Cajero");
        setContentPane(panel1);
        transaccionesCompraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transaccion vtrans = new transaccion();
                vtrans.iniciar();
                dispose();
            }
        });
    }
    public void iniciar(){
        setVisible(true);
        setSize(600,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
}
