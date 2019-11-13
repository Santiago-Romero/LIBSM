package libsm;

import com.toedter.calendar.*;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class Datos extends JDialog {

    private JPanel contentPane;
    private JComboBox teachers;
    private JRadioButton jrfecha;
    private JRadioButton jrteach;
    private JRadioButton jrent;
    private JRadioButton jrsal;
    private JDateChooser calendario;
    private DefaultTableModel md;
    private String usuario,contra;
    public Datos() {
        setBounds(100, 100, 600, 400);
        setIconImage(new ImageIcon(getClass().getResource("/img/ESCUDO.png")).getImage());
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblFiltrar = new JLabel("Filtrar por:");
        lblFiltrar.setBounds(10, 11, 117, 14);
        contentPane.add(lblFiltrar);

        JLabel lblTeacher = new JLabel("Teacher");
        lblTeacher.setBounds(10, 64, 59, 14);
        contentPane.add(lblTeacher);

        jrteach = new JRadioButton("");
        jrteach.setBounds(75, 60, 21, 23);
        jrteach.setSelected(true);
        contentPane.add(jrteach);
        jrteach.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
               calendario.setEnabled(false);
            }
        });

        jrent = new JRadioButton("Entradas");
        jrent.setBounds(70, 10, 100, 23);
        jrent.setSelected(true);
        contentPane.add(jrent);

        jrsal = new JRadioButton("Salidas");
        jrsal.setBounds(170, 10, 100, 23);
        contentPane.add(jrsal);

        teachers = new JComboBox();
        teachers.setBounds(10, 91, 161, 20);
        teachers.removeAllItems();
        contentPane.add(teachers);

        JLabel lblFecha = new JLabel("Fecha");
        lblFecha.setBounds(288, 64, 46, 14);
        contentPane.add(lblFecha);

        jrfecha = new JRadioButton("");
        jrfecha.setBounds(323, 60, 21, 23);
        contentPane.add(jrfecha);
        jrfecha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
               calendario.setEnabled(true);
            }
        });

        ButtonGroup bg1 = new ButtonGroup();
        bg1.add(jrteach);
        bg1.add(jrfecha);

        ButtonGroup bg2 = new ButtonGroup();
        bg2.add(jrent);
        bg2.add(jrsal);

        calendario = new JDateChooser();
        calendario.setBounds(288, 91, 143, 27);
        Date date = new Date();
        calendario.setEnabled(false);
        calendario.setDate(date);
        contentPane.add(calendario);
        

        ImageIcon icono = new javax.swing.ImageIcon(getClass().getResource("/img/filter.png"));
        Image imagen = icono.getImage();
        ImageIcon iconoEscalado = new ImageIcon(imagen.getScaledInstance(20, 20, Image.SCALE_SMOOTH));

        JButton btnFiltrar = new JButton("");
        btnFiltrar.setBounds(540, 120, 25, 25);
        btnFiltrar.setIcon(iconoEscalado);
        btnFiltrar.setOpaque(false);
        btnFiltrar.setContentAreaFilled(false);
        btnFiltrar.setBorderPainted(false);
        contentPane.add(btnFiltrar);
        btnFiltrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                md.setRowCount(0);
                filtrar();
            }
        });

        md = new DefaultTableModel();
        JTable table = new JTable(md);
        md.addColumn("ID");
        md.addColumn("NOMBRE");
        md.addColumn("HORA");
        md.addColumn("FECHA");
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 150, 560, 200);
        contentPane.add(scrollPane);
        leerContra();
        llenarteachers();
    }
    
    public void leerContra() {
        LeerContraBD LC = new LeerContraBD();
        usuario = LC.usuario;
        contra = LC.contra;
    }

    public void llenarteachers() {
        Conexion con = new Conexion();
        boolean error = con.conectarMySQL("libsm", usuario, contra, "localhost");

        if (!error) {

            ResultSet rs = con.consulta("SELECT * FROM teachers");
            int cant = con.getSizeQuery(rs);
            if (cant > 0) {
                try {
                    teachers.removeAllItems();
                    while (rs.next()) {
                        teachers.addItem(rs.getString(1) + " - " + rs.getString(2));
                    }
                } catch (SQLException sql) {
                    JOptionPane.showMessageDialog(null,
                            "Error al tratar de obtener la informacion" + sql);
                }
            } else {
                teachers.removeAllItems();
                teachers.addItem("No hay teachers");
            }
            con.desconectar();
        }
    }

    public void filtrar() {
        if (jrteach.isSelected() && jrent.isSelected()) {
            entradateachers();
        }
        if (jrteach.isSelected() && jrsal.isSelected()) {
            salidateachers();
        }
        if (jrfecha.isSelected() && jrent.isSelected()) {
            entradafecha();
        }
        if (jrfecha.isSelected() && jrsal.isSelected()) {
            salidafecha();
        }
    }

    public void entradateachers() {
        /// CAMBIAR Acá CUANDO CAMBIE EL TAMAño del ID
        String selectT=teachers.getSelectedItem().toString().substring(0,11);
        llenartabla(null, selectT, "entradas");
    }

    public void salidateachers() {
        /// CAMBIAR Acá CUANDO CAMBIE EL TAMAño del ID
        String selectT=teachers.getSelectedItem().toString().substring(0,11);
        llenartabla(null, selectT, "salidas");
    }

    public void entradafecha() {
        Date fecha = calendario.getDate();
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        llenartabla(dateFormat.format(fecha).toString(), null, "entradas");
    }

    public void salidafecha() {
        Date fecha = calendario.getDate();
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        llenartabla(dateFormat.format(fecha).toString(), null, "salidas");
    }

    public void llenartabla(String f, String tea, String tabl) {
        
        Conexion con = new Conexion();
        boolean error = con.conectarMySQL("libsm", usuario, contra, "localhost");
        /// POR TEACHER
        if (tea != null) {
            if (!error) {
                ResultSet rsx = con.consulta("SELECT * FROM "+tabl+" WHERE id="+tea);
                
                /// CAMBIAR Acá CUANDO CAMBIE EL TAMAño del ID
                String nombre=teachers.getSelectedItem().toString().substring(12);
                int cant2 = con.getSizeQuery(rsx);
                if (cant2 > 0) {
                    try {
                        while (rsx.next()) {
                            
                            String row[] = {rsx.getString(1),nombre, rsx.getString(2), rsx.getString(3)};
                            md.addRow(row);
                        }
                    } catch (SQLException sql) {
                        JOptionPane.showMessageDialog(null,
                                "Error al tratar de obtener la informacion" + sql);
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "No hay teachers");
                }
            }
            con.desconectar();

        }

        // POR FECHA
        if (f != null) {
            if (!error) {
                ResultSet rs = con.consulta("SELECT * FROM " + tabl + " WHERE fecha='" + f + "'");
                int cant = con.getSizeQuery(rs);
                if (cant > 0) {
                    try {
                        while (rs.next()) {
                            ResultSet rs2 = con.consulta("SELECT name FROM teachers WHERE id=" + rs.getString(1));

                            int cant2 = con.getSizeQuery(rs2);
                            if (cant2 > 0) {
                                try {
                                    while (rs2.next()) {
                                        String row[] = {rs.getString(1), rs2.getString(1), rs.getString(2), rs.getString(3)};
                                        md.addRow(row);
                                    }
                                } catch (SQLException sql) {
                                    JOptionPane.showMessageDialog(null,
                                            "Error al tratar de obtener la informacion" + sql);
                                }
                            } else {
                                JOptionPane.showMessageDialog(rootPane, "No hay teachers");
                            }
                        }
                    } catch (SQLException sql) {
                        JOptionPane.showMessageDialog(null,
                                "Error al tratar de obtener la informacion" + sql);
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "No hay teachers");
                }
            }
            con.desconectar();
        }

    }
}
