package parte1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class inventario extends JFrame{
    private JButton button1;
    private JPanel panel1;
    private JButton regresarButton;

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
    }
}
