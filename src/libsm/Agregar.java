package libsm;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author Santiago Romero
 */
public class Agregar extends JDialog {

    private JPanel contentPane;
    private JTextField codigo;
    private JTextField nombre;
    private String usuario,contra;
    public Agregar() {
        setBounds(100, 100, 450, 300);
        setIconImage(new ImageIcon(getClass().getResource("/img/ESCUDO.png")).getImage());
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton agregar = new JButton("Agregar");
        agregar.setBounds(168, 213, 89, 23);
        agregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                agregar();
            }
        });
        contentPane.add(agregar);

        JLabel lblCdigo = new JLabel("C\u00F3digo:");
        lblCdigo.setBounds(29, 80, 100, 25);
        lblCdigo.setFont(new Font("Arial", Font.PLAIN, 16));
        contentPane.add(lblCdigo);

        codigo = new JTextField();
        codigo.setBounds(29, 120, 159, 25);
        codigo.setDocument(new LimitC(codigo, 10, 0));
        codigo.setFont(new Font("Arial", Font.PLAIN, 16));
        contentPane.add(codigo);
        codigo.setColumns(10);
        codigo.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e){}
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    nombre.requestFocus(true);
                }
            }
            public void keyReleased(KeyEvent e) {}
        });

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(235, 80, 100, 25);
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        contentPane.add(lblNombre);
        

        nombre = new JTextField();
        nombre.setBounds(235, 120, 189, 25);
        nombre.setDocument(new LimitC(nombre, 30, 1));
        nombre.setFont(new Font("Arial", Font.PLAIN, 16));
        contentPane.add(nombre);
        nombre.setColumns(10);
        nombre.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e){}
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    agregar();
                }
            }
            public void keyReleased(KeyEvent e) {}
        });
        leerContra();
        

    }
    
    public void leerContra() {
        LeerContraBD LC = new LeerContraBD();
        usuario = LC.usuario;
        contra = LC.contra;
    }
    
    public void agregar() {
        /// CAMBIAR CUANDO CAMBIE EL TAMAÑO DE EL CODIGO
        if(codigo.getText().length()==10 && nombre.getText().length()>0){
        boolean tea=hayteacher();
        if(!tea){
        Conexion con = new Conexion();
        con.conectarMySQL("libsm", usuario, contra, "localhost");
        String query = "INSERT INTO teachers VALUES (" + Integer.parseInt(codigo.getText()) + ",'" + nombre.getText() + "')";
        con.actualizar(query);
        
        Conexion con2 = new Conexion();
        con2.conectarMySQL("libsm", usuario, contra, "localhost");
        String query2 = "INSERT INTO estado VALUES (" + Integer.parseInt(codigo.getText()) + ",'0')";
        con2.actualizar(query2);
        
        con.desconectar();
        con2.desconectar();
        JOptionPane.showMessageDialog(rootPane, "Teacher agregada: "+nombre.getText());
        codigo.setText("");
        nombre.setText("");
        }
        else{
            JOptionPane.showMessageDialog(rootPane, "Ya existe esa teacher","Error",0);
        }
        }
        else{
            JOptionPane.showMessageDialog(rootPane, "Ingrese correctamente los campos","Error",2);
        }
        
    }
    
    public boolean hayteacher(){
        Conexion con = new Conexion();
        boolean error = con.conectarMySQL("libsm", usuario, contra, "localhost");
        ResultSet rs = con.consulta("SELECT * FROM teachers WHERE id='" + Integer.parseInt(codigo.getText()) + "'");
        int cant = con.getSizeQuery(rs);
        if (cant>0) {
            return true;
        }
        else{
            return false;
        }
    }
}
