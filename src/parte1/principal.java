package parte1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class principal extends JFrame{
    private JButton ventas;
    private JPanel panel1;
    private JButton adminsitrarUsuarios;
    private JButton inventario;
    private JButton salir;

    public principal() {
        super("Ventana Principal");
        setContentPane(panel1);
        adminsitrarUsuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usuarios vuser = new usuarios();
                vuser.iniciar();
                dispose();
            }
        });

        ventas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventas vvent = new ventas();
                vvent.iniciar();
                dispose();
            }
        });
        inventario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inventario vinv = new inventario();
                vinv.iniciar();
                dispose();
            }
        });
        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);            }
        });

    }

    public void iniciar(){
    setVisible(true);
    setSize(600,500);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    }


}