package MinimarketPro;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ventas extends JFrame{
    private JButton button1;
    private JPanel panel1;
    private JButton button2;
    private JButton regresarButton;

    public ventas() {
        super("Ventana de Ventas");
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
    public void iniciar(){
        setVisible(true);
        setSize(600,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
}
