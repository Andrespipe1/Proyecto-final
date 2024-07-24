package parte1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class transaccion extends JFrame{
    private JButton comprar;
    private JPanel panel1;
    private JTextField textField1;
    private JButton buscarButton;

    public transaccion() {
        super("Transacciones");
        setContentPane(panel1);
        comprar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
    public void iniciar(){
        setVisible(true);
        setSize(600,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
}
