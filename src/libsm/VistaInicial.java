package libsm;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import java.text.*;
import java.util.Date;
import java.util.logging.*;

public class VistaInicial extends JFrame {

    private JPanel contentPane;
    private JTextField codigo;
    private JLabel alerta;
    public String usuario, contra;
    private JButton agregar, datos, registro, bd;

    public VistaInicial() {

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(contentPane);
        setIconImage(new ImageIcon(getClass().getResource("/img/ESCUDO.png")).getImage());
        contentPane.setLayout(null);
        contentPane.setBackground(Color.WHITE);
        ImageIcon icono = new javax.swing.ImageIcon(getClass().getResource("/img/LogoSanMiguel-1.png"));
        Image imagen = icono.getImage();
        ImageIcon iconoEscalado = new ImageIcon(imagen.getScaledInstance(242, 138, Image.SCALE_SMOOTH));
        JLabel img = new JLabel("");
        img.setIcon(iconoEscalado);
        img.setBounds(180, 40, 242, 138);
        contentPane.add(img);
        Font fuente = new Font("Arial", Font.PLAIN, 16);
        registro = new JButton("Registrar");
        registro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // CAMBIAR CUANDO CAMBIE EL TAMAÑO DEL CODIGO
                clickRegistro();
            }
        });
        registro.setBounds(250, 264, 117, 31);
        registro.setFont(fuente);
        registro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        contentPane.add(registro);

        codigo = new JTextField();
        codigo.setBounds(224, 209, 160, 31);
        // CAMBIAR CUANDO CAMBIE EL TAMAÑO DEL CODIGO
        codigo.setDocument(new LimitC(codigo, 10, 0));
        codigo.setFont(fuente);
        contentPane.add(codigo);
        codigo.setColumns(10);
        codigo.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    clickRegistro();
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        });

        alerta = new JLabel("", SwingConstants.CENTER);
        alerta.setBounds(157, 315, 302, 31);
        alerta.setFont(new Font("Arial", Font.PLAIN, 14));
        contentPane.add(alerta);

        ImageIcon icono2 = new javax.swing.ImageIcon(getClass().getResource("/img/db.png"));
        Image imagen2 = icono2.getImage();
        ImageIcon iconoEscalado2 = new ImageIcon(imagen2.getScaledInstance(31, 31, Image.SCALE_SMOOTH));
        bd = new JButton();
        bd.setBounds(500, 319, 31, 31);
        bd.setIcon(iconoEscalado2);
        bd.setOpaque(false);
        bd.setContentAreaFilled(false);
        bd.setBorderPainted(false);
        bd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        contentPane.add(bd);
        bd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                crearbase();
            }
        });
        ImageIcon icono3 = new javax.swing.ImageIcon(getClass().getResource("/img/data.png"));
        Image imagen3 = icono3.getImage();
        ImageIcon iconoEscalado3 = new ImageIcon(imagen3.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        datos = new JButton();
        datos.setIcon(iconoEscalado3);
        datos.setBounds(565, 300, 52, 52);
        datos.setOpaque(false);
        datos.setContentAreaFilled(false);
        datos.setBorderPainted(false);
        datos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        datos.setFocusPainted(false);
        contentPane.add(datos);
        datos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Datos data = new Datos();
                data.setVisible(true);
                data.setLocationRelativeTo(null);
            }
        });

        ImageIcon icono4 = new javax.swing.ImageIcon(getClass().getResource("/img/plus.png"));
        Image imagen4 = icono4.getImage();
        ImageIcon iconoEscalado4 = new ImageIcon(imagen4.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        agregar = new JButton();
        agregar.setIcon(iconoEscalado4);
        agregar.setBounds(10, 300, 52, 52);
        agregar.setOpaque(false);
        agregar.setContentAreaFilled(false);
        agregar.setBorderPainted(false);
        agregar.setFocusPainted(false);
        agregar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        contentPane.add(agregar);
        agregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Agregar agr = new Agregar();
                agr.setVisible(true);
                agr.setLocationRelativeTo(null);
            }
        });

        leerContra();
        comprobarBase();
    }


    public void clickRegistro() {
        if (codigo.getText().length()==10) {
            try {
                Registrar();
                codigo.setText("");
            } catch (SQLException ex) {
                Logger.getLogger(VistaInicial.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Digite el código correctamente");
        }
    }

    public void Registrar() throws SQLException {

        Date date = new Date();
        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");

        Conexion con2 = new Conexion();
        boolean error = con2.conectarMySQL("libsm", usuario, contra, "localhost");
        String nombreTeacher = "No encontrada";
        if (!error) {
            ResultSet rs = con2.consulta("SELECT name FROM teachers WHERE id='" + Integer.parseInt(codigo.getText()) + "'");
            int cant = con2.getSizeQuery(rs);
            if (cant > 0) {
                try {
                    while (rs.next()) {
                        nombreTeacher = rs.getString(1);
                    }
                    ResultSet rs2 = con2.consulta("SELECT flag FROM estado WHERE id='" + Integer.parseInt(codigo.getText()) + "'");
                    try {
                        while (rs2.next()) {
                            if (rs2.getString(1).equals("0")) {
                                try {
                                    Conexion con = new Conexion();
                                    con.conectarMySQL("libsm", usuario, contra, "localhost");
                                    String query = "INSERT INTO entradas VALUES (" + Integer.parseInt(codigo.getText()) + ",'" + hourFormat.format(date).toString() + "','" + dateFormat.format(date).toString() + "')";
                                    String query2 = "UPDATE estado SET flag='1' WHERE id=" + Integer.parseInt(codigo.getText()) + ";";
                                    con.actualizar(query);
                                    con.actualizar(query2);
                                    con.desconectar();
                                    alerta.setText("Registra entrada: " + nombreTeacher);
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, "Hubo un error" + e);
                                }
                            } else {
                                try {
                                    Conexion con = new Conexion();
                                    con.conectarMySQL("libsm", usuario, contra, "localhost");
                                    String query = "INSERT INTO salidas VALUES (" + Integer.parseInt(codigo.getText()) + ",'" + hourFormat.format(date).toString() + "','" + dateFormat.format(date).toString() + "')";
                                    String query2 = "UPDATE estado SET flag='0' WHERE id=" + Integer.parseInt(codigo.getText()) + ";";
                                    con.actualizar(query);
                                    con.actualizar(query2);
                                    con.desconectar();
                                    alerta.setText("Registra salida: " + nombreTeacher);
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, "Hubo un error" + e);
                                }
                            }
                        }

                    } catch (SQLException sql) {
                        JOptionPane.showMessageDialog(null,
                                "Error al tratar de obtener la informacion");
                    }
                } catch (SQLException sql) {
                    JOptionPane.showMessageDialog(null,
                            "Error al tratar de obtener la informacion");
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "No existe", "Error", 0);
                alerta.setText("No existe");
            }
        }
        con2.desconectar();
    }

    public void leerContra() {
        LeerContraBD LC = new LeerContraBD();
        usuario = LC.usuario;
        contra = LC.contra;
    }

    public void comprobarBase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/", usuario, contra);
            String comienzo = "use libsm;";
            Statement s = conexion.createStatement();
            s.executeUpdate(comienzo);
            conexion.close();
            alerta.setText("> Conectado <");
            bd.setVisible(false);
            registro.setEnabled(true);
            datos.setEnabled(true);
            agregar.setEnabled(true);
            codigo.setEnabled(true);
        } catch (Exception ex) {
            alerta.setText("No se encontró la base de datos");
            bd.setEnabled(true);
            registro.setEnabled(false);
            datos.setEnabled(false);
            agregar.setEnabled(false);
            codigo.setEnabled(false);
        }
    }

    public void crearbase() {
        DBSet DB = new DBSet();
        DB.setVisible(true);
        setVisible(false);
    }
}
