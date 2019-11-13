package libsm;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class DBSet extends JDialog {

    JLabel u, p, alt;
    JTextField us, pw;
    JButton crear;
    String escrito;
    String contraG = "";
    String usuarioG = "";
    Font fuente1;
    public boolean creado;

    public DBSet() {
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(300, 250);
        setTitle("Crear Base de Datos");
        Container c = getContentPane();
        c.setLayout(null);
        fuente1 = new Font("Arial", Font.PLAIN, 16);
        u = new JLabel("Usuario: ");
        p = new JLabel("Contraseña: ");
        alt = new JLabel("*******");
        us = new JTextField("", 15);
        pw = new JTextField("", 15);
        crear = new JButton("Crear Base de Datos");
        u.setFont(fuente1);
        us.setFont(fuente1);
        p.setFont(fuente1);
        pw.setFont(fuente1);
        crear.setFont(fuente1);
        u.setBounds(35, 40, 100, 15);
        us.setBounds(135, 40, 100, 30);
        p.setBounds(35, 75, 100, 20);
        pw.setBounds(135, 75, 100, 30);
        crear.setBounds(35, 140, 200, 30);
        alt.setBounds(35, 110, 200, 30);
        c.add(u);
        c.add(us);
        c.add(p);
        c.add(pw);
        c.add(crear);
        c.add(alt);
        c.setBackground(Color.gray.brighter());
        crear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                crearDB();
            }
        });
        us.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    pw.requestFocus();
                }
            }
        });
        pw.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    crear.requestFocus();
                }
            }
        });
        crear.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    crearDB();
                }
            }
        });
    }

    public void crearDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conexion1 = DriverManager.getConnection("jdbc:mysql://localhost/", us.getText(), pw.getText());
            try {
                File archivo = new File("claveBD.txt");
                FileWriter writer = new FileWriter(archivo, true);
                PrintWriter pr = new PrintWriter(archivo);
                escrito = us.getText() + "*" + pw.getText() + "-";
                pr.print(escrito);
                pr.close();
            } catch (Exception A) {
                System.out.println(A);
            }
            String Query1 = "CREATE DATABASE IF NOT EXISTS `libsm` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_spanish_ci */;\n";
            String Query2 = "USE libsm;";
            String Query4
                    = "CREATE TABLE IF NOT EXISTS `entradas` (\n"
                    + "  `id` bigint(20) DEFAULT NULL,\n"
                    + "  `hora` time DEFAULT NULL,\n"
                    + "  `fecha` date DEFAULT NULL,\n"
                    + "  KEY `FK_entradas_teachers` (`id`),\n"
                    + "  CONSTRAINT `FK_entradas_teachers` FOREIGN KEY (`id`) REFERENCES `teachers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE\n"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;";

            String Query5 = "CREATE TABLE IF NOT EXISTS `estado` (\n"
                    + "  `id` bigint(20) NOT NULL,\n"
                    + "  `flag` char(1) COLLATE utf8_spanish_ci NOT NULL,\n"
                    + "  KEY `FK_estado_teachers` (`id`),\n"
                    + "  CONSTRAINT `FK_estado_teachers` FOREIGN KEY (`id`) REFERENCES `teachers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE\n"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;";

            String Query6 = "CREATE TABLE IF NOT EXISTS `salidas` (\n"
                    + "  `id` bigint(20) DEFAULT NULL,\n"
                    + "  `hora` time DEFAULT NULL,\n"
                    + "  `fecha` date DEFAULT NULL,\n"
                    + "  KEY `FK_salidas_teachers` (`id`),\n"
                    + "  CONSTRAINT `FK_salidas_teachers` FOREIGN KEY (`id`) REFERENCES `teachers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE\n"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;";

            String Query3 = "CREATE TABLE IF NOT EXISTS `teachers` (\n"
                    + "  `id` bigint(20) NOT NULL,\n"
                    + "  `name` varchar(50) COLLATE utf8_spanish_ci DEFAULT NULL,\n"
                    + "  PRIMARY KEY (`id`)\n"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;";

            Statement s1 = conexion1.createStatement();
            s1.executeUpdate(Query1);
            s1.executeUpdate(Query2);
            s1.executeUpdate(Query3);
            s1.executeUpdate(Query4);
            s1.executeUpdate(Query5);
            s1.executeUpdate(Query6);
            conexion1.close();
            creado = true;
            setVisible(false);
        } catch (Exception x) {
            System.out.println(x);
            creado = false;
        }
        if (creado == false) {
            alt.setText("Error datos erroneos");
        }
        if (creado == true) {
            System.gc();
            reboot();
            setVisible(false);
            
            
        }
    }
    public void reboot(){
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        VistaInicial frame = new VistaInicial();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 640, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
